package grails

import grails.test.mixin.TestFor
import kitchensink.grails.MemberRestController

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
class MemberRestControllerTests extends  GroovyTestCase{

    def controller = new MemberRestController()

    void testJsonMember() {
        controller.params.id = 1
        controller.getMember()

        assert 'John Smith' ==  controller.response.json.name
        assert 'jonh.smith@mailinator.com' == controller.response.json.email
        assert '2125551212' == controller.response.json.phone_number
    }
}
