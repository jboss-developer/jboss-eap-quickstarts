servlet-security:  Using JEE Declarative Security to Control Access to Servlet 3
====================
Author: Sherif Makary, RH MW SA

This example demonstrates the use of JEE declarative security to control access to Servlets and Security in JBoss AS7 and JBoss Enterprise Application Platform 6.

The example can be deployed using Maven from the command line or from Eclipse using JBoss Tools.

To implement web security, you need to:

1. Add a security-domain to your jboss-web.xml, please refer to the /webapp/WEB-INF/jboss-web.xml for the security domain xml  
2. Configure a security domain in standalone.xml
3. Have users.properties and roles.properties files in WEB-INF/classes directory of your web application
4. Add a security-constraints to, please refer to the /webapp/WEB-INF/web.xml for the security-constraints xml

To implement Servlet declarative security, you need to:

1. Add security annotations to your Servlet declaration
2. Make sure the allowed user role is the same as the role defined in roles.properties file
3. Make sure the security domain referenced in the jboss-web.xml is defined in the JBoss AS standalone.xml configuration file. Find the `<subsystem xmlns="urn:jboss:domain:security:1.1">` and copy the following XML snippet into the `<security-domains>` section:

             <security-domain name="WebSecurityBasic" cache-type="required">
                 <authentication>
                     <login-module code="UsersRoles" flag="required">
                         <module-option name="usersProperties" value="users.properties"/>
                         <module-option name="rolesProperties" value="roles.properties"/>
                     </login-module>
                 </authentication>
             </security-domain>

For more information, refer to the  <a href="https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a> and find Security --> Servlet Security.


## Deploying the Quickstart

First you need to start JBoss AS 7 (or JBoss Enterprise Application Platform 6). To do this, run

    $JBOSS_HOME/bin/standalone.sh

or if you are using Windows

    $JBOSS_HOME/bin/standalone.bat

To deploy the application, you first need to produce the archive:

    mvn clean package


You can now deploy the artifact to JBoss AS by executing the following command:

                mvn jboss-as:deploy

This will deploy `target/jboss-as-servlet-security` to the running instance of JBoss AS.

## Testing the Quickstart

The application will be running at the following URL <http://localhost:8080/jboss-as-servlet-security/SecuredServlet/>.

When you access the application, you should get a browser login challenge.

After a successful login using admin/admin, the browser will display the following security info:

                Successfully called Secured Servlet

				Principal : admin
				Remote User : admin
				Authentication Type : BASIC

Change the role in the quickstart /src/main/webapp/WEB-INF/classes/roles.properties files to 'gooduser1'. 
Rebuild the application using by typing the following command:

                mvn clean package

Re-deploy the application by typing:

                mvn jboss-as:deploy

Refresh the browser, clear the active login, and you should get a security exception similar to the following: 

                HTTP Status 403 - Access to the requested resource has been denied

                type Status report
                message Access to the requested resource has been denied
                description Access to the specified resource (Access to the requested resource has been denied) has been forbidden.
