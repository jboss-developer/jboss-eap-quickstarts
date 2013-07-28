picketlink-deltaspike-authorization: Demonstrate the creation of a custom authorization example using PicketLink in conjunction with @SecurityBindingType from DeltaSpike
======================================================
Author: Shane Bryzak
Level: Intermediate
Technologies: JSF, CDI, PicketLink, Deltaspike
Summary: Demonstrate the creation of a custom authorization example using PicketLink and @SecurityBindingType from DeltaSpike
Prerequisites: 
Target Product: WFK
Source: <https://github.com/jboss-jdf/jboss-as-quickstart/>

What is it?
-----------

Security binding is DeltaSpike feature that restricts who can invoke a method (under the covers, it uses interceptors).

To restrict who can invoke a method, we create an annotation, called a security binding type. This quickstart has two security binding types - `@Admin` and `@Employee`.

The quickstart defines an `Authorizer` class that implements the restrictions for the security binding types. The authorizer is a CDI bean which defines methods (annotated with `@Secures) which perform the authorization checks for each security binding we create.

In this quickstart the `Authorizer` we delegate authentication to JAAS, but other authentication solutions could be used.

Methods on the `Controller` bean have been restricted using the security binding types.


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#configure-maven) before testing the quickstarts.


Start JBoss Enterprise Application Platform 6 or JBoss AS 7
-------------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat

Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package jboss-as:deploy
4. This will deploy `target/jboss-as-picketlink-deltaspike-authorization.war` to the running instance of the server.


Access the application
---------------------

You can access the running application in a browser at the following URL: <localhost:8080/jboss-as-picketlink-deltaspike-authorization/>

When you access the application you are redirected to a login form.  Once you have logged into the application using one of the two example user accounts you will see a page showing your username and two buttons. 

When you click on the `Employee Method` button you will see the following message: `You executed an @Employee method` - you are authorized to invoke this method.

When you click on the `Admin Method` button, depending on which user you logged in as you will either see the message `You executed an @Admin method`, or you will be redirected to an error page with the following exception: `org.apache.deltaspike.security.api.authorization.AccessDeniedException` - you aren't authorized to invoke this method.
        
Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------

You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 

Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc

