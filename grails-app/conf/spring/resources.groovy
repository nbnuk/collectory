package spring

import au.org.ala.ws.security.AlaWebServiceAuthFilter
import au.org.ala.ws.security.AlaRoleMapper
import au.org.ala.ws.security.JwtService
import au.org.ala.ws.security.LegacyApiKeyService
import collectory.SecurityConfig
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.boot.web.servlet.filter.OrderedFilter
import org.springframework.web.client.RestTemplate

// Place your Spring DSL code here
beans = {
    restService(RestTemplate)
    jwtService(JwtService)
    legacyApiKeyService(LegacyApiKeyService)
    alaWebServiceAuthFilter(AlaWebServiceAuthFilter)
    alaRoleMapper(AlaRoleMapper)
    alaSecurityConfig(SecurityConfig)

    securityFilterChainRegistration(FilterRegistrationBean) {
        filter = ref("springSecurityFilterChain")
        order = OrderedFilter.REQUEST_WRAPPER_FILTER_MAX_ORDER + 25 // This needs to be before the GrailsWebRequestFilter which is +30
    }
}
