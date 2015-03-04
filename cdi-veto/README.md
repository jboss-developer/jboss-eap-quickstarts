cdi-veto: A Simple CDI Portable Extension Example
======================================================
Author: Jason Porter  
Level: Intermediate  
Technologies: CDI  
Summary: The `cdi-veto` quickstart is a simple CDI Portable Extension that uses SPI classes to show how to remove beans and inject JPA entities into an application.  
Target Product: JBoss EAP  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

The `cdi-veto` quickstart demonstrates a simple CDI Portable Extension and some of the SPI classes used
to complete that task in an application deployed to Red Hat JBoss Enterprise Application Platform. 
This particular extension explores the ProcessInjectionTarget and
InjectionTarget SPI classes of CDI to demonstrate removing a bean from CDI's knowledge and
correctly injecting JPA entities in your application.

A Portable Extension is a user extension to Java EE 6 or above which is tailored to a specific
use case which will run on any Java EE 6 or later implementation. This may be something that the
specifications don't support just yet, but could be implemented via a portable extension such as
type-safe messages or external configuration of beans.

The project contains very simple domain model classes, an extension class, the service provider configuration file,
and an Arquillian test to verify the extension is working correctly.

This quickstart does not contain any user interface. The tests must be run to verify everything is working
correctly.

_Note: This quickstart uses the H2 database included with Red Hat JBoss Enterprise Application Platform 6. It is a lightweight, relational example datasource that is used for examples only. It is not robust or scalable, is not supported, and should NOT be used in a production environment!_
  
_Note: This quickstart uses a `*-ds.xml` datasource configuration file for convenience and ease of database configuration. These files are deprecated in JBoss EAP 6.4 and should not be used in a production environment. Instead, you should configure the datasource using the Management CLI or Management Console. Datasource configuration is documented in the [Administration and Configuration Guide](https://access.redhat.com/documentation/en-US/JBoss_Enterprise_Application_Platform/) for Red Hat JBoss Enterprise Application Platform._

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.1 or later.

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.


Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.


Use of EAP_HOME
---------------

In the following instructions, replace `EAP_HOME` with the actual path to your JBoss EAP 6 installation. The installation path is described in detail here: [Use of EAP_HOME and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_EAP_HOME.md#use-of-eap_home-and-jboss_home-variables).


Start the JBoss EAP Server
-------------------------

1. Open a command prompt and navigate to the root of the JBoss EAP directory.
2. The following shows the command line to start the server:

        For Linux:   EAP_HOME/bin/standalone.sh
        For Windows: EAP_HOME\bin\standalone.bat


Run the Arquillian Tests 
-------------------------

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container. 

_NOTE: The following commands assume you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Run the Arquillian Tests](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/RUN_ARQUILLIAN_TESTS.md#run-the-arquillian-tests) for complete instructions and additional options._

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn clean test -Parq-jbossas-remote 


Investigate the Console Output
----------------------------

Maven prints summary of the 4 performed tests to the console.

    -------------------------------------------------------
     T E S T S
    -------------------------------------------------------
    Running org.jboss.as.quickstart.cdi.veto.test.InjectionWithoutVetoExtensionWithManagerTest
    Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.492 sec
    Running org.jboss.as.quickstart.cdi.veto.test.InjectionWithVetoExtensionAndManagerTest
    Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 7.988 sec
    Running org.jboss.as.quickstart.cdi.veto.test.InjectionWithVetoExtensionWithoutManagerTest
    Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 3.093 sec

    Results :

    Tests run: 4, Failures: 0, Errors: 0, Skipped: 0

In the server log you'll see a few lines similar to 

             INFO  [VetoExtension] (MSC service thread 1-8) Vetoed class class org.jboss.as.quickstart.cdi.veto.model.Car
             INFO  [CarManager] (http--127.0.0.1-8080-2) Returning new instance of Car

That will let you know the extension is working. To really see what's going on and understand this example, please read the source and the tests.


Server Log: Expected warnings and errors
-----------------------------------

_Note:_ You will see the following warnings in the server log. You can ignore these warnings.

    JBAS010489: -ds.xml file deployments are deprecated. Support may be removed in a future version.

    HHH000431: Unable to determine H2 database version, certain features may not work


Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a JBoss EAP server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts)


Debug the Application
------------------------------------

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

        mvn dependency:sources

