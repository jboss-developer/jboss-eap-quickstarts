jboss-as-management server status Example
=========================================

What is it?
-----------

This example shows you how you can use the JBoss Application Server Management API to get status of a running standalone server.


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven
3.0 or better.

The application this project produces is designed to be run on a JBoss AS 7.1.0 or EAP 6.0
 
With the prerequisites out of the way, you're ready to build and deploy.


Run the standalone application
-------------------------

First of all you need to enable the "admin" user from $JBOSS_HOME/standalone/configuration/mgmt-users.properties file, and then start JBoss AS 7.1.0. by running this script
  
    $JBOSS_HOME/bin/standalone.sh
  
or if you are using windows
 
    $JBOSS_HOME/bin/standalone.bat

To run the application, use the following Maven goal:

    mvn test

Note: you can also run the sample standalone application "Client" from your Eclipse IDE 
After executing the maven goal or from eclipse, the result can look like this:

Jan 2, 2012 6:18:56 PM org.jboss.remoting3.EndpointImpl <clinit>
INFO: JBoss Remoting version 3.2.0.CR6

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
&&&&&& Server status: "running"
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------

You can also start JBoss AS 7 and test the project using Eclipse. See the JBoss AS 7
Getting Started Guide for Developers for more information.
