HelloWorld JMS Example
======================
This example demonstrates the use of external JMS clients with JBoss7.

The example provides a demonstration of both producing and consuming messages to and from
a JMS destination deployed in the JBoss7 environment. The example can be run from the Maven
commandline, or from the Eclipse environment. 

To run the example from the commandline simply type:

mvn compile exec:java

For the example to work JBoss7 must be running. 

Assuming the compilation phase executed correctly, you should see the following messages printed to the console:

Dec 7, 2011 6:45:02 PM org.jboss.as.quickstarts.jms.JMSClientUtil getConnectionFactory
INFO: Attempting to acquire ConnectionFactory with a connection type of: netty
Dec 7, 2011 6:45:02 PM org.jboss.as.quickstarts.jms.JMSClientUtil getConnectionFactory
INFO: Creating Netty Based ConnectionFactory.
Dec 7, 2011 6:45:02 PM org.jboss.as.quickstarts.jms.JMSClientUtil getDestination
INFO: Attempting to acquire Destination with a connection type of: netty
Dec 7, 2011 6:45:02 PM org.jboss.as.quickstarts.jms.JMSClientUtil getDestination
INFO: HornetQ Destination testQueue
Dec 7, 2011 6:45:02 PM org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
INFO: Sending 1 messages with content: Hello, World!
Dec 7, 2011 6:45:02 PM org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
INFO: Received message with content Hello, World!

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 3.856s

Any exception in the code will result in build failure. Typically failures are the result of a misconfiguration on
the server or the server not running. 

**Deployment Note**
By default, the JMS messaging provider is not deployed with the standalone JBoss7 server. You will either need to run
a domain server, or configure the standalone server for JMS message. Please refer to the JBoss7 Documentation for further
details.

Example Customization
=============
The example provides for a certain amount of customization for the mvn:exec plugin via system properties explained below:

Required Properties
===================
connection.type
	Which connection type to the JMS provider you want to use. Accepted values are netty or jndi.
	Default: netty
	Note**
		Currently external JNDI access is under development in JBoss7. As a result netty is the default value.
		
java.naming.factory.initial
	The JNDI intial context factory you wish to use
	Default: org.jboss.as.naming.InitialContextFactory

java.naming.provider.url
	The provider URL of the JNDI context you wish to use
	Default: jnp://localhost:1099

Optional Properties
===================
cf.name
	The JNDI name of the JMS ConnectionFactory you want to use.
	Default: RemoteConnectionFactory

dest.name
	The JNDI name of the JMS Destination you want to use.
	Default testQueue

message.content
	The content of the JMS TextMessage.
	Default: Hello, World!

message.count
	The number of JMS messages you want to produce and consume.
	Default 1
	


	


 