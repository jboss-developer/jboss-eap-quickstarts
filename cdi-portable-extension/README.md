cdi-portable-extension: CDI Portable Extension 
======================================================
Author: Jason Porter  
Level: Intermediate  
Technologies: CDI  
Summary: The `cdi-portable-extension` quickstart demonstrates a simple CDI Portable Extension that uses SPI classes to inject beans with data from an XML file.  
Target Product: JBoss EAP  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

The `cdi-portable-extension` quickstart demonstrates a simple CDI Portable Extension and some of the SPI classes used
to complete that task in an application deployed to Red Hat JBoss Enterprise Application Platform. 
This particular extension explores the ProcessInjectionTarget and 
InjectionTarget SPI classes of CDI, to demonstrate a possible way to seed data into beans.

A Portable Extension is essentially an extension to Java EE 6+ which is tailored to a specific
use case which will run on any Java EE 6 or later implementation. There may be something that the
specifications don't support just yet, but could be implemented via a portable extension such as
type safe messages or external configuration of beans.

The project contains very simple domain model classes, an extension class, the service registration file
for that extension and an Arquillian test to verify the extension is working correctly.

It does not contain any user interface, the tests must be run to verify everything is working
correctly.

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

Maven prints summary of the 2 performed tests to the console.

    -------------------------------------------------------
     T E S T S
    -------------------------------------------------------
    Running org.jboss.as.quickstart.cdi.extension.test.CreatureExtensionTest
    Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.87 sec

    Results :

    Tests run: 2, Failures: 0, Errors: 0, Skipped: 0


### Server log

There are two logging statements done when the tests are run:

#### Example

    15:07:13,145 INFO  [org.jboss.as.server.deployment] (MSC service thread 1-5) JBAS015876: Starting deployment of "test.war"
    15:07:13,267 INFO  [org.jboss.weld.deployer] (MSC service thread 1-1) JBAS016002: Processing weld deployment test.war
    15:07:13,283 INFO  [org.jboss.weld.deployer] (MSC service thread 1-7) JBAS016005: Starting Services for CDI deployment: test.war
    15:07:13,290 INFO  [org.jboss.weld.deployer] (MSC service thread 1-3) JBAS016008: Starting weld service for deployment test.war
    15:07:13,338 INFO  [org.jboss.as.quickstart.cdi.extension.CreatureExtension] (MSC service thread 1-3) Setting up injection target for class org.jboss.as.quickstart.cdi.extension.model.Monster
    15:07:13,343 INFO  [org.jboss.as.quickstart.cdi.extension.CreatureExtension] (MSC service thread 1-3) Setting up injection target for class org.jboss.as.quickstart.cdi.extension.model.NonPlayerCharacter
    15:07:13,377 INFO  [org.jboss.web] (MSC service thread 1-1) JBAS018210: Registering web context: /test
    15:07:13,495 INFO  [org.jboss.as.server] (management-handler-thread - 5) JBAS018559: Deployed "test.war"
    15:07:13,922 INFO  [org.jboss.weld.deployer] (MSC service thread 1-8) JBAS016009: Stopping weld service for deployment test.war
    15:07:13,932 INFO  [org.jboss.as.server.deployment] (MSC service thread 1-7) JBAS015877: Stopped deployment test.war in 16ms
    15:07:14,039 INFO  [org.jboss.as.repository] (management-handler-thread - 6) JBAS014901: Content removed from location /home/jporter/java_libraries/jboss-as/build/target/jboss-as-7.1.1.Final/standalone/data/content/4d/40e4e277a16327b45b62954d70d91bbf3fcf42/content
    15:07:14,040 INFO  [org.jboss.as.server] (management-handler-thread - 6) JBAS018558: Undeployed "test.war"

The two statements to look for are these:

    15:07:13,338 INFO  [org.jboss.as.quickstart.cdi.extension.CreatureExtension] (MSC service thread 1-3) Setting up injection target for class org.jboss.as.quickstart.cdi.extension.model.Monster
    15:07:13,343 INFO  [org.jboss.as.quickstart.cdi.extension.CreatureExtension] (MSC service thread 1-3) Setting up injection target for class org.jboss.as.quickstart.cdi.extension.model.NonPlayerCharacter

Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a JBoss EAP server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts)


Debug the Application
------------------------------------

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

        mvn dependency:sources

