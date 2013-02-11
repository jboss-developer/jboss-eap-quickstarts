deltaspike-invocationhandler: HelloWorld Example of DeltaSpike InvocationHandler
======================
Author: Jess Sightler
Level: Intermediate
Technologies: CDI, DeltaSpike
Summary: Demonstrates an InvocationHandler providing dynamic implementation of methods in an abstract class
Target Product: WFK

What is it?
-----------

This quickstart demonstrates the use of an InvocationHandler to provide dynamic implementations of an abstract class.

The quickstart consists of the following classes:

 - HelloWorldBean - Base implementation class. This class could contain concrete methods as well as abstract. The abstract methods (for example, "sayHello") will be filled in by the InvocationHandler itself (HelloWorldInvocationHandler in this case)
 - HelloWorldInvocationHandler - This class implements a dynamic InvocationHandler. When "sayHello" is called on the Bean, the "invoke" method will be called in its place.
 - HelloWorldInvocationHandlerBinding - This Annotation is used to bind the Bean to its InvocationHandler

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better and Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 


Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.

Build and Deploy the Quickstart
-------------------------

To run the quickstart from the command line:

1. Open a command line and navigate to the root of the deltaspike-invocationhandler quickstart directory:

        cd PATH_TO_QUICKSTARTS/deltaspike-invocationhandler

2. Type the following command to compile and execute the quickstart:

        For JBoss Enterprise Application Platform 6 (Maven user settings NOT configured): 

            mvn clean compile exec:java -s PATH_TO_QUICKSTARTS/example-settings.xml

        For JBoss AS 7 or JBoss Enterprise Application Platform 6 (Maven user settings configured): 

            mvn clean compile exec:java

Investigate the Console Output
-------------------------

If the maven command is successful, with the default configuration you will see output similar to this:

    Feb 11, 2013 6:47:16 PM org.apache.deltaspike.core.util.ClassDeactivationUtils cacheResult
    INFO: class: org.apache.deltaspike.core.impl.config.ConfigurationExtension activated=true
    Feb 11, 2013 6:47:16 PM org.apache.deltaspike.core.util.ClassDeactivationUtils cacheResult
    INFO: class: org.apache.deltaspike.core.impl.exclude.extension.ExcludeExtension activated=true
    Feb 11, 2013 6:47:16 PM org.apache.deltaspike.core.util.ClassDeactivationUtils cacheResult
    INFO: class: org.apache.deltaspike.core.impl.exclude.GlobalAlternative activated=true
    Feb 11, 2013 6:47:16 PM org.apache.deltaspike.core.util.ClassDeactivationUtils cacheResult
    INFO: class: org.apache.deltaspike.core.impl.exclude.CustomProjectStageBeanFilter activated=true
    Feb 11, 2013 6:47:16 PM org.apache.deltaspike.core.util.ClassDeactivationUtils cacheResult
    INFO: class: org.apache.deltaspike.core.impl.exception.control.extension.ExceptionControlExtension activated=true
    Feb 11, 2013 6:47:16 PM org.apache.deltaspike.core.util.ClassDeactivationUtils cacheResult
    INFO: class: org.apache.deltaspike.core.impl.message.MessageBundleExtension activated=true
    Feb 11, 2013 6:47:16 PM org.apache.deltaspike.core.util.ClassDeactivationUtils cacheResult
    INFO: class: org.apache.deltaspike.core.impl.invocationhandler.InvocationHandlerBindingExtension activated=true
    Feb 11, 2013 6:47:16 PM org.apache.deltaspike.core.util.ClassDeactivationUtils cacheResult
    INFO: class: org.apache.deltaspike.core.impl.jmx.MBeanExtension activated=true
    Feb 11, 2013 6:47:16 PM org.apache.deltaspike.core.util.ProjectStageProducer initProjectStage
    INFO: Computed the following DeltaSpike ProjectStage: Production
    Feb 11, 2013 6:47:16 PM org.jboss.as.quickstarts.invocationhandler.HelloWorldInvocationHandler invoke
    INFO: Method called: sayHello
    Feb 11, 2013 6:47:16 PM org.jboss.as.quickstarts.invocationhandler.HelloWorldInvocationHandler invoke
    INFO: Arguments passed: [JBoss Client]
    Feb 11, 2013 6:47:16 PM org.jboss.as.quickstarts.invocationhandler.Main main
    INFO: Hello call result: Hello JBoss Client


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc

