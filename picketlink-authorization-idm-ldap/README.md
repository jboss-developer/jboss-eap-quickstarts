picketlink-authorization-idm-ldap: PicketLink IDM Authorization using LDAP
===============================
Author: Pedro Igor
Level: Beginner
Technologies: CDI, PicketLink, JSF
Summary: Basic example that demonstrates IDM-based authorization using a LDAP-based PicketLink IDM configuration
Target Product: EAP
Source: <https://github.com/jboss-jdf/jboss-as-quickstart/>


What is it?
-----------

You'll learn from this example how to authenticate and authorize users(role-based) against a LDAP server,
using a LDAP-based Identity Store.

The latest PicketLink documentation is available [here](http://docs.jboss.org/picketlink/2/latest/).

You can test this quickstart using the provided embedded LDAP server or you can configure this quickstart to connect to your own LDAP server. Instructions for both methods are provided below.


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


Configure LDAP
--------------------

You can run this quickstart using your own or the embedded LDAP server. You must configure the LDAP server *before* you build and deploy the quickstart.

### Configure the Quickstart to Use Your LDAP Server

To run this quickstart using your own LDAP Server, modify the String constant values in the  `org.jboss.as.quickstarts.picketlink.idm.ldap.IDMConfiguration` class to the connection values for your LDAP server.

### Configure the Quickstart to Use the Embedded LDAP Server

To run this quickstart using an embedded LDAP server:

1. Open a new command line an navigate to the root of this quickstart directory.

2. Execute the following command:

        mvn -Dtest=LDAPServer test
3. The prompt does not return, but you should see the following messages:

        Running LDAPServer
        Starting Apache DS server
        Time taken = 71milisec
        Going to import LDIF:ldap/users.ldif
        Time taken = 6milisec

If you get the output above is because the embedded LDAP server is now running. To terminate the server you can hit CTRL-C anytime.


Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package jboss-as:deploy

4. This will deploy `target/jboss-as-picketlink-authorization-idm-ldap.war` to the running instance of the server.


Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-as-picketlink-authorization-idm-ldap>.


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
