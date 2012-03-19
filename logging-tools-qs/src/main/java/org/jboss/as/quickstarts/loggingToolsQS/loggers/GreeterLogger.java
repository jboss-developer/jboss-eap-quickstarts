package org.jboss.as.quickstarts.loggingToolsQS.loggers;

import org.jboss.logging.BasicLogger;
import org.jboss.logging.LogMessage;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;
import org.jboss.logging.Message;

@org.jboss.logging.MessageLogger(projectCode = "GREETER")
public interface GreeterLogger extends BasicLogger 
{
	GreeterLogger LOGGER = Logger.getMessageLogger(
			GreeterLogger.class,
			GreeterLogger.class.getPackage().getName() );


	@LogMessage
	@Message(id=1, value = "Hello message sent.")
	void logHelloMessageSent();

	@LogMessage
	@Message(id=2, value = "Hello message sent for locale: %s.")
	void logHelloMessageSentForLocale(String locale);


}
