negotiation-toolkit:  Enabling web application SPNEGO authentication.
====================
Author: Darran Lofthouse
Level: Advanced
Technologies: Servlet, Security, Kerberos
Summary: Demonstrates enabling SPNEGO security for a web application.

What is it?
-----------

This example is used to demonstrate and test the Kerberos configuration of an application server for SPNEGO authentication on 
web application deployed to JBoss Enterprise Application Platform 6 and  JBoss AS7.

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7.  In
addition to a running application server you will also require a Kerberos domain controller and configured security domains
for SPNEGO authentication. 


Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Security Domain
---------------

By default the negotiation toolkit assumes that the security domain to load the servers identity is called 'host' and that the
security domain to secure the web applications is called 'SPNEGO'.


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

4. This will deploy `target/jboss-negotiation-toolkit.war` to the running instance of the server.


Access the application 
---------------------

The application will be running at the following URL <http://localhost:8080/jboss-negotiation-toolkit/>.

_NOTE: SPNEGO is very dependent on using the host name in the URL that matches the name specified in the service principals._

On entering the application there are three test servlets to test the different features of the configuration.

* Basic Negotiation

This servlet is used to purely test that the web browser trusts the server sufficiently to initiate SPNEGO authentication.

* Security Domain Test

This servlet tests the security domain for the servers identity to verify that the server can obtain it's own ticket from the KDC.

* Secured

This is a full test that tests the SPNEGO authentication process and displays the results, once this servlet is accessible configuration is complete.

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
