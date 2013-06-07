shrinkwrap-resolver: Demonstrate usage of Shrinkwrap resolver
==============================================================
Author: Rafael Benevides
Level: Beginner
Technologies: CDI, Arquillian, Shrinkwrap
Summary: Demonstrate usage of some Shrinkwrap resolver use cases
Target Product: WFK
Source: https://github.com/jboss-jdf/jboss-as-quickstart/shrinkwrap-resolver


What is it?
-----------

With the advent of Maven and other build systems, typically thirdparty libraries and our own dependent modules are obtained from a backing software repository. In this case we supply a series of coordinates which uniquely identifies an artifact in the repository, and resolve the target files from there.

That is precisely the aim of the ShrinkWrap Resolver project; it is a Java API to obtain artifacts from a repository system. 

This quickstart demonstrate various use cases for ShrinkWrap Resolver. This Quickstart has 3 Test Classes that demonstrates the following Shrinkwrap use cases:

* ShrinkwrapResolveGAVWithoutTransitiveDepsTest
  - resolve an artifact via G:A:V without transitive dependencies
  - return resolution results as single java.io.File
  
* ShrinkwrapImportFromPomTest
  - loading pom.xml from file activating and deactivating profiles
  - importing dependencies of specified scope into list of artifacts to be resolved
  - return resolution results as a java.io.File array
  
* ShrinkwrapResolveGAPCVCustomRepoWithoutCentralTest
  - resolve an artifact via G:A:P:C:V without transitive dependencies 
  - resolve an artifact with classifer
  - disabling Maven Central
  - loading settings.xml from file (with custom repository)
  - return resolution results as a java.io.File array


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Start JBoss Enterprise Application Platform 6 or JBoss AS 7
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

To be able to run the tests from JBDS, first set the active Maven profile in project properties to be either 'arq-jbossas-managed' for running on managed server or 'arq-jbossas-remote' for running on remote server.

To run the tests, right click on the project or individual classes and select Run As --> JUnit Test in the context menu.


Investigate the Console Output
------------------------------


### Maven
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running org.jboss.as.quickstarts.shrinkwrap.resolver.ShrinkwrapImportFromPomTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 4.633 sec
Running org.jboss.as.quickstarts.shrinkwrap.resolver.ShrinkwrapResolveGAPCVCustomRepoWithoutCentralTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.439 sec
Running org.jboss.as.quickstarts.shrinkwrap.resolver.ShrinkwrapResolveGAVWithoutTransitiveDepsTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.016 sec

Results :

Tests run: 3, Failures: 0, Errors: 0, Skipped: 0


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc

