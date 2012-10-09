servlet-security:  Using Java EE Declarative Security to Control Access to Servlet 3
====================
Author: Sherif F. Makary
Level: Intermediate
Technologies: Servlet, Security
Summary: Demonstrates how to use Java EE declarative security to control access to Servlet 3
Target Product: EAP

What is it?
-----------

This example demonstrates the use of Java EE declarative security to control access to Servlets and Security in JBoss Enterprise Application Platform 6 and  JBoss AS7.

When you deploy this example, one user is automatically created for you: guest. This data is located in the src/main/resources/import.sql file. The guest user password is guest.

This quickstart takes the following steps to implement Servlet security:

1. Define a security domain in the `standalone.xml` configuration file using the Database JAAS LoginModule.
2. Add an application user with access rights to the application.
3. Add a security domain reference to `WEB-INF/jboss-web.xml`.
4. Add a security constraint to the `WEB-INF/web.xml` .
5. Add a security annotation to the EJB declaration.


Please note the allowed user role `guest` in the annotation -`@RolesAllowed`- is the same as the user role defined in step 2


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 


Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Define a security domain using the Database JAAS Login Module
---------------

This quickstart authenticates users using a simple database setup. The datasource configuration is located at /src/main/webapp/WEB-INF/servlet-security-quickstart-ds.xml.

To configure the security domain add the configuration bellow to the standalone.xml (security subsystem section):

	<security-domain name="servlet-security-quickstart" cache-type="default">
    	<authentication>
        	<login-module code="Database" flag="required">
            	<module-option name="dsJndiName" value="java:jboss/datasources/ServletSecurityDS"/>
                <module-option name="principalsQuery" value="SELECT PASSWORD FROM USERS WHERE USERNAME = ?"/>
                <module-option name="rolesQuery" value="SELECT R.NAME, 'Roles' FROM USERS_ROLES UR INNER JOIN ROLES R ON R.ID = UR.ROLE_ID INNER JOIN USERS U ON U.ID = UR.USER_ID WHERE U.USERNAME = ?"/>
            </login-module>
        </authentication>
    </security-domain>
    
Please note that the security domain name (servlet-security-quickstart) must match the one defined in the /src/main/webapp/WEB-INF/jboss-web.xml.

Start JBoss Enterprise Application Platform 6 or JBoss AS 7 with the Web Profile
-------------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package jboss-as:deploy

4. This will deploy `target/jboss-as-servlet-security.war` to the running instance of the server.


Access the application 
---------------------

The application will be running at the following URL <http://localhost:8080/jboss-as-servlet-security/>.

When you access the application, you should get a browser login challenge. 

After a successful login using username `guest` and password `guest`, the browser will display the following security info:

    Successfully called Secured Servlet

    Principal : guest
    Remote User : guest
    Authentication Type : BASIC

You can now change the role in the quickstart `src/main/resources/import.sql` files to `notauthorized`. You can do that by changing the following like:

	INSERT INTO ROLES (ID, NAME) VALUES (1, 'guest') to INSERT INTO ROLES (ID, NAME) VALUES (1, 'notauthorized');

Rebuild and redeploy the quickstart following the instructions under **Build and Deploy the Archive** above.

Refresh the browser, clear the active login, and you should get a security exception similar to the following: 

    HTTP Status 403 - Access to the requested resource has been denied

    type Status report
    message Access to the requested resource has been denied
    description Access to the specified resource (Access to the requested resource has been denied) has been forbidden.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

      mvn dependency:sources
      mvn dependency:resolve -Dclassifier=javadoc
