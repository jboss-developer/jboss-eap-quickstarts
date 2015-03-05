tasks-rs: JAX-RS, JPA quickstart
==============================
Author: Mike Musgrove  
Level: Intermediate  
Technologies: JPA, JAX-RS  
Summary: The `tasks-rs` quickstart demonstrates how to implement a JAX-RS service that uses JPA 2.0 persistence.  
Prerequisites: tasks  
Target Product: JBoss EAP  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

The `tasks-rs` quickstart demonstrates how to implement a JAX-RS service that uses JPA 2.0 persistence deployed to Red Hat JBoss Enterprise Application Platform.

* The client uses HTTP to interact with the service. It builds on the *tasks* quickstarts which provide simple Task management with secure login.

* The service interface is implemented using JAX-RS. The SecurityContext JAX-RS annotation is used to inject the security details into each REST method.

The application manages User and Task JPA entities. A user represents an authenticated principal and is associated with zero or more Tasks. Service methods validate that there is an authenticated principal and the first time a principal is seen, a JPA User entity is created to correspond to the principal. JAX-RS annotated methods are provided for associating Tasks with this User and for listing and removing Tasks.

_Note: This quickstart uses the H2 database included with Red Hat JBoss Enterprise Application Platform 6. It is a lightweight, relational example datasource that is used for examples only. It is not robust or scalable, is not supported, and should NOT be used in a production environment!_

_Note: This quickstart uses a `*-ds.xml` datasource configuration file for convenience and ease of database configuration. These files are deprecated in JBoss EAP 6.4 and should not be used in a production environment. Instead, you should configure the datasource using the Management CLI or Management Console. Datasource configuration is documented in the [Administration and Configuration Guide](https://access.redhat.com/documentation/en-US/JBoss_Enterprise_Application_Platform/) for Red Hat JBoss Enterprise Application Platform._


System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.1 or later.

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.


Configure Maven
-------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.


Use of EAP_HOME
---------------

In the following instructions, replace `EAP_HOME` with the actual path to your JBoss EAP 6 installation. The installation path is described in detail here: [Use of EAP_HOME and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_EAP_HOME.md#use-of-eap_home-and-jboss_home-variables).


Add an Application User
---------------

This quickstart uses secured management interfaces and requires that you create the following application user to access the running application. 

| **UserName** | **Realm** | **Password** | **Roles** |
|:-----------|:-----------|:-----------|:-----------|
| quickstartUser| ApplicationRealm | quickstartPwd1!| guest |

To add the application user, open a command prompt and type the following command:

        For Linux:   EAP_HOME/bin/add-user.sh -a -u 'quickstartUser' -p 'quickstartPwd1!' -g 'guest'
        For Windows: EAP_HOME\bin\add-user.bat  -a -u 'quickstartUser' -p 'quickstartPwd1!' -g 'guest'

If you prefer, you can use the add-user utility interactively. 
For an example of how to use the add-user utility, see the instructions located here: [Add an Application User](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CREATE_USERS.md#add-an-application-user).


Start the JBoss EAP Server
-------------------------

1. Open a command prompt and navigate to the root of the JBoss EAP directory.
2. The following shows the command line to start the server:

        For Linux:   EAP_HOME/bin/standalone.sh
        For Windows: EAP_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/BUILD_AND_DEPLOY.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install jboss-as:deploy

4. This will deploy `target/jboss-tasks-rs.war` to the running instance of the server.


Access the Application Resources
---------------------

Application resources for this quickstart are prefixed with the URL http://localhost:8080/jboss-tasks-rs/ and can be accessed by an HTTP client.

* For methods that accept *GET*, a web browser can be used.
* Otherwise, you must use cURL or some other command line tool that supports HTTP *POST* and *DELETE* methods.

Below you will find instructions to create, display, and delete tasks.

### Create a Task

To associate a task called `task1` with the user `quickstartUser`, you must authenticate as user `quickstartUser` and send an HTTP *POST* request to the url 'http://localhost:8080/jboss-tasks-rs/tasks/task1'.

To issue the *POST* command using cURL, type the following command:

    curl -i -u 'quickstartUser:quickstartPwd1!' -H "Content-Length: 0" -X POST http://localhost:8080/jboss-tasks-rs/tasks/task1

You will see the following response:

    HTTP/1.1 201 Created
    Server: Apache-Coyote/1.1
    Location: http://localhost:8080/jboss-tasks-rs/tasks/1
    Content-Length: 0
    Date: Sun, 15 Apr 2012 22:46:26 GMT

This is what happens when the command is issued:

* The `-i` flag tells cURL to print the returned headers. Notice that the `Location` header contains the URI of the resource corresponding to the new task you have just created.
* The `-u` flag provides the authentication information for the request.
* The `-H` flag adds a header to the outgoing request.
* The `-X` flag tells cURL which HTTP method to use. The HTTP *POST* is used to create resources.
* The `Location` header of the response contains the URI of the resource representing the newly created task.

The final argument to cURL determines the title of the task. Note that this approach is perhaps not very restful but it simplifies this quickstart. A better approach would be to *POST* to "http://localhost:8080/jboss-tasks-rs/tasks" passing the task title in the body of the request.


### Display the XML Representation of a Task

To display the XML representation of the newly created resource, issue a *GET* request on the task URI returned in the `Location` header during the create.

1. To issue a *GET* using a browser, open a browser and access the URI. You will be challenged to enter valid authentication credentials.

    <http://localhost:8080/jboss-tasks-rs/tasks/1>
2. To issue a *GET* using cURL, type the following command:

        curl -H "Accept: application/xml" -u 'quickstartUser:quickstartPwd1!' -X GET http://localhost:8080/jboss-tasks-rs/tasks/1

    The `-H flag tells the server that the client wishes to accept XML content.

Using either of the above *GET* methods, you should see the following XML:

    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
       <task id="1" ownerName="quickstartUser">
          <title>task1</title>
      </task>


### Display the XML Representation of all Tasks for a User

To obtain a list of all tasks for user `quickstartUser` in XML format, authenticate as user `quickstartUser` and send an HTTP `GET` request to the resource `tasks` URL.

1. To issue a *GET* using a browser, open a browser and access the following URL. You will be challenged to enter valid authentication credentials.

    <http://localhost:8080/jboss-tasks-rs/tasks>

2. To list all tasks associated with the user `quickstartUser` using cURL, type:

        curl -H "Accept: application/xml" -u 'quickstartUser:quickstartPwd1!' -X GET http://localhost:8080/jboss-tasks-rs/tasks

Using either of the above *GET* methods, you should see the following XML:

    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <collection>
        <task id="1" ownerName="quickstartUser">
        <title>task1</title>
        </task>
    </collection>

### Delete a Task

To delete a task, again authenticate as principal `quickstartUser` and send an HTTP *DELETE* request to the URI that represents the task.

To delete the task with id `1`:

    curl -i -u 'quickstartUser:quickstartPwd1!' -X DELETE http://localhost:8080/jboss-tasks-rs/tasks/1

You will see this response:

    HTTP/1.1 204 No Content
    Server: Apache-Coyote/1.1
    Pragma: No-cache
    Cache-Control: no-cache
    Expires: Thu, 01 Jan 1970 01:00:00 GMT
    Date: Sun, 15 Apr 2012 22:51:56 GMT

Now list all tasks associated with user `quickstartUser`:

    curl -u 'quickstartUser:quickstartPwd1!' -X GET http://localhost:8080/jboss-tasks-rs/tasks

You will see a response with an empty collection:

    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <collection/>


Modify this Quickstart to Support JSON Representations of Tasks
-----------------------------------------------------------------

JSON is not part of the JAX-RS standard but most JAX-RS implementations do support it. This quickstart can be modified to support JSON by uncommenting a few lines. Look for lines beginning with "// JSON:":

1. Open the file src/main/java/org/jboss/as/quickstarts/tasksrs/model/Task.java and remove the comments from the following two lines.

        // import org.codehaus.jackson.annotate.JsonIgnore;

        // @JsonIgnore

2. Open the file src/main/java/org/jboss/as/quickstarts/tasksrs/service/TaskResource.java and make sure the *GET* methods produce "application/json" as well as "application/xml". Again, look for lines beginning with "// JSON:".
    * Remove comments from these lines:

        //@Produces({ "application/xml", "application/json" })
    * Add comments to these lines:

        @Produces({ "application/xml" })
3. Open pom.xml and remove the comments from the dependency with artifactId `resteasy-jackson-provider`

        <!--
        <dependency>
            <groupId>org.jboss.resteasy</groupId>
            <artifactId>resteasy-jackson-provider</artifactId>
            <version>2.3.1.GA</version>
            <scope>provided</scope>
        </dependency>
        -->

4. [Create a Task](#create-a-task) as you did for the XML version of this quickstart.
5. Rebuild and redeploy the quickstart.


Now you can view task resources in JSON media type by specifying the correct Accept header. For example, using the cURL tool, type the following command:

    curl -H "Accept: application/json" -u 'quickstartUser:quickstartPwd1!' -X GET http://localhost:8080/jboss-tasks-rs/tasks/1

You will see the following response:

    {"id":1,"title":"task1","ownerName":"quickstartUser"}


Server Log: Expected warnings and errors
-----------------------------------

_Note:_ You will see the following warnings in the server log. You can ignore these warnings.

    JBAS010489: -ds.xml file deployments are deprecated. Support may be removed in a future version.

    HHH000431: Unable to determine H2 database version, certain features may not work


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a JBoss EAP server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts)

Be sure to [Add an Application User](#add-an-application-user) as described above.

_Note:_ When you deploy this quickstart, you see the following error. This is because JBoss Developer Studio automatically attempts to access the URL <http://localhost:8080/jboss-tasks-rs/>, however, all incoming requests are handled by the REST application. You can ignore this error.

    JBWEB000065: HTTP Status 404 - RESTEASY001185: Could not find resource for relative : / of full path: http://localhost:8080/jboss-tasks-rs/




Debug the Application
---------------------

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

        mvn dependency:sources

