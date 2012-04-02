package org.jboss.as.quickstarts.loggingToolsQS.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/*
 *  Wrapper class for WebApplicationException.  WebApplicationException doesn't
 *  use the `String message` parameter from Throwable which is required for the passing
 *  of the translated message.
 *
 *    This class simply provides a constructor with String message which is passed along
 *    to the super class constructor as part of it's Response object.
 *
 *    http://docs.oracle.com/javaee/6/api/javax/ws/rs/WebApplicationException.html
 *
 */

@SuppressWarnings("serial")
public class LocaleInvalidException extends WebApplicationException {
    public LocaleInvalidException(String message) {
        super(Response.status(404).entity(message).type(MediaType.TEXT_PLAIN).build());

    }
}
