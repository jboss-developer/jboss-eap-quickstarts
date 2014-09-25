app-client: Application Client to run with the JBoss EAP appclient container
======================================================
Author: Wolf-Dieter Fink  
Level: Intermediate  
Technologies: EJB, EAR, AppClient  
Summary: Shows how to use the JBoss EAP application client container to support injection  
Target Product: EAP  
Product Versions: EAP 6.1, EAP 6.2, EAP 6.3, EAP 6.4  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  


What is it?
-----------

This quickstart demonstrates how to use the JBoss EAP client container to start the client 'Main' program and provide Dependency Injections (DI) for client applications. It also shows you how to use Maven to package the application according to the JavaEE specification.


This example consists of the following Maven projects, each with a shared parent:

| **Sub-project** | **Description** |
|:-----------|:-----------|
| `ejb` | An application that can be called by the `client`.
| `ear` | The EAR packaging contains the server and client side.
| `client-simple` | A simple client application for running in the application-client container to show the injection

The root `pom.xml` file builds each of the subprojects in the appropriate order.



System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.1 or later.

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.


Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.


Add the Application Users
---------------

If the client and server are run on different hosts, you must add the following users to the JBoss EAP server side application. Be sure to use the names and passwords specified in the table as they are required to run this example.

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

1. Make sure you have started the JBoss Server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build the artifacts:

        mvn clean install jboss-as:deploy



Access the Remote Client Application from the same machine
---------------------

This example shows how to invoke an EJB from a remote standalone application on the same machine. Both the client and server are on the same machine, so the defaults are sufficient and no authentication is necessary.

1. Be sure the quickstart deployed successfully as described above.
2. Navigate to the root directory of this quickstart and type the following command to run the application. Be sure to replace `EAP_HOME` with the path to your JBoss EAP installation.

        For Linux:   EAP_HOME/bin/appclient.sh ear/target/jboss-application-client.ear#simpleClient.jar Hello from command line
        For Windows: EAP_HOME\bin\appclient.sh ear\target\jboss-application-client.ear#simpleClient.jar Hello from command line

3. Review the result. The client outputs the following information provided by the server application:

        [org.jboss.as.quickstarts.appclient.acc.client.Main] (Thread-51) Main started with arguments
        [org.jboss.as.quickstarts.appclient.acc.client.Main] (Thread-51)             [Hello, from, command, line]
        [org.jboss.as.quickstarts.appclient.acc.client.Main] (Thread-##) Hello from StatelessSessionBean@myhost

    This output shows that the `ServerApplication` is called at the jboss.node `myhost`.
    The application client connected automatically a server on the same machine.

4. Review the server log files to see the bean invocations on the server.

         ClientContext is here = {Client=myhost}


Access the Remote Client Application from a different machine
---------------------

This example shows how to invoke an EJB from a remote standalone Java EE application on a different machine. In this case, the client needs to define a properties file to define properties to connect and authenticate to the server. The properties file is passed on the command line using the "--ejb-client-properties" argument.

### Configure Machine 1

1. Install JBoss EAP on this machine.
2. Add the application users to JBoss EAP as described above.
3. Start the JBoss EAP server with the following command line. Be sure to replace `MACHINE_1_IP_ADDRESS` with the IP address of this machine. This argument makes the server accessible to the network. 

        For Linux:   EAP_HOME/bin/standalone.sh -b MACHINE_1_IP_ADDRESS -bmanagement MACHINE_1_IP_ADDRESS
        For Windows: EAP_HOME\bin\standalone.bat -b MACHINE_1_IP_ADDRESS -bmanagement MACHINE_1_IP_ADDRESS


### Configure Machine 2

1. Install JBoss EAP and the `app-client` quickstart on this machine. There is no need to add the application users to this server.
2. Open a command prompt and navigate to the root directory of the quickstart.
3. Create a `jboss-ejb-client.properties` file. This file can be located anywhere in the file system, but for ease of demonstration, we create it in the root directory of this quickstart. Add the following content, replacing `MACHINE_1_IP_ADDRESS` with the IP address of `Machine 1`.

        remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED=false
        remote.connections=default
        remote.connection.default.host=MACHINE_1_IP_ADDRESS
        remote.connection.default.port=4447
        remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS=false
        remote.connection.default.username=quickuser
        remote.connection.default.password=quickuser-123

4. Be sure that the quickstart deployed successfully and the server is running on `Machine 1` as described above.
5. Type this command to run the application:

        For Linux:   EAP_HOME/bin/appclient.sh --ejb-client-properties=jboss-ejb-client.properties ear/target/jboss-application-client.ear#simpleClient.jar Hello from command line
        For Windows: EAP_HOME\bin\appclient.sh --ejb-client-properties=jboss-ejb-client.properties ear\target\jboss-application-client.ear#simpleClient.jar Hello from command line

6. Review the result. The client outputs the following information, which was provided by the application:

        [org.jboss.as.quickstarts.appclient.acc.client.Main] (Thread-51) Main started with arguments
        [org.jboss.as.quickstarts.appclient.acc.client.Main] (Thread-51)             [Hello, from, command, line]
        [org.jboss.as.quickstarts.appclient.acc.client.Main] (Thread-##) Hello from StatelessSessionBean@theOtherHOST

    This output shows that the `ServerApplication` is called at the jboss.node `theOtherHOST`.

7. Review the server log files to see the bean invocations on the server.

         ClientContext is here = {Client=myhost}

    As shown above, the connected server(s) can be configured using the properties file. It is also possible to connect multiple servers
    or a cluster using the same `jboss-ejb-client.properties` file.



Undeploy the Archives
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive from a local machine:

        mvn jboss-as:undeploy
        
   Type this command to undeploy the archive from a remote machine:

        mvn jboss-as:undeploy -Djboss-as.hostname=MACHINE_1_IP_ADDRESS [-Djboss-as.port=9099] -Djboss-as.username=admin -Djboss-as.password=admin-123


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse)

Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc

