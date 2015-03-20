spring-kitchensink-matrixvariables: Kitchensink MatrixVariables Using Spring 4.x
========================================================================================
Author: Marius Bogoevici, Tejas Mehta, Joshua Wilson  
Level: Intermediate  
Technologies: JSP, JPA, JSON, Spring, JUnit  
Summary: The `spring-kitchensink-matrixvariables` quickstart showcases Spring 4.x's support for **Matrix Variables** in URLs that was introduced in Spring 3.2.  
Target Product: JBoss EAP  
Product Versions: EAP 6.1, EAP 6.2, EAP 6.3  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

This is your project! It is a sample, deployable Maven 3 project to help you get your foot in the door developing with 
Java EE 6 and Spring in Red Hat JBoss Enterprise Application Platform 6.1 or later.

This project is setup to allow you to create a compliant Java EE 6 application using JSP, JPA 2.0 and Spring 4.x. It 
includes a persistence unit and some sample persistence and transaction code to introduce you to database access in enterprise Java:

* This module showcases Spring 4.x's support for **Matrix Variables** in urls introduced in Spring 3.2.

* In `jboss-as-spring-mvc-context.xml` `<context:component-scan base-package="org.jboss.as.quickstarts.kitchensink.spring.matrixvariables.controller"/>` 
and `<mvc:annotation-driven/>` are used to register both the non-rest and rest controllers.  This is how it works normally, 
however if we want to use `@MatrixVariable` we must set the `removeSemicolonContent` property of `RequestMappingHandlerMapping` to `false`. 
This has been done by commenting out `<mvc:annotation-driven/>` and using `<bean class='org.jboss.as.quickstarts.kitchensink.spring.matrixvariables.config.WebConfig'/>`.
Then in the WebConfig class we set the `removeSemicolonContent` property to `false`.

* The controllers map the respective urls to methods using `@RequestMapping(url)`.

* To return JSON, the rest controller uses `@ResponseBody`.

* The datasource and entitymanager are retrieved via JNDI.

* An additional form is added in `index.jsp` which allows the user to filter the member list. The form is submitted in 
the url form: `/filter;n=Name;e=Email`.

* Using `@MatrixVariable` the controller method captures the values and feeds them to the memberDao.


System Requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform (JBoss EAP) 6.1 or later.

To run the quickstart with the provided build script, you need the following:

1. Java 1.6, to run JBoss and Maven. You can choose from the following:
    * OpenJDK
    * Oracle Java SE
    * Oracle JRockit

2. Maven 3.0.0 or later, to build and deploy the examples
    * If you have not yet installed Maven, see the [Maven Getting Started Guide](http://maven.apache.org/guides/getting-started/index.html) for details.
    * If you have installed Maven, you can check the version by typing the following in a command line:

            mvn --version 

3. The JBoss EAP distribution ZIP.
    * For information on how to install and run JBoss, refer to the product documentation.


Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.


Start the JBoss EAP Server
-------------------------

1. Open a command line and navigate to the root of the JBoss EAP directory.
2. The following shows the command line to start the server:

        For Linux:   EAP_HOME/bin/standalone.sh
        For Windows: EAP_HOME\bin\standalone.bat

 
Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include 
Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) 
for complete instructions and additional options._

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install jboss-as:deploy

4. This will deploy `target/jboss-spring-kitchensink-matrixvariables.war` to the running instance of the server.


Access the application
----------------------

The application will be running at the following URL: <http://localhost:8080/jboss-spring-kitchensink-matrixvariables/>.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Run the Arquillian Functional Tests
-----------------------------------

This quickstart provides Arquillian functional tests as well. They are located in the functional-tests/ subdirectory under 
the root directory of this quickstart. Functional tests verify that your application behaves correctly from the user's point 
of view. The tests open a browser instance, simulate clicking around the page as a normal user would do, and then close the browser instance.

To run these tests, you must build the main project as described above.

1. Open a command line and navigate to the root directory of this quickstart.
2. Build the quickstart WAR using the following command:

        mvn clean package

3. Navigate to the functional-tests/ directory in this quickstart.
4. If you have a running instance of the JBoss EAP server, as described above, run the remote tests by typing the following command:

        mvn clean verify -Parq-jbossas-remote

5. If you prefer to run the functional tests using managed instance of the JBoss EAP server, meaning the tests will start the 
server for you, type the following command:

        mvn clean verify -Parq-jbossas-managed


Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 


Debug the Application
---------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following 
commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
