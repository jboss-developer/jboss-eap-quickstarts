cdi-stereotype: Example Using CDI-stereotype.
=================================================================================
Author: Ievgen Shulga  
Level: Intermediate  
Technologies: JPA,JSF,EJB  
Summary: Demonstrates using cdi-stereotype for logging and auditing  
Target Product: EAP  
Product Versions: EAP 6.1, EAP 6.2, EAP 6.3  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------
A stereotype is an annotation, annotated `@Stereotype`, that packages several other annotations.
This quickstart is an extension of the `cdi-interceptors` quickstart and demonstrates how to use a CDI stereotype. Stereotypes allow a developer to declare common metadata for beans in a central place. A stereotype is an annotation, annotated `@Stereotype`, that packages several other annotations.
In this example, the stereotype encapsulates the following :

* All beans with this stereotype inherit the following interceptor bindings: @Logging and @Audit
* All beans with this stereotype are alternatives

Quickstart defines stereotype with 2 interceptors bindings (`@Logging` and `@Audit`) to be inherited by all beans with that stereotype. It also indicates that all beans to which it is applied are `@Alternatives`. An alternative stereotype lets us classify beans by deployment scenario.
Arquillian tests added in cdi-interceptors quickstart.

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.1 or later. 

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.


Start the JBoss EAP Server
-------------------------

1. Open a command prompt and navigate to the root of the JBoss EAP directory.
2. The following shows the command line to start the server:

        For Linux:   EAP_HOME/bin/standalone.sh
        For Windows: EAP_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install jboss-as:deploy

4. This will deploy `target/jboss-cdi-stereotype.war` to the running instance of the server.
 

Access the application 
---------------------

The application will be running at the following URL <http://localhost:8080/jboss-cdi-stereotype>

You can now comment out classes in the WEB-INF/beans.xml file to disable one or both of the interceptors or alternative stereotype and view the results.

* Comment the `<class>org.jboss.as.quickstarts.cdi.interceptor.AuditInterceptor</class>` and you will no longer see the audit history on the browser page.
* Comment the `<class>org.jboss.as.quickstarts.cdi.interceptor.LoggingInterceptor</class>` and you will no longer see the log messages in the server log.
* Comment the `<stereotype>org.jboss.as.quickstarts.cdi.interceptor.ServiceStereotype</stereotype>` and you no longer see ItemAlternativeServiceBean implementation invoked.

In this quickstart, in order to switch back to the default implementation, 
uncomment the `<interceptors>` and `<stereotype>` block in the WEB-INF/beans.xml file and redeploy the quickstart.


Server Log: Expected warnings and errors
-----------------------------------

_Note:_ You will see the following warnings and errors in the server log. Hibernate attempts to drop the table and constraints before they are created because the `hibernate.hbm2ddl.auto` value is set to `create-drop`. You can ignore these warnings and errors.

        HHH000431: Unable to determine H2 database version, certain features may not work

        HHH000389: Unsuccessful: drop sequence hibernate_sequence
        Sequence "HIBERNATE_SEQUENCE" not found; SQL statement: drop sequence hibernate_sequence [90036-168]


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JDBS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
