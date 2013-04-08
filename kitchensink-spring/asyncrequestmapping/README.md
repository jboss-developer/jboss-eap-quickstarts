asyncrequestmapping: Kitchensink AsynRequestMapping Example using Spring 3.2
====================================================================================
Author: Marius Bogoevici/Tejas Mehta
Level: Intermediate
Technologies: JSP, JPA, JSON, Spring, JUnit
Summary: An example that incorporates multiple technologies
Target Product: EAP
Source: <https://github.com/jboss-jdf/jboss-as-quickstart/>

What is it?
-----------

This is your project! It is a sample, deployable Maven 3 project to help you get your foot in the door developing with Java EE 6 on JBoss Enterprise Application Platform 6 or JBoss AS 7.

This project is setup to allow you to create a compliant Java EE 6 application using JSP, JPA 2.0 and Spring 3.2. It includes a persistence unit and some sample persistence and transaction code to introduce you to database access in enterprise Java.

In addition, this version of kitchensink showcases the use of asynchronous requests, introduced in the Spring 3.2.

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7.

Configure Maven
---------------

If you have not yet done so, you must Configure Maven before testing the quickstarts.
Start JBoss Enterprise Application Platform 6 or JBoss AS 7 with the Web Profile

    Open a command line and navigate to the root of the JBoss server directory.

    The following shows the command line to start the server with the web profile:

    For Linux:   JBOSS_HOME/bin/standalone.sh
    For Windows: JBOSS_HOME\bin\standalone.bat

Build and Deploy the Quickstart
--------------------------------

NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See Build and Deploy the Quickstarts for complete instructions and additional options.

    Make sure you have started the JBoss Server as described above.
    Open a command line and navigate to the root directory of this quickstart.

    Type this command to build and deploy the archive:

    mvn clean package jboss-as:deploy

    This will deploy target/asyncrequestmapping.war to the running instance of the server.
    
    If you don't have maven configured you can manually copy target/asyncrequestmapping.war to JBOSS_HOME/standalone/deployments.

Access the application
----------------------

The application will be running at the following URL: http://localhost:8080/kitchen-sink/.
Undeploy the Archive

    Make sure you have started the JBoss Server as described above.
    Open a command line and navigate to the root directory of this quickstart.

    When you are finished testing, type this command to undeploy the archive:

    mvn jboss-as:undeploy
    
    Or you can manually remove the application by removing kitchensink-spring.war from JBOSS_HOME/standalone/deployments

Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------------------------

You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see Use JBoss Developer Studio or Eclipse to Run the Quickstarts
Debug the Application

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

mvn dependency:sources
mvn dependency:resolve -Dclassifier=javadoc
