 jboss-javaee6-webapp-src
 ++++++++++++++++++++++++

 #Source archetype:# jboss-javaee6-webapp

 What is it?
 ===========

 This is your project! It's a sample, deployable Maven 3 project to help you
 get your foot in the door developing with Java EE 6. This project is setup to
 allow you to create a compliant Java EE 6 application using JSF 2.0, CDI 1.0,
 EJB 3.1, JPA 2.0 and Bean Validation 1.0 that can run on a certified
 application server (Full or Web Profile). It includes a persistence unit and
 some sample persistence and transaction code to help you get your feet wet
 with database access in enterprise Java. 

 System requirements
 ===================

 All you need to build this project is Java 5.0 (Java SDK 1.5) or better, Maven
 3.0 or better. We strongly recommend, however, that you use Java 6.0 (Java SDK
 1.6).

 The application this project produces is designed to be run on a Java EE 6
 application server. We recommend JBoss AS 6.0.0.Final or GlassFish 3.1, though
 it should work on any server that is Java EE 6 compliant. In order to use it
 on GlassFish 3.1, you'll need to update Weld. See instructions below.
 
 NOTE:
 This project retrieves artifacts from the JBoss Community Maven repository, a
 superset of the Maven central repository, because there are certain Java EE
 API JARs that are not published to Maven central (for details, see the JIRA
 https://issues.jboss.org/browse/WELD-222). The integration testing
 framework used by the project, Arquillian, is also only available in the JBoss
 Community Maven repository.

 With the prerequisites out of the way, you're ready to build and deploy.

 Deploying the application
 =========================

 To deploy the application, you first need to produce the archive to deploy using
 the following Maven goal:

  mvn package

 If you want to deploy the application on JBoss AS (standalone), make sure that
 your JBOSS_HOME environment variable points to a JBoss AS 6 installation.

 Alternatively, you can set the location of JBoss AS using the following
 profile definition in the .m2/settings.xml file in your home directory:

  <?xml version="1.0" encoding="UTF-8"?>
  <settings
     xmlns="http://maven.apache.org/POM/4.0.0"
     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
  
     <profiles>
        <profile>
           <id>environment</id>
           <activation>
              <activeByDefault>true</activeByDefault>
           </activation>
           <properties>
              <jboss.home>/path/to/jboss-as-6.0.0.Final</jboss.home>
           </properties>
        </profile>
     </profiles>
     
  </settings>

 Once you've set either the JBOSS_HOME environment variable or the jboss.home
 Maven property, you can deploy to JBoss AS by executing the following command:

  mvn package jboss:hard-deploy

 This will deploy two artifacts, target/jboss-javaee6-webapp-src.war and
 default-ds.xml.
 
 IMPORTANT:
 Don't forget to deploy default-ds.xml! If you are using JBoss Tools, right click
 on the file in the Package Explorer and select Mark as Deployable.

 NOTE:
 The default-ds.xml file installs a data source named jdbc/__default, so that
 both JBoss AS and GlassFish have a data source with the same name.

 You can also set jboss.home on the commandline:

  mvn package jboss:hard-deploy -Djboss.home=/path/to/jboss-as-6.0.0.Final

 In all, you have three options to set the path of your JBoss AS installation:

 1. Set the JBOSS_HOME environment variable (e.g., export JBOSS_HOME=/path/to/jboss-as)
 2. Define the jboss.home Maven property in $HOME/.m2/settings.xml
 3. Override the jboss.home Maven property from the commandline using -Djboss.home=/path/to/jboss-as

 Now start JBoss AS. The application will be running at the following URL:

 - http://localhost:8080/jboss-javaee6-webapp-src/

 To undeploy from JBoss AS, run this command:

  mvn jboss:hard-undeploy

 There are several ways to deploy the archive to GlassFish. The recommended
 approach is to open the project in NetBeans 6.8, right-click on the project
 and select "Run" from the context menu. That starts JavaDB, GlassFish and
 deploys the application. NetBeans then provides incremental deployment of web
 resources.

 IMPORTANT:
 Don't forget to upgrade Weld. See Upgrading Weld in GlassFish 3.1 for details.

 You can also start GlassFish from the commandline. Change to the glassfish/bin
 directory in the GlassFish install root and run these three commands (leading
 ./ not required on Windows):

  ./asadmin start-database
  ./asadmin start-domain domain1

 IMPORTANT:
 Don't forget to start the JavaDB database!

 NOTE:
 NetBeans starts the database automatically when it starts up GlassFish. If you
 have the GlassFish Tools Eclipse plug-in, you have to enable this feature.
 Select Window > Preferences > GlassFish Server Preferences and check the
 option "Start JavaDB database process when > Starting GlassFish Server"

 Now you can either deploy the target/jboss-javaee6-webapp-src.war through the
 web-based GlassFish admininstration console, or you can again use asadmin:

  ./asadmin deploy target/jboss-javaee6-webapp-src.war

 To undeploy the application, run:

  ./asadmin undeploy jboss-javaee6-webapp-src

 Upgrading Weld in GlassFish 3.1
 ===============================

 There are several known CDI visibility issues in the stock version of Weld
 that ships with GlassFish 3.1. One of these issues affects the sample code in
 this application. For more information, see http://sfwk.org/Seam3/Compatibility.
 
 You can upgrade Weld by following these steps:

 1. Stop GlassFish
 2. Download the most recent Weld OSGi Bundle 1.1.1.Final (http://repository.jboss.org/nexus/content/groups/public/org/jboss/weld/weld-osgi-bundle/1.1.1.Final)
 3. Copy that jar to $GLASSFISH_HOME/glassfish/modules/weld-osgi-bundle.jar
 4. Start GlassFish

 You can now deploy the application successfully to GlassFish 3.1.

 Running the Arquillian tests
 ============================

 By default, tests are configured to be skipped. The reason is that the sample
 test is an Arquillian test, which requires the use of a container. You can
 activate this test by selecting one of the two container configurations
 provided, JBoss AS 6 (remote) or GlassFish 3 (embedded).

 To run the test in Embedded GlassFish, first make sure that you don't have a
 GlassFish container running (to avoid port conflicts). Then run the test goal
 with the following profile activated:

  mvn clean test -Parq-glassfish-embedded

 (The clean goal is only required when you switch between containers)

 To run the test in JBoss AS 6, first start a JBoss AS 6 instance. Then, run the
 test goal with the following profile activated:

  mvn clean test -Parq-jbossas-remote

 Consult the Arquillian reference documentation to register profiles for any
 supported container.

 - http://docs.jboss.org/arquillian/reference/latest/en-US/html/containers.html#d0e678

 Importing the project into an IDE
 =================================

 If you created the project using the Maven archetype wizard in your IDE
 (Eclipse, NetBeans or IntelliJ IDEA), then there is nothing to do. You should
 already have an IDE project.

 If you created the project from the commandline using archetype:generate, then
 you need to import the project into your IDE. If you are using NetBeans 6.8 or
 IntelliJ IDEA 9, then all you have to do is open the project as an existing
 project. Both of these IDEs recognize Maven projects natively.

 To import the project into Eclipse, you need the m2eclipse plugins installed.
 Installation instructions are provided on the m2eclipse project site:

 - http://m2eclipse.sonatype.org/installing-m2eclipse.html

 Here's the quick guide:

 - Select Help > Install New Software...
 - Click the Add... button
 - Add the repository URL http://m2eclipse.sonatype.org/update
 - Select and install the Maven Integration for Eclipse
 - Restart as suggested
 - Repeat the first two steps
 - Add the repository URL http://m2eclipse.sonatype.org/sites/m2e-extras
 - Select and install the Maven Integration for WTP (Optional)
 - Restart as suggested

 Once the m2eclipse plugins are installed, you'll be ready to import the
 project into Eclipse and run the project on a Java EE server. We also
 recommend that you install the JBoss Tools plugins if you plan to deploy
 to JBoss AS - http://jboss.org/tools

 NOTE:
 If you are using Eclipse as your IDE, we we strongly recommend that you use
 Eclipse 3.6 (Helios) - http://www.eclipse.org/helios

 Select File > Import. . . and choose the Maven > Existing Maven Projects option.
 Navigate to the root directory of your project. Eclipse should recognize the
 Maven project and preselect it in the Project box. Click Finish and m2eclipse
 will take it from there.

 NOTE:
 You cannot use the import option General > Existing Projects into Workspace
 because Eclipse does not recognize Maven projects as Eclipse projects
 natively. You must use the importer provided by m2eclipse as just described.

 Additional configuration is required to get Eclipse to generate the JPA
 metamodel. The archetype includes the necessary Eclipse config files in your
 project. To enable this feature, right click on the project and select:

  Properties > Java Compiler > Annotation Processing

 Check "Enable annotation processing" then click OK and OK again when your are
 prompted to run a project build.

 This configuration is described in further detail in the Hibernate JPA documentation:

 - http://docs.jboss.org/hibernate/stable/jpamodelgen/reference/en-US/html_single/#d0e319

 Once the project is imported into the IDE, you can execute the Maven commands
 through the IDE controls to deploy the application to a container.

 To deploy to GlassFish from Eclipse, you'll need the GlassFish Tools Bundle,
 available at the update site http://download.java.net/glassfish/eclipse/helios
 or from the Help > Eclipse Marketplace. . .

 Downloading the sources and Javadocs
 ====================================

 If you want to be able to debug into the source code or look at the Javadocs
 of any library in the project, you can run either of the following two
 commands to pull them into your local repository. The IDE should then detect
 them.

  mvn dependency:sources
  mvn dependency:resolve -Dclassifier=javadoc

 Resources
 =========

 - Weld archetypes
   - Quickstart:         http://tinyurl.com/gojavaee
   - Issue tracker:      https://issues.jboss.org/browse/WELDRAD
   - Source code:        http://anonsvn.jboss.org/repos/weld/archetypes
   - Forums:             http://sfwk.org/Community/WeldUsers
 - JSR-299 overview:     http://sfwk.org/Weld
 - JSF community site:   http://www.javaserverfaces.org
 - Download JBoss AS:    http://jboss.org/jbossas/downloads
 - Download GlassFish:   http://glassfish.java.net/public/downloadsindex.html
