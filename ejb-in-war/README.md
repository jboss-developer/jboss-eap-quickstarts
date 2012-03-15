ejb-in-war: Deployment of a WAR Containing an EJB
=================================================
Author: Paul Robinson 

What is it?
-----------

This example demonstrates the deployment of an *EJB 3.1* bean bundled in a war archive for deployment to *JBoss AS 7*.  The project also includes a set of Aquillian tests for the managed bean and EJB.

The example follows the common "Hello World" pattern. These are the steps that occur:

1. A JSF page asks the user for their name.
2. On clicking submit, the name is sent to a managed bean (Greeter).
3. On setting the name, the Greeter invokes the GreeterEJB, which was injected to the managed bean (notice the field annotated with @EJB).
4. The response from invoking the GreeterEJB is stored in a field (message) of the managed bean.
5. The managed bean is annotated as @SessionScoped, so the same managed bean instance is used for the entire session. This ensures that the message is available when the page reloads and is
displayed to the user.

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven
3.0 or better.

The application this project produces is designed to be run on a JBoss AS 7 or JBoss Enterprise Application Platform 6.
The following instructions target JBoss AS 7, but they also apply to JBoss Enterprise Application Platform 6.

With the prerequisites out of the way, you're ready to build and deploy.

Deploying the application
-------------------------

First you need to start JBoss AS 7 (or JBoss Enterprise Application Platform 6). To do this, run

    $JBOSS_HOME/bin/standalone.sh

or if you are using windows

    $JBOSS_HOME/bin/standalone.bat

To deploy the application, you first need to produce the archive to deploy using
the following Maven goal:

    mvn package

You can now deploy the artifact to JBoss AS by executing the following command:

    mvn jboss-as:deploy

This will deploy `target/jboss-as-ejb-in-war.war`.

The application will be running at the following URL <http://localhost:8080/jboss-as-ejb-in-war>.

To undeploy from JBoss AS, run this command:

    mvn jboss-as:undeploy


Running the Arquillian tests
----------------------------

By default, tests are configured to be skipped. The reason is that the sample
tests are Arquillian tests, which require the use of a container. You can select either
managed or remote container, the difference is that the remote one requires a running JBoss AS 7 / 
JBoss Enterprise Application Platform 6 instance prior executing tests.

**Testing on Remote Server**
 
First you need to start JBoss AS 7 or JBoss Enterprise Application Platform 6 instance. To do this, run
  
    $JBOSS_HOME/bin/standalone.sh
  
or if you are using windows
 
    $JBOSS_HOME/bin/standalone.bat

Once the instance is started, run the test goal with the following profile activated:

    mvn clean test -Parq-jbossas-remote

**Testing on Managed Server**
 
Arquillian will start the container for you. All you have to do is setup a path to your JBoss AS7 or JBoss
Enterprise Application Platform 6. To do this, run
  
    export JBOSS_HOME=/path/to/jboss-as
  
or if you are using Windows
 
    set JBOSS_HOME=X:\path\to\jboss-as

To run the test in JBoss AS 7 or JBoss EAP 6, run the test goal with the following profile activated:

    mvn clean test -Parq-jbossas-managed

**Investigating console output**

JUnit will present you test report summary:

	Tests run: 2, Failures: 0, Errors: 0, Skipped: 0

If you are interested in more details, check ``target/surefire-reports`` directory. 
You can check console output to verify that Arquillian has really used the real application server. 
Search for lines similar to the following ones in the server output log:

	 [timestamp] INFO  [org.jboss.as.server.deployment] (MSC service thread 1-7) JBAS015876: Starting deployment of "test.war"
	 ...
	 [timestamp] INFO  [org.jboss.as.server] (management-handler-thread - 7) JBAS018559: Deployed "test.war"
	 ...
	 [timestamp] INFO  [org.jboss.as.server.deployment] (MSC service thread 1-1) JBAS015877: Stopped deployment test.war in 51ms
	 ...
	 [timestamp] INFO  [org.jboss.as.server] (management-handler-thread - 5) JBAS018558: Undeployed "test.war"
	 
	 

You can also start JBoss AS 7 and deploy the project using Eclipse. See the JBoss AS 7
<a href="https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a> 
for more information.

Importing the project into an IDE
=================================

If you created the project using the Maven archetype wizard in your IDE
(Eclipse, NetBeans or IntelliJ IDEA), then there is nothing to do. You should
already have an IDE project.

Detailed instructions for using Eclipse with JBoss AS 7 are provided in the
JBoss AS 7 <a href="https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a>.

If you created the project from the commandline using archetype:generate, then
you need to import the project into your IDE. If you are using NetBeans 6.8 or
IntelliJ IDEA 9, then all you have to do is open the project as an existing
project. Both of these IDEs recognize Maven projects natively.

Downloading the sources and Javadocs
====================================

If you want to be able to debug into the source code or look at the Javadocs
of any library in the project, you can run either of the following two
commands to pull them into your local repository. The IDE should then detect
them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
