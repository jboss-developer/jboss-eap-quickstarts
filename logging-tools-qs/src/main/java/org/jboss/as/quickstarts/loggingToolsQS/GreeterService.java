package org.jboss.as.quickstarts.loggingToolsQS;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jboss.as.quickstarts.loggingToolsQS.loggers.GreeterLogger;

/**
 * A simple REST service which says hello in different languages
 * 
 * @author dmison@me.com
 * 
 */

@Path("greetings")
public class GreeterService 
{
	
	@Inject
	Greeter greeter;

	// ======================================================================
	// Hello "name"!
	@GET
	@Path("{name}")
	public String getHelloName(@PathParam("name") String name) 
	{
		GreeterLogger.LOGGER.logHelloMessageSent();
		return greeter.sayHello(name);
	}

	@GET
	@Path("{name}/json")
	@Produces({ "application/json" })
	public String getHelloNameJSON(@PathParam("name") String name) 
	{
		GreeterLogger.LOGGER.logHelloMessageSentInFormat("json");
		return "{\"result\":\"" + greeter.sayHello(name) + "\"}";
	}

	@GET
	@Path("{name}/xml")
	@Produces({ "application/xml" })
	public String getHelloNameXML(@PathParam("name") String name) 
	{
		GreeterLogger.LOGGER.logHelloMessageSentInFormat("xml");
		return "<xml><result>" + greeter.sayHello(name)+ "</result></xml>";
	}
	
	
}
