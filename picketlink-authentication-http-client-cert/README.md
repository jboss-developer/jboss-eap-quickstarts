picketlink-authentication-http-client-cert: PicketLink HTTP CLIENT-CERT Authentication
===============================
Author: Pedro Igor
Level: Beginner
Technologies: CDI, PicketLink
Summary: Basic example that demonstrates simple username/password authentication using the HTTP CLIENT-CERT scheme
Target Product: EAP
Source: <https://github.com/jboss-jdf/jboss-as-quickstart/>


What is it?
-----------

This example demonstrates the use of *CDI 1.0* and *PicketLink* in *JBoss Enterprise Application Platform 6* or *JBoss AS 7*.

You'll learn from this quickstart how to use PicketLink to authenticate users using the HTTP CLIENT-CERT scheme.

The application is configured to provide public access for some resources(eg.: /index.html) and to protected others for
authenticated users only(eg.: /protected/*).

Identity data such as users, roles and groups, are managed using PicketLink IDM backed by a file-based identity store.
This store is used by default when no specific configuration is provided.

Before running this example, you must configure your server  installation to use SSL and to validate client certificates.

First, go to the following directory:

    For Linux:   JBOSS_HOME/standalone/configuration
    For Windows: JBOSS_HOME\standalone\configuration

Create a certificate for your server using the following command:

    keytool -genkey -alias server -keyalg RSA -keystore server.keystore -storepass change_it -validity 365

You'll be prompted for some additional information, you can provide the values your want.

Now, let's create the client certificate, which you'll use to authenticate against the server when accessing a resource
through SSL.

    keytool -genkey -alias client -keystore client.keystore -storepass change_it -validity 365 -keyalg RSA -keysize 2048 -storetype pkcs12

Now we need to export the client's certificate and create a truststore by importing this certificate:

    keytool -exportcert -keystore client.keystore  -storetype pkcs12 -storepass change_it -alias client -keypass change_it -file client.cer
    keytool -import -file client.cer -alias client -keystore client.truststore

Now that we have our certificates/keystores properly configured, you need to change your server installation to enable ssl.
Add the following connector to the web subsystem:

    <connector name="https" protocol="HTTP/1.1" scheme="https" socket-binding="https" enable-lookups="false" secure="true">
        <ssl name="localhost-ssl" key-alias="server" password="change_it"
            certificate-key-file="${jboss.server.config.dir}/server.keystore"
            protocol="TLSv1"
            verify-client="want"
            ca-certificate-file="${jboss.server.config.dir}/client.truststore"/>
    </connector>

You can now restart your server and check if it is responding on:

    https://localhost:8443

If everything is ok, you will be asked to trust the server certificate.

Before accessing the application, please import the *client.cer*, which holds the client certificate, to your browser.
When you access the application, the browser should ask you which certificate you want to use to authenticate with the server.
Select it and you're ready to go.

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

4. This will deploy `target/jboss-as-picketlink-authentication-http-client-cert.war` to the running instance of the server.


Access the application 
---------------------

The application will be running at the following URL: <https://localhost:8443/jboss-as-picketlink-authentication-http-client-cert>.


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