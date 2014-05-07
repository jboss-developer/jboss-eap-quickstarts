jboss-websocket-hello: A simple WebSocket application
========================
Author: Sande Gilda, Emmanuel Hugonett   
Level: Beginner  
Technologies: WebSocket  
Summary: An simple example of a WebSocket application  
Target Product: EAP  
Product Versions: EAP 6.3  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts>  

What is it?
-----------

This quickstart demonstrates how to create a simple WebSocket enabled application. It consists of the following:

* A JavaScript enabled WebSocket HTML client.
* A WebSocket server endpoint that uses annotations to interact with the WebSocket events.
* A `jboss-web.xml` file configured to enable WebSockets

_Note: This quickstart demonstrates only a few of the basic functions. A fully functional application should provide better error handling and intercept and handle additional events._

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.3 or later, which is running on Java 7 or later.

All you need to build this project is Java 6 (Java SDK 1.6) or later, Maven 3.0 or later. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.


Configure the JBoss EAP Server
---------------------------

As a reminder, this application requires JBoss EAP 6.3 or later, running on Java 7 or later.

Before you begin, you must enable the `NIO2` connector in the `web` subsystem of the JBoss EAP server configuration file. 

1. Before you begin, back up your server configuration file
    * If it is running, stop the JBoss EAP server.
    * Backup the file: `EAP_HOME/standalone/configuration/standalone.xml`
    * After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.
2. Start the JBoss EAP server by typing the following: 

        For Linux:  EAP_HOME/bin/standalone.sh
        For Windows:  EAP_HOME\bin\standalone.bat
3. Review the `configure-http-connector.cli` file in the root of this quickstart directory. This script configures the http connector in the `web` subsystem to use the NIO2 protocol.

4. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing EAP_HOME with the path to your server:

        For Linux: EAP_HOME/bin/jboss-cli.sh --connect --file=configure-http-connector.cli 
        For Windows: EAP_HOME\bin\jboss-cli.bat --connect --file=configure-http-connector.cli
You should see the following result when you run the script:

        The batch executed successfully.
        {"outcome" => "success"}


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

_Note: If JBoss EAP is running on Java 6, you will see the following message in the server log when you deploy a websocket enabled application. This is a reminder that it needs to be running on Java 7 or greater._

        INFO [org.apache.tomcat.websocket] (ServerService Thread Pool -- 64) JBWEB008813: WebSocket support is not available when running on Java 6

Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-websocket-hello>. 

1. Click on the `Open Connection` button to create the WebSocket connection and display current status of `Open`.
2. Type a name and click `Say Hello` to create and send the 'Say hello to <NAME>' message. The message appears in the server log and a response is sent to the client.
3. Click on the `Close Connection` button to close the WebSocket connection and display the current status of `Closed`.
4. If you attempt to send another message after closing the connection, the following message appears on the page: "WebSocket connection is not established. Please click the Open Connection button".


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Remove the NIO2  HTTP Connector Configuration
----------------------------

You can remove the connector configuration by running the  `restore-http-connector.cli` script provided in the root directory of this quickstart or by manually restoring the back-up copy the configuration file. 

### Remove the Connector Configuration by Running the JBoss CLI Script

1. Start the JBoss EAP server by typing the following: 

        For Linux:  EAP_HOME/bin/standalone.sh
        For Windows:  EAP_HOME\bin\standalone.bat
2. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing EAP_HOME with the path to your server:

        For Linux: EAP_HOME/bin/jboss-cli.sh --connect --file=restore-http-connector.cli
        For Windows: EAP_HOME\bin\jboss-cli.bat --connect --file=remove-http-connector-cli
This script restores the http connector protocol in the web subsystem of the server configuration file to the original `HTTP/1.1` protocol. You should see the following result when you run the script:

        The batch executed successfully.
        {"outcome" => "success"}


### Remove the Connector Configuration Manually
1. If it is running, stop the JBoss EAP server.
2. Replace the `EAP_HOME/standalone/configuration/standalone.xml` file with the back-up copy of the file.


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JDBS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
  
