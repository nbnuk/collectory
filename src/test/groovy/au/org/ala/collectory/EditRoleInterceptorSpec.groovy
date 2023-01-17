package au.org.ala.collectory

import grails.testing.web.interceptor.InterceptorUnitTest
import spock.lang.Specification

class EditRoleInterceptorSpec extends Specification implements InterceptorUnitTest<EditRoleInterceptor> {

    def setup() {
    }

    def cleanup() {

    }

    void "Test role interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(controller:"role")

        then:"The interceptor does match"
            interceptor.doesMatch()
    }
}
