PicketLink Federation: WS-Trust Security Token Service 
======================================================
 
This project is an implementation of a WS-Trust Security Token Service.

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.1 or later.

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.


Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#configure-maven) before testing the quickstarts.


Additional configuration options
--------------------------------

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



