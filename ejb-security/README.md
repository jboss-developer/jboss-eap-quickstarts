EJB Security Example

Author: Sherif Makary, RH MW SA

This example demonstrates the use of JEE declarative security to control access to EJB 3 and Security in JBoss EAP 6

The example can be deployed using Maven from the command line or from Eclipse using JBoss Tools.

To set up Maven or JBoss Tools in Eclipse, refer to the Getting Started Developing Applications Guide.

To deploy to JBoss AS 7, start JBoss AS 7 and type mvn package jboss-as:deploy. The application is deployed to http://localhost:8080/jboss-as-secured-servlet/CallSecuredEJBServlet. You can read more details in the Getting Started Developing Applications Guide.

To implement EJB security, you need to:
-Add a security-domain to your jboss-web.xml
-Configure a security domain in standalone.xml
-Have users.properties and roles.properties files in WEB-INF/classes directory of your web application

To implement EJB declerative security, you need to:
-Add security annotations to your EJB declaration
-Make sure the allowed user role is the same as the role defined in roles.properties file
-Make sure the security domain referenced in jboss-web.xml is defined in the EAP 6 standalone.xml, this is the configuration snipt:

<security-domain name="WebSecurityBasic" cache-type="required"></pre>

<authentication>

<login-module code="UsersRoles" flag="required">

<module-option name="usersProperties" value="users.properties"/>

<module-option name="rolesProperties" value="roles.properties"/>

</login-module>

</authentication>

</security-domain>

For references, please refer to:

Getting Started Developing Applications Guide.
JBoss AS7: Security : EJB3 Security.

Test Scenario:
-After successful war deployment to EAP 6
-Run the url http://localhost:8080/jboss-as-ejb-security/CallSecuredEJBServlet
-You should get a browser log-in challenge
-After successful login using admin/admin, the browser will display some security info:

"Successfully called Secured EJB

Principal : admin

Remote User : admin

Authentication Type : BASIC
"


-Change the role in roles.properties to "gooduser1"
-Redeploy the war and refresh the browser and clear the active login and you should get a security exception. 
