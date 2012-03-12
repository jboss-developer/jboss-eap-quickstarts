package org.jboss.as.quickstarts.loggingToolsQS;

import java.util.Locale;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.jboss.as.quickstarts.loggingToolsQS.exceptions.GreeterExceptions;
import org.jboss.as.quickstarts.loggingToolsQS.loggers.GreeterLogger;
import org.jboss.as.quickstarts.loggingToolsQS.messages.GreetingMessages;
import org.jboss.logging.Messages;

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

	// ======================================================================
	// Hello "name" in language
	@GET
	@Path("{locale}/{name}")
	public String getHelloNameForLocale( @PathParam("name") String name, 
										 @PathParam("locale") String locale	)
	{
		String[] locale_parts = locale.split("-");
		Locale newLocale = null;
		
		try {

			switch (locale_parts.length)
			{
				case 1:	newLocale = new Locale(locale_parts[0]);
						break;
				case 2:	newLocale = new Locale(locale_parts[0], locale_parts[1]);
						break;
				case 3:	newLocale = new Locale(locale_parts[0], locale_parts[1], locale_parts[2]);
						break;
				default: throw GreeterExceptions.EXCEPTIONS.localeNotValid(locale);
			}
		}
		catch (Exception e)
		{
			ResponseBuilder builder = Response.status(404);
			builder.entity("<h2>404: resource not found</h2><p>"+e.getMessage()+"</p>");
			throw new WebApplicationException(builder.build());
		}

		GreetingMessages messages = Messages.getBundle(GreetingMessages.class, newLocale);
		return messages.helloToYou(name);
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
