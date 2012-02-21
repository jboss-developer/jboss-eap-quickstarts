E-Mail Example
===================

This example demonstrates sending email with the use of *CDI 1.0* and *JSF 2.0* in *JBoss AS 7*.

The example uses the default Mail provider that comes out of the box with JBoss AS 7.  This uses your local mail relay and default smtp port of 25.
The configuration of the mail provider is found in either *$JBOSS_HOME/standalone/configuration.xml* or *$JBOSS_HOME/domain/configuration.xml*. An example is provided below:

<subsystem xmlns="urn:jboss:domain:mail:1.0">
        <mail-session jndi-name="java:jboss/mail/Default" >
                <smtp-server address="localhost" port="25"/>
        </mail-session>
	<mail-session jndi-name="java:/MyOtherMail">
		<smtp-server address="localhost" port="9999">
                       <login name="nobody" password="pass"/>
                </smtp-server>
                <pop3-server address="example.com" port="1234"/>
                <imap-server address="example.com" port="432">
                    <login name="nobody" password="pass"/>
                </imap-server>
	</mail-session>
</subsystem>

The example is a web application that takes To, From, Subject, and Message Body input and sends mail to that address.  The front end is a JSF page with a simple POJO backing, leveraging CDI for resource injection.

The example can be deployed using Maven from the command line or from Eclipse using
JBoss Tools.

To set up Maven or JBoss Tools in Eclipse, refer to the _Getting Started Guide_.

To deploy to JBoss AS 7, start JBoss AS 7, and type `mvn package jboss-as:deploy`. 
The application is deployed to <http://localhost:8080/jboss-as-mail>. You
can read more details in the _Getting Started Guide_.
