Logging Example
===================

This example demonstrates how to setup and log different levels of information in *JBoss AS 7*.

The example contains just one class file and one jsp file that, once accessed, fires off the logging information.
Also included in the example are an updated standalone.xml and logging.properties files.  Both of these would go in your
*JBOSS_HOME*/standalone/configuration directory.  An example of asynchronous logging is included in the configuration.
Be sure to make a backup of your original files beforehand.

The class file fires off logs of the various types (INFO, DEBUG, TRACE, WARN, ERROR, FATAL).  Each log message will go to a different 
file, as defined in the standalone.xml and logging.properties files.  Also notice in the standalone.xml that the package defines its own log level.

The class file demonstrates the usage of *log guards*.  *Log guards* are a development best practice.  Simply put, instead of just 
writing out logs, we wrap the log writes in a check for that log level being enabled. While this may seem like overhead, that boolean check is more efficient
than relying on the underlying framework to do the check at write time.

Finally the class file does log various levels, each to their own file.  Common uses of the 6 log levels are outlined below (though you should use what makes
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

The example can be deployed using Maven from the command line or from Eclipse using
JBoss Tools.

To set up Maven or JBoss Tools in Eclipse, refer to the _Getting Started Guide_.

To deploy to JBoss AS 7, start JBoss AS 7, and type `mvn package jboss-as:deploy`. 
The application is deployed to <http://localhost:8080/jboss-as-logging>. You
can read more details in the _Getting Started Guide_.