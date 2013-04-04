booking-mvc-spring: Booking MVC Example using Spring 3.1
======================================================
Author: Marius Bogoevici
Level: Advanced
Technologies: JPA 2.0, Junit, JMX, Spring Security, Spring Webflow, Spring Test, and Thymeleaf
Summary: This example demonstrates the use of JPA 2.0, Junit, JMX, Spring Security, Spring Webflow, Spring Test, and Thymeleaf
Target Product: WFK
Source: <https://github.com/jboss-jdf/jboss-as-quickstart/>

What is this?
-------------

This quickstart shows how to run the [Spring BookingMVC](<https://github.com/SpringSource/spring-webflow-samples/tree/master/booking-mvc>) Application in JBoss Enterprise Application Platform and Wildfly. The main modification needed to get the Spring BookingMVC to work was the removal of `<property name="saveOutputToFlashScopeOnRedirect" value="true"/>` from `FlowHandlerAdapter` in `webmvc-config.xml`. This is because the property was recently added in a M1 release and not yet in released in a Final Version. Additionally, the pom.xml was changed to make use of JDF-Boms, in paticular the following: jboss-javaee-6.0-with-hibernate, jboss-javaee-6.0-with-spring, and jboss-javaee-6.0-with-tools.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7.

Spring Travel showcases the use of Spring Webflow. It configures the flow of booking a hotel in `src/main/webapp/WEB-INF/hotels/booking/webflow.xml`.

Urls are mapped by `src/main/webapp/WEB-INF/config/webmvc-config.xml` and `HotelsController.java` and the various `views.xml` files configure which pages are rendered through tile definitions.

There are 4 users and 23 hotels populated in the database by `src/main/resources/import.sql`.

Security during the flow of booking a hotel is configured in `src/main/webapp/WEB-INF/config/spring/security-config.xml` using Spring Security. It uses an embedded `<authenciation-provider>` and a custom login page.

System requirements
-------------------

1. If you have not yet done so, you must Configure Maven before testing the quickstarts.
2. Start JBoss Enterprise Application Platform 6 or JBoss AS 7 with the Web Profile
3. Open a command line and navigate to the root of the JBoss server directory.
4. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat

Configure Maven
---------------

If you have not yet done so, you must Configure Maven before testing the quickstarts.

Start JBoss Enterprise Application Platform 6 or JBoss AS 7 with the Web Profile
---------------
Open a command line and navigate to the root of the JBoss server directory.

The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat

Start JBoss Enterprise Application Platform 6 or JBoss AS 7
-------------------------

Start JBoss Enterprise Application Platform 6 or JBoss AS 7 with the Web Profile


Build and Deploy the Quickstart
-------------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package jboss-as:deploy

4. This will deploy `target/jboss-as-travel-spring.war` to the running instance of the server.

If you don't have maven configured you can manually copy `target/jboss-as-travel-spring.war` to `JBOSS_HOME/standalone/deployments`.

Access the application
----------------------

The application will be running at the following URL: <http://localhost:8080/jboss-as-booking-mvc-spring>.

Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy

Or you can manually remove the application by removing jboss-as-travel-spring.war from JBOSS_HOME/standalone/deployments

Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------------------------

You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse)

Debug the Application
--------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
