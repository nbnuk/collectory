package au.org.ala.collectory

import grails.async.DelegateAsync

class AsyncGbifRegistryService {
    @DelegateAsync GbifRegistryService gbifRegistryService
}
