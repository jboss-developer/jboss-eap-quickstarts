package org.jboss.as.quickstarts.loggingToolsQS.loggers;

import java.text.ParseException;

import org.jboss.logging.BasicLogger;
import org.jboss.logging.Cause;
import org.jboss.logging.LogMessage;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;
import org.jboss.logging.Message;

@org.jboss.logging.MessageLogger(projectCode = "GTRDATES")
public interface DateLogger extends BasicLogger {
    DateLogger LOGGER = Logger.getMessageLogger(DateLogger.class, DateLogger.class.getPackage().getName());

    @LogMessage(level = Level.ERROR)
    @Message(id = 3, value = "Invalid date passed as string: %s")
    void logStringCouldntParseAsDate(String datestring, @Cause ParseException exception);

    @LogMessage
    @Message(id = 4, value = "Requested number of days until '%s'")
    void logDaysUntilRequest(String dateString);

}