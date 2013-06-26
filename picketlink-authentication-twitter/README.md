picketlink-authentication-twitter: PicketLink Authentication with Twitter login
===============================
Author: Anil Saldhana
Technologies: CDI, PicketLink
Summary: Basic example that demonstrates Twitter authentication using PicketLink
Target Product: EAP
Source: <https://github.com/jboss-jdf/jboss-as-quickstart/>

What is it?
-----------

This example demonstrates the use of *CDI 1.0* and *PicketLink* in *JBoss Enterprise Application Platform 6*.
This quickstart demonstrates using Twitter Login as the authentication mechanism for a Java EE application.
This example uses an application filter called TwitterFilter that makes use of PicketLink provided authenticator
called TwitterAuthenticator, for Twitter login functionality.

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6. 

 
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
     Callback URL: http://SOMEHOST.com:8080/jboss-as-picketlink-authentication-twitter/ 

NOTE: Twitter does not allow localhost as callback url due to security reasons. If you are testing an app on localhost, you can do some type of host mapping such as /etc/hosts
as follows:

     127.0.0.1 SOMEHOST.com    localhost

Configure the JBoss Enterprise Application Platform 6.1
--------------------------------------------------------

These steps asume that you are running the server in standalone mode and using the default standalone.xml supplied with the distribution.

You can configure the server `system-property` values by running the  `configure-twitter.cli` script provided in the root directory of this quickstart, by using the JBoss CLI interactively, or by manually editing the configuration file. The three different approaches are described below. Whichever approach you choose, it must be completed before you deploy the quickstart.

_NOTE - Before you begin:_

1. If it is running, stop the JBoss Enterprise Application Platform 6. 
2. Backup the file: `JBOSS_HOME/standalone/configuration/standalone.xml`
3. After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.

#### Configure the Twitter system-properties by Running the JBoss CLI Script

1. Start the JBoss Enterprise Application Platform 6 Server by typing the following: 

        For Linux:  JBOSS_HOME/bin/standalone.sh 
        For Windows:  JBOSS_HOME\bin\standalone.bat
2. Open the `configure-twitter.cli` file in an editor. Replace  `YOUR_CLIENT_ID`, `YOUR_CLIENT_SECRET_CODE`, and `YOUR_RETURN_URL` with the values provided when you registered as a Twitter developer.
3. Open a new command line, navigate to the root directory of this quickstart, and run the following command, replacing JBOSS_HOME with the path to your server:

        JBOSS_HOME/bin/jboss-cli.sh --connect --file=configure-twitter.cli
This script adds the system-property valuse to the the server configuration. You should see the following result when you run the script:

        #1 /system-property=TWIT_CLIENT_ID:add(value="YOUR_CLIENT_ID")
        #2 /system-property=TWIT_CLIENT_SECRET:add(value="YOUR_CLIENT_SECRET_CODE")
        #3 /system-property=TWIT_RETURN_URL:add(value="YOUR_RETURN_URL/")
        The batch executed successfully.
        {"outcome" => "success"}


### Configure the Twitter system-properties Using the JBoss CLI Interactively

1. Start the JBoss Enterprise Application Platform 6 server by typing the following: 

		For Linux:  JBOSS_HOME_SERVER_1/bin/standalone.sh
		For Windows:  JBOSS_HOME_SERVER_1\bin\standalone.bat
2. To start the JBoss CLI tool, open a new command line, navigate to the JBOSS_HOME directory, and type the following:
    
		For Linux: bin/jboss-cli.sh --connect
		For Windows: bin\jboss-cli.bat --connect
3. Add the Twitter system properties. At the prompt, enter the following series of commands, making sure to replace  `YOUR_CLIENT_ID`, `YOUR_CLIENT_SECRET_CODE`, and `YOUR_RETURN_URL` with the values provided when you registered as a Twitter developer. 

		[standalone@localhost:9999 /] /system-property=TWIT_CLIENT_ID:add(value="YOUR_CLIENT_ID")
		[standalone@localhost:9999 /] /system-property=TWIT_CLIENT_SECRET:add(value="YOUR_CLIENT_SECRET_CODE")
		[standalone@localhost:9999 /] /system-property=TWIT_CLIENT_RETURN_URL:add(value="YOUR_RETURN_URL/")
			
		[standalone@localhost:9999 /] :reload

You should see `{"outcome" => "success"}` after each command.

### Configure the Twitter system-properties by Manually Editing the Server Configuration File

1.  If it is running, stop the JBoss Enterprise Application Platform 6 Server.
2.  Backup the file: `JBOSS_HOME/standalone/configuration/standalone.xml`
3.  Open the file: `JBOSS_HOME/standalone/configuration/standalone.xml`
4.  Add the following XML, right after </extensions>. Be sure to replace  `YOUR_CLIENT_ID`, `YOUR_CLIENT_SECRET_CODE`, and `YOUR_RETURN_URL` with the values provided when you registered as a Twitter developer. 

        <system-properties>
            <property name="TWIT_CLIENT_ID" value="YOUR_CLIENT_ID"/>
            <property name="TWIT_CLIENT_SECRET" value="YOUR_CLIENT_SECRET_CODE"/>
            <property name="TWIT_RETURN_URL" value="YOUR_RETURN_URL"/>
        </system-properties>

An example of TWIT_RETURN_URL setting would be:
        <property name="TWIT_RETURN_URL" value="http://SOMEHOST.com:8080/jboss-as-picketlink-authentication-twitter/"/>

Start JBoss Enterprise Application Platform 6
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

4. This will deploy `target/jboss-as-picketlink-authentication-twitter.war` to the running instance of the server.


Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-as-picketlink-authentication-twitter/>. 


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy

Remove the Twitter Configuration
----------------------------

You can remove the Twitter system-properties  by running the  `remove-twitter.cli` script provided in the root directory of this quickstart or by manually restoring the back-up copy the configuration file. 

### Remove the Twitter Configuration by Running the JBoss CLI Script

1. Start the JBoss Enterprise Application Platform 6 Server by typing the following: 

        For Linux:  JBOSS_HOME_SERVER_1/bin/standalone.sh
        For Windows:  JBOSS_HOME_SERVER_1\bin\standalone.bat
2. Open a new command line, navigate to the root directory of this quickstart, and run the following command, replacing JBOSS_HOME with the path to your server:

        JBOSS_HOME/bin/jboss-cli.sh --connect --file=remove-twitter.cli 
This script removes the `test` queue from the `messaging` subsystem in the server configuration. You should see the following result when you run the script:

        #1 /system-property=TWIT_CLIENT_ID:remove
        #2 /system-property=TWIT_CLIENT_SECRET:remove
        #3 /system-property=TWIT_RETURN_URL:remove
        The batch executed successfully.
        {"outcome" => "success"}


### Remove the Twitter Configuration Manually
1. If it is running, stop the JBoss Enterprise Application Platform 6.
2. Replace the `JBOSS_HOME/standalone/configuration/standalone.xml` file with the back-up copy of the file.


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
