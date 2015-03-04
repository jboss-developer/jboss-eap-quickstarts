ejb-timer: Example of EJB Timer Service - @Schedule and @Timeout
===========================================
Author: Ondrej Zizka <ozizka@redhat.com>  
Level: Beginner  
Technologies: EJB 3.1 Timer  
Summary: The `ejb-timer` quickstart demonstrates how to use the EJB 3.1 timer service `@Schedule` and `@Timeout` annotations with JBoss EAP.  
Target Product: JBoss EAP  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

The `ejb-timer` quickstart demonstrates how to use the EJB 3.1 timer service in Red Hat JBoss Enterprise Application Platform. This example creates a timer service that uses the `@Schedule` and `@Timeout` annotations. 


The following EJB 3.1 Timer services are demonstrated:

 * `@Schedule`: Uses this annotation to mark a method to be executed according to the calendar schedule specified in the attributes of the annotation. This example schedules a message to be printed to the server console every 6 seconds.
 * `@Timeout`: Uses this annotation to mark a method to execute when a programmatic timer goes off. This example sets the timer to go off every 3 seconds, at which point the method prints a message to the server console.
 

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.1 or later. 

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.


Use of EAP_HOME
---------------

In the following instructions, replace `EAP_HOME` with the actual path to your JBoss EAP 6 installation. The installation path is described in detail here: [Use of EAP_HOME and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_EAP_HOME.md#use-of-eap_home-and-jboss_home-variables).


Start the JBoss EAP Server
-------------------------

1. Open a command prompt and navigate to the root of the JBoss EAP directory.
2. The following shows the command line to start the server:

        For Linux:   EAP_HOME/bin/standalone.sh
        For Windows: EAP_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/BUILD_AND_DEPLOY.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install jboss-as:deploy

This will deploy `target/jboss-ejb-timer.war` to the running instance of the server.


Access the application
----------------------

This application only prints messages to stdout.
To see it working, check the server log. You should see similar output:

    INFO  [stdout] (EJB default - 10) ScheduleExample.doWork() invoked at 2014.11.25 AD at 11:57:12 EST
    INFO  [stdout] (EJB default - 2) TimeoutExample.scheduler() EJB timer service timeout at 2014.11.25 AD at 11:57:12 EST
    INFO  [stdout] (EJB default - 4) TimeoutExample.scheduler() EJB timer service timeout at 2014.11.25 AD at 11:57:15 EST
    INFO  [stdout] (EJB default - 3) TimeoutExample.scheduler() EJB timer service timeout at 2014.11.25 AD at 11:57:18 EST
    INFO  [stdout] (EJB default - 5) ScheduleExample.doWork() invoked at 2014.11.25 AD at 11:57:18 EST
    INFO  [stdout] (EJB default - 7) TimeoutExample.scheduler() EJB timer service timeout at 2014.11.25 AD at 11:57:21 EST
    INFO  [stdout] (EJB default - 9) TimeoutExample.scheduler() EJB timer service timeout at 2014.11.25 AD at 11:57:24 EST
    INFO  [stdout] (EJB default - 6) ScheduleExample.doWork() invoked at 2014.11.25 AD at 11:57:24 EST
    INFO  [stdout] (EJB default - 8) TimeoutExample.scheduler() EJB timer service timeout at 2014.11.25 AD at 11:57:27 EST
    INFO  [stdout] (EJB default - 1) ScheduleExample.doWork() invoked at 2014.11.25 AD at 11:57:30 EST
    INFO  [stdout] (EJB default - 10) TimeoutExample.scheduler() EJB timer service timeout at 2014.11.25 AD at 11:57:30 EST
    INFO  [stdout] (EJB default - 2) TimeoutExample.scheduler() EJB timer service timeout at 2014.11.25 AD at 11:57:33 EST
    INFO  [stdout] (EJB default - 4) ScheduleExample.doWork() invoked at 2014.11.25 AD at 11:57:36 EST
    INFO  [stdout] (EJB default - 3) TimeoutExample.scheduler() EJB timer service timeout at 2014.11.25 AD at 11:57:36 EST
    INFO  [stdout] (EJB default - 5) TimeoutExample.scheduler() EJB timer service timeout at 2014.11.25 AD at 11:57:39 EST
    INFO  [stdout] (EJB default - 7) ScheduleExample.doWork() invoked at 2014.11.25 AD at 11:57:42 EST

Existing threads in the thread pool handle the invocations. They are rotated and the name of the thread that handles the invocation is printed within the parenthesis `(EJB Default - #)`.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy

Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a JBoss EAP server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 


Debug the Application
------------------------------------

If you want to debug the source code of any library in the project, 
run the following command to pull the source into your local repository. The IDE should then detect it.

        mvn dependency:sources

