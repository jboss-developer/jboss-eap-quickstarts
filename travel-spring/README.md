travel-spring: Travel Example using Spring 3.1
======================================================
Author: Marius Bogoevici
Level: Advanced
Technologies: JPA 2.0, Junit, JMX, Spring Security, Spring Webflow, Spring Test, and JSP
Summary: This example demonstrates the use of JPA 2.0, Junit, JMX, Spring Security, Spring Webflow, Spring Test, and JSP
Target Product: EAP
Source: <https://github.com/jboss-jdf/jboss-as-quickstart/>

What is this?
-------------

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7.

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

Configure Maven
---------------

If you have not yet done so, you must Configure Maven before testing the quickstarts.
Start JBoss Enterprise Application Platform 6 or JBoss AS 7 with the Web Profile

    Open a command line and navigate to the root of the JBoss server directory.

    The following shows the command line to start the server with the web profile:

    For Linux:   JBOSS_HOME/bin/standalone.sh
    For Windows: JBOSS_HOME\bin\standalone.bat

Configure Optional Components
-------------------------

None

Start JBoss Enterprise Application Platform 6 or JBoss AS 7
-------------------------

Start JBoss Enterprise Application Platform 6 or JBoss AS 7 with the Web Profile


Build and Deploy the Quickstart
-------------------------------

NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See Build and Deploy the Quickstarts for complete instructions and additional options.

    Make sure you have started the JBoss Server as described above.
    Open a command line and navigate to the root directory of this quickstart.

    Type this command to build and deploy the archive:

    mvn clean package jboss-as:deploy

    This will deploy target/travel-spring.war to the running instance of the server. 
    
    If you don't have maven configured you can manually copy target/travel-spring.war to JBOSS_HOME/standalone/deployments.

Access the application
----------------------

The application will be running at the following URL: http://localhost:8080/travel-spring.
Undeploy the Archive

    Make sure you have started the JBoss Server as described above.
    Open a command line and navigate to the root directory of this quickstart.

    When you are finished testing, type this command to undeploy the archive:

    mvn jboss-as:undeploy
    
    Or you can manually remove the application by removing travel-spring.war from JBOSS_HOME/standalone/deployments

Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------------------------

You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see Use JBoss Developer Studio or Eclipse to Run the Quickstarts
Debug the Application

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc

