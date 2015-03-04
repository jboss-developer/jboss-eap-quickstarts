ejb-asynchronous: EJB with asynchronous methods
======================================================
Author: Wolf-Dieter Fink  
Level: Advanced  
Technologies: Asynchronous EJB  
Summary: The `ejb-asynchronous` quickstart demonstrates the behavior of asynchronous EJB invocations by a deployed EJB and a remote client and how to handle errors.  
Target Product: JBoss EAP  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

The `ejb-asynchronous` quickstart demonstrates the behavior of asynchronous EJB invocations in Red Hat JBoss Enterprise Application Platform. The methods are invoked by both an EJB in the deployment and by a remote client. The quickstart also shows error handling if the asynchronous method invocation fails.

The example is composed of 2 Maven modules, each with a shared parent. The modules are as follows:

1. `ejb`: This module contains the EJB's and will be deployed to the server
2. `client` : This module contains a remote EJB client

The root `pom.xml` builds each of the submodules in the above order and deploys the archive to the server.


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


Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/BUILD_AND_DEPLOY.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install jboss-as:deploy
4. This will deploy `ejb/target/jboss-ejb-asynchronous-ejb.jar` to the running instance of the server.
 
Check whether the application is deployed successfully.


Access the application
---------------------

1. Open a command prompt and navigate to the root directory of this quickstart.
2. Type this command to start the client

        cd client
        mvn exec:exec
3. Check the client output

        INFO: The server log should contain a message at (about) <date>, indicating that the call to the asynchronous bean completed.
        INFO: Got the async result as expected => returning at <date>, duration was 200ms
        INFO: Got the async result as expected after wait => returning at <date>, duration was 1500ms
        INFO: Catch the expected Exception of the asynchronous execution!
        INFO: Results of the parallel (server) processing : [returning at <date> duration was 5000ms, returning at <date>, duration was 3000ms]

4. Check the server log. 

    There should be two INFO log messages for the `fireAndForget` invocation:

          'fireAndForget' Will wait for 15000ms

    and 15sec later (the client should be finished at this time)

          action 'fireAndForget' finished

_NOTE: In EAP 6.2, you may see the following errors in the server log after the client tests successfully complete. This is due to an issue with the server opening the outputstream and you can ignore the errors._

        JBAS014249: Error invoking method public abstract void org.jboss.as.quickstarts.ejb.asynchronous.AsynchronousAccess.fireAndForget(long) on bean named AsynchronousAccessBean for appname  modulename jboss-ejb-asynchronous-ejb distinctname : java.lang.NullPointerException
        
        JBAS014250: Could not write method invocation failure for method public abstract void org.jboss.as.quickstarts.ejb.asynchronous.AsynchronousAccess.fireAndForget(long) on bean named AsynchronousAccessBean for appname  modulename jboss-ejb-asynchronous-ejb distinctname  due to: java.io.IOException: JBAS014560: Could not open message outputstream for writing to Channel
        
Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a JBoss EAP server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 

Debug the Application
------------------------------------

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

    mvn dependency:sources
   

------------------------------------
