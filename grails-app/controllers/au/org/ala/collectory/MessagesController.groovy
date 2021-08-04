package au.org.ala.collectory

class MessagesController {

    def messageSource

    static defaultAction = "i18n"

    /**
     * Export raw i18n message properties as TEXT for use by JavaScript i18n library
     *
     * @param id - messageSource file name
     * @return
     */
    def i18n(String id) {
        def locale = org.springframework.web.servlet.support.RequestContextUtils.getLocale(request) ?: request.locale
        def keys = messageSource.withTraits(MessagePropertiesTrait).getMessageKeys(locale)
        response.setHeader("Content-type", "text/plain; charset=UTF-8")
        def messages = keys.collect { "${it.key}=${it.value}" }
        render ( text: messages.sort().join("\n") )
    }
}
