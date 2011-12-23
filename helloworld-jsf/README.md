Hello world JSF Example
===================

This example demonstrates the use of *CDI 1.0*, *JSF 2.0*, and *RichFaces* in *JBoss AS 7*.

In this example, a standard JSF h:inputText component is ajax enabled using the RichFaces
a4j:ajax tag, triggering the application server to re-render a sub-section of the page on
a browser event.

The example can be deployed using Maven from the command line or from Eclipse using
JBoss Tools.

To set up Maven or JBoss Tools in Eclipse, refer to the _Getting Started Guide_.

To deploy to JBoss AS 7, start JBoss AS 7, and type `mvn package jboss-as:deploy`. 
The application is deployed to <http://localhost:8080/jboss-as-helloworld-jsf>. You
can read more details in the _Getting Started Guide_.