kitchensink-jsp: Example Using Multiple Java EE 6 Technologies with a JSP (JavaServer Pages) Front End
======================================================================================================
Author: Elvadas Nono
Level: Intermediate
Technologies: JSP
Summary: Based on kitchensink, but uses a JSP for the user interface
Target Product: EAP

What is it?
-----------

This quickstart is a deployable Maven 3 project to help you get your foot in the door developing with Java EE 6 on JBoss AS 7 or JBoss Enterprise Application Platform 6. 

This project is setup to allow you to create a compliant Java EE 6 application using *JSP 2.0* *EL 2.0* *JSTL 1.2* *CDI 1.0*, *EJB 3.1*, *JPA 2.0* and Bean Validation 1.0. 

This project recreates the presentation tier of the `kitchensink` quickstart using JSP and JSTL instead of JSF features. It reuses all other components from the Member Registration template. It also reuses the persistence unit and some sample persistence and transaction code to help you with database access in enterprise Java. 

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

4. This will deploy `target/jboss-as-kitchensink-jsp.war` to the running instance of the server.

Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-as-kitchensink-jsp/>.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Run the Arquillian Tests 
-------------------------

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container. 

_NOTE: The following commands assume you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Run the Arquillian Tests](../README.md#arquilliantests) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn clean test -Parq-jbossas-remote 


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc

