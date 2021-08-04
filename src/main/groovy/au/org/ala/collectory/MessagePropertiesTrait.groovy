package au.org.ala.collectory

import grails.compiler.GrailsCompileStatic
import groovy.transform.TypeCheckingMode

@GrailsCompileStatic(TypeCheckingMode.SKIP)
trait MessagePropertiesTrait {

    Properties getMessageKeys(Locale locale) {
        this.getMergedProperties(locale).properties
    }

    Properties getPluginMessageKeys(Locale locale) {
        this.getMergedPluginProperties(locale).properties
    }
}