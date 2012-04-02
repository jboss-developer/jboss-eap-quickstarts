package org.jboss.as.quickstarts.loggingToolsQS;

import java.util.Locale;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.jboss.as.quickstarts.loggingToolsQS.exceptions.GreeterExceptionBundle;
import org.jboss.as.quickstarts.loggingToolsQS.loggers.GreeterLogger;
import org.jboss.as.quickstarts.loggingToolsQS.messages.GreetingMessagesBundle;
import org.jboss.logging.Messages;

/**
 * A simple REST service which says hello in different languages and logs the activity using localised loggers created using
 * JBoss Logging Tools.
 *
 * @author dmison@me.com
 *
 */

@Path("greetings")
public class GreeterService {
    // ======================================================================
    // Hello "name"!
    @GET
    @Path("{name}")
    public String getHelloName(@PathParam("name") String name) {
        GreeterLogger.LOGGER.logHelloMessageSent();
        return GreetingMessagesBundle.MESSAGES.helloToYou(name);
    }

    // ======================================================================
    // Hello "name" in language
    @GET
    @Path("{locale}/{name}")
    public String getHelloNameForLocale(@PathParam("name") String name, @PathParam("locale") String locale) {
        String[] locale_parts = locale.split("-");
        Locale newLocale = null;

        switch (locale_parts.length) {
            case 1:
                newLocale = new Locale(locale_parts[0]);
                break;
            case 2:
                newLocale = new Locale(locale_parts[0], locale_parts[1]);
                break;
            case 3:
                newLocale = new Locale(locale_parts[0], locale_parts[1], locale_parts[2]);
                break;
            default:
                throw GreeterExceptionBundle.EXCEPTIONS.localeNotValid(locale);
        }

        GreetingMessagesBundle messages = Messages.getBundle(GreetingMessagesBundle.class, newLocale);
        GreeterLogger.LOGGER.logHelloMessageSentForLocale(locale);
        return messages.helloToYou(name);
    }

    @GET
    @Path("crashme")
    public String crashMe() throws Exception {
        int value = 0;

        try {
            value = 50 / 0;
        } catch (Exception ex) {
            throw GreeterExceptionBundle.EXCEPTIONS.thrownOnPurpose(ex);
        }

        return "value=" + value + " (if you are reading this, then something went wrong!)";
    }

}
