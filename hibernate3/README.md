hibernate3: How to Use  Hibernate 3 in an Application
=====================================================
Author: Bartosz Baranowski   
Level: Intermediate  
Technologies: Hibernate 3  
Summary: Example that uses Hibernate 3 for database access. Compare the code in this quickstart to the _hibernate4_ quickstart to see the changes needed to upgrade to Hibernate 4.  
Target Product: EAP  
Product Versions: EAP 6.1, EAP 6.2  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

_Note: Hibernate 3.x is not a supported configuration in Red Hat JBoss Enterprise Application Platform 6.1 or later._

The sole purpose of this quickstart is to help you understand the changes needed to move your application from 
Hibernate 3.x to Hibernate 4. This quickstart has the same functionality as the `hibernate4` quickstart 
but uses the Hibernate 3 libraries. Compare this quickstart to the `hibernate4` quickstart to see the 
code and class differences between Hibernate 3 and Hibernate 4. 

This quickstart, like the `log4j` quickstart, demonstrates how to define a module dependency. However, this quickstart goes beyond that and also demonstrates the following:
 
* WAR creation - The Maven script and Maven WAR plugin create a *WAR* archive that includes ONLY the Hibernate 3.x binaries. To understand better how this is achieved, please refer to the *pom.xml* in the root directory of this quickstart. Additional information can be found in the <http://maven.apache.org/plugins/maven-war-plugin> documentation.
* Module exclusion and inclusion - This example demonstrates how to control class loading using *dependencies* and *exclusions* in the *jboss-deployment-structure.xml* file. For more information about this file, please refer to <https://docs.jboss.org/author/display/AS7/Developer+Guide#DeveloperGuide-JBossDeploymentStructureFile>
* Persistence configuration - Configuration is required to tell the container how to load JPA/Hibernate.
 

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.1 or later. 

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.

_Note: This quickstart uses the H2 database included with JBoss EAP 6. It is a lightweight, relational example datasource that is used for examples only. It is not robust or scalable and should NOT be used in a production environment!_
  
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#configure-maven) before testing the quickstarts.


Start the JBoss Server
-------------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat

 
Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install jboss-as:deploy

4. This will deploy `target/jboss-hibernate3.war` to the running instance of the server.


Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-hibernate3/>.


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

