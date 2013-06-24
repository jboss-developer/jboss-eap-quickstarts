picketlink-authentication-facebook: PicketLink Authentication with Twitter login
===============================
Author: Anil Saldhana
Technologies: CDI, PicketLink
Summary: Basic example that demonstrates Twitter authentication using PicketLink
Target Product: EAP
Source: <https://github.com/jboss-jdf/jboss-as-quickstart/>

What is it?
-----------

This example demonstrates the use of *CDI 1.0* and *PicketLink* in *JBoss Enterprise Application Platform 6* or *JBoss AS 7*.

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Obtain Twitter Application ClientID, ClientSecret
--------------------------------------------------
You will have to log in to Twitter Developer (https://dev.twitter.com/) account and register an application.
Then you will be provided a ClientID and ClientSecret. 

As an example,
Consumer Key: provided by Twitter
Consumer Secret: provided by Twitter
Website: some publicly accessible url
Callback URL:http://SOMEHOST.com:8080/jboss-as-picketlink-authentication-twitter/ 

NOTE: Twitter does not allow localhost as callback url due to security reasons. If you are testing an app on localhost, you can do some type of host mapping such as /etc/hosts
as follows:
127.0.0.1 SOMEHOST.com    localhost

Configure JBoss Enterprise Application Platform 6
-------------------------------------------------

In JBOSS_HOME/standalone/configuration/standalone.xml, add the following block, right after </extensions>:

<system-properties>
        <property name="TWIT_CLIENT_ID" value="client id provided by twitter"/>
        <property name="TWIT_CLIENT_SECRET" value="client secret provided by twitter"/>
        <property name="TWIT_RETURN_URL" value="http://SOMEHOST.com:8080/jboss-as-picketlink-authentication-twitter/"/>
    </system-properties>

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

4. This will deploy `target/jboss-as-picketlink-authentication.war` to the running instance of the server.


Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-as-picketlink-authentication-facebook/>. 


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
