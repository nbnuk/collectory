package collectory

import au.ala.org.ws.security.AlaOAuth2UserService
import io.micronaut.context.annotation.Value
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler

/**
 * WebSecurityConfigurerAdapter implementation that overrides the default Oauth2
 * configuration which makes all resource non-public by default.
 */
@Configuration
@EnableWebSecurity
@Order(1) // required to override the default Oauth2 spring configuration
class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AlaOAuth2UserService alaOAuth2UserService

    @org.springframework.beans.factory.annotation.Value('${spring.security.logoutUrl:"http://dev.ala.org.au:8080"}')
    String logoutUrl

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(
                        "/",
                        "/public/**",
                        "/css/**",
                        "/assets/**",
                        "/messages/**",
                        "/i18n/**",
                        "/static/**",
                        "/images/**",
                        "/js/**",
                        "/ws/**"
                ).permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                .successHandler(new SavedRequestAwareAuthenticationSuccessHandler())
                .userInfoEndpoint()
                .oidcUserService(alaOAuth2UserService)
                .and()
                .and()
                .logout()
                .logoutUrl(logoutUrl)
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID").permitAll()
                .and().csrf().disable()
    }
}
