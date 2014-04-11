hibernate4: How to Use Hibernate 4 in an Application
====================================================
Author: Madhumita Sadhukhan  
Level: Intermediate  
Technologies: Hibernate 4  
Summary: This quickstart performs the same functions as the _hibernate3_ quickstart, but uses Hibernate 4 for database access. Compare this quickstart to the _hibernate3_ quickstart to see the changes needed to run with Hibernate 4.  
Target Product: EAP  
Product Versions: EAP 6.1, EAP 6.2, EAP 6.3  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

This quickstart is based upon the kitchensink example, but demonstrates how to use Hibernate ORM 4 over JPA in Red Hat JBoss Enterprise Application Platform.

This project is setup to allow you to create a compliant Java EE 6 application using JSF 2.0, CDI 1.0, EJB 3.1, JPA 2.0 , Hibernate-Core and Hibernate Bean Validation.  It includes a persistence unit associated with Hibernate session and some sample persistence and transaction code to help you with database access in enterprise Java. 

You can compare this quickstart to the `hibernate3` quickstart to see the code differences between Hibernate 3 and Hibernate 4.

 _Note: This quickstart uses the H2 database included with JBoss EAP 6. It is a lightweight, relational example datasource that is used for examples only. It is not robust or scalable and should NOT be used in a production environment!_

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.1 or later. 

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.


Add the Correct Dependencies
---------------------------

JBoss EAP provides Hibernate 4 and JPA support. 

If you use Hibernate 4 packaged within Red Hat JBoss EAP, you will need to first import the JPA API.

This quickstart demonstrates usage of Hibernate Session and Hibernate Validators.

If you look at the pom.xml file in the root of the hibernate4 quickstart directory, you will see that the dependencies for the Hibernate modules have been added with the scope as `provided`.
For example:

      <dependency>
         <groupId>org.hibernate</groupId>
         <artifactId>hibernate-validator</artifactId>
         <version>4.2.0.Final</version>
         <scope>provided</scope>
         <exclusions>
            <exclusion>
               <groupId>org.slf4j</groupId>
               <artifactId>slf4j-api</artifactId>
            </exclusion>
         </exclusions>
      </dependency>

Please note that if you are working with Hibernate 3, the process is different. You will need to bundle the jars since JBoss EAP 6 does not ship with Hibernate 3. Refer to the `hibernate3` quickstart for details on how to bundle the JARs.


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

4. This will deploy `target/jboss-hibernate4.war` to the running instance of the server.


Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-hibernate4/>.


Server Log: Expected warnings and errors
-----------------------------------

_Note:_ You will see the following warning in the server log. You can ignore this warning.

        HHH000431: Unable to determine H2 database version, certain features may not work


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
