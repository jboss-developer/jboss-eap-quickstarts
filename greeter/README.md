greeter: Demonstrates CDI, JPA, JTA, EJB, and JSF
========================
Author: Pete Muir  
Level: Beginner  
Technologies: CDI, JSF, JPA, EJB, JTA  
Summary: The `greeter` quickstart demonstrates the use of *CDI*, *JPA*, *JTA*, *EJB* and *JSF* in JBoss EAP.  
Target Product: JBoss EAP  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

The `greeter` quickstart demonstrates the use of *CDI*, *JPA*, *JTA*, *EJB* and *JSF* in Red Hat JBoss Enterprise Application Platform.

When you deploy this example, two users are automatically created for you:  `emuster` and `jdoe`. This data is located in the `src/main/resources/import.sql file`.

To test this example:

1. Enter a name in the `username` field and click on `Greet!`.
2. If you enter a username that is not in the database, you get a message `No such user exists!`.
3. If you enter a valid username, you get a message `Hello, ` followed by the user's first and last name.
4. To create a new user, click the `Add a new user` link. Enter the username, first name, and last name and then click `Add User`. The user is added and a message displays the new user id number.
5. Click on the `Greet a user!` link to return to the `Greet!` page.


_Note: This quickstart uses the H2 database included with Red Hat JBoss Enterprise Application Platform 7. It is a lightweight, relational example datasource that is used for examples only. It is not robust or scalable, is not supported, and should NOT be used in a production environment!_

_Note: This quickstart uses a `*-ds.xml` datasource configuration file for convenience and ease of database configuration. These files are deprecated in JBoss EAP and should not be used in a production environment. Instead, you should configure the datasource using the Management CLI or Management Console. Datasource configuration is documented in the [Configuration Guide](https://access.redhat.com/documentation/en/red-hat-jboss-enterprise-application-platform/) for Red Hat JBoss Enterprise Application Platform._


System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 7 or later.

All you need to build this project is Java 8.0 (Java SDK 1.8) or later and Maven 3.1.1 or later. See [Configure Maven for JBoss EAP 7](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


Use of EAP7_HOME
---------------

In the following instructions, replace `EAP7_HOME` with the actual path to your JBoss EAP installation. The installation path is described in detail here: [Use of EAP7_HOME and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_EAP7_HOME.md#use-of-eap_home-and-jboss_home-variables).


Start the JBoss EAP Server
-------------------------

1. Open a command prompt and navigate to the root of the JBoss EAP directory.
2. The following shows the command line to start the server:

        For Linux:   EAP7_HOME/bin/standalone.sh
        For Windows: EAP7_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install wildfly:deploy

4. This will deploy `target/jboss-greeter.war` to the running instance of the server.


Access the application
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-greeter>.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


Server Log: Expected warnings and errors
-----------------------------------

_Note:_ You will see the following warnings in the server log. You can ignore these warnings.

    WFLYJCA0091: -ds.xml file deployments are deprecated. Support may be removed in a future version.

    HHH000431: Unable to determine H2 database version, certain features may not work


Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a JBoss EAP server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts).


Debug the Application
------------------------------------

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

        mvn dependency:sources

Run the application in Openshift
---
[EAP 7 xPaaS image documentation](https://access.redhat.com/documentation/en/red-hat-xpaas/version-0/red-hat-xpaas-eap-image/)

Download and install the [Container Development Kit](http://developers.redhat.com/products/cdk/download/) via this [installation guide](https://access.redhat.com/documentation/en/red-hat-container-development-kit/2.1/paged/installation-guide/).

After setting up the Openshift environment download the [CLI client tool](https://github.com/openshift/origin/releases/latest) and add it to your PATH envar (e.g copy it to /usr/local/bin). Log in as `openshift-dev` user: ```oc login 10.1.2.2:8443 -u openshift-dev -p devel``` and go to the following project: ```oc project sample-project```. From here install the EAP 7 xPaaS image and eap70-postgresql-s2i template.

#### Install EAP 7 xPaaS image

```
cat <<EOF | oc create -n sample-project -f -
---
  apiVersion: v1
  kind: ImageStream
  metadata:
    name: jboss-eap70-openshift
  spec:
    dockerImageRepository: registry.access.redhat.com/jboss-eap-7/eap70-openshift
EOF
```

#### Install PostgreSQL image
```
cat <<EOF | oc create -n sample-project -f -
---
  apiVersion: v1
  kind: ImageStream
  metadata:
    name: postgresql
  spec:
    dockerImageRepository: registry.access.redhat.com/openshift3/postgresql-92-rhel7
EOF
```

#### Install eap70-postgresql-s2i template
```
oc create -n sample-project -f https://raw.githubusercontent.com/jboss-openshift/application-templates/master/secrets/eap7-app-secret.json
oc create  -f https://raw.githubusercontent.com/jboss-openshift/application-templates/master/eap/eap70-postgresql-s2i.json
```

#### Build and run the application in Openshift
```
oc process -v \
SOURCE_REPOSITORY_URL=https://github.com/josefkarasek/jboss-eap-quickstarts,\
SOURCE_REPOSITORY_REF=7.1.x-develop,\
CONTEXT_DIR=greeter,\
DB_JNDI=java:jboss/datasources/GreeterQuickstartDS,\
DB_DATABASE=USERS,\
HTTPS_NAME=jboss,\
HTTPS_PASSWORD=mykeystorepass,\
JGROUPS_ENCRYPT_NAME=secret-key,\
JGROUPS_ENCRYPT_PASSWORD=password,\
IMAGE_STREAM_NAMESPACE=sample-project eap70-postgresql-s2i | oc create -f -
```

Wait for the build to finish:
```
watch oc get pod
```
View build logs
```
oc logs <pod_name-build>  # most likely 'eap-app-1-build'
```
After the build completes visit the project in web console at `https://10.1.2.2:8443/console`. Because we used self-signed certificate there will be two routes for this application available - http and https. Click at either one of them (in case of https, accept the self-signed certificate as trusted in your browser) and the application will display.
