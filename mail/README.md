mail: E-Mail Example using CDI and JSF
======================================
Author: Joel Tosi  
Level: Beginner  
Technologies: JavaMail, CDI, JSF  
Summary: The `mail` quickstart demonstrates how to send email using CDI 1.0 and JSF 2.1 and the default Mail provider that ships with JBoss EAP.  
Target Product: JBoss EAP  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

The `mail` quickstart demonstrates sending email with the use of *CDI 1.0* (Contexts and Dependency Injection) and *JSF 2.1* (JavaServer Faces) in Red Hat JBoss Enterprise Application Platform.

The example uses the default Mail provider that comes out of the box with JBoss EAP. It uses your local mail relay and the default SMTP port of 25.

The configuration of the mail provider is found in the `EAP_HOME/standalone/configuration/standalone.xml` if you are running a standalone server or in the `EAP_HOME/domain/configuration/domain.xml` file if you are running in a managed domain. An example of the mail subsystem XML configuration is provided below:

    <subsystem xmlns="urn:jboss:domain:mail:1.0">
        <mail-session jndi-name="java:jboss/mail/Default" >
            <smtp-server address="localhost" port="25"/>
        </mail-session>
        <mail-session jndi-name="java:/MyOtherMail">
            <smtp-server address="localhost" port="9999">
                <login name="nobody" password="pass"/>
            </smtp-server>
            <pop3-server address="example.com" port="1234"/>
            <imap-server address="example.com" port="432">
                <login name="nobody" password="pass"/>
            </imap-server>
        </mail-session>
    </subsystem>

The example is a web application that takes `To`, `From`, `Subject`, and `Message Body` input and sends mail to that address. The front end is a JSF page with a simple POJO backing, leveraging CDI for resource injection.

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.1 or later. 

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.


Use of EAP_HOME
---------------

In the following instructions, replace `EAP_HOME` with the actual path to your JBoss EAP 6 installation. The installation path is described in detail here: [Use of EAP_HOME and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_EAP_HOME.md#use-of-eap_home-and-jboss_home-variables).


Start the JBoss EAP Server
-------------------------

1. Open a command prompt and navigate to the root of the JBoss EAP directory.
2. The following shows the command line to start the server:

        For Linux:   EAP_HOME/bin/standalone.sh
        For Windows: EAP_HOME\bin\standalone.bat

 
Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/BUILD_AND_DEPLOY.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install jboss-as:deploy

4. This will deploy `target/jboss-mail.war` to the running instance of the server.


Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-mail>. 


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a JBoss EAP server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 


Debug the Application
------------------------------------

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

        mvn dependency:sources

