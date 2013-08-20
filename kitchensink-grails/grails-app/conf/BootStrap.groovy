import groovy.sql.Sql
import kitchensink.grails.domain.Member

class BootStrap {

    def dataSource

    def init = { servletContext ->
        /* Initialization of member data*/
        def member = new Member(id: 0, name: 'John Smith', email: 'jonh.smith@mailinator.com', phone_number: '2125551212')
        member.save()
    }
    def destroy = {
    }
}
