package au.org.ala.collectory

import grails.testing.web.interceptor.InterceptorUnitTest
import spock.lang.Specification

class EditRoleInterceptorSpec extends Specification implements InterceptorUnitTest<EditRoleInterceptor> {

    def setup() {
    }

    def cleanup() {

    }

    void "Test EditRole interceptor matching"() {
        when:"A request matches the interceptor"
        withRequest(controller: 'collection', action: "list")
        withRequest('controller': 'collection')
        withRequest('controller': 'institution', action: 'list')
        withRequest('controller': 'contact')
        withRequest('controller': 'licence', action: 'create')
        withRequest('controller': 'licence', action: 'edit')
        withRequest('controller': 'licence', action: 'show')
        withRequest('controller': 'licence', action: 'save')
        withRequest('controller':'providerGroup')
        withRequest('controller':'providerMap')
        withRequest('controller':'providerCode')
        withRequest('controller':'dataProvider')
        withRequest('controller':'dataHub')
        withRequest('controller':'reports')

        then:"The interceptor does match"
        interceptor.doesMatch()
    }


    void "Test EditRole interceptor not matching"() {
        when:"A request matches the interceptor"
        withRequest(controller: 'data', action: "getEntity")
        withRequest(controller: 'data', action: "mapToCsv")
        withRequest(controller: 'admin')
        withRequest(controller: 'randomController', action: "randomAction")

        then:"The interceptor does not match"
        !interceptor.doesMatch()
    }
}
