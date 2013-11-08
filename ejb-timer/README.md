ejb-timer: EJB Timers example - @Schedule and @Timeout
===========================================
Author: Ondrej Zizka <ozizka@redhat.com>  
Level: Beginner  
Technologies: EJB 3.1 Timer  
Summary: Demonstrates how to use EJB 3.1 Timer (@Schedule and @Timeout) with the JBoss AS server.  
Target Product: EAP  
Product Versions: EAP 6.1, EAP 6.2  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

Demonstrates how to use EJB 3.1 Timer (@Schedule and @Timeout) with the JBoss AS server.


Features used:

 * Usage of `@Schedule`
 * Usage of `@Timeout`
 

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.1 or later. 

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Start the JBoss Server
-------------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy) for complete instructions and additional options._

1. Make sure you have started the JBoss Application Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install jboss-as:deploy

This will deploy `target/jboss-ejb-timer.war` to the running instance of the server.
To undeploy, use

        mvn jboss-as:undeploy


Access the application
----------------------

This application only prints messages to stdout.
To see it working, check the server log. You should see similar output:

    ...
    18:33:36,004 INFO  [stdout] (EJB default - 7) Hi from the EJB timer example!
    18:33:38,003 INFO  [stdout] (EJB default - 8) Hi from the EJB timer example!
    18:33:40,002 INFO  [stdout] (EJB default - 9) Hi from the EJB timer example!
    18:33:42,002 INFO  [stdout] (EJB default - 10) Hi from the EJB timer example!
    18:33:44,003 INFO  [stdout] (EJB default - 1) Hi from the EJB timer example!
    18:33:46,004 INFO  [stdout] (EJB default - 2) Hi from the EJB timer example!
    18:33:48,003 INFO  [stdout] (EJB default - 3) Hi from the EJB timer example!
    ...

The parentheses contain name of thread executing the particular invocation.
There are pre-created threads in thread pool, and they are rotated. Hence the changing number.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, 
run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
