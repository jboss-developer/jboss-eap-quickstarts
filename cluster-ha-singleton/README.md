cluster-ha-singleton: A SingletonService Started by a SingletonStartup
=============================================================================================================
Author: Wolf-Dieter Fink  
Level: Advanced  
Technologies: EJB, HASingleton, JNDI  
Summary: The `cluster-ha-singleton` quickstart deploys a Service, wrapped with the SingletonService decorator, and used as a cluster-wide singleton service.  
Target Product: JBoss EAP  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

The `cluster-ha-singleton` quickstart demonstrates the deployment of a Service that is wrapped with the 
SingletonService decorator and used as a cluster-wide singleton service in Red Hat JBoss Enterprise Application Platform.
The service activates a scheduled timer, which is started only once in the cluster.

The example is composed of a Maven subproject and a parent project. The projects are as follows:

1. `service`: This subproject contains the Service and the EJB code to instantiate, start, and access the service.
2. The root parent `pom.xml` builds the `service` subproject and deploys the archive to the server.

_Note: This quickstart uses a class, org.jboss.as.clustering.singleton.SingletonService, that is part of the JBoss EAP private API. A public API will become available in a future EAP release and the private classes will be deprecated, but these classes will be maintained and available for the duration of the EAP 6.x release cycle._


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


Clone the EAP_HOME Directory
----------------------------

While you can run this example starting only one instance of the server, if you want to see the singleton behavior, you must start at least two instances of the server. Make a copy of the JBoss EAP directory structure to use for the second server.


Start the JBoss EAP Server with a HA profile
-------------------------

_Note: You must start the server using the HA profile or the singleton service will not start correctly._

Start the the two JBoss EAP servers with the HA profile, passing a unique node ID by typing the following commands. You must pass a socket binding port offset on the command to start the second server. 

If you are using Linux:

        Server 1: EAP_HOME_1/bin/standalone.sh --server-config=standalone-ha.xml -Djboss.node.name=Node1
        Server 2: EAP_HOME_2/bin/standalone.sh --server-config=standalone-ha.xml -Djboss.node.name=Node2 -Djboss.socket.binding.port-offset=100

If you are using Windows

        Server 1: EAP_HOME_1\bin\standalone.bat --server-config=standalone-ha.xml -Djboss.node.name=Node1
        Server 2: EAP_HOME_2\bin\standalone.bat --server-config=standalone-ha.xml -Djboss.node.name=Node2 -Djboss.socket.binding.port-offset=100

_Note: If you want to test with more than two servers, you can start additional servers by specifying a unique node name and unique port offset for each one._

Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/BUILD_AND_DEPLOY.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss EAP servers as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install jboss-as:deploy

4. This deploys `service/target/jboss-cluster-ha-singleton-service.jar` to the running instance of the first server.
5. Since default socket binding port is `9999` and the second server runs at a port offset of `100`, you must pass port `10099` (9999 + 100) as an argument when you deploy to the second server. Type this command to deploy the archive to the second server. 

        mvn jboss-as:deploy -Djboss-as.port=10099
    
    If the second server is on a different host, you must also pass an argument for the host name as follows:
    
        mvn jboss-as:deploy [-Djboss-as.hostname=OTHERHOST] -Djboss-as.port=10099
    _Note: If you test with more than two servers, repeat the command, replacing the unique node name and unique port offset for each server._
6. This deploys `service/target/jboss-cluster-ha-singleton-service.jar` to the running instance of the additional server.
 
7. To verify the application deployed to each server instance, check the server logs. All instances should have the following message:

        INFO  [org.jboss.as.clustering.singleton] (SingletonService lifecycle - 1) JBAS010342: Node1/cluster elected as the singleton provider of the jboss.quickstart.ejb.ha.singleton service
        
   Only `Node1` will have this message:
   
        INFO  [org.jboss.as.clustering.singleton] (SingletonService lifecycle - 1) JBAS010340: This node will now operate as the singleton provider of the jboss.quickstart.ejb.ha.singleton service
    
    You also see the following warning in the server logs. As mentioned above, this quickstart accesses the class `org.jboss.as.clustering.singleton.SingletonService`, which is part of the JBoss EAP private API. This server log message provides an additional warning about the use of the private API.
   
        WARN  [org.jboss.as.dependency.private] (MSC service thread 1-11) JBAS018567: Deployment "deployment.jboss-cluster-ha-singleton-service.jar" is using a private module ("org.jboss.as.clustering.singleton:main") which may be changed or removed in future versions without notice.


8. The timer on the started node will log a message every 10 seconds. If you stop the `Node1` server, you see messages in the `Node2` server console indicating it is now the singleton provider.

9. If you prefer to use a special node, the election-policy can be used.
   In the example, the node with the name `Node1` will be used as master, if it is available.
   If it has failed or shutdown, any other node will be used.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type the following commands to undeploy the archives:

        mvn jboss-as:undeploy
        mvn jboss-as:undeploy [-Djboss-as.hostname=OTHERHOST] -Djboss-as.port=10099

_Note: You may see the following exception when you undeploy the archive from the second server. You can ignore this message as it is expected._

        ERROR [org.jboss.as.ejb3.invocation] (MSC service thread 1-11) JBAS014134: EJB Invocation failed on component SchedulerBean for method public abstract void org.jboss.as.quickstarts.cluster.hasingleton.service.ejb.Scheduler.stop(): org.jboss.as.ejb3.component.EJBComponentUnavailableException: JBAS014559: Invocation cannot proceed as component is shutting down


Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a JBoss EAP server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts).

This quickstart is more complex than the others. It requires that you configure and run two instances of the JBoss EAP server, so it deploys and runs differently in JBoss Developer Studio than the other quickstarts.

1. Be sure to import the quickstart into JBoss Developer Studio. 
2. Follow the instructions above to [Clone the EAP_HOME Directory](#clone-the-eaphome-directory).
3. Configure the first server instance in JBoss Developer Studio.
   * In the `Server` tab, right-click and choose `New` --> `Server`.
   * For the `Server name`, enter "Node1" and click `Next`.
   * In the `Create a new Server Adapter` dialog, choose `Create a new runtime (next page)` and click `Next`.
   * In the `JBoss Runtime` dialog, enter the following information and then click `Next`.
   
            Name: Node1
            Home Directory: (Browse to the directory for the first server and select it)
            Execution Environment: (Choose your runtime JRE if not correct)
            Configuration base directory: (This should already point to your server configuration directory)
            Configuration file: (Browse and choose the `standalone-ha.xml` file)
   * In the `Add and Remove` dialog, add the `jboss-cluster-ha-singleton-service` to the `Configured` list and click `Finished`.
   * In the `Server` tab, double-click on `Node1` to open it. 
   * Click `Open launch configuration` and at the end of the `VM Arguments`, paste "-Djboss.node.name=Node1" and click `OK`.
   
4. Configure the second server instance in JBoss Developer Studio.
   * In the `Server` tab, right-click and choose `New` --> `Server`.
   * For the `Server name`, enter "Node2" and click `Next`.
   * In the `Create a new Server Adapter` dialog, choose `Create a new runtime (next page)` and click `Next`.
   * In the `JBoss Runtime` dialog, enter the following information and then click `Next`.
   
            Name: Node2
            Home Directory: (Browse to the cloned directory for the second server and select it)
            Execution Environment: (Choose your runtime JRE if not correct)
            Configuration base directory: (This should already point to your cloned server configuration directory)
            Configuration file: (Browse and choose the `standalone-ha.xml` file)
   * In the `Add and Remove` dialog, add the `jboss-cluster-ha-singleton-service` to the `Configured` list and click `Finished`.
   * In the `Server` tab, double-click on `Node2` to open the `Overview` page. 
   * Click `Open launch configuration` and at the end of the `VM Arguments`, paste "-Djboss.node.name=Node2 -Djboss.socket.binding.port-offset=100" and click `OK`.
   * Still in the `Overview` page for `Node2`, under `Server Ports`, uncheck the `Detect from Local Runtime` next to `Port Offset` and enter "100". Save the changes using the menu `File --> Save`

5. To deploy the cluster-ha-singleton service to `Node 1`, right-click on the `jboss-cluster-ha-singleton-service` project, choose `Run As` --> `Run on Server`, choose `Node1` and click `Finish`. Note the messages in the `Node1` server console indicate it is the singleton provider of the service.
   
            JBAS010340: This node will now operate as the singleton provider of the jboss.quickstart.ha.singleton.timer service
            JBAS015961: Http management interface listening on http://127.0.0.1:9990/management
            INFO  [org.jboss.as.quickstarts.cluster.hasingleton.service.ejb.SchedulerBean] (EJB default - 1) HASingletonTimer: Info=HASingleton timer @Node1 Mon Jan 19 09:02:36 EST 2015
            INFO  [org.jboss.as.quickstarts.cluster.hasingleton.service.ejb.SchedulerBean] (EJB default - 2) HASingletonTimer: Info=HASingleton timer @Node1 Mon Jan 19 09:02:36 EST 2015

6. To deploy the cluster-ha-singleton service to `Node 2`, right-click on the `jboss-cluster-ha-singleton-service` project, choose `Run As` --> `Run on Server`, choose `Node2` and click `Finish`. Note the messages in the `Node2` server console indicate `Node1` is the singleton provider of the service and `Node1` continues to provide the service.
   
            JBAS010342: Node1/singleton elected as the singleton provider of the jboss.quickstart.ha.singleton.timer service
            JBAS015961: Http management interface listening on http://127.0.0.1:10090/management   

7. Stop the `Node1` server and note the following message in the `Node2` server console indicating it is now the singleton provider.

        JBAS010340: This node will now operate as the singleton provider of the jboss.quickstart.ha.singleton.timer service
        INFO  [org.jboss.as.quickstarts.cluster.hasingleton.service.ejb.SchedulerBean] (EJB default - 1) HASingletonTimer: Info=HASingleton timer @Node2 Mon Jan 19 09:05:17 EST 2015
        INFO  [org.jboss.as.quickstarts.cluster.hasingleton.service.ejb.SchedulerBean] (EJB default - 2) HASingletonTimer: Info=HASingleton timer @Node2 Mon Jan 19 09:05:17 EST 2015
        INFO  [org.jboss.as.quickstarts.cluster.hasingleton.service.ejb.SchedulerBean] (EJB default - 3) HASingletonTimer: Info=HASingleton timer @Node2 Mon Jan 19 09:05:17 EST 2015


Debug the Application
------------------------------------

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

    mvn dependency:sources
   

------------------------------------
