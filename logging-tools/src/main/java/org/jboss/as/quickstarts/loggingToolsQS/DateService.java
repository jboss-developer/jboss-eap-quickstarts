package org.jboss.as.quickstarts.loggingToolsQS;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.as.quickstarts.loggingToolsQS.exceptions.DateExceptionsBundle;
import org.jboss.as.quickstarts.loggingToolsQS.loggers.DateLogger;




/**
 * A simple REST service which says hello in different languages
 * 
 * @author dmison@me.com
 * 
 */

@Path("dates")
public class DateService 
{

	@GET
	@Path("daysuntil/{targetdate}")
	public int showDaysUntil(@PathParam("targetdate") String targetdate)
	{
		DateLogger.LOGGER.logDaysUntilRequest(targetdate);
		
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Date target = null;
		Date now = new Date();
		
		float days = 0;
		
		try 
		{
			target = df.parse(targetdate);
			days = (float)target.getTime() - now.getTime();  
			days = days / (1000*60*60*24);	//turn milliseconds into days
		} 
		catch (ParseException ex) 
		{
			//create localised ParseException using method from bundle with details from ex
			ParseException nex = DateExceptionsBundle.EXCEPTIONS.targetDateStringDidntParse(targetdate, ex.getErrorOffset());

			//log a message using nex as the cause
			DateLogger.LOGGER.logStringCouldntParseAsDate(targetdate, nex);

			//throw a WebApplicationException (400) with the localised message from nex
			throw new WebApplicationException(Response.status(400)
					.entity(nex.getMessage())
					.type(MediaType.TEXT_PLAIN)
					.build());
		}
			
		
		return Math.round(days);
	}
	
	

}
