ejb-security:  Using Java EE Declarative Security to Control Access to EJB 3
====================
Author: Sherif F. Makary  
Level: Intermediate  
Technologies: EJB, Security  
Summary: Shows how to use Java EE Declarative Security to Control Access to EJB 3  
Target Product: EAP  
Product Versions: EAP 6.1, EAP 6.2  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

This example demonstrates the use of Java EE declarative security to control access to Servlets and EJBs in Red Hat JBoss Enterprise Application Platform.

This quickstart takes the following steps to implement EJB security:

1. Define the security domain. This can be done either in the `security` subsytem of the `standalone.xml` configuration file or in the `WEB-INF/jboss-web.xml` configuration file. This quickstart uses the `other` security domain which is provided by default in the `standalone.xml` file:

        <security-domain name="other" cache-type="default">
            <authentication>
                <login-module code="Remoting" flag="optional">
                    <module-option name="password-stacking" value="useFirstPass"/>
                </login-module>
                <login-module code="RealmDirect" flag="required">
                    <module-option name="password-stacking" value="useFirstPass"/>
                </login-module>
            </authentication>
        </security-domain>

2. Add the `@SecurityDomain("other")` security annotation to the EJB declaration to tell the EJB container to apply authorization to this EJB.
3. Add the `@RolesAllowed({ "guest" })` annotation to the EJB declaration to authorize access only to users with `guest` role access rights.
4. Add the `@RolesAllowed({ "guest" })` annotation to the Servlet declaration to authorize access only to users with `guest` role access rights.
5. Add a `<login-config>` security constraint to the `WEB-INF/web.xml` file to force the login prompt.
6. Add an application user with `guest` role access rights to the EJB. This quickstart defines a user `quickstartUser` with password `quickstartPwd1!` in the `guest` role. The `guest` role matches the allowed user role defined in the `@RolesAllowed` annotation in the EJB.
7. Add a second user that has no `guest` role access rights.


System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.1 or later. 

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.


Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.


Add the Application Users
---------------

Using the add-user utility script, you must add the following users to the `ApplicationRealm`:

| **UserName** | **Realm** | **Password** | **Roles** |
|:-----------|:-----------|:-----------|:-----------|
| quickstartUser| ApplicationRealm | quickstartPwd1!| guest |
| user1 | ApplicationRealm | password1! | app-user |

The first application user has access rights to the application. The second application user is not authorized to access the application.

To add the application users, open a command prompt and type the following commands:

        For Linux:        
          JBOSS_HOME/bin/add-user.sh -a -u 'quickstartUser' -p 'quickstartPwd1!' -g 'guest'
          JBOSS_HOME/bin/add-user.sh -a -u 'user1' -p 'password1!' -g 'app-user'

        For Windows: 
          JBOSS_HOME\bin\add-user.bat  -a -u 'quickstartUser' -p 'quickstartPwd1!' -g 'guest'
          JBOSS_HOME\bin\add-user.bat -a -u 'user1' -p 'password1!' -g 'app-user'

If you prefer, you can use the add-user utility interactively. 
For an example of how to use the add-user utility, see instructions in the root README file located here: [Add an Application User](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CREATE_USERS.md#add-an-application-user).


Start the JBoss EAP Server
-------------------------

1. Open a command prompt and navigate to the root of the JBoss EAP directory.
2. The following shows the command line to start the server:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install jboss-as:deploy

4. This will deploy `target/jboss-ejb-security.war` to the running instance of the server.


Access the application 
---------------------

The application will be running at the following URL <http://localhost:8080/jboss-ejb-security/>.

When you access the application, you are presented with a browser login challenge. 

1. If you attempt to login with a user name and password combination that has not been added to the server, the login challenge will be redisplayed.
2. When you login successfully using `quickstartUser`/`quickstartPwd1!`, the browser displays the following security info:

        Successfully called Secured EJB

        Principal : quickstartUser
        Remote User : quickstartUser
        Authentication Type : BASIC
        
3. Now close and reopen the brower session and access the application using the `user1`/`password1!` credentials. In this case, the Servlet, which only allows the `guest` role, restricts the access and you get a security exception similar to the following: 

        HTTP Status 403 - Access to the requested resource has been denied

        type Status report
        message Access to the requested resource has been denied
        description Access to the specified resource (Access to the requested resource has been denied) has been forbidden.

4. Next, change the EJB (SecuredEJB.java) to a different role, for example, `@RolesAllowed({ "other-role" })`. Do not modify the `guest` role in the Servlet (SecuredEJBServlet.java). Build and redeploy the quickstart, then close and reopen the browser and login using `quickstartUser`/`quickstartPwd1!`. This time the Servlet will allow the `guest` access, but the EJB, which only allows the role `other-role`, will throw an EJBAccessException:

        HTTP Status 500

        message
        description  The server encountered an internal error () that prevented it from fulfilling this request.
        exception
        javax.ejb.EJBAccessException: JBAS014502: Invocation on method: public java.lang.String org.jboss.as.quickstarts.ejb_security.SecuredEJB.getSecurityInfo() of bean: SecuredEJB is not allowed



Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JDBS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
