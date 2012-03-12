package org.jboss.as.quickstarts.loggingToolsQS.exceptions;

import org.jboss.logging.Message;
import org.jboss.logging.Messages;

@org.jboss.logging.MessageBundle(projectCode = "GREETER")
public interface GreeterExceptionBundle 
{
	GreeterExceptionBundle EXCEPTIONS = Messages.getBundle(GreeterExceptionBundle.class);

	@Message(id=5, value = "Requested locale not valid: %s")
	LocaleInvalidException localeNotValid(String locale);	
	
}
