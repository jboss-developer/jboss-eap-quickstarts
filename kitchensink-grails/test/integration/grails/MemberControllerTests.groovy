package grails

import grails.test.mixin.TestFor
import kitchensink.grails.MemberController

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
class MemberControllerTests extends  GroovyTestCase {
    def controller = new MemberController()

    def testIndex(){
        def result = controller.index()

        assert (result.members as List).size() == 1
    }
}
