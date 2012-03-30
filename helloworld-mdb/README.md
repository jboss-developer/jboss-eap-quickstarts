helloworld-mdb: Helloword Using an MDB (Message-Driven Bean)
============================================================
Author: Serge Pagop, Andy Taylor

What is it?
-----------

This example demonstrates the use of *JMS 1.1* and *EJB 3.1 Message-Driven Bean* in JBoss AS 7.1.0.

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven
3.0 or better.

The application this project produces is designed to be run on a JBoss AS 7 or JBoss Enterprise Application Platform 6. 
 
This Project will use the provided connection factory named `InVmConnectionFactory` which is bound into JNDI as `java:/ConnectionFactory` and a queue named `testQueue` which is bound into JNDI as `queue/test`.

With the prerequisites out of the way, you're ready to build and deploy.

Deploying the application
-------------------------

For JBoss AS 7 or JBoss Enterprise Application Platform 6:

    On Linux run: $JBOSS_HOME/bin/standalone.sh -c standalone-full.xml

    On Windows run: $JBOSS_HOME/bin/standalone.bat -c standalone-full.xml

To deploy the application, you first need to produce the archive to deploy using
the following Maven goal:

    mvn package

You can now deploy the artifact to JBoss AS by executing the following command:

    mvn jboss-as:deploy

This will deploy `target/jboss-as-helloworld-mdb.war`.
 
The application will be running at the following URL <http://localhost:8080/jboss-as-helloworld-mdb/HelloWorldMDBServletClient>.

Go to the JBoss Application Server console or Server log and the result can look like this:

    15:42:35,453 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-47 (group:HornetQ-client-global-threads-1267410030)) Received Message: This is message 1
    15:42:35,455 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-46 (group:HornetQ-client-global-threads-1267410030)) Received Message: This is message 2
    15:42:35,457 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-50 (group:HornetQ-client-global-threads-1267410030)) Received Message: This is message 3
    15:42:35,478 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-53 (group:HornetQ-client-global-threads-1267410030)) Received Message: This is message 5
    15:42:35,481 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-52 (group:HornetQ-client-global-threads-1267410030)) Received Message: This is message 4


To undeploy from JBoss AS, run this command:

    mvn jboss-as:undeploy

You can also start JBoss AS 7 and deploy the project using Eclipse. See the JBoss AS 7
<a href="https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a> 
for more information.

Deploying the Application in OpenShift
----------------------------------

Firstly lets assume you already have an openshift(express) account with a domain created. If you don't please visit
https://openshift.redhat.com/app/login create an account and follow the getting started guide which can be found at
http://docs.redhat.com/docs/en-US/OpenShift_Express/2.0/html/Getting_Started_Guide/index.html.

Note that for brevity some of the commands have been simplified to remove login details etc.

Open up a shell and from the directory of your choice run the following command to create our helloworld application.

    rhc-create-app -a helloworldmdb -t jbossas-7.1

You should see some output which will show the application being deployed and also the URL at which it can be accessed.

    helloworldmdb published:  http://helloworldmdb-andytaylor.rhcloud.com/
    git url:  ssh://cda61f9f967d42cd98486eb1a293acbd@helloworldmdb-andytaylor.rhcloud.com/~/git/helloworldmdb.git/
    To make changes to 'helloworldmdb', commit to helloworldmdb/.
    Successfully created application: helloworldmdb

Now in a separate shell navigate to the quick starts helloworld-mdb directory and build the war, like so:

    mvn clean package

we now need to copy the packaged jboss-as-helloworld-mdb.war into the helloworld/deployments directory of your openshift
application and add, commit and push it to the openshift repository like so:

    cd helloworldmdb

    git add deployments/jboss-as-helloworld-mdb.war

    git commit -m "deploy"

    git push

Once the app is deployed open up a browser and run the application, the URL will be similar as follows but with your own
domain name.

    http://helloworldmdb-ataylor.dev.rhcloud.com/jboss-as-helloworld-mdb/HelloWorldMDBServletClient

If the application has run succesfully you should see some output in the browser.

now you can look at the output of the server by running the following command:

    rhc-ctl-app -a helloworldmdb -c status

This will show the tail of the servers log which should show something like the following.

2012/03/02 05:52:33,065 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-0 (HornetQ-client-global-threads-1772719)) Received Message: This is message 4
2012/03/02 05:52:33,065 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-1 (HornetQ-client-global-threads-1772719)) Received Message: This is message 1
2012/03/02 05:52:33,067 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-6 (HornetQ-client-global-threads-1772719)) Received Message: This is message 5
2012/03/02 05:52:33,065 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-3 (HornetQ-client-global-threads-1772719)) Received Message: This is message 3
2012/03/02 05:52:33,065 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-2 (HornetQ-client-global-threads-1772719)) Received Message: This is message 2

