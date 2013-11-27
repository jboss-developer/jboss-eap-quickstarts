helloworld-ws: Hello World JAX-WS Web Service
==================================================
Author: Lee Newson  
Level: Beginner  
Technologies: JAX-WS  
Summary: Deployment of a basic JAX-WS Web service bundled in a WAR archive  
Target Product: EAP  
Product Versions: EAP 6.1, EAP 6.2  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

This example demonstrates the use of *JAX-WS* in Red Hat JBoss Enterprise Application Platform as a simple Hello World application.

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.1 or later.

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#configure-maven) before testing the quickstarts.


Start the JBoss Server
----------------------         

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install jboss-as:deploy

4. This will deploy `target/jboss-helloworld-ws.war` to the running instance of the server.

Access the application 
---------------------

You can check that the Web Service is running and deployed correctly by accessing the following URL: <http://localhost:8080/jboss-helloworld-ws?wsdl>. This URL will display the deployed WSDL endpoint for the Web Service.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Run the Client Tests using Arquillian
-------------------------

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container. 

_NOTE: The following commands assume you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Run the Arquillian Tests](../README.md#run-the-arquillian-tests) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

		mvn clean test -Parq-jbossas-remote


Investigate the Console Output
----------------------------

The following expected output should appear. The output shows what was said to the Web Service by the client and the responses it received.

    WSDL Deployment URL: http://localhost:8080/jboss-helloworld-ws/HelloWorldService?wsdl
    [Client] Requesting the WebService to say Hello.
    [WebService] Hello World!
    [Client] Requesting the WebService to say Hello to John.
    [WebService] Hello John!
    [Client] Requesting the WebService to say Hello to John, Mary and Mark.
    [WebService] Hello John, Mary & Mark!
    Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.988 sec


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc


Build and Deploy the Quickstart - to OpenShift
-------------------------

### Create an OpenShift Express Account and Domain

If you do not yet have an OpenShift account and domain, [Sign in to OpenShift](https://openshift.redhat.com/app/login) to create the account and domain. [Get Started with OpenShift](https://openshift.redhat.com/app/getting_started) will show you how to install the OpenShift Express command line interface.

### Create the OpenShift Application

Note that we use `USER_DOMAIN_NAME` for these examples. You need to substitute it with your own OpenShift account user name.

Open a shell command prompt and change to a directory of your choice. Enter the following command to create a JBoss EAP 6 application:

    rhc app create -a helloworldws -t jbosseap-6

_NOTE_: The domain name for this application will be `helloworldws-YOUR_DOMAIN_NAME.rhcloud.com`. Be sure to replace `YOUR_DOMAIN_NAME` with your own OpenShift account user name.

This command creates an OpenShift application called `helloworldws` and will run the application inside the `jbosseap-6` container. You should see some output similar to the following:

    Application Options
    -------------------
      Namespace:  YOUR_DOMAIN_NAME
      Cartridges: jbosseap-6 (addtl. costs may apply)
      Gear Size:  default
      Scaling:    no

    Creating application 'helloworldws' ... done

    Waiting for your DNS name to be available ... done

    Cloning into 'helloworldws'...
    Warning: Permanently added the RSA host key for IP address '54.237.58.0' to the list of known hosts.

    Your application 'helloworldws' is now available.

      URL:        http://helloworldws-YOUR_DOMAIN_NAME.rhcloud.com/
      SSH to:     52864af85973ca430200006f@helloworldws-YOUR_DOMAIN_NAME.rhcloud.com
      Git remote: ssh://52864af85973ca430200006f@helloworldws-YOUR_DOMAIN_NAME.rhcloud.com/~/git/helloworldws.git/
      Cloned to:  CURRENT_DIRECTORY/helloworldws

    Run 'rhc show-app helloworldws' for more details about your app.

The create command creates a git repository in the current directory with the same name as the application, in this case, `helloworldws`. Notice that the output also reports the URL at which the application can be accessed. Make sure it is available by typing the published url <http://helloworldws-YOUR_DOMAIN_NAME.rhcloud.com/> into a browser or use command line tools such as curl or wget. Be sure to replace `YOUR_DOMAIN_NAME` with your OpenShift account domain name.

### Migrate the Quickstart Source

Now that you have confirmed it is working you can now migrate the quickstart source and POM file. You no longer need the default application, so change directory into the new git repository and tell git to remove the source and pom files:

    cd helloworldws
    git rm -r src pom.xml

Copy the source and POM file for the `helloworld-ws` quickstart into this new git repository:

    cp -r QUICKSTART_HOME/helloworld-ws/src .
    cp QUICKSTART_HOME/helloworld-ws/pom.xml .
        
### Configure the OpenShift Server

Verify that Openshift has Web services configured by default. To do this: 

1. Open the hidden `.openshift/config/standalone.xml` file in an editor. 
2. If the `webservices` subsystem is not configured as below under the `<profile>` element, copy the following and replace the `webservices` subsystem to enable and configure Web Services:
        
        <subsystem xmlns="urn:jboss:domain:webservices:1.1">
            <modify-wsdl-address>true</modify-wsdl-address>
            <wsdl-host>${env.OPENSHIFT_APP_DNS}</wsdl-host>
            <wsdl-port>80</wsdl-port>
            <endpoint-config name="Standard-Endpoint-Config"/>
            <endpoint-config name="Recording-Endpoint-Config">
                <pre-handler-chain name="recording-handlers" protocol-bindings="##SOAP11_HTTP ##SOAP11_HTTP_MTOM ##SOAP12_HTTP ##SOAP12_HTTP_MTOM">
                    <handler name="RecordingHandler" class="org.jboss.ws.common.invocation.RecordingServerHandler"/>
                </pre-handler-chain>
            </endpoint-config>
        </subsystem>

### Deploy the OpenShift Application

You can now deploy the changes to your OpenShift application using git as follows:

    git add src pom.xml
    git commit -m "helloworld-ws quickstart on OpenShift"
    git push

The final push command triggers the OpenShift infrastructure to build and deploy the changes. 

Note that the `openshift` profile in `pom.xml` file is activated by OpenShift and causes the WAR built by openshift to be copied to the `deployments/` directory and deployed to the "jboss-helloworld-ws" context path.

### Access the OpenShift Application

Now you will start to tail the log files of the server. To do this run the following command, remembering to replace the application name and login id.

    rhc tail helloworldws

Once the application is deployed, you can test the application by accessing the following URL either via a browser or using tools such as curl or wget. Be sure to replace the `YOUR_DOMAIN_NAME` in the URL with your OpenShift account domain name.

    http://helloworldws-YOUR_DOMAIN_NAME.rhcloud.com/jboss-helloworld-ws/HelloWorldService?wsdl

If the application has run successfully you should see the WSDL output in the browser.

You can use the OpenShift command line tools or the OpenShift web console to discover and control the application.

### Run the Remote Client Tests against Openshift

This quickstart provides tests that can be run remotely. By default, these tests are configured to be skipped as the tests require the application to be running remotely. 

_NOTE: The following commands assume you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line.._

1. Make sure you have deployed the Application to Openshift as described above.
2. Type the following command to run the test goal with the following profile activated and the URL of the deployed Application. Be sure to replaces `YOUR_DOMAIN_NAME` in the URL with your OpenShift account domain name:

		mvn clean test -Pjbossas-remote -Dremote.server.url=http://helloworldws-YOUR_DOMAIN_NAME.rhcloud.com/

### Delete the OpenShift Application

When you are finished with the application you can delete it from OpenShift as follows:

    rhc app-delete -a helloworldws

_Note_: There is a limit to the number of applications you can deploy concurrently to OpenShift. If the `rhc app create` command returns an error indicating you have reached that limit, you must delete an existing application before you continue. 

* To view the list of your OpenShift applications, type: `rhc domain show`
* To delete an application from OpenShift, type the following, substituting the application name you want to delete: `rhc app-delete -a APPLICATION_NAME_TO_DELETE`

