cdi-portable-extension-drools: An example of a Portable Extension using Drools 
======================================================
Author: Marco Battaglia
Level: Intermediate
Technologies: CDI, DeltaSpike, Drools, Arquillian Drone
Summary: Creating a basic CDI extension with a custom @FactModel annotation that interacts with Rule Engine 
Target Product: BRMS
Source: <https://github.com/jboss-jdf/jboss-as-quickstart/>

What is it?
-----------

This project demonstrates a simple CDI Portable Extension and some of the SPI classes used to complete that task. This particular extension explores InjectionTarget, ProcessAnnotatedType, interceptor bindings from CDI, and AnnotatedTypeBuilder from DeltaSpike to demonstrate how you can augment Java EE 6 with custom extensions.

The presentation layer and business layer are well decoupled and the business logic is augmented by the Drools as the rule engine. In Drools' philosophy, the world is a whole of facts that interact each other by rules.

The JSF page consist of billable LineItems that represent this whole of facts modified by economic rules, defined in resources/SimpleRule.drl. When you add or remove a LineItem, the rule engine triggers an action that assigns a discount. To obtain this behavior, our managed beans, i.e LineItems, are promoted to be (Drools) facts using an amazing feature of CDI: portable extension. The implementation can be found in DroolsExtension.java and LineItemInjectionTarget.java. The rules are defined inside a knowledge session whose scope is managed by a producer. In this case, ksession lives inside a ConversationScope.

You can also load rules from an URL or from a repository like Guvnor in BRMS. Your repository then acts as a single point of truth and manages the logic of your application's world. From a business point of view, this means you can change economic rules without changing application code.

Test is built by Arquillian Drone using Selenium in a trasparent way.

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7.


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

This test will automatically open a popup of your browser and it operates by itself on the web page adding and removing some products (LineItems). In every step it checks the discount value that is setted by the rule engine.

Investigate the Console Output
----------------------------


### Maven

Maven prints summary of performed tests into the console:

	-------------------------------------------------------
	 T E S T S
	-------------------------------------------------------
	Running org.jboss.as.quickstarts.jboss_as_cdi_portable_extension_drools.CdiExtensionDroolsTest
	log4j:WARN No appenders could be found for logger (org.jboss.logging).
	log4j:WARN Please initialize the log4j system properly.
	12:41:45.622 INFO - Command request: getNewBrowserSession[*firefox, http://localhost:8080, ] on session null
	12:41:45.628 INFO - creating new remote session
	12:41:45.666 WARN - Caution: '/usr/bin/firefox': file is a script file, not a real executable.  The browser environment is no longer fully under RC control
	12:41:45.668 INFO - Allocated session 49647b38341d4667aa7fd4336d608505 for http://localhost:8080, launching...
	jar:file:/home/marco/.m2/repository/org/seleniumhq/selenium/selenium-server/2.25.0/selenium-server-2.25.0.jar!/customProfileDirCUSTFFCHROME
	12:41:45.697 INFO - Preparing Firefox profile...
	12:41:47.302 INFO - Launching Firefox...
	12:41:49.645 INFO - Got result: OK,49647b38341d4667aa7fd4336d608505 on session 49647b38341d4667aa7fd4336d608505
	12:41:49.653 INFO - Command request: setSpeed[0, ] on session 49647b38341d4667aa7fd4336d608505
	12:41:49.653 INFO - Got result: OK on session 49647b38341d4667aa7fd4336d608505
	12:41:49.657 INFO - Command request: setTimeout[60000, ] on session 49647b38341d4667aa7fd4336d608505
	12:41:49.666 INFO - Got result: OK on session 49647b38341d4667aa7fd4336d608505
	12:41:54.935 INFO - Command request: open[http://127.0.0.1:8080/test/index.xhtml, ] on session 49647b38341d4667aa7fd4336d608505
	12:41:54.936 WARN - you appear to be changing domains from http://localhost:8080 to http://127.0.0.1:8080/test/index.xhtml
	this may lead to a 'Permission denied' from the browser (unless it is running as *iehta or *chrome,
	or alternatively the selenium server is running in proxy injection mode)
	12:41:55.451 INFO - Got result: OK on session 49647b38341d4667aa7fd4336d608505
	12:41:55.454 INFO - Command request: type[id=form:txtDescription, demo] on session 49647b38341d4667aa7fd4336d608505
	12:41:55.480 INFO - Got result: OK on session 49647b38341d4667aa7fd4336d608505
	12:41:55.483 INFO - Command request: type[id=form:txtPrice, 24] on session 49647b38341d4667aa7fd4336d608505
	12:41:55.493 INFO - Got result: OK on session 49647b38341d4667aa7fd4336d608505
	12:41:55.497 INFO - Command request: click[id=form:btnBuy, ] on session 49647b38341d4667aa7fd4336d608505
	12:41:55.535 INFO - Got result: OK on session 49647b38341d4667aa7fd4336d608505
	12:41:55.538 INFO - Command request: waitForPageToLoad[45000, ] on session 49647b38341d4667aa7fd4336d608505
	12:41:55.642 INFO - Got result: OK on session 49647b38341d4667aa7fd4336d608505
	12:41:55.646 INFO - Command request: isElementPresent[xpath=//tr/td[contains(text(), '0.00')], ] on session 49647b38341d4667aa7fd4336d608505
	12:41:55.661 INFO - Got result: OK,true on session 49647b38341d4667aa7fd4336d608505
	12:41:55.685 INFO - Command request: type[id=form:txtPrice, 100] on session 49647b38341d4667aa7fd4336d608505
	12:41:55.699 INFO - Got result: OK on session 49647b38341d4667aa7fd4336d608505
	12:41:55.703 INFO - Command request: click[id=form:btnBuy, ] on session 49647b38341d4667aa7fd4336d608505
	12:41:55.720 INFO - Got result: OK on session 49647b38341d4667aa7fd4336d608505
	12:41:55.725 INFO - Command request: waitForPageToLoad[45000, ] on session 49647b38341d4667aa7fd4336d608505
	12:41:55.781 INFO - Got result: OK on session 49647b38341d4667aa7fd4336d608505
	12:41:55.785 INFO - Command request: isElementPresent[xpath=//tr/td[contains(text(), '2.40')], ] on session 49647b38341d4667aa7fd4336d608505
	12:41:55.809 INFO - Got result: OK,true on session 49647b38341d4667aa7fd4336d608505
	12:41:55.832 INFO - Command request: type[id=form:txtPrice, 100] on session 49647b38341d4667aa7fd4336d608505
	12:41:55.839 INFO - Got result: OK on session 49647b38341d4667aa7fd4336d608505
	12:41:55.841 INFO - Command request: click[id=form:btnBuy, ] on session 49647b38341d4667aa7fd4336d608505
	12:41:55.853 INFO - Got result: OK on session 49647b38341d4667aa7fd4336d608505
	12:41:55.856 INFO - Command request: waitForPageToLoad[45000, ] on session 49647b38341d4667aa7fd4336d608505
	12:41:55.905 INFO - Got result: OK on session 49647b38341d4667aa7fd4336d608505
	12:41:55.908 INFO - Command request: isElementPresent[xpath=//tr/td[contains(text(), '4.80')], ] on session 49647b38341d4667aa7fd4336d608505
	12:41:55.916 INFO - Got result: OK,true on session 49647b38341d4667aa7fd4336d608505
	12:41:55.951 INFO - Command request: click[xpath=//tr[last()]/td/input, ] on session 49647b38341d4667aa7fd4336d608505
	12:41:55.967 INFO - Got result: OK on session 49647b38341d4667aa7fd4336d608505
	12:41:55.976 INFO - Command request: click[xpath=//tr[last()]/td/input, ] on session 49647b38341d4667aa7fd4336d608505
	12:41:55.993 INFO - Got result: OK on session 49647b38341d4667aa7fd4336d608505
	12:41:55.996 INFO - Command request: waitForPageToLoad[45000, ] on session 49647b38341d4667aa7fd4336d608505
	12:41:56.033 INFO - Got result: OK on session 49647b38341d4667aa7fd4336d608505
	12:41:56.035 INFO - Command request: isElementPresent[xpath=//tr/td[contains(text(), '0.00')], ] on session 49647b38341d4667aa7fd4336d608505
	12:41:56.047 INFO - Got result: OK,true on session 49647b38341d4667aa7fd4336d608505
	12:41:56.192 INFO - Command request: close[, ] on session 49647b38341d4667aa7fd4336d608505
	12:41:56.235 INFO - Got result: OK on session 49647b38341d4667aa7fd4336d608505
	12:41:56.238 INFO - Command request: testComplete[, ] on session 49647b38341d4667aa7fd4336d608505
	12:41:56.238 INFO - Killing Firefox...
	12:41:56.298 INFO - Got result: OK on session 49647b38341d4667aa7fd4336d608505
	Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 11.746 sec
	
	Results :
	
	Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
	
	[INFO] ------------------------------------------------------------------------
	[INFO] BUILD SUCCESS
	[INFO] ------------------------------------------------------------------------
	[INFO] Total time: 17.808s
	[INFO] Finished at: Tue Mar 12 12:41:56 CET 2013
	[INFO] Final Memory: 32M/290M
	[INFO] ------------------------------------------------------------------------


### Server log

There is a logging statement when the tests are run:
(Test is client side)
#### Example

	22:32:18,270 INFO  [org.jboss.as.server.deployment] (MSC service thread 1-1) JBAS015876: Starting deployment of "test.war"
	22:32:19,689 INFO  [org.jboss.weld.deployer] (MSC service thread 1-6) JBAS016002: Processing weld deployment test.war
	22:32:19,704 INFO  [org.jboss.as.ejb3.deployment.processors.EjbJndiBindingsDeploymentUnitProcessor] (MSC service thread 1-6) JNDI bindings for session bean named OrderBuilder in deployment unit deployment "test.war" are as follows:
	
		java:global/test/OrderBuilder!org.jboss.as.quickstarts.model.OrderBuilder
		java:app/test/OrderBuilder!org.jboss.as.quickstarts.model.OrderBuilder
		java:module/OrderBuilder!org.jboss.as.quickstarts.model.OrderBuilder
		java:global/test/OrderBuilder
		java:app/test/OrderBuilder
		java:module/OrderBuilder
	
	22:32:20,078 INFO  [org.jboss.weld.deployer] (MSC service thread 1-2) JBAS016005: Starting Services for CDI deployment: test.war
	22:32:20,280 INFO  [org.jboss.weld.Version] (MSC service thread 1-2) WELD-000900 1.1.8 (redhat)
	22:32:20,358 INFO  [org.jboss.as.osgi] (MSC service thread 1-4) JBAS011907: Register module: Module "deployment.test.war:main" from Service Module Loader
	22:32:20,359 INFO  [org.jboss.weld.deployer] (MSC service thread 1-3) JBAS016008: Starting weld service for deployment test.war
	22:32:20,686 INFO  [org.jboss.as.quickstarts.cdi.drools.extension.DroolsExtension] (MSC service thread 1-3) beginning the scanning process
	22:32:20,693 INFO  [org.apache.deltaspike.core.util.ClassDeactivationUtils] (MSC service thread 1-3) class: org.apache.deltaspike.core.impl.exclude.extension.ExcludeExtension activated=true
	22:32:20,693 INFO  [org.apache.deltaspike.core.util.ClassDeactivationUtils] (MSC service thread 1-3) class: org.apache.deltaspike.core.impl.exclude.GlobalAlternative activated=true
	22:32:20,693 INFO  [org.apache.deltaspike.core.util.ClassDeactivationUtils] (MSC service thread 1-3) class: org.apache.deltaspike.core.impl.exclude.CustomProjectStageBeanFilter activated=true
	22:32:20,694 INFO  [org.apache.deltaspike.core.util.ClassDeactivationUtils] (MSC service thread 1-3) class: org.apache.deltaspike.core.impl.config.ConfigurationExtension activated=true
	22:32:20,694 INFO  [org.apache.deltaspike.core.util.ClassDeactivationUtils] (MSC service thread 1-3) class: org.apache.deltaspike.core.impl.exception.control.extension.ExceptionControlExtension activated=true
	22:32:20,694 INFO  [org.apache.deltaspike.core.util.ClassDeactivationUtils] (MSC service thread 1-3) class: org.apache.deltaspike.core.impl.message.MessageBundleExtension activated=true
	22:32:20,703 INFO  [org.apache.deltaspike.core.util.ProjectStageProducer] (MSC service thread 1-3) Computed the following DeltaSpike ProjectStage: Production
	22:32:21,208 INFO  [org.jboss.as.quickstarts.cdi.drools.extension.DroolsExtension] (MSC service thread 1-3) Setting up injection target for class org.jboss.as.quickstarts.model.LineItem
	22:32:21,252 INFO  [org.jboss.as.quickstarts.cdi.drools.extension.DroolsExtension] (MSC service thread 1-3) finished the scanning process
	22:32:21,400 INFO  [org.apache.catalina.core.StandardContext] (MSC service thread 1-8) The listener "com.sun.faces.config.ConfigureListener" is already configured for this context. The duplicate definition has been ignored.
	22:32:21,514 INFO  [javax.enterprise.resource.webcontainer.jsf.config] (MSC service thread 1-8) Initializing Mojarra 2.1.7-jbossorg-2 (20120412-0335) for context '/test'
	22:32:22,170 INFO  [org.hibernate.validator.util.Version] (MSC service thread 1-8) Hibernate Validator 4.2.0.Final-redhat-1
	22:32:22,428 INFO  [org.jboss.web] (MSC service thread 1-8) JBAS018210: Registering web context: /test
	22:32:22,577 INFO  [org.jboss.as.server] (management-handler-thread - 4) JBAS018559: Deployed "test.war"
	22:32:24,881 INFO  [org.jboss.as.osgi] (MSC service thread 1-5) JBAS011908: Unregister module: Module "deployment.test.war:main" from Service Module Loader
	22:32:24,908 INFO  [org.jboss.weld.deployer] (MSC service thread 1-3) JBAS016009: Stopping weld service for deployment test.war
	22:32:24,932 INFO  [org.jboss.as.server.deployment] (MSC service thread 1-4) JBAS015877: Stopped deployment test.war in 57ms
	22:32:25,161 INFO  [org.jboss.as.repository] (management-handler-thread - 1) JBAS014901: Content removed from location /home/marco/Documents/MaterialeCorsi/JBOSS7/JB248/Security/jboss-eap-6.0/standalone/data/content/df/727aec828af1fdaed081040148916245f9b808/content
	22:32:25,162 INFO  [org.jboss.as.server] (management-handler-thread - 1) JBAS018558: Undeployed "test.war"


Run tests from JBDS
-----------------------

To be able to run the tests from JBDS, first set the active Maven profile in project properties to be either 'arq-jbossas-managed' for running on
managed server or 'arq-jbossas-remote' for running on remote server.

To run the tests, right click on the project or individual classes and select Run As --> JUnit Test in the context menu.

For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse)

Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
