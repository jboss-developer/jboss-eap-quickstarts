Ear Example
===================

@author paul.robinson@redhat.com


This example demonstrates the deployment of an EAR artifact. The EAR contains: *JSF 2.0* WAR and an *EJB 3.1* JAR.

The example is composed of three maven projects, each with a shared parent. The projects are as follows:

1. ejb
This project contains the EJB code and can be built independently to produce the JAR archive.

2. web
This project contains the JSF pages and the managed bean.

3. ear
This project builds the EAR artifact and pulls in the ejb and web artifacts.

The root pom builds each of the projects in the above order and deploys the EAR archive to the server.


The example follows the common "Hello World" pattern. These are the steps that occur:

1. A JSF page asks the user for their name.
2. On clicking 'Greet', the name is sent to a managed bean (Greeter).
3. On setting the name, the Greeter invokes the GreeterEJB, which was injected to the managed bean (notice the field annotated with @EJB).
4. The response from invoking the GreeterEJB is stored in a field (message) of the managed bean.
5. The managed bean is annotated as @SessionScoped, so the same managed bean instance is used for the entire session. This ensures that the message is available when the page reloads and is
displayed to the user.

The example can be deployed using Maven from the command line or from Eclipse using JBoss Tools.

To set up Maven or JBoss Tools in Eclipse, refer to the _Getting Started Guide_.

To deploy to JBoss AS 7, start JBoss AS 7, and type `mvn package`.
The application is deployed to <http://localhost:8080/jboss-as-ejb-in-ear>.

