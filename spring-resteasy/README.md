spring-resteasy: Example Using Resteasy Spring Integration
==========================================================
Author: Weinan Li <l.weinan@gmail.com>, Paul Gier <pgier@redhat.com>  
Level: Beginner  
Technologies: Resteasy, Spring  
Summary: The `spring-resteasy` quickstart demonstrates how to package and deploy a web application that includes resteasy-spring integration.  
Target Product: JBoss EAP  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

The `spring-resteasy` quickstart demonstrates how to package and deploy a web application, which includes resteasy-spring integration, in 
Red Hat JBoss Enterprise Application Platform.

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 7 or later. 

All you need to build this project is Java 8.0 (Java SDK 1.8) or later and Maven 3.1.1 or later. See [Configure Maven for JBoss EAP 7](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


Start the JBoss EAP Server
----------------------

1. Open a command line and navigate to the root of the JBoss EAP directory.
2. The following shows the command line to start the server with the full profile:

        For Linux:   EAP7_HOME/bin/standalone.sh
        For Windows: EAP7_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package wildfly:deploy

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

And the same set as above but using the `locating` path.

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

        mvn wildfly:undeploy
