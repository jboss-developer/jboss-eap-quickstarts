#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.ejb;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

public class HelloServiceTest {

	private HelloService helloService;
	
	@Before
	public void setUp() {
		HelloServiceImpl service = new HelloServiceImpl();
		service.log = LoggerFactory.getLogger(getClass());
		helloService = service;
	}
	
	@Test
	public void testGreet() {
		assertEquals("Hello Mike", helloService.greet("Mike"));
		assertEquals("Hello World", helloService.greet(""));
		assertEquals("Hello World", helloService.greet(null));
	}

	@After
	public void tearDown() {
		helloService = null;
	}
	
}
