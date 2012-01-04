package org.jboss.as.quickstarts.webservice;

import javax.ejb.Stateless;
import javax.jws.WebService;

@Stateless
@WebService(
	endpointInterface = "org.jboss.as.quickstarts.webservice.Calculator",
	targetNamespace = "org.jboss.as.quickstarts.webservice", 
	serviceName = "CalculatorService"
)
/**
 * 
 * @author Serge Pagop (spagop@redhat.com)
 *
 */
public class CalculatorBean implements Calculator {
	public int add(int x, int y) {
		return x + y;
	}

	public int subtract(int x, int y) {
		return x - y;
	}
}
