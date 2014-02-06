spring-resteasy: Example Using Resteasy Spring Integration
==========================================================
Author: Weinan Li <l.weinan@gmail.com>, Paul Gier <pgier@redhat.com>  
Level: Beginner  
Technologies: Resteasy, Spring  
Summary: Basic example demonstrating how a spring application can be packaged for JBoss EAP  
Target Product: WFK  
Product Versions: EAP 6.2, WFK 2.5  
Source: <https://github.com/jboss-developer/jboss-wfk-quickstarts/>  

What is it?
-----------

This project demonstrates how to package and deploy a web application, which includes resteasy-spring integration, into 
Red Hat JBoss Enterprise Application Platform.

Currently the resteasy-spring.jar is using Spring 3.0.3, as such this quickstart needs to run on some version of Spring 3.x.


System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.1 or later. 

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#configure-maven) before testing the quickstarts.

Start the JBoss Server
----------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the full profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package jboss-as:deploy     
4. This deploys the `target/jboss-spring-resteasy.war` to the running instance of the server.


Access the application 
---------------------

The application will be running at the following URL:  <http://localhost:8080/jboss-spring-resteasy/hello?name=yourname>. 

Run the Tests
-------------

1. Make sure you have started the JBoss Server as described above and deployed the quickstart.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn install -Prest-test
4. You should see the following output:

        -------------------------------------------------------
         T E S T S
        -------------------------------------------------------
        Running org.jboss.as.quickstarts.resteasyspring.test.ResteasySpringTest
        Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.211 sec

        Results :

        Tests run: 2, Failures: 0, Errors: 0, Skipped: 0

        [INFO] ------------------------------------------------------------------------
        [INFO] BUILD SUCCESS
        [INFO] ------------------------------------------------------------------------



Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy
