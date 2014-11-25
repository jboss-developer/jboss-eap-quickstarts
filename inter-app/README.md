inter-app: Communicate Between Two Applications Using EJB and CDI 
==============================================================================
Author: Pete Muir  
Level: Advanced  
Technologies: EJB, CDI, JSF  
Summary: The `inter-app` quickstart shows you how to use a shared API JAR and an EJB to provide inter-application communication between two WAR deployments.  
Target Product: EAP  
Product Versions: EAP 6.1, EAP 6.2, EAP 6.3, EAP 6.4  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

The `inter-app` quickstart shows you how to easily communicate between two modular deployments to Red Hat JBoss Enterprise Application Platform. Two WARs, with a shared API JAR, are deployed to the application server. EJB is used to provide inter-application communication, with EJB beans alised to CDI beans, making the inter-application communication transparent to clients of the bean.

CDI only provides intra-applicaion injection (i.e within a top level deployment, EAR, WAR, JAR etc). This improves performance of the application server, as to satisfy an injection point all possible candidates have to be scanned / analyzed. If inter-app injection was supported by CDI, performance would scale according to the number of deployments you have (the more deployments in the running system, the slower the deployment). Java EE injection uses unique JNDI names for the wiring, so each injection point is O(1). The approach shown here combines the two approaches such that you limit the name based wiring to one location in your code, and the main consumers of components can use CDI injection to reference these name wired components. For the name approach to work though, you still need to publish instances, and EJB singletons allow you to do that with just one extra annotation.


In all, the project has three modules:

* `jboss-inter-app-shared.jar` - this module contains the interfaces which define the contract between the beans exposed by the WARs. It is deployed as a module
* `jboss-inter-app-A.war` - the first WAR, whiches exposes an EJB singleton, and a simple UI that allows you to read the value set on the bean in appB
* `jboss-inter-app-B.war` - the second WAR, whiches exposes an EJB singleton, and a simple UI that allows you to read the value set on the bean in appA

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.1 or later. 

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.

Start the JBoss EAP Server
-------------------------

1. Open a command prompt and navigate to the root of the JBoss EAP directory.
2. The following shows the command line to start the server:

        For Linux:   EAP_HOME/bin/standalone.sh
        For Windows: EAP_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install jboss-as:deploy
4. This will deploy `shared/target/jboss-inter-app-shared.jar`, `appA/target/jboss-inter-app-A.war` and `appB/target/jboss-inter-app-B.war` to the running instance of the server.

Access the application
---------------------


Access the running application in a browser at the following URLs:

* <http://localhost:8080/jboss-inter-app-A>
* <http://localhost:8080/jboss-inter-app-B>

You are presented with a form that allows you to set the value on the bean in the other application, as well as display of the value on this application's bean. Enter a new value and press "Update and Send!" to update the value on the other application. Do the same on the other application, and hit the button again on the first application. You should see the values shared between the applications.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn package jboss-as:undeploy


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 

Debug the Application
------------------------------------

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

    mvn dependency:sources
   

------------------------------------

