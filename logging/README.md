Logging Example: Example application that sets up different logging levels
===============================================================
Author: Joel Tosi  
Level: Intermediate  
Technologies: Logging  
Summary: Demonstrates how to set various application logging levels  
Prerequisites: None  
Target Product: EAP  
Product Versions: EAP 6.1, EAP 6.2, EAP 6.3  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

This example demonstrates how to set up and log different levels of information in Red Hat JBoss Enterprise Application Platform. An example of asynchronous logging is also included in the configuration examples.

This quickstart contains just one class file and one JSP file. When you access the application, it fires off the logging information.

To better visualize how the logging configuration works, you first deploy and access the application before configuring the logs and view the resulting log files. Then you configure the logs, redeploy and access the application, and look at the log files again to see the differences.


System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.1 or later. 

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.


Start the JBoss EAP Server
-------------------------

1. Open a command prompt and navigate to the root of the JBoss EAP directory.
2. The following shows the command line to start the JBoss EAP server with the default profile:

        For Linux:   EAP_HOME/bin/standalone.sh
        For Windows: EAP_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install jboss-as:deploy

4. This deploys `target/jboss-logging.war` to the running instance of the server.
 
 
Access the application 
---------------------

The application is running at the following URL: <http://localhost:8080/jboss-logging/>.


Check the Server Logs
---------------------

The log files are located in the `EAP_HOME/standalone/log` log directory. At this point you should see the following standard log file that is produced by the application server:

        * `server.log`



Configure the Logging Quickstart Log File Handlers
----------------------------------------------

To test logging the different logging levels, you must add handlers to the server `logging.properties` file and configure the server to use them. 

1. Stop the application server.
2. Create a backup of the `logging.properties` file located in the `EAP_HOME/standalone/configuration` directory.
3. Open the `logging.properties` in an editor and find the following line:
   * Find the line containing:

            logger.handlers=CONSOLE,FILE
   
     _Note: Property value order is not guaranteed, so you may see `logger.handlers=FILE,CONSOLE` instead of the line above._
     
     Replace that line with the following:

            logger.handlers=FILE,CONSOLE,FILE_QS_WARN,FILE_QS_ERROR,FILE_QS_INFO,FILE_QS_DEBUG,FILE_QS_TRACE,FILE_QS_FATAL
   * Copy and paste the following file handler configuration lines at the end of the `logging.properties` file. 

            ##### New file handler config for quickstart example warnings
            handler.FILE_QS_WARN=org.jboss.logmanager.handlers.FileHandler
            handler.FILE_QS_WARN.level=WARN
            handler.FILE_QS_WARN.properties=autoFlush,fileName
            handler.FILE_QS_WARN.autoFlush=true
            handler.FILE_QS_WARN.fileName=${org.jboss.server.log.file:quickstart_warn.log}
            handler.FILE_QS_WARN.formatter=FILE

            ##### New file handler config for quickstart example errors
            handler.FILE_QS_ERROR=org.jboss.logmanager.handlers.FileHandler
            handler.FILE_QS_ERROR.level=ERROR
            handler.FILE_QS_ERROR.properties=autoFlush,fileName
            handler.FILE_QS_ERROR.autoFlush=true
            handler.FILE_QS_ERROR.fileName=${org.jboss.server.log.file:quickstart_error.log}
            handler.FILE_QS_ERROR.formatter=FILE

            ##### New file handler config for quickstart example info messages
            handler.FILE_QS_INFO=org.jboss.logmanager.handlers.FileHandler
            handler.FILE_QS_INFO.level=INFO
            handler.FILE_QS_INFO.properties=autoFlush,fileName
            handler.FILE_QS_INFO.autoFlush=true
            handler.FILE_QS_INFO.fileName=${org.jboss.server.log.file:quickstart_info.log}
            handler.FILE_QS_INFO.formatter=FILE

            ##### New file handler config for quickstart example debug messages
            handler.FILE_QS_DEBUG=org.jboss.logmanager.handlers.FileHandler
            handler.FILE_QS_DEBUG.level=DEBUG
            handler.FILE_QS_DEBUG.properties=autoFlush,fileName
            handler.FILE_QS_DEBUG.autoFlush=true
            handler.FILE_QS_DEBUG.fileName=${org.jboss.server.log.file:quickstart_debug.log}
            handler.FILE_QS_DEBUG.formatter=FILE

            ##### New file handler config for quickstart example trace messages
            handler.FILE_QS_TRACE=org.jboss.logmanager.handlers.FileHandler
            handler.FILE_QS_TRACE.level=TRACE
            handler.FILE_QS_TRACE.properties=autoFlush,fileName
            handler.FILE_QS_TRACE.autoFlush=true
            handler.FILE_QS_TRACE.fileName=${org.jboss.server.log.file:quickstart_trace.log}
            handler.FILE_QS_TRACE.formatter=FILE

            ##### New file handler config for quickstart example fatal messages
            handler.FILE_QS_FATAL=org.jboss.logmanager.handlers.FileHandler
            handler.FILE_QS_FATAL.level=FATAL
            handler.FILE_QS_FATAL.properties=autoFlush,fileName
            handler.FILE_QS_FATAL.autoFlush=true
            handler.FILE_QS_FATAL.fileName=${org.jboss.server.log.file:quickstart_fatal.log}
            handler.FILE_QS_FATAL.formatter=FILE

    The quickstart distribution also includes a `logging-properties.txt` file containing these configuration lines.

Configure the JBoss EAP Server
---------------------------

You configure server logging by running JBoss CLI commands. For your convenience, this quickstart batches the commands into a `configure-logging.cli` script provided in the root directory of this quickstart.

1. Before you begin, back up your server configuration file
    * If it is running, stop the JBoss EAP server.
    * Backup the file: `EAP_HOME/standalone/configuration/standalone.xml`
    * After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.

2. Start the JBoss EAP server by typing the following: 

        For Linux:  EAP_HOME/bin/standalone.sh 
        For Windows:  EAP_HOME\bin\standalone.bat
3. Review the `configure-logging.cli` file in the root of this quickstart directory. This script configures the logging subsytem in the server configuration file. It configures the periodic rotating file handlers corresponding to those added to the logging properties file, configures the async handlers, creates the logger for our quickstart class and sets the level to TRACE, and assigns the async handlers for our quickstart class. 
 
4. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing EAP_HOME with the path to your server:
   
        For Linux: EAP_HOME/bin/jboss-cli.sh --connect --file=configure-logging.cli
        For Windows: EAP_HOME\bin\jboss-cli.bat --connect --file=configure-logging.cli
You should see the following result when you run the script:

        The batch executed successfully


Review the Modified Server Configuration
-----------------------------------

If you want to review and understand newly added XML configuration, stop the JBoss EAP server and open the  `EAP_HOME/standalone/configuration/standalone.xml` file. 

The following XML was added to the end of the the `logging` subsystem.

        <!-- EXAMPLE ASYNCHRONOUS LOGGER CONFIGURATION FOR QUICKSTART, NOTE IT LOGS TO FILES AS DEFINED BELOW -->
        <!-- Configure the logging async handlers -->
        <async-handler name="TRACE_QS_ASYNC">
            <level name="TRACE"/>
            <queue-length value="1024"/>
            <overflow-action value="block"/>
            <subhandlers>
                <handler name="FILE_QS_TRACE"/>
            </subhandlers>
        </async-handler>
        <async-handler name="DEBUG_QS_ASYNC">
            <level name="DEBUG"/>
            <queue-length value="1024"/>
            <overflow-action value="block"/>
            <subhandlers>
                <handler name="FILE_QS_DEBUG"/>
            </subhandlers>
        </async-handler>
        <async-handler name="INFO_QS_ASYNC">
            <level name="INFO"/>
            <queue-length value="1024"/>
            <overflow-action value="block"/>
            <subhandlers>
                <handler name="FILE_QS_INFO"/>
            </subhandlers>
        </async-handler>
        <async-handler name="WARN_QS_ASYNC">
            <level name="WARN"/>
            <queue-length value="1024"/>
            <overflow-action value="block"/>
            <subhandlers>
                <handler name="FILE_QS_WARN"/>
            </subhandlers>
        </async-handler>
        <async-handler name="ERROR_QS_ASYNC">
            <level name="ERROR"/>
            <queue-length value="1024"/>
            <overflow-action value="block"/>
            <subhandlers>
                <handler name="FILE_QS_ERROR"/>
            </subhandlers>
        </async-handler>
        <async-handler name="FATAL_QS_ASYNC">
            <level name="FATAL"/>
            <queue-length value="1024"/>
            <overflow-action value="block"/>
            <subhandlers>
                <handler name="FILE_QS_FATAL"/>
            </subhandlers>
        </async-handler>
        
        <!-- Add the periodic rotating file handlers corresponding to those added to the logging properties file -->
        <periodic-rotating-file-handler name="FILE_QS_TRACE">
            <file relative-to="jboss.server.log.dir" path="quickstart.trace.log"/>
            <suffix value=".yyyy.MM.dd"/>
        </periodic-rotating-file-handler>
        <periodic-rotating-file-handler name="FILE_QS_DEBUG">
            <file relative-to="jboss.server.log.dir" path="quickstart.debug.log"/>
            <suffix value=".yyyy.MM.dd"/>
        </periodic-rotating-file-handler>
        <periodic-rotating-file-handler name="FILE_QS_INFO">
            <file relative-to="jboss.server.log.dir" path="quickstart.info.log"/>
            <suffix value=".yyyy.MM.dd"/>
        </periodic-rotating-file-handler>
        <periodic-rotating-file-handler name="FILE_QS_WARN">
            <file relative-to="jboss.server.log.dir" path="quickstart.warn.log"/>
            <suffix value=".yyyy.MM.dd"/>
        </periodic-rotating-file-handler>
        <periodic-rotating-file-handler name="FILE_QS_ERROR">
            <file relative-to="jboss.server.log.dir" path="quickstart.error.log"/>
            <suffix value=".yyyy.MM.dd"/>
        </periodic-rotating-file-handler>
        <periodic-rotating-file-handler name="FILE_QS_FATAL">
            <file relative-to="jboss.server.log.dir" path="quickstart.fatal.log"/>
            <suffix value=".yyyy.MM.dd"/>
        </periodic-rotating-file-handler>

        <!-- INITIALLY SET THE LOG LEVEL TO TRACE FOR THE EXAMPLE CALLS-->
        <logger category="org.jboss.as.quickstarts.logging">
            <!-- To view different logging levels, change the level below 
            from TRACE to DEBUG, INFO, WARN, ERROR, or FATAL, 
            then access the application.-->
            <level name="TRACE"/>
            <handlers>
                <handler name="TRACE_QS_ASYNC"/>
                <handler name="DEBUG_QS_ASYNC"/>
                <handler name="INFO_QS_ASYNC"/>
                <handler name="WARN_QS_ASYNC"/>
                <handler name="ERROR_QS_ASYNC"/>
                <handler name="FATAL_QS_ASYNC"/>
            </handlers>
        </logger>


Test the New Logging Configuration
-------------------------

1. If your server is not started (i.e. you didn't use one of the CLI routes), then [Start the JBoss EAP server](#start-the-jboss-eap-server).
2. [Build and deploy the quickstart](#build-and-deploy-the-quickstart).
3. [Access the application](#access-the-application).


Recheck the Server Logs
---------------------

The log files are located in the `EAP_HOME/standalone/log` log directory. You should now see 8 log files.

* The following log is the standard log file produced by the application server:
    * `server.log` 
    
* The following logs are produced by the quickstart. They are listed in hierarchical order from the largest file containing the most messages to the smallest file containing the least messages. 
    * `quickstart.trace.log`
    * `quickstart.debug.log`
    * `quickstart.info.log`
    * `quickstart.warn.log`
    * `quickstart.error.log`
    * `quickstart.fatal.log`

The following describes what happens when you access this quickstart:

1. The application class file fires off logs of the various types (INFO, DEBUG, TRACE, WARN, ERROR, FATAL).  Each log message goes to a different file, as defined in the `standalone.xml` and `logging.properties` files.  Also notice in the `standalone.xml` that the application package defines its own log level.
2. The class file demonstrates the usage of *log guards*.  *Log guards* are a development best practice.  Simply put, instead of just writing out logs, we wrap the log writes in a check for that log level being enabled. While this may seem like overhead, that boolean check is more efficient than relying on the underlying framework to do the check at write time.
3. Finally, the class file logs various levels, each to its own file as configured in `standalone.xml`.  Note that log levels are hierarchical.  When set, all log levels above the specified level are logged as well.
4. Common uses of the 6 log levels are outlined below. You should use the level that makes the most sense in your environment.

        FATAL - Used to track critical system failures.  When this log message is written, it is writing application error that has caused service to cease.  This is the most narrow logging.  
        ERROR - Used to track application errors that may cause one request to fail (not a service ceasement).
        WARN - This is setting is used in most production environments.  At this level, all *WARN*, *ERROR*, and *FATAL* messages are written.  Use this level message  as a predictive measure for possible forthcoming issues.  
        INFO - Usually only used in a development environment.  This provides any information - state transition, object values, etc
        DEBUG - Turned on in any environment when a problem is occuring.  The information captured may be throughput, communication, object values, etc.
        TRACE - Turned on in any environment where you are trying to follow an execution path, for optimization or debugging.  This is the most broad logging level and all messages are written.
5. To view log file differences for different logging levels, change the level for the "org.jboss.as.quickstarts.logging" logger 
from TRACE to DEBUG, INFO, WARN, ERROR, or FATAL, then access the application.

Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Remove the Logging Configuration
----------------------------

### Restore the Logging Properties File

1. If it is running, stop the JBoss EAP server.
2. Replace the `EAP_HOME/standalone/configuration/logging.properties` file with the back-up copy of the file.

### Remove the Server Logging Configuration

You can remove the logging configuration by running the  `remove-logging.cli` script provided in the root directory of this quickstart or by manually restoring the back-up copy the configuration file. 

#### Remove the Logging Configuration by Running the JBoss CLI Script

1. Start the JBoss EAP server by typing the following: 

        For Linux:  EAP_HOME/bin/standalone.sh
        For Windows:  EAP_HOME\bin\standalone.bat
2. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing EAP_HOME with the path to your server:

        For Linux: EAP_HOME/bin/jboss-cli.sh --connect --file=remove-logging.cli 
        For Windows: EAP_HOME\bin\jboss-cli.bat --connect --file=remove-logging.cli 
This script removes the log and file handlers from the `logging` subsystem in the server configuration. You should see the following result when you run the script:

        The batch executed successfully.
        {"outcome" => "success"}


#### Remove the Logging Configuration Manually
1. If it is running, stop the JBoss EAP server.
2. Replace the `EAP_HOME/standalone/configuration/standalone.xml` file with the back-up copy of the file.



Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JDBS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc


