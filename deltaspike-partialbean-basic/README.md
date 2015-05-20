deltaspike-partialbean-basic: HelloWorld Example of the DeltaSpike PartialBean API
======================================================
Author: Jess Sightler  
Level: Advanced  
Technologies: CDI, DeltaSpike  
Summary: The `deltaspike-partialbean-basic` quickstart shows use of an InvocationHandler and DeltaSpike to provide dynamic implementations of partial CDI bean.   
Target Product: JBoss EAP  
Product Versions: EAP 6.1, EAP 6.2, EAP 6.3, WFK 2.7  
Source: <https://github.com/jboss-developer/jboss-wfk-quickstarts/>  

What is it?
-----------

The `deltaspike-partialbean-basic` quickstart demonstrates the use of an InvocationHandler to provide dynamic implementations of an interface, as well as implementations of abstract methods within an abstract class in Red Hat JBoss Enterprise Application Platform.

The quickstart consists of the following classes:

 - ExamplePartialBeanAbstractClass - Abstract implementation class implementing a partial CDI Bean. This class could contain concrete methods as well as abstract. The abstract methods (for example, "sayHello") will be filled in by the InvocationHandler itself (ExamplePartialBeanImplementation in this case). The concrete methods will use the implementations provided within this class.
 - ExamplePartialBeanInterface - The methods in this interface will all be provided via the InvocationHandler itself  (ExamplePartialBeanImplementation in this case)
 - ExamplePartialBeanBinding - This Annotation is used to bind the Bean classes and interfaces to an InvocationHandler
 - ExamplePartialBeanImplementation - This class implements a dynamic InvocationHandler. When "sayHello" is called on the Bean, the "invoke" method will be called in its place.

It does not contain any user interface; the tests must be run to verify everything is working correctly.

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform (JBoss EAP) 6.1 or later with the Red Hat JBoss Web Framework Kit (WFK) 2.7.

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.

Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.


Start the JBoss EAP Server
-------------------------

1. Open a command line and navigate to the root of the JBoss EAP directory.
2. The following shows the command line to start the server with the default profile:

        For Linux:   EAP_HOME/bin/standalone.sh
        For Windows: EAP_HOME\bin\standalone.bat


Run the Arquillian Tests
-------------------------

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container.

_NOTE: The following commands assume you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Run the Arquillian Tests](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/RUN_ARQUILLIAN_TESTS.md#run-the-arquillian-tests) for complete instructions and additional options._

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn clean test -Parq-jbossas-remote


Run tests from JBoss Developer Studio
-----------------------

To be able to run the tests from JBoss Developer Studio, first set the active Maven profile in project properties to be either 'arq-jbossas-managed' for running on
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


Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc

