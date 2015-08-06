jaxws-retail: A Retail JAX-WS Web Service
=========================================
Author: R Searls  
Level: Beginner  
Technologies: JAX-WS  
Summary: The `jaxws-retail` quickstart is a working example of a simple web service endpoint.
Target Product: JBoss EAP  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

The `jaxws-retail` quickstart demonstrates the use of *JAX-WS* in Red Hat JBoss Enterprise Application Platform as a simple profile management application.

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 7 or later. 

All you need to build this project is Java 8.0 (Java SDK 1.8) or later and Maven 3.1.1 or later. See [Configure Maven for JBoss EAP 7](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.

To build this sample with Java SDK 1.8, file jaxws-retail/jaxp.properties must be installed
in ${JDK-8-PATH}/jre/lib/jaxp.properties.  (see http://docs.oracle.com/javase/7/docs/api/javax/xml/XMLConstants.html#ACCESS_EXTERNAL_SCHEMA)


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

        mvn clean install wildfy:deploy

4. This will deploy `service/target/jboss-jaxws-retail-service.war` to the running instance of the server.

Access the application 
---------------------

You can check that the Web Service is running and deployed correctly by accessing the following URL: <http://localhost:8080/jboss-jaxws-retail/ProfileMgmtService/ProfileMgmt?wsdl>. This URL will display the deployed WSDL endpoint for the Web Service.

Run the Client
--------------
1. Make sure the service deployed properly.

2. Open a command prompt and navigate into the client directory of this quickstart.

     cd client/
3. Type this command to run the client.

     mvn exec:java
     
4. You should see the following output in the client console.
   
     Jay Boss's discount is 10.00


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfy:undeploy
