package au.org.ala.collectory

import grails.plugin.cache.Cacheable

/**
 * A service that provides a java.util.Map representation of the i18n
 * messages for a given locale (cached). The main use for this service is
 * to provide a faster lookup for many i18n calls in a taglib, due to performance
 * issues with the <g.message> tag (too slow).
 */
class MessageSourceCacheService {

    def messageSource

    @Cacheable('longTermCache')
    def getMessagesMap(Locale locale) {

        if (!locale) {
            locale = new Locale("en","us")
        }

        def keys = messageSource.withTraits(MessagePropertiesTrait).getMessageKeys(locale)
        def messagesMap = [:]
        keys.entrySet().each { messagesMap.put(it.key, it.value) }
        messagesMap
    }
}