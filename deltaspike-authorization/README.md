deltaspike-authorization: Demonstrate the creation of a custom authorization example using @SecurityBindingType from DeltaSpike
======================================================
Author: Rafael Benevides  
Level: Beginner  
Technologies: JSF, CDI, Apache Deltaspike  
Summary: Demonstrate the creation of a custom authorization example using @SecurityBindingType from DeltaSpike  
Prerequisites:   
Target Product: WFK  
Product Versions: EAP 6.1, EAP 6.2, WFK 2.6  
Source: <https://github.com/jboss-developer/jboss-wfk-quickstarts/>  

What is it?
-----------

Security binding is DeltaSpike feature that restricts who can invoke a method (under the covers, it uses interceptors).

To restrict who can invoke a method, we create an annotation, called a security binding type. This quickstart has two security binding types - `@AdminAllowed` and `@GuestAllowed`.

The quickstart defines an `Authorizer` class that implements the restrictions for the security binding types. The authorizer is a CDI bean which defines methods (annotated with `@Secures) which perform the authorization checks for each security binding we create.

In this quickstart the `Authorizer` we delegate authentication to JAAS, but other authentication solutions could be used.

Methods on the `Controller` bean have been restricted using the security binding types.


System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform (EAP) 6.1 or later with the  Red Hat JBoss Web Framework Kit (WFK) 2.6.

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#configure-maven) before testing the quickstarts.


Add an Application User
----------------

This quickstart uses secured management interfaces and requires that you create the following `quickstartUser` application user to access the running application. 

| **UserName** | **Realm** | **Password** | **Roles** |
|:-----------|:-----------|:-----------|:-----------|
| quickstartUser| ApplicationRealm | quickstartPwd1! | guest |

To add the application user, open a command prompt and type the following commands:

        For Linux:
            EAP_HOME/bin/add-user.sh -a -u quickstartUser -p quickstartPwd1! -g 'guest'

        For Windows:
            EAP_HOME\bin\add-user.bat -a -u quickstartUser -p quickstartPwd1! -g 'guest'

If you have not added an administrative user to your server, add the following `admin` management user. The password below is just a suggestion. If you prefer, you can choose a different administrative password.

        For Linux:
            EAP_HOME/bin/add-user.sh -u admin -p admin-123

        For Windows:
            EAP_HOME\bin\add-user.sh -u admin -p admin-123

If you prefer, you can use the add-user utility interactively. 
For an example of how to use the add-user utility, see instructions in the root README file located here: [Add an Application User](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CREATE_USERS.md#add-an-application-user).


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
4. This will deploy `target/jboss-deltaspike-authorization.war` to the running instance of the server.


Access the application
---------------------

You can access the running application in a browser at the following URL: <localhost:8080/jboss-deltaspike-authorization/>

When you access the application you are redirected to a login form, already filled in with the details of the application user you set up above. Once you have logged into the application you see a page showing your username and two buttons. 

When you click on the `Employee Method` button you will see the following message: `You executed a @EmployeeAllowed method` - you are authorized to invoke this method.

When you click on the `Admin Method` button you will be redirected to a error page with the following exception: `org.apache.deltaspike.security.api.authorization.AccessDeniedException` - you aren't authorized to invole thos method.
        
Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy

Run the Arquillian Functional Tests
-----------------------------------

This quickstart provides Arquillian functional tests. They are located under the directory "functional-tests". Functional tests verify that your application behaves correctly from the user's point of view - simulating clicking around the page as a normal user would do.

To run these tests, you must build the main project as described above.

1. Open a command line and navigate to the root directory of this quickstart.
2. Build the quickstart WAR using the following command:

        mvn clean package

3. Navigate to the functional-tests/ directory in this quickstart.
4. If you have a running instance of the JBoss EAP server, as described above, run the remote tests by typing the following command:

        mvn clean verify -Parq-jbossas-remote

5. If you prefer to run the functional tests using managed instance of the JBoss EAP server, meaning the tests will start the server for you, type the following command:

_NOTE: For this to work, Arquillian needs to know the location of the JBoss EAP server. This can be declared through the `JBOSS_HOME` environment variable or the `jbossHome` property in `arquillian.xml`. See [Run the Arquillian Tests](../README.md#run-the-arquillian-tests) for complete instructions and additional options._

        mvn clean verify -Parq-jbossas-managed


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------

You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 

Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc

