QUICKSTART_NAME: Brief Description of the Quickstart (try to limit the description to 55 characters)
======================================================
Author: YOUR_NAME and optional CONTACT_INFO  
Level: [one of the following: Beginner, Intermediate, or Advanced]  
Technologies: (list technologies used here)  
Summary: (A brief description of the quickstart to appear in the table and in Google search SEO results. Try to limit the description to 155 characters )  
Prerequisites: (list any quickstarts that must be deployed prior to running this one)  
Target Product: (JBoss EAP, JBoss Mobile, JBoss Data Grid, etc)  _Official names are here: https://mojo.redhat.com/docs/DOC-962110_
Source: (The URL for the repository that is the source of record for this quickstart)  


_NOTE: This file is meant to serve as a template or guideline for your own quickstart README.md file:_

* _The first lines in the file after the quickstart name and description (Author:, Level:, etc.) are metadata tags used by the [JBoss Developer site](http://www.jboss.org/developer-materials/#!formats=jbossdeveloper_quickstart). Make sure you include 2 spaces at the end of each line so they also render correctly when rendered as HTML files._
* _Be sure to replace the `QUICKSTART_NAME` and `YOUR_NAME` variables in your `README` file with the appropriate values._
* _Contributor instructions are enclosed within comments `<!-- Contributor: -->`. These instructions are only meant to help you and you should NOT include them in your README file!_
* _Review the other quickstart `README` files if you need help with formatting or content._

What is it?
-----------

<!-- Contributor: This is where you provide an overview of what the quickstart demonstrates. Be sure to include the full product name on the first line. For example: -->

The `QUICKSTART_NAME` quickstart demonstrates ... in Red Hat JBoss Enterprise Application Platform.
 * What are the technologies demonstrated by the quickstart?
 * What does it do when you run it?

You should include any information that would help the user understand the quickstart.

If possible, give an overview, including any code they should look at to understand how it works..


System requirements
-------------------

<!-- Contributor: For example: -->

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 7 or later. 

All you need to build this project is Java 8.0 (Java SDK 1.8) or later, Maven 3.0 or later.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.


Use of EAP_HOME
---------------

In the following instructions, replace `EAP_HOME` with the actual path to your JBoss EAP installation. The installation path is described in detail here: [Use of EAP_HOME and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_EAP_HOME.md#use-of-eap_home-and-jboss_home-variables).


Configure Optional Components
-------------------------

<!-- Contributor: If your quickstart requires any additional components, decribe how to set them up here. If your quickstart requires a secured user, PostgreSQL, or Byteman, you can copy instructions you find in other quickstarts, or you can use the examples here: -->

 * This quickstart uses a secured management interface and requires that you create a management (or application) user to access the running application. Instructions to set up a Management (or Application) user can be found here: 

    * [Add a Management User](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CREATE_USERS.md#add-a-management-user)

    * [Add an Application User](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CREATE_USERS.md#add-an-application-user)

 * This quickstart requires the PostgreSQL database. Instructions to install an configure PostgreSQL can be found here: [Configure the PostgreSQL Database for Use with the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_POSTGRESQL.md#configure-the-postgresql-database-for-use-with-the-quickstarts)

 * This quickstart uses Byteman to help demonstrate crash recovery. Instructions to install and configure Byteman can be found here: [Configure Byteman for Use with the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_BYTEMAN.md#configure-byteman-for-use-with-the-quickstarts)


Start the JBoss EAP Server
-------------------------

<!-- Contributor: Does this quickstart require one or more running servers? If so, you must show how to start the server. If you start the server in one of the following 3 ways, you can simply copy the instructions in the README file located in the root folder of the quickstart directory: -->

 * Start the JBoss EAP Server

 * Start the JBoss EAP Server with the Full Profile

 * Start the JBoss EAP Server with Custom Options. You will need to provide the argument string to pass on the command line, for example: 

      `--server-config=../../docs/examples/configs/standalone-xts.xml`

<!-- Contributor: If the server is started in a different manner than above, give the specific instructions. -->


Build and Deploy the Quickstart
-------------------------

<!-- Contributor: If the quickstart is built and deployed using the standard Maven commands, copy the following: -->

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install wildfly:deploy
4. This will deploy `target/jboss-QUICKSTART_NAME.war` (or `target/jboss-QUICKSTART_NAME.ear`) to the running instance of the server.
 
<!-- Contributor: Be sure to replace the `QUICKSTART_NAME`. If this quickstart requires different or additional instructions, be sure to modify or add those instructions here. -->


Access the application
---------------------

<!-- Contributor: Add this section only if the quickstart has a UI component and provide the URL to access the running application. Be sure to make the URL a hyperlink as below, substituting the your quickstart name for the `QUICKSTART_NAME`. -->

        Access the running application in a browser at the following URL:  <http://localhost:8080/jboss-QUICKSTART_NAME>


<!--Contributor: Briefly describe what you will see when you access the application. For example: -->

        You will be presented with a simple form for adding key/value pairs and a checkbox to indicate whether the updates should be executed using an unmanaged component. 

            If the box is checked, the updates will be executed within a session bean method. 
            If the box is not checked, the transactions and JPA updates will run in a servlet instead of session beans. 

        To list all existing key/value pairs, leave the key input box empty. 
    
        To add or update the value of a key, enter a key and value input boxe and click the submit button to see the results.

<!-- Contributor: Add any information that will help them run and understand your quickstart. -->


Undeploy the Archive
--------------------

<!--Contributor: For example: -->

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


Run the Arquillian Tests (For quickstarts that contain Arquillian tests)
-------------------------

<!-- Contributor: For example: -->

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container. 

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn clean test -Parq-jbossas-remote 

<!-- Contributor: The quickstart README should show what to expect from the the tests. -->

* Copy and paste output from the JUnit tests to show what to expect in the console from the tests.

* Copy and paste log messages output by the application to show what to expect in the server log when running the tests.



Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------

<!-- Contributor: For example: -->

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a JBoss EAP server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 

Debug the Application
------------------------------------

<!--Contributor: For example: -->

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

    mvn dependency:sources
   



Build and Deploy the Quickstart - to OpenShift
-------------------------

<!-- Contributor: If the quickstart deploys to OpenShift, you can use the following template a starting point to describe the process. Be sure to replace the following variables as noted below: -->

* APPLICATION_NAME should be replaced with a variation of the quickstart name, for example: myquickstart
* QUICKSTART_NAME should be replaced with your quickstart name, for example:  my-quickstart

### Create an OpenShift Account and Domain

If you do not yet have an OpenShift account and domain, [Sign in to OpenShift](https://openshift.redhat.com/app/login) to create the account and domain. [Get Started with OpenShift](https://openshift.redhat.com/app/getting_started) will show you how to install the OpenShift Express command line interface.

### Create the OpenShift Application

_NOTE_: The domain name for this application will be `APPLICATION_NAME-YOUR_DOMAIN_NAME.rhcloud.com`. In these instructions, be sure to replace all instances of `YOUR_DOMAIN_NAME` with your own OpenShift account user name.

Open a shell command prompt and change to a directory of your choice. Enter the following command to create a JBoss EAP application:

        rhc app create -a APPLICATION_NAME -t jbosseap-6


This command creates an OpenShift application named APPLICATION_NAME and will run the application inside the `jbosseap-6`  container. You should see some output similar to the following:

    Application Options
    -------------------
      Namespace:  YOUR_DOMAIN_NAME
      Cartridges: jbosseap-6 (addtl. costs may apply)
      Gear Size:  default
      Scaling:    no

    Creating application 'APPLICATION_NAME' ... done

    Waiting for your DNS name to be available ... done

    Cloning into 'APPLICATION_NAME'...
    Warning: Permanently added the RSA host key for IP address '54.237.58.0' to the list of known hosts.

    Your application 'APPLICATION_NAME' is now available.

      URL:        http://APPLICATION_NAME-YOUR_DOMAIN_NAME.rhcloud.com/
      SSH to:     52864af85973ca430200006f@APPLICATION_NAME-YOUR_DOMAIN_NAME.rhcloud.com
      Git remote: ssh://52864af85973ca430200006f@APPLICATION_NAME-YOUR_DOMAIN_NAME.rhcloud.com/~/git/APPLICATION_NAME.git/
      Cloned to:  CURRENT_DIRECTORY/APPLICATION_NAME

    Run 'rhc show-app APPLICATION_NAME' for more details about your app.

The create command creates a git repository in the current directory with the same name as the application. Notice that the output also reports the URL at which the application can be accessed. Make sure it is available by typing the published url <http://APPLICATION_NAME-YOUR_DOMAIN_NAME.rhcloud.com/> into a browser or use command line tools such as curl or wget. Be sure to replace `YOUR_DOMAIN_NAME` with your OpenShift account domain name.

### Migrate the Quickstart Source

Now that you have confirmed it is working you can migrate the quickstart source. You do not need the generated default application, so navigate to the new git repository directory and tell git to remove the source and pom files:

        cd APPLICATION_NAME
        git rm -r src pom.xml

Copy the source for the QUICKSTART_NAME quickstart into this new git repository:

        cp -r QUICKSTART_HOME/QUICKSTART_NAME/src .
        cp QUICKSTART_HOME/QUICKSTART_NAME/pom.xml .

### Configure the OpenShift Server

<!-- Contributor: Here you describe any modifications needed for the `.openshift/config/standalone.xml` file. See other quickstart README.md files for examples. -->

### Deploy the OpenShift Application

You can now deploy the changes to your OpenShift application using git as follows:

        git add src pom.xml
        git commit -m "QUICKSTART_NAME quickstart on OpenShift"
        git push

The final push command triggers the OpenShift infrastructure to build and deploy the changes. 

Note that the `openshift` profile in `pom.xml` is activated by OpenShift, and causes the WAR build by openshift to be copied to the `deployments/` directory, and deployed without a context path.

### Test the OpenShift Application

When the push command returns you can test the application by getting the following URL either via a browser or using tools such as curl or wget. Be sure to replace the `YOUR_DOMAIN_NAME` in the URL with your OpenShift account domain name.

        http://APPLICATION_NAME-YOUR_DOMAIN_NAME.rhcloud.com 

You can use the OpenShift command line tools or the OpenShift web console to discover and control the application.

### View the JBoss EAP Server Log on OpenShift

Now you can look at the output of the server by running the following command:

        rhc tail -a APPLICATION_NAME

This will show the tail of the JBoss EAP server log.

_Note:_ You may see the following error in the log:

        2014/03/17 07:50:36,231 ERROR [org.jboss.as.controller.management-operation] (management-handler-thread - 4) JBAS014613: Operation ("read-resource") failed - address: ([("subsystem" => "deployment-scanner")]) - failure description: "JBAS014807: Management resource '[(\"subsystem\" => \"deployment-scanner\")]' not found"

This is a benign error that occurs when the status of the deployment is checked too early in the process. This process is retried, so you can safely ignore this error.

### Delete the OpenShift Application

When you are finished with the application you can delete it as follows:

        rhc app-delete -a APPLICATION_NAME

_Note_: There is a limit to the number of applications you can deploy concurrently to OpenShift. If the `rhc app create` command returns an error indicating you have reached that limit, you must delete an existing application before you continue. 

* To view the list of your OpenShift applications, type: `rhc domain show`
* To delete an application from OpenShift, type the following, substituting the application name you want to delete: `rhc app-delete -a APPLICATION_NAME_TO_DELETE`

