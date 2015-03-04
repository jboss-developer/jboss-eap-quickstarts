shopping-cart: EJB 3.1 Stateful Session Bean (SFSB) Example 
=====================================
Author: Serge Pagop  
Level: Intermediate  
Technologies: SFSB EJB  
Summary: The `shopping-cart` quickstart demonstrates how to deploy and run a simple Java EE 6 shopping cart application that uses a stateful session bean (SFSB).   
Target Product: JBoss EAP  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

The `shopping-cart` quickstart demonstrates how to deploy and run a simple Java EE 6 application that uses a stateful session bean (SFSB) in Red Hat JBoss Enterprise Application Platform. The application allows customers to buy, checkout, and view their cart contents. 

The `shopping-cart` application consists of the following:

1. A server side component:

    This standalone Java EE module is a JAR containing EJBs. It is responsible for managing the shopping cart.
2. A Java client:

    This simple Java client is launched using a "main" method. The remote client looks up a reference to the server module's API, via JNDI. It then uses this API to perform the operations the customer requests.


System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.1 or later. 

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.


Use of EAP_HOME
---------------

In the following instructions, replace `EAP_HOME` with the actual path to your JBoss EAP 6 installation. The installation path is described in detail here: [Use of EAP_HOME and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_EAP_HOME.md#use-of-eap_home-and-jboss_home-variables).


Start the JBoss EAP Server
-------------------------

1. Open a command prompt and navigate to the root of the JBoss EAP directory.
2. The following shows the command line to start the server:

        For Linux:   EAP_HOME/bin/standalone.sh
        For Windows: EAP_HOME\bin\standalone.bat

 
Build and Deploy the Quickstart
-------------------------

1. Make sure you have started the JBoss EAP server. See the instructions in the previous section.

2. Open a command prompt and navigate to the `shopping-cart` quickstart directory
3. To build both the server component and the remote client program, deploy the server module, change into the examples shopping-cart directory and type the following:

        mvn clean install jboss-as:deploy 
4. This Maven goal will deploy `server/target/jboss-shopping-cart-server.jar`. You can check the Application Server console to see information messages regarding the deployment.


Run the Client Application
------------------------

Now start a client that will access the beans you just deployed. 

You can use the command prompt from the previous step or open a new one and navigate to the root of the `shopping-cart` quickstart directory.

Type the following command:

        mvn exec:java -f client/pom.xml 

Investigate the Console Output
-------------------------------

You should see the following: 

1. The client sends a remote method invocation to the stateful session bean to buy two "Red Hat JBoss Enterprise Application Platform 6" subscriptions and one "JBoss SOA Platform 6" subscription.
2. The client sends a remote method invocation to get the contents of the cart and prints it to the console.
3. The client sends a remote method invocation to invoke checkout. Note the `checkout()` method in the server `ShoppingCartBean` has the `@Remove` annotation. This means the container will destroy shopping cart after the call and it will no longer be available. 
4. The client calls `getCartContents()` to make sure the shopping cart was removed after checkout. This results in a `javax.ejb.NoSuchEJBException` trace in the server, proving the cart was removed.

On the client console, you should see output similar to:

    &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    Obtained the remote interface to the shopping cart
    Buying a "Red Hat JBoss Enterprise Application Platform 6"
    Buying another "Red Hat JBoss Enterprise Application Platform 6"
    Buying a "JBoss SOA Platform 6"
    
    Print cart:
    1     JBoss SOA Platform 6
    2     Red Hat JBoss Enterprise Application Platform 6
    
    Checkout
    Cart was correctly removed, as expected, after Checkout
    &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&


On the server console, you should see output similar to (remember the server messages might change for different versions):

    INFO  [org.jboss.as.ejb3.deployment.processors.EjbJndiBindingsDeploymentUnitProcessor] (MSC service thread 1-2) JNDI bindings for session bean named ShoppingCartBean in deployment unit deployment "jboss-shopping-cart-server.jar" are as follows:

    	java:global/jboss-shopping-cart-server/ShoppingCartBean!org.jboss.as.quickstarts.sfsb.ShoppingCart
    	java:app/jboss-shopping-cart-server/ShoppingCartBean!org.jboss.as.quickstarts.sfsb.ShoppingCart
    	java:module/ShoppingCartBean!org.jboss.as.quickstarts.sfsb.ShoppingCart
    	java:global/jboss-shopping-cart-server/ShoppingCartBean
    	java:app/jboss-shopping-cart-server/ShoppingCartBean
    	java:module/ShoppingCartBean

    INFO  [org.jboss.as.server] (management-handler-threads - 2) JBAS018559: Deployed "jboss-shopping-cart-server.jar"
    INFO  [stdout] (pool-9-thread-8) implementing checkout() left as exercise for the reader!
    
_Note_: You also see the following `EJB Invocation failed` and `NoSuchEJBException` messages in the server log. This is the expected result because method is annotated with `@Remove`. This means the next invocation after the shopping cart checkout fails because the container has destroyed the instance and it is no longer available.
    
    ERROR [org.jboss.as.ejb3.invocation] (EJB default - 5) JBAS014134: EJB Invocation failed on component ShoppingCartBean for method public abstract java.util.HashMap org.jboss.as.quickstarts.sfsb.ShoppingCart.getCartContents(): javax.ejb.NoSuchEJBException: JBAS014300: Could not find EJB with id {...]}


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a JBoss EAP server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 

This quickstart consists of multiple projects, so it deploys and runs differently in JBoss Developer Studio than the other quickstarts.

* To deploy the server project, right-click on the `jboss-shopping-cart-server` project and choose `Run As` --> `Run on Server`.
* To run the client, right-click on the `jboss-shopping-cart-client` project and choose `Run As` --> `Java Application`. In the `Select Java Application` window, choose `Client - org.jboss.as.quickstarts.client` and click `OK`. The client output displays in the `Console` window.

Debug the Application
---------------------

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

        mvn dependency:sources

