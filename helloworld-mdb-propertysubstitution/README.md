helloworld-mdb-propertysubstitution: MDB (Message-Driven Bean) Using Property Substitution
============================================================
Author: Serge Pagop, Andy Taylor, Jeff Mesnil  
Level: Intermediate  
Technologies: JMS, EJB, MDB  
Summary: The `helloworld-mdb-propertysubstitution` quickstart demonstrates the use of *JMS* and *EJB MDB*, enabling property substitution with annotations.  
Target Product: JBoss EAP  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

The `helloworld-mdb-propertysubstitution` quickstart demonstrates the use of *JMS* and *EJB Message-Driven Bean* in Red Hat JBoss Enterprise Application Platform. 

It is based on the [helloworld-mdb](../helloworld-mdb/README.md) quickstart, but has been enhanced to enable property substitution using the `@Resource` and `@ActivationConfigProperty` annotations.

This project creates two JMS resources:

* A queue named `HELLOWORLDMDBQueue` bound in JNDI as `java:/${property.helloworldmdb.queue}`
* A topic named `HELLOWORLDMDBTopic` bound in JNDI as `java:/${property.helloworldmdb.topic}`


System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 7 or later. 

All you need to build this project is Java 8.0 (Java SDK 1.8) or later and Maven 3.1.1 or later. See [Configure Maven for JBoss EAP 7](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


Use of EAP7_HOME
---------------

In the following instructions, replace `EAP7_HOME` with the actual path to your JBoss EAP installation. The installation path is described in detail here: [Use of EAP7_HOME and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_EAP7_HOME.md#use-of-eap_home-and-jboss_home-variables).


Configure the JBoss EAP Server
---------------------------

You enable MDB property substitution by running JBoss CLI commands. For your convenience, this quickstart batches the commands into a `enable-mdb-property-substitution.cli` script provided in the root directory of this quickstart. 

1. Before you begin, back up your server configuration file
    * If it is running, stop the JBoss EAP server.
    * Backup the file: `EAP7_HOME/standalone/configuration/standalone-full.xml`
    * After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.
2. Start the JBoss EAP server by typing the following: 

        For Linux:  EAP7_HOME/bin/standalone.sh -c standalone-full.xml
        For Windows:  EAP7_HOME\bin\standalone.bat -c standalone-full.xml
3. Review the `enable-mdb-property-substitution.cli` script file in the root of this quickstart directory. This script first enables MDB annotation property substitution the `ee` subsystem of the server configuration file by creating an `annotation-property-replacement` property with a value of `true`. It then defines the system properties that are used in the substitution. 

4. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing EAP7_HOME with the path to your server:

        For Linux: EAP7_HOME/bin/jboss-cli.sh --connect --file=enable-mdb-property-substitution.cli
        For Windows: EAP7_HOME\bin\jboss-cli.bat --connect --file=enable-mdb-property-substitution.cli
You should see the following result when you run the script:

        The batch executed successfully
        {
            "outcome" => "success",
            "result" => undefined
        }
5. Stop the JBoss EAP server.

Review the Modified Server Configuration
-----------------------------------

After stopping the server, open the `EAP7_HOME/standalone/configuration/standalone-full.xml` file and review the changes.

The `<annotation-property-replacement>` attribute is set to true in the `ee` subsystem :

        <subsystem xmlns="urn:jboss:domain:ee:4.0">
            ...
            <annotation-property-replacement>true</annotation-property-replacement>
            ...
        </subsystem>

The following system properties are defined and appear after the `<extensions>`:

    <system-properties>
        <property name="property.helloworldmdb.queue" value="java:/queue/HELLOWORLDMDBPropQueue"/>
        <property name="property.helloworldmdb.topic" value="java:/topic/HELLOWORLDMDBPropTopic"/>
        <property name="property.connection.factory" value="java:/ConnectionFactory"/>
    </system-properties>
 


Start the JBoss EAP Server with the Full Profile
---------------

1. Open a command prompt and navigate to the root of the JBoss EAP directory.
2. The following shows the command line to start the server with the full profile:

        For Linux:   EAP7_HOME/bin/standalone.sh -c standalone-full.xml
        For Windows: EAP7_HOME\bin\standalone.bat -c standalone-full.xml


Build and Deploy the Quickstart
-------------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install wildfly:deploy

4. This will deploy `target/jboss-helloworld-mdb-propertysubstitution.war` to the running instance of the server. Look at the JBoss EAP console or Server log and you should see log messages corresponding to the deployment of the message-driven beans and the JMS destinations:

        INFO  [org.wildfly.extension.messaging-activemq] (MSC service thread 1-8) WFLYMSGAMQ0002: Bound messaging object to jndi name java:/${property.helloworldmdb.queue}
        INFO  [org.wildfly.extension.messaging-activemq] (MSC service thread 1-5) WFLYMSGAMQ0002: Bound messaging object to jndi name java:/${property.helloworldmdb.topic}
        ...
        INFO  [org.wildfly.extension.messaging-activemq] (ServerService Thread Pool -- 70) WFLYMSGAMQ0002: Bound messaging object to jndi name java:/queue/HELLOWORLDMDBPropQueue
        INFO  [org.apache.activemq.artemis.core.server] (ServerService Thread Pool -- 73) AMQ221003: trying to deploy queue jms.topic.HelloWorldMDBTopic
        INFO  [org.apache.activemq.artemis.core.server] (ServerService Thread Pool -- 72) AMQ221003: trying to deploy queue jms.topic.HELLOWORLDMDBTopic
        INFO  [org.wildfly.extension.messaging-activemq] (ServerService Thread Pool -- 72) WFLYMSGAMQ0002: Bound messaging object to jndi name java:/topic/HELLOWORLDMDBPropTopic
        INFO  [org.apache.activemq.artemis.core.server] (ServerService Thread Pool -- 71) AMQ221003: trying to deploy queue jms.queue.HelloWorldMDBQueue
        INFO  [org.jboss.as.ejb3] (MSC service thread 1-7) WFLYEJB0042: Started message driven bean 'HelloWorldQTopicMDB' with 'activemq-ra.rar' resource adapter
        INFO  [org.jboss.as.ejb3] (MSC service thread 1-6) WFLYEJB0042: Started message driven bean 'HelloWorldQueueMDB' with 'activemq-ra.rar' resource adapter


Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-helloworld-mdb-propertysubstitution/> and will send some messages to the queue.

To send messages to the topic, use the following URL: <http://localhost:8080/jboss-helloworld-mdb-propertysubstitution/HelloWorldMDBServletClient?topic>

Investigate the Server Console Output
-------------------------

Look at the JBoss EAP console or Server log and you should see log messages like the following:

    INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-9 (ActiveMQ-client-global-threads-1189700957)) Received Message from queue: This is message 5
    INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-6 (ActiveMQ-client-global-threads-1189700957)) Received Message from queue: This is message 1
    INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-7 (ActiveMQ-client-global-threads-1189700957)) Received Message from queue: This is message 4
    INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-5 (ActiveMQ-client-global-threads-1189700957)) Received Message from queue: This is message 2
    INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-4 (ActiveMQ-client-global-threads-1189700957)) Received Message from queue: This is message 3

Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy

Restore the JBoss EAP Server Configuration
----------------------------

You can remove the server configuration by running the  `disable-mdb-property-substitution.cli` script provided in the root directory of this quickstart or by manually restoring the back-up copy the configuration file. 

### Restore Configuration by Running the JBoss CLI Script

1. Start the JBoss EAP server by typing the following: 

        For Linux:  EAP7_HOME/bin/standalone.sh -c standalone-full.xml
        For Windows:  EAP7_HOME\bin\standalone.bat -c standalone-full.xml
2. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing EAP7_HOME with the path to your server:

        For Linux: EAP7_HOME/bin/jboss-cli.sh --connect --file=disable-mdb-property-substitution.cli
        For Windows: EAP7_HOME\bin\jboss-cli.bat --connect --file=disable-mdb-property-substitution.cli
This script removes the system properties and sets the `<annotation-property-replacement>` value to `false` in the `ee` subsystem of the server configuration. You should see the following result when you run the script:

        The batch executed successfully.
        {
            "outcome" => "success",
            "result" => undefined
        }


### Restore the Configuration Manually
1. If it is running, stop the JBoss EAP server.
2. Replace the `EAP7_HOME/standalone/configuration/standalone-full.xml` file with the back-up copy of the file.


Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a JBoss EAP server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 

* Be sure to enable MDB property substitution by running the JBoss CLI commands as described above under [Configure the JBoss EAP Server](#configure-the-jboss-eap-server). Stop the server at the end of that step.
* Within JBoss Developer Studio, be sure to define a server runtime environment that uses the `standalone-full.xml` configuration file.
* Be sure to [Restore the JBoss EAP Server Configuration](#restore-the-jboss-eap-server-configuration) when you have completed testing this quickstart.

You may see the following warning when you import this quickstart into JBoss Developer Studio 9.0 or earlier. You can ignore this warning.

        No grammar constraints (DTD or XML Schema) referenced in the document.


Debug the Application
------------------------------------

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

    mvn dependency:sources
   


