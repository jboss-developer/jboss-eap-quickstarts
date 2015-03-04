cdi-alternative: Demonstrates CDI Alternatives
======================================================
Author: Nevin Zhu  
Level: Intermediate  
Technologies: CDI, Servlet, JSP  
Summary: The `cdi-alternative` quickstart demonstrates how to create a bean that can be implemented for different purposes without changing the source code.   
Target Product: JBoss EAP  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

The `cdi-alternative` quickstart demonstrates how to create a bean that can be implemented for different purposes without changing the source code in Red Hat JBoss Enterprise Application Platform. Instead, you choose the bean implementation during development by injecting a qualifier. Then at deployment time, rather than modify the source code, you choose the alternative.

Alternatives are commonly used for purposes like the following:

1. To handle client-specific business logic that is determined at runtime.
2. To specify beans that are valid for a particular deployment scenario, for example, when country-specific sales tax laws require country-specific sales tax business logic.
3. To create dummy or mock versions of beans to be used for testing.

Any java class which has a no-args constructor and is in an archive with a beans.xml is available for lookup and injection. 
For EL resolution, it must contain @Named


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
        
4. This will deploy `target/jboss-cdi-alternative.war` to the running instance of the server.


Access the application
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-cdi-alternative>.

You can specify alternative versions of the bean in the WEB-INF/beans.xml file by doing one of the following:

1. You can remove the `<alternatives>` tag
2. You can change the class name.

In this quickstart, in order to switch back to the default implementation, 
comment the `<alternatives>` block in the WEB-INF/beans.xml file and redeploy the quickstart.

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
   

