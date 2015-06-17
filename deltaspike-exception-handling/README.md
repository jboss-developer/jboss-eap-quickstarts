deltaspike-exception-handling: An Example Showing DeltaSpike Exception Handling
====================================================================================
Author: Rafael Benevides  
Level: Intermediate  
Technologies: CDI, JSF, DeltaSpike  
Summary: The `deltaspike-exception-handling` quickstart demonstrates exception handling using the DeltaSpike library, which is based on the CDI eventing model.  
Prerequisites:   
Target Product: JBoss EAP  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

The `deltaspike-exception-handling` quickstart demonstrates exception handling with Java EE and CDI using the DeltaSpike library in Red Hat JBoss Enterprise Application Platform. Exception handling is based around the CDI eventing model.

The entire exception handling process starts with an event. This means your application is not tightly coupled to DeltaSpike, and allows for further extension. Exception handling in DeltaSpike aims to keep out of your way, and let you handle exceptions the way that makes the most sense to you. The eventing model allows for this delicate balance; firing an event is the main way to start handling an exception.

The quickstart can be told to throw two Exceptions: `MyException` and `MyOtherException`. And there are 4 different handlers:   
 
* `FacesExceptionHandler`  - Displays each exception on the page using a `Faces`. Only handles `@FacesRequest` exceptions.
* `LogExceptionHandler` - Logs each exception to the server console.
* `MyExceptionCountHandler` - Counts the the number of times `MyException` is thrown. This handler is also a CDI bean with a name.
* `RestExceptionHandler` - Produces a `javax.ws.rs.core.Response` which encapsulates the error, using a `ResponseBuilder`. Only handles `@RestRequest` exceptions.

Any exceptions from the REST endpoint are passed to the `DeltaSpikeExceptionMapper` (a JAX-RS exception mapper), which fires an exception event, which is observed by all relevant exception handlers. Of particular interest is the `RestExceptionHandler` which uses the ResponseBuilderProducer to create a instance of a javax.ws.rs.core.Response. The built response is then returned to the client by the `RestExceptionMapper`.

Any exceptions from beans invoked by JSF are passed to the `DeltaSpikeExceptionHandler` which fires an exception event, which is observed by all relevant exception handlers. Of particular interest is the `FacesExceptionHandler` which builds and registers some `FacesMessage`s.

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform (JBoss EAP) 7 or later. 

All you need to build this project is Java 8.0 (Java SDK 1.8) or later, Maven 3.0 or later.

 
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

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package jboss-as:deploy
4. This will deploy `target/jboss-deltaspike-exception-handling.war` to the running instance of the server.
 

Access the application 
---------------------

Access the running application in a browser at the following URL:  <http://localhost:8080/jboss-deltaspike-exception-handling>

You will be presented with a form that contains two buttons. One button throws the exception `MyException`. The other button throws the exception `MyOtherException`.

When you click on a button, a message is displayed showing the exception message followed by the number of times the service was invoked. Notice that the `MyException` counter is only incremented when `MyException` is thrown. You can also view the exception messages in the server log.

Click on `REST Invocation Test` link. The rest response is displayed on the iframe bellow. 

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

_NOTE: For this to work, Arquillian needs to know the location of the JBoss EAP server. This can be declared through the `JBOSS_HOME` environment variable or the `jbossHome` property in `arquillian.xml`. See [Run the Arquillian Tests](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/RUN_ARQUILLIAN_TESTS.md#run-the-arquillian-tests) for complete instructions and additional options._

        mvn clean verify -Parq-jbossas-managed

Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
