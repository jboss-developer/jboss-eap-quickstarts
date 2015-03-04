wsat-simple: WS-AT (WS-AtomicTransaction) - Simple
==================================================
Author: Paul Robinson  
Level: Intermediate  
Technologies: WS-AT, JAX-WS  
Summary: The `wsat-simple` quickstart demonstrates a WS-AT (WS-AtomicTransaction) enabled JAX-WS Web service, bundled as a WAR, and deployed to JBoss EAP.  
Target Product: JBoss EAP  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

The `wsat-simple` quickstart demonstrates the deployment of a WS-AT (WS-AtomicTransaction) enabled JAX-WS Web service bundled in a WAR archive for deployment to Red Hat JBoss Enterprise Application Platform.

The Web service is offered by a Restaurant for making bookings. The Service allows bookings to be made within an Atomic Transaction.

This example demonstrates the basics of implementing a WS-AT enabled Web service. It is beyond the scope of this quick start to demonstrate more advanced features. In particular:

1. The Service does not implement the required hooks to support recovery in the presence of failures.
2. It also does not utilize a transactional back end resource.
3. Only one Web service participates in the protocol. As WS-AT is a 2PC coordination protocol, it is best suited to multi-participant scenarios.

For a more complete example, please see the XTS demonstrator application that ships with the Narayana project: http://www.jboss.org/narayana.

It is also assumed that you have an understanding of WS-AtomicTransaction. For more details, read the XTS documentation that ships with the Narayana project, which can be downloaded here: http://www.jboss.org/narayana/documentation/4_17_4_Final.

The application consists of a single JAX-WS web service that is deployed within a WAR archive. It is tested with a JBoss Arquillian enabled JUnit test.

When running the `org.jboss.as.quickstarts.wsat.simple.ClientTest#testCommit()` method, the following steps occur:

1. A new Atomic Transaction (AT) is created by the client.
2. An operation on a WS-AT enabled Web service is invoked by the client.
3. The JaxWSHeaderContextProcessor in the WS Client handler chain inserts the WS-AT context into the outgoing SOAP message
4. When the service receives the SOAP request, the JaxWSHeaderContextProcessor in its handler chain inspects the WS-AT context and associates the request with this AT.
5. The Web service operation is invoked...
6. A participant is enlisted in this AT. This allows the Web Service logic to respond to protocol events, such as Commit and Rollback.
7. The service invokes the business logic. In this case, a booking is made with the restaurant.
8. The backend resource is prepared. This ensures that the Backend resource can undo or make permanent the change when told to do so by the coordinator.
10. The client can then decide to commit or rollback the AT. If the client decides to commit, the coordinator will begin the 2PC protocol. If the participant decides to rollback, all participants will be told to rollback.

There is another test that shows what happens if the client decides to rollback the AT.


System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.1 or later. 

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.


Start the JBoss EAP Server with the Custom Options
----------------------

First, edit the log level to reduce the amount of log output. This should make it easier to read the logs produced by this example. To do this add the
following logger block to the ./docs/examples/configs/standalone-xts.xml of your JBoss distribution. You should add it just bellow one of the other logger blocks.

        <logger category="org.apache.cxf.service.factory.ReflectionServiceFactoryBean">
            <level name="WARN"/>
        </logger>         

Next you need to start JBoss EAP with the XTS subsystem enabled. This is enabled through the optional server configuration *standalone-xts.xml*. To do this, run the following commands from the top-level directory of JBoss EAP:

        For Linux:     ./bin/standalone.sh --server-config=../../docs/examples/configs/standalone-xts.xml
        For Windows:   \bin\standalone.bat --server-config=..\..\docs\examples\configs\standalone-xts.xml


Run the Arquillian Tests 
-------------------------

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container. 

_NOTE: The following commands assume you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Run the Arquillian Tests](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/RUN_ARQUILLIAN_TESTS.md#run-the-arquillian-tests) for complete instructions and additional options._

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn clean test -Parq-jbossas-remote 

4. You should see the following result.

        Results :

        Tests run: 2, Failures: 0, Errors: 0, Skipped: 0

_Note: You see the following warning when you run the Arquillian tests in remote mode._

      WARNING: Configuration contain properties not supported by the backing object org.jboss.as.arquillian.container.remote.RemoteContainerConfiguration
      Unused property entries: {serverConfig=../../docs/examples/configs/standalone-xts.xml}
      Supported property names: [managementPort, username, managementAddress, password]

_This is because, in remote mode, you are responsible for starting the server with the XTS subsystem enabled. When you run the Arquillian tests in managed mode, the container uses the `serverConfig` property defined in the `arquillian.xml` file to start the server with the XTS subsystem enabled._

Investigate the Server Log
----------------------------

The following messages should appear in the server log. The messages trace the steps taken by the tests. Note there may be other informational log messages interlaced between these. 

Test rollback:

        10:54:29,607 INFO  [stdout] (http-localhost.localdomain/127.0.0.1:8080-4) Starting 'testRollback'. This test invokes a WS within an AT. The AT is later rolled back, which causes the back-end resource(s) to be rolled back.
        10:54:29,607 INFO  [stdout] (http-localhost.localdomain/127.0.0.1:8080-4) [CLIENT] Creating a new WS-AT User Transaction
        10:54:29,608 INFO  [stdout] (http-localhost.localdomain/127.0.0.1:8080-4) [CLIENT] Beginning Atomic Transaction (All calls to Web services that support WS-AT wil be included in this transaction)
        10:54:29,932 INFO  [stdout] (http-localhost.localdomain/127.0.0.1:8080-4) [CLIENT] invoking makeBooking() on WS
        10:54:30,000 INFO  [stdout] (http-localhost.localdomain/127.0.0.1:8080-25) [SERVICE] Restaurant service invoked to make a booking
        10:54:30,000 INFO  [stdout] (http-localhost.localdomain/127.0.0.1:8080-25) [SERVICE] Enlisting a Durable2PC participant into the AT
        10:54:30,121 INFO  [stdout] (http-localhost.localdomain/127.0.0.1:8080-25) [SERVICE] Invoking the back-end business logic
        10:54:30,122 INFO  [stdout] (http-localhost.localdomain/127.0.0.1:8080-25) [SERVICE] makeBooking called on backend resource.
        10:54:30,126 INFO  [stdout] (http-localhost.localdomain/127.0.0.1:8080-4) [CLIENT] rolling back Atomic Transaction (This will cause the AT and thus the enlisted back-end resources to rollback)
        10:54:30,349 INFO  [stdout] (TaskWorker-2) [SERVICE] one or more participants voted 'aborted' or a failure occurred, so coordinator tells the participant to rollback
        10:54:30,350 INFO  [stdout] (TaskWorker-2) [SERVICE] rollback called on backend resource.

Test commit:

        10:54:30,662 INFO  [stdout] (http-localhost.localdomain/127.0.0.1:8080-54) Starting 'testCommit'. This test invokes a WS within an AT. The AT is later committed, which causes the back-end resource(s) to be committed.
        10:54:30,663 INFO  [stdout] (http-localhost.localdomain/127.0.0.1:8080-54) [CLIENT] Creating a new WS-AT User Transaction
        10:54:30,663 INFO  [stdout] (http-localhost.localdomain/127.0.0.1:8080-54) [CLIENT] Beginning Atomic Transaction (All calls to Web services that support WS-AT wil be included in this transaction)
        10:54:30,797 INFO  [stdout] (http-localhost.localdomain/127.0.0.1:8080-54) [CLIENT] invoking makeBooking() on WS
        10:54:30,848 INFO  [stdout] (http-localhost.localdomain/127.0.0.1:8080-66) [SERVICE] Restaurant service invoked to make a booking
        10:54:30,849 INFO  [stdout] (http-localhost.localdomain/127.0.0.1:8080-66) [SERVICE] Enlisting a Durable2PC participant into the AT
        10:54:30,936 INFO  [stdout] (http-localhost.localdomain/127.0.0.1:8080-66) [SERVICE] Invoking the back-end business logic
        10:54:30,937 INFO  [stdout] (http-localhost.localdomain/127.0.0.1:8080-66) [SERVICE] makeBooking called on backend resource.
        10:54:30,942 INFO  [stdout] (http-localhost.localdomain/127.0.0.1:8080-54) [CLIENT] committing Atomic Transaction (This will cause the AT to complete successfully)
        10:54:31,046 INFO  [stdout] (TaskWorker-2) [SERVICE] Prepare called on participant, about to prepare the back-end resource
        10:54:31,046 INFO  [stdout] (TaskWorker-2) [SERVICE] prepare called on backend resource.
        10:54:31,047 INFO  [stdout] (TaskWorker-2) [SERVICE] back-end resource prepared, participant votes prepared
        10:54:31,067 WARN  [com.arjuna.wst] (TaskWorker-2) ARJUNA043219: Could not save recovery state for non-serializable durable WS-AT participant restaurantServiceAT:ba222c73-00c3-4ecc-921c-80fd5dfdc11a
        10:54:31,209 INFO  [stdout] (TaskWorker-2) [SERVICE] all participants voted 'prepared', so coordinator tells the participant to commit
        10:54:31,210 INFO  [stdout] (TaskWorker-2) [SERVICE] commit called on backend resource.

_Note: You can ignore the warning message `ARJUNA043219: Could not save recovery state for non-serializable durable WS-AT participant restaurantServiceAT` that is printed in the server console. This quickstart does not implement the required recovery hooks in the interest of making it easy to follow. In a real world production application, you should provide the required recovery code. For more information, see_ <http://docs.jboss.org/jbosstm/4.17.4.Final/guides/xts-administration_and_development_guide/index.html#d0e2450>.



Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a JBoss EAP server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 

This quickstart is more complex than the others. It requires that you configure the JBoss EAP server to use the *standalone-xts.xml* configuration file, which is located in an external configuration directory.

1. Import the quickstart into JBoss Developer Studio. 
2. If you have not already done so, you must configure a new JBoss EAP server to use the XTS configuration.
   * In the `Server` tab, right-click and choose `New` --> `Server`.
   * For the `Server name`, enter "JBoss EAP XTS Configuration" and click `Next`.
   * In the `Create a new Server Adapter` dialog, choose `Create a new runtime (next page)` and click `Next`.
   * In the `JBoss Runtime` dialog, enter the following information and then click `Finish`.
   
            Name: JBoss EAP XTS Runtime
            Home Directory: (Browse to the server directory and select it)
            Execution Environment: (Choose your runtime JRE if not correct)
            Configuration base directory: (This should already point to your server configuration directory)
            Configuration file: ../../docs/examples/configs/standalone-xts.xml
3. Start the new `JBoss EAP XTS Configuration` server. 
4. Right-click on the `jboss-wsat-simple` project, choose `Run As` --> `Maven build`, enter "clean test -Parq-jbossas-remote" for the `Goals:`, and click `Run` to run the Arquillian tests. The test results appear in the console.


Debug the Application
------------------------------------

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

        mvn dependency:sources



Build and Deploy the Quickstart - to OpenShift
-------------------------

### Create an OpenShift Express Account and Domain

If you do not yet have an OpenShift account and domain, [Sign in to OpenShift](https://openshift.redhat.com/app/login) to create the account and domain. [Get Started with OpenShift](https://www.openshift.com/get-started) will show you how to install the OpenShift Express command line interface.

### Create the OpenShift Application

_NOTE_: The domain name for this application will be `wsatsimple-YOUR_DOMAIN_NAME.rhcloud.com`. In these instructions, be sure to replace all instances of `YOUR_DOMAIN_NAME` with your own OpenShift account user name.

Open a shell command prompt and change to a directory of your choice. Enter the following command to create a JBoss EAP 6 application:

    rhc app create -a wsatsimple -t jbosseap-6

This command creates an OpenShift application called `wsatsimple` and will run the application inside the `jbosseap-6` container. You should see some output similar to the following:

    Application Options
    -------------------
      Namespace:  YOUR_DOMAIN_NAME
      Cartridges: jbosseap-6 (addtl. costs may apply)
      Gear Size:  default
      Scaling:    no

    Creating application 'wsatsimple' ... done

    Waiting for your DNS name to be available ... done

    Cloning into 'wsatsimple'...
    Warning: Permanently added the RSA host key for IP address '54.237.58.0' to the list of known hosts.

    Your application 'wsatsimple' is now available.

      URL:        http://wsatsimple-YOUR_DOMAIN_NAME.rhcloud.com/
      SSH to:     52864af85973ca430200006f@wsatsimple-YOUR_DOMAIN_NAME.rhcloud.com
      Git remote: ssh://52864af85973ca430200006f@wsatsimple-YOUR_DOMAIN_NAME.rhcloud.com/~/git/wsatsimple.git/
      Cloned to:  CURRENT_DIRECTORY/wsatsimple

    Run 'rhc show-app wsatsimple' for more details about your app.

The create command creates a git repository in the current directory with the same name as the application, in this case, `wsatsimple`. Notice that the output also reports the URL at which the application can be accessed. Make sure it is available by typing the published url <http://wsatsimple-YOUR_DOMAIN_NAME.rhcloud.com/> into a browser or use command line tools such as curl or wget. Be sure to replace `YOUR_DOMAIN_NAME` with your OpenShift account domain name.

### Migrate the Quickstart Source

Now that you have confirmed it is working you can migrate the quickstart source. You do not need the generated default application, so navigate to the new git repository directory and tell git to remove the source and pom files:

        cd wsatsimple
        git rm -r src pom.xml

Copy the source for the `wsat-simple` quickstart into this new git repo:

        cp -r <quickstarts>/wsat-simple/src .
        cp <quickstarts>/wsat-simple/pom.xml .

### Configure the OpenShift Server

Openshift does not have Web services or WS-AT enabled by default, so we need to modify the server configuration. To do this:

1. Open the hidden `.openshift/config/standalone.xml` file in an editor. 
2. If the following extensions do not exist, add them under the `<extensions>` element: 

        <extension module="org.jboss.as.webservices"/>
        <extension module="org.jboss.as.xts"/>
3. If the `jmx` subsystem is not configured under the `<profile>` element, copy the following under the `<profile>` element to enable and configure JMX:

        <subsystem xmlns="urn:jboss:domain:jmx:1.1">
            <show-model value="true"/>
            <remoting-connector/>
        </subsystem>
4. If the `webservices` subsystem is not configured under the `<profile>` element, copy the following under the `<profile>` element to enable and configure Web Services:
        
        <subsystem xmlns="urn:jboss:domain:webservices:1.2">
            <modify-wsdl-address>true</modify-wsdl-address>
            <wsdl-host>${env.OPENSHIFT_GEAR_DNS}</wsdl-host>
            <wsdl-port>80</wsdl-port>
            <endpoint-config name="Standard-Endpoint-Config"/>
            <endpoint-config name="Recording-Endpoint-Config">
                <pre-handler-chain name="recording-handlers" protocol-bindings="##SOAP11_HTTP ##SOAP11_HTTP_MTOM ##SOAP12_HTTP ##SOAP12_HTTP_MTOM">
                    <handler name="RecordingHandler" class="org.jboss.ws.common.invocation.RecordingServerHandler"/>
                </pre-handler-chain>
            </endpoint-config>
        </subsystem>
5. If the `xts` subsystem is not configured under the `<profile>` element, copy the following under the `<profile>` element to enable and configure XTS:

        <subsystem xmlns="urn:jboss:domain:xts:1.0">
            <xts-environment url="http://${env.OPENSHIFT_JBOSSEAP_IP}:8080/ws-c11/ActivationService"/>
        </subsystem>
6. To reduce the amount of logging and make it easier to read the logs produced by this quickstart, edit the log level by adding the following block just below the other blocks:

        <logger category="org.apache.cxf.service.factory.ReflectionServiceFactoryBean">
            <level name="WARN"/>
        </logger>

The `.openshift/config/standalone.xml` is now ready, so save it and exit your editor.

### Deploy the OpenShift Application

You can now deploy the changes to your OpenShift application using git as follows:

        git add src pom.xml .openshift/config/standalone.xml
        git commit -m "wsat-simple quickstart on OpenShift"
        git push

OpenShift will build the application using Maven, and deploy it to JBoss EAP. If successful, you should see output similar to:

        remote: [INFO] ------------------------------------------------------------------------
        remote: [INFO] BUILD SUCCESS
        remote: [INFO] ------------------------------------------------------------------------
        remote: [INFO] Total time: 19.991s
        remote: [INFO] Finished at: Wed Mar 07 12:48:15 EST 2012
        remote: [INFO] Final Memory: 8M/168M
        remote: [INFO] ------------------------------------------------------------------------
        remote: Running .openshift/action_hooks/build
        remote: Emptying tmp dir: /var/lib/libra/1e63c17c2dd94a329f21555a33dc617d/wsatsimple/jbossas-7/standalone/tmp/vfs
        remote: Emptying tmp dir: /var/lib/libra/1e63c17c2dd94a329f21555a33dc617d/wsatsimple/jbossas-7/standalone/tmp/work
        remote: Running .openshift/action_hooks/deploy
        remote: Starting application...
        remote: Done
        remote: Running .openshift/action_hooks/post_deploy
        To ssh://1e63c17c2dd94a329f21555a33dc617d@wsatsimple-YOUR_DOMAIN_NAME.rhcloud.com/~/git/wsatsimple.git/
           e6f80bd..63504b9  master -> master

Note that the `openshift` profile in the `pom.xml` file is activated by OpenShift. This causes the WAR built by OpenShift to be copied to the `deployments/` directory and deployed without a context path.

### Test the OpenShift Application

Once the application is deployed, you can test it by accessing the following URL either via a browser or using tools such as curl or wget. Be sure to replace the `YOUR_DOMAIN_NAME` in the URL with your OpenShift account domain name.

        http://wsatsimple-YOUR_DOMAIN_NAME.rhcloud.com/WSATSimpleServletClient

If the application has run successfully you should see some output in the browser. You should also see some output on the server log, similar to the output from the "Test commit" test above.

You can use the OpenShift command line tools or the OpenShift web console to discover and control the application.

### View the JBoss EAP Server Log on OpenShift

Now you can look at the output of the server by running the following command:

        rhc tail -a wsatsimple

This will show the tail of the JBoss EAP server log.

_Note:_ You may see the following error in the log:

        2014/03/17 07:50:36,231 ERROR [org.jboss.as.controller.management-operation] (management-handler-thread - 4) JBAS014613: Operation ("read-resource") failed - address: ([("subsystem" => "deployment-scanner")]) - failure description: "JBAS014807: Management resource '[(\"subsystem\" => \"deployment-scanner\")]' not found"

This is a benign error that occurs when the status of the deployment is checked too early in the process. This process is retried, so you can safely ignore this error.

### Delete the OpenShift Application

When you are finished with the application you can delete it from OpenShift as follows:

        rhc app-delete -a wsatsimple

_Note_: There is a limit to the number of applications you can deploy concurrently to OpenShift. If the `rhc app create` command returns an error indicating you have reached that limit, you must delete an existing application before you continue. 

* To view the list of your OpenShift applications, type: `rhc domain show`
* To delete an application from OpenShift, type the following, substituting the application name you want to delete: `rhc app-delete -a APPLICATION_NAME_TO_DELETE`

