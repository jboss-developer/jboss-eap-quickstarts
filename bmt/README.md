bmt: Bean Managed Transactions with JPA and JTA
=================================================================================
Author: Mike Musgrove  
Level: Intermediate  
Technologies: EJB, BMT  
Summary: The `bmt` quickstart demonstrates Bean-Managed Transactions (BMT), showing how to manually manage transaction demarcation while accessing JPA entities.  
Target Product: JBoss EAP  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

The `bmt` quickstart demonstrates how to manually manage transaction demarcation while accessing JPA entities in Red Hat JBoss Enterprise Application Platform.

On occasion, the application developer requires finer grained control over the lifecycle of JTA transactions and JPA Entity Managers than the defaults provided by the Java EE container. This example shows how the developer can override these defaults and take control of aspects of the lifecycle of JPA and transactions.

When you run this example, you will be provided with a `Use bean managed Entity Managers` checkbox.
* If you check the checkbox, it shows the developer responsibilities when injecting an Entity Manager into a managed (stateless) bean.
* If you uncheck the checkbox, shows the developer responsibilities when using JPA and transactions with an unmanaged component.

JBoss EAP ships with H2, an in-memory database written in Java. This example shows how to transactionally insert key value pairs into the H2 database and demonstrates the requirements on the developer with respect to the JPA Entity Manager.

_Note: This quickstart uses the H2 database included with Red Hat JBoss Enterprise Application Platform 6. It is a lightweight, relational example datasource that is used for examples only. It is not robust or scalable, is not supported, and should NOT be used in a production environment!_

_Note: This quickstart uses a `*-ds.xml` datasource configuration file for convenience and ease of database configuration. These files are deprecated in JBoss EAP 6.4 and should not be used in a production environment. Instead, you should configure the datasource using the Management CLI or Management Console. Datasource configuration is documented in the [Administration and Configuration Guide](https://access.redhat.com/documentation/en-US/JBoss_Enterprise_Application_Platform/) for Red Hat JBoss Enterprise Application Platform._

_NOTE: A Java EE container is designed with robustness in mind, so you should carefully analyze the scalabiltiy, concurrency, and performance needs of your application before taking advantage of these techniques in your own applications._


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


Start the JBoss EAP Server
-------------------------

1. Open a command prompt and navigate to the root of the JBoss EAP directory.
2. The following shows the command line to start the server:

        For Linux:   EAP_HOME/bin/standalone.sh
        For Windows: EAP_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/BUILD_AND_DEPLOY.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install jboss-as:deploy

4. This will deploy `target/jboss-bmt.war` to the running instance of the server.
 

Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-bmt/>.

You will be presented with a simple form for adding key/value pairs and a checkbox to indicate whether the updates should be executed using an unmanaged component. Effectively this will run the transaction and JPA updates in the servlet, not session beans. If the box is checked then the updates will be executed within a session bean method.

1. To list all pairs leave the key input box empty. 
2. To add or update the value of a key fill in the key and value input boxes. 
3. Press the submit button to see the results.

Server Log: Expected warnings and errors
-----------------------------------

_Note:_ You will see the following warnings in the server log. You can ignore these warnings.

    JBAS010489: -ds.xml file deployments are deprecated. Support may be removed in a future version.

    HHH000431: Unable to determine H2 database version, certain features may not work


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a JBoss EAP server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 


Debug the Application
------------------------------------

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

        mvn dependency:sources

