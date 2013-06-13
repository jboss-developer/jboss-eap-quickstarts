picketlink-authentication-http-basic: PicketLink Two-Factor Authentication
===============================
Author: Pedro Igor
Level: Beginner
Technologies: CDI, PicketLink, JSF
Summary: Basic example that demonstrates how to use two-factor authentication using Time-based One-Time Passwords(TOTP) with a JSF view layer
Target Product: EAP
Source: <https://github.com/jboss-jdf/jboss-as-quickstart/>


What is it?
-----------

This example demonstrates the use of *CDI 1.0* and *PicketLink* in *JBoss Enterprise Application Platform 6* or *JBoss AS 7*.

You'll learn from this quickstart how to use PicketLink to authenticate users using a two-factor authentication with Time-based One-Time Passwords(TOTP).

The application provides a login page from where you can sign in using an username/password and a token.

At your first login, you will be redirect to a page from where you will be able to scan a QR Code. This QR Code can
be read by your phone using the [Google Authenticator](https://support.google.com/accounts/answer/1066447) application.

After installing the application in your phone, you should be able to scan the QR Code to configure an account. Your
new account will generate tokens every 30 seconds.

Please, check the server where the application is deployed and make sure its date/time is in sync with your phone. Otherwise,
the tokens won't be valid. By default, you can provide the same token in a 60 seconds interval. This is fully configurable.

Now you can use the tokens to perform a two-factor based authentication.

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

4. This will deploy `target/jboss-as-picketlink-authentication-http-basic.war` to the running instance of the server.


Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-as-picketlink-authentication-http-basic>. 


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
