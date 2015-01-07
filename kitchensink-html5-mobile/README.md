kitchensink-html5-mobile: More Complex Example of HTML5, Mobile and JAX-RS 
=========================================================================================================
Author: Jay Balunas  
Level: Beginner   
Technologies: CDI, HTML5, REST  
Summary: The `kitchensink-html5-mobile` quickstart is based on `kitchensink`, but uses HTML5 and jQuery Mobile, making it suitable for mobile and tablet computers.  
Target Product: WFK  
Product Versions: EAP 6.1, EAP 6.2, EAP 6.3, WFK 2.7  
Source: <https://github.com/jboss-developer/jboss-wfk-quickstarts>  

What is it?
-----------

The `kitchensink-html5-mobile` quickstart is based on the `kitchensink` quickstart and demonstrates a Java EE 6 mobile database application using HTML5, jQuery Mobile, JAX-RS, JPA 2.0, and REST.

This application is built using a HTML5 + REST approach.  This uses a pure HTML client that interacts with the application server via restful end-points (JAX-RS).  This application also uses some of the latest HTML5 features and advanced JAX-RS. And since testing is just as important with client side as it is server side, this application uses QUnit to show you how to unit test your JavaScript.

What is a modern web application without mobile web support? This application also integrates jQuery mobile and basic client side device detection to give you both a desktop and mobile  version of the interface. Both support the same features, including form validation, member registration, etc. However the mobile version adds in mobile layout, touch, and performance  improvements needed to get you started with mobile web development on JBoss.

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform (EAP) 6.1 or later with the  Red Hat JBoss Web Framework Kit (WFK) 2.7.

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.

An HTML5 compatible browser such as Chrome, Safari 5+, Firefox 5+, or IE 9+ are required. and note that some behaviors will vary slightly (ex. validations) based on browser support, especially IE 9.

Mobile web support is limited to Android and iOS devices.  It should run on HP, and Black Berry devices as well.  Windows Phone, and others will be supported as  jQuery Mobile announces support.
 
With the prerequisites out of the way, you're ready to build and deploy.


Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#configure-maven) before testing the quickstarts.


Start the JBoss EAP Server
-----------------------

1. Open a command line and navigate to the root of the JBoss EAP directory.
2. The following shows the command line to start the server with the default profile:

        For Linux:   EAP_HOME/bin/standalone.sh
        For Windows: EAP_HOME\bin\standalone.bat

   Note: Adding "-b 0.0.0.0" to the above commands will allow external clients (phones, tablets, desktops, etc...) connect through your local network.

   For example

        For Linux:   EAP_HOME/bin/standalone.sh -b 0.0.0.0
        For Windows: EAP_HOME\bin\standalone.bat -b 0.0.0.0


Build and Deploy the Quickstart
-------------------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean jboss-as:deploy

4. This deploys `target/jboss-kitchensink-html5-mobile.war` to the running instance of the server.


Access the application
----------------------

Access the running client application in a browser at the following URL: <http://localhost:8080/jboss-kitchensink-html5-mobile/>.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------

You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 


Build and Deploy the Quickstart - to OpenShift
-------------------------------------

You can also deploy the application directly to OpenShift, Red Hat's cloud based PaaS offering, follow the instructions [here](https://community.jboss.org/wiki/DeployingHTML5ApplicationsToOpenshift)


Minification
-----------------

By default, the project uses the [wro4j](http://code.google.com/p/wro4j/) plugin, which provides the ability to concatenate, validate and minify JavaScript and CSS files. These minified files, as well as their unmodified versions are deployed with the project.

With just a few quick changes to the project, you can link to the minified versions of your JavaScript and CSS files.

First, in the <project-root>/src/main/webapp/index.html file, search for references to minification and comment or uncomment the appropriate lines.

Finally, wro4j runs in the compile phase so any standard build command like package, install, etc. will trigger it. The plugin is in a profile with an id of "minify" so you will want to specify that profile in your maven build.

NOTE: By default there are turn off tests so you must use the arquillian test profile to run tests when minifying.
For example:

    #No Tests
    mvn clean jboss-as:deploy -Pminify

OR

    #With Tests
    mvn clean jboss-as:deploy -Pminify,arq-jbossas-remote
 
Run the Arquillian tests
-------------------------------------

By default, tests are configured to be skipped. The reason is that the sample test is an Arquillian test, which requires the use of a container. You can activate this test by selecting one of the container configuration provided  for JBoss.

To run the test in JBoss, first start the container instance. Then, run the test goal with the following profile activated:

    mvn clean test -Parq-jbossas-remote

Run the QUnit tests
-------------------------------------

QUnit is a JavaScript unit testing framework used and built by jQuery. Because JavaScript code is the core of an HTML5 application, this quickstart provides a set of QUnit tests that automate testing of this code in various browsers. 

Executing QUnit test cases is quite easy. Simply load the following HTML file in the browser you want to test.

        QUICKSTART_HOME/kitchensink-html5-mobile/src/test/qunit/index.html

You can also display the QUnit tests using the Eclipse built-in browser.

For more information on QUnit tests see <http://docs.jquery.com/QUnit>


Import the Project into an IDE
-------------------------------------

If you created the project using the Maven archetype wizard in your IDE (Eclipse, NetBeans or IntelliJ IDEA), then there is nothing to do. You should already have an IDE project.

If you created the project from the command line using archetype:generate, then you need to import the project into your IDE. If you are using NetBeans 6.8 or IntelliJ IDEA 9, then all you have to do is open the project as an existing project. Both of these IDEs recognize Maven projects natively.

Debug the Application
-------------------------------------

If you want to be able to debug into the source code or look at the Javadocs of any library in the project, you can run either of the following two commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
