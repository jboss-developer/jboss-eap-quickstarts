# picketlink-sts: PicketLink Federation: WS-Trust Security Token Service

Author: Peter Skopek  
Level: Advanced  
Technologies: WS-Trust, SAML  
Summary: The `picketlink-sts` quickstart demonstrates how to deploy a fully compliant WS-Trust Security Token Service (STS).  
Target Product: JBoss EAP  
Source: <https://github.com/jbossas/eap-quickstarts/>  

## What is it?


The `picketlink-sts` quickstart demonstrates how to deploy a fully compliant WS-Trust Security Token Service (STS) to Red Hat JBoss Enterprise Application Platform.

WS-Trust extends the WS-Security specification to allow the issuance, renewal, and validation of security tokens.
Many WS-Trust functions center around the use of a Security Token Service, or STS.
The STS is contacted to obtain security tokens that are used to create messages to talk to the services.
The primary use of the STS is to acquire SAML tokens used to talk to the service.
The STS also plays an important role when you need to propagate credentials between different layers, for example, the web and service layer.

PicketLink also supports different token providers, which means you can provide your own custom security tokens.

_Note:_ This quickstart is not a fully functional application. It is a JAX-WS Endpoint based on PicketLink's WS-Trust implementation, which by default, allows you to issue, renew and validate SAML assertions. It is a service intended to be called by other applications.


## How to Use This Quickstart

This quickstart is preconfigured to use the `picketlink-sts` security domain. By default, the STS is protected to only allow requests from authenticated users. All users and also their roles, are defined in two properties files:

        Users: src/main/resources/users.properties
        Roles: src/main/resources/roles.properties

You can view the WSDL for the STS at the following URL: <http://localhost:8080/picketlink-sts?wsdl>.

From a JAX-WS perspective, you can use any tool you want to start using the STS. Below is an example of a SOAP envelope asking the STS to issue a SAML v2.0 Assertion:

        <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" xmlns:urn="urn:picketlink:identity-federation:sts">
            <soap:Header/>
            <soap:Body>
                <wst:RequestSecurityToken xmlns:wst="http://docs.oasis-open.org/ws-sx/ws-trust/200512">
                    <wst:TokenType>http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0</wst:TokenType>
                    <wst:RequestType>http://docs.oasis-open.org/ws-sx/ws-trust/200512/Issue</wst:RequestType>
                </wst:RequestSecurityToken>
            </soap:Body>
        </soap:Envelope>

There is a simple example of WS-Trust client usage provided by PicketLink. To use this example deploy PicketLink STS as described below and run the `mvn exec:java` command. The assertion from PicketLink STS is printed to the console. This process is described in detail below in the section entitled [Access the Application](#access-the-application).

_Note: This example is not suitable for production use. You must change the application security to comply with your organization's standards._


## Where to Find Additional Information

* For more information about PicketLink STS, see [Security Token Service (STS)](https://access.redhat.com/documentation/en/red-hat-jboss-enterprise-application-platform/version-7.0/developing-web-services-applications/#about_sts) in _Developing Web Services Applications_.

* Additional PicketLink quickstarts can be found here: [PicketLink Quickstarts](https://docs.jboss.org/author/display/PLINK/PicketLink+Quickstarts).


## System Requirements

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 7.1 or later.

All you need to build this project is Java 8.0 (Java SDK 1.8) or later and Maven 3.2.5 or later. See [Configure Maven for JBoss EAP 7.1](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


## Use of EAP7_HOME

In the following instructions, replace `EAP7_HOME` with the actual path to your JBoss EAP installation. The installation path is described in detail here: [Use of EAP7_HOME and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_EAP7_HOME.md#use-of-eap_home-and-jboss_home-variables).


## Configure the Server

You configure the security domain by running JBoss CLI commands. For your convenience, this quickstart batches the commands into a `configure-security-domain.cli` script provided in the root directory of this quickstart.

1. Before you begin, back up your server configuration file
    * If it is running, stop the JBoss EAP server.
    * Backup the file: `EAP7_HOME/standalone/configuration/standalone.xml`
    * After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.

2. Start the JBoss EAP server by typing the following:

        For Linux:  EAP7_HOME/bin/standalone.sh
        For Windows:  EAP7_HOME\bin\standalone.bat
3. Review the `configure-security-domain.cli` file in the root of this quickstart directory. This script adds the `picketlink-sts` security domain to the `security` subsystem in the server configuration and configures authentication access.

4. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing EAP7_HOME with the path to your server:

        For Linux: EAP7_HOME/bin/jboss-cli.sh --connect --file=configure-security-domain.cli
        For Windows: EAP7_HOME\bin\jboss-cli.bat --connect --file=configure-security-domain.cli

   If you are running the controller on different host, pass the following argument, replacing HOST_NAME and PORT_NUMBER with the correct values:

        --controller=HOST_NAME:PORT_NUMBER
   You should see the following result when you run the script:

        The batch executed successfully
        {"outcome" => "success"}

   The batch file also restarts the server.
5. Stop the JBoss EAP server.

## Review the Modified Server Configuration

After stopping the server, open the `EAP7_HOME/standalone/configuration/standalone.xml` file and review the changes.

The following `picketlink-sts` security-domain was added to the `security` subsystem.


        <security-domain name="picketlink-sts">
            <authentication>
                <login-module code="UsersRoles " flag="required">
                    <module-option name="usersProperties" value="users.properties"/>
                    <module-option name="rolesProperties" value="roles.properties"/>
                </login-module>
            </authentication>
        </security-domain>

## Start the Server

If you do not have a running server:

1. Open a command prompt and navigate to the root of the JBoss EAP directory.
2. The following shows the command line to start the server:

        For Linux:   EAP7_HOME/bin/standalone.sh
        For Windows: EAP7_HOME\bin\standalone.bat


## Build and Deploy the Quickstart

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install wildfly:deploy
4. This deploys `target/picketlink-sts.war` to the running instance of the server.

_Note:_ When you deploy the quickstart, you will see the following warnings in the server log. These warnings are expected.

        WARN  [org.jboss.as.dependency.deprecated] (MSC service thread 1-5) WFLYSRV0221: Deployment "deployment.picketlink-sts.war" is using a deprecated module ("org.picketlink:main") which may be removed in future versions without notice.
        WARN  [org.jboss.as.dependency.deprecated] (MSC service thread 1-5) WFLYSRV0221: Deployment "deployment.picketlink-sts.war" is using a deprecated module ("org.picketlink:main") which may be removed in future versions without notice.


## Access the Application

You can test the service as follows:

1. Open a command prompt and navigate to the root directory of this quickstart.
2. Type the following command:

        mvn exec:java
3. You should see a `<saml:Assertion` assertion from PicketLink STS along with a `BUILD SUCCESS` printed to the console.

        Invoking token service to get SAML assertion for user:UserA with password:PassA
        SAML assertion for user:UserA successfully obtained!
        <saml:Assertion xmlns:saml="urn:oasis:names:tc:SAML:2.0:assertion" ID="ID_79157aa6-38ab-4e5e-a562-78bade9ffb82" IssueInstant="2013-11-18T18:19:35.955Z" Version="2.0"><saml:Issuer>PicketLinkSTS</saml:Issuer><dsig:Signature xmlns:dsig="http://www.w3.org/2000/09/xmldsig#"><dsig:SignedInfo><dsig:CanonicalizationMethod Algorithm="http://www.w3.org/2001/10/xml-exc-c14n#WithComments"/><dsig:SignatureMethod Algorithm="http://www.w3.org/2000/09/xmldsig#rsa-sha1"/><dsig:Reference URI="#ID_79157aa6-38ab-4e5e-a562-78bade9ffb82"><dsig:Transforms><dsig:Transform Algorithm="http://www.w3.org/2000/09/xmldsig#enveloped-signature"/><dsig:Transform Algorithm="http://www.w3.org/2001/10/xml-exc-c14n#"/></dsig:Transforms><dsig:DigestMethod Algorithm="http://www.w3.org/2000/09/xmldsig#sha1"/><dsig:DigestValue>7LaVacKTsP6wnuNlsQ6KASNDgdE=</dsig:DigestValue></dsig:Reference></dsig:SignedInfo><dsig:SignatureValue>jiyC63KG65d019PY7ThZzyojiU6iJMAr9N39uqrPr3HBGPfW7JjwFH9tahsFKjgoQQH7ToRLKZJKvm12TmDured+s+5VyI+Py6TsDiaQCRnNSeARvYdXFwNCA1D8Sx0xDkXKWpgB3YZenBV6U0IZtmAa5CxXFKmdqxEzHweAPq0=</dsig:SignatureValue><dsig:KeyInfo><dsig:KeyValue><dsig:RSAKeyValue><dsig:Modulus>suGIyhVTbFvDwZdx8Av62zmP+aGOlsBN8WUE3eEEcDtOIZgO78SImMQGwB2C0eIVMhiLRzVPqoW1dCPAveTm653zHOmubaps1fY0lLJDSZbTbhjeYhoQmmaBro/tDpVw5lKJns2qVnMuRK19ju2dxpKwlYGGtrP5VQv00dfNPbs=</dsig:Modulus><dsig:Exponent>AQAB</dsig:Exponent></dsig:RSAKeyValue></dsig:KeyValue></dsig:KeyInfo></dsig:Signature><saml:Subject><saml:NameID NameQualifier="urn:picketlink:identity-federation">UserA</saml:NameID><saml:SubjectConfirmation Method="urn:oasis:names:tc:SAML:2.0:cm:bearer"/></saml:Subject><saml:Conditions NotBefore="2013-11-18T18:19:35.955Z" NotOnOrAfter="2013-11-18T20:19:35.955Z"/><saml:AuthnStatement AuthnInstant="2013-11-18T18:19:35.955Z"><saml:AuthnContext><saml:AuthnContextClassRef>urn:oasis:names:tc:SAML:2.0:cm:bearer</saml:AuthnContextClassRef></saml:AuthnContext></saml:AuthnStatement></saml:Assertion>
        [INFO] ------------------------------------------------------------------------
        [INFO] BUILD SUCCESS
        [INFO] ------------------------------------------------------------------------
        [INFO] Total time: 1.404s
        [INFO] Finished at: Mon Nov 18 13:19:36 EST 2013
        [INFO] Final Memory: 7M/146M
        [INFO] ------------------------------------------------------------------------

_Note:_: You also see the following warnings in the server log. These warnings are expected because the quickstart does not provide a configuration that persists tokens.

        INFO  [org.picketlink.common] (http-/127.0.0.1:8080-4) Loading STS configuration
        WARN  [org.picketlink.common] (http-/127.0.0.1:8080-4) Security Token registry option not specified: Issued Tokens will not be persisted!
        WARN  [org.picketlink.common] (http-/127.0.0.1:8080-4) Security Token registry option not specified: Issued Tokens will not be persisted!
        INFO  [org.picketlink.common] (http-/127.0.0.1:8080-4) picketlink-sts.xml configuration file loaded
        WARN  [org.picketlink.common] (http-/127.0.0.1:8080-4) Lifetime has not been specified. Using the default timeout value.


## Undeploy and Remove the Security Domain Configuration

### Undeploy and Remove the Security Domain Using the JBoss CLI

You can undeploy the quickstart and remove the security domain configuration in one easy step using the `undeploy-and-remove-security-domain.cli` script located in the root directory of this quickstart.

1. Open a new command prompt, navigate to the root directory of this quickstart.
2. Run the following command, replacing EAP7_HOME with the path to your server:

        For Linux: EAP7_HOME/bin/jboss-cli.sh --file=undeploy-and-remove-security-domain.cli
        For Windows: EAP7_HOME\bin\jboss-cli.bat --file=undeploy-and-remove-security-domain.cli
   You should see the following result when you run the script:

        The batch executed successfully
        process-state: reload-required

### Undeploy the quickstart and Remove the Security Domain Manually


1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy
4. Stop the JBoss EAP server.
5. Replace the `EAP7_HOME/standalone/configuration/standalone.xml` file with the back-up copy of the file.


## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a JBoss EAP server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts).

1. Be sure to configure the security domain by running the JBoss CLI commands as described above under [Configure the JBoss EAP Server](#configure-the-server). Stop the server at the end of that step.
2. To deploy the server project, right-click on the `picketlink-sts` project and choose `Run As` --> `Run on Server`.
3. You are presented with a server message `PicketLinkSTSRealm` and challenged to enter valid authentication credentials. Enter the following information and then click `OK`.

            UserName: JBoss
            Password: JBoss
   JBoss Developer Studio then displays the welcome file.
4. Follow these steps to test the service.

      * Right-click on the `picketlink-sts` project and choose `Run As` --> `Maven Build`.
      * Enter `picketlink-sts` for the `Name`.
      * Enter `exec:java` for the `Goals:`.
      * Click `Run`.
      * Review the output in the console window.

_NOTE:_ Be sure to [Undeploy and Remove the Security Domain Configuration](#undeploy-and-remove-the-security-domain-configuration) when you have completed testing this quickstart.

## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

      mvn dependency:sources
