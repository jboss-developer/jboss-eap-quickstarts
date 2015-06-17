cdi-add-interceptor-binding: CDI Interceptor Binding Portable Extension Example
======================================================
Author: Pete Muir  
Level: Intermediate  
Technologies: CDI, DeltaSpike  
Summary: The `cdi-add-interceptor-binding` quickstart demonstrates how to create a simple CDI extension to automatically add an interceptor binding to a class.  
Target Product: JBoss EAP  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

The `cdi-add-interceptor-binding` quickstart demonstrates a simple CDI Portable Extension and some of the SPI classes used
to complete that task in Red Hat JBoss Enterprise Application Platform. This particular extension explores ProcessAnnotatedType and interceptor 
bindings from CDI, and AnnotatedTypeBuilder from DeltaSpike. It demonstrates how
to automatically add an interceptor binding to a class based on method parameter annotations.

A Portable Extension is essentially an extension to Java EE which is tailored to a specific
use case that will run on any Java EE 7 or higher implementation. This may be something that the
specifications don't support just yet, but could be implemented via a portable extension such as
type safe messages or external configuration of beans.

The project contains very simple domain model classes, an extension class, the service registration file
for that extension and an Arquillian test to verify the extension is working correctly.

It does not contain any user interface, the tests must be run to verify everything is working
correctly.

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform (JBoss EAP) 7 or later.

All you need to build this project is Java 8.0 (Java SDK 1.8) or later, Maven 3.0 or later.


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

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn clean test -Parq-jbossas-remote


Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 

Basically, you first set the active Maven profile in project properties to be either 'arq-jbossas-managed' for running on managed server or 'arq-jbossas-remote' for running on remote server. Then, to run the tests, right click on the project or individual classes and select Run As --> JUnit Test in the context menu.


Investigate the Console Output
----------------------------

### Maven

Maven prints summary of performed tests into the console:

    -------------------------------------------------------
    T E S T S
    -------------------------------------------------------
    Running org.jboss.as.quickstart.cdi.extension.test.ParameterLoggerTest
    log4j:WARN No appenders could be found for logger (org.jboss.logging).
    log4j:WARN Please initialize the log4j system properly.
    Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 5.032 sec

    Results :

    Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
    


### Server log

There is a logging statement when the tests are run:

#### Example

    19:56:12,018 INFO  [org.jboss.as.server.deployment] (MSC service thread 1-4) JBAS015876: Starting deployment of "parameter-logger.war"
    19:56:12,261 INFO  [org.jboss.weld.deployer] (MSC service thread 1-1) JBAS016002: Processing weld deployment parameter-logger.war
    19:56:12,292 INFO  [org.jboss.weld.deployer] (MSC service thread 1-3) JBAS016005: Starting Services for CDI deployment: parameter-logger.war
    19:56:12,297 INFO  [org.jboss.as.osgi] (MSC service thread 1-8) JBAS011907: Register module: Module "deployment.parameter-logger.war:main" from Service Module Loader
    19:56:12,298 INFO  [org.jboss.weld.deployer] (MSC service thread 1-2) JBAS016008: Starting weld service for deployment parameter-logger.war
    19:56:12,358 INFO  [org.apache.deltaspike.core.util.ClassDeactivationUtils] (MSC service thread 1-2) class: org.apache.deltaspike.core.impl.exclude.extension.ExcludeExtension activated=true
    19:56:12,358 INFO  [org.apache.deltaspike.core.util.ClassDeactivationUtils] (MSC service thread 1-2) class: org.apache.deltaspike.core.impl.exclude.GlobalAlternative activated=true
    19:56:12,358 INFO  [org.apache.deltaspike.core.util.ClassDeactivationUtils] (MSC service thread 1-2) class: org.apache.deltaspike.core.impl.exclude.CustomProjectStageBeanFilter activated=true
    19:56:12,359 INFO  [org.apache.deltaspike.core.util.ClassDeactivationUtils] (MSC service thread 1-2) class: org.apache.deltaspike.core.impl.config.ConfigurationExtension activated=true
    19:56:12,359 INFO  [org.apache.deltaspike.core.util.ClassDeactivationUtils] (MSC service thread 1-2) class: org.apache.deltaspike.core.impl.exception.control.extension.ExceptionControlExtension activated=true
    19:56:12,359 INFO  [org.apache.deltaspike.core.util.ClassDeactivationUtils] (MSC service thread 1-2) class: org.apache.deltaspike.core.impl.message.MessageBundleExtension activated=true
    19:56:12,364 INFO  [org.apache.deltaspike.core.util.ProjectStageProducer] (MSC service thread 1-2) Computed the following DeltaSpike ProjectStage: Production
    19:56:12,700 INFO  [org.jboss.web] (MSC service thread 1-1) JBAS018210: Registering web context: /parameter-logger
    19:56:12,708 INFO  [org.jboss.as.server] (management-handler-thread - 19) JBAS018559: Deployed "parameter-logger.war"
    19:56:13,163 INFO  [org.jboss.as.quickstart.cdi.parameterlogger.model.ParameterLogger] (http-/127.0.0.1:8080-2) Logging parameter value for parameter 1 of method sayHello: Hardy
    19:56:13,195 INFO  [org.jboss.as.osgi] (MSC service thread 1-2) JBAS011908: Unregister module: Module "deployment.parameter-logger.war:main" from Service Module Loader
    19:56:13,208 INFO  [org.jboss.weld.deployer] (MSC service thread 1-5) JBAS016009: Stopping weld service for deployment parameter-logger.war
    19:56:13,229 INFO  [org.jboss.as.server.deployment] (MSC service thread 1-6) JBAS015877: Stopped deployment parameter-logger.war in 36ms
    19:56:13,235 INFO  [org.jboss.as.repository] (management-handler-thread - 20) JBAS014901: Content removed from location /Users/pmuir/development/jboss/standalone/data/content/d7/b3a96b69d25b93b0385b7d7fa17524fcd3343b/content
    19:56:13,236 INFO  [org.jboss.as.server] (management-handler-thread - 20) JBAS018558: Undeployed "parameter-logger.war"    

The statement to look for is:

    19:56:13,163 INFO  [org.jboss.as.quickstart.cdi.parameterlogger.model.ParameterLogger] (http-/127.0.0.1:8080-2) Logging parameter value for parameter 1 of method sayHello: Hardy


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
