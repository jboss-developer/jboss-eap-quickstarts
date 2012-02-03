Example of JTA Crash Recovery
=============================
Author: Mike Musgrove

What is it?
-----------

If your application needs to modify more than one datasource (MDBs, databases etc)
then you will need to use distributed transactions (we call these XA transactions
[after the standard](https://www2.opengroup.org/ogsys/jsp/publications/PublicationDetails.jsp?catalogno=c193) 
in which they were first introduced).

An important feature of the example is to demonstrate XA recovery by showing how the system is brought
back into a consistent state after halting the application server. *"XA recovery deals with system or
application failures to ensure that resources of a transaction are applied consistently to all
resources affected by the transaction, even if any of the application processes or the machine
hosting them crash or lose network connectivity."*

The example shows how to atomically update a relational database table using JPA and send a
message using JMS (as mentioned these kinds of paired updates to two different resources are called XA
transactions and are defined by the JEE JTA specification). We use H2 (<http://www.h2database.com>)
for the database. Although H2 XA support is not recommended for production systems the example
does illustrate the general steps you will need to perform for any datasource vendor.
The relational table contains two columns to represent a key value pair - the application presents
an HTML form containing two input boxes for creating, updating, deleting or listing these pairs.

When you add or update a key value pair the quickstart starts a transaction, updates the table, produces
a JMS message containing the update and finally commits the transaction. If all goes well then
eventually the consumer gets the message and generates a database update setting the value
corresponding to the key to something that indicates it was changed by the message consumer.
The idea is to demonstrate recovery by halting the application server after the database
modification is committed but before the JMS producer is committed.

System requirements
-------------------

You will need Java 6.0 (Java SDK 1.6) and Maven 3.0 or better.

The application this project produces is designed to be run on a JBoss AS 7 or EAP 6.
The following instructions target JBoss AS 7, but they also apply to JBoss EAP 6.

The example uses Byteman which is a java agent and a set of tools that enables the user
to insert extra Java code into an application. Byteman can be downloaded from
<http://www.jboss.org/byteman/downloads/>. Once downloaded, extract Byteman to a location
on your hard drive.

In order to keep the instructions manageable the various OS commands target linux based systems
but with minor changes to file paths and executable names will work on Windows systems too.

Deploying the application
-------------------------

The example requires more configuration steps than most apps. In brief:

1. Start the full JBoss AS 7 (or EAP 6) (type `./standalone.sh -c standalone-full.xml` in
the distribution bin folder)

2. Build the example (`mvn package`) and deploy it to the running AS (`mvn jboss-as:deploy`)
The example provides its own H2 XA datasource configuration (by including a xyz-ds.xml file in the
WEB-INF folder of the war archive). If you want to run with a different database then you
will need to either get hold of an equivalent file for your target database or
adapt [the instructions below](#xaconfig) which outline how to create an H2 XA datasource
using JBoss AS 7 admin tools. If you do decide to experiment with other databases we
recommend you use a more robust XA database such as postgreSQL (one of the other transaction
quickstarts provides instructions for postgreSQL configuration)

3. The application is available at the URL <http://localhost:8080/jboss-as-jta-crash-rec/XA> where you
will find a web page containing two html input boxes for adding key value pairs to 
a database. Instructions on using the application are shown when you open the application web page.
When you add a new key value pair the change is committed to the database and a JMS message sent.
The message consumer then updates the row just inserted by appending the text *"updated via JMS"* to the value

4. When an _XA transaction_ is committed the application server does the completion in two phases.
In phase 1 each resource (in this example a database and a JMS message producer)
is asked to prepare to commit any changes made
during the transaction. If all resources vote to commit then the AS starts phase 2 in which it
tells each resource to commit those changes. The added complexity is to cope with failures (especially
failures that occur during phase 2). Some failure modes require cooperation between
the AS and the datasources in order to guarantee that any pending changes are recovered.
We demonstrate this functionality by terminating the AS whilst phase 2 is running using a tool
called Byteman. Now you should [enable Byteman by following the instructions below](#byteman).

5. If you have completed step 4 then you are ready to create a _recovery record_. Go to the
application URL and insert another row into the database.

6. Step 5 will have halted the AS. If you need to convince yourself that the database insert was
committed but that message delivery is still pending then use an SQL client such as the H2 database
console tool to show that the value is present but that it does not contain the message
added by the consumer (*" updated via JMS"*). If you want to use the H2 console start it by typing:

            java -jar <AS install directory>/modules/com/h2database/h2/main/h2*.jar

    The console is available at the url <http://localhost:8082>. If you receive an error
    such as `Exception opening port "8082"` then its most likely because some other application
    has that port open (meaning you will need to find it and close it). Use `jdbc:h2:file:~/xaqs1` for
    the database url and sa/sa for the username/password (these should correspond to what's in
    the datasource configuration section of standalone-full.xml.
    Once you are logged in enter the query

            select * from xa_kvpair

    to see that the pair you entered is present but does not contain *"updated via JMS"*.

    *Important: H2 only allows one connection per database so make sure you close the H2 console before
    restarting the Application Server.*
    
    If you are using the default file based transaction logging store then there will be a record in
    the file system corresponding to the pending transaction. From the AS bin directory, type:

            ls ../standalone/data/tx-object-store/ShadowNoFileLockStore/defaultStore/StateManager/BasicAction/TwoPhaseCoordinator/AtomicAction/

    An example of a logging record file name is: `0_ffff7f000001_-7f1cf331_4f0b0ad4_15`.

    After recovery log records are normally automatically deleted except in the case where the
    Transaction Manager (TM) commit request was received, and acted upon, by a resource but the TM
    crashed before it had time to clean up its log of that resource.
    
7. To observe recovery restart the AS (*but depending upon how you configured Byteman don't forget to 
    disable any scripts and also if you used the H2 console remember to close it first*).
    By the time the AS is ready the transaction should have recovered.
    A message is printed on the AS console when the consumer has done the update (look for a line
    that reads 'JTA Crash Record Quickstart: key value pair updated via JMS').
    Check that the row you inserted in step 5 now contains the text *"updated via JMS"* thus showing
    that the JMS message was recovered successfully (use the application URL to perform this check).

    *Note, you will most likely see the following message on the console:
    `ARJUNA016038: No XAResource to recover ... eis_name=...H2XADS1` during recovery.
    This is normal, what actually happened is that the first resource (H2XADS1) committed
    before the AS was halted in step 5. The transaction logs are only updated/deleted after the
    outcome of the transaction is determined. If the transaction manager did update the log
    as each participant (database) completed then throughput would suffer.* Notice you do not get
    a similar message for the JMS resource since that is the resource that recovered and the log record was
    updated to reflect this change. So you will need to manually remove the record for the first
    participant if you know which one is which or, if you are using the community version 
    of the AS then you can also inspect the transaction logs using a JMX browser. For the demo it is
    simplest to delete the records from the file system (but *be wary* of doing this on a production system).
    If you need to clear out the H2 database then delete its backing files (`rm ~/xaqs1*`).
    In a future version of the transactions module clean up should be automatic.

Downloading the sources and Javadocs
------------------------------------

If you want to be able to debug into the source code or look at the Javadocs
of any library in the project, you can run either of the following two
commands to pull them into your local repository. The IDE should then detect
them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc


<a name="xaconfig"/> Configure an XA datasource
----------------------------

Step 1. Create an admin user with access to the ManagementRealm (this will give you access to the
        AS7 web admin console):

        [mmusgrov@dev1 bin]$ ./add-user.sh

        Enter the details of the new user to add.
        Realm (ManagementRealm) : 
        Username : admin
        Password : 
        Re-enter Password : 
        The username 'admin' is easy to guess
        Are you sure you want to add user 'admin' yes/no? yes
        About to add user 'admin' for realm 'ManagementRealm'
        Is this correct yes/no? yes
        Added user 'admin' to file '.../jboss-as/build/target/jboss-as-7.1.0.Final-SNAPSHOT/standalone/configuration/mgmt-users.properties'
        Added user 'admin' to file '.../jboss-as/build/target/jboss-as-7.1.0.Final-SNAPSHOT/domain/configuration/mgmt-users.properties'

Step 2. Create a Datasource

        Go to the JBoss web console url:
        <http://localhost:9990/console/> and login as the admin user you just created.

        Select the Profile tab (top right) and from the menu on the left choose Connector -> Datasources.

        Choose the XA Datasources tab. Click the Add button (top right) to create an XA Datasource:

            Step 1/4:
            Name: java:jboss/datasources/H2XADS1
            JNDI Name: java:jboss/datasources/H2XADS1

            Step 2/4: Select the default XA Driver (ie Name: h2 and Datasource Class: org.h2.jdbcx.JdbcDataSource)

            Step 3/4: Add an single property to define the backing file for the database
            Key: URL and Value: jdbc:h2:file:*/xaqs1;DB_CLOSE_ON_EXIT=FALSE

            Step 4/4: Define Connection Settings (Username: sa and Password: sa)

        Select the new XA Datasource and click the Attributes tab. Click the Enable button.

Step 3. There is one final attribute that needs setting which can only be done via the command shell.
        For each datasource you need to set the recovery credentials as follows:

        Go to the AS bin directory and start the command shell:

            > [mmusgrov@dev1 bin]$ ./jboss-cli.sh --connect
            [standalone@localhost:9999 /] cd /subsystem=datasources/xa-data-source="java:jboss/datasources/H2XADS1"
            [standalone@localhost:9999 xa-data-source=java:jboss/datasources/H2XADS1] :write-attribute(name="recovery-username",value="sa")
            {
                "outcome" => "success",
                    "response-headers" => {
                    "operation-requires-reload" => true,
                    "process-state" => "reload-required"
                }
            }
            [standalone@localhost:9999 xa-data-source=java:jboss/datasources/H2XADS1] :write-attribute(name="recovery-password",value="sa")
            {
                "outcome" => "success",
                    "response-headers" => {
                    "operation-requires-reload" => true,
                    "process-state" => "reload-required"
                }
            }

        Now go back to the web console Runtime tab and select Server -> Configuration
        (from the left menu) and click the Reload button on the far left.

At the end of this process you should end up with an entry similar to the following in your
standalone.xml config file:

                <xa-datasource jndi-name="java:jboss/datasources/H2XADS1" pool-name="java:jboss/datasources/H2XADS1" enabled="true" use-ccm="false">
                    <xa-datasource-property name="URL">
                        jdbc:h2:file:/tmp/xaqs1;DB_CLOSE_ON_EXIT=FALSE
                    </xa-datasource-property>
                    <driver> h2 </driver>
                    <xa-pool>
                        <is-same-rm-override> false </is-same-rm-override>
                        <interleaving> false </interleaving>
                        <pad-xid> false </pad-xid>
                        <wrap-xa-resource> true </wrap-xa-resource>
                    </xa-pool>
                    <security>
                        <user-name> sa </user-name>
                        <password> sa </password>
                    </security>
                    <recovery>
                        <recover-credential>
                            <user-name> sa </user-name>
                            <password> sa </password>
                        </recover-credential>
                    </recovery>
                    <validation>
                        <validate-on-match> false </validate-on-match>
                        <background-validation> false </background-validation>
                        <background-validation-millis> 0 </background-validation-millis>
                    </validation>
                    <statement>
                        <prepared-statement-cache-size> 0 </prepared-statement-cache-size>
                        <share-prepared-statements> false </share-prepared-statements>
                    </statement>
                </xa-datasource>

<a name="byteman"/> Halting the Application using Byteman
--------------------------------------------

As you may have guessed, you will use the Byteman tool to halt the AS7 JVM during phase 2 of an XA commit.

        Backup the file <APP SERVER>/bin/standalone.conf (standalone.conf.bat on Windows)
        Open the file <APP SERVER>/bin/standalone.conf (standalone.conf.bat on Windows)
        Append the following text to the end of the file: NOTE: You should replace the file paths
        as appropriate for you system:

        JAVA_OPTS="-javaagent:/home/user/byteman-download-2.0.0/lib/byteman.jar=script:/home/user/quickstart/jta-crash-rec/src/main/scripts/xa.btm ${JAVA_OPTS}"

These changes will only take effect during AS startup.

*IMPORTANT: After you have finished with the quickstart, it is important to restore your backup of standalone.conf thereby removing the Byteman configuration from the standalone.conf[.bat] file. Otherwise your server will always crash when a transaction commits!*

[The Byteman download also contains scripts for installing the Bytman agent ('bminstall.sh <procid>' and
'bminstall.bat <procid>') and for uploading Byteman scripts ('bmsubmit.sh -l src/main/scripts/xa.btm' and
'bmsubmit.bat -l src/main/scripts/xa.btm'). Personally I prefer these scripts since they give me more
control over loading and unloading rules and I don't need to worry about forgetting to put
standalone.conf back to its original contents.]

