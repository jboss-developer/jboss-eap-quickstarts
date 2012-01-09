Example of XA Recovery
======================

What is it?
-----------

If your application needs to modify more than one datasource (or MDBs or a mix) then you will need to use
distributed transactions (we call these XA transactions after the standard where they were
first introduced). This example demonstrates how to update two H2 datasources within
the same transaction. Although H2 XA support is not recommended for production systems the
example does illustrate the general steps you will need to carry out for any datasource vendor.
The example also demonstrates how to recover from transaction failures.

System requirements
-------------------

You will need Java 6.0 (Java SDK 1.6) and Maven 3.0 or better.

The application this project produces is designed to be run on a JBoss AS 7 or EAP 6.
The following instructions target JBoss AS 7, but they also apply to JBoss EAP 6.

Deploying the application
-------------------------

The example requires more configuration steps than most apps, In brief:

1. Start JBoss AS 7 (or EAP 6) (./standalone.sh in the distribution bin folder)

2. Create 2 XA datasources using JBoss AS 7 admin tools:- the web console
[see ref 2 below for instructions below](#2)
and the [admin shell - see ref 3](#3). To use the web console you will need to
set up a user with access to the ManagementRealm using the [add-user.sh tool](#1)

3. Build the example (mvn package) and deploy it to the running AS (mvn jboss-as:deploy)

4. The application is available at the URL: <http://localhost:8080/jboss-as-xa/XA>
It consists of a pair input boxes for entering key value pairs into two different H2 databases.
There are also 2 checkboxes corresponding to each database. Both should to be checked if you
want the AS to use an XA transaction. To list all key value pairs leave the key input box empty.

5. When an "XA transaction" is committed the application server does the completion in two phases.
In phase 1 each resource (database or MDB, for example) is asked to prepare to commit any changes made
during the transaction. If all resources vote to commit then the AS starts phase 2 in which it
tells each resource to commit those changes. The added complexity is to cope with failures (especially
failures that occur during phase 2). Some failure modes require cooperation between
the AS and the datasources in order to guarantee that any pending changes are recovered.
We demonstrate this functionality by terminating the AS whilst phase 2 is running. You will have to
install a tool called byteman (<http://www.jboss.org/byteman/>) [xxx](x1) which lets you perform various
actions at arbitrary points in the code. To enable this functionality you will add some extra options
to JAVA\_OPTS when starting the AS [see notes for details](#4).
This tells the JVM agent about byteman and indicates which
byteman script(s) to run. As you have probably guessed, the script that comes with the example
halts the JVM during phase 2 of an XA commit (its located at src/main/scripts/xa.btm). After updating
standalone.conf you must restart the AS.

6. If you have completed step 5 then you are ready to create a "recovery record". Repeat step 4
by going to the application URL and update both databases. I normally use meaningful keys such as
crash2 if I have enabled the byteman script or simply key2 if not - this way it's easy to keep track of
of recovered database rows.

7. Step 6 will have halted the AS. If you need to convince yourself that the key value pair is still
pending you can use the H2 database console tool downloadable from <http://www.h2database.com>.
(The reason you can not use the AS to check that the row is missing is that starting the AS will
recover the row before you have the chance to look at it.)
To start the H2 console cd in the bin directory of the download and type:

            java -jar h2\*.jar

    The console is available at the url <http://http://localhost:8082>. Use jdbc:h2:file:/tmp/test2 for
    the database url and sa/sa for the username/password.  Enter the query

            select * from xa\_kvpair

    to see that the pair you entered is not present. To see that there is a transaction pending enter
    the query

            SELECT * FROM INFORMATION_SCHEMA.IN_DOUBT

    If you are using the default file based transaction logging store then there will be a record in
    the file system corresponding to the pending transaction. From the AS bin directory, type:

            ls ../standalone/data/tx-object-store/ShadowNoFileLockStore/defaultStore/StateManager/BasicAction/TwoPhaseCoordinator/AtomicAction/

    An example of a logging record is: `0_ffff7f000001_-7f1cf331_4f0b0ad4_15`.
    After recovery this log record is deleted automatically. If you ever need to tell recovery to 
    forget about a particular record you can either delete it manually (if you know which one is which)
    or you can use a JMX browser. For the demo it is simplest to simply delete the records from the file
    system (but *be wary* of doing this on a production system).

8. To observe recovery you *must* disable the byteman script 
    (just comment out the two JAVA\_OPT lines you added in [step 5](#4)), otherwise the AS will simply halt
    again when the recovery system tries to complete the pending transaction. Now you can restart
    the AS, by the time it completely starts the transaction should have completed.
    *But note the hypersonic the database needs to be closed and re-opened to apply the changes
    <http://www.h2database.com/html/advanced.html#two_phase_commit> has more details*.

    If you forget to do this then
    H2 puts the database in read-only mode and you won't be able to run an more transactional accesses to it.
    The simplest way to close and reopen the database is to restart the AS (again).
    If you want to check that the record was recovered you can either use
    the H2 console after shutting down the AS or use the application url when the AS has restarted.


Downloading the sources and Javadocs
------------------------------------

If you want to be able to debug into the source code or look at the Javadocs
of any library in the project, you can run either of the following two
commands to pull them into your local repository. The IDE should then detect
them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc

Notes:
------

<a name="1"/> [1] How to add a web console user
----------------------------

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
    Added user 'admin' to file '/home/mmusgrov/source/as/jboss-as/build/target/jboss-as-7.1.0.Final-SNAPSHOT/standalone/configuration/mgmt-users.properties'
    Added user 'admin' to file '/home/mmusgrov/source/as/jboss-as/build/target/jboss-as-7.1.0.Final-SNAPSHOT/domain/configuration/mgmt-users.properties'

<a name="2"/> [2] Configuring XA datasources in the web console
-----------------------------------------------

Go to the JBoss web console url:
<http://localhost:9990/console/> and login as the admin user you just created in [ref 1](#1).

Select the Profile tab (top right) and from the menu on the left choose Connector -> Datasources.

Choose the XA Datasources tab. Click the Add button (top right) to create an XA Datasource:

    Step 1/4:
    Name: java:jboss/datasources/H2XADS1
    JNDI Name: java:jboss/datasources/H2XADS1

    Step 2/4: Select the default XA Driver (ie Name: h2 and Datasource Class: org.h2.jdbcx.JdbcDataSource)

    Step 3/4: Add an single property to define the backing file for the database
    Key: URL and Value: jdbc:h2:file:/tmp/test1;DB\_CLOSE_ON\_EXIT=FALSE

    Step 4/4: Define Connection Settings ( Username: sa and Password: sa)

Select the new XA Datasource and click the Attributes tab. Click the Enable button.

Do the same for a second XA datasource (instead of H2XADS1 use H2XADS2 and instead of
jdbc:h2:file:/tmp/test1 for the H2 database url use jdbc:h2:file:/tmp/test2)

There is one final attribute that needs setting which can only be done via the command shell.
See ref [3] for instructions.

<a name="3"/> [3] Enable an XA datasource for recovery
------------------------------------------------------

Set the recovery credentials on each of the datasources you created in [2].

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

Now go back to the web console and select Server -> Configuration (from the left menu) and click
the Reload button on the far left.

<a name="4"/> [4] Enabling byteman scripting
--------------------------------------------

    BYTEMAN\_HOME=<install location>
    QUICSTART_DIR=<<this quickstart dir>
    JAVA\_OPTS="$JAVA\_OPTS -javaagent:$BYTEMAN\_HOME/lib/byteman.jar=script:$QUICSTART\_DIR/src/main/scripts/xa.btm"
    JAVA\_OPTS="$JAVA\_OPTS -Dorg.jboss.byteman.verbose=true"

<a name="x1"/>
TODO download it via maven
