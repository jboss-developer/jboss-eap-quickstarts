jboss-websocket-hello: A simple WebSocket application
========================
Author: Sande Gilda  
Level: Beginner  
Technologies: CDI, JSF, WebSocket  
Summary: An simple example of a WebSocket application  
Target Product: EAP  
Product Versions: EAP 6.1, EAP 6.2, EAP 6.3  
Source: <https://github.com/jboss-developer/jboss-sandbox-quickstarts>  

What is it?
-----------

This quickstart demonstrates how to create a WebSocket enabled application. It consists of the following:

* A JavaScript enabled WebSocket HTML client.
* A WebSocket server endpoint that uses annotations to interact with the WebSocket events.
* A `jboss-web.xml` file configured to enable WebSockets

The JBoss EAP WebSocket implementation is available via the `jbossweb-<VERSION>.jar` file.

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.1 or later. 

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.
 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.

_Note_: This quickstart requires the `jbossweb-<CURRENT-VERSION>.jar` file that is located in the JBoss Nexus Maven repository. You must add the following repository to your Maven settings: <http://repository.jboss.org/nexus/content/groups/public/>.

Start the JBoss EAP Server
-------------------------

1. Open a command prompt and navigate to the root of the JBoss EAP directory.
2. The following shows the command line to start the server:

        For Linux:   EAP_HOME/bin/standalone.sh
        For Windows: EAP_HOME\bin\standalone.bat

 
Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install jboss-as:deploy

4. This will deploy `target/jboss-websocket-hello.war` to the running instance of the server.


Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-websocket-hello>. 

1. Click on the `Connect` button to create the WebSocket connection and display current status of `Open`.
2. Type a name and click `Say Hello` to call create and send the 'Say hello to <NAME>' message. The message should appear in the server log.
3. Click on `Disconnect` to close the WebSocket connection and display the current status of `Closed`.
4. If you attempt to send another message after closing the connection, the following message appears on the page: "WebSocket connection is not established. Please click the Connect button".


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JDBS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
  
