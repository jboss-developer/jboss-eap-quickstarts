jboss-as-helloworld-webservice Example
===============================

What is it?
-----------

This quickstart demonstrates to build a basic JAX-WS compatible web service based on EJB 3.1 Stateless Session Bean in JBoss AS 7.1.0.

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven
3.0 or better.

The application this project produces is designed to be run on a JBoss AS 7.1.0 or EAP 6. 
 
NOTE: The artifacts will come from the JBoss Community Maven repository, a superset of the Maven central repository.

With the prerequisites out of the way, you're ready to build and deploy.

Deploying the application
-------------------------

First of all you need to enable the "admin" user from $JBOSS_HOME/standalone/configuration/mgmt-users.properties file or use the script $JBOSS_HOME/bin/add-user.sh|.bat to create a user, and then start JBoss AS 7.1.0. by running this script
  
    $JBOSS_HOME/bin/standalone.sh
  
or if you are using windows
 
    $JBOSS_HOME/bin/standalone.bat

You can now deploy the artifact to JBoss AS by executing the following command:

    mvn jboss-as:deploy

This will deploy `target/jboss-as-helloworld-webservice.war`.
 
The wsdl of the application can be look at the following URL <http://localhost:8080/jboss-as-helloworld-webservice/CalculatorService/CalculatorBean?wsdl>.


To run the application, use the following Maven goal:

    mvn test

Note: you can also run the sample standalone application "Client" from your Eclipse IDE 
After executing the maven goal or from eclipse, the result can look like this:

[INFO] 
[INFO] --- exec-maven-plugin:1.2.1:java (default) @ jboss-as-helloworld-webservice ---
Jan 4, 2012 6:31:03 PM com.sun.xml.internal.ws.model.RuntimeModeler getRequestWrapperClass
INFO: Dynamically creating request wrapper Class org.jboss.as.quickstarts.webservice.jaxws.Add
Jan 4, 2012 6:31:03 PM com.sun.xml.internal.ws.model.RuntimeModeler getResponseWrapperClass
INFO: Dynamically creating response wrapper bean Class org.jboss.as.quickstarts.webservice.jaxws.AddResponse
Jan 4, 2012 6:31:03 PM com.sun.xml.internal.ws.model.RuntimeModeler getRequestWrapperClass
INFO: Dynamically creating request wrapper Class org.jboss.as.quickstarts.webservice.jaxws.Subtract
Jan 4, 2012 6:31:03 PM com.sun.xml.internal.ws.model.RuntimeModeler getResponseWrapperClass
INFO: Dynamically creating response wrapper bean Class org.jboss.as.quickstarts.webservice.jaxws.SubtractResponse

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
ADD:  5 + 2 = 7
DIFF: 1 - 1 = 0
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

To undeploy from JBoss AS, run this command:

    mvn jboss-as:undeploy

You can also start JBoss AS 7 and deploy the project using Eclipse. See the JBoss AS 7
Getting Started Guide for Developers for more information.
