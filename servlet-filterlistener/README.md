servlet-filterlistener: How to Write Servlet Filters and Listeners
================================================================
Author: Jonathan Fuerth  
Level: Intermediate  
Technologies: Servlet Filter, Servlet Listener  
Summary: The `servlet-filterlistener` quickstart demonstrates how to use Servlet filters and listeners in an application.  
Target Product: JBoss EAP  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

The `servlet-filterlistener` quickstart demonstrates how to use Servlet filters and listeners in Red Hat JBoss Enterprise Application Platform. 

This example contains the following classes:

* `FilterExampleServlet`: A simple servlet that prints a form that contains an input field and a *Send* button. 
* `VowelRemoverFilter`: A servlet filter that removes the vowels from the text entered in the form.
* `ParameterDumpingRequestListener`: A simple servlet request listener that creates a map of the original HTTP request parameter values before the filter is applied.


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

4. This will deploy `target/jboss-servlet-filterlistener.war` to the running instance of the server.


Access the application 
---------------------

The application will be running at the following URL <http://localhost:8080/jboss-servlet-filterlistener/>.

You are presented with a form containing an input field and a *Send* button. To test the quickstart:

1. Enter some text in the field, for example: 

        This is only a test!
2. Click *Send*.
3. The servlet filter intercepts and removes the vowels from the text entered in the form and displays: 

        You Typed: Ths s nly tst!
4. The server console displays the following log messages.

   The following messages appear in the log when you access the application URL. This is because the ParameterDumpingRequestListener, which is a ServletRequestListener, and the VowelRemoverFilter both map to the application context and print logs for every request. Note that the VowelRemoveFilter contains logic to handle empty input field arguments.

        INFO  [org.apache.catalina.core.ContainerBase.[jboss.web].[default-host].[/jboss-servlet-filterlistener]] (http-/127.0.0.1:8080-1) ParameterDumpingRequestListener: request has been initialized. It has 0 parameters:
        INFO  [org.apache.catalina.core.ContainerBase.[jboss.web].[default-host].[/jboss-servlet-filterlistener]] (http-/127.0.0.1:8080-1) VowelRemoverFilter invoking filter chain...
        INFO  [org.apache.catalina.core.ContainerBase.[jboss.web].[default-host].[/jboss-servlet-filterlistener]] (http-/127.0.0.1:8080-1) VowelRemoverFilter done filtering request
        INFO  [org.apache.catalina.core.ContainerBase.[jboss.web].[default-host].[/jboss-servlet-filterlistener]] (http-/127.0.0.1:8080-1) ParameterDumpingRequestListener: request has been destroyed
        
    The following messages appear in the log when you type "This is only a test!" in the input field and click `Send`. 
        
        INFO  [org.apache.catalina.core.ContainerBase.[jboss.web].[default-host].[/jboss-servlet-filterlistener]] (http-/127.0.0.1:8080-1) ParameterDumpingRequestListener: request has been initialized. It has 1 parameters:
        INFO  [org.apache.catalina.core.ContainerBase.[jboss.web].[default-host].[/jboss-servlet-filterlistener]] (http-/127.0.0.1:8080-1)   userInput=This is only a test!
        INFO  [org.apache.catalina.core.ContainerBase.[jboss.web].[default-host].[/jboss-servlet-filterlistener]] (http-/127.0.0.1:8080-1) VowelRemoverFilter invoking filter chain...
        INFO  [org.apache.catalina.core.ContainerBase.[jboss.web].[default-host].[/jboss-servlet-filterlistener]] (http-/127.0.0.1:8080-1) VowelRemoverFilter done filtering request
        INFO  [org.apache.catalina.core.ContainerBase.[jboss.web].[default-host].[/jboss-servlet-filterlistener]] (http-/127.0.0.1:8080-1) ParameterDumpingRequestListener: request has been destroyed


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

