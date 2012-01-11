JTS and CMT: Using transactions managed by the container across several containers 
==================================================================================

What is it?
-----------

This example uses JTS to propagate a transaction from one server to another and access
two CMT EJBs that although deployed in separate servers will participate in the same
transaction.

The example expects to be deployed onto the same physical machine. This is not a restriction
of JTS and the example can easily be converted to run on separate machines by editing the
hostname value for EJB2 in org.jboss.as.quickstarts.cmt.jts.ejb.AccountManagerEJBImpl.

The example builds upon the CMT example also available in the quickstarts. 

Again, a simple MDB has been provided that prints out the messages sent but this is not a 
transactional MDB and is purely provided for debugging purposes.

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on a JBoss AS 7 or EAP 6. 
The following instructions target JBoss AS 7, but they also apply to JBoss EAP 6.


Testing the application
-------------------------

NOTE: Due to a difference in configuration between AS7 and EAP, all references of standalone-full.xml
apply to AS7 only, you can replace these references with standalone.xml if deploying into EAP.


For this example, you will need two instances of the application server, with a subtle startup 
configuration difference. Application server 2 must be started up with a port offset provided
to the startup script as "-Djboss.socket.binding.port-offset=100"

The application servers 1 should both be configured as follows:

1.	Open the file <APP_SERVER_1_HOME>/standalone/configuration/standalone-full.xml
2.	Enable JTS:
	Find the orb subsystem and change the configuration to:
        <subsystem xmlns="urn:jboss:domain:jacorb:1.1">
            <orb>
                <initializers security="on" transactions="on"/>
            </orb>
        </subsystem>
    Find the transaction subsystem and append the <jts/> element:
        <subsystem xmlns="urn:jboss:domain:transactions:1.1">
            <!-- LEAVE EXISTING CONFIG AND APPEND THE FOLLOWING -->
            <jts/>
        </subsystem>
		

To start JBoss AS 7 (or EAP 6) with a JMS connection factory and a queue named 
test queue in it. The easiest way to do this is to run:

		<APP_SERVER_1_HOME>/bin/standalone.sh -c standalone-full.xml
		<APP_SERVER_2_HOME>/bin/standalone.sh -c standalone-full.xml -Djboss.socket.binding.port-offset=100

or if you are using windows

		<APP_SERVER_1_HOME>\bin\standalone.bat -c standalone-full.xml
		<APP_SERVER_2_HOME>\bin\standalone.bat -c standalone-full.xml -Djboss.socket.binding.port-offset=100

To deploy the application, you first need to produce the archives to deploy using
the following Maven goals. Note that application-component-2 must be built first as it provides an EJB client
to application-component-1. Also note that application-component-2 must be "installed"

		cd <JTS_QUICKSTART_HOME>/application-component-2
		mvn install
		cd <JTS_QUICKSTART_HOME>/application-component-1
		mvn package
		
You can now deploy the artifact to JBoss AS by executing the following command. Again, due to the way the 
application is written (with little failure detection), it is best to deploy application-component-2 first
so that when application-component-1 is deployed it can resolve the EJB from the other container: 
		
		cd <JTS_QUICKSTART_HOME>/application-component-2
		mvn jboss-as:deploy
		cd <JTS_QUICKSTART_HOME>/application-component-1
		mvn jboss-as:deploy

The application will now be running at the following URL <http://localhost:8080/jboss-as-jts-application-component-1/>.

When you enter a name and click to "invoice" that customer, you will see the following in the application server 1 console:
12:09:38,424 INFO  [org.jboss.ejb.client] (http-localhost-127.0.0.1-8080-1) JBoss EJB Client version 1.0.0.Beta11
12:09:38,513 ERROR [jacorb.orb] (http-localhost-127.0.0.1-8080-1) no adapter activator exists for jboss-as-jts-application-component-2&%InvoiceManagerEJBImpl&%home
12:09:39,204 INFO  [class org.jboss.as.quickstarts.cmt.jts.mdb.HelloWorldMDB] (Thread-1 (group:HornetQ-client-global-threads-1095034080)) Received Message: Created customer named: Tom

You will also see the following in application-server-2 console:
12:09:38,697 INFO  [org.jboss.ejb.client] (RequestProcessor-10) JBoss EJB Client version 1.0.0.Beta11
12:09:39,204 INFO  [class org.jboss.as.quickstarts.cmt.jts.mdb.HelloWorldMDB] (Thread-3 (group:HornetQ-client-global-threads-649946595)) Received Message: Created invoice for customer named: Tom

The web page will not change but as long as you see the MDB messages in the server consoles you can be satisfied that the quickstart
has operated correctly.