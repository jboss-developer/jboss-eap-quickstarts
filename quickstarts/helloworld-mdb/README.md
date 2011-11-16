HelloworldMDB Example
==================

jboss-as-helloworld-mdb
===============================

What is it?
-----------

This example demonstrates the use of *JMS 1.1* and *EJB 3.1 Message-Driven Bean* in JBoss AS 7.1.0.

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven
3.0 or better.

The application this project produces is designed to be run on a JBoss AS 7.1.0. 
 
NOTE:
This project retrieves artifacts from the JBoss Community Maven repository, a
superset of the Maven central repository.

With the prerequisites out of the way, you're ready to build and deploy.

Deploying the application
-------------------------
 
First you need to start JBoss AS 7.1.0. To do this, run
  
    $JBOSS_HOME/bin/standalone.sh
  
or if you are using windows
 
    $JBOSS_HOME/bin/standalone.bat

To deploy the application, you first need to produce the archive to deploy using
the following Maven goal:

    mvn package

You can now deploy the artifact to JBoss AS by executing the following command:

    mvn jboss-as:deploy

This will deploy `target/jboss-as-helloworld-mdb.war`.
 
The application will be running at the following URL <http://localhost:8080/jboss-as-helloworld-mdb/HelloWorldMDBServletClient>.

Go to the JBoss AS console and the result can look like this:

10:38:24,139 INFO  [class org.jboss.as.quickstarts.servlet.HelloWorldMDBServletClient] (http--127.0.0.1-8080-2) Sending message: This is message 1
10:38:24,142 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-25 (group:HornetQ-client-global-threads-1185067593)) Received Message: This is message 1
10:38:24,144 INFO  [class org.jboss.as.quickstarts.servlet.HelloWorldMDBServletClient] (http--127.0.0.1-8080-2) Sending message: This is message 2
10:38:24,146 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-20 (group:HornetQ-client-global-threads-1185067593)) Received Message: This is message 2
10:38:24,147 INFO  [class org.jboss.as.quickstarts.servlet.HelloWorldMDBServletClient] (http--127.0.0.1-8080-2) Sending message: This is message 3
10:38:24,153 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-25 (group:HornetQ-client-global-threads-1185067593)) Received Message: This is message 3
10:38:24,154 INFO  [class org.jboss.as.quickstarts.servlet.HelloWorldMDBServletClient] (http--127.0.0.1-8080-2) Sending message: This is message 4
10:38:24,157 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-20 (group:HornetQ-client-global-threads-1185067593)) Received Message: This is message 4
10:38:24,158 INFO  [class org.jboss.as.quickstarts.servlet.HelloWorldMDBServletClient] (http--127.0.0.1-8080-2) Sending message: This is message 5
10:38:24,163 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-20 (group:HornetQ-client-global-threads-1185067593)) Received Message: This is message 5

To undeploy from JBoss AS, run this command:

    mvn jboss-as:undeploy

You can also start JBoss AS 7 and deploy the project using Eclipse. See the JBoss AS 7
Getting Started Guide for Developers for more information.
