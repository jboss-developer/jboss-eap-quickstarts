kitchensink-angularjs: Shows how to use AngularJS with JAX-RS and Java EE on JBoss
========================
Author: Pete Muir
Level: Intermediate
Technologies: AngularJS, CDI, JPA, EJB, JPA, JAX-RS, BV
Summary: An example that incorporates multiple technologies
Target Product: WFK
Product Versions: EAP 6.1, EAP 6.2, WFK 2.4
Source: <https://github.com/jboss-developer/jboss-wfk-quickstarts>

What is it?
-----------

This is your project! It is a sample, deployable Maven 3 project to help you get your foot in the door developing with AngularJS on Java EE 6 with Red Hat JBoss Enterprise Application Platform 6.1 or later. 

This project is setup to allow you to create a compliant Java EE 6 application using CDI 1.0, EJB 3.1, JPA 2.0 and Bean Validation 1.0. It includes a persistence unit and some sample persistence and transaction code to introduce you to database access in enterprise Java. 

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform (EAP) 6.1 or later with the  Red Hat JBoss Web Framework Kit (WFK) 2.4.

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#configure-maven) before testing the quickstarts.


Start the JBoss Server
-------------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat

 
Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package jboss-as:deploy

4. This will deploy `target/jboss-kitchensink-angularjs.war` to the running instance of the server.
 

Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-kitchensink-angularjs/>.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Run the Arquillian Tests 
-------------------------

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container. 

_NOTE: The following commands assume you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Run the Arquillian Tests](../README.md#run-the-arquillian-tests) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn clean test -Parq-jbossas-remote 


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

If you do not yet have an OpenShift account and domain, [Sign in to OpenShift](https://openshift.redhat.com/app/login) to create the account and domain. [Get Started with OpenShift](https://www.openshift.com/get-started) will show you how to install the OpenShift Express command line interface.
### Create the OpenShift Application

Note that we use the `jboss-as-quickstart@jboss.org` user for these examples. You need to substitute it with your own user name.

Open a shell command prompt and change to a directory of your choice. Enter the following command, replacing APPLICATION_TYPE with `jbosseap-6` for quickstarts running on JBoss Enterprise Application Platform 6.1 or later:

    rhc app create -a kitchensinkangularjs -t APPLICATION_TYPE

_NOTE_: The domain name for this application will be `kitchensinkangularjs-YOUR_DOMAIN_NAME.rhcloud.com`. Here we use the _quickstart_ domain. You will need to replace it with your own OpenShift domain name.

This command creates an OpenShift application called `kitchensinkangularjs` and will run the application inside the `jbosseap-6` container. You should see some output similar to the following:

    Creating application: kitchensinkangularjs
    Now your new domain name is being propagated worldwide (this might take a minute)...
    Warning: Permanently added the RSA host key for IP address '23.20.102.147' to the list of known hosts.
    Confirming application 'kitchensinkangularjs' is available:  Success!

    kitchensinkangularjs published:  http://kitchensinkangularjs-quickstart.rhcloud.com/
    git url:  ssh://76f095330e3f49af97a52e513a9c966b@kitchensinkangularjs-quickstart.rhcloud.com/~/git/kitchensinkangularjs.git/
    Successfully created application: kitchensinkangularjs

### Migrate the Quickstart Source

Now that you have confirmed it is working you can migrate the quickstart source. You do not need the generated default application, so navigate to the new git repository directory and tell git to remove the source and pom files:

        cd kitchensinkangularjs
        git rm -r src pom.xml

Copy the source for the kitchensink-angularjs quickstart into this new git repo:

        cp -r <quickstarts>/kitchensink-angularjs/src .
        cp <quickstarts>/kitchensink-angularjs/pom.xml .


### Deploy the OpenShift Application

You can now deploy the changes to your OpenShift application using git as follows:

        git add src pom.xml
        git commit -m "kitchensink-angularjs quickstart on OpenShift"
        git push

OpenShift will build the application using Maven, and deploy it to the JBoss server. If successful, you should see output similar to:

    remote: [INFO] ------------------------------------------------------------------------
    remote: [INFO] BUILD SUCCESS
    remote: [INFO] ------------------------------------------------------------------------
    remote: [INFO] Total time: 19.991s
    remote: [INFO] Finished at: Wed Mar 07 12:48:15 EST 2012
    remote: [INFO] Final Memory: 8M/168M
    remote: [INFO] ------------------------------------------------------------------------
    remote: Running .openshift/action_hooks/build
    remote: Emptying tmp dir: /var/lib/libra/1e63c17c2dd94a329f21555a33dc617d/kitchensinkangularjs/jbosseap-6/standalone/tmp/vfs
    remote: Emptying tmp dir: /var/lib/libra/1e63c17c2dd94a329f21555a33dc617d/kitchensinkangularjs/jbosseap-6/standalone/tmp/work
    remote: Running .openshift/action_hooks/deploy
    remote: Starting application...
    remote: Done
    remote: Running .openshift/action_hooks/post_deploy
    To ssh://1e63c17c2dd94a329f21555a33dc617d@kitchensinkangularjs-quickstart.rhcloud.com/~/git/kitchensinkangularjs.git/
       e6f80bd..63504b9  master -> master

Note that the `openshift` profile in the `pom.xml` file is activated by OpenShift. This causes the WAR built by OpenShift to be copied to the `deployments` directory and deployed without a context path.

### Test the OpenShift Application

Now you will start to tail the log files of the server. To do this run the following command, remembering to replace the application name and login id.

        rhc tail -a kitchensinkangularjs

Once the app is deployed, you can test the application by accessing the following URL either via a browser or using tools such as curl or wget. Be sure to replace the `quickstart` in the URL with your domain name.

    http://kitchensinkangularjs-quickstart.rhcloud.com/

You should now be able to interact with the application in a similar mannor as when you deployed it locally.

You can use the OpenShift command line tools or the OpenShift web console to discover and control the application.

### Destroy the OpenShift Application

When you are finished with the application you can destroy it as follows:

        rhc app destroy -a kitchensinkangularjs

To view the list of your current OpenShift applications, type:

        rhc domain

_Note_: There is a limit to the number of applications you can deploy concurrently to OpenShift. If the `rhc app create` command returns an error indicating you have reached that limit, you must destroy an existing application before you continue.

* To view the list of your OpenShift applications, type: `rhc domain show`
* To destroy an existing application, type the following, substituting the application name you want to destroy: `rhc app destroy -a APPLICATION_NAME_TO_DESTROY`
