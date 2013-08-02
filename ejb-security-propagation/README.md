ejb-security-propagation: EJB security propagation across servers
=================================================================
Author: Claudio Miranda <claudio@redhat.com>
Level: Advanced
Technologies: EJB, Servlets, Security
Summary: Security context propagation between JBoss server instances, when using EJB calls. 
Target Product: EAP 6.1
Source: <https://github.com/jboss-jdf/jboss-as-quickstart/>

What is it?
-----------

Demonstrates how to use a EJB client protected by a security domain, deployed in a server that calls a EJB deployed on a secondary server, protected by a security domain.
The security context authenticated on the first server is propagated to the second server. 
The security domain on both servers uses the same Database login module.

It is based on the ejb-security-interceptors and servlet-security quickstart.

There is a SecuredEJB protected by security domain "security-propagation-quickstart".  

JBoss EAP 6.1 or later is necessary to work with this quickstart. This quickstart runs in a managed domain and uses the domain.xml and host.xml. 


The web application jboss-as-ejb-security-propagation-web is deployed on server server-one, and jboss-as-ejb-security-propagation-ejb is deployed on server-two.
The web application has two servlets:
* HelloServlet: unsecure servlet that makes a remote EJB call to unsecure HelloEJB
* SecuredEJBServlet: protected servlet that makes a remote EJB call to SecuredEJB, protected with the same security domains as SecuredEJBServlet.  

The root 'pom.xml' builds each of the subprojects in an appropriate order.

System requirements
-------------------

All you need to build this project is:
  Java 6.0 (Java SDK 1.6) or better, 
  Maven 3.0 or better.

The application this project produces is designed to be run on:

        JBoss Enterprise Application Platform 6.1
 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Configure Components
-------------------------

 *  Add an Application User

This quickstart uses secured management interfaces and requires that you create an application user to access the running application. Instructions to set up the quickstart application user can be found here: [Add an Application User](../README.md#add-an-application-user)

That user is necessary for the server-one (where the ejb client is deployed) establish a trusted communication channel to server-two (where the EJB is deployed).

The last line shows the secret value we are going to use.  

 * Configure the H2 Network Server
 
Start the H2 network server on background

    $JAVA_HOME/bin/java -classpath $JB_HOME/modules/system/layers/base/com/h2database/h2/main/h2-1.3.168-redhat-2.jar org.h2.tools.Server -tcp -baseDir $HOME &

  It should listen to 9092 port.

Create the table structure and input some data

    $JAVA_HOME/bin/java -classpath $JB_HOME/modules/system/layers/base/com/h2database/h2/main/h2-1.3.168-redhat-2.jar org.h2.tools.RunScript -url jdbc:h2:tcp://localhost/qs_ejb -user sa -script ejb/src/main/resources/import.sql
  
 * Configure server groups

Open host.xml, go to the servers section. For the server-two, rename the group name from main-server-group to main-server-group-2. Save it. The result should be:

        <server name="server-one" group="main-server-group">
        </server>
        <server name="server-two" group="main-server-group-2" auto-start="true">
            <socket-bindings port-offset="150"/>
        </server>

 * Configure the jboss client to jboss server authentication

Open host.xml, add the following security realm, after "ApplicationRealm"

        <security-realm name="ejb-remote-call">
            <server-identities>
                <secret value="QGFkbWluMTIz"/>
            </server-identities>
        </security-realm> 

`QGFkbWluMTIz` is the base64 format for @admin123 password. You can also generate it with "echo -n @admin123 | base64"
 
Open domain.xml, go to the profiles section, duplicate "full" profile, and name it as "full-2". 

Go to server-groups section, duplicate main-server-group and name it main-server-group-2 and associates it with full-2 profile.

 * Configure the server profiles

Go to "full-sockets" socket binding group, add the following outbound-socket-binding

        <outbound-socket-binding name="srv2srv-ejb-socket">
            <remote-destination host="localhost" port="4597"/>
        </outbound-socket-binding> 

Go to "remoting" subsystem of "full" profile and add the relevant section as show below

        <subsystem xmlns="urn:jboss:domain:remoting:1.1">
            <connector name="remoting-connector" socket-binding="remoting" security-realm="ApplicationRealm"/>
            <outbound-connections>
                <remote-outbound-connection name="ejb-outbound-connection" outbound-socket-binding-ref="srv2srv-ejb-socket" username="quickstartUser" security-realm="ejb-remote-call">
                    <properties>
                        <property name="SSL_ENABLED" value="false"/>
                        <property name="SASL_POLICY_NOANONYMOUS" value="false"/>
                    </properties>
                </remote-outbound-connection>
            </outbound-connections>
        </subsystem>


Add the datasource SecurityPropagationDS to the full and full-2 profiles. 
It is used in the security module to authenticates the user stored in the H2 database.

    <datasource jta="false" jndi-name="java:jboss/datasources/SecurityPropagationDS" pool-name="SecurityPropagationDS" enabled="true" use-ccm="false">
        <connection-url>jdbc:h2:tcp://localhost/qs_ejb</connection-url>
        <driver-class>org.h2.Driver</driver-class>
        <driver>h2</driver>
        <pool>
            <min-pool-size>1</min-pool-size>
            <max-pool-size>5</max-pool-size>
        </pool>
        <security>
            <user-name>sa</user-name>
            <password></password>
        </security>
        <validation>
            <validate-on-match>false</validate-on-match>
            <background-validation>false</background-validation>
        </validation>
        <statement>
            <share-prepared-statements>false</share-prepared-statements>
        </statement>
    </datasource> 

Add the security-propagation-quickstart login-module in the security-domain subsystem of full profile.
This security domain is used only for the web application (EJB client).

    <security-domain name="security-propagation-quickstart" cache-type="default">
        <authentication>
            <login-module code="Database" flag="required">
                <module-option name="dsJndiName" value="java:jboss/datasources/SecurityPropagationDS"/>
                <module-option name="principalsQuery" value="SELECT PASSWORD FROM USERS WHERE USERNAME = ?"/>
                <module-option name="rolesQuery" value="SELECT R.NAME, 'Roles' FROM USERS_ROLES UR INNER JOIN ROLES R ON R.ID = UR.ROLE_ID INNER JOIN USERS U ON U.ID = UR.USER_ID WHERE U.USERNAME = ?"/>
                <module-option name="password-stacking" value="useFirstPass"/>
            </login-module>
        </authentication>
    </security-domain>

Add the following security-propagation-quickstart login-module in the security-domain subsystem of full-2 profile.

    <security-domain name="security-propagation-quickstart" cache-type="default">
        <authentication>
            <login-module code="org.jboss.as.quickstarts.ejb_security_interceptors.DelegationLoginModule" flag="optional">
                <module-option name="password-stacking" value="useFirstPass"/>
            </login-module>
            <login-module code="Remoting" flag="optional">
                <module-option name="password-stacking" value="useFirstPass"/>
            </login-module>
            <login-module code="Database" flag="required">
                <module-option name="dsJndiName" value="java:jboss/datasources/SecurityPropagationDS"/>
                <module-option name="principalsQuery" value="SELECT PASSWORD FROM USERS WHERE USERNAME = ?"/>
                <module-option name="rolesQuery" value="SELECT R.NAME, 'Roles' FROM USERS_ROLES UR INNER JOIN ROLES R ON R.ID = UR.ROLE_ID INNER JOIN USERS U ON U.ID = UR.USER_ID WHERE U.USERNAME = ?"/>
                <module-option name="password-stacking" value="useFirstPass"/>
            </login-module>
        </authentication>
    </security-domain>
    
The security-propagation-quickstart for the full-2 profile contains additional login-modules as they are necessary for the security context propagation.

Now you must generate the ejb-propagation-interceptor.jar file and add it as a module

    mkdir -p $JB_HOME/modules/system/layers/base/org/jboss/as/quickstarts/ejb_security/main

Add the following module.xml to $JB_HOME/modules/system/layers/base/org/jboss/as/quickstarts/ejb_security/main

    <module xmlns="urn:jboss:module:1.1" name="org.jboss.as.quickstarts.ejb_security">
    
        <resources>
            <resource-root path="ejb-propagation-interceptor.jar"/>
        </resources>
    
        <dependencies>
            <module name="org.jboss.remoting3" />
            <module name="org.jboss.as.domain-management" />
            <module name="org.jboss.as.controller" />
            <module name="javaee.api" />
            <module name="org.jboss.logging" />
            <module name="org.jboss.ejb-client" />
            <module name="org.picketbox" />
            <module name="org.jboss.as.security" />
        </dependencies>
    </module>

   
Build the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Open a command line and navigate to the root directory of this quickstart.
2. Type this command to build the archive:

        mvn clean install

3. Copy the interceptor module

        cp interceptor-module/target/ejb-propagation-interceptor.jar $JB_HOME/modules/system/layers/base/org/jboss/as/quickstarts/ejb_security/main


Start JBoss Enterprise Application Platform 6.1
-------------------------

Start JBoss EAP 6.1 as ./domain.sh you must see there are two server process as seen with: ps -ef|grep Server:server

Deploy the Quickstart
-------------------------

1. cd $JB_HOME/bin
2. $JB_HOME/bin/jboss-cli.sh --connect --command="deploy ejb/target/jboss-as-propagation-ejb.jar --server-groups=main-server-group-2"
3. $JB_HOME/bin/jboss-cli.sh --connect --command="deploy web/target/jboss-as-propagation-web.war --server-groups=main-server-group"

Access the application
---------------------

When you deploy the quickstart (or start the server-one after deployment) it should display in server-one log the registration for the ClientSecurityInterceptor as

    >>>>>>>>>> ClientSecurityInterceptor Constructor

When you deploy EJB it shows the JNDI names for HelloEJB and SecuredEJB.

Access `http://localhost:8080/jboss-as-propagation-web/hello` 

    It should displays "Successfully called Hello EJB". This show the remoting part is working. This is a non protected servlet that calls a non protected EJB.
    The response is
    Hello Principal : anonymous
    Hello Remote User : null
    Hello Authentication Type : null

The log shoud show (only the relevant parts)

    [Server:server-one] ejb: Proxy for remote EJB StatelessEJBLocator{appName='', moduleName='jboss-as-propagation-ejb', distinctName='', beanName='HelloEJB', view='interface org.jboss.as.quickstarts.ejb_security.Hello'}
    [Server:server-one] >>>>>>>>>>>> principal: null
    [Server:server-two]  ==> EJB principal: anonymous

Access `http://localhost:8080/jboss-as-propagation-web/secure_ejb`

It prompts for a user and password, type as

    user    : admin
    password: admin123 

It should display in the browser

    Successfully called Secured EJB
    Principal : admin
    Remote User : admin
    Authentication Type : BASIC

The server-two log should displays

    [Server:server-one] ejb: Proxy for remote EJB StatelessEJBLocator{appName='', moduleName='jboss-as-propagation-ejb', distinctName='', beanName='SecuredEJB', view='interface org.jboss.as.quickstarts.ejb_security.Secured'}
    [Server:server-one] >>>>>>>>>>>> principal: admin
    [Server:server-two] >>>>>>>>>> contextData {TestDelegationUser=admin}
    [Server:server-two] >>>>>>>>>>>>> Switch users 
    [Server:server-two] >>>>>>>>>> delegationAcceptable: admin

The user is authenticated to the servlet (security domain security-propagation-quickstart), before the EJB call, EJB client interceptor sends the credentials to the target JBoss server.
On the EJB side, the EJB server interceptor receives the credential, switches the EJB authenticated user from "quickstartUser" to "admin", the security-propagation-quickstart security domain calls DelegationLoginModule that checks the credential received from EJB server interceptor and grants access. 


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------

You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
