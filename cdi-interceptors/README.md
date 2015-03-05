cdi-interceptors: Example Using CDI Interceptors
=================================================================================
Author: Ievgen Shulga  
Level: Intermediate  
Technologies: JPA, JSF, EJB  
Summary: The `cdi-interceptors` quickstart demonstrates how to use CDI interceptors for cross-cutting concerns such as logging and simple auditing.  
Target Product: JBoss EAP  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

The `cdi-interceptors` quickstart demonstrates how to use CDI interceptors for cross-cutting concerns such as logging and simple auditing in applications deployed to Red Hat JBoss Enterprise Application Platform. 
Interceptors can be applied to any business methods or beans, simply by adding appropriate interceptor binding type annotation. The project contains EJB service that can create and retrieve object from database.
This example demonstrates 2 interceptors: `AuditInterceptor` and `LoggingInterceptor`

The quickstart defines the `@Audit` and `@Logging` interceptor binding types. The `AuditInterceptor` and `LoggingInterceptor` classes are annotated with the binding type annotation and contain a method annotated `@AroundInvoke`. If the interceptor is enabled, this method will be called when the intercepted methods are invoked. In the `ItemServiceBean` bean, notice the `create()`and `getList()` methods are annotated with the `@Audit` and `@Logging` binding types. This means the `aroundInvoke()` method in the `AuditInterceptor` and `LoggingInterceptor` classes will be called when the `ItemServiceBean` bean's `create()` and `getList()` methods are called, but only if that interceptor is enabled.
To enable an interceptor, you must add the interceptor class to the WEB-INF/beans.xml descriptor file.

_Note: This quickstart uses the H2 database included with Red Hat JBoss Enterprise Application Platform 6. It is a lightweight, relational example datasource that is used for examples only. It is not robust or scalable, is not supported, and should NOT be used in a production environment!_
  
_Note: This quickstart uses a `*-ds.xml` datasource configuration file for convenience and ease of database configuration. These files are deprecated in JBoss EAP 6.4 and should not be used in a production environment. Instead, you should configure the datasource using the Management CLI or Management Console. Datasource configuration is documented in the [Administration and Configuration Guide](https://access.redhat.com/documentation/en-US/JBoss_Enterprise_Application_Platform/) for Red Hat JBoss Enterprise Application Platform._

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

4. This will deploy `target/jboss-cdi-interceptors.war` to the running instance of the server.
 

Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-cdi-interceptors>.

You can now comment out classes in the WEB-INF/beans.xml file to disable one or both of the interceptors and view the results.

* Comment the `<class>org.jboss.as.quickstarts.cdi.interceptor.AuditInterceptor</class>` and you will no longer see the audit history on the browser page.
* Comment the `<class>org.jboss.as.quickstarts.cdi.interceptor.LoggingInterceptor</class>` and you will no longer see the log messages in the server log.

In this quickstart, in order to switch back to the default implementation, 
comment the `interceptors` block in the WEB-INF/beans.xml file and redeploy the quickstart.


Server Log: Expected warnings and errors
-----------------------------------

_Note:_ You will see the following warnings in the server log. You can ignore these warnings.

    JBAS010489: -ds.xml file deployments are deprecated. Support may be removed in a future version.

    HHH000431: Unable to determine H2 database version, certain features may not work

Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Run the Arquillian Tests 
-------------------------

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container. 

_NOTE: The following commands assume you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Run the Arquillian Tests](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/RUN_ARQUILLIAN_TESTS.md#run-the-arquillian-tests) for complete instructions and additional options._

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn clean test -Parq-jbossas-remote 


Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a JBoss EAP server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 


Debug the Application
------------------------------------

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

        mvn dependency:sources

