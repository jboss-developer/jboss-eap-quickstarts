picketlink-authorization-rs-rbac: PicketLink Role-based Access Control(RBAC) for JAX-RS Endpoints
===============================
Author: Pedro Igor
Level: Intermediate
Technologies: CDI, PicketLink, Apache Deltaspike, JAX-RS
Summary: Basic example that demonstrates how to use RBAC to your JAX-RS Endpoints
Target Product: EAP
Source: <https://github.com/jboss-jdf/jboss-as-quickstart/>


What is it?
-----------

This example demonstrates the use of *CDI 1.0* and *PicketLink* in *JBoss Enterprise Application Platform 6* or *JBoss AS 7*.

You'll learn from this quickstart how to use PicketLink to authorize JAX-RS endpoints using a Role-based Access Control.

This application provides a view layer built using the JQuery API and AJAX, JAX-RS endpoints for authentication/logout
and some application specific JAX-RS endpoints.

Depending on the user you choose to sign in, you'll be presented to a simple menu where items change according to users roles.
Each menu item is a link for a specific REST service which is also protected using RBAC. This helps to ensure that direct
invocations to those services are also protected from unauthorized users.

At the server side, we're using security annotations from Apache Deltaspike. We just need to annotate our
REST methods with a specific annotation that tells which roles are allowed.

The latest PicketLink documentation is available [here](http://docs.jboss.org/picketlink/2/latest/).

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#configure-maven) before testing the quickstarts.


Start JBoss Enterprise Application Platform 6 or JBoss AS 7 with the Web Profile
-------------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat

 
Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package jboss-as:deploy

4. This will deploy `target/jboss-as-picketlink-authorization-rs-rbac.war` to the running instance of the server.


Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-as-picketlink-authorization-rs-rbac>. 


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
