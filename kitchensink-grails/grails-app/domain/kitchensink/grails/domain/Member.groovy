package kitchensink.grails.domain
/**
 * Created with IntelliJ IDEA.
 * User: tmehta
 * Date: 21/06/13
 * Time: 3:51 PM
 * To change this template use File | Settings | File Templates.
 */
class Member {

    String name;

    String email;

    String phone_number;

    static constraints = {
        name (blank: false, matches: "[A-Za-z ]*", maxSize: 25)
        email  (blank: false, email: true, maxSize: 25, unique: true)
        phone_number (size: 10..12, matches: /\d+/)
    }

    /* Map table name to MemberGrails */
    static mapping = {
        table 'MemberGrails'
    }
}
