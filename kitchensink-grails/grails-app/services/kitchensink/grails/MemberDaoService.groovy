package kitchensink.grails

import kitchensink.grails.domain.Member

class MemberDaoService {

    static transactional = true

    def listMembers(){
        Member.list()
    }

    def getById(int id){
        Member.findById(id)
    }

    def getByEmail(String email){
        Member.findByEmail(email)
    }

    def findByName(String name){
        Member.findByName(name)
    }

    /* Create Member from params */
    def createMember(String name, String email, String phoneNumber){
        def newMember = new Member(name: name, email: email, phone_number: phoneNumber)
        newMember.save()
        newMember
    }
}
