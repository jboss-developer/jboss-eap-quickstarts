hornetq-clustering: HornetQ Demonstrating using Clustering
============================================================
Author: Jess Sightler
Level: Intermediate
Technologies: JMS, MDB, HornetQ
Summary: Demonstrates the use of HornetQ Clustering
Prerequisites: helloworld-mdb
Target Product: EAP
Product Versions: EAP 6.1, EAP 6.2
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>
What is it?
-----------

This example demonstrates the use of clustering with HornetQ. It uses the `helloworld-mdb` quickstart for its tests, so there is no code associated with this quickstart. Instructions are provided to run the quickstart on either a standalone server or in a managed domain.

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.1 or later. 

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.
 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.

Prerequisites
---------------

IMPORTANT: This quickstart depends on the deployment of the `helloworld-mdb` quickstart WAR for its tests. Before you continue, you must build the `helloworld-mdb` quickstart WAR.

Open a command line and navigate to the root directory of the helloworld-mdb quickstart.
Type this command to build the WAR archive:

        mvn clean install

See the helloworld-mdb [README](../helloworld-mdb/README.md) for further information about this quickstart.

Configure and Start the JBoss server
---------------

You can choose to deploy and run this quickstart in a managed domain or on a standalone server. The sections below describe how to configure and start the server for both modes. 

_NOTE - Before you begin:_

1. If it is running, stop the JBoss server.

2. If you plan to test using a standalone server, backup the file:

        $JBOSS_HOME/standalone/configuration/standalone-full.xml


3. If you plan to test using a managed domain, backup the following files:

        $JBOSS_HOME/domain/configuration/domain.xml
        $JBOSS_HOME/domain/configuration/host.xml

After you have completed testing this quickstart, you can replace these files to restore the server to its original configuration.

You can configure the server by running the install-domain.cli script provided in the root directory of this quickstart, by using the JBoss CLI interactively, or by manually editing the configuration file.

### Configure and Start the JBoss Server in Domain Mode

#### Start the server in domain mode.
1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server in domain mode:

        For Linux:   JBOSS_HOME/bin/domain.sh
        For Windows: JBOSS_HOME\bin\domain.bat


#### Configure the Domain Server and Deploy the Quickstart Using the JBoss CLI
1. Review the `install-domain.cli` file in the root of this quickstart directory. This script creates the server group and servers and
configures HornetQ Clustering for testing this quickstart. You will note it does the following:
    * Stops the servers
    * Creates a server-group to test HornetQ Clustering
    * Adds 2 servers to the server-group
    * Configures HornetQ clustering in the full-ha profile
    * Deploys the `helloworld-mdb.war` archive
    * Restarts the servers.
    
    _NOTE: If your `helloworld-mdb` quickstart is not located at the same level in the file structure as this quickstart, you
    must modify its path in this script. Find the 'NOTE:' in the file for instructions._

2. Open a command line, navigate to the root directory of this quickstart, and run the following command to run the script:

        JBOSS_HOME/bin/jboss-cli.sh --connect --file=install-domain.cli
        
   You should see "outcome" => "success" for all of the commands.


### Configure and Start the JBoss Server in Standalone Mode

If you choose to use standalone servers rather than domain mode, you will need two instances of the application server. Application
server 2 must be started with a port offset parameter provided to the startup script as "-Djboss.socket.binding.port-offset=100". 

Since both application servers must be configured in the same way, you must configure the first server and then clone it.

#### Start the server in standalone mode using the full-ha profile.

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the full-ha profile. This profile supports clustering/HA

        For Linux:   JBOSS_HOME/bin/standalone.sh -c standalone-full-ha.xml
        For Windows: JBOSS_HOME\bin\standalone.bat -c standalone-full-ha.xml


#### Configure the Standalone Server and Deploy the Quickstart Using the JBoss CLI

1. Review the `install-standalone.cli` file in the root of this quickstart directory. This script configures clustering for a standalone server. You will note it does the following:
    * Enables clustering and sets a cluster password
    * Enables clustering in the RemoteConnectionFactory
    * Deploys the `helloworld-mdb.war` archive
    * Reloads the server configuration
    
    _NOTE: If your `helloworld-mdb` quickstart is not located at the same level in the file structure as this quickstart, you
    must modify its path in this script. Find the 'NOTE:' in the file for instructions._
2. Open a command line, navigate to the root directory of this quickstart, and run the following command to run the script:

        JBOSS_HOME/bin/jboss-cli.sh --connect --file=install-standalone.cli
        
   You should see "outcome" => "success" for all of the commands.

#### Clone the JBOSS_HOME Directory     

After you have successfully configured the server, make a copy of this JBoss directory structure to use for the second server.

#### Start the JBoss EAP Standalone Servers with the Full Profile

If you are using Linux:

        Server 1: JBOSS_HOME_SERVER_1/bin/standalone.sh -c standalone-full.xml
        Server 2: JBOSS_HOME_SERVER_2/bin/standalone.sh -c standalone-full.xml -Djboss.socket.binding.port-offset=100

If you are using Windows:

        Server 1: JBOSS_HOME_SERVER_1\bin\standalone.bat -c standalone-full.xml
        Server 2: JBOSS_HOME_SERVER_2\bin\standalone.bat -c standalone-full.xml -Djboss.socket.binding.port-offset=100

Access the application 
---------------------

The application will be running at the following URL: <http://localhost:9080/jboss-helloworld-mdb/HelloWorldMDBServletClient>. 

It will send some messages to the queue. 

To send messages to the topic, use the following URL: <http://localhost:9080/jboss-helloworld-mdb/HelloWorldMDBServletClient?topic>

Investigate the Server Console Output
-------------------------

Look at the JBoss Application Server console or Server log and you should see log messages like the following:

        [Server:jdf-hornetqcluster-node1] 16:34:41,165 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-8 (HornetQ-client-global-threads-1067469862)) Received Message from queue: This is message 1
        [Server:jdf-hornetqcluster-node1] 16:34:41,274 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-8 (HornetQ-client-global-threads-1067469862)) Received Message from queue: This is message 3
        [Server:jdf-hornetqcluster-node1] 16:34:41,323 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-6 (HornetQ-client-global-threads-1067469862)) Received Message from queue: This is message 5
        [Server:jdf-hornetqcluster-node2] 16:34:41,324 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-8 (HornetQ-client-global-threads-1771031398)) Received Message from queue: This is message 2
        [Server:jdf-hornetqcluster-node2] 16:34:41,330 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-7 (HornetQ-client-global-threads-1771031398)) Received Message from queue: This is message 4

Note that the logging indicates messages have arrived from both node 1 (jdf-hornetqcluster-node1) as well as node 2 (jdf-hornetqcluster-node2).

Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of the helloworld-mdb quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy

Remove the Server Configuration
--------------------

1. Stop the JBoss server.
2. If you were running in standalone mode, copy the backed up standalone-full.xml file into the $JBOSS_HOME/domain/configuration/ directory.
3. If you were running in a managed domain, copy the backed up domain.xml and host.xml configuration files into the $JBOSS_HOME/domain/configuration/ directory.


