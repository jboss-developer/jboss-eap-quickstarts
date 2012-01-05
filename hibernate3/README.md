jboss-as-hibernate3
========================

What is it?
-----------

This is a simple JSF 2.0 example based on 'numberguess' quickstart. Its purpose is to demonstrate
how you can include custom library in your application.

In some cases you can find it useful to include third party library directly in Java EE application
deployment unit. There are different cases when above is true, for instance:

	* to provide fully portable application package</li>
	* when container has no module defined for binary dependency</li>
	* when container has module with different version of binary dependency</li>

This example falls into third category. It shows how to include and configure Hibernate 3.x.
To better understand how it is done, its good to get acquainted with 'log4jdemo' quickstart.

 Even thought this examples java code is simple, it is quite complicated example. As shown in 'log4jdemo' defining dependency is quite easy.
 This example, however goes a bit beyond simple module dependency.<br>
 There are three aspects of this example that play a role here:

 	* assembly - the Maven script and maven-war-plugin create 'war' which includes ONLY Hibernate 3.x binaries. To understand better how its 
 	     achieved, please refer to this examples 'pom.xml' and <a href="http://maven.apache.org/plugins/maven-war-plugin/">http://maven.apache.org/plugins/maven-war-plugin/</a> documentation.
 	* module exclusion and inclusion - this example alters runtime dependency on container provided modules. It declares dependency on container provided modules which are dependencies of Hibernate binaries included 
 	     in application deployment unit. It also excludes implicit dependency on container default Hibernate module (which is version 4.x).
 	     This is achieved with JBoss AS7 specific file, the <b>jboss-deployment-structure.xml</b>. For more details about this file, please refer to <a href="https://docs.jboss.org/author/display/AS7/Developer+Guide#DeveloperGuide-JBossDeploymentStructureFile">https://docs.jboss.org/author/display/AS7/Developer+Guide#DeveloperGuide-JBossDeploymentStructureFile</a>
 	* persistence configuration - specific configuration is required to instruct container to ensure it loads proper JPA/Hibernate.
  
System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven
3.0 or better.

The application this project produces is designed to be run on a JBoss AS 7 or EAP 6.
The following instructions target JBoss AS 7, but they also apply to JBoss EAP 6.

With the prerequisites out of the way, you're ready to build and deploy.

Deploying the application
-------------------------

First you need to start JBoss AS 7 (or EAP 6). To do this, run

    $JBOSS_HOME/bin/standalone.sh

or if you are using windows

    $JBOSS_HOME/bin/standalone.bat

To deploy the application, you first need to produce the archive to deploy using
the following Maven goal:

    mvn package

You can now deploy the artifact to JBoss AS by executing the following command:

    mvn jboss-as:deploy

This will deploy `target/jboss-as-hibernate3.war`.

The application will be running at the following URL <http://localhost:8080/jboss-as-hibernate3/>.

To undeploy from JBoss AS, run this command:

    mvn jboss-as:undeploy

You can also start JBoss AS 7 and deploy the project using Eclipse. See the JBoss AS 7
Getting Started Guide for Developers for more information.

Downloading the sources and Javadocs
====================================

If you want to be able to debug into the source code or look at the Javadocs
of any library in the project, you can run either of the following two
commands to pull them into your local repository. The IDE should then detect
them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc