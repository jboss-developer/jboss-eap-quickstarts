app-client: Application Client to run with the JBoss appclient container
======================================================
Author: Wolf-Dieter Fink
Level: Intermediate
Technologies: EJB, EAR, AppClient
Summary: Show how to use the JBoss application client contiainer to support injection
Target Product: EAP
Product Versions: EAP 6.1, EAP 6.2
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>


What is it?
-----------

This quickstart demonstrates how an application, including the client side, can be packaged according to the JavaEE specification with maven.
The JBoss application client container can be used to start the client Main program and provide dependency injections (DI) for thea client application.

This example consists of the following Maven projects, each with a shared parent:

| **Sub-project** | **Description** |
|:-----------|:-----------|
| `ejb` | An application that can be called by the `client`.
| `ear` | The EAR packaging contains the server and client side.
| `client-simple` | A simple client application for running in the application-client container to show the injection

The root `pom.xml` builds each of the subprojects in an appropriate order.



System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.1 or later.

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.


Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.


Add the Application Users
---------------

The following users must only be added  to the applicaion server side if the server and the client run at different hosts. Be sure to use the names and passwords specified in the table as they are required to run this example.

| **UserName** | **Realm** | **Password** | **Roles** |
|:-----------|:-----------|:-----------|:-----------|
| admin| ManagementRealm | admin-123 | _leave blank for none_ |
| quickuser| ApplicationRealm | quick-123 | _leave blank for none_ |

To add the users, open a command prompt and type the following commands:

        For Linux:
            EAP_HOME/bin/add-user.sh -u admin -p admin-123
            EAP_HOME/bin/add-user.sh -a -u quickuser -p quick-123

        For Windows:
            EAP_HOME\bin\add-user.sh -u admin -p admin-123
            EAP_HOME\bin\add-user.bat -a -u quickuser -p quick-123

If you prefer, you can use the add-user utility interactively. For an example of how to use the add-user utility, see instructions in the root README file located here: [Add an Application User](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CREATE_USERS.md#add-an-application-user).


Start the JBoss EAP Server
-------------------------

1. Open a command prompt and navigate to the root of the JBoss EAP directory.
2. The following shows the command line to start the server:

        For Linux:   EAP_HOME/bin/standalone.sh
        For Windows: EAP_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy) for complete instructions and additional options._

1. Make sure you have started and configured the JBoss Server successful as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build the artifacts:

        mvn clean install jboss-as:deploy



Access the Remote Client Application at the same machine
---------------------

This example shows how to invoke an EJB from a remote standalone application.
It also demonstrates how to invoke an EJB from a client using a scoped-context rather than a properties file containing the parameters required by the InitialContext.

1. Make sure that the deployments are successful as described above.
2. Type this command to run the application:

        For Linux:   EAP_HOME/bin/appclient.sh ear/target/jboss-application-client.ear#simpleClient.jar
        For Windows: EAP_HOME\bin\appclient.sh ear\target\jboss-application-client.ear#simpleClient.jar

    The client will output the following information provided by the applications:

        [org.jboss.as.quickstarts.appclient.acc.client.Main] (Thread-##) Hello from StatelessSessionBean@myhost

    This output shows that the `ServerApplication` is called at the jboss.node `myhost`.
    The appclient try to connect automatically a server at the same machine.

    Review the server log files to see the bean invocations on the server.

         ClientContext is here = {Client=myhost}


Access the Remote Client Application at different machines
---------------------

This example shows how to invoke an EJB from a remote standalone application.
It also demonstrates how to invoke an EJB from a client using a scoped-context rather than a properties file containing the parameters required by the InitialContext.

1. Install a server on different machines
2. Add the application user as describe above
3. Start the server at the other machine with the following command line:

        For Linux:   EAP_HOME/bin/standalone.sh -b <your IP> -bmanagement <your IP>
        For Windows: EAP_HOME\bin\standalone.bat -b <your IP> -bmanagement <your IP>

4. Type this command to deploy the artifacts on your local machine:

        mvn clean install jboss-as:deploy -Djboss-as.hostname=<server IP> [-Djboss-as.port=9099] -Djboss-as.username=admin -Djboss-as.password=admin-123

5. Make sure that the deployments are successful as described above.
6. Create a jboss-ejb-client.properties file with the following content

        remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED=false
        remote.connections=default
        remote.connection.default.host=<the server IP>
        remote.connection.default.port=4447
        remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS=false
        remote.connection.default.username=quickuser
        remote.connection.default.password=quickuser-123

7. Type this command to run the application:

        For Linux:   EAP_HOME/bin/appclient.sh --ejb-client-properties=jboss-ejb-client.properties ear/target/jboss-application-client.ear#simpleClient.jar
        For Windows: EAP_HOME\bin\appclient.sh --ejb-client-properties=jboss-ejb-client.properties ear\target\jboss-application-client.ear#simpleClient.jar

    The client will output the following information provided by the applications:

        [org.jboss.as.quickstarts.appclient.acc.client.Main] (Thread-##) Hello from StatelessSessionBean@theOtherHOST

    This output shows that the `ServerApplication` is called at the jboss.node `theOtherHOST`.

    Review the server log files to see the bean invocations on the server.

         ClientContext is here = {Client=myhost}

    As shown the connected server(s) can be configured with the properties file. It is also possible to connect multiple servers
    or a cluster with the same jboss-ejb-client.properties as it works for a standalone client.



Undeploy the Archives
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy



Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse)

Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc

