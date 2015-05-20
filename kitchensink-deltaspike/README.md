kitchensink-deltaspike: Example Using the DeltaSpike @Transactional Annotation
========================
Author: Pete Muir, Bernard Tison  
Level: Intermediate  
Technologies: CDI, JSF, JPA, JPA, JAX-RS, BV, DeltaSpike  
Summary: The `kitchensink-deltaspike` quickstart demonstrates how to give transacational behavior to a CDI bean using the Deltaspike @Transactional Annotation.  
Target Product: JBoss EAP  
Product Versions: EAP 6.1, EAP 6.2, EAP 6.3, WFK 2.7  
Source: <https://github.com/jboss-developer/jboss-wfk-quickstarts/>  

What is it?
-----------

The `kitchensink-deltaspike` quickstart is a deployable Maven 3 project that demonstrates how to create a compliant Java EE 6 application using JSF 2.1, CDI 1.0, JPA 2.0 and Bean Validation 1.0 in Red Hat JBoss Enterprise Application Platform. It uses the `kitchensink` quickstart as its starting point, however, rather than using a Stateless EJB, it leverages the DeltaSpike @Transactional annotation to give transactional behavior to a CDI bean. The entity manager is managed by the application rather than the container. 

The DeltaSpike project (http://incubator.apache.org/deltaspike) consists of a number of portable CDI extensions that provide useful features for Java application developers.

Changes compared to the original `kitchensink` quickstart
---------------------------------------------------------

* The `org.jboss.as.quickstarts.kitchensink.service.MemberRegistration` class is annotated with the DeltaSpike `@Transactional` annotation rather than `@javax.ejb.Stateless`.
* The `org.jboss.as.quickstarts.kitchensink.util.Resources` class has been modified to handle an application managed entity manager.
* The transaction type in the persistence unit configuration file (`src/main/resources/META-INF/persistence.xml`) has been changed to `RESOURCE_LOCAL`. 
* The DeltaSpike `TransactionalInterceptor` has been added to the beans.xml CDI configuration file (`src/main/webapp/WEB-INF/beans.xml`).
* The DeltaSpike dependencies have been added to the project POM.
* The ShrinkWrap `shrinkwrap-resolver-bom` dependency has been added to the project POM, to be able to build the archive for the Arquillian test.
* The JSF components has an Ajax Validation feature that can be enabled/disabled on DeltaSpike configuration file (`src/main/resources/META-INF/apache-deltaspike.properties`).

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform (JBoss EAP) 6.1 or later with the Red Hat JBoss Web Framework Kit (WFK) 2.7.

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.


Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.


Start the JBoss EAP Server
-------------------------

1. Open a command line and navigate to the root of the JBoss EAP directory.
2. The following shows the command line to start the server with the default profile:

        For Linux:   EAP_HOME/bin/standalone.sh
        For Windows: EAP_HOME\bin\standalone.bat

 
Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package jboss-as:deploy

4. This will deploy `target/jboss-kitchensink-deltaspike.war` to the running instance of the server.
 

Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-kitchensink-deltaspike/>.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Run the Arquillian Tests 
-------------------------

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container. 

_NOTE: The following commands assume you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Run the Arquillian Tests](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/RUN_ARQUILLIAN_TESTS.md#run-the-arquillian-tests) for complete instructions and additional options._

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn clean test -Parq-jbossas-remote 

_NOTE: If you use the Maven settings command line argument with this quickstart, you need to pass an additional argument to allow ShrinkWrap Resolver to function properly:_

    mvn clean test -Parq-jbossas-remote -s /path/to/custom/settings.xml -Dorg.apache.maven.user-settings=/path/to/custom/settings.xml


Run the Arquillian Functional Tests
-----------------------------------

This quickstart provides Arquillian functional tests as well. They are located under the directory "functional-tests". Functional tests verify that your application behaves correctly from the user's point of view - simulating clicking around the page as a normal user would do.

To run these tests, you must build the main project as described above.

1. Open a command line and navigate to the root directory of this quickstart.
2. Build the quickstart WAR using the following command:

        mvn clean package

3. Navigate to the functional-tests/ directory in this quickstart.
4. If you have a running instance of the JBoss EAP server, as described above, run the remote tests by typing the following command:

        mvn clean verify -Parq-jbossas-remote

5. If you prefer to run the functional tests using managed instance of the JBoss EAP server, meaning the tests will start the server for you, type fhe following command:

        mvn clean verify -Parq-jbossas-managed


Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
