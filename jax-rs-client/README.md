jax-rs-client: External JAX-RS Client
======================
Author: Blaine Mincey  
Level: Intermediate  
Technologies: JAX-RS, CDI  
Summary: The `jax-rs-client` quickstart demonstrates an external JAX-RS RestEasy client, which interacts with a JAX-RS Web service that uses *CDI 1.0* and *JAX-RS*.  
Prerequisites: helloworld-rs  
Target Product: JBoss EAP  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

The `jax-rs-client` quickstart demonstrates an external JAX-RS RestEasy client which interacts with a JAX-RS Web service that uses *CDI 1.0* and *JAX-RS* 
in Red Hat JBoss Enterprise Application Platform.

This client "calls" the HelloWorld JAX-RS Web Service that was created in the [helloworld-rs](../helloworld-rs/README.md) quickstart. See the **Prerequisite** section below for details on how to build and deploy the [helloworld-rs](../helloworld-rs/README.md) quickstart.


System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.1 or later. 

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.


Prerequisites
-----------

IMPORTANT: This quickstart depends on the deployment of the 'helloworld-rs' quickstart for its test. Before running this quickstart, see the [helloworld-rs](../helloworld-rs/README.md)  README file for details on how to deploy it.

You can verify the deployment of the [helloworld-rs](../helloworld-rs/README.md) quickstart by accessing the following content:

* The *XML* content can be viewed by accessing the following URL: <http://localhost:8080/jboss-helloworld-rs/rest/xml> 
* The *JSON* content can be viewed by accessing this URL: <http://localhost:8080/jboss-helloworld-rs/rest/json>



Run the Arquillian Tests 
-------------------------

This quickstart provides Arquillian tests. 

_NOTE: The following commands assume you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Run the Arquillian Tests](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/RUN_ARQUILLIAN_TESTS.md#run-the-arquillian-tests) for complete instructions and additional options._

1. Make sure you have started the JBoss EAP server as described above.
2. Make sure the `helloworld-rs` quickstart has been deployed on the server as noted in the **Prerequisites** section above.
3. Open a command prompt and navigate to the root directory of this quickstart.
4. Type the following command to run the test goal with the following profile activated:

        mvn clean test 


Investigate the Console Output
----------------------------

This command will compile the example and execute a test to make two separate requests to the Web Service.  Towards the end of the Maven build output, you 
should see the following if the execution is successful:

        ===============================================
        URL: http://localhost:8080/jboss-helloworld-rs/rest/xml
        MediaType: application/xml

        *** Response from Server ***

        <xml><result>Hello World!</result></xml>
    
        ===============================================
        ===============================================
        URL: http://localhost:8080/jboss-helloworld-rs/rest/json
        MediaType: application/json

        *** Response from Server ***

        {"result":"Hello World!"}
        ===============================================


OpenShift
---------

To make this quickstart more interesting, deploy the RESTful service to OpenShift.  The following instructions will guide you as to the modifications that must be made to successfully execute the jax-rs-client against a service deployed to OpenShift.


Build and Deploy the Quickstart - to OpenShift
-------------------------

_IMPORTANT_: This quickstart depends on the deployment of the `helloworld-rs` quickstart to OpenShift for its test. Follow the instructions [Build and Deploy the Quickstart - to OpenShift](../helloworld-rs/README.md#build-and-deploy-the-quickstart-to-openshift) in the helloworld-rs README to deploy that application to OpenShift. Do NOT yet follow the step "Delete the OpenShift Application".

As it says in the `helloworld-rs` instructions, you can verify the deployment of the `helloworld-rs` quickstart by accessing the following content. In these instructions, be sure to replace all instances of `YOUR_DOMAIN_NAME` with your own OpenShift account user name.

* <http://helloworldrs-YOUR_DOMAIN_NAME.rhcloud.com/rest/xml> if you want *xml* or
* <http://helloworldrs-YOUR_DOMAIN_NAME.rhcloud.com/rest/json> if you want *json*


### Modify the jax-rs-client quickstart pom.xml

Now that you have deployed the application, it is time to make changes to the jax-rs-client quickstart Arquillian tests. 

1. Open a shell command prompt and navigate to the `QUICKSTART_HOME/jax-rs-client/` directory.
2. Make a backup copy of the `pom.xml` file.
3. Open the `pom.xml` file in an editor and modify the `xmlUrl` and `jsonUrl` property values as follows. Be sure to replace the `YOUR_DOMAIN_NAME` in the URL with your OpenShift domain name.

        <property>
            <name>xmlUrl</name>
            <value>http://helloworldrs-YOUR_DOMAIN_NAME.rhcloud.com/rest/xml</value>
        </property>
        <property>
            <name>jsonUrl</name>
            <value>http://helloworldrs-YOUR_DOMAIN_NAME.rhcloud.com/rest/json</value>
        </property>


### Run the Maven test

Type the following command to run the jax-rs-client:

        mvn test

This command will compile the example and execute a test to make two separate requests to the Web Service.  Towards the end of the Maven build output, you should see the following if the execution is successful:

        ===============================================
        URL: http://helloworldrs-YOUR_DOMAIN_NAME.rhcloud.com/rest/xml
        MediaType: application/xml

        *** Response from Server ***

        <xml><result>Hello World!</result></xml>

        ===============================================
        ===============================================
        URL: http://helloworldrs-YOUR_DOMAIN_NAME.rhcloud.com/rest/json
        MediaType: application/json

        *** Response from Server ***

        {"result":"Hello World!"}

        ===============================================

When you are finished testing, restore the `pom.xml` file to the previous version if you want to test locally.

### Delete the OpenShift Application

When you are finished with the application you can delete it from OpenShift as follows:

        rhc app-delete -a helloworldrs

_Note_: There is a limit to the number of applications you can deploy concurrently to OpenShift. If the `rhc app create` command returns an error indicating you have reached that limit, you must delete an existing application before you continue. 

* To view the list of your OpenShift applications, type: `rhc domain show`
* To delete an application from OpenShift, type the following, substituting the application name you want to delete: `rhc app-delete -a APPLICATION_NAME_TO_DELETE`

