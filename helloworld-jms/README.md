HelloWorld JMS Example
======================
This example demonstrates the use of external JMS clients with JBoss AS 7.

The example provides a demonstration of both producing and consuming messages to and from
a JMS destination deployed in the JBoss AS 7 environment. The example can be run from the Maven
commandline, or from the Eclipse environment. 

To run the example from the commandline simply type:

    mvn compile exec:java

The example provides for a certain amount of customization for the `mvn:exec` plugin via system properties explained below:


Required Properties
-------------------

* `username`
   
   This username is used for both the JMS connection and the JNDI look-up (if necessary).  Use the bin/add-user.sh script on the server
   to add this user to the "guest" role and the "ApplicationRealm".
   
   Default: `defaultUser`
		
* `password`

   This password is used for both the JMS connection and the JNDI look-up (if necessary).  See note about "username" regarding how to add
   the proper credentials to the server.
   
   Default: `defaultPassword`


Optional Properties
-------------------

* `cf.name`

   The name of the JMS ConnectionFactory you want to use.

   Default: `RemoteConnectionFactory`

* `dest.name`

   The name of the JMS Destination you want to use.
   
   Default: `testQueue`

* `message.content`

   The content of the JMS TextMessage.
	
   Default: `"Hello, World!"`

* `message.count`

   The number of JMS messages you want to produce and consume.

   Default: `1`
	

Deployment Note
---------------

By default, the JMS messaging provider is not deployed with the standalone JBoss AS 7 server. You will either need to run 
a domain server, or configure the standalone server for JMS message. Please refer to the JBoss AS 7 Documentation for further details.

To run the standalone server with JMS Messaging enabled

    standalone.sh -c standalone-full.xml


	


 
