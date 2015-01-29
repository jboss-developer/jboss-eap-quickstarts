deltaspike-helloworld-jms: HelloWorld JMS Using DeltaSpike Configuration Properties
======================
Author: Weston Price, Rafael Benevides  
Level: Intermediate  
Technologies: JMS, CDI, DeltaSpike  
Summary: The `deltaspike-helloworld-jms` quickstart demonstrates how to create JMS clients using DeltaSpike configuration properties.  
Target Product: WFK  
Product Versions: EAP 6.1, EAP 6.2, EAP 6.3, WFK 2.7  
Source: <https://github.com/jboss-developer/jboss-wfk-quickstarts/>  

What is it?
-----------

The `deltaspike-helloworld-jms` quickstart demonstrates the use of JMS clients in Red Hat JBoss Enterprise Application Platform 6.1 or later using DeltaSpike configuration properties.

It contains the following:

1. A message producer that sends messages to a JMS destination deployed to a JBoss EAP 6.1 or later server.

2. A message consumer that receives message from a JMS destination deployed to a JBoss EAP 6.1 or later server. 


System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform (JBoss EAP) 6.1 or later with the  Red Hat JBoss Web Framework Kit (WFK) 2.7.

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.

Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.


Add an Application User
----------------

This quickstart uses secured management interfaces and requires that you create the following application user to access the running application. 

| **UserName** | **Realm** | **Password** | **Roles** |
|:-----------|:-----------|:-----------|:-----------|
| quickstartUser| ApplicationRealm | quickstartPwd1!| guest |

To add the application user, open a command prompt and type the following command:

        For Linux:   EAP_HOME/bin/add-user.sh -a -u 'quickstartUser' -p 'quickstartPwd1!' -g 'guest'
        For Windows: EAP_HOME\bin\add-user.bat  -a -u 'quickstartUser' -p 'quickstartPwd1!' -g 'guest'

If you prefer, you can use the add-user utility interactively. 
For an example of how to use the add-user utility, see instructions in the root README file located here: [Add an Application User](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CREATE_USERS.md#add-an-application-user).


Configure the JBoss EAP Server
---------------------------

You configure the JMS `test` queue by running JBoss CLI commands. For your convenience, this quickstart batches the commands into a `configure-jms.cli` script provided in the root directory of this quickstart. 

1. Before you begin, back up your server configuration file
    * If it is running, stop the JBoss EAP server.
    * Backup the file: `EAP_HOME/standalone/configuration/standalone-full.xml`
    * After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.
2. Start the JBoss EAP server by typing the following: 

        For Linux:  EAP_HOME/bin/standalone.sh -c standalone-full.xml
        For Windows:  EAP_HOME\bin\standalone.bat -c standalone-full.xml
3. Review the `configure-jms.cli` file in the root of this quickstart directory. This script adds the `test` queue to the `messaging` subsystem in the server configuration file.
4. Open a new command line, navigate to the root directory of this quickstart, and run the following command, replacing EAP_HOME with the path to your server:

        For Linux: EAP_HOME/bin/jboss-cli.sh --connect --file=configure-jms.cli 
        For Windows: EAP_HOME\bin\jboss-cli.bat --connect --file=configure-jms.cli 
   This script adds the `test` queue to the `messaging` subsystem in the server configuration. You should see the following result when you run the script:

        #1 jms-queue add --queue-address=testQueue --entries=queue/test,java:jboss/exported/jms/queue/test
        The batch executed successfully.
        {"outcome" => "success"}
5. Stop the JBoss EAP server.


Review the Modified Server Configuration
-----------------------------------

If you want to review and understand newly added XML configuration, stop the JBoss EAP server and open the  `EAP_HOME/standalone/configuration/standalone-full.xml` file. 

The following `testQueue` jms-queue was configured in a new `<jms-destinations>` element under the hornetq-server section of the `messaging` subsystem.

      <jms-destinations>
          <jms-queue name="testQueue">
              <entry name="queue/test"/>
              <entry name="java:jboss/exported/jms/queue/test"/>
          </jms-queue>
      </jms-destinations>
 

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
   
    This username is used for both the JMS connection and the JNDI look-up. See the instructions above to [Add an Application User](#add-an-application-user).
   
    Default: `quickstartUser`
		
* `password`

    This password is used for both the JMS connection and the JNDI look-up. See the instructions above to [Add an Application User](#add-an-application-user).
   
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

_NOTE: For this to work, Arquillian needs to know the location of the JBoss EAP server. This can be declared through the `JBOSS_HOME` environment variable or the `jbossHome` property in `arquillian.xml`. See [Run the Arquillian Tests](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/RUN_ARQUILLIAN_TESTS.md#run-the-arquillian-tests) for complete instructions and additional options._

        mvn clean verify -Parq-jbossas-managed


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


Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 

_NOTE:_

* Be sure to configure the JMS `test` queue by running the JBoss CLI commands as described in the section above entitled *Configure the JBoss EAP Server*. Stop the server at the end of that step.
* Within Red Hat JBoss Developer Studio, be sure to define a server runtime environment that uses the `standalone-full.xml` configuration file.


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
