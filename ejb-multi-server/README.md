ejb-multi-server: EJB applications deployed in different servers communicating vie EJB remote calls
======================================================
Author: Wolf-Dieter Fink
Level: Advanced
Technologies: EJB, EAR


What is it?
-----------

This example demonstrates the communication between applications deployed to different servers.
Each application is deployed as an EAR that contains a simple EJB3.1 bean which only log the invocation.

Also the configuration is done by CLI batch-scripts.


The example is composed of multiple maven projects, each with a shared parent. The projects are as follows:

app-main      : Application which can is called by the 'client' and call the different sub-applications

app-one/two   : Simple application contain an ejb and ear sub-project to build the ejb.jar and app.ear file.
                The application is only one EJB which log a statement if a method is called and return the jboss.node.name and credentials.

app-web       : A simple war application with only one servlet to show how to invoke EJB's on a different server

client        : This project builds the standalone client and execute it.

The root `pom.xml` builds each of the subprojects in the above order.



System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Start and configure JBoss Enterprise Application Platform EAP6 or JBoss AS7
-------------------------

With this quickstart scripts are provided to configure a standard JBoss AS7 or EAP6 via CLI batch scripts.

 * Unzip or install a fresh AS7 or EAP6 instance
 * Start the instance
 
     `bin/domain.sh`
     
 * navigate to the project root directory and run the following command
 
     `JBOSS_HOME/bin/jboss-cli.sh --connect --file=install-domain.cli`
     
 * Add application user  [Add an Application User](../README.md#addapplicationuser)
   To add all necessary users run the following commands, please use this usernames and paswords because the domain configuration and the client use it.
   
     `bin/add-user.sh -a -u quickuser -p quick-123 --silent`
     `bin/add-user.sh -a -u quickuser1 -p quick123+ --silent`
     `bin/add-user.sh -a -u quickuser2 -p quick+123 --silent`
 
_NOTE: If a server 7.1.1 is used the install-domain.cli will fail because of unsupported commands, see the comments within the cli script._
_NOTE: The add-user script does not support the direct user add via parameter until AS7.2 EAP6.1, please add the required users manually as ApplicationUser without apply any roles._
_NOTE: If there are ERROR's shown in the server.log during installing or deploying please stop the domain and start it again to ensure further steps are running correct._

Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy) for complete instructions and additional options._

1. Make sure you have started and configured the JBoss Server successful as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build the artifacts:

        mvn clean install
        
4. Deploy the applications by using the provided CLI batch script

       `JBOSS_HOME/bin/jboss-cli.sh --connect --file=deploy-domain.cli`
       
     This will deploy the app-*.ear files to different server-groups of the running domain.


Access the remote-client application
---------------------

1. Make sure that the deployment are successful as described above.
2. navigate to the client root directory of this quickstart.
3. Type this command to run the application

        `mvn exec:java`
        

        The output of the client will show you a simple line with the informations provided by the different applications:
        
          InvokeAll succeed: MainApp[anonymous]@master:app-main  >  [ app1[anonymous]@master:app-oneA > app2[quickuser2]@master:app-two ; app2[quickuser2]@master:app-two ]

        The line shows that the MainApp is called with the user 'anonymous' at node 'master:app-main' and the sub-call is proceeded by the 'master:app-oneA' node and 'master:app-two' node as quickuser2.
        In the logfiles of the different servers you might follow the invocations on server-side.

4. To invoke the bean witch uses the 'scoped-client-context' add a property, type the following command

        `mvn exec:java -DUseEjbClient34=true`

        The invocation of appTwo will not work as the secured method will be called and there is no Role for the user defined.
        Try to update the user 'quickuser1' and 'quickuser2' and give them one of the Roles 'AppTwo' or 'Intern'. After that the invocation will be successful.
        The log output of the appTwo servers shows which Role is applied to the user.

        The output of the client will show you a simple line with the informations provided by the different applications:
        
          InvokeAll succeed: MainEjbClient34App[anonymous]@master:app-main  >  [ {app1[quickuser1]@master:app-oneA, app1[quickuser2]@master:app-oneB, app1[quickuser2]@master:app-oneB, app1[quickuser1]@master:app-oneA, app1[quickuser1]@master:app-oneA, app1[quickuser1]@master:app-oneA, app1[quickuser2]@master:app-oneB, app1[quickuser1]@master:app-oneA} >  appTwo loop(7 time A-B expected){app2[quickuser1]@master:app-twoA, app2[quickuser2]@master:app-twoB, app2[quickuser1]@master:app-twoA, app2[quickuser2]@master:app-twoB, app2[quickuser1]@master:app-twoA, app2[quickuser2]@master:app-twoB, app2[quickuser1]@master:app-twoA, app2[quickuser2]@master:app-twoB, app2[quickuser1]@master:app-twoA, app2[quickuser2]@master:app-twoB, app2[quickuser1]@master:app-twoA, app2[quickuser2]@master:app-twoB, app2[quickuser1]@master:app-twoA, app2[quickuser2]@master:app-twoB, app2[quickuser1]@master:app-twoA, app2[quickuser2]@master:app-twoB} ]
         
        The line shows that the bean MainEjbClient34App is not secured and called at app-main server, the sub calls to app-one# are using the scoped-context and the cluster view
        need a time to be established. This is shown as the cluster-view call the appOne with the user 'quickuser2'.
        AppTwo is called with two different scoped-context settings both are used alternately 7 times.


_NOTE : If exec is called more often the invocation for app1 might use app-oneA and app-oneB node due to cluster loadbalancing._

_NOTE : If a version newer than AS7.2 of JBoss is used, a new feature deny the invocation of unsecured methods of appOne/appTwo as the security is enabled but the method does not include @Roles.
        You need to set 'default-missing-method-permissions-deny-access = false' for the ejb3 subsystem within the domain profile "ha" and "default" to allow the method invocation._

_NOTE : For AS 7.1.x and EAP 6.0 the client library must not be changed for this test, but if additional tests are added or a newer server version is used you might update the property '<jboss.client.bom.version>7.1.1.Final</jboss.client.bom.version>' in the root pom.xml to an apropriate version._


Access the JSF application inside the main-application
---------------------

1. Make sure that the deployment are successful as described above.
2. Use a browser to access localhost:8080/multi-server-MainApp
3. Insert a message in the Text input and invoke the different methods, the result is shown in the browser
4. See server logfiles and find your given message logged as INFO.

_NOTE : If you try to invoke MainEjbClient34App you need to update the user 'quickuser1' and 'quickuser2' and give them one of the Roles 'AppTwo' or 'Intern'._


Access the Servlet application deployed as a WAR inside a minimal server
---------------------

1. Make sure that the deployment are successful as described above.
2. Use a browser to access localhost:8380/appweb
3. The servlet will invoke the remote EJB's directly and show the results, compare that the invocation is successful

_NOTE : If a version newer than AS7.2 of JBoss is used, a new feature deny the invocation of unsecured methods of appOne/appTwo as the security is enabled but the method does not include @Roles.
        You need to set 'default-missing-method-permissions-deny-access = false' for the ejb3 subsystem within the domain profile "ha" and "default" to allow the method invocation._


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

       `JBOSS_HOME/bin/jboss-cli.sh --connect --file=undeploy-domain.cli`




Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse) 

Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc

