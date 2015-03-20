spring-greeter: Greeter Example using Spring 4.x
======================================================
Author: Marius Bogoevici  
Level: Beginner  
Technologies: Spring MVC, JSP, JPA 2.0  
Summary: The `spring-greeter` quickstart is based on the `greeter` quickstart, but differs in that it uses Spring MVC for Mapping GET and POST requests.  
Target Product: JBoss EAP  
Product Versions: EAP 6.1, EAP 6.2, EAP 6.3  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is this?
-------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform (JBoss EAP) 6.1 or later.

The `spring-greeter` quickstart is based on the `greeter` quickstart, but differs in that it uses Spring MVC for Mapping GET and POST requests:

* `<mvc:annotation-driven\>` configured in `src/main/webapp/WEB-INF/spring-mvc-context.xml` tells Spring to look for 
`@RequestMapping` in our controllers.
* Spring then routes the HTTP requests to the correct methods in `CreateController.java` and `GreetController`

Spring's XML configurations are used to get hold of the database and entity manager (via jndi) to perform transactional operations:

* `<tx:jta-transaction-manager/>` and `<tx:annotation-driven/>` are configured in `/src/main/webapp/WEB-INF/spring-business-context.xml`
* Methods in UserDaoImpl are marked as `@Transactional`, which Spring, using aspect oriented programming, surrounds with 
boilerplate code to make the methods transactional

When you deploy this example, two users are automatically created for you: emuster and jdoe. This data is located in the 
`src/main/resources/init-db.sql` file.


To test this example:

1. Enter a name in the username field and click on Greet!.
2. If you enter a username that is not in the database, you get a message No such user exists!.
3. If you enter a valid username, you get a message "Hello, " followed by the user's first and last name.
4. To create a new user, click the Add a new user link. Enter the username, first name, and last name and then click Add User. 
The user is added and a message displays the new user id number.
5. Click on the Greet a user! link to return to the Greet! page.

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform (JBoss EAP) 6.1 or later.

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.

Configure Maven
---------------

If you have not yet done so, you must Configure Maven before testing the quickstarts.

Start the JBoss EAP Server
---------------

1. Open a command line and navigate to the root of the JBoss EAP directory.
2. The following shows the command line to start the server with the default profile:

        For Linux:   EAP_HOME/bin/standalone.sh
        For Windows: EAP_HOME\bin\standalone.bat

Build and Deploy the Quickstart
----------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include 
Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) 
for complete instructions and additional options._

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:
        mvn clean package jboss-as:deploy

4. This will deploy target/spring-greeter.war to the running instance of the server.

If you don't have maven configured you can manually copy target/spring-greeter.war to EAP_HOME/standalone/deployments.

Access the application
----------------------

The application will be running at the following URL: <http://localhost:8080/jboss-spring-greeter>

Undeploy the Archive
---------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy

Or you can manually remove the application by removing spring-greeter.war from EAP_HOME/standalone/deployments

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
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following 
commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
