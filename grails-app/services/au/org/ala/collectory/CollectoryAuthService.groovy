package au.org.ala.collectory

import au.org.ala.ws.service.WebService
import grails.web.servlet.mvc.GrailsParameterMap
import org.pac4j.core.config.Config
import org.pac4j.core.context.WebContext
import org.pac4j.core.profile.ProfileManager
import org.pac4j.core.profile.UserProfile
import org.pac4j.core.util.FindBest
import org.pac4j.http.client.direct.DirectBearerAuthClient
import org.pac4j.jee.context.JEEContextFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.context.request.RequestContextHolder

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CollectoryAuthService{
    static transactional = false
    def apiKeyService
    def grailsApplication
    def authService
    def providerGroupService
    @Autowired(required = false)
    Config config
    @Autowired(required = false)
    DirectBearerAuthClient directBearerAuthClient

    static final API_KEY_COOKIE = "ALA-API-Key"

    def username() {
        def username = 'not available'
        if(RequestContextHolder.currentRequestAttributes()?.getUserPrincipal()?.name)
            username = RequestContextHolder.currentRequestAttributes()?.getUserPrincipal()?.name
        else {
            if(authService)
                username = authService.getUserName()
        }

        return (username) ? username : 'not available'
    }

    def isAdmin() {
        return !grailsApplication.config.security.oidc.enabled.toBoolean() || authService.userInRole(grailsApplication.config.ROLE_ADMIN as String)
    }

    protected boolean userInRole(role) {
        def roleFlag = false
        if(!grailsApplication.config.security.oidc.enabled.toBoolean())
            roleFlag = true
        else {
            if (authService) {
                roleFlag = authService.userInRole(role)
            }
        }

        return roleFlag || isAdmin()
    }

    /**
     * Returns a list of entities that the specified user is authorised to edit.
     *
     * Note that more than one contact may correspond to the user's email address. In this
     * case, the result is a union of the lists for each contact.
     *
     * @param email
     * @return a map holding entities, a list of their uids and the latest modified date
     */
    def authorisedForUser(String email) {
        def contacts = Contact.findAllByEmail(email)
        switch (contacts.size()) {
            case 0: return [sorted: [], keys: [], latestMod: null]
            case 1: return authorisedForUser(contacts[0])
            default:
                def result = [sorted: [], keys: [], latestMod: null]
                contacts.each {
                    def oneResult = authorisedForUser(it)
                    result.sorted += oneResult.sorted
                    result.keys += oneResult.keys
                    if (oneResult.latestMod > result.latestMod) { result.latestMod = oneResult.latestMod }
                }
                return result
        }
    }

    /**
     * Returns a list of entities that the specified contact is authorised to edit.
     *
     * @param contact
     * @return a map holding entities, a list of their uids and the latest modified date
     */
    def authorisedForUser(Contact contact) {
        // get list of contact relationships
        def latestMod = null
        def entities = [:]  // map by uid to remove duplicates
        ContactFor.findAllByContact(contact).each {
            if (it.administrator) {
                def pg = providerGroupService._get(it.entityUid)
                if (pg) {
                    entities.put it.entityUid, [uid: pg.uid, name: pg.name]
                    if (it.dateLastModified > latestMod) { latestMod = it.dateLastModified }
                }
                // add children
                pg.children().each { child ->
                    // children() now seems to return some internal class resources
                    // so make sure they are PGs
                    if (child instanceof ProviderGroup) {
                        def ch = providerGroupService._get(child.uid)
                        if (ch) {
                            entities.put ch.uid, [uid: ch.uid, name: ch.name]
                        }
                    }
                }
            }
        }
        return [sorted: entities.values().sort { it.name }, keys:entities.keySet().sort(), latestMod: latestMod]
    }

    /**
     * Get the provided api key from all possible options i.e. params, cookie, and header
     * @param params
     * @param request
     * @return apiKey String
     */
    private static String getApiKey(params, HttpServletRequest request) {
        def apiKey = {
            // handle api keys if present in params
            if (request.JSON && request.JSON.api_key) {
                request.JSON.api_key
            } else if (request.JSON && request.JSON.apiKey) {
                request.JSON.apiKey
            } else if (request.JSON && request.JSON.Authorization) {
                request.JSON.Authorization
            } else if (params.api_key) {
                params.api_key
            }else if (params.apiKey) {
                params.apiKey
                // handle api keys if present in cookie
            } else  if (request.cookies.find { cookie -> cookie.name == API_KEY_COOKIE }){
                def cookieApiKey = request.cookies.find { cookie -> cookie.name == API_KEY_COOKIE }
                cookieApiKey.value
            } else {
                // handle api key in  header. check for default api key header and the check for Authorization
                def headerValue = request.getHeader(WebService.DEFAULT_API_KEY_HEADER) ?: request.getHeader("Authorization")
                headerValue
            }
        }.call()
        apiKey
    }



    def checkJWT(HttpServletRequest request, HttpServletResponse response, String role, String scope) {
        def result = false
            def context = context(request, response)
            ProfileManager profileManager = new ProfileManager(context, config.sessionStore)
            profileManager.setConfig(config)

            def credentials = directBearerAuthClient.getCredentials(context, config.sessionStore)
            if (credentials.isPresent()) {
                def profile = directBearerAuthClient.getUserProfile(credentials.get(), context, config.sessionStore)
                if (profile.isPresent()) {
                    def userProfile = profile.get()
                    profileManager.save(
                            directBearerAuthClient.getSaveProfileInSession(context, userProfile),
                            userProfile,
                            directBearerAuthClient.isMultiProfile(context, userProfile)
                    )

                    result = true
                    if (role) {
                        result = userProfile.roles.contains(role)
                    }

                    if (result && scope) {
                        result = userProfile.permissions.contains(scope) || profileHasScope(userProfile, scope)
                    }
                }
            }
        return result
    }

    private static boolean profileHasScope(UserProfile userProfile, String scope) {
        def scopes = userProfile.attributes['scope']
        def result = false
        if (scopes != null) {
            if (scopes instanceof String) {
                result = scopes.tokenize(',').contains(scope)
            } else if (scopes.class.isArray()) {
                result =scopes.any { it?.toString() == scope }
            } else if (scopes instanceof Collection) {
                result =scopes.any { it?.toString() == scope }
            }
        }
        return result
    }

    private WebContext context(HttpServletRequest request, HttpServletResponse response) {
        final WebContext context = FindBest.webContextFactory(null, config, JEEContextFactory.INSTANCE).newContext(request, response)
        return context
    }

    def isAuthorisedWsRequest(GrailsParameterMap params, HttpServletRequest request, HttpServletResponse response){
        Boolean authorised
        // check for JWT first
        authorised = checkJWT(request, response, null, 'users/read')
        // if still unauthorised, check for and attempt  to validate API key
        if(!authorised && grailsApplication.config.security.apikey.checkEnabled.toBoolean()){
            def apiKey = getApiKey(params, request)
            def apiKeyResponse = apiKeyService.checkApiKey(apiKey)
            authorised = apiKeyResponse.valid
        }

        return authorised
    }

}
