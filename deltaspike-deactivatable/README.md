deltaspike-deactivatable: Example Using DeltaSpike Deactivatable
======================================================
Author: Rafael Benevides  
Level: Beginner  
Technologies: CDI, DeltaSpike
Summary: The `deltaspike-deactivatable` quickstart demonstrates how to use DeltaSpike `ClassDeactivator` to manually deactivate artifacts. 
  
Target Product: WFK  
Product Versions: EAP 6.1, EAP 6.2, EAP 6.3, WFK 2.7  
Source: <https://github.com/jboss-developer/jboss-wfk-quickstarts/>  

What is it?
-----------

The `deltaspike-deactivatable` quickstart demonstrates how to use DeltaSpike `ClassDeactivator` to manually deactivate artifacts in Red Hat JBoss Enterprise Application Platform. This can improve the performance if a part isn't needed,  provide a custom implementation if the default implementation isn't pluggable by default, or bypass an implementation which causes an issue.

To deactivate a class you need to implement `ClassDeactivator`. Returning 'false' or 'true' allows to deactivate or activate the class in question. This project has a ClassDeactivator that deactivates the ExcludeExtension. If the `ExcludeExtension` is deactivated then `MyBean`, which is annotated with `@Exclude`, won't be excluded
 

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

_NOTE: If you use the Maven settings command line argument with this quickstart, you need to pass an additional argument to allow ShrinkWrap Resolver to function properly:_

    mvn clean test -Parq-jbossas-remote -s /path/to/custom/settings.xml -Dorg.apache.maven.user-settings=/path/to/custom/settings.xml

Run tests from JBoss Developer Studio
-----------------------

To be able to run the tests from JBoss Developer Studio, first set the active Maven profile in project properties to be either 'arq-jbossas-managed' for running on managed server or 'arq-jbossas-remote' for running on remote server.

To run the tests, right click on the project or individual classes and select Run As --> JUnit Test in the context menu.


Investigate the Console Output
----------------------------


### Maven

        -------------------------------------------------------
         T E S T S
        -------------------------------------------------------
        Running org.jboss.as.quickstart.deltaspike.beanbuilder.test.DeactivatableTest
        log4j:WARN No appenders could be found for logger (org.jboss.logging).
        log4j:WARN Please initialize the log4j system properly.
        Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 7.745 sec

        Results :

        Tests run: 1, Failures: 0, Errors: 0, Skipped: 0


### Server log

When the tests are run, statements are written to the server log as follows:

        15:36:34,065 INFO  [org.jboss.as.server.deployment] (MSC service thread 1-1) JBAS015876: Starting deployment of "deactivator.war"
        15:36:34,632 INFO  [org.jboss.weld.deployer] (MSC service thread 1-1) JBAS016002: Processing weld deployment deactivator.war
        15:36:34,719 INFO  [org.jboss.weld.deployer] (MSC service thread 1-2) JBAS016005: Starting Services for CDI deployment: deactivator.war
        15:36:34,726 INFO  [org.jboss.weld.deployer] (MSC service thread 1-3) JBAS016008: Starting weld service for deployment deactivator.war
        15:36:34,849 INFO  [org.apache.deltaspike.core.impl.config.EnvironmentPropertyConfigSourceProvider] (MSC service thread 1-3) Custom config found by DeltaSpike. Name: 'META-INF/apache-deltaspike.properties', URL: 'vfs:/content/deactivator.war/WEB-INF/classes/META-INF/apache-deltaspike.properties'
        15:36:34,860 INFO  [org.apache.deltaspike.core.util.ClassDeactivationUtils] (MSC service thread 1-3) class: org.apache.deltaspike.core.impl.config.ConfigurationExtension activated=true
        15:36:34,861 INFO  [org.apache.deltaspike.core.util.ClassDeactivationUtils] (MSC service thread 1-3) class: org.apache.deltaspike.core.impl.exclude.extension.ExcludeExtension activated=false
        15:36:34,864 INFO  [org.apache.deltaspike.core.util.ClassDeactivationUtils] (MSC service thread 1-3) class: org.apache.deltaspike.core.impl.exclude.GlobalAlternative activated=true
        15:36:34,867 INFO  [org.apache.deltaspike.core.util.ClassDeactivationUtils] (MSC service thread 1-3) class: org.apache.deltaspike.core.impl.exclude.CustomProjectStageBeanFilter activated=true
        15:36:34,868 INFO  [org.apache.deltaspike.core.util.ClassDeactivationUtils] (MSC service thread 1-3) class: org.apache.deltaspike.core.impl.exception.control.extension.ExceptionControlExtension activated=true
        15:36:34,868 INFO  [org.apache.deltaspike.core.util.ClassDeactivationUtils] (MSC service thread 1-3) class: org.apache.deltaspike.core.impl.message.MessageBundleExtension activated=true
        15:36:35,978 INFO  [org.apache.deltaspike.core.util.ProjectStageProducer] (MSC service thread 1-3) Computed the following DeltaSpike ProjectStage: Production
        15:36:36,026 INFO  [org.jboss.web] (MSC service thread 1-4) JBAS018210: Registering web context: /deactivator
        15:36:36,164 INFO  [org.jboss.as.server] (management-handler-thread - 42) JBAS018559: Deployed "deactivator.war"
        15:36:37,510 INFO  [org.jboss.weld.deployer] (MSC service thread 1-3) JBAS016009: Stopping weld service for deployment deactivator.war
        15:36:37,566 INFO  [org.jboss.as.server.deployment] (MSC service thread 1-4) JBAS015877: Stopped deployment deactivator.war in 70ms
        15:36:37,685 INFO  [org.jboss.as.repository] (management-handler-thread - 43) JBAS014901: Content removed from location /java/tools/jboss-as-7.1.1.Final/standalone/data/content/db/87621f007d7af7741881722f7d3c152f6648f0/content
        15:36:37,686 INFO  [org.jboss.as.server] (management-handler-thread - 43) JBAS018558: Undeployed "deactivator.war"

The log statement to look for is this:

        15:36:34,861 INFO  [org.apache.deltaspike.core.util.ClassDeactivationUtils] (MSC service thread 1-3) class: org.apache.deltaspike.core.impl.exclude.extension.ExcludeExtension activated=false


Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
