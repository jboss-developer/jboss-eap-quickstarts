#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.util;

import java.util.ResourceBundle;

/**
 * Class used to read the project version from a maven filtered resource bundle. 
 */
public class Version {
	
	private static ResourceBundle BUNDLE = ResourceBundle.getBundle("${parentArtifactId}-util");
	
	public static String VALUE = BUNDLE.getString("version"); 
}
