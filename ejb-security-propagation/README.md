ejb-security-propagation: EJB security propagation across servers
=================================================================
Author: Claudio Miranda <claudio@redhat.com>
Level: Advanced
Technologies: EJB, Servlets, Security
Summary: Security context propagation between JBoss server instances, when using EJB calls. 
Target Product: EAP
Product Versions: EAP 6.1, EAP 6.2
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>

What is it?
-----------

This quickstart is based on the `ejb-security-interceptors` and `servlet-security` quickstarts, but was enhanced to demonstrate EJB security propagation across servers. The following is a general overview of this example:

* An EJB client, protected by the "security-propagation-quickstart" security domain, is deployed to the first server. 
* It calls the `SecuredEJB`, also protected by the "security-propagation-quickstart" security domain, that is deployed on a secondary server. 
* The security context authenticated on the first server is propagated to the second server. 
* The security domain on both servers uses the same database login module.

The web application `jboss-ejb-security-propagation-web` is deployed to `server-one`. It has two servlets:

1. HelloServlet: An unsecured servlet that makes a remote EJB call to the unsecured `HelloEJB`/
2. SecuredEJBServlet: A protected servlet that makes a remote EJB call to `SecuredEJB`, protected with the same security domains as `SecuredEJBServlet`.

The `jboss-ejb-security-propagation-ejb` EJB is deployed to `server-two`.

The root `pom.xml` builds each of the subprojects in an appropriate order.

This quickstart runs in a managed domain and uses the `domain.xml` and `host.xml`. 

_Note: This quickstart uses the H2 database included with JBoss EAP 6. It is a lightweight, relational example database that is used for examples only. It is not robust or scalable and should NOT be used in a production environment!_

System requirements
-------------------

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6.1 or later.

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.
 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Add an Application User
----------------
This quickstart uses secured management interfaces and requires that you create an application user to access the running application. Instructions to set up the quickstart application user can be found here: [Add an Application User](../README.md#add-an-application-user)

The standard user and password is:

        Username : quickstartUser
        Password : quickstartPwd1!

This user is necessary for `server-one`, where the EJB client is deployed, to establish a trusted communication channel to `server-two`, where the EJB is deployed.

Configure H2 database server
-------------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. Start the H2 network server in the background. Be sure to replace JBOSS_HOME with the path to your server.

        For Linux:  $JAVA_HOME/bin/java -classpath JBOSS_HOME/modules/system/layers/base/com/h2database/h2/main/h2-1.3.168-redhat-2.jar org.h2.tools.Server -tcp -baseDir $HOME & 
        For Windows:  %JAVA_HOME%\bin\java -classpath JBOSS_HOME\modules\system\layers\base\com\h2database\h2\main\h2-1.3.168-redhat-2.jar org.h2.tools.Server -tcp -baseDir %HOMEPATH% &

  It should listen on 9092 port.

3. Open another command line and navigate to the root directory of this quickstart. You must be in this directory to access the `import.sql` script file.
4. Create the table structure and input some data. Be sure to replace JBOSS_HOME with the path to your server.

        For Linux:  $JAVA_HOME/bin/java -classpath JBOSS_HOME/modules/system/layers/base/com/h2database/h2/main/h2-1.3.168-redhat-2.jar org.h2.tools.RunScript -url jdbc:h2:tcp://localhost/qs_ejb -user sa -script ejb/src/main/resources/import.sql
        For Windows:  %JAVA_HOME%\bin\java -classpath JBOSS_HOME\modules\system\layers\base\com\h2database\h2\main\h2-1.3.168-redhat-2.jar org.h2.tools.RunScript -url jdbc:h2:tcp://localhost/qs_ejb -user sa -script ejb\src\main\resources\import.sql


Configure the JBoss EAP server
-------------------------

You can configure the server by running the  `configure-server.cli` script provided in the root directory of this quickstart, by using the JBoss CLI interactively, or by manually editing the configuration file.

_NOTE - Before you begin:_

1. If it is running, stop the JBoss EAP server.
2. Backup the following files, replacing JBOSS_HOME with the path to your server.: 

        JBOSS_HOME/domain/configuration/domain.xml
        JBOSS_HOME/domain/configuration/host.xml
        
3. After you have completed testing this quickstart, you can replace these files to restore the server to its original configuration.


#### Configure the Server by Running the JBoss CLI Script

1. Start the JBoss EAP server by typing the following command, replacing JBOSS_HOME with the path to your server. 

        For Linux:  JBOSS_HOME/bin/domain.sh 
        For Windows:  JBOSS_HOME\bin\domain.bat

   You should see two server processes when you execute the following command: `ps -ef|grep Server:server`

2. Open a new command line, navigate to the root directory of this quickstart, and run the following command, replacing JBOSS_HOME with the path to your server:

        For Linux:  JBOSS_HOME/bin/jboss-cli.sh --connect --file=configure-server.cli
        For Windows:  JBOSS_HOME\bin\/jboss-cli.sh --connect --file=configure-server.cli
        
   This script configures and starts multiple servers needed to run this quickstart. You should see the script commands echoed, followed by:
   
        The batch executed successfully.
        {"outcome" => "success"}


#### Configure the Server by Running the JBoss CLI Interactively

1. If it is running, stop the JBoss EAP server.
2. Backup the following files, replacing JBOSS_HOME with the path to your server: 

        JBOSS_HOME/domain/configuration/domain.xml
        JBOSS_HOME/domain/configuration/host.xml
3. Start the JBoss EAP server by typing the following: 

        For Linux:  JBOSS_HOME/bin/domain.sh 
        For Windows:  JBOSS_HOME\bin\domain.bat

   You should see two server processes when you execute the following command: `ps -ef|grep Server:server`
3. Open a new command line, navigate to the root directory of this quickstart, and run the following command, replacing JBOSS_HOME with the path to your server:

        JBOSS_HOME/bin/jboss-cli.sh --connect 

4. At the prompt, enter the following series of commands:

* First stop the default servers, block until the server is down

        /host=master/server-config=server-one:stop(blocking=true)
        /host=master/server-config=server-two:stop(blocking=true)

* Remove server-two from host master, and add again associated to "other-server-group"

        /host=master/server-config=server-two:remove()
        /host=master/server-config=server-two:add(auto-start=true, group="other-server-group",socket-binding-port-offset=150)

* Add the security realms with the secret (password base64) values for the server communication

        /host=master/core-service=management/security-realm=ejb-remote-call:add()
        /host=master/core-service=management/security-realm=ejb-remote-call/server-identity=secret:add(value="cXVpY2tzdGFydFB3ZDEh")

* Add the socket binding for connection from server-one to server-two

        /socket-binding-group=full-sockets/remote-destination-outbound-socket-binding=srv2srv-ejb-socket:add(host=localhost,port=4597)

        /profile=full/subsystem=remoting/remote-outbound-connection=ejb-outbound-connection:add(outbound-socket-binding-ref=srv2srv-ejb-socket, security-realm=ejb-remote-call, username="quickstartUser")
        /profile=full/subsystem=remoting/remote-outbound-connection=ejb-outbound-connection/property=SASL_POLICY_NOANONYMOUS:add(value=false)
        /profile=full/subsystem=remoting/remote-outbound-connection=ejb-outbound-connection/property=SSL_ENABLED:add(value=false)

* Add the datasource to full profile

        /profile=full/subsystem=datasources/data-source=SecurityPropagationDS:add(connection-url="jdbc:h2:tcp://localhost/qs_ejb",jta=false,jndi-name="java:jboss/datasources/SecurityPropagationDS", use-ccm=false,driver-class="org.h2.Driver", driver-name="h2",user-name="sa")

* Add the datasource to full-ha profile

        /profile=full-ha/subsystem=datasources/data-source=SecurityPropagationDS:add(connection-url="jdbc:h2:tcp://localhost/qs_ejb",jta=false,jndi-name="java:jboss/datasources/SecurityPropagationDS", use-ccm=false,driver-class="org.h2.Driver", driver-name="h2",user-name="sa")

* Add the security domain to full profile

        /profile=full/subsystem=security/security-domain=security-propagation-quickstart:add(cache-type=default)
        /profile=full/subsystem=security/security-domain=security-propagation-quickstart/authentication=classic:add(login-modules=[{code=>"Database", flag=>required, module-options=>{"dsJndiName"=>"java:jboss/datasources/SecurityPropagationDS","principalsQuery"=>"SELECT PASSWORD FROM USERS WHERE USERNAME = ?","rolesQuery"=>"SELECT R.NAME, 'Roles' FROM USERS_ROLES UR INNER JOIN ROLES R ON R.ID = UR.ROLE_ID INNER JOIN USERS U ON U.ID = UR.USER_ID WHERE U.USERNAME = ?"}}])

* Add the security domain to full-ha profile

        /profile=full-ha/subsystem=security/security-domain=security-propagation-quickstart:add(cache-type=default)
        /profile=full-ha/subsystem=security/security-domain=security-propagation-quickstart/authentication=classic:add(login-modules=[{"code"=>"org.jboss.as.quickstarts.ejb.security.interceptors.DelegationLoginModule", flag=>optional, module=>"org.jboss.as.quickstarts.ejb.security.propagation", "module-options"=>{"password-stacking"=>"useFirstPass"}},{"code"=>"Remoting", flag=>optional, module-options=>{"password-stacking"=>"useFirstPass"}},{"code"=>"Database", flag=>required, module-options=>{"dsJndiName"=>"java:jboss/datasources/SecurityPropagationDS","principalsQuery"=>"SELECT PASSWORD FROM USERS WHERE USERNAME = ?","rolesQuery"=>"SELECT R.NAME, 'Roles' FROM USERS_ROLES UR INNER JOIN ROLES R ON R.ID = UR.ROLE_ID INNER JOIN USERS U ON U.ID = UR.USER_ID WHERE U.USERNAME = ?","password-stacking"=>"useFirstPass"}}])

* Start servers

        /host=master/server-config=server-one:start(blocking=true)
        /host=master/server-config=server-two:start(blocking=true)


#### Configure the Server by Manually Editing the Server Configuration File

1. If it is running, stop the JBoss server.
2. Backup the following files, replacing JBOSS_HOME with the path to your server: 

        JBOSS_HOME/domain/configuration/domain.xml
        JBOSS_HOME/domain/configuration/host.xml
3. Open the file: `JBOSS_HOME/domain/configuration/domain.xml`
4. Make the additions described below.

#####  Manually Configure the Server Groups

1. If it is running, stop the JBoss server.
2. Open the `host.xml` file and find the 'servers' section. For the `server-two`, associate the server-group to "other-server-group". Save it. The result should be:

        <server name="server-one" group="main-server-group">
        </server>
        <server name="server-two" group="other-server-group" auto-start="true">
            <socket-bindings port-offset="150"/>
        </server>

##### Manually Configure the JBoss Client to JBoss Server Authentication

1. Open host.xml and find the 'security-realm' section.
2. Add the following security realm, after "ApplicationRealm"

        <security-realm name="ejb-remote-call">
            <server-identities>
                <secret value="cXVpY2tzdGFydFB3ZDEh"/>
            </server-identities>
        </security-realm> 

        `cXVpY2tzdGFydFB3ZDEh` is the base64 format for `quickstartPwd1!` password. You can also generate it with `echo -n 'quickstartPwd1!' | base64`


##### Manually Configure the Server Profiles

1. Open the `domain.xml` file and find the "full-sockets" socket binding group.
2. Add the following outbound-socket-binding

        <outbound-socket-binding name="srv2srv-ejb-socket">
            <remote-destination host="localhost" port="4597"/>
        </outbound-socket-binding> 

3. Find the "remoting" subsystem of "full" profile and add the relevant section as show below

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

4. Add the datasource `SecurityPropagationDS` to the full" and full-ha" profiles. 
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

5. Add the `security-propagation-quickstart` login-module in the security-domain subsystem of full profile.
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

6. Add the following `security-propagation-quickstart` login-module in the security-domain subsystem of full-ha profile.

        <security-domain name="security-propagation-quickstart" cache-type="default">
            <authentication>
                <login-module code="org.jboss.as.quickstarts.ejb.security.interceptors.DelegationLoginModule" flag="optional" module="org.jboss.as.quickstarts.ejb.security.propagation">
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
    
        The security-propagation-quickstart for the full-ha profile contains additional login-modules as they are necessary for the security context propagation.

Create the Module for the ejb-propagation-interceptor.jar
-------------------------

Now you must generate the `ejb-propagation-interceptor.jar` file and add it as a module to the JBoss server.

1. Create the directory structure in the JBoss server `modules` directory. Be sure to replace JBOSS_HOME with the path to your server.

        mkdir -p JBOSS_HOME/modules/system/layers/base/org/jboss/as/quickstarts/ejb/security/propagation/main

2. Navigate to the root directory of this quickstart and copy the `module.xml` file to the server's `module/` directory structure:

        cp interceptor-module/module.xml JBOSS_HOME/modules/system/layers/base/org/jboss/as/quickstarts/ejb/security/propagation/main/
 
   
Build the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Open a command line and navigate to the root directory of this quickstart.
2. Type this command to build the archive:

        mvn clean install

3. Type the following command to copy the interceptor JAR to the server's `module/` directory structure, replacing JBOSS_HOME with the path to your server:

        cp interceptor-module/target/jboss-ejb-security-propagation-interceptor.jar JBOSS_HOME/modules/system/layers/base/org/jboss/as/quickstarts/ejb/security/propagation/main

4. If JBoss is running, restart it, so the module can be loaded.

Start the JBoss Server
-------------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server. Be sure to replace JBOSS_HOME with the path to your server.

        For Linux:   JBOSS_HOME/bin/domain.sh
        For Windows: JBOSS_HOME\bin\domain.bat
   You should see two server processes when you execute the following command: `ps -ef|grep Server:server`

Deploy the Quickstart
-------------------------

1. Open a command line and navigate to the to the root directory of this quickstart.
2. Type the following commands to deploy the quickstart, replacing JBOSS_HOME with the path to your server:

        JBOSS_HOME/bin/jboss-cli.sh --connect --command="deploy ejb/target/jboss-ejb-security-propagation-ejb.jar  --server-groups=other-server-group"
        JBOSS_HOME/bin/jboss-cli.sh --connect --command="deploy web/target/jboss-ejb-security-propagation-web.war --server-groups=main-server-group"

Access the application
---------------------

1. When you deploy the quickstart (or start the server-one after deployment) it should display in server-one log the registration for the ClientSecurityInterceptor as

        >>>>>>>>>> ClientSecurityInterceptor Constructor

2. When you deploy EJB it shows the JNDI names for HelloEJB and SecuredEJB.

3. Access the application at the following URL: <http://localhost:8080/jboss-ejb-security-propagation-web/hello>

    It displays a message "Successfully called Hello EJB", showing the remote access is working. This is a non-protected servlet that calls a non-protected EJB. The response is:

        The response is
        Hello Principal : anonymous
        Hello Remote User : null
        Hello Authentication Type : null

   The revelant section of the server log shows:

        [Server:server-one] ejb: Proxy for remote EJB StatelessEJBLocator{appName='', moduleName='jboss-as-propagation-ejb', distinctName='', beanName='HelloEJB', view='interface org.jboss.as.quickstarts.ejb.security.propagation.Hello'}
        [Server:server-one] >>>>>>>>>>>> principal: null
        [Server:server-two]  ==> EJB principal: anonymous

4. Now access the application at the following URL: <http://localhost:8080/jboss-ejb-security-propagation-web/secure_ejb>

   This time, it prompts for a user and password. Enter:

        user    : admin
        password: admin123 

   The browser displays:

        Successfully called Secured EJB
        Principal : admin
        Remote User : admin
        Authentication Type : BASIC

   The revelant section of the `server-two` log shows:

        [Server:server-one] ejb: Proxy for remote EJB StatelessEJBLocator{appName='', moduleName='jboss-as-propagation-ejb', distinctName='', beanName='SecuredEJB', view='interface org.jboss.as.quickstarts.ejb.security.propagation.Secured'}
        [Server:server-one] >>>>>>>>>>>> principal: admin
        [Server:server-two] >>>>>>>>>> contextData {TestDelegationUser=admin}
        [Server:server-two] >>>>>>>>>>>>> Switch users 
        [Server:server-two] >>>>>>>>>> delegationAcceptable: admin

   The user is authenticated to the servlet in the "security-propagation-quickstart" security domain, before the EJB call. The EJB client interceptor sends the credentials to the target JBoss server.

    On the EJB side, the EJB server interceptor receives the credential, switches the EJB authenticated user from "quickstartUser" to "admin", the "security-propagation-quickstart" security domain calls `DelegationLoginModule` that checks the credential received from EJB server interceptor and grants access. 


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive, replacing JBOSS_HOME with the path to your server:

        JBOSS_HOME/bin/jboss-cli.sh --connect --command="undeploy jboss-ejb-security-propagation-web.war --all-relevant-server-groups "
        JBOSS_HOME/bin/jboss-cli.sh --connect --command="undeploy jboss-as-propagation-ejb.jar --all-relevant-server-groups "


Remove the Security Domain Configuration
--------------------

You can remove the security domain configuration by manually restoring the back-up copy the configuration file. 

### Remove the Security Domain Configuration Manually           
1. If it is running, stop the JBoss server.
2. Restore the `JBOSS_HOME/domain/configuration/domain.xml` and `JBOSS_HOME/domain/configuration/host.xml` files with the back-up copies of the files. Be sure to replace JBOSS_HOME with the path to your server.


### Remove the Security Domain Configuration by Running the JBoss CLI Script

1. Start the JBoss server by typing the following: 

        For Linux:   JBOSS_HOME/bin/domain.sh
        For Windows: JBOSS_HOME\bin\domain.bat
2. Open a new command line, navigate to the root directory of this quickstart, and run the following command, replacing JBOSS_HOME with the path to your server:

        JBOSS_HOME/bin/jboss-cli.sh --connect --file=remove-configuration.cli 
This script removes the server configuration that was done by the `configure-server.cli` script. You should see the following result when you run the script:

        #1 /profile=full/subsystem=remoting/remote-outbound-connection=ejb-outbound-connection:remove
        #2 /socket-binding-group=full-sockets/remote-destination-outbound-socket-binding=srv2srv-ejb-socket:remove
        #3 /profile=full/subsystem=datasources/data-source=SecurityPropagationDS:remove
        #4 /profile=full-ha/subsystem=datasources/data-source=SecurityPropagationDS:remove
        #5 /host=master/core-service=management/security-realm=ejb-remote-call:remove
        #6 /profile=full/subsystem=security/security-domain=security-propagation-quickstart:remove
        #7 /profile=full-ha/subsystem=security/security-domain=security-propagation-quickstart:remove
        The batch executed successfully.
3. Restart the JBoss server, as described above, for the changes to take effect.

Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------

You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
