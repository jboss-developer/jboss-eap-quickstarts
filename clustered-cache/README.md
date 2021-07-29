Web application: A web application use a cache provided by the EAP server
=================================================================================
Author: Wolf-Dieter Fink
Level: Beginner
Technologies: Infinispan Datagrid, EAP


What is it?
-----------

This example demonstrates how to use the included RHDG bits inside of EAP and configure an EAP server to manage JDG caches.
The configuration is included within the EAP standalone.xml or domain.xml file. The application code will get a cache reference
by injection or lookup via JNDI. The caches are not exposed and only applications deployed on this EAP instance can use it.
The lifecycle of the cache is managed by the EAP instance, the applications can undeployed or deployed without affects to the cache.



Prepare the server instances
-------------
Simple start
1.  Create a fresh copy of EAP 7.4 (or newer) server

2.  start EAP with the configuration standalone.xml or standalone-ha.xml

        $EAP_HOME/bin/standalone.sh -c standalone-ha.xml

3. use the standlone-local or standalone-clustered cli scripts to configure the server, depending whether you use an ha profile or not.

        $EAP_HOME/bin/jboss-cli.sh -c --file=standalone-local.cli



Build and Run the example
-------------------------
1. Type this command to build and deploy the archive:

        mvn clean package
        cp target/clustered-cache.war $EAP_HOME/standalone/deployments


Run the web application in your browser
=======================================

      http://localhost:8080/clustered-cache

Add, delete and list entries ...

Notes
=======
  - You can simple rename the clustered-cache.war to any other name.war to simulate different applications and copy it to $EAP_HOME/standalone/deployments folder.
    If there are multiple applicatations they share the same cache!
  - Caches are independent from application lifecycles. Undeploying any, or all, applications instances has no affect on a cache's lifecycle or data.
    Even if the cache has no persistence the data will be kept as long as the server is not stopped.
