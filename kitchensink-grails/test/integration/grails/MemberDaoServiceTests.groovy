package grails

import grails.test.mixin.TestFor
import kitchensink.grails.MemberDaoService
import org.junit.Test

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
class MemberDaoServiceTests extends GroovyTestCase {

    MemberDaoService memberDaoService

    @Test
    void testFindById() {
        def member = memberDaoService.getById(1)
        assert member.name == 'John Smith'
        assert member.email == 'jonh.smith@mailinator.com'
        assert member.phone_number == '2125551212'
    }

    @Test
    void testFindByName() {
        def member = memberDaoService.findByName('John Smith')
        assert member.id == 1
        assert member.email == 'jonh.smith@mailinator.com'
        assert member.phone_number == '2125551212'
    }

    @Test
    void testFindByEmail() {
        def member = memberDaoService.getByEmail('jonh.smith@mailinator.com')
        assert member.name == 'John Smith'
        assert member.id == 1
        assert member.phone_number == '2125551212'
    }

    @Test
    void testRegister() {
        def member = memberDaoService.createMember('Jane Smith', 'jane.smith@anon.com', '1232322344')
        assert member.name == 'Jane Smith'
        assert member.id == 2
        assert member.phone_number == '1232322344'
        assert member.email == 'jane.smith@anon.com'
    }

}
