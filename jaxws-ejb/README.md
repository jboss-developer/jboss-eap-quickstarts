jaxws-ejb: An EJB JAX-WS Web Service
==================================================
Author: R Searls  
Level: Beginner  
Technologies: JAX-WS  
Summary: The `jaxws-ejb` quickstart is a working example of the web service endpoint created from an EJB.  
Target Product: JBoss EAP  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

The `jaxws-ejb` quickstart demonstrates the use of *JAX-WS* in Red Hat JBoss Enterprise Application Platform as a simple EJB web service application.

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 7 or later. 

All you need to build this project is Java 8.0 (Java SDK 1.8) or later and Maven 3.1.1 or later. See [Configure Maven for JBoss EAP 7](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


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

4. This will deploy `service/target/jboss-jaxws-ejb-service.war` to the running instance of the server.

Access the application 
---------------------

You can check that the Web Service is running and deployed correctly by accessing the following URL: <http://localhost:8080/jboss-jaxws-ejb-endpoint/EJB3Bean01?wsdl>. This URL will display the deployed WSDL endpoint for the Web Service.

Run the Client
--------------
1. Make sure the service deployed properly.

2. Open a command prompt and navigate into the client directory of this quickstart.

        cd client/
3. Type this command to run the client.
     
        mvn exec:java    
4. You should see the following output in the client console.
   
        EJB3Bean01 returning: ejbClient calling

Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfy:undeploy
