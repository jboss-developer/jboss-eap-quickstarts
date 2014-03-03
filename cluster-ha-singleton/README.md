cluster-ha-singleton: A SingletonService deployed in a JAR started by SingletonStartup and accessed by an EJB
=============================================================================================================
Author: Wolf-Dieter Fink  
Level: Advanced  
Technologies: EJB, HASingleton, JNDI  
Summary: A SingletonService deployed in a JAR started by SingletonStartup and accessed by an EJB  
Target Product: EAP  
Product Versions: EAP 6.1, EAP 6.2  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

This example demonstrates the deployment of a Service that is wrapped with the SingletonService decorater
to be a cluster wide singleton service.

The example is composed of 2 maven projects with a shared parent. The projects are as follows:

1. `service`: This project contains the Service and the EJB code to instantiate, start and access the service
2. `client` : This project contains a remote ejb client to show the behaviour

The root `pom.xml` builds each of the subprojects in the above order and deploys the archive to the server.

_Note: This quickstart uses two classes, org.jboss.as.server.CurrentServiceContainer and org.jboss.as.clustering.singleton.SingletonService, that are part of the JBoss EAP private API. A public API will become available in the EAP 6.3 release and the private classes will be deprecated, but these classes will be maintained and available for the duration of the EAP 6.x release cycle._


System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.1 or later. 

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.

 
Configure Maven
---------------

You can copy or link to the Maven configuration information in the README file in the root folder of the quickstarts. For example:

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.


Start the JBoss EAP Server with a HA profile
-------------------------

If you run a non HA profile the singleton service will not start correctly. To run the example one instance must be started, to see the singleton behaviour at minimum two instances
should be started.

    Start server one : standalone.sh --server-config=standalone-ha.xml -Djboss.node.name=nodeOne
    Start server two : standalone.sh --server-config=standalone-ha.xml -Djboss.node.name=nodeTwo -Djboss.socket.binding.port-offset=100


Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install jboss-as:deploy

4. This will deploy `service/target/jboss-cluster-ha-singleton-service.jar` to the running instance of the server.
5. Type this command to deploy the archive to the second server (or more) and replace the port, depending on your settings:

        mvn jboss-as:deploy -Djboss-as.port=10099

6. This will deploy `service/target/jboss-cluster-ha-singleton-service.jar` to the running instance of the additional server.
 
7. Check whether the application is deployed on each instance. All instances will have a message:

        INFO  [org.jboss.as.clustering.singleton] (SingletonService lifecycle - 1) JBAS010342: nodeOne/cluster elected as the singleton provider of the jboss.quickstart.ejb.ha.singleton service
        Only nodeOne (or even one instance) will have a message:
        INFO  [org.jboss.as.clustering.singleton] (SingletonService lifecycle - 1) JBAS010340: This node will now operate as the singleton provider of the jboss.quickstart.ejb.ha.singleton service
8. The timer on the started node will log a message every 10sec.

Check the timer
---------------------

1. Open a command prompt and navigate to the root directory of this quickstart.
2. Type the following command to start the client:

        cd client
        mvn exec:exec

3. Check the output. The request to the EJB is running four times and every time it should respond:

        # The timer service is active on node with name = NodeOne
  If you look in the server log files, you will see that each node will process requests
4. Stop the server `nodeOne`. If you look into the server `nodeTwo` log file, you will see the message:

        JBAS010342: nodeTwo/cluster elected as the singleton provider of ..."
  This shows that the singleton service was started. The timer will be started here and log a message every 10 seconds.
5. Repeat step 2. The request is running four times and the message is: 

        # The timer service is active on node with name = NodeTwo
  If you look into the server #2 log file you will see that it process all four requests.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy
        mvn jboss-as:undeploy -Djboss-as.port=10099


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JDBS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 

Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc

------------------------------------
