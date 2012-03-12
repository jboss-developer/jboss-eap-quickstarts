package org.jboss.as.quickstarts.loggingToolsQS.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@SuppressWarnings("serial")
public class LocaleInvalidException extends WebApplicationException 
{
	public LocaleInvalidException(String message)
	{
		super(Response.status(404)
	             .entity(message)
	             .type(MediaType.TEXT_PLAIN)
	             .build());

	}
}
