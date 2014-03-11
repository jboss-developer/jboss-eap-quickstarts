jts: Java Transaction Service - Distributed EJB Transactions Across Multiple Containers 
======================================================================================
Author: Tom Jenkinson  
Level: Intermediate  
Technologies: JTS  
Summary: Uses Java Transaction Service (JTS) to coordinate distributed transactions  
Prerequisites: cmt  
Target Product: EAP  
Product Versions: EAP 6.1, EAP 6.2  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  


What is it?
-----------

This example demonstrates how to perform distributed transactions in an application. A distributed transaction is a set of operations performed by two or more nodes, participating in an activity coordinated as a single entity of work, and fulfilling the properties of an ACID transaction. 

ACID is a set of 4 properties that guarantee the resources are processed in the following manner:

* Atomic - if any part of the transaction fails, all resources remain unchanged. 
* Consistent - the state will be consistent across resources after a commit
* Isolated - the execution of the transaction for each resource is isolated from each others
* Durable - the data will persist after the transaction is committed


The example uses Java Transaction Service (JTS) to propagate a transaction context across two Container-Managed Transaction (CMT) EJBs that, although deployed in separate servers, participate in the same transaction. In this example, one server processes the Customer and Account data and the other server processes the Invoice data.

The code base is essentially the same as the _cmt_ quickstart, however in this case the <code>InvoiceManager</code>
has been separated to a different deployment archive to demonstrate the usage of JTS. You can see the changes in the 
following ways:

1. `cmt/src/main/java/org/jboss/as/quickstarts/cmt/ejb/InvoiceManagerEJB.java` has been moved to `application-component-2/src/main/java/org/jboss/as/quickstarts/cmt/jts/ejb/InvoiceManagerEJB`
2. `cmt/src/main/java/org/jboss/as/quickstarts/cmt/ejb/CustomerManagerEJB.java` has been moved to `jts/application-component-1/src/main/java/org/jboss/as/quickstarts/cmt/jts/ejb/CustomerManagerEJB.java`

The changes to `CustomerManagerEJB` are purely to accommodate the fact that `InvoiceManager` is now distributed.

You will see that the `CustomerManagerEJB` uses the EJB home for the remote EJB, this is expected to connect to remote EJBs. The example expects the EJBs to be deployed onto the same physical machine. This is not a restriction of JTS and the example can easily be converted to run on separate machines by editing the hostname value for the `InvoiceManagerEJB` in `org.jboss.as.quickstarts.cmt.jts.ejb.CustomerManagerEJB`.

A simple MDB has been provided that prints out the messages sent but this is not a transactional MDB and is purely provided for debugging purposes.

After users complete this quickstart, they are invited to run through the following quickstart:

1. _jts-distributed-crash-rec_ - The crash recovery quickstart builds upon the _jts_ quickstart by demonstrating the fault tolerance of JBossAS.


System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.1 or later. 

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.

Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.


Prerequisites
------------------

Developers should be familiar with the concepts introduced in the _cmt_ quickstart.

This quickstart requires the configuration of two servers. The first server must be configured to use the PostgreSQL database. Instructions to install and configure PostgreSQL are below.


Configure the PostgreSQL Database for Use with this Quickstart
--------------------------------------------------

This quickstart requires the PostgreSQL database. Instructions to install and configure PostgreSQL can be found here: [Download and Install PostgreSQL](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_POSTGRESQL.md#download-and-install-postgresql)

_Note_: For the purpose of this quickstart, replace the word `QUICKSTART_DATABASE_NAME` with `jts-quickstart-database` in the PostgreSQL instructions.

Be sure to start the PostgreSQL database. Unless you have set up the database to automatically start as a service, you must repeat the instructions "Start the database server" for your operating system every time you reboot your machine.

Wait until later in these instructions to add the PostgreSQL module and driver configuration to the first JBoss EAP server.


Configure the JBoss EAP Servers
---------------------------

For this example, you will need two instances of the application server, with a subtle startup configuration difference. Application server 2 must be started up with a port offset parameter provided to the startup script as "-Djboss.socket.binding.port-offset=100". 

Since both application servers must be configured in the same way, you must configure the first server and then clone it. After you clone the second server, the first server must be configured for PostgreSQL. 

### Configure the First Server 

You configure the security domain by running JBoss CLI commands. For your convenience, this quickstart batches the commands into a `configure-transactions.cli` script provided in the root directory of this quickstart. 

1. Before you begin, back up your server configuration file
    * If it is running, stop the JBoss EAP server.
    * Backup the file: `JBOSS_HOME/standalone/configuration/standalone-full.xml`
    * After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.
2. Start the JBoss EAP server with the full profile, passing a unique node ID by typing the following command. Be sure to replace `UNIQUE_NODE_ID_1` with a node identifier that is unique to both servers.

        For Linux:  JBOSS_HOME/bin/standalone.sh -c standalone-full.xml -Djboss.tx.node.id=UNIQUE_NODE_ID_1
        For Windows:  JBOSS_HOME\bin\standalone.bat -c standalone-full.xml  -Djboss.tx.node.id=UNIQUE_NODE_ID_1
3. Review the `configure--jts-transactions.cli` file in the root of this quickstart directory. This script configures the server to use jts transaction processing.
4. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing JBOSS_HOME with the path to your server:

        JBOSS_HOME/bin/jboss-cli.sh --connect --file=configure-jts-transactions.cli
 You should see the following result when you run the script:

        The batch executed successfully.
        {"outcome" => "success"}

_NOTE:_ When you have completed testing this quickstart, it is important to [Remove the JTS Configuration from the JBoss EAP Server](#remove-the-jts-configuration-from-the-jboss-eap-server).


### Review the Modified Server Configuration

If you want to review and understand newly added XML configuration, stop the JBoss EAP server and open the  `JBOSS_HOME/standalone/configuration/standalone-full.xml` file. 

1. The orb initializers `transactions` attribute is changed from "spec" to "on" in the  `jacorb` subsystem to enable JTS. A naming root is also added to the subsystem.

        <subsystem xmlns="urn:jboss:domain:jacorb:1.3">
            <orb name="$" socket-binding="jacorb" ssl-socket-binding="jacorb-ssl">
                <initializers security="identity" transactions="on"/>
            </orb>
            <naming root-context="$/Naming/root"/>
        </subsystem>

2. An empty `<jts/>` element is added to the the end of the `transactions` subsystem to enable JTS.

        <subsystem xmlns="urn:jboss:domain:transactions:1.4">
            <core-environment node-identifier="${jboss.tx.node.id}">
                <process-id>
                    <uuid/>
                </process-id>
            </core-environment>
            <recovery-environment socket-binding="txn-recovery-environment" status-socket-binding="txn-status-manager"/>
            <coordinator-environment default-timeout="300"/>
            <jts/>
        </subsystem>
        
_NOTE:_ When you have completed testing this quickstart, it is important to [Remove the JTS Configuration from the JBoss EAP Server](#remove-the-jts-configuration-from-the-jboss-eap-server).
  
### Clone the JBOSS_HOME Directory     

Make a copy of this JBoss EAP directory structure to use for the second server.

### Configure Server1 to use PostgreSQL

Application server 1 must be now configured to use PostgreSQL.

1. Be sure to start the PostgreSQL database. Unless you have set up the database to automatically start as a service, you must repeat the instructions "Start the database server" for your operating system every time you reboot your machine.
2. Follow the instructions to [Add the PostgreSQL Module to the JBoss EAP Server](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_POSTGRESQL.md#add-the-postgresql-module-to-the-jboss-eap-server) to the server 1 install only.
3. Follow the instructions to [Configure the PostgreSQL Driver in the JBoss EAP Server](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_POSTGRESQL.md#configure-the-postgresql-driver-in-the-jboss-eap-server) for the server 1 configuration.


Start the JBoss EAP Servers
-------------------------

Start the the two JBoss EAP servers with the full profile, passing a unique node ID by typing the following command. You must pass a socket binding port offset on the command to start the second server. Be sure to replace `UNIQUE_NODE_ID` with a node identifier that is unique to both servers.

If you are using Linux:

        Server 1: JBOSS_HOME_SERVER_1/bin/standalone.sh -c standalone-full.xml -Djboss.tx.node.id=UNIQUE_NODE_ID_1
        Server 2: JBOSS_HOME_SERVER_2/bin/standalone.sh -c standalone-full.xml -Djboss.tx.node.id=UNIQUE_NODE_ID_2 -Djboss.socket.binding.port-offset=100

If you are using Windows

        Server 1: JBOSS_HOME_SERVER_1\bin\standalone.bat -c standalone-full.xml -Djboss.tx.node.id=UNIQUE_NODE_ID_1
        Server 2: JBOSS_HOME_SERVER_2\bin\standalone.bat -c standalone-full.xml -Djboss.tx.node.id=UNIQUE_NODE_ID_2 -Djboss.socket.binding.port-offset=100


Build and Deploy the Quickstart
-------------------------

Since this quickstart builds two separate components, you can not use the standard *Build and Deploy* commands used by most of the other quickstarts. You must follow these steps to build, deploy, and run this quickstart.


1. Make sure you have started the JBoss EAP server with the PostgreSQL driver
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install jboss-as:deploy

4. This will deploy `application-component-1/target/jboss-jts-application-component-1.war` and `application-component-2/target/jboss-jts-application-component-2.jar` to the running instance of the server.

Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-jts-application-component-1/>.

When you enter a name and click to "Add" that customer, you will see the following in the application server 1 console:
    
    14:31:48,334 WARNING [javax.enterprise.resource.webcontainer.jsf.renderkit] (http-localhost-127.0.0.1-8080-1) Unable to find component with ID name in view.
    14:31:50,457 ERROR [jacorb.orb] (http-localhost-127.0.0.1-8080-1) no adapter activator exists for jts-quickstart&%InvoiceManagerEJBImpl&%home
    14:31:50,767 INFO  [org.jboss.ejb.client] (http-localhost-127.0.0.1-8080-1) JBoss EJB Client version 1.0.5.Final
    14:31:51,430 WARN  [com.arjuna.ats.jts] (RequestProcessor-5) ARJUNA022261: ServerTopLevelAction detected that the transaction was inactive

You will also see the following in application-server-2 console:

    14:31:50,750 INFO  [org.jboss.ejb.client] (RequestProcessor-10) JBoss EJB Client version 1.0.5.Final
    14:31:51,395 INFO  [class org.jboss.as.quickstarts.cmt.jts.mdb.HelloWorldMDB] (Thread-1 (HornetQ-client-global-threads-1567863645)) Received Message: Created invoice for customer named: Tom

The web page will also change and show you the new list of customers.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn package jboss-as:undeploy


Remove the JTS Configuration from the JBoss EAP Server
---------------------------

You must remove the JTS server configuration you did during setup because it interferes with the JTA quickstarts. 

You can modify the server configuration by running the `remove-jts-transactions.cli` script provided in the root directory of this quickstart, by using the JBoss CLI interactively, or by manually editing the configuration file.

### Remove the JTS Server Configuration by Running the JBoss CLI Script

1. Start the JBoss EAP server with the full profile.

        For Linux:  JBOSS_HOME_SERVER_1/bin/standalone.sh -c standalone-full.xml
        For Windows:  JBOSS_HOME_SERVER_1\bin\standalone.bat -c standalone-full.xml
2. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing JBOSS_HOME with the path to your server:

        JBOSS_HOME/bin/jboss-cli.sh --connect --file=remove-jts-transactions.cli 
This script removes the `test` queue from the `messaging` subsystem in the server configuration. You should see the following result when you run the script:

        The batch executed successfully.
        {"outcome" => "success"}


### Remove the JTS Server Configuration using the JBoss CLI Tool

1. Start the JBoss EAP server with the full profile.

        For Linux:  JBOSS_HOME_SERVER_1/bin/standalone.sh -c standalone-full.xml
        For Windows:  JBOSS_HOME_SERVER_1\bin\standalone.bat -c standalone-full.xml
2. To start the JBoss CLI tool, open a new command prompt, navigate to the JBOSS_HOME directory, and type the following:
    
        For Linux: bin/jboss-cli.sh --connect
        For Windows: bin\jboss-cli.bat --connect
3. At the prompt, type the following:

        /subsystem=jacorb/:write-attribute(name=transactions,value=spec)
        /subsystem=jacorb/:undefine-attribute(name=name)
        /subsystem=transactions/:undefine-attribute(name=jts)
        /subsystem=transactions/:undefine-attribute(name=node-identifier)
        :reload

### Remove the JTS Server Configuration Manually

1. Stop the server.
2. If you backed up the JBOSS_HOME/standalone/configuration/standalone-full.xml,simply replace the edited configuration file with the backup copy.
3. If you did not make a backup copy, open the file JBOSS_HOME/standalone/configuration/standalone-full.xml and disable JTS as follows:

    * Find the orb subsystem and change the configuration back to:

            <subsystem xmlns="urn:jboss:domain:jacorb:1.2">
                <orb>
                    <initializers security="on" transactions="spec"/>
                </orb>
            </subsystem>
    * Find the transaction subsystem and remove the `<jts/>` element:

            <subsystem xmlns="urn:jboss:domain:transactions:1.2">
                <!-- REMOVE node-identifier ATTRIBUTE FROM core-environment ELEMENT -->
                <!-- LEAVE EXISTING CONFIG AND REMOVE THE </jts> -->
            </subsystem>

