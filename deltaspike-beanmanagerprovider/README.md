deltaspike-beanmanagerprovider: Shows how to use DeltaSpike BeanManagerProvider to access CDI in a EntityListener
======================================================
Author: Rafael Benevides  
Level: Intermediate  
Technologies: CDI, Deltaspike, JPA, JSF  
Summary: Shows how to use DeltaSpike BeanManagerProvider to access CDI in a EntityListener  
Prerequisites:   
Target Product: WFK  
Product Versions: EAP 6.1, EAP 6.2, WFK 2.6  
Source: <https://github.com/jboss-developer/jboss-wfk-quickstarts/>  

What is it?
-----------

This project demonstrates the use of DeltaSpike's BeanManagerProvider.  

`BeanmanagerProvider` provides access to the `BeanManager` by registering the current `BeanManager` during the startup. This is really handy if you like to access CDI functionality from places where no CDI based injection is available. If a simple but manual bean-lookup is needed, it's easier to use the `BeanProvider` instead. 

This projects uses an EntityListener to create audit records. EntityListeners aren't managed by CDI, so you can't inject dependencies. In this project we used `BeanManagerProvider` to get a reference to a Stateless EJB that is responsible to persist the audit records.


System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform (EAP) 6.1 or later with the Red Hat JBoss Web Framework Kit (WFK) 2.5.

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#configure-maven) before testing the quickstarts.


Start the JBoss EAP Server
-------------------------

1. Open a command line and navigate to the root of the JBoss EAP directory.
2. The following shows the command line to start the server with the default profile:

        For Linux:   EAP_HOME/bin/standalone.sh
        For Windows: EAP_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package jboss-as:deploy
4. This will deploy `target/jboss-deltaspike-beanmanagerprovider.war` to the running instance of the server.
 
Access the application
---------------------

Access the running application in a browser at the following URL:  <http://localhost:8080/jboss-deltaspike-beanmanagerprovider>

You are presented with a simple form to insert, edit, or remove contacts.

- To insert a new contact, click the `New Contact` button. Complete the form fields and then click the `Save` button.
- To modify an existing contact, find the name in the *All Contacts* table and click the `Select for edit` button. The *Contacts* form fields are populated with the contact data. Modify the data and then click the `Save` button.
- To remove a contact, find the name in the *All Contacts* table and click the `Remove` button. A dialog appears asking 'Do you want to remove this record?'. Click the `OK` button to remove the contact.

When you insert a contact, notice that both the *All Contacts* and *Audit Records* tables are updated.
When you edit a contact, notice that the *Audit Records* table is updated with the new contact data.

Continue to insert, edit, or remove contacts to see registration of audit records.
        
_NOTE: To fire the update audit, you must change one of the contact fields_

        
Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy

Run the Arquillian Functional Tests
-----------------------------------

This quickstart provides Arquillian functional tests. They are located under the directory "functional-tests". Functional tests verify that your application behaves correctly from the user's point of view - simulating clicking around the page as a normal user would do.

To run these tests, you must build the main project as described above.

1. Open a command line and navigate to the root directory of this quickstart.
2. Build the quickstart WAR using the following command:

        mvn clean package

3. Navigate to the functional-tests/ directory in this quickstart.
4. If you have a running instance of the JBoss EAP server, as described above, run the remote tests by typing the following command:

        mvn clean verify -Parq-jbossas-remote

5. If you prefer to run the functional tests using managed instance of the JBoss EAP server, meaning the tests will start the server for you, type the following command:

_NOTE: For this to work, Arquillian needs to know the location of the JBoss EAP server. This can be declared through the `JBOSS_HOME` environment variable or the `jbossHome` property in `arquillian.xml`. See [Run the Arquillian Tests](../README.md#run-the-arquillian-tests) for complete instructions and additional options._

        mvn clean verify -Parq-jbossas-managed

Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------

You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 

Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
