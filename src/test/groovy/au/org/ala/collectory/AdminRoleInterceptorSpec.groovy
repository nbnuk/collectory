package au.org.ala.collectory

import grails.testing.web.interceptor.InterceptorUnitTest
import spock.lang.Specification

class AdminRoleInterceptorSpec extends Specification implements InterceptorUnitTest<AdminRoleInterceptor> {

    def setup() {
    }

    def cleanup() {

    }

    void "Test AdminRole interceptor matching"() {
        // admin role interceptor should also match everything from editrole interceptor.
        when:"A request matches the interceptor"
        withRequest(controller: 'admin', action: 'reloadConfig')


        then:"The interceptor does match"
        interceptor.doesMatch()
    }


    void "Test AdminRole interceptor not matching"() {
        when:"A request matches the interceptor"
        withRequest(controller: 'admins', action: 'test')
        then:"The interceptor does not match"
        !interceptor.doesMatch()
    }
}
