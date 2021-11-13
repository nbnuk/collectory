package spring

import au.ala.org.ws.security.AlaOAuth2UserService
import au.ala.org.ws.security.AlaWebServiceAuthFilter
import collectory.SecurityConfig

// Place your Spring DSL code here
beans = {
    alaOAuth2UserService(AlaOAuth2UserService)
    alaWebServiceAuthFilter(AlaWebServiceAuthFilter)
    securityConfig(SecurityConfig)
}
