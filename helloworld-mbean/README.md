helloworld-mbean: Helloworld Using MBean and CDI component
==========================================================================
Author: Lagarde Jeremie
Level: Intermediate
Technologies: CDI, JMX and MBean
Summary: Demonstrates the use of CDI 1.0 and MBean
Target Product: EAP

What is it?
-----------

This example demonstrates the use of *CDI 1.0* and *MBean* in  *JBoss Enterprise Application Platform 6* or *JBoss AS 7*. The project also includes a set of Aquillian tests for mbeans.

The example is composed of mbeans. They are as follows :

1. `AnnotatedComponentHelloWorld`: This mbean is a managed bean with '@MXBean' annotation.

1. `MXComponentHelloWorld`:  This mbean is a managed bean with 'MXBean' interface.

1. `MXPojoHelloWorld`:  This mbean is a pojo using MXBean interface and declared in jboss-service.xml.

1. `SarMXPojoHelloWorld`:  This mbean is a pojo using MXBean interface and declared in jboss-service.xml in Sar packaging.

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


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

4. This will deploy `helloworld-mbean-webapp\target\jboss-as-helloworld-mbean-webapp.war` and `helloworld-mbean-service\target\jboss-as-helloworld-mbean-service.sar` to the running instance of the server.


Access the application 
---------------------

The application is deployed to <http://localhost:8080/jboss-as-helloworld-mbean-webapp>.



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
