forge-from-scratch: Shows How Forge Can Generate an Application
======================================================================================
Author: Lincoln Baxter, Matej Briskar  
Level: Intermediate  
Technologies: Forge  
Summary: The `forge-from-scratch` quickstart demonstrates how *JBoss Forge 2* can generate a Java EE (JPA, EJB 3.1, JAX-RS, JSF) web-enabled database application.   
Target Product: JBoss EAP  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

The `forge-from-scratch` quickstart demonstrates how to create a fully **Java EE compliant** project using **JBoss Forge 2** and **Red Hat JBoss Developer Studio 8** or later and deploy it to **Red Hat JBoss Enterprise Application Platform 6.4** or later. 

Once generated, the sample project will be a standard Maven 3, Java Web project with **JPA 2.0, EJB 3.1, CDI 1.0, JSF 2.1** with complete **JAX-RS** endpoints for all data Entities. It will also provide views to Create, Read, Update, and Delete records.

But that is not all! You can use Forge on your new or existing projects to continue to enhance any application.

_Note: This quickstart uses the H2 database included with Red Hat JBoss Enterprise Application Platform 6. It is a lightweight, relational example datasource that is used for examples only. It is not robust or scalable, is not supported, and should NOT be used in a production environment!_

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.4 or later.

All you need to build this project is Java 7.0 (Java SDK 1.7) or later and Red Hat JBoss Developer Studio 8 or greater.


Configure Maven for JBoss Developer Studio
------------------------------------------

If you have not yet done so, you must [Configure Maven - For Use with JBoss Developer Studio](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-for-use-with-jboss-developer-studio) before testing the quickstarts.


Run the Quickstart in Red Hat JBoss Developer Studio
--------------------------------------------

Forge is available in JBoss Developer Studio. To run this example, you must use JBoss Developer Studio 8 or greater.

### Generate and Build the Application

1. Start JBoss Developer Studio.
2. Open the `Forge Console` Window. To open it, navigate to menu item _Window -> Show View -> Other_. Locate _Forge -> Forge Console_ and click _OK_. 
3. Click the _Start_ button (green triangle) in top right corner of the Forge Console to start the default Forge runtime.
4. In the Forge Console Window, navigate to the root directory of this quickstart.

        $ cd QUICKSTART_HOME/forge-from-scratch/
5. Notice there is a file in this directory named `generate.fsh`. Run this file from the Forge console using the `run` command:

        $ run generate.fsh

6. At this point, Forge creates the new project and builds it. 
    * The script issues this command: `$ project-new --named forge-example --topLevelPackage org.example;`
    * You next see the console message: `***SUCCESS*** Project named 'forge-example' has been created.`
    * This is followed by a dialog saying `User Operation is waiting for "Importing Forge project" to complete.`.
    * After a number of `***SUCCESS***` messages, you see `[INFO] BUILD SUCCESS` near the end of the console output.


### What Did This Create?

This quickstart created a native Java EE 6 application. 

* After the command completes, look in your `QUICKSTART_HOME/forge-from-scratch/` folder. You see a folder with the name `forge-example`. 
* This project also appears in the `Project Explorer` view in JBoss Developer Studio. 
* Browse through this project to see the code that was generated as a result of this command. 

### Deploy the Generated Application

1. If you have not yet done so, add the JBoss EAP 6.4 runtime server to Red Hat JBoss Developer Studio. For general information about how to import a quickstart, add a JBoss EAP server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md).
2. Right-click on the project name and choose `Run As` --> `Run on Server`. If you have more than one server, choose the JBoss EAP 6.4 Runtime server.
3. Upon successful deployment, a **Welcome to Forge Window** opens with the application running at the following URL: <http://localhost:8080/forge-example/>
    
### Server Log: Expected warnings and errors

_Note:_ You will see the following warnings in the server log. You can ignore these warnings.

    HHH000059: Defining hibernate.transaction.flush_before_completion=true ignored in HEM
    HHH000431: Unable to determine H2 database version, certain features may not work

           
### Access the Running Application

The application appears in a 'Welcome to Forge' Window and displays the following:

    Welcome to Forge
        
    Your application is running. 

The following entities are displayed on the lower left side of the page:

* Address
* Customer
* Item
* Product Order
* Profile
* Zip Code

When you click on an entity, you are provided with a form that allows you to:

* Search for an existing entity
* Create a new entity
* Edit or delete an existing entity

The running application also provides links to find more information about the Forge. 
 

Undeploy the Application
------------------------

When you are ready to undeploy the application from JBoss EAP:

   
1. Go to the Red Hat JBoss Developer Studio `Servers` window.
2. Expand the JBoss EAP 6.4 Server to see the list of deployed applications.
3. Choose the `forge-example` project created by this quickstart, right-click, and choose `Remove`. 
4. Click `OK` when asked if you are sure you want to remove resource from the server. You should see the following message:

        INFO  [org.jboss.as.server] (DeploymentScanner-threads - 1) JBAS018558: Undeployed "forge-example.war" (runtime-name: "forge-example.war")


 
Next Steps
-------

Open `generate.fsh` and take a look inside! There is not much magic happening here. All of the commands used to generate this project are clearly listed just as if they were typed by your own hands.

Play around with creating more entities, relationships, UI, and generating JAX-RS endpoints,all with just a few simple commands.



