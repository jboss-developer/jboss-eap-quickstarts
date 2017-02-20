log4j: Define a Module Dependency and Use log4j in an Application
=================================================================
Author: Bartosz Baranowski  
Level: Beginner  
Technologies: JBoss Modules  
Summary: The `log4j` quickstart demonstrates how to use container defined modules to add dependencies on 3rd party libraries and limit the application package size.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

What is it?
-----------

The `log4j` quickstart is a simple JSF application that shows how to use container defined modules to limit the size of the application package in ${product.name.full}. It also shows how to use common versions of certain classes at runtime.

Applications must often depend on third-party libraries. By default, Java EE packages allow you to include dependencies in a deployable unit which can lead to uncontrolled growth of the deployable unit. This can be avoided by the use of container defined modules. A module is nothing more than a container managed binary dependency which is shared by all deployed applications.

This example is very simple. It declares dependency on the Apache Log4j module which allows it to use a custom logging framework. This is achieved with a simple addition to the xml file: `src/main/webapp/WEB-INF/jboss-deployment-structure.xml`. This file and modular class loading are described in more detail in the [${product.name.full}](https://access.redhat.com/documentation/en/red-hat-jboss-enterprise-application-platform/) documentation.


System requirements
-------------------

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later. 

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


Use of ${jboss.home.name}
---------------

In the following instructions, replace `${jboss.home.name}` with the actual path to your ${product.name} installation. The installation path is described in detail here: [Use of ${jboss.home.name} and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_${jboss.home.name}.md#use-of-eap_home-and-jboss_home-variables).


Start the ${product.name} Server
-------------------------

1. Open a command prompt and navigate to the root of the ${product.name} directory.
2. The following shows the command line to start the server:

        For Linux:   ${jboss.home.name}/bin/standalone.sh
        For Windows: ${jboss.home.name}\bin\standalone.bat

 
Build and Deploy the Quickstart
-------------------------

1. Make sure you have started the ${product.name} server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install wildfly:deploy

4. This will deploy `target/jboss-log4j.war` to the running instance of the server.


Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-log4j/>.


Undeploy the Archive
--------------------

1. Make sure you have started the ${product.name} server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy

Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}). 


Debug the Application
------------------------------------

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

        mvn dependency:sources

