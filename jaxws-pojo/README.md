jaxws-pojo: An POJO JAX-WS Web Service
==================================================
Author: R Searls
Level: Beginner  
Technologies: JAX-WS  
Summary: The `jaxws-pojo` quickstart is a working example of the web service discussed in section,
`About JAX-WS Web Service Endpoints` of the `JBoss Development Application Platform Development Guide`.
Target Product: EAP  
Product Versions: EAP 6.1, EAP 6.2, EAP 6.3, EAP 6.4  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

The `jaxws-pojo` quickstart demonstrates the use of *JAX-WS* in Red Hat JBoss Enterprise Application Platform as a simple POJO web service application.

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.1 or later.

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.


Start the JBoss EAP Server
----------------------         

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

4. This will deploy `service/target/jboss-jaxws-pojo-service.war` to the running instance of the server.

Access the application 
---------------------

You can check that the Web Service is running and deployed correctly by accessing the following URL: <http://localhost:8080/jaxws-samples-endpoint-pojo/JSEBean01?wsdl>. This URL will display the deployed WSDL endpoint for the Web Service.

Run the Client
--------------
1. Make sure the service deployed properly.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to run the client.

     java -jar client/target/jboss-jaxws-pojo-client.jar   org.jboss.quickstarts.ws.client.Client

Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy