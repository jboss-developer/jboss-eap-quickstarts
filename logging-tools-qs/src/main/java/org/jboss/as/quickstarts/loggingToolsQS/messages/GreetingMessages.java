package org.jboss.as.quickstarts.loggingToolsQS.messages;

import org.jboss.logging.Message;
import org.jboss.logging.MessageBundle;
import org.jboss.logging.Messages;


@MessageBundle(projectCode="GREETER")
public interface GreetingMessages 
{
	GreetingMessages MESSAGES = Messages.getBundle(GreetingMessages.class);
	
	@Message(id=4, value = "Hello %s.")
	String helloToYou(String name);
	
}
