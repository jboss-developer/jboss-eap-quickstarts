servlet-security-genericheader-auth:  Authenticate Via External SSO Using HTTP Request Headers
====================
Author: Gary Lamperillo, Jesse Sightler  
Level: Intermediate  
Technologies: Servlet, Security, JAAS  
Summary: The `servlet-security-genericheader-auth` quickstart demonstrates a custom authenticator to enable support for HTTP header-based authentication.  
Target Product: JBoss EAP  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

_NOTE: This quickstart can not yet be built in Red Hat JBoss EAP 7. It has been temporarily disabled until the API dependencies are resolved._

The `servlet-security-genericheader-auth` quickstart demonstrates a method for HTTP authentication based upon an HTTP header in the incoming request in Red Hat JBoss Enterprise Application Platform. 
A Tomcat Valve called "GenericHeaderAuthenticator" is used to pass these credentials to JBoss. Tomcat Valves provide a 
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

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 7 or later. 

All you need to build this project is Java 8.0 (Java SDK 1.8) or later and Maven 3.0 or later.

Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.


Use of EAP_HOME
---------------

In the following instructions, replace `EAP_HOME` with the actual path to your JBoss EAP installation. The installation path is described in detail here: [Use of EAP_HOME and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_EAP_HOME.md#use-of-eap_home-and-jboss_home-variables).


Configure the JBoss EAP Server
---------------

This quickstart requires a custom security `GenericHeaderAuth` domain be enabled in order to trust the remote proxy server's username header.

You configure the security domain by running JBoss CLI commands. For your convenience, this quickstart batches the commands into a `configure-security-domain.cli` script provided in the root directory of this quickstart. 

1. Before you begin, back up your server configuration file
    * If it is running, stop the JBoss EAP server.
    * Backup the file: `EAP_HOME/standalone/configuration/standalone.xml`
    * After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.

2. Start the JBoss EAP server by typing the following: 

        For Linux:  EAP_HOME/bin/standalone.sh
        For Windows:  EAP_HOME\bin\standalone.bat
3. Review the `configure-security-domain.cli` file in the root of this quickstart directory. This script adds the `quickstart-domain` domain to the `security` subsystem in the server configuration and configures authentication access. Comments in the script describe the purpose of each block of commands.


4. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing EAP_HOME with the path to your server:

        For Linux:   EAP_HOME/bin/jboss-cli.sh --connect --file=configure-security-domain.cli
        For Windows: EAP_HOME\bin\jboss-cli.bat --connect --file=configure-security-domain.cli
   This script adds the `GenericHeaderAuth` domain to the `security` subsystem in the server configuration and configures authentication access. You should see the following result when you run the script:

        The batch executed successfully
        {"outcome" => "success"}
5. Stop the JBoss EAP server.


Review the Modified Server Configuration
-----------------------------------

After stopping the server, open the `EAP_HOME/standalone/configuration/standalone.xml` file and review the changes.

The following `GenericHeaderAuth` security-domain was added to the `security` subsystem.

        <security-domain name="GenericHeaderAuth">
            <authentication>
                <login-module code="org.jboss.security.auth.spi.RemoteHostTrustLoginModule" flag="required">
                    <module-option name="trustedHosts" value="127.0.0.1"/>
                    <module-option name="roles" value="guest"/>
                </login-module>
            </authentication>
        </security-domain>


Start the JBoss EAP Server
-------------------------

1. Open a command prompt and navigate to the root of the JBoss EAP directory.
2. The following shows the command line to start the server:

        For Linux:   EAP_HOME/bin/standalone.sh
        For Windows: EAP_HOME\bin\standalone.bat


Run the Arquillian Tests
-------------------------

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container.

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn clean test -Parq-jbossas-remote


Investigate the Console Output
----------------------------

Maven prints summary of the 1 performed test to the console.

   -------------------------------------------------------
     T E S T S
    -------------------------------------------------------
    Running org.jboss.as.quickstart.deltaspike.beanbuilder.test.ByIdExtensionTest
    Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 8.641 sec
    
    Results :
    
    Tests run: 1, Failures: 0, Errors: 0, Skipped: 0



Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a JBoss EAP server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 

### Run the Arquillian Tests in JBoss Developer Studio
-----------------------

To run the tests from JBoss Developer Studio, you must first set the active Maven profile in the project properties to `arq-jbossas-managed` for running on managed server, or `arq-jbossas-remote` for running on remote server.

To run the tests, right click on the project or individual classes and select `Run As --> JUnit Test` in the context menu.


Debug the Application
------------------------------------

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

      mvn dependency:sources
     


Remove the Security Domain Configuration
----------------------------

You can remove the security domain configuration by running the  `remove-security-domain.cli` script provided in the root directory of this quickstart or by manually restoring the back-up copy the configuration file. 

### Remove the Security Domain Configuration by Running the JBoss CLI Script

1. Start the JBoss EAP server by typing the following: 

        For Linux:  EAP_HOME/bin/standalone.sh
        For Windows:  EAP_HOME\bin\standalone.bat
2. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing EAP_HOME with the path to your server:

        For Linux:   EAP_HOME/bin/jboss-cli.sh --connect --file=remove-security-domain.cli
        For Windows: EAP_HOME\bin\jboss-cli.bat --connect --file=remove-security-domain.cli
This script removes the `test` queue from the `messaging` subsystem in the server configuration. You should see the following result when you run the script:

        The batch executed successfully
        {"outcome" => "success"}

### Remove the Security Domain Configuration Manually
1. If it is running, stop the JBoss EAP server.
2. Replace the `EAP_HOME/standalone/configuration/standalone.xml` file with the back-up copy of the file.



