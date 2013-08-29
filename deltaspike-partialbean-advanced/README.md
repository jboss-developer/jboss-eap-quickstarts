deltaspike-partialbean-advanced: Advanced Example of DeltaSpike's Partial Bean API
======================================================
Author: Jess Sightler
Level: Advanced
Technologies: CDI, DeltaSpike
Summary: Demonstrates a partial bean providing a dynamic implementation of a generic Entity Query service
Target Product: WFK
Source: <https://github.com/jboss-jdf/jboss-as-quickstart/>

What is it?
-----------

This quickstart demonstrates the use of a partial bean to provide dynamic implementations of an interface, and how this might be used to provide a generic query service.

The PersonQueryService provides an example of the end user API that might be available from such a framework. Extensive code comments document the example implementation.

It does not contain any user interface; the tests must be run to verify everything is working correctly.

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


Run the Arquillian Tests
-------------------------

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container.

_NOTE: The following commands assume you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Run the Arquillian Tests](../README.md#arquilliantests) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn clean test -Parq-jbossas-remote


Run tests from JBDS
-----------------------

To be able to run the tests from JBDS, first set the active Maven profile in project properties to be either 'arq-jbossas-managed' for running on
managed server or 'arq-jbossas-remote' for running on remote server.

To run the tests, right click on the project or individual classes and select Run As --> JUnit Test in the context menu.


Investigate the Console Output
----------------------------


### Maven

Maven prints summary of performed tests into the console:

   -------------------------------------------------------
     T E S T S
    -------------------------------------------------------
    Running org.jboss.as.quickstart.deltaspike.partialbean.test.QueryServiceTest
    log4j:WARN No appenders could be found for logger (org.jboss.logging).
    log4j:WARN Please initialize the log4j system properly.
    Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 4.897 sec

    Results :

    Tests run: 4, Failures: 0, Errors: 0, Skipped: 0




Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse)


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc

