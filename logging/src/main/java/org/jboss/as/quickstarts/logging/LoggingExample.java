package org.jboss.as.quickstarts.logging;

import org.jboss.logging.Logger;

public class LoggingExample 
{
 
	//a JBoss Logging 3 style level (FATAL, ERROR, WARN, INFO, DEBUG, TRACE), or a special level (OFF, ALL).
	private static Logger log = Logger.getLogger(LoggingExample.class.getName());
	static
	{
		logFatal();
		logError();
		logWarn();
		logInfo();
		logDebug();
		logTrace();
	}
	
	public static void logFatal()
	{
		if (log.isEnabled(Logger.Level.FATAL))
		{
			log.fatal("THIS IS A FATAL MESSAGE");
		}
	}
	
	public static void logError()
	{
		if (log.isEnabled(Logger.Level.ERROR))
		{
			log.error("THIS IS AN ERROR MESSAGE");
		}
	}
	
	public static void logWarn()
	{
		if (log.isEnabled(Logger.Level.WARN))
		{
			log.warn("THIS IS A WARNING MESSAGE");
		}
	}
	
	public static void logInfo()
	{
		if (log.isEnabled(Logger.Level.INFO))
		{
			log.info("THIS IS AN INFO MESSAGE");
		}
	}
	
	public static void logDebug()
	{
		if (log.isEnabled(Logger.Level.DEBUG))
		{
			log.debug("THIS IS A DEBUG MESSAGE");
		}
	}
	
	public static void logTrace()
	{
		if (log.isEnabled(Logger.Level.TRACE))
		{
			log.trace("THIS IS A TRACE MESSAGE");
		}
	}
}