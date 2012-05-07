Logging Example: Example setting up logging for different levels of information in *JBoss AS 7*
===============================================================================================
Author: Joel Tosi

What is it?
-----------

This example demonstrates how to setup and log different levels of information in *JBoss AS 7*. An example of asynchronous logging is also included in the configuration.

The example contains just one class file and one jsp file that, once accessed, fires off the logging information.
Also included in the example are an updated standalone.xml and logging.properties files.  

Follow these steps to better visualize the logging working:

1) First deploy the jsp file and access it at <http://localhost:8080/jboss-as-logging>

2) Look at your log files, located at *JBOSS_HOME*/standalone/log.  What information do you see there?  What came from the application?
You should see 8 log files.  server.log and boot.log are standard log files.  You will notice 6 other files: quickstart.debug.log; quickstart.error.log; quickstart.fatal.log; quickstart.info.log; quickstart.trace.log; quickstart.warn.log.  All of these log files will have log messages inside of them.  quickstart.fatal.log will have the least amount of messages while quickstart.trace.log will have the most.  This is because of the way hieararchical natrue of the log priorities - see EXPLANATION section for more information

3) Create a backup of your standalone.xml and logging.properties files located in your *JBoss_Home*/standalone/configuration directory.  Inside logging.properties, add the following:

# START OF ADDITIONS FOR QUICKSTARTS.  FILE HANDLERS ADDED FOR QUICKSTART LOGGING TO VARIOUS FILES
# IN A DEFAULT AS7 INSTALLATION, THESE HANDLERS WOULD NOT EXIST
#File handler config for quickstart example warnings
handler.FILE_QS_WARN=org.jboss.logmanager.handlers.FileHandler
handler.FILE_QS_WARN.level=WARN
handler.FILE_QS_WARN.properties=autoFlush,fileName
handler.FILE_QS_WARN.autoFlush=true
handler.FILE_QS_WARN.fileName=${org.jboss.boot.log.file:quickstart_warn.log}
handler.FILE_QS_WARN.formatter=PATTERN

#File handler config for quickstart example errors
handler.FILE_QS_ERROR=org.jboss.logmanager.handlers.FileHandler
handler.FILE_QS_ERROR.level=ERROR
handler.FILE_QS_ERROR.properties=autoFlush,fileName
handler.FILE_QS_ERROR.autoFlush=true
handler.FILE_QS_ERROR.fileName=${org.jboss.boot.log.file:quickstart_error.log}
handler.FILE_QS_ERROR.formatter=PATTERN

#File handler config for quickstart example info messages
handler.FILE_QS_INFO=org.jboss.logmanager.handlers.FileHandler
handler.FILE_QS_INFO.level=INFO
handler.FILE_QS_INFO.properties=autoFlush,fileName
handler.FILE_QS_INFO.autoFlush=true
handler.FILE_QS_INFO.fileName=${org.jboss.boot.log.file:quickstart_info.log}
handler.FILE_QS_INFO.formatter=PATTERN

#File handler config for quickstart example debug messages
handler.FILE_QS_DEBUG=org.jboss.logmanager.handlers.FileHandler
handler.FILE_QS_DEBUG.level=DEBUG
handler.FILE_QS_DEBUG.properties=autoFlush,fileName
handler.FILE_QS_DEBUG.autoFlush=true
handler.FILE_QS_DEBUG.fileName=${org.jboss.boot.log.file:quickstart_debug.log}
handler.FILE_QS_DEBUG.formatter=PATTERN

#File handler config for quickstart example trace messages
handler.FILE_QS_TRACE=org.jboss.logmanager.handlers.FileHandler
handler.FILE_QS_TRACE.level=TRACE
handler.FILE_QS_TRACE.properties=autoFlush,fileName
handler.FILE_QS_TRACE.autoFlush=true
handler.FILE_QS_TRACE.fileName=${org.jboss.boot.log.file:quickstart_trace.log}
handler.FILE_QS_TRACE.formatter=PATTERN

#File handler config for quickstart example fatal messages
handler.FILE_QS_FATAL=org.jboss.logmanager.handlers.FileHandler
handler.FILE_QS_FATAL.level=FATAL
handler.FILE_QS_FATAL.properties=autoFlush,fileName
handler.FILE_QS_FATAL.autoFlush=true
handler.FILE_QS_FATAL.fileName=${org.jboss.boot.log.file:quickstart_fatal.log}
handler.FILE_QS_FATAL.formatter=PATTERN

# END OF HANDLERS ADDED FOR QUICKSTARTS

Inside standalone.xml in the logging subsytsem (identifiable by <subsystem xmlns="urn:jboss:domain:logging:1.1"> ), add the following

<!-- EXAMPLE ASYNCHRONOUS LOGGER CONFIGURATION FOR QUICKSTART, NOTE IT LOGS TO A FILE AS DEFINED BELOW -->
<async-handler name="WARN_QS_ASYNC">
    <!-- WHICH LEVEL WE SHOULD BE LOGGING-->
	<level name="WARN"/>
	<!-- HOW MUCH INFORMATION TO BUFFER BEFORE WRITING-->
	<queue-length value="1024"/>
	<!-- WHAT TO DO WHEN BUFFER IS FULL-->
	<overflow-action value="BLOCK"/>
	<!-- SUB HANDLER FOR THIS ASYNCRHONOUS HANDLER.  IN THIS EXAMPLE, WE BUFFER AND THEN WRITE ASYNCHRONOUSLY-->
	<subhandlers>
	    <handler name="FILE_QS_WARN"/>
	</subhandlers>
</async-handler>
            <!-- EXAMPLE FILE LOGGER CONFIGURATION FOR QUICKSTART WARNING-->
            <periodic-rotating-file-handler name="FILE_QS_WARN">
            	<!-- THE LEVEL WE WANT TO LOG-->
                <level name="WARN"/>
                <!-- FORMATTING OF THE LOG MESSAGES (date, message) -->
                <formatter>
                    <pattern-formatter pattern="%d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%E%n"/>
                </formatter>
                <!-- WHERE THIS LOG SHOULD BE WRITTEN TO-->
                <file relative-to="jboss.server.log.dir" path="quickstart.warn.log"/>
                <!-- WHEN THE LOGS ROTATE, THE SUFFIX TO ADD TO THE FILENAME-->
                <suffix value=".yyyy-MM-dd"/>
                <!-- APPEND TO THE END OF FILES-->
                <append value="true"/>
            </periodic-rotating-file-handler>
            <!-- EXAMPLE FILE LOGGER CONFIGURATION FOR QUICKSTART ERRORS-->
            <periodic-rotating-file-handler name="FILE_QS_ERROR">
                <level name="ERROR"/>
                <formatter>
                    <pattern-formatter pattern="%d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%E%n"/>
                </formatter>
                <file relative-to="jboss.server.log.dir" path="quickstart.error.log"/>
                <suffix value=".yyyy-MM-dd"/>
                <append value="true"/>
            </periodic-rotating-file-handler>
            <!-- EXAMPLE FILE LOGGER CONFIGURATION FOR QUICKSTART INFO MESSAGES-->
            <periodic-rotating-file-handler name="FILE_QS_INFO">
                <level name="INFO"/>
                <formatter>
                    <pattern-formatter pattern="%d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%E%n"/>
                </formatter>
                <file relative-to="jboss.server.log.dir" path="quickstart.info.log"/>
                <suffix value=".yyyy-MM-dd"/>
                <append value="true"/>
            </periodic-rotating-file-handler>
            <!-- EXAMPLE FILE LOGGER CONFIGURATION FOR QUICKSTART DEBUG MESSAGES-->
            <periodic-rotating-file-handler name="FILE_QS_DEBUG">
                <level name="DEBUG"/>
                <formatter>
                    <pattern-formatter pattern="%d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%E%n"/>
                </formatter>
                <file relative-to="jboss.server.log.dir" path="quickstart.debug.log"/>
                <suffix value=".yyyy-MM-dd"/>
                <append value="true"/>
            </periodic-rotating-file-handler>
				<!-- EXAMPLE FILE LOGGER CONFIGURATION FOR QUICKSTART TRACE MESSAGES-->
            <periodic-rotating-file-handler name="FILE_QS_TRACE">
                <level name="TRACE"/>
                <formatter>
                    <pattern-formatter pattern="%d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%E%n"/>
                </formatter>
                <file relative-to="jboss.server.log.dir" path="quickstart.trace.log"/>
                <suffix value=".yyyy-MM-dd"/>
                <append value="true"/>
            </periodic-rotating-file-handler>
            <!-- EXAMPLE FILE LOGGER CONFIGURATION FOR QUICKSTART FATAL MESSAGES-->
            <periodic-rotating-file-handler name="FILE_QS_FATAL">
                <level name="FATAL"/>
                <formatter>
                    <pattern-formatter pattern="%d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%E%n"/>
                </formatter>
                <file relative-to="jboss.server.log.dir" path="quickstart.fatal.log"/>
                <suffix value=".yyyy-MM-dd"/>
                <append value="true"/>
            </periodic-rotating-file-handler>
<!-- SETTING THE LOG LEVEL TO TRACE FOR THE EXAMPLE CALLS-->
            <logger category="org.jboss.as.quickstarts.logging">
                <level name="TRACE"/>
                <!-- EXAMPLE DEFINING THE HANDLERS TO USE FOR THIS LOGGING-->
                <handlers>
                	  <handler name="WARN_QS_ASYNC"/>
                    <handler name="FILE_QS_INFO"/>
                    <handler name="FILE_QS_DEBUG"/>
                    <handler name="FILE_QS_TRACE"/>
                    <handler name="FILE_QS_ERROR"/>
					     <handler name="FILE_QS_FATAL"/>
                </handlers>
            </logger>

*Please note that completed standalone.xml and logging.properties files are provided for your reference.  They are commented as above as well. *

4) Restart your server

5) Access the jsp at <http://localhost:8080/jboss-as-logging>

6) Now look at your log directory, located at *JBOSS_HOME*/standalone/log.  What new files are there?  How do the files differ?

EXPLANATION
The class file fires off logs of the various types (INFO, DEBUG, TRACE, WARN, ERROR, FATAL).  Each log message will go to a different 
file, as defined in the standalone.xml and logging.properties files.  Also notice in the standalone.xml that the package defines its own log level.

The class file demonstrates the usage of *log guards*.  *Log guards* are a development best practice.  Simply put, instead of just 
writing out logs, we wrap the log writes in a check for that log level being enabled. While this may seem like overhead, that boolean check is more efficient
than relying on the underlying framework to do the check at write time.

Finally the class file does log various levels, each to their own file as configured in standalone.xml.  Common uses of the 6 log levels are outlined below (though you should use what makes
the most sense for your environment).  Note that log levels are hierarchical.  When set, all log levels above are logged as well:

*FATAL* - Used to track critical system failures.  When this log message is written, it is writing application error that has caused service to cease.  This
	is the most narrow logging.
	
*ERROR* - Used to track application errors that may cause one request to fail (not a service ceasement).

*WARN* - This is setting is used in most production environments.  At this level, all *WARN*, *ERROR*, and *FATAL* messages are written.  Use this level message
	as a predictive measure for possible forthcoming issues.
	
*INFO* - Usually only used in a development environment.  This provides any information - state transition, object values, etc

*DEBUG* - Turned on in any environment when a problem is occuring.  The information captured may be throughput, communication, object values, etc.

*TRACE* - Turned on in any environment where you are trying to follow an execution path, for optimization or debugging.  This is the most broad logging level and all
	messages will be written.

System requirements
-------------------

The example can be deployed using Maven from the command line or from Eclipse using
JBoss Tools.

To set up Maven or JBoss Tools in Eclipse, refer to the <a href="https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a>.

Deploying the application
-------------------------

To deploy to JBoss AS 7 or JBoss Enterprise Application Platform 6 using Maven, start the server, and type:

    mvn package jboss-as:deploy
 
The application is deployed to <http://localhost:8080/jboss-as-logging>. 

You can read more details in the 
<a href="https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a>.
