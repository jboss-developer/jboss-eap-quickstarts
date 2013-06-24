package kitchensink.grails

import kitchensink.grails.domain.Member

/* Member Controller*/
class MemberController {

    def MemberDaoService memberDaoService

    /* For each of the methods, we defined a list allowed methods*/
    static allowedMethods = [index: ['GET', 'POST'],
            member: 'POST']

    def index() {
        [newMember: new Member(), members: memberDaoService.listMembers()]
    }

    def member() {
        def newMember = memberDaoService.createMember(params.name as String, params.email as String,
                params.phoneNumber as String)
        /* Check if there is an error in the created member */
        if (!newMember.hasErrors()) {
            /* If all is well redirect to index */
            redirect view: 'index'
        } else {
            /* Else rerender with errors */
            render view: 'index', model: [newMember: newMember, members: memberDaoService.listMembers()]
        }
    }
}
