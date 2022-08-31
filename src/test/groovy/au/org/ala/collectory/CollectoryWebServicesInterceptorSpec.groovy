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
            withRequest(controller: 'data', action: "saveEntity")
            withRequest(controller:'data', action:"syncGBIF")
            withRequest('controller':'data', action: 'updateContact')
            withRequest('controller':'data', action: 'updateContactFor')
            withRequest('controller':'data', action: 'contacts')
            withRequest(controller:'gbif', action:"scan")
            withRequest(controller:'ipt', action:"scan")

        then:"The interceptor does match"
            interceptor.doesMatch()
    }


    void "Test collectoryWebServices interceptor not matching"() {
        when:"A request matches the interceptor"
            withRequest(controller: 'data', action: "getEntity")
            withRequest(controller: 'data', action: "mapToCsv")
            withRequest(controller: 'randomController', action: "randomAction")

        then:"The interceptor does not match"
        !interceptor.doesMatch()
    }
}
