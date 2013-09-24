hibernate-search: Demonstrates doing a full text search of your entities
======================================================
Author: Tharindu Jayasuriya
Level: Intermediate
Technologies: hibernate-search
Summary: Hibernate Search brings the power of full text search engines to the persisted domain model by combining Hibernate Core with the capabilities of the Apache Lucene™ search engine. Lucene was just designed to index documents such as HTML and not object models. Hibernate Search allows Lucene to index objects too. It indexes your domain model with the help of a few annotations, takes care of database/index synchronization and brings back regular managed objects from free text queries. This quickstart demonstrates how the full text search can be done on your entities.
Prerequisites: None
Target Product: EAP & JDG
Source: https://github.com/jboss-jdf/jboss-as-quickstart/


What is it?
-----------

This is sample demo project which demonstrates a full text search of the entities. Here I will be using a Hibernate, AngularJS, Servlets, PostgreSQL, Maven and store the indexes in Infinispan. The AngularJS-based browser front-end will communicate with the Servlet-based back-end via RESTful web services. The functionality for above use cases will be exposed using a JAX-RS annotations on the Servlet. From  the Servlet data is served from PostgreSQL database or search results from the indexed data.

This demo application does a full text search on the RSS news feed data. RSS News has attributes “title”, “description”, “author” and “published date”. Around these attributes and some of the other attributes available in the field I have build the full text search demonstration. The demonstration will support below use cases.

	Use case 1:  Submit feed

		Admin users can add/edit/delete the RSS feed URL to the system.

	Use case 2:  Add news to database

		This will be done by a period polling of RSS feeds from the feed providers and identifying the content attributes of the feed and saving them. 

	Use case 3: Search the news

		This is where the full text search is going to happen

	Use case 4: View the news 
	
		View the news by clicking selected news from Use case 3

System requirements
-------------------

Java 6 JDK or better, Maven 3.0 and PostgreSQL or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#configure-maven) before testing the quickstarts.


Configure Optional Components
-------------------------

 * This quickstart uses a secured management interface and requires that you create a management (or application) user to access the running application. Instructions to set up a Management (or Application) user can be found here: 

    * [Add a Management User](../README.md#add-a-management-user)

    * [Add an Application User](../README.md#add-an-application-user)

 * This quickstart requires the PostgreSQL database. Instructions to install an configure PostgreSQL can be found here: [Install and Configure the PostgreSQL Database](../README.md#install-and-configure-the-postgresql-database)


Start JBoss Enterprise Application Platform 6 or JBoss AS 7
-------------------------

 * Start JBoss Enterprise Application Platform 6 or JBoss AS 7 with the Web Profile

 * Start JBoss Enterprise Application Platform 6 or JBoss AS 7 with the Full Profile

 * Start JBoss Enterprise Application Platform 6 or JBoss AS 7 with Custom Options. You will need to provide the argument string to pass on the command line, for example: 

      `--server-config=../../docs/examples/configs/standalone-xts.xml`

If the server is started in a different manner than above, give the specific instructions.


Build and Deploy the Quickstart
-------------------------

Following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

Standalone  mode configuration

	1. Make sure you have started the JBoss Server in `standalone` mode as described above. 
	2. Create cache-container called `jboss-as-hibernate-search` in `standalone.xml` 
		<cache-container name="jboss-as-hibernate-search" aliases="standard-local-cache" 
 			default-cache="jboss-as-hibernate-search" 
			jndi-name="java:jboss/infinispan/container/jboss-as-hibernate-search" 
			module="org.jboss.as.clustering.web.infinispan">
                	<local-cache name="jboss-as-hibernate-search" batching="true">
                    		<file-store passivation="false" purge="false"/>
                	</local-cache>
            </cache-container>
	3. Open a command line and navigate to the root directory of this quickstart.
	4. Type this command to build and deploy the archive:
	
	        mvn clean package jboss-as:deploy
	5. This will deploy `target/jboss-as-hibernate-search.war` to the running instance of the server.


Domain  mode configuration

	1. Make sure you have started the JBoss Server in `domain` mode as described above. 
	2. Create system-property called `DO_NOT_SCHEDULE` in `domain` mode in other instance other than master server
	3. Create cache-container called `jboss-as-hibernate-search` in `domain.xml` in the profile where the application is deployed
		<cache-container name="jboss-as-hibernate-search" aliases="standard-domain-cache" 
			default-cache="jboss-as-hibernate-search" 
			jndi-name="java:jboss/infinispan/container/jboss-as-hibernate-search" 
			module="org.jboss.as.clustering.web.infinispan">
                    <distributed-cache name="jboss-as-hibernate-search" mode="SYNC"/>
                </cache-container>

	4. Open a command line and navigate to the root directory of this quickstart.
	5. Type this command to build and deploy the archive:
	
	        mvn clean package jboss-as:deploy -Pdefault-cluster
	6. This will deploy `target/jboss-as-hibernate-search.war` to the running domain instance of the server.

Additional configuration required to store the indexes in remote cache in Infinispan Server and Domain mode in Jboss

	1. Make sure you have started the JBoss Server in `domain` mode as described above. 
	2. Create system-property called `DO_NOT_SCHEDULE` in `domain` mode in other instance other than master server
	3. Create cache-container called `jboss-as-hibernate-search` in `domain.xml` in the profile where the application is deployed and add below socket binding also
		<cache-container name="jboss-as-hibernate-search" aliases="jboss-as-hibernate-search" 
			default-cache="jboss-as-hibernate-search" 
			jndi-name="java:jboss/infinispan/container/jboss-as-hibernate-search" 
			module="org.jboss.as.clustering.web.infinispan">
                    <transport lock-timeout="60000"/>
                    <invalidation-cache name="clustered" mode="ASYNC" batching="true">
                        <remote-store shared="true" passivation="false" purge="false">
                            <remote-server outbound-socket-binding="remote-store-hotrod-server"/>
                        </remote-store>
                    </invalidation-cache>
                </cache-container>

		<outbound-socket-binding name="remote-store-hotrod-server">
                	<remote-destination host="localhost" port="<inifinispan-port>"/>
            	</outbound-socket-binding>

	4. Open a command line and navigate to the root directory of this quickstart.
	5. Start the Infinispan standalone instance to form a remote storage cluster
	5. Type this command to build and deploy the archive:
	
	        mvn clean package jboss-as:deploy -Pdefault-cluster
	6. This will deploy `target/jboss-as-hibernate-search.war` to the running domain instance of the server.
	



Access the application (For quickstarts that have a UI component)
---------------------
 
Standalone  mode

	Access the running application in a browser at the following URL:  <http://localhost:8080/jboss-as-hibernate-search>

Domain  mode

	Access the running application in a browser at the following URLs
	
		* <http://localhost:8080/jboss-as-hibernate-search>
		* <http://localhost:<port_of_other_instances>/jboss-as-hibernate-search>


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



Build and Deploy the Quickstart - to OpenShift
-------------------------

### Create an OpenShift Account and Domain

If you do not yet have an OpenShift account and domain, [Sign in to OpenShift](https://openshift.redhat.com/app/login) to create the account and domain. [Get Started with OpenShift](https://openshift.redhat.com/app/getting_started) will show you how to install the OpenShift Express command line interface.

### Create the OpenShift Application

Open a shell command prompt and change to a directory of your choice. Enter the following command, replacing APPLICATION_TYPE with `jbosseap-6.0` for quickstarts running on JBoss Enterprise Application Platform 6, or `jbossas-7` for quickstarts running on JBoss AS 7:

    rhc app create -a jbossashibernatesearch -t jbossas-7

This command creates an OpenShift application named  and will run the application inside the `jbosseap-6.0`  or `jbossas-7` container. You should see some output similar to the following:

    Creating application: jbossashibernatesearch
    Now your new domain name is being propagated worldwide (this might take a minute)...
    Warning: Permanently added 'jbossashibernatesearch-quickstart.rhcloud.com,107.22.36.32' (RSA) to the list of known hosts.
    Confirming application 'jbossashibernatesearch' is available:  Success!
    
    jbossashibernatesearch published:  http://jbossashibernatesearch-quickstart.rhcloud.com/
    git url:  ssh://b92047bdc05e46c980cc3501c3577c1e@jbossashibernatesearch-quickstart.rhcloud.com/~/git/jbossashibernatesearch.git/
    Successfully created application: jbossashibernatesearch

The create command creates a git repository in the current directory with the same name as the application. Notice that the output also reports the URL at which the application can be accessed. Make sure it is available by typing the published url <http://jbossashibernatesearch-quickstart.rhcloud.com/> into a browser or use command line tools such as curl or wget.

### Migrate the Quickstart Source

Now that you have confirmed it is working you can migrate the quickstart source. You do not need the generated default application, so navigate to the new git repository directory and tell git to remove the source and pom files:

    cd jbossashibernatesearch
    git rm -r src pom.xml

Copy the source for the hibernate-search quickstart into this new git repository:

    cp -r QUICKSTART_HOME/hibernate-search/src .
    cp QUICKSTART_HOME/hibernate-search/pom.xml .

### Configure your application on OpenShift

   Add the following cartridges to your OpenShift application
   	1. JBoss Application Server 7
   	2. PostgreSQL Database 8.4 
   	3. Modify the pom.xml with correct database configurations

### Deploy the Application on OpenShift

You can now deploy the changes to your application on OpenShift using git as follows:

    git add src pom.xml
    git commit -m "hibernate-search quickstart on OpenShift"
    git push

The final push command triggers the OpenShift infrastructure to build and deploy the changes. 

Note that the `openshift` profile in `pom.xml` is activated by OpenShift, and causes the war build by openshift to be copied to the `deployments` directory, and deployed without a context path.

### Test the Application on OpenShift

When the push command returns you can test the application by getting the following URL either via a browser or using tools such as curl or wget:

* <http://jbossashibernatesearch-quickstart.rhcloud.com/> 

You can use the OpenShift command line tools or the OpenShift web console to discover and control the application.

### Destroy the Application on OpenShift

When you are finished with the application you can destroy it as follows:

        rhc app destroy -a jbossashibernatesearch

* To view the list of your OpenShift applications, type: `rhc domain show`
* To destroy an existing application, type the following, substituting the application name you want to destroy: `rhc app destroy -a jbossashibernatesearch`
