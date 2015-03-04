helloworld-jms: Helloworld JMS Example
======================
Author: Weston Price  
Level: Intermediate  
Technologies: JMS  
Summary: The `helloworld-jms` quickstart demonstrates the use of external JMS clients with JBoss EAP.  
Target Product: JBoss EAP  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

The `helloworld-jms` quickstart demonstrates the use of external JMS clients with Red Hat JBoss Enterprise Application Platform.

It contains the following:

1. A message producer that sends messages to a JMS destination deployed to a JBoss EAP server.

2. A message consumer that receives message from a JMS destination deployed to a JBoss EAP server. 


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
For an example of how to use the add-user utility, see the instructions located here: [Add an Application User](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CREATE_USERS.md#add-an-application-user).


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

4. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing EAP_HOME with the path to your server:

        For Linux: EAP_HOME/bin/jboss-cli.sh --connect --file=configure-jms.cli 
        For Windows: EAP_HOME\bin\jboss-cli.bat --connect --file=configure-jms.cli 
   You should see the following result when you run the script:

        The batch executed successfully.
        {"outcome" => "success"}
5. Stop the JBoss EAP server.


Review the Modified Server Configuration
-----------------------------------

After stopping the server, open the `EAP_HOME/standalone/configuration/standalone-full.xml` file and review the changes.

The following `testQueue` jms-queue was configured in a new `<jms-destinations>` element under the hornetq-server section of the `messaging` subsystem.

      <jms-destinations>
          <jms-queue name="testQueue">
              <entry name="queue/test"/>
              <entry name="java:jboss/exported/jms/queue/test"/>
          </jms-queue>
      </jms-destinations>
 

Start the JBoss EAP Server with the Full Profile
---------------

1. Open a command prompt and navigate to the root of the JBoss EAP directory.
2. The following shows the command line to start the server with the full profile:

        For Linux:   EAP_HOME/bin/standalone.sh -c standalone-full.xml
        For Windows: EAP_HOME\bin\standalone.bat -c standalone-full.xml


Build and Execute the Quickstart
-------------------------

To run the quickstart from the command line:

1. Make sure you have started the JBoss EAP server. See the instructions in the previous section.

2. Open a command prompt and navigate to the root of the helloworld-jms quickstart directory:

        cd PATH_TO_QUICKSTARTS/helloworld-jms

3. Type the following command to compile and execute the quickstart:

        mvn clean compile exec:java

 
Investigate the Console Output
-------------------------

If the Maven command is successful, with the default configuration you will see output similar to this:

    Mar 14, 2012 1:38:58 PM org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
    INFO: Attempting to acquire connection factory "jms/RemoteConnectionFactory"
    Mar 14, 2012 1:38:58 PM org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
    INFO: Found connection factory "jms/RemoteConnectionFactory" in JNDI
    Mar 14, 2012 1:38:58 PM org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
    INFO: Attempting to acquire destination "jms/queue/test"
    Mar 14, 2012 1:38:58 PM org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
    INFO: Found destination "jms/queue/test" in JNDI
    Mar 14, 2012 1:38:58 PM org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
    INFO: Sending 1 messages with content: Hello, World!
    Mar 14, 2012 1:38:58 PM org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
    INFO: Received message with content Hello, World!

_Note_: After the above INFO message, you may see the following error. You can ignore the error as it is a well known error message and does not indicate the Maven command was unsuccessful in any way. 

    Mar 14, 2012 1:38:58 PM org.jboss.naming.remote.protocol.v1.RemoteNamingStoreV1$MessageReceiver handleEnd
    ERROR: Channel end notification received, closing channel Channel ID cd114175 (outbound) of Remoting connection 00392fe8 to localhost/127.0.0.1:4447


Optional Properties
-------------------

The example provides for a certain amount of customization for the `mvn:exec` plugin using the system properties.

* `username`
   
    This username is used for both the JMS connection and the JNDI look-up.  Instructions to set up the quickstart application user can be found here: [Add an Application User](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CREATE_USERS.md#add-an-application-user).
   
    Default: `quickstartUser`
		
* `password`

    This password is used for both the JMS connection and the JNDI look-up.  Instructions to set up the quickstart application user can be found here: [Add an Application User](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CREATE_USERS.md#add-an-application-user)
   
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


Remove the JMS Configuration
----------------------------

You can remove the JMS configuration by running the  `remove-jms.cli` script provided in the root directory of this quickstart or by manually restoring the back-up copy the configuration file. 

### Remove the JMS Configuration by Running the JBoss CLI Script

1. Start the JBoss EAP server by typing the following: 

        For Linux:  EAP_HOME/bin/standalone.sh -c standalone-full.xml
        For Windows:  EAP_HOME\bin\standalone.bat -c standalone-full.xml
2. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing EAP_HOME with the path to your server:

        For Linux: EAP_HOME/bin/jboss-cli.sh --connect --file=remove-jms.cli 
        For Windows: EAP_HOME\bin\jboss-cli.bat --connect --file=remove-jms.cli 
   This script removes the `test` queue from the `messaging` subsystem in the server configuration. You should see the following result when you run the script:

        The batch executed successfully.
        {"outcome" => "success"}


### Remove the JMS Configuration Manually
1. If it is running, stop the JBoss EAP server.
2. Replace the `EAP_HOME/standalone/configuration/standalone-full.xml` file with the back-up copy of the file.


Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a JBoss EAP server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 

This quickstart consists of multiple projects, so it deploys and runs differently in JBoss Developer Studio than the other quickstarts.

1. Configure and start the JBoss EAP server Red Hat JBoss Developer Studio:
   * Define a server runtime environment that uses the `standalone-full.xml` configuration file.
   * Start the server defined in the previous step.
2. Outside of JBoss Developer Studio, configure the JMS `test` queue by running the JBoss CLI commands as described in the section above entitled *Configure the JBoss EAP Server*. 
3. In JBoss Developer Studio, right-click and choose `Run As` --> `Java Application`.  In the `Select Java Application` window, choose `HellowWorldJMSClient - org.jboss.as.quickstarts.jms` and click `OK`. The client output displays in the `Console` window.
The output messages appear in the `Console` window.

Debug the Application
------------------------------------

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

        mvn dependency:sources


 
