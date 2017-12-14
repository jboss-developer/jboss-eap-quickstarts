# helloworld-ssl: JBoss EAP Server Side SSL Configuration Example

Author: Giriraj Sharma, Stefan Guilhen  
Level: Beginner  
Technologies: SSL, Undertow  
Summary: The `helloworld-ssl` quickstart is a basic example that demonstrates server side SSL configuration in JBoss EAP.  
Target Product: JBoss EAP  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

## What is it?

This `helloworld-ssl` quickstart demonstrates the configuration of *SSL* in *Red Hat JBoss Enterprise Application Platform*.

This quickstart shows how to configure JBoss EAP to enable TLS/SSL configuration for the new `undertow` web subsystem.

Before you run this example, you must create certificates and configure the server to use SSL.


## System Requirements

The applications these projects produce are designed to be run on Red Hat JBoss Enterprise Application Platform 7.1 or later.

All you need to build these projects is Java 8.0 (Java SDK 1.8) or later and Maven 3.3.1 or later. See [Configure Maven for JBoss EAP 7.1](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.

To run these quickstarts with the provided build scripts, you need the JBoss EAP distribution ZIP. For information on how to install and run JBoss, see the [Red Hat JBoss Enterprise Application Platform Documentation](https://access.redhat.com/documentation/en/red-hat-jboss-enterprise-application-platform/) _Getting Started Guide_ located on the Customer Portal.

You can also use [JBoss Developer Studio or Eclipse](#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) to run the quickstarts.

## Use of EAP7_HOME

In the following instructions, replace `EAP7_HOME` with the actual path to your JBoss EAP installation. The installation path is described in detail here: [Use of EAP7_HOME and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_EAP7_HOME.md#use-of-eap_home-and-jboss_home-variables).

## Generate a Keystore and Self-signed Certificate

1. Open a command line and navigate to the JBoss EAP server `configuration` directory:

        For Linux:   standalone/configuration
        For Windows: standalone\configuration
2. Create a certificate for your server using the following command:

        $>keytool -genkey -alias mycert -keyalg RSA -sigalg MD5withRSA -keystore server.keystore -storepass secret -keypass secret -validity 9999

        What is your first and last name?
           [Unknown]:  localhost
        What is the name of your organizational unit?
           [Unknown]:  wildfly
        What is the name of your organization?
           [Unknown]:  jboss
        What is the name of your City or Locality?
           [Unknown]:  Raleigh
        What is the name of your State or Province?
           [Unknown]:  Carolina
        What is the two-letter country code for this unit?
           [Unknown]:  US
        Is CN=localhost, OU=wildfly, O=jboss, L=Raleigh, ST=Carolina, C=US correct?
           [no]:  yes

    Make sure to put your desired "hostname" into the "first and last name" field, otherwise you might run into issues while permanently accepting this certificate as an exception in some browsers. Chrome does not have an issue with that though.

## Configure the Server

These steps assume you are running the server in standalone mode and using the default `standalone.xml` supplied with the distribution.

You configure the SSL context by running JBoss CLI commands. For your convenience, this quickstart batches the commands into a `configure-ssl.cli` script provided in the root directory of this quickstart.

1. Before you begin, back up your server configuration file
    * If it is running, stop the JBoss EAP server.
    * Back up the file: `EAP7_HOME/standalone/configuration/standalone.xml`
    * After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.

2. Start the JBoss EAP server by typing the following:

        For Linux:  EAP7_HOME/bin/standalone.sh
        For Windows:  EAP7_HOME\bin\standalone.bat
3. Review the `configure-ssl.cli` file in the root of this quickstart directory. Comments in the script describe the purpose of each block of commands.

4. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing EAP7_HOME with the path to your server:

        For Linux: EAP7_HOME/bin/jboss-cli.sh --connect --file=configure-ssl.cli
        For Windows: EAP7_HOME\bin\jboss-cli.bat --connect --file=configure-ssl.cli
    You should see the following result when you run the script:

        The batch executed successfully
        process-state: reload-required

5. Stop the JBoss EAP server.

Now you're ready to connect to the SSL port of your instance https://localhost:8443/. Note, that you get the privacy error as the server certificate is self-signed. If you need to use a fully signed certificate you mostly get a PEM file from the Certificate Authority. In such a case, you need to import the PEM into the keystore.

## Review the Modified Server Configuration

After stopping the server, open the `EAP7_HOME/standalone/configuration/standalone.xml` file and review the changes.

1. The following `key-store` was added to the `elytron` subsystem:

        <key-stores>
            <key-store name="qsKeyStore">
                <credential-reference clear-text="secret"/>
                <implementation type="JKS"/>
                <file path="server.keystore" relative-to="jboss.server.config.dir"/>
            </key-store>
        </key-stores>

2. The following `key-manager` was added to the `elytron` subsystem:

        <key-managers>
            <key-manager name="qsKeyManager" key-store="qsKeyStore">
                <credential-reference clear-text="secret"/>
            </key-manager>
        </key-managers>

3. The following `ssl-context` was added to the `elytron` subsystem:

        <server-ssl-contexts>
            <server-ssl-context name="qsSSLContext" protocols="TLSv1.2" key-manager="qsKeyManager"/>
        </server-ssl-contexts>

4. The `https-listener` in the `undertow` subsystem was changed to reference the `qsSSLContext` `ssl-context`:

        <https-listener name="https" socket-binding="https" ssl-context="qsSSLContext" enable-http2="true"/>

## Test the Server SSL Configuration

To test the SSL configuration, access: <https://localhost:8443>

## Start the Server

1. Open a command line and navigate to the root of the JBoss EAP directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   bin/standalone.sh
        For Windows: bin\standalone.bat

## Build and Deploy the Quickstart

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command line and navigate to the root directory of one of the quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package wildfly:deploy

4. This will deploy `target/helloworld-ssl.war` to the running instance of the server.

## Access the Application

The application will be running at the following URL: <https://localhost:8443/helloworld-ssl/>.

## Undeploy the Archive

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy

## Restore the Server Configuration

You can restore the original server configuration by running the  `restore-configuration.cli` script provided in the root directory of this quickstart or by manually restoring the back-up copy the configuration file.

### Restore the Server Configuration by Running the JBoss CLI Script

1. Start the JBoss EAP server by typing the following:

        For Linux:  EAP7_HOME/bin/standalone.sh
        For Windows:  EAP7_HOME\bin\standalone.bat
2. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing EAP7_HOME with the path to your server:

        For Linux: EAP7_HOME/bin/jboss-cli.sh --connect --file=restore-configuration.cli
        For Windows: EAP7_HOME\bin\jboss-cli.bat --connect --file=restore-configuration.cli
    This script reverts the changes made to the `undertow` subsystem and it also removes the `ssl-context`, `key-manager`
    and `key-store` from the `elytron` subsystem. You should see the following result when you run the script:

        The batch executed successfully
        process-state: reload-required

### Restore the Server Configuration Manually

1. If it is running, stop the JBoss EAP server.
2. Replace the `EAP7_HOME/standalone/configuration/standalone.xml` file with the back-up copy of the file.

## Remove the keystore created for this quickstart

1. Open a command line and navigate to the JBoss EAP server `configuration` directory:

        For Linux:   standalone/configuration
        For Windows: standalone\configuration
2. Remove the keystore generated for this quickstart.

## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a JBoss EAP server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts).

* Be sure to configure the server by running the JBoss CLI commands as described above under [Configure the JBoss EAP Server](#configure-the-server). Stop the server at the end of that step.
* Be sure to [Restore the Server Configuration](#restore-the-server-configuration) when you have completed testing this quickstart.

## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

        mvn dependency:sources        
