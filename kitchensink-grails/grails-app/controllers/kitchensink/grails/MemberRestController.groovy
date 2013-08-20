package kitchensink.grails

class MemberRestController {

    MemberDaoService memberDaoService

    def getMember() {
        /* Return content type json with member */
        render(contentType: "text/json") { memberDaoService.getById(params.id as int) }
    }

    def getMembers() {
        /* Return content type json with members */
        render(contentType: "text/json") { memberDaoService.listMembers() }
    }
}
