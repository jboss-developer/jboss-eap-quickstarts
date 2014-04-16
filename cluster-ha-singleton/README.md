cluster-ha-singleton: A SingletonService deployed in a JAR started by SingletonStartup and accessed by an EJB
=============================================================================================================
Author: Wolf-Dieter Fink  
Level: Advanced  
Technologies: EJB, HASingleton, JNDI  
Summary: A SingletonService deployed in a JAR started by SingletonStartup and accessed by an EJB  
Target Product: EAP  
Product Versions: EAP 6.1, EAP 6.2, EAP 6.3  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

This example demonstrates the deployment of a Service that is wrapped with the SingletonService decorator
and used as a cluster-wide singleton service.
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

You can copy or link to the Maven configuration information in the README file in the root folder of the quickstarts. For example:

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.


Clone the EAP_HOME Directory
----------------------------

While you can run this example starting only one instance of the server, if you want to see the singleton behavior, you must start at least two instances of the server. Make a copy of the JBoss EAP directory structure to use for the second server.


Start the JBoss EAP Server with a HA profile
-------------------------

_Note: You must start the server using the HA profile or the singleton service will not start correctly._

Start the the two JBoss EAP servers with the HA profile, passing a unique node ID by typing the following commands. You must pass a socket binding port offset on the command to start the second server. 

If you are using Linux:

        Server 1: EAP_HOME_1/bin/standalone.sh --server-config=standalone-ha.xml -Djboss.node.name=nodeOne
        Server 2: EAP_HOME_2/bin/standalone.sh --server-config=standalone-ha.xml -Djboss.node.name=nodeTwo -Djboss.socket.binding.port-offset=100

If you are using Windows

        Server 1: EAP_HOME_1\bin\standalone.bat --server-config=standalone-ha.xml -Djboss.node.name=nodeOne
        Server 2: EAP_HOME_2\bin\standalone.bat --server-config=standalone-ha.xml -Djboss.node.name=nodeTwo -Djboss.socket.binding.port-offset=100

_Note: If you want to test with more than two servers, you can start additional servers by specifying a unique node name and unique port offset for each one._

Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

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
 
7. To verify the application deployed to each server instance, check the server logs. All instances should have the following messages:

        INFO  [org.jboss.as.clustering.singleton] (SingletonService lifecycle - 1) JBAS010342: nodeOne/cluster elected as the singleton provider of the jboss.quickstart.ejb.ha.singleton service
        Only nodeOne (or even one instance) will have a message:
        INFO  [org.jboss.as.clustering.singleton] (SingletonService lifecycle - 1) JBAS010340: This node will now operate as the singleton provider of the jboss.quickstart.ejb.ha.singleton service
        
    You also see the following warning in the server logs. As mentioned above, this quickstart accesses the class `org.jboss.as.clustering.singleton.SingletonService`, which is part of the JBoss EAP private API. This server log message provides an additional warning about the use of the private API.
   
        WARN  [org.jboss.as.dependency.private] (MSC service thread 1-11) JBAS018567: Deployment "deployment.jboss-cluster-ha-singleton-service.jar" is using a private module ("org.jboss.as.clustering.singleton:main") which may be changed or removed in future versions without notice.


8. The timer on the started node will log a message every 10 seconds.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type the following commands to undeploy the archives:

        mvn jboss-as:undeploy
        mvn jboss-as:undeploy [-Djboss-as.hostname=OTHERHOST] -Djboss-as.port=10099

_Note: You may see the following exception when you undeploy the archive from the second server. You can ignore this message as it is expected._

        ERROR [org.jboss.as.ejb3.invocation] (MSC service thread 1-11) JBAS014134: EJB Invocation failed on component SchedulerBean for method public abstract void org.jboss.as.quickstarts.cluster.hasingleton.service.ejb.Scheduler.stop(): org.jboss.as.ejb3.component.EJBComponentUnavailableException: JBAS014559: Invocation cannot proceed as component is shutting down


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JDBS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 

Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc

------------------------------------
