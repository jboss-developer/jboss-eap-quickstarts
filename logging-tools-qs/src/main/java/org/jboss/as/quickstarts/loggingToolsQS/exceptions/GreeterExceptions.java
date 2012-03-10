package org.jboss.as.quickstarts.loggingToolsQS.exceptions;

import javax.ws.rs.WebApplicationException;

import org.jboss.logging.Message;
import org.jboss.logging.Messages;

@org.jboss.logging.MessageBundle(projectCode = "GREETER")

public interface GreeterExceptions 
{
	GreeterExceptions EXCEPTIONS = Messages.getBundle(GreeterExceptions.class);

	@Message(value = "Requested locale not valid: %s")
	WebApplicationException localeNotValid(String locale);
	
	
	
}
