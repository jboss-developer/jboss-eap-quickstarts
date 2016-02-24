wicket-war: Wicket Framework used in a WAR.
===========================================
Author: Ondrej Zizka <ozizka@redhat.com>
Level: Intermediate
Technologies: Apache Wicket 7.x, JPA
Summary: Demonstrates how to use the Wicket Framework 7.2 with the JBoss EAP server using the Wicket-Stuff Java EE integration packaged as a WAR
Target Product: EAP
Source: <https://github.com/jboss-jdf/jboss-as-quickstart/>

What is it?
-----------

This is an example of how to use Wicket Framework 7.2 with JBoss EAP, leveraging features of Java EE 7, using the Wicket-Stuff Java EE integration.

Features used:

 * Injection of `@PersistenceContext`
 * Injection of a value from `web.xml` using `@Resource`
 * Injection of a stateless session bean using `@EJB`

This is a WAR version.


System requirements
-------------------

All you need to build this project is Java 8.0 (Java SDK 1.8) or better, Maven 3.3.3 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 7 or JBoss WildFly 10.


Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#configure-maven) before testing the quickstarts.


Start JBoss Enterprise Application Platform 7 or JBoss WildFly 10.
-------------------------

1. Open a command line and navigate to the root of the server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat



Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package wildfly:deploy

4. This will deploy `target/jboss-eap-wicket-war.war` to the running instance of the server.



Access the application
----------------------

Access the running application in a browser at the following URL:  <http://localhost:8080/jboss-eap-wicket-war>

You will see a page with a table listing user entities. Initially, this table is empty.  By clicking a link, you can add more users.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadoc of any library in the project,
run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
