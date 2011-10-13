#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.ejb;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

/**
 * Session Bean implementation class HelloServiceImpl
 */
@Stateless(mappedName = "helloService")
public class HelloServiceImpl implements HelloService {

	@Inject
	Logger log;

	private static String DEFAULT_NAME = "World"; 
	
	public String greet(String name) {
		String finalName = StringUtils.defaultIfEmpty(name, DEFAULT_NAME);
		String greeting =  "Hello "+finalName;
		log.debug(greeting);
		return greeting;
	}
}
