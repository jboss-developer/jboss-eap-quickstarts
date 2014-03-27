deltaspike-helloworld-jms: HelloWorld JMS Example Using DeltaSpike Configuration Properties
======================
Author: Weston Price, Rafael Benevides  
Level: Intermediate  
Technologies: JMS, CDI, DeltaSpike  
Summary: Demonstrates a JMS client using DeltaSpike configuration properties  
Target Product: WFK  
Product Versions: EAP 6.1, EAP 6.2, WFK 2.5  
Source: <https://github.com/jboss-developer/jboss-wfk-quickstarts/>  

What is it?
-----------

This quickstart demonstrates the use of  JMS clients with Red Hat JBoss Enterprise Application Platform 6.1 or later using DeltaSpike configuration properties.

It contains the following:

1. A message producer that sends messages to a JMS destination deployed to a JBoss EAP 6.1 or later server.

2. A message consumer that receives message from a JMS destination deployed to a JBoss EAP 6.1 or later server. 


System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform (EAP) 6.1 or later with the  Red Hat JBoss Web Framework Kit (WFK) 2.5.

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.

Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#configure-maven) before testing the quickstarts.


Add an Application User
-----------------------
This quickstart uses secured management interfaces and requires that you create an application user to access the running application. Instructions to set up the quickstart application user can be found here: [Add an Application User](../README.md#add-an-application-user)


Configure the JBoss EAP Server
---------------------------

You must first add the JMS `test` queue to the application server configuration file. You can configure JMS by running the  `configure-jms.cli` script provided in the root directory of this quickstart, by using the JBoss CLI interactively, or by manually editing the configuration file.

_NOTE - Before you begin:_

1. If it is running, stop the JBoss EAP 6.1 server.
2. Backup the file: `EAP_HOME/standalone/configuration/standalone-full.xml`
3. After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.

#### Configure JMS by Running the JBoss CLI Script

1. Start the JBoss EAP server by typing the following: 

        For Linux:  EAP_HOME/bin/standalone.sh -c standalone-full.xml
        For Windows:  EAP_HOME\bin\standalone.bat -c standalone-full.xml
2. Open a new command line, navigate to the root directory of this quickstart, and run the following command, replacing EAP_HOME with the path to your server:

        For Linux: EAP_HOME/bin/jboss-cli.sh --connect --file=configure-jms.cli 
        For Windows: EAP_HOME\bin\jboss-cli.bat --connect --file=configure-jms.cli 
This script adds the `test` queue to the `messaging` subsystem in the server configuration. You should see the following result when you run the script:

        #1 jms-queue add --queue-address=testQueue --entries=queue/test,java:jboss/exported/jms/queue/test
        The batch executed successfully.
        {"outcome" => "success"}


#### Configure JMS Using the JBoss CLI Tool Interactively

1. Start the JBoss EAP server by typing the following: 

        For Linux:  EAP_HOME/bin/standalone.sh -c standalone-full.xml
        For Windows:  EAP_HOME\bin\standalone.bat -c standalone-full.xml
2. To start the JBoss CLI tool, open a new command line, navigate to the EAP_HOME directory, and type the following:
    
        For Linux: bin/jboss-cli.sh --connect
        For Windows: bin\jboss-cli.bat --connect
3. At the prompt, type the following:

        [standalone@localhost:9999 /] jms-queue add --queue-address=testQueue --entries=queue/test,java:jboss/exported/jms/queue/test


#### Configure JMS by Manually Editing the Server Configuration File

1.  If it is running, stop the JBoss EAP server.
2.  Backup the file: `EAP_HOME/standalone/configuration/standalone-full.xml`
3.  Open the file: EAP_HOME/standalone/configuration/standalone-full.xml
4.  Add the JMS `test` queue as follows:
    * Find the messaging subsystem:  

            <subsystem xmlns="urn:jboss:domain:messaging:1.1">
    * Scroll to the end of this section and add the following XML after the `</jms-connection-factories>` end tag but before the `</hornetq-server>` element:

                <jms-destinations>
                    <jms-queue name="testQueue">
                        <entry name="queue/test"/>
                        <entry name="java:jboss/exported/jms/queue/test"/>
                    </jms-queue>
                </jms-destinations>
    * Save the changes and close the file.


Start the JBoss EAP Server with the Full Profile
---------------------------------------------------------------------------------

1. Open a command line and navigate to the root of the JBoss EAP directory.
2. The following shows the command line to start the server with the full profile:

        For Linux:   EAP_HOME/bin/standalone.sh -c standalone-full.xml
        For Windows: EAP_HOME\bin\standalone.bat -c standalone-full.xml


Build and Deploy the Quickstart
-------------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package jboss-as:deploy
4. This will deploy `target/jboss-deltaspike-helloworld-jms.war` to the running instance of the server.

Access the application
----------------------

Access the running application in a browser at the following URL:  <http://localhost:8080/jboss-deltaspike-helloworld-jms/>

1. You are presented with a simple form that has some values already filled.
2. Click on `Send Messages` button.
3. Investigate the Console Output

If the command is successful, with the default configuration you will see output similar to this:

    08:46:52,961 INFO  [org.jboss.as.quickstarts.jms.controller.JmsClientController] (http--127.0.0.1-8080-2) Attempting to acquire connection factory "jms/RemoteConnectionFactory"
    08:46:53,041 INFO  [org.jboss.as.quickstarts.jms.controller.JmsClientController] (http--127.0.0.1-8080-2) Found connection factory "jms/RemoteConnectionFactory" in JNDI
    08:46:53,041 INFO  [org.jboss.as.quickstarts.jms.controller.JmsClientController] (http--127.0.0.1-8080-2) Attempting to acquire destination "jms/queue/test"
    08:46:53,050 INFO  [org.jboss.as.quickstarts.jms.controller.JmsClientController] (http--127.0.0.1-8080-2) Found destination "jms/queue/test" in JNDI
    08:46:53,151 INFO  [org.jboss.as.quickstarts.jms.controller.JmsClientController] (http--127.0.0.1-8080-2) Sending 1 messages with content: Hello, World from Deltaspike!
    08:46:53,164 INFO  [org.jboss.as.quickstarts.jms.controller.JmsClientController] (http--127.0.0.1-8080-2) Received message #1 with content: Hello, World from Deltaspike!

_Note_: After the above INFO message, you may see the following error. You can ignore the error as it is a well known error message and does not indicate the maven command was unsuccessful in any way. 

    08:46:53,168 ERROR [org.jboss.naming.remote.protocol.v1.RemoteNamingStoreV1] (Remoting "config-based-naming-client-endpoint" task-3) Channel end notification received, closing channel Channel ID f777fa41 (outbound) of Remoting connection 45f04731 to null


Optional Properties
-------------------

This quickstart uses DeltaSpike to inject properties configuration.

The configuration is injected by using the `@ConfigProperty` annotation

        @Inject
        @ConfigProperty(name = "username", defaultValue = "quickstartUser")
        private String usernameConfig;
        
The following properties can be configured:

* `username`
   
    This username is used for both the JMS connection and the JNDI look-up.  Instructions to set up the quickstart application user can be found here: [Add an Application User](../README.md#add-an-application-user).
   
    Default: `quickstartUser`
		
* `password`

    This password is used for both the JMS connection and the JNDI look-up.  Instructions to set up the quickstart application user can be found here: [Add an Application User](../README.md#add-an-application-user)
   
    Default: `quickstartPwd1!`

* `connection.factory`

    The name of the JMS ConnectionFactory you want to use.

    Default: `jms/RemoteConnectionFactory`

* `destination`

    The name of the JMS Destination you want to use.
   
    Default: `jms/queue/test`

* `message.count`

    The number of JMS messages you want to produce and consume.

    Default: `1`

* `message.content`

    The content of the JMS TextMessage.
	
    Default: `"Hello, World!"`

* `java.naming.provider.url`

	  This property allows configuration of the JNDI directory used to lookup the JMS destination. This is useful when the client resides on another host. 

    Default: `"localhost"`

You can overwrite these properties by adding the value that you want on file `src/main/resources/META-INF/apache-deltaspike.properties`

Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Remove the JMS Configuration
----------------------------

You can remove the JMS configuration by running the  `remove-jms.cli` script provided in the root directory of this quickstart or by manually restoring the back-up copy the configuration file. 

#### Remove the JMS Configuration by Running the JBoss CLI Script

1. Start the JBoss EAP server by typing the following: 

        For Linux:  EAP_HOME/bin/standalone.sh -c standalone-full.xml
        For Windows:  EAP_HOME\bin\standalone.bat -c standalone-full.xml
2. Open a new command line, navigate to the root directory of this quickstart, and run the following command, replacing EAP_HOME with the path to your server:

        For Linux: EAP_HOME/bin/jboss-cli.sh --connect --file=remove-jms.cli 
        For Windows: EAP_HOME\bin\jboss-cli.bat --connect --file=remove-jms.cli 
This script removes the `test` queue from the `messaging` subsystem in the server configuration. You should see the following result when you run the script:

        #1 jms-queue remove --queue-address=testQueue
        The batch executed successfully.
        {"outcome" => "success"}


#### Remove the JMS Configuration Manually
1. If it is running, stop the JBoss EAP server.
2. Replace the `EAP_HOME/standalone/configuration/standalone-full.xml` file with the back-up copy of the file.

Run the Arquillian Functional Tests
-----------------------------------

This quickstart provides Arquillian functional tests. They are located under the directory "functional-tests". Functional tests verify that your application behaves correctly from the user's point of view - simulating clicking around the page as a normal user would do.

To run these tests, you must build the main project as described above.

1. Open a command line and navigate to the root directory of this quickstart.
2. Build the quickstart WAR using the following command:

        mvn clean package

3. Navigate to the functional-tests/ directory in this quickstart.
4. If you have a running instance of the JBoss EAP server, as described above, run the remote tests by typing the following command:

        mvn clean verify -Parq-jbossas-remote

5. If you prefer to run the functional tests using managed instance of the JBoss EAP server, meaning the tests will start the server for you, type the following command:

_NOTE: For this to work, Arquillian needs to know the location of the JBoss EAP server. This can be declared through the `JBOSS_HOME` environment variable or the `jbossHome` property in `arquillian.xml`. See [Run the Arquillian Tests](../README.md#run-the-arquillian-tests) for complete instructions and additional options._

        mvn clean verify -Parq-jbossas-managed

Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------

You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 

Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
