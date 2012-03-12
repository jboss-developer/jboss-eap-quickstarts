package org.jboss.as.quickstarts.loggingToolsQS.exceptions;



import org.jboss.logging.Message;
import org.jboss.logging.Messages;

@org.jboss.logging.MessageBundle(projectCode = "GREETER")

public interface GreeterExceptions 
{
	GreeterExceptions EXCEPTIONS = Messages.getBundle(GreeterExceptions.class);

	@Message(id=5, value = "Requested locale not valid: %s")
	Exception localeNotValid(String locale);
	
	
}
