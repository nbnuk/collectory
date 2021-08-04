package au.org.ala.custom.marshalling;

import grails.converters.JSON;
import grails.core.GrailsApplication;
import grails.core.support.proxy.ProxyHandler;
import org.grails.datastore.mapping.model.PersistentEntity;
import org.grails.datastore.mapping.model.PersistentProperty;
import org.grails.web.converters.exceptions.ConverterException;
import org.grails.web.converters.marshaller.json.DomainClassMarshaller;
import org.grails.web.json.JSONWriter;

/**
 * Overrides the way associations are marshalled as JSON to include UID for ProviderGroup entities.
 *
 * Created by markew
 * Date: Sep 13, 2010
 * Time: 9:52:48 AM
 */
public class DomainClassWithUidMarshaller extends DomainClassMarshaller {

    public DomainClassWithUidMarshaller(boolean includeVersion, GrailsApplication application) {
        super(includeVersion, application);
    }

    public DomainClassWithUidMarshaller(boolean includeVersion, ProxyHandler proxyHandler, GrailsApplication application) {
        super(includeVersion, proxyHandler, application);
    }

    @Override
    protected void asShortObject(Object refObj, JSON json, PersistentProperty idProperty, PersistentEntity referencedDomainClass) throws ConverterException {
        if (referencedDomainClass.getName().equals("Institution") ||
            referencedDomainClass.getName().equals("DataProvider")) {
            JSONWriter writer = json.getWriter();
            writer.object();
            writer.key("class").value(referencedDomainClass.getName());
            writer.key("uid").value(extractValue(refObj, referencedDomainClass.getPropertyByName("uid")));
            writer.endObject();
        } else {
            super.asShortObject(refObj, json, idProperty, referencedDomainClass);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }

}
