servlet-security-genericheader-auth:  Authenticate via external SSO system using HTTP request headers
====================
Author: Gary Lamperillo, Jesse Sightler
Level: Intermediate
Technologies: Servlet, JAAS
Summary: Demonstrates the use a custom authenticator to enable support for header-based authentication
Target Product: EAP
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>

What is it?
-----------

This example demonstrates a method for HTTP authentication based upon an HTTP header in the incoming request. A Tomcat
Valve called "GenericHeaderAuthenticator" is used to pass these credentials to JBoss. Tomcat Valves provide a 
powerful, flexible way to insert a Java component into the request servlet container's request processing pipeline in 
order to implement features such as this.

This quickstart takes the following steps to implement Servlet security:

1. Define a security domain in the `standalone.xml` configuration file.
2. Add a security domain reference to `WEB-INF/jboss-web.xml`.
3. Add a security constraint to a sample servlet via an Annotation

In this example, the Security Domain is configured to provide a `guest` role. This matches the role in the `@HttpConstraint` annotation
on the `SecuredServlet` sample.

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better and Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6.1. 


Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#configure-maven) before testing the quickstarts.


Prerequisites
-------------

_Note_: Unlike most of the quickstarts, this one must be run against JBoss Enterprise Application Platform 6.1.


Configure the JBoss Enterprise Application Platform 6.1 server
---------------

This quickstart requires a custom security `GenericHeaderAuth` domain be enabled in order to trust the remote proxy server's username header.

_NOTE - Before you begin:_

1. If it is running, stop the JBoss Enterprise Application Platform 6.1 servr.
2. Backup the file: `JBOSS_HOME/standalone/configuration/standalone.xml`
3. After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.

#### Configure the Security Domain by Running the JBoss CLI Script

1. Start the JBoss Enterprise Application Platform 6.1 server by typing the following: 

        For Linux:  JBOSS_HOME_SERVER_1/bin/standalone.sh
        For Windows:  JBOSS_HOME_SERVER_1\bin\standalone.bat

2. Open a new command line, navigate to the root directory of this quickstart, and run the following command, replacing JBOSS_HOME with the path to your server:

        For Linux:   JBOSS_HOME/bin/jboss-cli.sh --connect --file=configure-security-domain.cli
        For Windows: JBOSS_HOME\bin\jboss-cli.bat --connect --file=configure-security-domain.cli
This script adds the `GenericHeaderAuth` domain to the `security` subsystem in the server configuration and configures authentication access. You should see the following result when you run the script:

        #1 /subsystem=security/security-domain=GenericHeaderAuth:add
        #2 /subsystem=security/security-domain=GenericHeaderAuth/authentication=classic:add(login-modules=[{"code" => "org.jboss.security.auth.spi.RemoteHostTrustLoginModule", "flag" => "required", "module-options" => [("trustedHosts" => "127.0.0.1"), ("roles" => "guest"),]}])
        The batch executed successfully
        {"outcome" => "success"}

### Configure the Security Domain Using the JBoss CLI Interactively

1. Start the JBoss Enterprise Application Platform 6.1 server by typing the following: 

        For Linux:  JBOSS_HOME_SERVER_1/bin/standalone.sh
        For Windows:  JBOSS_HOME_SERVER_1\bin\standalone.bat
2. To start the JBoss CLI tool, open a new command line, navigate to the JBOSS_HOME directory, and type the following:
    
        For Linux: bin/jboss-cli.sh --connect
        For Windows: bin\jboss-cli.bat --connect
3. At the prompt, enter the following series of commands:

        [standalone@localhost:9999 /] /subsystem=security/security-domain=GenericHeaderAuth:add
        [standalone@localhost:9999 /] /subsystem=security/security-domain=GenericHeaderAuth/authentication=classic:add(login-modules=[{"code" => "org.jboss.security.auth.spi.RemoteHostTrustLoginModule", "flag" => "required", "module-options" => [("trustedHosts" => "127.0.0.1"), ("roles" => "guest"),]}])
    
    Then reload the server with this command:

        [standalone@localhost:9999 /] :reload

### Configure the Security Domain by Manually Editing the Server Configuration File

1.  If it is running, stop the JBoss Enterprise Application Platform 6.1 server.
2.  Make sure you have backed up the `JBOSS_HOME/standalone/configuration/standalone.xml` file as noted in the beginning of this section.
3.  Open the `JBOSS_HOME/standalone/configuration/standalone.xml` file in an editor and locate the subsystem `urn:jboss:domain:security`. 
4.  Add the following XML just before the `</security-domains>` tag:

        <security-domain name="GenericHeaderAuth">
            <authentication>
                <login-module code="org.jboss.security.auth.spi.RemoteHostTrustLoginModule" flag="required">
                    <module-option name="trustedHosts" value="127.0.0.1"/>
                    <module-option name="roles" value="guest"/>
                </login-module>
            </authentication>
        </security-domain>


Start JBoss Enterprise Application Platform 6.1 with the Web Profile
-------------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat


Run the Arquillian Tests
-------------------------

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container.

_NOTE: The following commands assume you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Run the Arquillian Tests](../README.md#run-the-arquillian-tests) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn clean test -Parq-jbossas-remote


Investigate the Console Output
----------------------------


### Maven

Maven prints summary of performed tests into the console:

   -------------------------------------------------------
     T E S T S
    -------------------------------------------------------
    Running org.jboss.as.quickstart.deltaspike.beanbuilder.test.ByIdExtensionTest
    log4j:WARN No appenders could be found for logger (org.jboss.logging).
    log4j:WARN Please initialize the log4j system properly.
    Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 8.641 sec
    
    Results :
    
    Tests run: 1, Failures: 0, Errors: 0, Skipped: 0



Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 

### Run the Arquillian Tests in JBoss Developer Studio
-----------------------

To run the tests from JBoss Developer Studio, you must first set the active Maven profile in the project properties to `arq-jbossas-managed` for running on managed server, or `arq-jbossas-remote` for running on remote server.

To run the tests, right click on the project or individual classes and select `Run As --> JUnit Test` in the context menu.


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

      mvn dependency:sources
      mvn dependency:resolve -Dclassifier=javadoc


Remove the Security Domain Configuration
----------------------------

You can remove the security domain configuration by running the  `remove-security-domain.cli` script provided in the root directory of this quickstart or by manually restoring the back-up copy the configuration file. 

### Remove the Security Domain Configuration by Running the JBoss CLI Script

1. Start the JBoss Enterprise Application Platform 6.1 server by typing the following: 

        For Linux:  JBOSS_HOME_SERVER_1/bin/standalone.sh
        For Windows:  JBOSS_HOME_SERVER_1\bin\standalone.bat
2. Open a new command line, navigate to the root directory of this quickstart, and run the following command, replacing JBOSS_HOME with the path to your server:

        For Linux:   JBOSS_HOME/bin/jboss-cli.sh --connect --file=remove-security-domain.cli
        For Windows: JBOSS_HOME\bin\jboss-cli.bat --connect --file=remove-security-domain.cli
This script removes the `test` queue from the `messaging` subsystem in the server configuration. You should see the following result when you run the script:

        #1 /subsystem=security/security-domain=GenericHeaderAuth:remove
        The batch executed successfully


### Remove the Security Domain Configuration Manually
1. If it is running, stop the JBoss Enterprise Application Platform 6.1 Server.
2. Replace the `JBOSS_HOME/standalone/configuration/standalone.xml` file with the back-up copy of the file.



