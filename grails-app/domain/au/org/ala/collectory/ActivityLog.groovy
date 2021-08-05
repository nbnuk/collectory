package au.org.ala.collectory

/**
 * Records user activity such as login and editing.
 */
class ActivityLog implements Serializable {

    Date timestamp                              // time of the event
    String user                                 // username
    String entityUid                            // id of the affected record if any
    boolean contactForEntity = false            // are they a contact for the record
    boolean administratorForEntity = false      // are they an administrator of the record
    String action                               // what did they do
    boolean admin = false                       // true if the user is sys admin (used to differentiate 'real' users in stats)

    static transients = ['Actions']
    
    static constraints = {
        timestamp()
        user(blank:false)
        entityUid(nullable:true)
        contactForEntity()
        administratorForEntity()
        action(blank:false)
    }

    String toString() {
        def adm = admin ? " (admin)" : ""
        if (entityUid) {
            "${timestamp}: ${user}${adm} ${action} ${entityUid}"
        } else {
            "${timestamp}: ${user}${adm} ${action}"
        }
    }
}
