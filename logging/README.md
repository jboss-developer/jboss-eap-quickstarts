Logging Example: Example application that sets up different logging levels
===============================================================
Author: Joel Tosi
Level: Intermediate
Technologies: Logging
Summary: Demonstrates how to set various application logging levels
Prerequisites: None
Target Product: EAP

What is it?
-----------

This example demonstrates how to set up and log different levels of information in JBoss Enterprise Application Platform 6 and JBoss AS 7. An example of asynchronous logging is also included in the configuration examples.

This quickstart contains just one class file and one JSP file. When you access the application, it fires off the logging information.

This quickstart also includes an example of the `standalone.xml` and `logging.properties` configuration files, updated with the logging configuration changes.

To better visualize how the logging configuration works, you will first deploy and access the application before configuring the logs and look at the log files. Then you will configure the logs, redeploy and access the application, and look at the log files again to see the differences.


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


<a id="startserver"></a>
Start JBoss Enterprise Application Platform 6 or JBoss AS 7 with the Web Profile
-------------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat


<a id="buildanddeploy"></a> 
Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package jboss-as:deploy

4. This will deploy `target/jboss-as-logging.war` to the running instance of the server.
 
 
<a id="accessapp"></a>
Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-as-logging/>.


Check the Server Logs
---------------------

The log files are located in the `JBOSS_HOME/standalone/log` log directory. At this point you should see the following log files that are produced by the application server:

        * `server.log` - a standard log files produced by the application server
        * `boot.log` - a standard log files produced by the application server



Configure the Logging Quickstart Log File Handlers
----------------------------------------------

1. Stop the application server.
2. Create a backup of the `logging.properties` file located in the `JBOSS_HOME/standalone/configuration` directory.  Open the `logging.properties` in an editor and copy and paste the following file handler configuration lines. Please note that an example of a completed `logging.properties` file is provided for your reference.

        ##### New file handler config for quickstart example warnings
        handler.FILE_QS_WARN=org.jboss.logmanager.handlers.FileHandler
        handler.FILE_QS_WARN.level=WARN
        handler.FILE_QS_WARN.properties=autoFlush,fileName
        handler.FILE_QS_WARN.autoFlush=true
        handler.FILE_QS_WARN.fileName=${org.jboss.boot.log.file:quickstart_warn.log}
        handler.FILE_QS_WARN.formatter=PATTERN

        ##### New file handler config for quickstart example errors
        handler.FILE_QS_ERROR=org.jboss.logmanager.handlers.FileHandler
        handler.FILE_QS_ERROR.level=ERROR
        handler.FILE_QS_ERROR.properties=autoFlush,fileName
        handler.FILE_QS_ERROR.autoFlush=true
        handler.FILE_QS_ERROR.fileName=${org.jboss.boot.log.file:quickstart_error.log}
        handler.FILE_QS_ERROR.formatter=PATTERN

        ##### New file handler config for quickstart example info messages
        handler.FILE_QS_INFO=org.jboss.logmanager.handlers.FileHandler
        handler.FILE_QS_INFO.level=INFO
        handler.FILE_QS_INFO.properties=autoFlush,fileName
        handler.FILE_QS_INFO.autoFlush=true
        handler.FILE_QS_INFO.fileName=${org.jboss.boot.log.file:quickstart_info.log}
        handler.FILE_QS_INFO.formatter=PATTERN

        ##### New file handler config for quickstart example debug messages
        handler.FILE_QS_DEBUG=org.jboss.logmanager.handlers.FileHandler
        handler.FILE_QS_DEBUG.level=DEBUG
        handler.FILE_QS_DEBUG.properties=autoFlush,fileName
        handler.FILE_QS_DEBUG.autoFlush=true
        handler.FILE_QS_DEBUG.fileName=${org.jboss.boot.log.file:quickstart_debug.log}
        handler.FILE_QS_DEBUG.formatter=PATTERN

        ##### New file handler config for quickstart example trace messages
        handler.FILE_QS_TRACE=org.jboss.logmanager.handlers.FileHandler
        handler.FILE_QS_TRACE.level=TRACE
        handler.FILE_QS_TRACE.properties=autoFlush,fileName
        handler.FILE_QS_TRACE.autoFlush=true
        handler.FILE_QS_TRACE.fileName=${org.jboss.boot.log.file:quickstart_trace.log}
        handler.FILE_QS_TRACE.formatter=PATTERN

        ##### New file handler config for quickstart example fatal messages
        handler.FILE_QS_FATAL=org.jboss.logmanager.handlers.FileHandler
        handler.FILE_QS_FATAL.level=FATAL
        handler.FILE_QS_FATAL.properties=autoFlush,fileName
        handler.FILE_QS_FATAL.autoFlush=true
        handler.FILE_QS_FATAL.fileName=${org.jboss.boot.log.file:quickstart_fatal.log}
        handler.FILE_QS_FATAL.formatter=PATTERN
3. Create a backup of the `standalone.xml` file located in the `JBOSS_HOME/standalone/configuration` directory.  Open the file in and editor and locate the logging subystem, identified by `<subsystem xmlns="urn:jboss:domain:logging:1.1">` and copy the following XML before the ending `</subsystem>` element. Please note that an example of a completed `standalone.xml` file is provided for your reference.

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
4. [Start the server](#startserver).
5. [Build and deploy](#buildanddeploy) the quickstart.
6. [Access the application](#accessapp).


Recheck the Server Logs
---------------------

The log files are located in the `JBOSS_HOME/standalone/log` log directory. You should now see 8 log files.

* The following logs are standard log files produced by the application server:
    * `server.log` - a standard log files produced by the application server
    * `boot.log` - a standard log files produced by the application server

* The following logs are produced by the quickstart. They are listed in hierarchical order from the largest file containing the most messages to the smallest file containing the least messages. 
    * `quickstart.trace.log`
    * `quickstart.debug.log`
    * `quickstart.info.log`
    * `quickstart.warn.log`
    * `quickstart.error.log`
    * `quickstart.fatal.log`

The following describes what happens when you access this quickstart:

1. The application class file fires off logs of the various types (INFO, DEBUG, TRACE, WARN, ERROR, FATAL).  Each log message will go to a different file, as defined in the `standalone.xml` and `logging.properties` files.  Also notice in the `standalone.xml` that the package defines its own log level.
2. The class file demonstrates the usage of *log guards*.  *Log guards* are a development best practice.  Simply put, instead of just writing out logs, we wrap the log writes in a check for that log level being enabled. While this may seem like overhead, that boolean check is more efficient than relying on the underlying framework to do the check at write time.
3. Finally, the class file logs various levels, each to its own file as configured in `standalone.xml`.  Note that log levels are hierarchical.  When set, all log levels above the specified level are logged as well.
4. Common uses of the 6 log levels are outlined below. You should use the level that makes the most sense in your environment.

        FATAL - Used to track critical system failures.  When this log message is written, it is writing application error that has caused service to cease.  This is the most narrow logging.	
        ERROR - Used to track application errors that may cause one request to fail (not a service ceasement).
        WARN - This is setting is used in most production environments.  At this level, all *WARN*, *ERROR*, and *FATAL* messages are written.  Use this level message	as a predictive measure for possible forthcoming issues.	
        INFO - Usually only used in a development environment.  This provides any information - state transition, object values, etc
        DEBUG - Turned on in any environment when a problem is occuring.  The information captured may be throughput, communication, object values, etc.
        TRACE - Turned on in any environment where you are trying to follow an execution path, for optimization or debugging.  This is the most broad logging level and all messages will be written.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc


