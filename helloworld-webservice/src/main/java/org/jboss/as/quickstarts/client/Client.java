package org.jboss.as.quickstarts.client;

import java.net.URL;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.jboss.as.quickstarts.webservice.Calculator;

/**
 * 
 * @author Serge Pagop (spagop@redhat.com)
 * 
 */
public class Client {
	public static void main(String[] args) throws Exception {

		final Hashtable jndiProperties = new Hashtable();
		jndiProperties.put(Context.URL_PKG_PREFIXES,
				"org.jboss.ejb.client.naming");final Context context = new InitialContext(jndiProperties);

		final QName serviceName = new QName("org.jboss.as.quickstarts.webservice", "CalculatorService");
		final URL wsdlURL = new URL("http://localhost:8080/jboss-as-helloworld-webservice/CalculatorService/CalculatorBean?wsdl");

		final Service service = Service.create(wsdlURL, serviceName);
		final Calculator calculator = (Calculator) service
				.getPort(Calculator.class);

		System.out.println("1 + 1 = " + calculator.add(1, 1));
		System.out.println("1 - 1 = " + calculator.subtract(1, 1));
	}
}
