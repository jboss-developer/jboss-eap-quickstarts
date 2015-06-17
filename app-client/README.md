app-client: Use the JBoss EAP Application Client Container
======================================================
Author: Wolf-Dieter Fink  
Level: Intermediate  
Technologies: EJB, EAR, AppClient  
Summary: The `app-client` quickstart demonstrates how to code and package a client app and use the JBoss EAP client container to start the client Main program.  
Target Product: JBoss EAP  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  


What is it?
-----------

The `app-client` quickstart demonstrates how to use the JBoss EAP client container to start the client 'Main' program and provide Dependency Injections (DI) for client applications in Red Hat JBoss Enterprise Application Platform. It also shows you how to use Maven to package the application according to the JavaEE specification.


This example consists of the following Maven projects, each with a shared parent:

| **Sub-project** | **Description** |
|:-----------|:-----------|
| `ejb` | An application that can be called by the `client`.
| `ear` | The EAR packaging contains the server and client side.
| `client-simple` | A simple client application for running in the application-client container to show the injection

The root `pom.xml` file builds each of the subprojects in the appropriate order.



System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 7 or later.

All you need to build this project is Java 8.0 (Java SDK 1.8) or later, Maven 3.0 or later.


Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.


Use of EAP_HOME
---------------

In the following instructions, replace `EAP_HOME` with the actual path to your JBoss EAP installation. The installation path is described in detail here: [Use of EAP_HOME and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_EAP_HOME.md#use-of-eap_home-and-jboss_home-variables).


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

If you prefer, you can use the add-user utility interactively. For an example of how to use the add-user utility, see the instructions located here: [Add an Application User](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CREATE_USERS.md#add-an-application-user).


Start the JBoss EAP Server
-------------------------

1. Open a command prompt and navigate to the root of the JBoss EAP directory.
2. The following shows the command line to start the server:

        For Linux:   EAP_HOME/bin/standalone.sh
        For Windows: EAP_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build the artifacts:

        mvn clean install jboss-as:deploy



Access the Remote Client Application from the same machine
---------------------

This example shows how to invoke an EJB from a remote standalone application on the same machine. Both the client and server are on the same machine, so the defaults are sufficient and no authentication is necessary.

1. Be sure the quickstart deployed successfully as described above.
2. Navigate to the root directory of this quickstart and type the following command to run the application. Be sure to replace `EAP_HOME` with the path to your JBoss EAP installation.

        For Linux:   EAP_HOME/bin/appclient.sh ear/target/jboss-app-client.ear#simpleClient.jar Hello from command line
        For Windows: EAP_HOME\bin\appclient.sh ear\target\jboss-app-client.ear#simpleClient.jar Hello from command line

3. Review the result. The client outputs the following information provided by the server application:

        [org.jboss.as.quickstarts.appclient.acc.client.Main] (Thread-51) Main started with arguments
        [org.jboss.as.quickstarts.appclient.acc.client.Main] (Thread-51)             [Hello, from, command, line]
        [org.jboss.as.quickstarts.appclient.acc.client.Main] (Thread-##) Hello from StatelessSessionBean@myhost

    This output shows that the `ServerApplication` is called at the jboss.node `myhost`.
    The application client connected automatically a server on the same machine.

4. Review the server log files to see the bean invocations on the server.

         ClientContext is here = {Client =myhost}


Access the Remote Client Application from a different machine
---------------------

This example shows how to invoke an EJB from a remote standalone Java EE application on a different machine. In this case, the client needs to define a properties file to define properties to connect and authenticate to the server. The properties file is passed on the command line using the "--ejb-client-properties" argument.

### Configure Machine_1 (Remote Server Machine)

1. Install JBoss EAP on this machine.
2. Add the application users to the JBoss EAP server on this machine as described above.
3. Start the JBoss EAP server with the following command. Be sure to replace `MACHINE_1_IP_ADDRESS` with the IP address of this machine. These arguments make the server accessible to the network. 

        For Linux:   EAP_HOME/bin/standalone.sh -b MACHINE_1_IP_ADDRESS -bmanagement MACHINE_1_IP_ADDRESS
        For Windows: EAP_HOME\bin\standalone.bat -b MACHINE_1_IP_ADDRESS -bmanagement MACHINE_1_IP_ADDRESS


### Configure Machine_2 (Local Client Machine)

1. Install JBoss EAP on this server. There is no need to add the application users to this server.
2. Download the `app-client` quickstart to this machine. 
3. Create a `jboss-ejb-client.properties` file. This file can be located anywhere in the file system, but for ease of demonstration, we create it in the root directory of this quickstart. Add the following content, replacing `MACHINE_1_IP_ADDRESS` with the IP address of `Machine_1`.

        remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED=false
        remote.connections=default
        remote.connection.default.host=MACHINE_1_IP_ADDRESS
        remote.connection.default.port=4447
        remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS=false
        remote.connection.default.username=quickuser
        remote.connection.default.password=quick-123

4. Open a command prompt and navigate to the root directory of the quickstart.
5. Deploy the `app-client` quickstart to the remote machine using the following command:

        mvn clean install jboss-as:deploy -Djboss-as.hostname=MACHINE_1_IP_ADDRESS [-Djboss-as.port=9099] -Djboss-as.username=admin -Djboss-as.password=admin-123
6. Be sure that the quickstart deployed successfully and the server is running on `Machine_1` as described above.
7. Type this command to run the `app-client` application:

        For Linux:   EAP_HOME/bin/appclient.sh --ejb-client-properties=jboss-ejb-client.properties ear/target/jboss-app-client.ear#simpleClient.jar Hello from command line
        For Windows: EAP_HOME\bin\appclient.sh --ejb-client-properties=jboss-ejb-client.properties ear\target\jboss-app-client.ear#simpleClient.jar Hello from command line

8. Review the result. The client outputs the following information, which was provided by the application:

        [org.jboss.as.quickstarts.appclient.acc.client.Main] (Thread-51) Main started with arguments
        [org.jboss.as.quickstarts.appclient.acc.client.Main] (Thread-51)             [Hello, from, command, line]
        [org.jboss.as.quickstarts.appclient.acc.client.Main] (Thread-##) Hello from StatelessSessionBean@theOtherHOST

    This output shows that the `ServerApplication` is called at the jboss.node `theOtherHOST`.

9. Review the server log files on the remote machine to see the bean invocations on the server.

         ClientContext is here = {Client =myhost}

    As shown above, the connected server(s) can be configured using the properties file. It is also possible to connect multiple servers
    or a cluster using the same `jboss-ejb-client.properties` file.



Undeploy the Archive from the Local machine
--------------------

Follow these instructions if you are testing the quickstart on the same machine.

1. Make sure you have started the JBoss Server on the machine where the quickstart is deployed as described above.
2. Open a command prompt on that server and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive from the local machine.

        mvn jboss-as:undeploy
        

Undeploy the Archive from the Remote Machine
--------------------

Follow these instructions if you are testing the quickstart on a diffent machine.

1. Make sure you have started the JBoss Server on the remote server machine, `Machine_1`, where the quickstart is deployed as described above.
2. Open a command prompt on the local client machine, `Machine_2`, and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive from the remote server machine, `Machine_1`.

        mvn jboss-as:undeploy -Djboss-as.hostname=MACHINE_1_IP_ADDRESS [-Djboss-as.port=9099] -Djboss-as.username=admin -Djboss-as.password=admin-123
        


