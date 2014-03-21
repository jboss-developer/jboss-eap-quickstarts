ejb-multi-server: EJB Communication Across Servers
======================================================
Author: Wolf-Dieter Fink  
Level: Advanced  
Technologies: EJB, EAR  
Summary: EJB applications deployed to different servers that communicate via EJB remote calls  
Target Product: EAP  
Product Versions: EAP 6.1, EAP 6.2  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  


What is it?
-----------

This quickstart demonstrates communication between applications deployed to different servers. Each application is deployed as an EAR and contains a simple EJB3.1 bean. The only function of each bean is to log the invocation.

This example consists of the following Maven projects, each with a shared parent:

| **Sub-project** | **Description** |
|:-----------|:-----------|
| `app-main` | An application that can be called by the `client`. It can also call the different sub-applications. |
| `app-one` and `app-two` | These are simple applications that contain an EJB sub-project to build the `ejb.jar` file and an EAR sub-project to build the `app.ear` file. Each application contains only one EJB that logs a statement on a method call and returns the `jboss.node.name` and credentials. |
| `app-web` |  A simple WAR application. It consists of one Servlet that demonstrates how to invoke EJBs on a different server. | 
| `client` | This project builds the standalone client and executes it.|

The root `pom.xml` builds each of the subprojects in an appropriate order.

The server configuration is done using CLI batch scripts located in the root of the quickstart folder.



System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.1 or later.

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.


Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.


Start with a Clean JBoss EAP Install
--------------------------------------

It is important to start with a clean version of JBoss EAP before testing this quickstart. Be sure to unzip or install a fresh JBoss EAP instance. 


Add the Application Users
---------------

The following users must be added to the `ApplicationRealm` to run this quickstart. Be sure to use the names and passwords specified in the table as they are required to run this example.

| **UserName** | **Realm** | **Password** | **Roles** |
|:-----------|:-----------|:-----------|:-----------|
| quickuser| ApplicationRealm | quick-123 | _leave blank for none_ |
| quickuser1 | ApplicationRealm | quick123+ | _leave blank for none_ |
| quickuser2 | ApplicationRealm | quick+123 | _leave blank for none_ |

To add the users, open a command prompt and type the following commands:

        For Linux:
            EAP_HOME/bin/add-user.sh -a -u quickuser -p quick-123
            EAP_HOME/bin/add-user.sh -a -u quickuser1 -p quick123+
            EAP_HOME/bin/add-user.sh -a -u quickuser2 -p quick+123

        For Windows:
            EAP_HOME\bin\add-user.bat -a -u quickuser -p quick-123
            EAP_HOME\bin\add-user.bat -a -u quickuser1 -p quick123+
            EAP_HOME\bin\add-user.bat -a -u quickuser2 -p quick+123

If you prefer, you can use the add-user utility interactively. For an example of how to use the add-user utility, see instructions in the root README file located here: [Add an Application User](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CREATE_USERS.md#add-an-application-user).


Configure the JBoss EAP Server
---------------------------

You configure the domain server by running JBoss CLI commands. For your convenience, this quickstart batches the commands into a `install-domain.cli` script provided in the root directory of this quickstart. 

1. Start with a fresh instance of the JBoss EAP as noted above under [Start with a Clean JBoss EAP Install](#start-with-a-clean-jboss-eap-install).

2. Be sure you add the required users as specified above under [Add the Application Users](#add-the-application-users). 

3. Before you begin, back up your server configuration files.
    * If it is running, stop the JBoss EAP server.
    * Backup the following files, replacing EAP_HOME with the path to your server: 

            EAP_HOME/domain/configuration/domain.xml
            EAP_HOME/domain/configuration/host.xml        
    * After you have completed testing and undeployed this quickstart, you can replace these files to restore the server to its original configuration.
4.  Start the JBoss EAP server 
    * Open a command prompt and navigate to the root of the EAP directory. 
    * Start the server using the following command:

            bin/domain.sh    
5. Review the `install-domain.cli` file in the root of this quickstart directory. This script configures and starts multiple servers needed to run this quickstart. 

6. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing EAP_HOME with the path to your server:
 
        For Linux: EAP_HOME/bin/jboss-cli.sh --connect --file=install-domain.cli
        For Windows: EAP_HOME\bin\jboss-cli.bat --connect --file=install-domain.cli
     You should see the following result when you run the script:

        The batch executed successfully
        {"outcome" => "success"}
        
Review the Modified Server Configuration
-----------------------------------

There are too many additions to the configuration files to list here. Feel free to compare the `domain.xml` and `host.xml` to the backup copies to see the changes made to configure the server to run this quickstart.

Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy) for complete instructions and additional options._

1. Make sure you have started and configured the JBoss EAP server successfully   as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build the artifacts:

        mvn clean install
        
4. Open a new command prompt and navigate to the root directory of this quickstart. Deploy the applications using the provided CLI batch script by typing the following command:

        For Linux: EAP_HOME/bin/jboss-cli.sh --connect --file=deploy-domain.cli
        For Windows: EAP_HOME\bin\jboss-cli.bat --connect --file=deploy-domain.cli
       
    This will deploy the app-*.ear files to different server-groups of the running domain.

 
_NOTE: If ERRORs appear in the server.log when the installing or deploying the quickstart, please stop the domain and restart it. This should ensure further steps run correctly._


Access the Remote Client Application
---------------------

This example shows how to invoke an EJB from a remote standalone application. 
It also demonstrates how to invoke an EJB from a client using a scoped-context rather than a properties file containing the parameters required by the InitialContext. 

1. Make sure that the deployments are successful as described above.
2. Navigate to the quickstart `client/` subdirectory.
3. Type this command to run the application:

        mvn exec:java

    The client will output the following information provided by the applications:
        
        InvokeAll succeed: MainApp[anonymous]@master:app-main  >  [ app1[anonymous]@master:app-oneA > app2[quickuser2]@master:app-twoA ; app2[quickuser2]@master:app-twoA ]

    This output shows that the `MainApp` is called with the user `anonymous` at node `master:app-main` and the sub-call is proceeded by the `master:app-oneA` node and `master:app-twoA` node as `quickuser2`. 
    
    Review the server log files to see the bean invocations on the servers.

4. To invoke the bean that uses the `scoped-client-context`, you must pass a property. Type the following command

        mvn exec:java -DUseScopedContext=true
    
    The invocation of `appTwo` throws a  `java.lang.reflect.InvocationTargetException` since the secured method is called and there is no Role for the user defined.  You get a `BUILD FAILURE` and the client outputs the following information:

        [ERROR] Failed to execute goal org.codehaus.mojo:exec-maven-plugin:1.2.1:java (default-cli) on project jboss-ejb-multi-server-client: An exception occured while executing the Java class. null: InvocationTargetException: JBAS014502: Invocation on method: public abstract java.lang.String org.jboss.as.quickstarts.ejb.multi.server.app.AppTwo.invokeSecured(java.lang.String) of bean: AppTwoBean is not allowed -> [Help 1]

    Update the user `quickuser1` and `quickuser2` and give them one of the Roles `AppTwo` or `Intern`.
    To update the roles, open a command prompt and type the following commands:

        For Linux:
              EAP_HOME/bin/add-user.sh -a -u quickuser1 -p quick123+ --role Intern
              EAP_HOME/bin/add-user.sh -a -u quickuser2 -p quick+123 --role AppTwo

        For Windows:
              EAP_HOME\bin\add-user.bat -a -u quickuser1 -p quick123+ --role Intern
              EAP_HOME\bin\add-user.bat -a -u quickuser2 -p quick+123 --role AppTwo

    If the connection was established before changing the roles it might be necessary to restart the main server, or even the whole domain.
    After that the invocation will be successful. The log output of the `appTwo` servers shows which Role is applied to the user. The output of the client will show you a simple line with the information provided by the different applications:
        
          InvokeAll succeed: MainAppSContext[anonymous]@master:app-main  >  [ {app1[quickuser1]@master:app-oneA, app1[quickuser1]@master:app-oneA, app1[quickuser2]@master:app-oneB, app1[quickuser2]@master:app-oneA, app1[quickuser2]@master:app-oneA, app1[quickuser2]@master:app-oneB, app1[quickuser2]@master:app-oneA, app1[quickuser2]@master:app-oneA} >  appTwo loop(7 time A-B expected){app2[quickuser1]@master:app-twoA, app2[quickuser2]@master:app-twoB, app2[quickuser1]@master:app-twoA, app2[quickuser2]@master:app-twoB, app2[quickuser1]@master:app-twoA, app2[quickuser2]@master:app-twoB, app2[quickuser1]@master:app-twoA, app2[quickuser2]@master:app-twoB, app2[quickuser1]@master:app-twoA, app2[quickuser2]@master:app-twoB, app2[quickuser1]@master:app-twoA, app2[quickuser2]@master:app-twoB, app2[quickuser1]@master:app-twoA, app2[quickuser2]@master:app-twoB, app2[quickuser1]@master:app-twoA, app2[quickuser2]@master:app-twoB} ]
         
    The resulting output in detail:
    * The client calls the `MainAppSContext` bean on the `app-main` server on host master with no application security.
    * The `MainAppSContext` bean in `app-main` calls the `AppOne` bean in `app-one` using the scoped-context and establishes the clustered view.
        * It initially connects using `quickuser1`
        * The clustered view is created using `quickuser2`. This takes some time, but once it takes effect, all calls are load-balanced.
    * The calls to the 'AppTwo' bean in `app-two` are made using two different scoped-context settings and both are used alternately 7 times. This means the servers `app-twoA` and `app-twoB` are called alternately seven times each.

5. If it is necessary to invoke the client with a different JBoss version the main class can be invoked by using the following command from the root directory of this quickstart. Replace EAP_HOME with your current installation path. The output should be similar to the previous mvn executions.

        java -cp EAP_HOME/bin/client/jboss-client.jar:app-main/ejb/target/jboss-ejb-multi-server-app-main-ejb-client.jar:app-two/ejb/target/jboss-ejb-multi-server-app-two-ejb-client.jar:client/target/jboss-ejb-multi-server-client.jar org.jboss.as.quickstarts.ejb.multi.server.Client


_NOTE:_
 
* _If exec is called multiple times, the invocation for `app1` might use `app-oneA` and `app-oneB` node due to cluster loadbalancing._
* _A new feature introduced in JBoss EAP 6.1 or later will deny the invocation of unsecured methods of `appOne`/`appTwo` since security is enabled but the method does not include @Roles. You need to set 'default-missing-method-permissions-deny-access = false' for the `ejb3` subsystem within the domain profile "ha" and "default" to allow the method invocation. See the install-domain.cli script._


Access the JSF application inside the main-application
---------------------

The JSF example shows different annotations to inject the EJB. Also how to handle the annotation if different beans implement the same interface and therefore the container is not able to decide which bean needs to be injected without additional informations.

1. Make sure that the deployments are successful as described above.
2. Use a browser to access the JSF application at the following URL: <http://localhost:8080/jboss-ejb-multi-server-app-main-web/>
3. Insert a message in the Text input and invoke the different methods. The result is shown in the browser.
4. See server logfiles and find your given message logged as INFO.

_NOTE :_

* _If you try to invoke `MainAppSContext` you need to update the user `quickuser1` and `quickuser2` and give them one of the Roles `AppTwo` or `Intern`._

Access the Servlet application deployed as a WAR inside a minimal server
---------------------

An example how to access EJB's from a separate instance which only contains a web application.

1. Make sure that the deployments are successful as described above.
2. Use a browser to access the Servlet at the following URL: <http://localhost:8380/jboss-ejb-multi-server-app-web/>
3. The Servlet will invoke the remote EJBs directly and show the results, compare that the invocation is successful

_NOTE : A new feature in EAP 6.1 or later will deny the invocation of unsecured methods of `appOne`/`appTwo` since security is enabled but the method does not include @Roles. You need to set 'default-missing-method-permissions-deny-access = false' for the `ejb3` subsystem within the domain profile "ha" and "default" to allow the method invocation.  See the install-domain.cli script._


Undeploy the Archives
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        For Linux: EAP_HOME/bin/jboss-cli.sh --connect --file=undeploy-domain.cli
        For Windows: EAP_HOME\bin\jboss-cli.bat --connect --file=undeploy-domain.cli


Remove the Server Domain Configuration
--------------------

You can remove the domain configuration by manually restoring the back-up copies the configuration files or by running the JBoss CLI Script. 

### Remove the Server Domain Configuration Manually           
1. If it is running, stop the JBoss EAP server.
2. Restore the `EAP_HOME/domain/configuration/domain.xml` and `EAP_HOME/domain/configuration/host.xml` files with the back-up copies of the files. Be sure to replace EAP_HOME with the path to your server.

### Remove the Security Domain Configuration by Running the JBoss CLI Script

_Note: This script returns the server to a default configuration and the result may not match the server configuration prior to testing this quickstart. If you were not running with the default configuration before testing this quickstart, you should follow the intructions above to manually restore the configuration to its previous state._

1. Start the JBoss EAP server by typing the following: 

        For Linux:   EAP_HOME/bin/domain.sh
        For Windows: EAP_HOME\bin\domain.bat
2. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing EAP_HOME with the path to your server.

        For Linux: EAP_HOME/bin/jboss-cli.sh --connect --file=remove-configuration.cli
        For Windows: EAP_HOME\bin\jboss-cli.bat --connect --file=remove-configuration.cli
This script removes the server configuration that was done by the `install-domain.cli` script. You should see the following result following the script commands:

        The batch executed successfully.

Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse) 

Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc

