helloworld-mdb-propertysubstitution: MDB (Message-Driven Bean) Using Property Substitution
============================================================
Author: Serge Pagop, Andy Taylor, Jeff Mesnil  
Level: Intermediate  
Technologies: JMS, EJB, MDB  
Summary: The `helloworld-mdb-propertysubstitution` quickstart demonstrates the use of *JMS 1.1* and *EJB 3.1 MDB*, enabling property substitution with annotations.  
Target Product: JBoss EAP  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

The `helloworld-mdb-propertysubstitution` quickstart demonstrates the use of *JMS 1.1* and *EJB 3.1 Message-Driven Bean* in Red Hat JBoss Enterprise Application Platform. 

It is based on the [helloworld-mdb](../helloworld-mdb/README.md) quickstart, but has been enhanced to enable property substitution using 
the `@Resource` and `@ActivationConfigProperty` annotations.

This project creates two JMS resources:

* A queue named `MDBPropertySubQueue` bound in JNDI as `java:/queue/MDBPROPERTYSUBQueue`
* A topic named `MDBPropertySubTopic` bound in JNDI as `java:/topic/MDBPROPERTYSUBTopic`


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


Configure the JBoss EAP Server
---------------------------

You enable MDB property substitution by running JBoss CLI commands. For your convenience, this quickstart batches the commands into a `enable-mdb-property-substitution.cli` script provided in the root directory of this quickstart. 

1. Before you begin, back up your server configuration file
    * If it is running, stop the JBoss EAP server.
    * Backup the file: `EAP_HOME/standalone/configuration/standalone-full.xml`
    * After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.
2. Start the JBoss EAP server by typing the following: 

        For Linux:  EAP_HOME/bin/standalone.sh -c standalone-full.xml
        For Windows:  EAP_HOME\bin\standalone.bat -c standalone-full.xml
3. Review the `enable-mdb-property-substitution.cli` script file in the root of this quickstart directory. This script adds the `test` queue to the `messaging` subsystem in the server configuration file.

4. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing EAP_HOME with the path to your server:

        For Linux: EAP_HOME/bin/jboss-cli.sh --connect --file=enable-mdb-property-substitution.cli
        For Windows: EAP_HOME\bin\jboss-cli.bat --connect --file=enable-mdb-property-substitution.cli
You should see the following result when you run the script:

        The batch executed successfully
        {
            "outcome" => "success",
            "result" => undefined
        }
5. Stop the JBoss EAP server.

Review the Modified Server Configuration
-----------------------------------

After stopping the server, open the `EAP_HOME/standalone/configuration/standalone-full.xml` file and review the changes.

The `<annotation-property-replacement>` attribute is set to true in the `ee` subsystem :

        <subsystem xmlns="urn:jboss:domain:ee:1.2">
            <spec-descriptor-property-replacement>false</spec-descriptor-property-replacement>
            <jboss-descriptor-property-replacement>true</jboss-descriptor-property-replacement>
             <annotation-property-replacement>true</annotation-property-replacement>
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

        For Linux:   EAP_HOME/bin/standalone.sh -c standalone-full.xml
        For Windows: EAP_HOME\bin\standalone.bat -c standalone-full.xml


Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/BUILD_AND_DEPLOY.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install jboss-as:deploy

4. This will deploy `target/jboss-helloworld-mdb-propertysubstitution.war` to the running instance of the server. Look at the JBoss EAP console or Server log and you should see log messages corresponding to the deployment of the message-driven beans and the JMS destinations:

        INFO  [org.hornetq.core.server] (ServerService Thread Pool -- 62) HQ221003: trying to deploy queue jms.queue.HELLOWORLDMDBQueue
        INFO  [org.jboss.as.messaging] (ServerService Thread Pool -- 62) JBAS011601: Bound messaging object to jndi name java:/queue/HELLOWORLDMDBPropQueue
        INFO  [org.hornetq.core.server] (ServerService Thread Pool -- 63) HQ221003: trying to deploy queue jms.topic.HELLOWORLDMDBTopic
        INFO  [org.jboss.as.messaging] (ServerService Thread Pool -- 63) JBAS011601: Bound messaging object to jndi name java:/topic/HELLOWORLDMDBPropTopic
        INFO  [org.jboss.as.ejb3] (MSC service thread 1-10) JBAS014142: Started message driven bean 'HelloWorldQTopicMDB' with 'hornetq-ra' resource adapter
        INFO  [org.jboss.as.ejb3] (MSC service thread 1-15) JBAS014142: Started message driven bean 'HelloWorldQueueMDB' with 'hornetq-ra' resource adapter
        INFO  [org.jboss.web] (ServerService Thread Pool -- 66) JBAS018210: Register web context: /jboss-helloworld-mdb-propertysubstitution
        INFO  [org.jboss.as.server] (management-handler-thread - 1) JBAS018559: Deployed "jboss-helloworld-mdb-propertysubstitution.war" (runtime-name : "jboss-helloworld-mdb-propertysubstitution.war")


Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-helloworld-mdb-propertysubstitution/> and will send some messages to the queue.

To send messages to the topic, use the following URL: <http://localhost:8080/jboss-helloworld-mdb-propertysubstitution/HelloWorldMDBServletClient?topic>

Investigate the Server Console Output
-------------------------

Look at the JBoss EAP console or Server log and you should see log messages like the following:


        INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-5 (HornetQ-client-global-threads-479334962)) Received Message from queue: This is message 5
        INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-3 (HornetQ-client-global-threads-479334962)) Received Message from queue: This is message 3
        INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-2 (HornetQ-client-global-threads-479334962)) Received Message from queue: This is message 1
        INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-1 (HornetQ-client-global-threads-479334962)) Received Message from queue: This is message 4
        INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-0 (HornetQ-client-global-threads-479334962)) Received Message from queue: This is message 2

Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy

Restore the JBoss EAP Server Configuration
----------------------------

You can remove the server configuration by running the  `disable-mdb-property-substitution.cli` script provided in the root directory of this quickstart or by manually restoring the back-up copy the configuration file. 

### Restore Configuration by Running the JBoss CLI Script

1. Start the JBoss EAP server by typing the following: 

        For Linux:  EAP_HOME/bin/standalone.sh -c standalone-full.xml
        For Windows:  EAP_HOME\bin\standalone.bat -c standalone-full.xml
2. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing EAP_HOME with the path to your server:

        For Linux: EAP_HOME/bin/jboss-cli.sh --connect --file=disable-mdb-property-substitution.cli
        For Windows: EAP_HOME\bin\jboss-cli.bat --connect --file=disable-mdb-property-substitution.cli
This script removes the system properties and sets the `<annotation-property-replacement>` value to `false` in the `ee` subsystem of the server configuration. You should see the following result when you run the script:

        The batch executed successfully.
        {"outcome" => "success"}


### Restore the Configuration Manually
1. If it is running, stop the JBoss EAP server.
2. Replace the `EAP_HOME/standalone/configuration/standalone-full.xml` file with the back-up copy of the file.


Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a JBoss EAP server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 

_NOTE:_

* Be sure to enable MDB property substitution by running the JBoss CLI commands as described in the section above entitled *Configure the JBoss EAP Server*. Stop the server at the end of that step.
* Within JBoss Developer Studio, be sure to define a server runtime environment that uses the `standalone-full.xml` configuration file.

Debug the Application
------------------------------------

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

    mvn dependency:sources
   


