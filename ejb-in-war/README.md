ejb-in-war: Deployment of a WAR Containing an EJB
=================================================
Author: Paul Robinson  
Level: Intermediate  
Technologies: EJB, JSF, WAR  
Summary: The `ejb-in-war` quickstart demonstrates how to package an *EJB 3.1* bean in a WAR archive and deploy it to JBoss EAP. Arquillian tests are also provided.  
Target Product: JBoss EAP  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

The `ejb-in-war` quickstart demonstrates the deployment of an *EJB 3.1* bean bundled in a WAR archive for deployment to Red Hat JBoss Enterprise Application Platform. The project also includes a set of Arquillian tests for the managed bean and EJB.

The example follows the common "Hello World" pattern. These are the steps that occur:

1. A JSF page asks the user for their name.
2. On clicking submit, the name is sent to a managed bean namde `Greeter`.
3. On setting the name, the `Greeter` invokes the `GreeterEJB`, which was injected into the managed bean. Notice the field annotated with `@EJB`.
4. The response from invoking the `GreeterEJB` is stored in a field `message` of the managed bean.
5. The managed bean is annotated as `@SessionScoped`, so the same managed bean instance is used for the entire session. This ensures that the message is available when the page reloads and is displayed to the user.

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.1 or later. 

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.


Configure Maven 
-------------

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


Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/BUILD_AND_DEPLOY.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install jboss-as:deploy

4. This will deploy `target/jboss-ejb-in-war.war` to the running instance of the server.
 

Access the application 
---------------------

The application will be running at the following URL <http://localhost:8080/jboss-ejb-in-war>.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


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


Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a JBoss EAP server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 


Debug the Application
------------------------------------

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

        mvn dependency:sources

