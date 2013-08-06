petclinic-spring: PetClinic Example using Spring 3.1
======================================================
Author: Ken Krebs, Juergen Hoeller, Rob Harrop, Costin Leau, Sam Brannen, Scott Andrews
Level: Advanced
Technologies: JPA 2.0, Junit, JMX, Spring MVC Annotations, Spring Data, JSP, webjars, and Dandellion
Summary: An example that incorporates multiple technologies in JBoss Enterprise Application Platform 6 or JBoss AS 7.
Target Product: WFK
Source: <https://github.com/jboss-jdf/jboss-as-quickstart/>

What is it?
-----------
_This quickstart shows how to run the [Spring PetClinic](<http://static.springsource.org/docs/petclinic.html>) Application in JBoss Enterprise Application Platform and Wildfly. The only modification needed to get the Spring PetClinic to work is the addtion of the `webapp/WEB-INF/jboss-deployment-structure.xml` file. This file specifies which modules to include or exclude when building the application. In this case, we exclude Hibernate libraries since the application uses Spring Data JPA. Additionally, this is only required when using the spring-data-jpa profile, see `webapp/WEB-INF/web.xml`._

For detailed explanation of the changes made to adapt the Quickstart to JBOSS see: [CHANGES.md](CHANGES.md)

PetClinic features alternative DAO implementations and application
configurations for JDBC, JPA, and Spring Data JPA, with HSQLDB and MySQL as
target databases. The default PetClinic configuration is JDBC on HSQLDB.
See `src/main/resources/spring/data-access.properties` as well as `web.xml` and
`business-config.xml` in the `src/main/resources/spring` folder for
details. A simple comment change in `web.xml` switches between the data
access strategies.

All versions of PetClinic also demonstrate JMX support
via the use of `<context:mbean-export/>` in `resources\spring\tools-config.xml` for exporting MBeans.
The `CallMonitorAspect.java` is exposed using Spring's `@ManagedResource` and `@ManagedOperation`
annotations and with `@Around` annotation we add monitoring around all
`org.springframework.stereotype.Repository *` functions. You can start up the JDK's
JConsole to manage the exported bean.

The use of `@Cacheable` is also demonstrated in `ClinicServiceImpl.java` by caching the reuslts of the method `findVets`.
The cacheManager in configured in `tools-config.xml` and `ehcache.xml` specifies the `vets` cache properties.

The default transaction manager for JDBC is DataSourceTransactionManager and for JPA and Spring Data JPA, JpaTransactionManager.
Those local strategies allow for working with any locally defined DataSource. These are defined in the `business-config.xml`

_Note that the sample configurations for JDBC, JPA, and Spring Data JPA configure
a BasicDataSource from the Apache Commons DBCP project for connection
pooling. See `datasource-config.xml`._

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Start JBoss Enterprise Application Platform 6 or JBoss AS 7 with the Web Profile
-------------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package jboss-as:deploy

4. This will deploy `petclinic-spring/target/jboss-as-petclinic-spring.war` to the running instance of the server.

If you don't have maven configured you can manually copy `petclinic-spring/target/jboss-as-petclinic-spring.war` to JBOSS_HOME/standalone/deployments.

For MySQL, you'll need to use the corresponding schema and SQL scripts in
the "db/mysql" subdirectory.

In you intend to use a local DataSource, the JDBC settings can be adapted
in "src/main/resources/spring/datasource-config.xml". To use a JTA DataSource, you need
to set up corresponding DataSources in your Java EE container.
 
Access the application
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-as-petclinic-spring/>.

Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse)

Debug the Application
----------------------
Note: Eclipse/JBDS may generate a persistence.xml file in the src/main/resources/META-INF/ directory. In order to avoid errors delete this file.

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc

