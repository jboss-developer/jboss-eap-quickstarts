ejb-timer: Example of EJB Timer Service - @Schedule and @Timeout
===========================================
Author: Ondrej Zizka <ozizka@redhat.com>  
Level: Beginner  
Technologies: EJB 3.1 Timer  
Summary: The `ejb-timer` quickstart demonstrates how to use the EJB 3.1 timer service `@Schedule` and `@Timeout` annotations with JBoss EAP.  
Target Product: EAP  
Product Versions: EAP 6.1, EAP 6.2, EAP 6.3, EAP 6.4  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

The `ejb-timer` quickstart demonstrates how to use the EJB 3.1 timer service with Red Hat JBoss Enterprise Application Platform. This example creates a timer service that uses the `@Schedule` and `@Timeout` annotations. 


The following EJB 3.1 Timer services are demonstrated:

 * `@Schedule`: Uses this annotation to mark a method to be executed according to the calendar schedule specified in the attributes of the annotation. This example schedules a message to be printed to the server console every 6 seconds.
 * `@Timeout`: Uses this annotation to mark a method to execute when a programmatic timer goes off. This example sets the timer to go off every 3 seconds.
 

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
2. The following shows the command line to start the server:

        For Linux:   EAP_HOME/bin/standalone.sh
        For Windows: EAP_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy) for complete instructions and additional options._

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install jboss-as:deploy

This will deploy `target/jboss-ejb-timer.war` to the running instance of the server.


Access the application
----------------------

This application only prints messages to stdout.
To see it working, check the server log. You should see similar output:

      12:01:06,002 INFO  [stdout] (EJB default - 7) EJB timer service scheduler says the current time is: 2014.11.05 AD at 12:01:06 EST
      12:01:12,002 INFO  [stdout] (EJB default - 6) EJB timer service scheduler says the current time is: 2014.11.05 AD at 12:01:12 EST
      12:01:18,001 INFO  [stdout] (EJB default - 5) EJB timer service scheduler says the current time is: 2014.11.05 AD at 12:01:18 EST
      12:01:20,002 INFO  [stdout] (EJB default - 8) EJB Timer: Info = EJB timer service timeout!
      12:01:24,002 INFO  [stdout] (EJB default - 9) EJB timer service scheduler says the current time is: 2014.11.05 AD at 12:01:24 EST
      12:01:30,002 INFO  [stdout] (EJB default - 10) EJB timer service scheduler says the current time is: 2014.11.05 AD at 12:01:30 EST
      12:01:36,002 INFO  [stdout] (EJB default - 2) EJB timer service scheduler says the current time is: 2014.11.05 AD at 12:01:36 EST
      12:01:40,002 INFO  [stdout] (EJB default - 1) EJB Timer: Info = EJB timer service timeout!
      12:01:42,001 INFO  [stdout] (EJB default - 4) EJB timer service scheduler says the current time is: 2014.11.05 AD at 12:01:42 EST
      12:01:48,001 INFO  [stdout] (EJB default - 3) EJB timer service scheduler says the current time is: 2014.11.05 AD at 12:01:48 EST
      12:01:54,001 INFO  [stdout] (EJB default - 7) EJB timer service scheduler says the current time is: 2014.11.05 AD at 12:01:54 EST
      12:02:00,001 INFO  [stdout] (EJB default - 6) EJB Timer: Info = EJB timer service timeout!

Existing threads in the thread pool handle the invocations. They are rotated and the name of the thread that handles the invocation is printed within the parenthesis `(EJB Default - #)`.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Debug the Application
------------------------------------

If you want to debug the source code of any library in the project, 
run the following command to pull the source into your local repository. The IDE should then detect it.

        mvn dependency:sources

