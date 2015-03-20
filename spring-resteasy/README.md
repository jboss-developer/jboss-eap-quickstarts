spring-resteasy: Example Using Resteasy Spring Integration
==========================================================
Author: Weinan Li <l.weinan@gmail.com>, Paul Gier <pgier@redhat.com>  
Level: Beginner  
Technologies: Resteasy, Spring  
Summary: The `spring-resteasy` quickstart demonstrates how to package and deploy a web application that includes resteasy-spring integration.  
Target Product: JBoss EAP  
Product Versions: EAP 6.2, EAP 6.3  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

The `spring-resteasy` quickstart demonstrates how to package and deploy a web application, which includes resteasy-spring integration, in 
Red Hat JBoss Enterprise Application Platform.

Currently the resteasy-spring.jar is using Spring 3.0.3, as such this quickstart needs to run on some version of Spring 3.x.


System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.1 or later. 

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.

Start the JBoss EAP Server
----------------------

1. Open a command line and navigate to the root of the JBoss EAP directory.
2. The following shows the command line to start the server with the full profile:

        For Linux:   EAP_HOME/bin/standalone.sh
        For Windows: EAP_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include 
Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) 
for complete instructions and additional options._

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package jboss-as:deploy

4. This deploys the `target/jboss-spring-resteasy.war` to the running instance of the server.


Access the application 
---------------------

The application will be running at the following URL:  <http://localhost:8080/jboss-spring-resteasy/>. 

That will provide links to the following URLs that demonstrate various path and parameter configurations.

* [jboss-spring-resteasy/hello?name=yourname](http://localhost:8080/jboss-spring-resteasy/hello?name=yourname)
* [jboss-spring-resteasy/basic](http://localhost:8080/jboss-spring-resteasy/basic)
* [jboss-spring-resteasy/queryParam?param=query](http://localhost:8080/jboss-spring-resteasy/queryParam?param=query)
* [jboss-spring-resteasy/matrixParam;param=matrix](http://localhost:8080/jboss-spring-resteasy/matrixParam;param=matrix)
* [jboss-spring-resteasy/uriParam/789](http://localhost:8080/jboss-spring-resteasy/uriParam/789)

And the same set as above but using the 'locating' path.

* [jboss-spring-resteasy/locating/hello?name=yourname](http://localhost:8080/jboss-spring-resteasy/locating/hello?name=yourname)
* [jboss-spring-resteasy/locating/basic](http://localhost:8080/jboss-spring-resteasy/locating/basic)
* [jboss-spring-resteasy/locating/queryParam?param=query](http://localhost:8080/jboss-spring-resteasy/locating/queryParam?param=query)
* [jboss-spring-resteasy/locating/matrixParam;param=matrix](http://localhost:8080/jboss-spring-resteasy/locating/matrixParam;param=matrix)
* [jboss-spring-resteasy/locating/uriParam/789](http://localhost:8080/jboss-spring-resteasy/locating/uriParam/789)


Run the Tests
-------------

1. Make sure you have **started** the JBoss EAP server as described above and **deployed** the quickstart.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn install -Prest-test

4. You should see the following output:

        -------------------------------------------------------
         T E S T S
        -------------------------------------------------------
        Running org.jboss.as.quickstarts.resteasyspring.test.ResteasySpringTest
        Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.211 sec

        Results :

        Tests run: 2, Failures: 0, Errors: 0, Skipped: 0

        [INFO] ------------------------------------------------------------------------
        [INFO] BUILD SUCCESS
        [INFO] ------------------------------------------------------------------------



Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy
