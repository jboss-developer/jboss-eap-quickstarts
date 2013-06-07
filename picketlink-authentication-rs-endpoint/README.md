picketlink-authentication-rs-endpoint: PicketLink JAX-RS Authentication Endpoint
===============================
Author: Pedro Igor
Level: Beginner
Technologies: CDI, PicketLink, JAX-RS
Summary: Basic example that demonstrates how to create a JAX-RS Authentication Endpoint using PicketLink
Target Product: EAP
Source: <https://github.com/jboss-jdf/jboss-as-quickstart/>


What is it?
-----------

This example demonstrates the use of *CDI 1.0* and *PicketLink* in *JBoss Enterprise Application Platform 6* or *JBoss AS 7*.

You'll learn from this quickstart how to use PicketLink to create a JAX-RS Authentication Endpoint.

This application provides an JAX-RS Authentication Endpoint supporting different credentials types. You can authenticate
using a username/password combination or a pre-defined token. This first maps to a JSON format and the last is just a
simple string which represents the token.

The endpoints provides a single entry point for all authentication requests (eg.: /authenticate) returning a JSON payload
when the authentication runs successfully.

Each credential type maps to a specific content type. When an authentication request arrives, it is dispatched to a specific
method that knows how to handle a specific credential. If the request was sent using an invalid/unsupported content type,
the server returns a HTTP Status 415.

To validate these credential types, we have configured PicketLink to use a custom authenticator. This authenticator is who
knows how to process the different credential types and validate them accordingly.

To send authentication requests we're using the JQuery API and AJAX.

Is also provided a JAX-RS endpoint for logout.


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Start JBoss Enterprise Application Platform 6 or JBoss AS 7 with the Web Profile
-------------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat

 
Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package jboss-as:deploy

4. This will deploy `target/jboss-as-picketlink-authentication-rs-endpoint.war` to the running instance of the server.


Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-as-picketlink-authentication-rs-endpoint>. 


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
