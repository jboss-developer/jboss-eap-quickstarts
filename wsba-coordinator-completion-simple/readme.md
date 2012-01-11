WSBA - Coordinator Completion
========================

What is it?
-----------

This example demonstrates the deployment of a WS-BA (WS Business Activity) enabled JAX-WS Web service bundled in a war
archive for deployment to *JBoss AS 7*.

The Web service exposes a simple 'set' collection as a service. The Service allows items to be added to the set within a
Business Activity.

The example demonstrates the basics of implementing a WS-BA enabled Web service. It is beyond the scope of this
quick start to demonstrate more advanced features. In particular:

1. The Service does not implement the required hooks to support recovery in the presence of failures.
2. It also does not utilize a transactional back end resource.
3. Only one Web service participates in the protocol. As WS-BA is a coordination protocol, it is best suited to multi-participant scenarios.

For a more complete example, please see the XTS demonstrator application that ships with the JBossTS project: http://www.jboss.org/jbosstm.

It is also assumed tht you have an understanding of WS-BusinessActivity. For more details, read the XTS documentation
that ships with the JBossTS project, which can be downloaded here: http://www.jboss.org/jbosstm/downloads/JBOSSTS_4_16_0_Final

The application consists of a single JAX-WS web service that is deployed within a war archive. It is tested with a JBoss
Arquillian enabled JUnit test.

When running the org.jboss.as.quickstarts.wsba.coordinatorcompletion.simple.ClientTest#testSuccess() method, the
following steps occur:

1. A new Business Activity is created by the client.
2. Multiple operations on a WS-BA enabled Web service is invoked by the client.
3. The JaxWSHeaderContextProcessor in the WS Client handler chain inserts the BA context into the outgoing SOAP messages
4. When the service receives a SOAP request, it's JaxWSHeaderContextProcessor in it's handler chain inspects the BA context and associates the request with this BA.
5. The Web service operation is invoked...
6. For the first request, in this BA, A participant is enlisted in this BA. This allows the Web Service logic to respond to protocol events, such as compensate and close.
7. The service invokes the business logic. In this case, a String value is added to the set.
9. The client can then make additional calls to the SetService. As the SetService participates as a CoordinatorCompletion protocol, it will continue to accept calls to 'addValueToSet' until it is told to complete by the coordinator.
10. The client can then decide to complete or cancel the BA. If the client decides to complete, all participants will be told to complete. Providing all participants successfully complete, the coordinator will then tell all participants to close, otherwise the completed participants will be told to compensate.  If the participant decides to cancel, all participants will be told to compensate.

There are other tests that show:

# What happens when an application exception is thrown by the service.
# How the client can cancel a BA.


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven
3.0 or better.

The application this project produces is designed to be run on a JBoss AS 7 or EAP 6.
The following instructions target JBoss AS 7, but they also apply to JBoss EAP 6.

With the prerequisites out of the way, you're ready to build and deploy.

Deploying the application
-------------------------

First you need to start JBoss AS 7 (7.1.0.CR1 or above, or EAP 6), with the XTS sub system enabled. To do this, run

    $JBOSS_HOME/bin/standalone.sh --server-config=../../docs/examples/configs/standalone-xts.xml

or if you are using windows

    $JBOSS_HOME/bin/standalone.bat --server-config=../../docs/examples/configs/standalone-xts.xml

To test the application run:

    mvn clean test -Parq-jbossas-remote

You can also start JBoss AS 7 and run the tests within Eclipse. See the JBoss AS 7
Getting Started Guide for Developers for more information.

Importing the project into an IDE
=================================

If you created the project using the Maven archetype wizard in your IDE
(Eclipse, NetBeans or IntelliJ IDEA), then there is nothing to do. You should
already have an IDE project.

Detailed instructions for using Eclipse with JBoss AS 7 are provided in the
JBoss AS 7 Getting Started Guide for Developers.

If you created the project from the commandline using archetype:generate, then
you need to import the project into your IDE. If you are using NetBeans 6.8 or
IntelliJ IDEA 9, then all you have to do is open the project as an existing
project. Both of these IDEs recognize Maven projects natively.

Downloading the sources and Javadocs
====================================

If you want to be able to debug into the source code or look at the Javadocs
of any library in the project, you can run either of the following two
commands to pull them into your local repository. The IDE should then detect
them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
