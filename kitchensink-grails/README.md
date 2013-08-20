kitchensink-grails: Kitchensink using Grails 2.2.3
====================================================================================
Author: Tejas Mehta
Level: Begineer
Technologies: GSP, Hibernate, Grails, Spring
Summary: An example that incorporates multiple technologies
Target Product: EAP
Source: <https://github.com/jboss-jdf/jboss-as-quickstart/>

What is it?
-----------

This is your project! It is a sample, deployable Maven 3 project to help you get your foot in the door developing with Java EE 6 on JBoss Enterprise Application Platform 6 or JBoss AS 7.

This project is setup to allow you to create a compliant Java EE 6 application using GSP, JPA 2.0 and Grails. It includes a persistence unit and some sample persistence and transaction code to introduce you to database access in enterprise Java:

This is a Grails project, whose dependencies are managed by Maven. The `pom.xml` defines the `<packaging>` as `grails-app`, which Maven, using the `grails-maven-plugin`, delegates the task to Grails.

Grails endorses the principle of convention over configuration and but deviates from Maven's src/java/main... configuration and opts for a Rails like structure. All the controllers/domain/services classes are based in the `grails-app` dir.

Grails also derives from Spring MVC and thus has the typical Model, View, Controller setup. In `grails-app/controllers/kitchensink/grails/` are the controllers, both the REST and the Non-REST. The `MemberDaoService` is in the `grails-app/services/kitchensink/grails/`.

All the configuration files are in `grails/conf` dir. The `DataSource.groovy` configures the H2 datasource and Hibernate. In `BootStrap.groovy` we create initial entry for the Database. The url mappings to the controllers are mapped in `UrlMappings.groovy`. Finally, to tell Grails that Maven is the dependency manager, we set `pom true` in `BuildConfig.groovy`

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7.

Configure Maven
---------------

_NOTE: If you have not yet done so, you must Configure Maven before testing the quickstarts._

1. Start JBoss Enterprise Application Platform 6 or JBoss AS 7 with the Web Profile
2. Open a command line and navigate to the root of the JBoss server directory.
3. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat

Build and Deploy the Quickstart
-------------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package jboss-as:deploy

4. This will deploy `target/jboss-as-kitchensink-grails.war` to the running instance of the server.

If you don't have maven configured you can manually copy `target/jboss-as-kitchensink-grails.war` to JBOSS_HOME/standalone/deployments.

Access the application
----------------------

The application will be running at the following URL: <http://localhost:8080/jboss-as-kitchensink-grails/>.

Undeploy the Archive
----------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy

Or you can manually remove the application by removing `jboss-as-kitchensink-grails.war` from `JBOSS_HOME/standalone/deployments`

Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------

You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts)


Debug the Application
---------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
