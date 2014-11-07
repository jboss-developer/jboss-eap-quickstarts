deltaspike-beanbuilder: Shows how to create new beans using DeltaSpike utilities
======================================================
Author: Rafael Benevides  
Level: Advanced  
Technologies: CDI, Apache DeltaSpike  
Summary: Shows how to create new beans using DeltaSpike utilities.  
Target Product: WFK  
Product Versions: EAP 6.1, EAP 6.2, EAP 6.3, WFK 2.7  
Source: <https://github.com/jboss-developer/jboss-wfk-quickstarts/>  

What is it?
-----------

This project demonstrates a CDI Portable Extension that uses DeltaSpike utilities to create new Beans.

This extension permits the injection of JPA entities by id, without the need to query it. To achieve this, the extension observes the `ProcessInjectionTarget` event and get locates all the injection points that have requested injection by id. In `AfterBeanDiscovery` event, the extension creates `Bean` instances using the `BeanBuilder` utility. 

The project contains a very simple JPA entity class, the extension class, the service registration file for that extension and an Arquillian test to verify the extension is working correctly.

It does not contain any user interface; the tests must be run to verify everything is working correctly.

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform (EAP) 6.1 or later with the  Red Hat JBoss Web Framework Kit (WFK) 2.7.

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.


Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#configure-maven) before testing the quickstarts.

Start the JBoss EAP Server
-------------------------

1. Open a command line and navigate to the root of the JBoss EAP directory.
2. The following shows the command line to start the server with the default profile:

        For Linux:   EAP_HOME/bin/standalone.sh
        For Windows: EAP_HOME\bin\standalone.bat


Run the Arquillian Tests
-------------------------

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container.

_NOTE: The following commands assume you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Run the Arquillian Tests](../README.md#run-the-arquillian-tests) for complete instructions and additional options._

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn clean test -Parq-jbossas-remote

_NOTE: If you use the Maven settings command line argument with this quickstart, you need to pass an additional argument to allow ShrinkWrap Resolver to function properly:_

    mvn clean test -Parq-jbossas-remote -s /path/to/custom/settings.xml -Dorg.apache.maven.user-settings=/path/to/custom/settings.xml

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
    Running org.jboss.as.quickstart.deltaspike.beanbuilder.test.ByIdExtensionTest
    log4j:WARN No appenders could be found for logger (org.jboss.logging).
    log4j:WARN Please initialize the log4j system properly.
    Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 8.641 sec
    
    Results :
    
    Tests run: 1, Failures: 0, Errors: 0, Skipped: 0


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts)


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
