picketlink-sts: PicketLink Federation: WS-Trust Security Token Service 
======================================================
Author: Peter Skopek
Level: Advanced
Technologies: WS-Trust, SAML
Summary: This project is an implementation of a WS-Trust Security Token Service.
Target Product: EAP
Product Versions: EAP 6.1, EAP 6.2
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>
 
What is it?
-----------

This example demonstrates how to deploy a fully compliant WS-Trust Security Token Service (STS).

WS-Trust extends the WS-Security specification to allow the issuance, renewal, and validation of security tokens. 
Many WS-Trust functions center around the use of a "Security Token Service", or STS. 
The STS is contacted to obtain security tokens that are used to create messages to talk to the services. 
The primary use of the STS is to acquire SAML tokens used to talk to the service.
The STS also plays an important role when you need to propagate credentials between different layers, for example, the web and service layer.

This quickstart is just a service application for use by other applications. It is a JAX-WS Endpoint based on PicketLink's WS-Trust implementation, which by default, allows you to issue, renew and validate SAML assertions.

PicketLink also supports different token providers, which means you can provide your own custom security tokens.


See more examples in [PicketLink project documentation.](http://docs.jboss.org/picketlink/2/2.1.7.Final/reference/html/ch01.html#sid-819345). 
Additional PicketLink quickstarts can be found here: [PicketLink Quickstarts](https://docs.jboss.org/author/display/PLINK/PicketLink+Quickstarts).

For more information about PicketLink STS, see the [PicketLink Security Token Server Documentation](https://docs.jboss.org/author/display/PLINK/Security+Token+Server+%28STS%29).

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.1 or later.

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.


Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#configure-maven) before testing the quickstarts.


Additional PicketLink STS configuration options
-----------------------------------------------

Application is preconfigured to use "picketlink-sts" security domain with user.properties and roles.properties files.
This is not suitable for production use. Change application security according to your organization standards.

For more information about PicketLink see the [PicketLink Reference Documentation](http://docs.jboss.org/picketlink/2/2.1.7.Final/reference/html/).


Configure the JBoss Server
-------------------------------------------------

_NOTE - Before you begin:_

1. If it is running, stop the JBoss server.
2. Backup the file: `JBOSS_HOME/standalone/configuration/standalone.xml`
3. After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.

### Configure the Security Domain Using the JBoss CLI 

1. Start the JBoss server by typing the following:

        For Linux:  JBOSS_HOME/bin/standalone.sh
        For Windows:  JBOSS_HOME\bin\standalone.bat
2. Open a new command line, navigate to the root directory of this quickstart, and run the following command, replacing JBOSS_HOME with the path to your server:

        For Linux: JBOSS_HOME/bin/jboss-cli.sh --file=configure-security-domain.cli 
        For Windows: JBOSS_HOME\bin\jboss-cli.bat --file=configure-security-domain.cli 

   If you are running the controller on different host, pass the following argument, replacing HOST_NAME and PORT_NUMBER with the correct values:

        --controller=HOST_NAME:PORT_NUMBER
   You should see the following result when you run the script:
   
          #1 /subsystem=security/security-domain=picketlink-sts:add
          #2 /subsystem=security/security-domain=picketlink-sts/authentication=classic:add(  login-modules=[  {  "code" => "UsersRoles ",  "flag" => "required",  "module-options" => [  "usersProperties"=>"users.properties",  "rolesProperties"=>"roles.properties"  ]  }  ]  )
          The batch executed successfully

Start the JBoss Server
-------------------------

If you do not have a running server:

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install jboss-as:deploy
4. This will deploy `target/jboss-picketlink-sts.war` to the running instance of the server.


Access the Application 
---------------------

The application will be running at the following URL <http://localhost:8080/jboss-picketlink-sts/>.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Remove the Security Domain Configuration 
----------------------------------------

1. If it is running, stop the JBoss server.
2. Replace the `JBOSS_HOME/standalone/configuration/standalone.xml` file with the back-up copy of the file.


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

      mvn dependency:sources
      mvn dependency:resolve -Dclassifier=javadoc
