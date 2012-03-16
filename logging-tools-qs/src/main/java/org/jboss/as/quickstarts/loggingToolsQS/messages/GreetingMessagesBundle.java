package org.jboss.as.quickstarts.loggingToolsQS.messages;

import org.jboss.logging.Message;
import org.jboss.logging.MessageBundle;
import org.jboss.logging.Messages;


@MessageBundle(projectCode="GREETER")
public interface GreetingMessagesBundle 
{
	GreetingMessagesBundle MESSAGES = Messages.getBundle(GreetingMessagesBundle.class);
	
	@Message(value = "Hello %s.")
	String helloToYou(String name);
	
}
