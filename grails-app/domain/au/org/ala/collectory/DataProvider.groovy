package au.org.ala.collectory

class DataProvider implements ProviderGroup, Serializable {

    static final String ENTITY_TYPE = 'DataProvider'
    static final String ENTITY_PREFIX = 'dp'

    static auditable = [ignore: ['version','dateCreated','lastUpdated','userLastModified']]

    static hasMany = [resources: DataResource, approvals: ApprovedAccess]

    String hiddenJSON // web service only (non-UI) JSON; used by fieldcapture to store project data

    String gbifCountryToAttribute      // the 3 digit iso code of the country to attribute in GBIF

    static mapping = {
        uid index:'uid_idx'
        pubShortDescription type: "text"
        pubDescription type: "text"
        techDescription type: "text"
        focus type: "text"
        taxonomyHints type: "text"
        notes type: "text"
        networkMembership type: "text"
        sort: 'name'
        hiddenJSON type: "text"
        pubShortDescription type: "text"
        pubDescription type: "text"
        techDescription type: "text"
        focus type: "text"
        taxonomyHints type: "text"
        notes type: "text"
        networkMembership type: "text"
        groupClassification type: "text"
    }

    static constraints = {
        guid(nullable:true, maxSize:256)
        uid(blank:false, maxSize:20)
        name(blank:false, maxSize:1024)
        acronym(nullable:true, maxSize:45)
        pubShortDescription(nullable:true, maxSize:100)
        pubDescription(nullable:true)
        techDescription(nullable:true)
        focus(nullable:true)
        address(nullable:true)
        latitude(nullable:true)
        longitude(nullable:true)
        altitude(nullable:true)
        state(nullable:true, maxSize:45)
        websiteUrl(nullable:true, maxSize:256)
        logoRef(nullable:true)
        imageRef(nullable:true)
        email(nullable:true, maxSize:256)
        phone(nullable:true, maxSize:200)
        isALAPartner()
        notes(nullable:true)
        networkMembership(nullable:true, maxSize:256)
        attributions(nullable:true, maxSize:256)
        taxonomyHints(nullable:true)
        keywords(nullable:true)
        gbifRegistryKey(nullable:true, maxSize:36)
        guid(nullable:true, maxSize:256)
        uid(blank:false, maxSize:20)
        name(blank:false, maxSize:1024)
        acronym(nullable:true, maxSize:45)
        pubShortDescription(nullable:true, maxSize:100)
        pubDescription(nullable:true)
        techDescription(nullable:true)
        focus(nullable:true)
        address(nullable:true)
        latitude(nullable:true)
        longitude(nullable:true)
        altitude(nullable:true)
        state(nullable:true, maxSize:45)
        websiteUrl(nullable:true, maxSize:256)
        logoRef(nullable:true)
        imageRef(nullable:true)
        email(nullable:true, maxSize:256)
        phone(nullable:true, maxSize:200)
        isALAPartner()
        notes(nullable:true)
        networkMembership(nullable:true, maxSize:256)
        attributions(nullable:true, maxSize:256)
        taxonomyHints(nullable:true)
        keywords(nullable:true)
        gbifRegistryKey(nullable:true, maxSize:36)
        hiddenJSON(nullable:true, blank: false)
        keywords(nullable:true)
        gbifCountryToAttribute(nullable:true, maxSize: 3)
        groupClassification(nullable:true, maxSize:256)
    }

    /**
     * Returns a summary of the data provider including:
     * - id
     * - name
     * - acronym
     * - lsid if available
     * - description
     * - provider codes for matching with biocache records
     *
     * @return CollectionSummary
     */
    DataProviderSummary buildSummary() {
        DataProviderSummary dps = init(new DataProviderSummary()) as DataProviderSummary
        // safety
        if (resources) {
            def list = []
            def unused = resources.toString()  // workaround for odd problem where resources don't seem
                                               // to exist unless they are touched directly - lazy loading??
            resources.each { res ->
                if (res.hasProperty('uid')) {
                    list << [res.uid, res.name]
                } else {
                    log.error("problem accessing resources for uid = " + uid)
                }
            }
            dps.resources = list
        }
        def consumers = listConsumers()
        consumers.each {
            if (it[0..1] == 'co') {
                def pg = Collection.findByUid(it)
                dps.relatedCollections << [uid: pg.uid, name: pg.name]
            } else {
                def pg = Institution.findByUid(it)
                dps.relatedInstitutions << [uid: pg.uid, name: pg.name]
            }
        }
        return dps
    }

    /**
     * Return the first related institution address if the provider does not have one.
     * @return
     */
    @Override def resolveAddress() {
       ProviderGroup.super.resolveAddress()
    }

    /**
     * Returns the best available primary contact.
     * @return
     */
    @Override
    ContactFor inheritPrimaryContact() {
        if (getPrimaryContact()) {
            return getPrimaryContact()
        }
        else {
            for (con in listConsumers()) {
                def related = Collection.findByUid(con) ?: Institution.findByUid(con)
                if (related.inheritPrimaryContact()) {
                    return related.inheritPrimaryContact()
                }
            }
            return null
        }
    }

    /**
     * Returns the best available primary contact that can be published.
     * @return
     */
    @Override
    ContactFor inheritPrimaryPublicContact() {
        if (getPrimaryPublicContact()) {
            return getPrimaryPublicContact()
        }
        else {
            for (con in listConsumers()) {
                def related = Collection.findByUid(con) ?: Institution.findByUid(con)
                if (related.inheritPrimaryPublicContact()) {
                    return related.inheritPrimaryPublicContact()
                }
            }
            return null
        }
    }

    @Override
    def children() {
        return resources
    }

    long dbId() {
        return id;
    }

    String entityType() {
        return ENTITY_TYPE;
    }
}
