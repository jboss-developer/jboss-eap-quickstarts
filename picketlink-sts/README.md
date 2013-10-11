picketlink-sts: PicketLink Federation: WS-Trust Security Token Service 
======================================================
Author: Peter Skopek
Level: Advanced
Technologies: WS-Trust, SAML
Summary: This project is an implementation of a WS-Trust Security Token Service.
Target Product: EAP
Product Versions: EAP 6.1, EAP 6.2
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>
 

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

For more information about PicketLink [see:](http://docs.jboss.org/picketlink/2/2.1.7.Final/reference/html/) 


Configure the Security Domain Using the JBoss CLI 
-------------------------------------------------

1. Start the JBoss server by typing the following:

        For Linux:  JBOSS_HOME/bin/standalone.sh
        For Windows:  JBOSS_HOME\bin\standalone.bat
2. To configure security domain run the JBoss CLI tool, open a new command line, navigate to the JBOSS_HOME directory, and type the following:

        For Linux: bin/jboss-cli.sh --file=<your quickstarts directorey>/picketlink-sts/configure-security-domain.cli 
        For Windows: bin\jboss-cli.bat --file=<your quickstarts directorey>\picketlink-sts\configure-security-domain.cli 

   In case running controller on different host use --controller=host:port.


Build and Deploy PicketLink STS
-------------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install jboss-as:deploy
4. This will deploy `target/picketlink-sts.war` to the running instance of the server.


Further actions
---------------

This quickstart is just a service application for another applications you can use. See more examples in [PicketLink project documemntation.](http://docs.jboss.org/picketlink/2/2.1.7.Final/reference/html/ch01.html#sid-819345). You can take a look for more applications to play with at [PicketLink Quickstarts.](https://docs.jboss.org/author/display/PLINK/PicketLink+Quickstarts)


