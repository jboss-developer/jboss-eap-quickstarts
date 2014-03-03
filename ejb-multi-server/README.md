ejb-multi-server: EJB Communication Across Servers
======================================================
Author: Wolf-Dieter Fink  
Level: Advanced  
Technologies: EJB, EAR  
Summary: EJB applications deployed to different servers that communicate via EJB remote calls  
Target Product: EAP  
Product Versions: EAP 6.1, EAP 6.2  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  


What is it?
-----------

This quickstart demonstrates communication between applications deployed to different servers. Each application is deployed as an EAR and contains a simple EJB3.1 bean. The only function of each bean is to log the invocation.

This example consists of the following Maven projects, each with a shared parent:

| **Sub-project** | **Description** |
|:-----------|:-----------|
| `app-main` | An application that can be called by the `client`. It can also call the different sub-applications. |
| `app-one` and `app-two` | These are simple applications that contain an EJB sub-project to build the `ejb.jar` file and an EAR sub-project to build the `app.ear` file. Each application contains only one EJB that logs a statement on a method call and returns the `jboss.node.name` and credentials. |
| `app-web` |  A simple WAR application. It consists of one Servlet that demonstrates how to invoke EJBs on a different server. | 
| `client` | This project builds the standalone client and executes it.|

The root `pom.xml` builds each of the subprojects in an appropriate order.

The server configuration is done using CLI batch scripts located in the root of the quickstart folder.



System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.1 or later.

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.


Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.


Start with a Clean JBoss EAP Install
--------------------------------------

It is important to start with a clean version of JBoss EAP before testing this quickstart. Be sure to unzip or install a fresh JBoss EAP instance. 


Add the Application Users
---------------

The following users must be added to the `ApplicationRealm` to run this quickstart. Be sure to use the names and passwords specified in the table as they are required to run this example.

| **UserName** | **Realm** | **Password** | **Roles** |
|:-----------|:-----------|:-----------|:-----------|
| quickuser| ApplicationRealm | quick-123 | _leave blank for none_ |
| quickuser1 | ApplicationRealm | quick123+ | _leave blank for none_ |
| quickuser2 | ApplicationRealm | quick+123 | _leave blank for none_ |

To add the users, open a command prompt and type the following commands:

        For Linux:
            JBOSS_HOME/bin/add-user.sh -a -u quickuser -p quick-123
            JBOSS_HOME/bin/add-user.sh -a -u quickuser1 -p quick123+
            JBOSS_HOME/bin/add-user.sh -a -u quickuser2 -p quick+123

        For Windows:
            JBOSS_HOME\bin\add-user.bat -a -u quickuser -p quick-123
            JBOSS_HOME\bin\add-user.bat -a -u quickuser1 -p quick123+
            JBOSS_HOME\bin\add-user.bat -a -u quickuser2 -p quick+123

If you prefer, you can use the add-user utility interactively. For an example of how to use the add-user utility, see instructions in the root README file located here: [Add an Application User](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CREATE_USERS.md#add-an-application-user).


Configure the JBoss EAP Server
---------------------------

You configure the domain server by running JBoss CLI commands. For your convenience, this quickstart batches the commands into a `install-domain.cli` script provided in the root directory of this quickstart. 

1. Start with a fresh instance of the JBoss EAP as noted above under [Start with a Clean JBoss EAP Install](#start-with-a-clean-jboss-eap-install).

2. Be sure you add the required users as specified above under [Add the Application Users](#add-the-application-users). 

3. Before you begin, back up your server configuration files.
    * If it is running, stop the JBoss EAP server.
    * Backup the following files, replacing JBOSS_HOME with the path to your server: 

            JBOSS_HOME/domain/configuration/domain.xml
            JBOSS_HOME/domain/configuration/host.xml        
    * After you have completed testing and undeployed this quickstart, you can replace these files to restore the server to its original configuration.
4.  Start the JBoss EAP server 
    * Open a command prompt and navigate to the root of the EAP directory. 
    * Start the server using the following command:

            bin/domain.sh    
5. Review the `install-domain.cli` file in the root of this quickstart directory. This script configures and starts multiple servers needed to run this quickstart. 

6. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing JBOSS_HOME with the path to your server:
 
        JBOSS_HOME/bin/jboss-cli.sh --connect --file=install-domain.cli
     You should see the following result when you run the script:

        #1 /host=master/server-config=server-one:stop(blocking=true)
        #2 /host=master/server-config=server-two:stop(blocking=true)
        #3 /host=master/server-config=server-one:remove
        #4 /host=master/server-config=server-two:remove
        #5 /host=master/server-config=server-three:remove
        #6 /server-group=main-server-group:remove
        #7 /server-group=other-server-group:remove
        #8 /host=master/core-service=management/security-realm=ApplicationRealm/authentication=local:remove
        #9 /profile=default/subsystem=ejb3:write-attribute(name=default-missing-method-permissions-deny-access, value=false)
        #10 /profile=ha/subsystem=ejb3:write-attribute(name=default-missing-method-permissions-deny-access, value=false)
        #11 /socket-binding-group=full-sockets/remote-destination-outbound-socket-binding=remote-ejb-1:add(host=localhost, port=4547)
        #12 /socket-binding-group=full-sockets/remote-destination-outbound-socket-binding=remote-ejb-2:add(host=localhost, port=4647)
        #13 /profile=full/subsystem=remoting/remote-outbound-connection=remote-ejb-connection-1:add(outbound-socket-binding-ref=remote-ejb-1, security-realm=ejb-security-realm-1, username=quickuser1)
        #14 /profile=full/subsystem=remoting/remote-outbound-connection=remote-ejb-connection-1/property=SASL_POLICY_NOANONYMOUS:add(value=false)
        #15 /profile=full/subsystem=remoting/remote-outbound-connection=remote-ejb-connection-1/property=SSL_ENABLED:add(value=false)
        #16 /profile=full/subsystem=remoting/remote-outbound-connection=remote-ejb-connection-2:add(outbound-socket-binding-ref=remote-ejb-2, security-realm=ejb-security-realm-2, username=quickuser2)
        #17 /profile=full/subsystem=remoting/remote-outbound-connection=remote-ejb-connection-2/property=SASL_POLICY_NOANONYMOUS:add(value=false)
        #18 /profile=full/subsystem=remoting/remote-outbound-connection=remote-ejb-connection-2/property=SSL_ENABLED:add(value=false)
        #19 /host=master/core-service=management/security-realm=ejb-security-realm-1:add()
        #20 /host=master/core-service=management/security-realm=ejb-security-realm-1/server-identity=secret:add(value=cXVpY2sxMjMr)
        #21 /host=master/core-service=management/security-realm=ejb-security-realm-2:add()
        #22 /host=master/core-service=management/security-realm=ejb-security-realm-2/server-identity=secret:add(value=cXVpY2srMTIz)
        #23 /host=master/jvm=default:write-attribute(name=permgen-size, value=64m)
        #24 /host=master/jvm=default:write-attribute(name=max-permgen-size, value=128m)
        #25 /server-group=quickstart-ejb-multi-main-server:add(profile=full,socket-binding-group=full-sockets)
        #26 /server-group=quickstart-ejb-multi-main-server/jvm=default:add()
        #27 /host=master/server-config=app-main:add(auto-start=true, group=quickstart-ejb-multi-main-server)
        #28 /server-group=quickstart-ejb-multi-appOne-server:add(profile=ha,socket-binding-group=ha-sockets)
        #29 /server-group=quickstart-ejb-multi-appOne-server/jvm=default:add()
        #30 /host=master/server-config=app-oneA:add(auto-start=true, group=quickstart-ejb-multi-appOne-server, socket-binding-port-offset=100)
        #31 /host=master/server-config=app-oneB:add(auto-start=true, group=quickstart-ejb-multi-appOne-server, socket-binding-port-offset=700)
        #32 /server-group=quickstart-ejb-multi-appTwo-server:add(profile=default,socket-binding-group=standard-sockets)
        #33 /server-group=quickstart-ejb-multi-appTwo-server/jvm=default:add()
        #34 /host=master/server-config=app-twoA:add(auto-start=true, group=quickstart-ejb-multi-appTwo-server, socket-binding-port-offset=200)
        #35 /host=master/server-config=app-twoB:add(auto-start=true, group=quickstart-ejb-multi-appTwo-server, socket-binding-port-offset=800)
        #36 /profile=full/subsystem=naming/binding=java:global/AliasAppTwo:global\/AliasAppTwo:add(binding-type=lookup, lookup="ejb:jboss-ejb-multi-server-app-two/ejb//AppTwoBean!org.jboss.as.quickstarts.ejb.multi.server.app.AppTwo")
        #37 /profile=default-web:add()
        #38 /profile=default-web/subsystem=logging:add()
        #39 /profile=default-web/subsystem=logging/periodic-rotating-file-handler=FILE:add(file={"relative-to"=>"jboss.server.log.dir", "path"=>"server.log"},append=true,suffix=.yyyy-MM-dd,formatter="%d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%E%n")
        #40 /profile=default-web/subsystem=logging/logger=com.arjuna:add(level=WARN)
        #41 /profile=default-web/subsystem=logging/logger=org.apache.tomcat.util.modeler:add(level=WARN)
        #42 /profile=default-web/subsystem=logging/logger=sun.rmi:add(level=WARN)
        #43 /profile=default-web/subsystem=logging/logger=jacorb:add(level=WARN)
        #44 /profile=default-web/subsystem=logging/logger=jacorb.config:add(level=ERROR)
        #45 /profile=default-web/subsystem=logging/root-logger=ROOT:add(level=INFO,handlers=["FILE"])
        #46 /profile=default-web/subsystem=configadmin:add()
        #47 /profile=default-web/subsystem=ee:add(spec-descriptor-property-replacement=false,jboss-descriptor-property-replacement=true)
        #48 /profile=default-web/subsystem=ejb3:add()
        #49 /profile=default-web/subsystem=ejb3:write-attribute(name=default-slsb-instance-pool,value=slsb-strict-max-pool)
        #50 /profile=default-web/subsystem=ejb3:write-attribute(name=default-sfsb-cache,value=simple)
        #51 /profile=default-web/subsystem=ejb3:write-attribute(name=default-stateful-bean-access-timeout, value=5000)
        #52 /profile=default-web/subsystem=ejb3:write-attribute(name=default-singleton-bean-access-timeout, value=5000)
        #53 /profile=default-web/subsystem=ejb3/strict-max-bean-instance-pool=slsb-strict-max-pool:add(max-pool-size=20,timeout=5,timeout-unit=MINUTES)
        #54 /profile=default-web/subsystem=ejb3/strict-max-bean-instance-pool=mdb-strict-max-pool:add(max-pool-size=20,timeout=5,timeout-unit=MINUTES)
        #55 /profile=default-web/subsystem=ejb3/cache=simple:add(aliases=["NoPassivationCache"])
        #56 /profile=default-web/subsystem=ejb3/cache=passivating:add(passivation-store=file,aliases=["SimpleStatefulCache"])
        #57 /profile=default-web/subsystem=ejb3/file-passivation-store=file:add
        #58 /profile=default-web/subsystem=ejb3/service=async:add(thread-pool-name=default)
        #59 /profile=default-web/subsystem=ejb3/service=timer-service:add(thread-pool-name=default,path=timer-service-data,relative-to=jboss.server.data.dir)
        #60 /profile=default-web/subsystem=ejb3/service=remote:add(connector-ref=remoting-connector,thread-pool-name=default)
        #61 /profile=default-web/subsystem=ejb3/thread-pool=default:add(max-threads=10,keepalive-time={"time"=>"100","unit"=>"MILLISECONDS"})
        #62 /profile=default-web/subsystem=jca:add()
        #63 /profile=default-web/subsystem=jca/archive-validation=archive-validation:add(enabled=true, fail-on-error=true, fail-on-warn=false)
        #64 /profile=default-web/subsystem=jca/bean-validation=bean-validation:add(enabled=true)
        #65 /profile=default-web/subsystem=jca/cached-connection-manager=cached-connection-manager:add(install=true)
        #66 /profile=default-web/subsystem=jca/workmanager=default:add(name=default)
        #67 /profile=default-web/subsystem=jca/workmanager=default/short-running-threads=default:add(core-threads=50,queue-length=50,max-threads=50,keepalive-time={"time"=>"10", "unit"=>"SECONDS"})
        #68 /profile=default-web/subsystem=jca/workmanager=default/long-running-threads=default:add(core-threads=50,queue-length=50,max-threads=50,keepalive-time={"time"=>"10", "unit"=>"SECONDS"})
        #69 /profile=default-web/subsystem=naming:add()
        #70 /profile=default-web/subsystem=naming/service=remote-naming:add
        #71 /profile=default-web/subsystem=remoting:add()
        #72 /profile=default-web/subsystem=remoting/connector=remoting-connector:add(socket-binding=remoting, security-realm=ApplicationRealm)
        #73 /profile=default-web/subsystem=remoting/remote-outbound-connection=remote-connection-war-ejb-1:add(outbound-socket-binding-ref=remote-war-1, security-realm=ejb-security-realm-1, username=quickuser1)
        #74 /profile=default-web/subsystem=remoting/remote-outbound-connection=remote-connection-war-ejb-1/property=SASL_POLICY_NOANONYMOUS:add(value=false)
        #75 /profile=default-web/subsystem=remoting/remote-outbound-connection=remote-connection-war-ejb-1/property=SSL_ENABLED:add(value=false)
        #76 /profile=default-web/subsystem=remoting/remote-outbound-connection=remote-connection-war-ejb-2:add(outbound-socket-binding-ref=remote-war-2, security-realm=ejb-security-realm-2, username=quickuser2)
        #77 /profile=default-web/subsystem=remoting/remote-outbound-connection=remote-connection-war-ejb-2/property=SASL_POLICY_NOANONYMOUS:add(value=false)
        #78 /profile=default-web/subsystem=remoting/remote-outbound-connection=remote-connection-war-ejb-2/property=SSL_ENABLED:add(value=false)
        #79 /profile=default-web/subsystem=security:add
        #80 /profile=default-web/subsystem=security/security-domain=other:add(cache-type=default)
        #81 /profile=default-web/subsystem=security/security-domain=other/authentication=classic:add(login-modules=[{"code"=>"Remoting","flag"=>"optional","module-options"=>[("password-stacking"=>"useFirstPass")]},{"code"=>"RealmDirect","flag"=>"required","module-options"=>[("password-stacking"=>"useFirstPass")]}])
        #82 /profile=default-web/subsystem=security/security-domain=jboss-web-policy:add(cache-type=default)
        #83 /profile=default-web/subsystem=security/security-domain=jboss-web-policy/authorization=classic:add(policy-modules=[{"code"=>"Delegating","flag"=>"required"}])
        #84 /profile=default-web/subsystem=threads:add
        #85 /profile=default-web/subsystem=transactions:add(socket-binding=txn-recovery-environment, status-socket-binding=txn-status-manager, default-timeout=300, process-id-uuid=true)
        #86 /profile=default-web/subsystem=web:add(default-virtual-server=default-host,native=false)
        #87 /profile=default-web/subsystem=web/connector=http:add(protocol=HTTP/1.1, scheme=http, socket-binding=http)
        #88 /profile=default-web/subsystem=web/virtual-server=default-host:add(enable-welcome-root=true, alias=["localhost","example.com"])
        #89 /socket-binding-group=standard-sockets-web:add(default-interface=public)
        #90 /socket-binding-group=standard-sockets-web/socket-binding=http:add(port=8080)
        #91 /socket-binding-group=standard-sockets-web/socket-binding=remoting:add(port=4447)
        #92 /socket-binding-group=standard-sockets-web/socket-binding=txn-recovery-environment:add(port=4712)
        #93 /socket-binding-group=standard-sockets-web/socket-binding=txn-status-manager:add(port=4713)
        #94 /socket-binding-group=standard-sockets-web/remote-destination-outbound-socket-binding=remote-war-1:add(host=localhost, port=4547)
        #95 /socket-binding-group=standard-sockets-web/remote-destination-outbound-socket-binding=remote-war-2:add(host=localhost, port=4647)
        #96 /server-group=quickstart-ejb-multi-appWeb-server:add(profile=default-web,socket-binding-group=standard-sockets-web)
        #97 /server-group=quickstart-ejb-multi-appWeb-server/jvm=default:add()
        #98 /host=master/server-config=app-web:add(auto-start=true, group=quickstart-ejb-multi-appWeb-server, socket-binding-port-offset=300)
        The batch executed successfully

        
    You should also see "outcome" => "success" for all of the commands. 

Review the Modified Server Configuration
-----------------------------------

There are too many additions to the configuration files to list here. Feel free to compare the `domain.xml` and `host.xml` to the backup copies to see the changes made to configure the server to run this quickstart.

Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy) for complete instructions and additional options._

1. Make sure you have started and configured the JBoss EAP server successfully   as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build the artifacts:

        mvn clean install
        
4. Open a new command prompt and navigate to the root directory of this quickstart. Deploy the applications using the provided CLI batch script by typing the following command:

        JBOSS_HOME/bin/jboss-cli.sh --connect --file=deploy-domain.cli
       
    This will deploy the app-*.ear files to different server-groups of the running domain.

 
_NOTE: If ERRORs appear in the server.log when the installing or deploying the quickstart, please stop the domain and restart it. This should ensure further steps run correctly._


Access the Remote Client Application
---------------------

This example shows how to invoke an EJB from a remote standalone application. 
It also demonstrates how to invoke an EJB from a client using a scoped-context rather than a properties file containing the parameters required by the InitialContext. 

1. Make sure that the deployments are successful as described above.
2. Navigate to the quickstart `client/` subdirectory.
3. Type this command to run the application:

        mvn exec:java

    The client will output the following information provided by the applications:
        
        InvokeAll succeed: MainApp[anonymous]@master:app-main  >  [ app1[anonymous]@master:app-oneA > app2[quickuser2]@master:app-twoA ; app2[quickuser2]@master:app-twoA ]

    This output shows that the `MainApp` is called with the user `anonymous` at node `master:app-main` and the sub-call is proceeded by the `master:app-oneA` node and `master:app-twoA` node as `quickuser2`. 
    
    Review the server log files to see the bean invocations on the servers.

4. To invoke the bean that uses the `scoped-client-context`, you must pass a property. Type the following command

        mvn exec:java -DUseScopedContext=true
    
    The invocation of `appTwo` throws a  `java.lang.reflect.InvocationTargetException` since the secured method is called and there is no Role for the user defined.  You get a `BUILD FAILURE` and the client outputs the following information:

        [ERROR] Failed to execute goal org.codehaus.mojo:exec-maven-plugin:1.2.1:java (default-cli) on project jboss-ejb-multi-server-client: An exception occured while executing the Java class. null: InvocationTargetException: JBAS014502: Invocation on method: public abstract java.lang.String org.jboss.as.quickstarts.ejb.multi.server.app.AppTwo.invokeSecured(java.lang.String) of bean: AppTwoBean is not allowed -> [Help 1]

    Update the user `quickuser1` and `quickuser2` and give them one of the Roles `AppTwo` or `Intern`.
    To update the roles, open a command prompt and type the following commands:

        For Linux:
              JBOSS_HOME/bin/add-user.sh -a -u quickuser1 -p quick123+ --role Intern
              JBOSS_HOME/bin/add-user.sh -a -u quickuser2 -p quick+123 --role AppTwo

        For Windows:
              JBOSS_HOME\bin\add-user.bat -a -u quickuser1 -p quick123+ --role Intern
              JBOSS_HOME\bin\add-user.bat -a -u quickuser2 -p quick+123 --role AppTwo

    If the connection was established before changing the roles it might be necessary to restart the main server, or even the whole domain.
    After that the invocation will be successful. The log output of the `appTwo` servers shows which Role is applied to the user. The output of the client will show you a simple line with the information provided by the different applications:
        
          InvokeAll succeed: MainAppSContext[anonymous]@master:app-main  >  [ {app1[quickuser1]@master:app-oneA, app1[quickuser1]@master:app-oneA, app1[quickuser2]@master:app-oneB, app1[quickuser2]@master:app-oneA, app1[quickuser2]@master:app-oneA, app1[quickuser2]@master:app-oneB, app1[quickuser2]@master:app-oneA, app1[quickuser2]@master:app-oneA} >  appTwo loop(7 time A-B expected){app2[quickuser1]@master:app-twoA, app2[quickuser2]@master:app-twoB, app2[quickuser1]@master:app-twoA, app2[quickuser2]@master:app-twoB, app2[quickuser1]@master:app-twoA, app2[quickuser2]@master:app-twoB, app2[quickuser1]@master:app-twoA, app2[quickuser2]@master:app-twoB, app2[quickuser1]@master:app-twoA, app2[quickuser2]@master:app-twoB, app2[quickuser1]@master:app-twoA, app2[quickuser2]@master:app-twoB, app2[quickuser1]@master:app-twoA, app2[quickuser2]@master:app-twoB, app2[quickuser1]@master:app-twoA, app2[quickuser2]@master:app-twoB} ]
         
    The resulting output in detail:
    * The client calls the `MainAppSContext` bean on the `app-main` server on host master with no application security.
    * The `MainAppSContext` bean in `app-main` calls the `AppOne` bean in `app-one` using the scoped-context and establishes the clustered view.
        * It initially connects using `quickuser1`
        * The clustered view is created using `quickuser2`. This takes some time, but once it takes effect, all calls are load-balanced.
    * The calls to the 'AppTwo' bean in `app-two` are made using two different scoped-context settings and both are used alternately 7 times. This means the servers `app-twoA` and `app-twoB` are called alternately seven times each.

5. If it is necessary to invoke the client with a different JBoss version the main class can be invoked by using the following command from the root directory of this quickstart. Replace $JBOSS_HOME with your current installation path. The output should be similar to the previous mvn executions.

        java -cp $JBOSS_HOME/bin/client/jboss-client.jar:app-main/ejb/target/jboss-ejb-multi-server-app-main-ejb-client.jar:app-two/ejb/target/jboss-ejb-multi-server-app-two-ejb-client.jar:client/target/jboss-ejb-multi-server-client.jar org.jboss.as.quickstarts.ejb.multi.server.Client


_NOTE:_
 
* _If exec is called multiple times, the invocation for `app1` might use `app-oneA` and `app-oneB` node due to cluster loadbalancing._
* _A new feature introduced in JBoss EAP 6.1 or later will deny the invocation of unsecured methods of `appOne`/`appTwo` since security is enabled but the method does not include @Roles. You need to set 'default-missing-method-permissions-deny-access = false' for the `ejb3` subsystem within the domain profile "ha" and "default" to allow the method invocation. See the install-domain.cli script._


Access the JSF application inside the main-application
---------------------

The JSF example shows different annotations to inject the EJB. Also how to handle the annotation if different beans implement the same interface and therefore the container is not able to decide which bean needs to be injected without additional informations.

1. Make sure that the deployments are successful as described above.
2. Use a browser to access the JSF application at the following URL: <http://localhost:8080/jboss-ejb-multi-server-app-main-web/>
3. Insert a message in the Text input and invoke the different methods. The result is shown in the browser.
4. See server logfiles and find your given message logged as INFO.

_NOTE :_

* _If you try to invoke `MainAppSContext` you need to update the user `quickuser1` and `quickuser2` and give them one of the Roles `AppTwo` or `Intern`._

Access the Servlet application deployed as a WAR inside a minimal server
---------------------

An example how to access EJB's from a separate instance which only contains a web application.

1. Make sure that the deployments are successful as described above.
2. Use a browser to access the Servlet at the following URL: <http://localhost:8380/jboss-ejb-multi-server-app-web/>
3. The Servlet will invoke the remote EJBs directly and show the results, compare that the invocation is successful

_NOTE : A new feature in EAP 6.1 or later will deny the invocation of unsecured methods of `appOne`/`appTwo` since security is enabled but the method does not include @Roles. You need to set 'default-missing-method-permissions-deny-access = false' for the `ejb3` subsystem within the domain profile "ha" and "default" to allow the method invocation.  See the install-domain.cli script._


Undeploy the Archives
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        JBOSS_HOME/bin/jboss-cli.sh --connect --file=undeploy-domain.cli


Remove the Server Domain Configuration
--------------------

You can remove the domain configuration by manually restoring the back-up copies the configuration files or by running the JBoss CLI Script. 

### Remove the Server Domain Configuration Manually           
1. If it is running, stop the JBoss EAP server.
2. Restore the `JBOSS_HOME/domain/configuration/domain.xml` and `JBOSS_HOME/domain/configuration/host.xml` files with the back-up copies of the files. Be sure to replace JBOSS_HOME with the path to your server.

### Remove the Security Domain Configuration by Running the JBoss CLI Script

_Note: This script returns the server to a default configuration and the result may not match the server configuration prior to testing this quickstart. If you were not running with the default configuration before testing this quickstart, you should follow the intructions above to manually restore the configuration to its previous state._

1. Start the JBoss EAP server by typing the following: 

        For Linux:   JBOSS_HOME/bin/domain.sh
        For Windows: JBOSS_HOME\bin\domain.bat
2. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing JBOSS_HOME with the path to your server.

        JBOSS_HOME/bin/jboss-cli.sh --connect --file=remove-configuration.cli 
This script removes the server configuration that was done by the `install-domain.cli` script. You should see the following result following the script commands:

        The batch executed successfully.

Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse) 

Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc

