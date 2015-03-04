cdi-decorator: Demostrates CDI Decorator
======================================================
Author: Ievgen Shulga  
Level: Intermediate  
Technologies: CDI, JSF  
Summary: The `cdi-decorator` quickstart demonstrates the use of a CDI Decorator to intercept bean methods and modify the business logic.  
Target Product: JBoss EAP  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------
The `cdi-decorator` quickstart demonstrates the use of CDI Decorator in Red Hat JBoss Enterprise Application Platform.

It represents a common decorator design pattern. We take a class and we wrap decorator class around it. 
When we call the class, we always pass through the surrounding decorator class before we reach the inner class. 
In this example, the decorator class simply changes the staff bonus from '100' to '200' and the staff position from 'Java Developer' to 'Team Lead'. It then logs a message to the server console.

By default, all decorators are disabled, so application will run without using decorator. We need to enable our decorator in the 'beans.xml' descriptor to make it work.


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
        
4. This will deploy `target/jboss-cdi-decorator.war` to the running instance of the server.


Access the application
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-cdi-decorator>.

You can specify decorator of the bean in the WEB-INF/beans.xml file by doing one of the following:

1. You can add a decorators tag and specify a decorator class.
2. You can specify a different decorator class name in the decorators tag.

For this example, uncomment the `<decorators>` tag in the WEB-INF/beans.xml file and redeploy the application. 
When you access the application, you will see changed information from web-browser and following in the server log: `CDI decorator method was called!`

In order to switch back to the default implementation, 
comment the 'decorators' block in the WEB-INF/beans.xml file and redeploy the quickstart.

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
   

