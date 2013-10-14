cdi-decorator: Demostrates CDI Decorator
======================================================
Author: Ievgen Shulga
Level: Intermediate
Technologies: CDI
Summary: Demonstrates the use of CDI Decorator where the bean is can be decorated.
Target Product: EAP
Product Versions: EAP 6.1, EAP 6.2
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>

What is it?
-----------
This example demonstrates the use of CDI Decorator.
It represents a common decorator design pattern. We take a class and we wrap decorator class around it. 
When we call the class, we always pass through the surrounding decorator class before we reach the inner class. 
In this example, the decorator class simply changes the staff bonus from '100' to '200' and the staff position from 'Java Developer' to 'Team Lead'. It then logs a message to the server console.
By default, all decorators are disabled, so application will run without using decorator. We need to enable our decorator in the 'beans.xml' descriptor to make it work.


System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.1 or later. 

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#configure-maven) before testing the quickstarts.


Start the JBoss Server
-------------------------

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
        
4. This will deploy `target/jboss-cdi-decorator.war` to the running instance of the server.


Access the application
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-cdi-decorator>.

You can specify decorator of the bean in the WEB-INF/beans.xml file by doing one of the following:

1. You can add a decorators tag and specify a decorator class.
2. You can specify a different decorator class name in the decorators tag.

For this example, uncomment the <decorators> tag in the WEB-INF/beans.xml file and redeploy the application. 
When you access the application, you will see changed information from web-browser and following in the server log: `CDI decorator method was called!`

In order to switch back to the default implementation, 
comment the 'decorators' block in the WEB-INF/beans.xml file and redeploy the quickstart.

Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 

Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc

