jsonp: JSON-P Object-based JSON generation and Stream-based JSON consuming
======================================================
Author: Rafael Benevides  
Level: Beginner  
Technologies: CDI, JSF, JSON-P  
Summary: This 
Prerequisites: This Quickstarts creates a JSON string through Object-based JSON generation and them parses it using Stream-based JSON consuming.  
Target Product: EAP
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts>  


What is it?
-----------

The `jsonp` quickstarts creates a JSON string through Object-based JSON generation and them parses it using Stream-based JSON consuming.

It shows how the JSON-P API can be used to generate JSON files and also parses it. 


System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 7 or later. 

All you need to build this project is Java 8.0 (Java SDK 1.8) or later, Maven 3.x or later.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.


Start the JBoss EAP Server
-------------------------

1. Open a command line and navigate to the root of the  JBoss EAP directory.
2. The following shows the command line to start the server with the default profile:

        For Linux:   EAP_HOME/bin/standalone.sh
        For Windows: EAP_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------


1. Make sure you have started the JBoss EAP server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn package wildfly:deploy
4. This will deploy `target/jboss-jsonp` to the running instance of the server.
 

Access the application
---------------------

Access the running application in a browser at the following URL:  <http://localhost:8080/jboss-jsonp/>

You're presented with a simple form that shows a pre filled Personal data. You can change those values if you want. 

Click on `Generate JSON String from Personal Data` button. The textarea bellow the button will present a JSON string representing the data and values from the previous form.

Note that the JSON string contains String, number, boolean and array values.

Now, Click on `Parse JSON String using Stream` button. The textarea bellow the button will show the events generated from the parsed json string.


Undeploy the Archive
--------------------

<!--Contributor: For example: -->

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------


You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 


Debug the Application
------------------------------------

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

    mvn dependency:sources
   

<!-- Build and Deploy the Quickstart to OpenShift - Coming soon! -->

