helloworld-rs: Helloworld Using JAX-RS (Java API for RESTful Web Services)
==========================================================================
Author: Gustavo A. Brey, Gaston Coco  
Level: Intermediate  
Technologies: CDI, JAX-RS  
Summary: The `helloworld-rs` quickstart demonstrates a simple Hello World application, bundled and deployed as a WAR, that uses *JAX-RS* to say Hello.    
Target Product: JBoss EAP  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

The `helloworld-rs` quickstart demonstrates the use of *CDI 1.0* and *JAX-RS* in Red Hat JBoss Enterprise Application Platform.


System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.1 or later.

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.


Use of EAP_HOME
---------------

In the following instructions, replace `EAP_HOME` with the actual path to your JBoss EAP 6 installation. The installation path is described in detail here: [Use of EAP_HOME and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_EAP_HOME.md#use-of-eap_home-and-jboss_home-variables).


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

4. This will deploy `target/jboss-helloworld-rs.war` to the running instance of the server.


Access the application 
---------------------

The application is deployed to <http://localhost:8080/jboss-helloworld-rs>.

The *XML* content can be viewed by accessing the following URL: <http://localhost:8080/jboss-helloworld-rs/rest/xml> 

The *JSON* content can be viewed by accessing this URL: <http://localhost:8080/jboss-helloworld-rs/rest/json>


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a JBoss EAP server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 


Debug the Application
------------------------------------

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

        mvn dependency:sources



Build and Deploy the Quickstart - to OpenShift
-------------------------

### Create an OpenShift Express Account and Domain

If you do not yet have an OpenShift account and domain, [Sign in to OpenShift](https://openshift.redhat.com/app/login) to create the account and domain. [Get Started with OpenShift](https://openshift.redhat.com/app/getting_started) will show you how to install the OpenShift Express command line interface.

### Create the OpenShift Application

_NOTE_: The domain name for this application will be `helloworldrs-YOUR_DOMAIN_NAME.rhcloud.com`. In these instructions, be sure to replace all instances of `YOUR_DOMAIN_NAME` with your own OpenShift account user name.

Open a shell command prompt and change to a directory of your choice. Enter the following command to create a JBoss EAP 6 application:

        rhc app create -a helloworldrs -t jbosseap-6

This command creates an OpenShift application named `helloworldrs` and will run the application inside the `jbosseap-6` container. You should see some output similar to the following:

    Application Options
    -------------------
      Namespace:  YOUR_DOMAIN_NAME
      Cartridges: jbosseap-6 (addtl. costs may apply)
      Gear Size:  default
      Scaling:    no

    Creating application 'helloworldrs' ... done

    Waiting for your DNS name to be available ... done

    Cloning into 'helloworldrs'...
    Warning: Permanently added the RSA host key for IP address '54.237.58.0' to the list of known hosts.

    Your application 'helloworldrs' is now available.

      URL:        http://helloworldrs-YOUR_DOMAIN_NAME.rhcloud.com/
      SSH to:     52864af85973ca430200006f@helloworldrs-YOUR_DOMAIN_NAME.rhcloud.com
      Git remote: ssh://52864af85973ca430200006f@helloworldrs-YOUR_DOMAIN_NAME.rhcloud.com/~/git/helloworldrs.git/
      Cloned to:  CURRENT_DIRECTORY/helloworldrs

    Run 'rhc show-app helloworldrs' for more details about your app.

The create command creates a git repository in the current directory with the same name as the application. Notice that the output also reports the URL at which the application can be accessed. Make sure it is available by typing the published url <http://helloworldrs-YOUR_DOMAIN_NAME.rhcloud.com/> into a browser or use command line tools such as curl or wget. Be sure to replace `YOUR_DOMAIN_NAME` with your OpenShift account domain name.

### Migrate the Quickstart Source

Now that you have confirmed it is working you can migrate the quickstart source. You do not need the generated default application, so navigate to the new git repository directory and tell git to remove the source and pom files:

        cd helloworldrs
        git rm -r src pom.xml

Copy the source for the `helloworld-rs` quickstart into this new git repository:

        cp -r QUICKSTART_HOME/helloworld-rs/src .
        cp QUICKSTART_HOME/helloworld-rs/pom.xml .

### Deploy the OpenShift Application

You can now deploy the changes to your OpenShift application using git as follows:

        git add src pom.xml
        git commit -m "helloworld-rs quickstart on OpenShift"
        git push

The final push command triggers the OpenShift infrastructure to build and deploy the changes. 

Note that the `openshift` profile in the `pom.xml` file is activated by OpenShift. This causes the WAR built by OpenShift to be copied to the `deployments/` directory and deployed without a context path.

### Test the OpenShift Application

When the push command returns you can test the application by getting the following URLs either via a browser or using tools such as curl or wget. Be sure to replace the `YOUR_DOMAIN_NAME` in the URL with your OpenShift account domain name.

* <http://helloworldrs-YOUR_DOMAIN_NAME.rhcloud.com/rest/xml> if you want *xml* or
* <http://helloworldrs-YOUR_DOMAIN_NAME.rhcloud.com/rest/json> if you want *json*

You can use the OpenShift command line tools or the OpenShift web console to discover and control the application.

### View the JBoss EAP Server Log on OpenShift

Now you can look at the output of the server by running the following command:

        rhc tail -a helloworldrs

This will show the tail of the JBoss EAP server log.

_Note:_ You may see the following error in the log:

        2014/03/17 07:50:36,231 ERROR [org.jboss.as.controller.management-operation] (management-handler-thread - 4) JBAS014613: Operation ("read-resource") failed - address: ([("subsystem" => "deployment-scanner")]) - failure description: "JBAS014807: Management resource '[(\"subsystem\" => \"deployment-scanner\")]' not found"

This is a benign error that occurs when the status of the deployment is checked too early in the process. This process is retried, so you can safely ignore this error.

### Delete the OpenShift Application

If you plan to test the `jax-rs-client` quickstart on OpenShift, you may want to wait to delete this application because it is also used by that quickstart for testing. When you are finished with the application you can delete if from OpenShift as follows:

        rhc app-delete -a helloworldrs

_Note_: There is a limit to the number of applications you can deploy concurrently to OpenShift. If the `rhc app create` command returns an error indicating you have reached that limit, you must delete an existing application before you continue. 

* To view the list of your OpenShift applications, type: `rhc domain show`
* To delete an application from OpenShift, type the following, substituting the application name you want to delete: `rhc app-delete -a APPLICATION_NAME_TO_DELETE`

