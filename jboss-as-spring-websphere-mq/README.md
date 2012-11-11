spring-websphere-mq: Using IBM WebSphere MQ and Spring with JBoss EAP 6
======================================================
Author: Doug Grove
Level: Intermediate
Technologies: JMS, Spring
Summary: Spring Message Driven POJOs with IBM MQ Messaging
Prerequisites: none
Target Product: EAP


What is it?
-----------

This quickstart demonstrates deploying IBM's MQ messaging product and integration with Spring JMS.  The sample contains configurations
for both in-bound and out-bound messaging.

The out-bound messaging components are defined in the JBoss application servers XML configuration file.  An example section is
provided in the src/test/resources folder.  This example configures the logging for IBM MQ and both a queue and a connection factory
that are later used for out-bound messaging.  Change the host names, port and queue names as needed.

The IBM MQ Resource Adapter (wmq.jmsra.rar) needs to be deployed.  The Resource Adapter can be deployed from the WebUI.

Note that there needs to be an entry in the MANIFEST.MF indicating that the IBM MQ Resource Adapter is needed by this applicaction.  This
is:

Dependencies: deployment.wmq.jmsra.rar

without this entry in the MANIFEST.MF file, the Resource Adapter will not be loaded and the classes in the adapter will not be found.  This
entry is configured the pom.xml file in the maven-war-plugin configuration.

The in-bound, message driven, components are configured in the Spring application context file.  This is the applicationContextServices.xml
file located in the src/main/resources folder.

When ran, a messages placed on the in-bound message queue is delivered to the Message Driven POJO.  This class, in the onMessage() method,
then resends the message to the out-bound message queue.


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Configure Optional Components
-------------------------

 * This quickstart uses IBM WebSphere MQ messaging.  It requires that the IBM MQ Resource Adapter is deployed to the application server.  The configuration
files will need to be changed to match the messaging servers deployment.  Note that where queue names are in all-caps that these are the queue names as
defined on the IBM MQ product.  


Start JBoss Enterprise Application Platform 6 or JBoss AS 7 with the Web Profile
-------------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package jboss-as:deploy

4. This will deploy `target/jboss-as-spring-websphere-mq.war` to the running instance of the server.


Undeploy the Archive
--------------------

Contributor: For example: 

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------

You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse) 


Debug the Application
------------------------------------

Contributor: For example: 

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc

