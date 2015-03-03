websocket-endpoint: Web application using WebSockets and JSON-P
================================================================
Author: Rafael Benevides  
Level: Beginner  
Technologies: CDI, WebSocket, JSON-P  
Summary: Shows how to use WebSockets with JSON to broadcast information to all open WebSocket sessions in JBoss EAP.  
Target Product: Sandbox  
Source: <https://github.com/jboss-developer/jboss-sandbox-quickstarts>   


What is it?
-----------

The `websocket-endpoint` quickstart demonstrates how to use Java API for WebSockets to create server endpoints in Red Hat JBoss Enterprise Application Platform. 

The `BidWebSocketEndpoint` provides the WebSocket endpoint that receives `Message` instances from clients/browsers and replies with the current `Bidding` instance. The conversion from JSON content to the specific instances are made by `MessageDecoder` and `BiddingEncode` classes.

Every update made on the `Bidding` are immediately propagated to all opened WebSocket sessions without any browser submission or AJAX polling mechanism.


System requirements
-------------------


The application this project produces is designed to be run on WildFly . 

All you need to build this project is Java 7.0 (Java SDK 1.7) or later, Maven 3.0 or later.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.


Use of WILDFLY_HOME
---------------

In the following instructions, replace `WILDFLY_HOME` with the actual path to your WildFly installation. The installation path is described in detail here: [Use of EAP_HOME and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_EAP_HOME.md#use-of-eap_home-and-jboss_home-variables).



Start the WildFly Server
-------------------------

1. Open a command line and navigate to the root of the  WildFly directory.
2. The following shows the command line to start the server with the default profile:

        For Linux:   WILDFLY_HOME/bin/standalone.sh
        For Windows: WILDFLY_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------

1. Make sure you have started the WildFly server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn package wildfly:deploy
4. This will deploy `target/jboss-websocket-endpoint.war` to the running instance of the server.
 


Access the application
---------------------

Access the running application in a browser at the following URL:  <http://localhost:8080//jboss-websocket-endpoint>

You're presented with a simple form that shows a bidding with the status `NOT_STARTED`. 

Click on `Do a bid!` button. That will start the bidding and trigger the 1 minute countdown timer. You can also notice that every Bid will be listed under the "List of bids" section.

You should open the application URL in other browsers or tabs. You will notice that every change on the bidding is automatically update in all opened browser or tabs. The item will be SOLD once that it reaches the "Buy now price". At the countdown timeout, the bidding will be EXPIRED. You can click on `Buy it now` button to immediately buy the item.

You can restart the bidding if you click on `Reset bidding` button.


Undeploy the Archive
--------------------

1. Make sure you have started the WildFly server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------


You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 


Debug the Application
------------------------------------

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

    mvn dependency:sources
   

Build and Deploy the Quickstart - to OpenShift
-------------------------

### Create an OpenShift Account and Domain

If you do not yet have an OpenShift account and domain, [Sign in to OpenShift](https://openshift.redhat.com/app/login) to create the account and domain. [Get Started with OpenShift](https://openshift.redhat.com/app/getting_started) will show you how to install the OpenShift Express command line interface.

### Create the OpenShift Application

_NOTE_: The domain name for this application will be `websocket-YOUR_DOMAIN_NAME.rhcloud.com`. In these instructions, be sure to replace all instances of `YOUR_DOMAIN_NAME` with your own OpenShift account user name.

Open a shell command prompt and change to a directory of your choice. Enter the following command to create a JBoss EAP 6 application:

       rhc create-app websocket jboss-wildfly-8

This command creates an OpenShift application named APPLICATION_NAME and will run the application inside the `jboss-wildfly-8`  container. You should see some output similar to the following:

    Application Options
    -------------------
      Namespace:  YOUR_DOMAIN_NAME
      Cartridges: jboss-wildfly-8
      Gear Size:  default
      Scaling:    no

    Creating application 'websocket' ... done

    Waiting for your DNS name to be available ... done

    Cloning into 'websocket'...
    Warning: Permanently added the RSA host key for IP address '54.237.58.0' to the list of known hosts.

    Your application 'websocket' is now available.

      URL:        http://websocket-YOUR_DOMAIN_NAME.rhcloud.com/
      SSH to:     52864af85973ca430200006f@websocket-YOUR_DOMAIN_NAME.rhcloud.com
      Git remote: ssh://52864af85973ca430200006f@websocket-YOUR_DOMAIN_NAME.rhcloud.com/~/git/APPLICATION_NAME.git/
      Cloned to:  CURRENT_DIRECTORY/APPLICATION_NAME

    Run 'rhc show-app websocket' for more details about your app.

The create command creates a git repository in the current directory with the same name as the application. Notice that the output also reports the URL at which the application can be accessed. Make sure it is available by typing the published url <http://websocket-YOUR_DOMAIN_NAME.rhcloud.com/> into a browser or use command line tools such as curl or wget. Be sure to replace `YOUR_DOMAIN_NAME` with your OpenShift account domain name.

### Migrate the Quickstart Source

Now that you have confirmed it is working you can migrate the quickstart source. You do not need the generated default application, so navigate to the new git repository directory and tell git to remove the source and pom files:

        cd websocket
        git rm -r src pom.xml

Copy the source for the QUICKSTART_NAME quickstart into this new git repository:

        cp -r QUICKSTART_HOME/websocket-endpoint/src .
        cp QUICKSTART_HOME/websocket-endpoint/pom.xml .


### Deploy the OpenShift Application

You can now deploy the changes to your OpenShift application using git as follows:

        git add src pom.xml
        git commit -m "websocket quickstart on OpenShift"
        git push

The final push command triggers the OpenShift infrastructure to build and deploy the changes. 

Note that the `openshift` profile in `pom.xml` is activated by OpenShift, and causes the WAR build by openshift to be copied to the `deployments/` directory, and deployed without a context path.

### Test the OpenShift Application

When the push command returns you can test the application by getting the following URL either via a browser or using tools such as curl or wget. Be sure to replace the `YOUR_DOMAIN_NAME` in the URL with your OpenShift account domain name.

        http://websocket-YOUR_DOMAIN_NAME.rhcloud.com 

You can use the OpenShift command line tools or the OpenShift web console to discover and control the application.

### View the JBoss EAP Server Log on OpenShift

Now you can look at the output of the server by running the following command:

        rhc tail -a websocket

This will show the tail of the JBoss EAP server log.

_Note:_ You may see the following error in the log:

        2014/03/17 07:50:36,231 ERROR [org.jboss.as.controller.management-operation] (management-handler-thread - 4) JBAS014613: Operation ("read-resource") failed - address: ([("subsystem" => "deployment-scanner")]) - failure description: "JBAS014807: Management resource '[(\"subsystem\" => \"deployment-scanner\")]' not found"

This is a benign error that occurs when the status of the deployment is checked too early in the process. This process is retried, so you can safely ignore this error.

### Delete the OpenShift Application

When you are finished with the application you can delete it as follows:

        rhc app-delete -a websocket

_Note_: There is a limit to the number of applications you can deploy concurrently to OpenShift. If the `rhc app create` command returns an error indicating you have reached that limit, you must delete an existing application before you continue. 

* To view the list of your OpenShift applications, type: `rhc domain show`
* To delete an application from OpenShift, type the following, substituting the application name you want to delete: `rhc app-delete -a APPLICATION_NAME_TO_DELETE`

