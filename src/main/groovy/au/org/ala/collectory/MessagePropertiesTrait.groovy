package au.org.ala.collectory
import groovy.transform.CompileDynamic

@CompileDynamic
trait MessagePropertiesTrait {

    Properties getMessageKeys(Locale locale) {
        this.getMergedProperties(locale).properties
    }
}