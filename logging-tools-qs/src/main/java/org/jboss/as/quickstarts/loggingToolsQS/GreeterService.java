package org.jboss.as.quickstarts.loggingToolsQS;

import java.util.Locale;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jboss.as.quickstarts.loggingToolsQS.exceptions.GreeterExceptionBundle;
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
	// ======================================================================
	// Hello "name"!
	@GET
	@Path("{name}")
	public String getHelloName(@PathParam("name") String name) 
	{
		GreeterLogger.LOGGER.logHelloMessageSent();
		return "hello "+name+".";
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
		
			switch (locale_parts.length)
			{
				case 1:	newLocale = new Locale(locale_parts[0]);
						break;
				case 2:	newLocale = new Locale(locale_parts[0], locale_parts[1]);
						break;
				case 3:	newLocale = new Locale(locale_parts[0], locale_parts[1], locale_parts[2]);
						break;
				default: throw GreeterExceptionBundle.EXCEPTIONS.localeNotValid(locale);
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
		return "{\"result\":\"Hello" + name + "\"}";
	}

	@GET
	@Path("{name}/xml")
	@Produces({ "application/xml" })
	public String getHelloNameXML(@PathParam("name") String name) 
	{
		GreeterLogger.LOGGER.logHelloMessageSentInFormat("xml");
		return "<xml><result>Hello" + name + "</result></xml>";
	}
	
	
}
