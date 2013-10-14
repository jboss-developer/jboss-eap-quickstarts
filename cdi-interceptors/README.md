cdi-interceptors: Example Using CDI-interceptors
=================================================================================
Author: Ievgen Shulga
Level: Intermediate
Technologies: JPA,JSF,EJB
Summary: Demonstrates using cdi-interceptors for logging and auditing
Target Product: EAP
Product Versions: EAP 6.1, EAP 6.2
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>

What is it?
-----------
The quickstart demonstrates using CDI interceptors for cross-cutting concerns such as logging and simple auditing. 
Interceptors can be applied to any business methods or beans, simply by adding appropriate interceptor binding type annotation. The project contains EJB service that can create and retrieve object from database.
This example demonstrates 2 interceptors: `AuditInterceptor` and `LoggingInterceptor`

The quickstart defines the `@Audit` and `@Logging` interceptor binding types. The `AuditInterceptor` and `LoggingInterceptor` classes are annotated with the binding type annotation and contain a method annotated `@AroundInvoke`. If the interceptor is enabled, this method will be called when the intercepted methods are invoked. In the `ItemServiceBean` bean, notice the `create()`and `getList()` methods are annotated with the `@Audit` and `@Logging` binding types. This means the `aroundInvoke()` method in the `AuditInterceptor` and `LoggingInterceptor` classes will be called when the `ItemServiceBean` bean's `create()` and `getList()` methods are called, but only if that interceptor is enabled.
To enable an interceptor, you must add the interceptor class to the WEB-INF/beans.xml descriptor file.

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.1 or later. 

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.
 
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

4. This will deploy `target/jboss-cdi-interceptors.war` to the running instance of the server.
 

Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-cdi-interceptors>.

You can now comment out classes in the WEB-INF/beans.xlm file to disable one or both of the interceptors and view the results.

* Comment the `<class>org.jboss.as.quickstarts.cdi.interceptor.AuditInterceptor</class>` and you will no longer see the audit history on the browser page.
* Comment the `<class>org.jboss.as.quickstarts.cdi.interceptor.LoggerInterceptor</class>` and you will no longer see the log messages in the server log.

In this quickstart, in order to switch back to the default implementation, 
comment the `interceptors` block in the WEB-INF/beans.xml file and redeploy the quickstart.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Run the Arquillian Tests 
-------------------------

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container. 

_NOTE: The following commands assume you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Run the Arquillian Tests](../README.md#run-the-arquillian-tests) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn clean test -Parq-jbossas-remote 


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
