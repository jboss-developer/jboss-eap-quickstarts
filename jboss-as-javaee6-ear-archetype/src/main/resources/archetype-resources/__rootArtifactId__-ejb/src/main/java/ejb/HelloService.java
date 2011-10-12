#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.ejb;
import javax.ejb.Local;

/**
 * Hello Service interface.
 */
@Local
public interface HelloService {

	/**
	 * Return a greeting
	 * @param name
	 * @return a famous greeting
	 */
	String greet(String name);

}
