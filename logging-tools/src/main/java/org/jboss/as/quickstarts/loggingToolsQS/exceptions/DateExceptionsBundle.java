package org.jboss.as.quickstarts.loggingToolsQS.exceptions;

import java.text.ParseException;

import org.jboss.logging.Message;
import org.jboss.logging.Messages;
import org.jboss.logging.Param;

@org.jboss.logging.MessageBundle(projectCode = "GRTDATES")
public interface DateExceptionsBundle {
    DateExceptionsBundle EXCEPTIONS = Messages.getBundle(DateExceptionsBundle.class);

    @Message(id = 7, value = "The date you sent me isn't valid, '%s'.  Sorry.")
    ParseException targetDateStringDidntParse(String dateString, @Param int errorOffset);

}
