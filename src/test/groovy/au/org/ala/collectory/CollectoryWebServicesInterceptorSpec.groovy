package au.org.ala.collectory

import grails.testing.web.interceptor.InterceptorUnitTest
import spock.lang.Specification

class CollectoryWebServicesInterceptorSpec extends Specification implements InterceptorUnitTest<CollectoryWebServicesInterceptor> {

    def setup() {
    }

    def cleanup() {

    }

    void "Test collectoryWebServices interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(controller:"collectoryWebServices")

        then:"The interceptor does match"
            interceptor.doesMatch()
    }
}
