deltaspike-partialbean-basic: HelloWorld Example of the DeltaSpike PartialBean API
======================================================
Author: Jess Sightler
Level: Advanced
Technologies: CDI, DeltaSpike
Summary: Demonstrates a class providing a dynamic implementation of a DeltaSpike Partial Bean
Target Product: WFK

What is it?
-----------

This quickstart demonstrates the use of an InvocationHandler to provide dynamic implementations of an interface, as well as implementations of abstract methods within an abstract class.

The quickstart consists of the following classes:

 - ExamplePartialBeanAbstractClass - Abstract implementation class implementing a partial CDI Bean. This class could contain concrete methods as well as abstract. The abstract methods (for example, "sayHello") will be filled in by the InvocationHandler itself (ExamplePartialBeanImplementation in this case). The concrete methods will use the implementations provided within this class.
 - ExamplePartialBeanInterface - The methods in this interface will all be provided via the InvocationHandler itself  (ExamplePartialBeanImplementation in this case)
 - ExamplePartialBeanBinding - This Annotation is used to bind the Bean classes and interfaces to an InvocationHandler
 - ExamplePartialBeanImplementation - This class implements a dynamic InvocationHandler. When "sayHello" is called on the Bean, the "invoke" method will be called in its place.

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
    Running org.jboss.as.quickstart.deltaspike.partialbean.test.ExamplePartialBeanTest
    log4j:WARN No appenders could be found for logger (org.jboss.logging).
    log4j:WARN Please initialize the log4j system properly.
    Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 4.766 sec

    Results :

    Tests run: 3, Failures: 0, Errors: 0, Skipped: 0



Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse)


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc

