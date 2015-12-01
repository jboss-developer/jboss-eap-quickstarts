ejbremote: Remote EJB Client Example
=====================================
Authors: Francesco Marchioni based on Jaikiran Maven pom.xml
Level: Intermediate
Technologies: EJB
Summary: Shows how to access an EJB from a remote Java client program using JNDI
Target Product: JBoss AS 7.1.1 community edition

What is it?
-----------

This example shows how to access an EJB from a remote Java client application. It demonstrates the use of *EJB 3.1* and *JNDI* in *JBoss AS 7*.

There are two components to this example: 

1. A server side component:

    The server component is comprised of a stateful EJB and a stateless EJB. It provides both an EJB JAR that is deployed to the server and a JAR file containing the remote business interfaces required by the remote client application.
2. A remote client application that accesses the server component. 

    The remote client application depends on the remote business interfaces from the server component. This application looks up the stateless and stateful beans via JNDI and invokes a number of methods on them.

Each component is defined in its own standalone Maven module. The quickstart provides a top level Maven module to simplify the packaging of the artifacts.


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

Since this quickstart builds two separate components, you can not use the standard *Build and Deploy* commands used by most of the other quickstarts. You must follow these steps to build, deploy, and run this quickstart.

1. Make sure you have started the JBoss server. See the instructions in the previous section.
2. Open a command line and navigate to the ejb-remote quickstart directory
3. Build and install the server side component:
    * Navigate to the server-side subdirectory:

        cd server-side
    * Build the EJB and client interfaces JARs and install them in your local Maven repository.

        For JBoss Enterprise Application Platform 6 (Maven user settings NOT configured): 

            mvn clean install -s PATH_TO_QUICKSTARTS/example-settings.xml

        For JBoss AS 7 or JBoss Enterprise Application Platform 6 (Maven user settings configured): 

            mvn clean install        
    * Deploy the EJB JAR to your server. This maven goal will deploy `server-side/target/jboss-as-ejb-remote-app.jar`. You can check the JBoss server console to see information messages regarding the deployment.

            mvn jboss-as:deploy
4. Build and run the client application
    * Navigate to the server-side subdirectory:

            cd ../client
    * Compile the client code

        For JBoss Enterprise Application Platform 6 (Maven user settings NOT configured): 

            mvn clean compile -s PATH_TO_QUICKSTARTS/example-settings.xml

        For JBoss AS 7 or JBoss Enterprise Application Platform 6 (Maven user settings configured): 

            mvn clean compile
    * Execute the client application within Maven

            mvn exec:exec


