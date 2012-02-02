Example of XA Recovery
======================

What is it?
-----------

If your application needs to modify more than one datasource (or MDB or a mix of both)
then you will need to use distributed transactions (we call these XA transactions
[after the standard](https://www2.opengroup.org/ogsys/jsp/publications/PublicationDetails.jsp?catalogno=c193) 
in which they were first introduced). This example demonstrates how to update
two H2 (hypersonic) datasources within the same transaction in the presence of failures.
Although H2 XA support is not recommended for production systems the example does illustrate the
general steps you will need to perform for any datasource vendor.

An important feature of the example is to demonstrate XA recovery by showing how the system is brought
back into a consistent state after crashing the application server. *"XA recovery deals with system or
application failures to ensure that resources of a transaction are applied consistently to all
resources affected by the transaction, even if any of the application processes or the machine
hosting them crash or lose network connectivity."*

System requirements
-------------------

You will need Java 6.0 (Java SDK 1.6) and Maven 3.0 or better.

The application this project produces is designed to be run on a JBoss AS 7 or EAP 6.
The following instructions target JBoss AS 7, but they also apply to JBoss EAP 6.

The example uses Byteman which is a java agent and a set of tools that enables the user
to insert extra Java code into an application.

In order to keep the instructions manageable the various OS commands target linux based systems
but with minor changes to file paths and executable names will work on Windows systems too.

Deploying the application
-------------------------

The example requires more configuration steps than most apps. In brief:

1. Start JBoss AS 7 (or EAP 6) (type `./standalone.sh &` in the distribution bin folder)

2. Create 2 XA datasources using JBoss AS 7 admin tools as [outline below](#xaconfig)

3. Build the example (`mvn package`) and deploy it to the running AS (`mvn jboss-as:deploy`)

4. The application is available at the URL <http://localhost:8080/jboss-as-xa/XA> where you
will find a web page containing two html input boxes for adding key value pairs to one or two
databases. There are two check boxes corresponding to the two H2 databases both of which should
be checked if you want the AS to use an XA transaction. To list all key value pairs leave the
key input box empty.

5. When an _XA transaction_ is committed the application server does the completion in two phases.
In phase 1 each resource (database or MDB, for example) is asked to prepare to commit any changes made
during the transaction. If all resources vote to commit then the AS starts phase 2 in which it
tells each resource to commit those changes. The added complexity is to cope with failures (especially
failures that occur during phase 2). Some failure modes require cooperation between
the AS and the datasources in order to guarantee that any pending changes are recovered.
We demonstrate this functionality by terminating the AS whilst phase 2 is running using a tool
called Byteman. Now you should [enable Byteman by following the instructions below](#byteman).

6. If you have completed step 5 then you are ready to create a _recovery record_. Repeat step 4
by going to the application URL and update both databases. I normally use meaningful keys such as
crash2 if I have enabled the Byteman script or simply key2 if not - this way it's easy to keep track
of recovered database rows.

7. Step 6 will have halted the AS. If you need to convince yourself that the key value pair is still
pending you can use the H2 database console tool which can be found in the AS modules directly or
can be downloaded directly from <http://www.h2database.com>.
(The reason you can not use the AS to check that the row is missing is that starting the AS will
recover the row before you have the chance to look at it.)
To start the H2 console type:

            java -jar <h2 install directory>/bin/h2*.jar

    or,

            java -jar <AS install directory>/modules/com/h2database/h2/main/h2*.jar

    The console is available at the url <http://localhost:8082>. If you receive an error
    such as `Exception opening port "8082"` then its most likely because some other application
    has that port open (meaning you will need to find it and close it). Use `jdbc:h2:file:/tmp/xaqs2` for
    the database url and sa/sa for the username/password (these should correspond to what you [configured
    in standalone.xml](#xaconfig)). Once you are logged in enter the query

            select * from xa_kvpair

    to see that the pair you entered is *not* present. To see that there is a transaction pending enter
    the query

            SELECT * FROM INFORMATION_SCHEMA.IN_DOUBT

    If you look at `jdbc:h2:file:/tmp/xaqs1` instead then the SQL row will be present and there
    should be no in doubt transactions (unless you have some still lying around from unrelated
    transactions).
    If you are using the default file based transaction logging store then there will be a record in
    the file system corresponding to the pending transaction. From the AS bin directory, type:

            ls ../standalone/data/tx-object-store/ShadowNoFileLockStore/defaultStore/StateManager/BasicAction/TwoPhaseCoordinator/AtomicAction/

    An example of a logging record file name is: `0_ffff7f000001_-7f1cf331_4f0b0ad4_15`.
    After recovery log records are automatically deleted. If you ever need to tell recovery to 
    forget about a particular record you can either delete it manually (if you know which one is which)
    or you can use a JMX browser. For the demo it is simplest to delete the records from the file
    system (but *be wary* of doing this on a production system). If you need to clear out the H2
    databases then delete their backing files (`rm /tmp/xaqs*`).

8. To observe recovery restart the AS, by the time it's ready the transaction should have recovered.
    However XA support in the hypersonic product isn't great and after recovery the new SQL row will
    not be immediately visible - [the database needs to be closed and re-opened for it to make the
    changes visible](http://www.h2database.com/html/advanced.html#two_phase_commit).
    The easiest way to do this is to shutdown the AS thus releasing its connection to the recovered
    database. And now if you use use the H2 console to inspect the table
    (`SELECT * FROM XA_KVPAIR;`) you should see the missing row and there should no longer be an in
    doubt transaction. Similarly, if you restart the AS, you can use the example application to list
    the database contents.

    *Note, you will most likely the following message on the console
    `ARJUNA016038: No XAResource to recover ... eis_name=...H2XADS1` during recovery.
    This is normal, what actually happened is that the first resource (H2XADS1) committed
    before the AS was halted in step 6. The transaction logs are only updated/deleted after the
    outcome of the transaction is determined. If the transaction manager did update the log
    as each participant (database) completed then throughput would suffer.* Notice you do not get
    a similar message for H2XADS2 since that is the database that recovered and the log record was
    updated to reflect this change. So you will need to
    manually remove the record for the first participant, you will know it's safe to do so since
    you can see on the H2 console that there are no in doubt transactions. If you are using the 
    community version of the AS then you can also inspect the transaction logs using jconsole
    where you will be able to determine that the log record corresponds to the first H2 database.
    In a future version of the transactions module clean up should be automatic.

Downloading the sources and Javadocs
------------------------------------

If you want to be able to debug into the source code or look at the Javadocs
of any library in the project, you can run either of the following two
commands to pull them into your local repository. The IDE should then detect
them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc


<a name="xaconfig"/> Configure XA datasources
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
            Key: URL and Value: jdbc:h2:file:/tmp/xaqs1;DB_CLOSE_ON_EXIT=FALSE

            Step 4/4: Define Connection Settings (Username: sa and Password: sa)

        Select the new XA Datasource and click the Attributes tab. Click the Enable button.

        Do the same for a second XA datasource (instead of H2XADS1 use H2XADS2 and instead of
        jdbc:h2:file:/tmp/xaqs1 for the H2 database url use jdbc:h2:file:/tmp/xaqs2)

Step 3. There is one final attribute that needs setting which can only be done via the command shell.
        For each datasource you need to set the recovery credentials as follows:

        Go to the AS bin directory and start the command shell:

            > [mmusgrov@dev1 bin]$ ./jboss-admin.sh
            You are disconnected at the moment. Type 'connect' to connect to the server or 'help' for the list of supported commands.
            [disconnected /] connect
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

        Do the same for java:jboss/datasources/H2XADS2

            > cd /subsystem=datasources/xa-data-source="java:jboss/datasources/H2XADS2"
            :write-attribute(name="recovery-username",value="sa")
            :write-attribute(name="recovery-password",value="sa")
            :write-attribute(name="wrap-xa-resource",value="true")

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

                <xa-datasource jndi-name="java:jboss/datasources/H2XADS2" pool-name="java:jboss/datasources/H2XADS1" enabled="true" use-ccm="false">
                    <xa-datasource-property name="URL">
                        jdbc:h2:file:/tmp/xaqs2;DB_CLOSE_ON_EXIT=FALSE

                etc ...

<a name="byteman"/> Crashing the Application using Byteman
--------------------------------------------

Byteman is a java agent and tools that lets you insert extra Java code into an application. As you
may have guessed, you will use this tool to halt the AS7 JVM during phase 2 of an XA commit.

Download Byteman from <http://www.jboss.org/byteman/downloads>. Unzip the download and set the
environment variable BYTEMAN\_HOME to the directory where you unpacked it and put the Byteman
bin directory in your path: `export PATH=$PATH:$BYTEMAN_HOME/bin`

To install the Byteman agent into the running application server first find its process id.
[On linux systems the process will be a child (labeled with the process cmd `java -server`) of
a process called `standalone.sh`. For example: `ps -C standalone.sh -o pid=` to find its process id
and then to find the JVM that the shell script spawned type `ps --ppid <pid>`.
Or even simpler, just type `ps` in the terminal where you started the AS - chances are there will only
be one process that has the command name `java`].

        bminstall.sh -Dorg.jboss.byteman.verbose=true <process id>

[Note: It may be the case that your JVM is configured such that the abilitity to dynamically install
agents is disabled. If so then `bminstall.sh` will fail and you will need configure Byteman via
the JAVA\_OPTS environment variable before starting the AS:
`JAVA_OPTS="${JAVA_OPTS} -javaagent:$BYTEMAN_HOME/lib/byteman.jar=listener:true`
*But remember to unset JAVA_OPTS when you are done with this qickstart (since I doubt you will need
Byteman for the other quickstarts).*]

Now you are ready to install the Byteman script that comes with the quickstart:

         bmsubmit.sh -l src/main/scripts/xa.btm

