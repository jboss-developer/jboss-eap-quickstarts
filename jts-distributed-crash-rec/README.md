jts-distributed-crash-rec: JTS and distributed crash recovery 
=============================================================
Author: Tom Jenkinson  
Level: Advanced  
Technologies: JTS  
Summary: Demonstrates recovery of distributed crashed components  
Prerequisites: jts  
Target Product: EAP  
Product Versions: EAP 6.1, EAP 6.2, EAP 6.3  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts/>  

What is it?
-----------

This quickstart demonstrates a distributed crash recovery across multiple application servers. 

Crash recovery is a key feature provided by an application server and ensures an application's data consistency, even in the presence of failure. Providing reliable crash recovery helps qualify the pedigree of an application server, distributed crash recovery even more so.

This quickstart uses the application components from the `jts` quickstart. It provides a Byteman rule to inject a halt into the application server at a crucial point in the phase2 commit of the transaction. Byteman is used solely to raise an artificial fault. An IDE could also provide this simulation, although it is more complex to use an IDE for this purpose.

Apart from that, this quickstart works the same as the `jts` quickstart and if the Byteman rule is left out, this quickstart _is_ the JTS quickstart.

As an overview, the sequence of events to expect:

1. Configure and start two JBoss EAP servers.
2. Build and deploy the two application components.
3. Open a web browser and attempt to invoice two customers as with the `jts` quickstart.
4. JBoss EAP server 1 will run through a two-phase commit (2PC), preparing the resources in JBoss EAP server 1 and JBoss EAP server 2. JBoss EAP server 1 will then crash before it can call commit.
5. The user is invited to inspect the content of the transaction objectstore.
6. JBoss EAP server 1 should be restarted. It will then recover the "invoices" delivered to the MDBs, just as it does in the _jts_ quickstart


System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.1 or later. 

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.


Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md) before testing the quickstarts.


Download and Configure Byteman
-----------------
 
This quickstart uses _Byteman_ to help demonstrate crash recovery. You can find more information about _Byteman_ here: [Configure Byteman for Use with the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_BYTEMAN.md#configure-byteman-for-use-with-the-quickstarts)

Follow the instructions here to download and configure _Byteman_: [Download and Configure Byteman](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_BYTEMAN.md#download-and-configure-byteman)


Prerequisites
--------------

Developers should be familiar with the concepts introduced in the following quickstarts:

* _cmt_
* _jts_

IMPORTANT: This quickstart depends on the deployment of the `jts` quickstart for its test. Before running this quickstart, see the [jts README](../jts/README.md) file for details on how to deploy it.

You can verify the deployment of the `jts` quickstart by accessing the following URL:  <http://localhost:8080/jboss-jts-application-component-1/>.


Test the Application
-----------------------------------

1. If you have not yet done so, configure the two application servers and deploy the `jts` quickstart. Follow the instructions in the [jts README](../jts/README.md) file.

2. Configure _Byteman_ to halt JBoss EAP server 1
    * Stop both JBoss EAP servers.
    * Follow the instructions here to clear the transaction objectstore remaining from any previous tests: [Clear the Transaction ObjectStore](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_BYTEMAN.md#clear-the-transaction-objectstore)
    * The following 2 lines of text must be appended to the server configuration file for server 1 only using the instructions located here: [Use Byteman to Halt the Application](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_BYTEMAN.md#use-byteman-to-halt-the-application)

        For Linux:
        
            JAVA_OPTS="-javaagent:/BYTEMAN_HOME/lib/byteman.jar=script:/QUICKSTART_HOME/jts-distributed-crash-rec/byteman-scripts/failAfterPrepare.btm ${JAVA_OPTS}"
            JAVA_OPTS="-Dorg.jboss.byteman.transform.all -Djboss.modules.system.pkgs=org.jboss.byteman -Dorg.jboss.byteman.verbose=true ${JAVA_OPTS}"

        For Windows: 
        
             JAVA_OPTS=%JAVA_OPTS% -javaagent:C:BYTEMAN_HOME\lib\byteman.jar=script:C:\QUICKSTART_HOME\jts-distributed-crash-rec\byteman-scripts\failAfterPrepare.btm %JAVA_OPTS%
             JAVA_OPTS=%JAVA_OPTS% -Dorg.jboss.byteman.transform.all -Djboss.modules.system.pkgs=org.jboss.byteman -Dorg.jboss.byteman.verbose=true 

3. Start both of the JBoss EAP servers

    If you are using Linux:

        Server 1: EAP_HOME_1/bin/standalone.sh -c standalone-full.xml
        Server 2: EAP_HOME_2/bin/standalone.sh -c standalone-full.xml -Djboss.socket.binding.port-offset=100

    If you are using Windows

        Server 1: EAP_HOME_1\bin\standalone.bat -c standalone-full.xml
        Server 2: EAP_HOME_2\bin\standalone.bat -c standalone-full.xml -Djboss.socket.binding.port-offset=100

4. Access the application at the following URL: <http://localhost:8080/jboss-jts-application-component-1/>
    * When you enter a name and click to "add" that customer, you will see the following in the application server 1 console:

            15:46:55,070 INFO  [org.jboss.ejb.client] (http-localhost-127.0.0.1-8080-1) JBoss EJB Client version 1.0.0.Beta12
            15:46:55,658 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) Rule.execute called for Fail 2PC after prepare_0
            15:46:55,665 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) HelperManager.install for helper classorg.jboss.byteman.rule.helper.Helper
            15:46:55,666 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) calling activated() for helper classorg.jboss.byteman.rule.helper.Helper
            15:46:55,666 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) Default helper activated
            15:46:55,667 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) calling installed(Fail 2PC after prepare) for helper classorg.jboss.byteman.rule.helper.Helper
            15:46:55,667 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) Installed rule using default helper : Fail 2PC after prepare
            15:46:55,668 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) Fail 2PC after prepare execute
            15:46:55,669 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) rule.debug{Fail 2PC after prepare} : !!!killing JVM!!!
    * NOTE: Until you restart JBoss EAP server 1, you will see several error messages in JBoss EAP server 2. These are to be expected:

            15:46:55,044 INFO  [org.jboss.ejb.client] (RequestProcessor-10) JBoss EJB Client version 1.0.0.Beta12
            15:49:06,579 WARN  [com.arjuna.ats.jts] (Periodic Recovery) ARJUNA022167: Got TRANSIENT from ORB for tx 0:ffffc0a8013c:-2eb1158b:4f280ce3:1a, unable determine status, will retry later
            15:51:19,103 WARN  [com.arjuna.ats.jts] (Periodic Recovery) ARJUNA022167: Got TRANSIENT from ORB for tx 0:ffffc0a8013c:-2eb1158b:4f280ce3:1a, unable determine status, will retry later
            15:51:19,120 WARN  [com.arjuna.ats.jta] (Periodic Recovery) ARJUNA016005: JTS XARecoveryModule.xaRecovery - failed to recover XAResource. status is $3
            15:53:31,638 WARN  [com.arjuna.ats.jts] (Periodic Recovery) ARJUNA022167: Got TRANSIENT from ORB for tx 0:ffffc0a8013c:-2eb1158b:4f280ce3:1a, unable determine status, will retry later
            15:53:31,644 WARN  [com.arjuna.ats.jta] (Periodic Recovery) ARJUNA016005: JTS XARecoveryModule.xaRecovery - failed to recover XAResource. status is $3

5. At this point, Byteman halts or crashes server 1. You should also be able to view the contents of the object store by typing:

        tree server*/standalone/data/tx-object-store

    This should display:

        server1/standalone/data/tx-object-store
        `-- ShadowNoFileLockStore
            `-- defaultStore
                |-- CosTransactions
                |   `-- XAResourceRecord
                |       `-- 0_ffffc0a8013c_38e104bd_4f280cdb_1d
                |-- Recovery
                |   `-- FactoryContact
                |       |-- 0_ffffc0a8013c_38e104bd_4f280cdb_17
                |       |-- 0_ffffc0a8013c_-671009a_4f280e7e_17
                |       `-- 0_ffffc0a8013c_6d5d82b5_4f280a16_f
                |-- RecoveryCoordinator
                |   `-- 0_ffff52e38d0c_c91_4140398c_0
                `-- StateManager
                    `-- BasicAction
                        `-- TwoPhaseCoordinator
                            `-- ArjunaTransactionImple
                                `-- 0_ffffc0a8013c_38e104bd_4f280cdb_19
        server2/standalone/data/tx-object-store
        `-- ShadowNoFileLockStore
            `-- defaultStore
                |-- CosTransactions
                |   `-- XAResourceRecord
                |       `-- 0_ffffc0a8013c_-2eb1158b_4f280ce3_1e
                |-- Recovery
                |   `-- FactoryContact
                |       |-- 0_ffffc0a8013c_-2eb1158b_4f280ce3_18
                |       `-- 0_ffffc0a8013c_4f6459f0_4f280a24_f
                |-- RecoveryCoordinator
                |   `-- 0_ffff52e38d0c_c91_4140398c_0
                `-- StateManager
                    `-- BasicAction
                        `-- TwoPhaseCoordinator
                            `-- ArjunaTransactionImple
                                `-- ServerTransaction
                                    `-- 0_ffffc0a8013c_-2eb1158b_4f280ce3_1a


6. [Disable the Byteman script](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_BYTEMAN.md##disable-the-byteman-script) by restoring the backup configuration file for server 1.

7. Follow the steps above to restart server 1 and wait for recovery to complete. 

    _IMPORTANT: By default, the recovery process checks the transactional state every two minutes, therefore it can take a while for recovery to happen. Also recovery for each server will take place at its own recovery interval._
    * You will know when recovery is complete for server 2 as you will see the following in application-server-2 console:

            12:09:38,697 INFO  [org.jboss.ejb.client] (RequestProcessor-10) JBoss EJB Client version 1.0.0.Beta11
            12:09:39,204 INFO  [class org.jboss.as.quickstarts.cmt.jts.mdb.HelloWorldMDB] (Thread-3 (group:HornetQ-client-global-threads-649946595)) Received Message: Created invoice for customer named: Tom
    * NOTE: You will also get several stack traces in JBoss EAP server 1 console during recovery, these are to be expected as not all resources are available at all stages of recovery.

            15:55:41,706 WARN  [com.arjuna.ats.jts] (Thread-84) ARJUNA022223: ExtendedResourceRecord.topLevelCommit caught exception: org.omg.CORBA.OBJECT_NOT_EXIST: Server-side Exception: unknown oid
                at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) [:1.6.0_22]
                at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:57) [:1.6.0_22]
                at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) [:1.6.0_22]
                at java.lang.reflect.Constructor.newInstance(Constructor.java:532) [:1.6.0_22]
                at org.jacorb.orb.SystemExceptionHelper.read(SystemExceptionHelper.java:223) [jacorb-2.3.1.jbossorg-1.jar:]
                at org.jacorb.orb.ReplyReceiver.getReply(ReplyReceiver.java:319) [jacorb-2.3.1.jbossorg-1.jar:]
                at org.jacorb.orb.Delegate.invoke_internal(Delegate.java:1090) [jacorb-2.3.1.jbossorg-1.jar:]
                at org.jacorb.orb.Delegate.invoke(Delegate.java:957) [jacorb-2.3.1.jbossorg-1.jar:]
                at org.omg.CORBA.portable.ObjectImpl._invoke(ObjectImpl.java:80) [jacorb-2.3.1.jbossorg-1.jar:]
                at com.arjuna.ArjunaOTS._ArjunaSubtranAwareResourceStub.commit(_ArjunaSubtranAwareResourceStub.java:252) [jbossjts-4.16.1.Final.jar:]
                at com.arjuna.ats.internal.jts.resources.ExtendedResourceRecord.topLevelCommit(ExtendedResourceRecord.java:502) [jbossjts-4.16.1.Final.jar:]
                at com.arjuna.ats.arjuna.coordinator.BasicAction.doCommit(BasicAction.java:2753) [jbossjts-4.16.1.Final.jar:]
                at com.arjuna.ats.arjuna.coordinator.BasicAction.doCommit(BasicAction.java:2669) [jbossjts-4.16.1.Final.jar:]
                at com.arjuna.ats.arjuna.coordinator.BasicAction.phase2Commit(BasicAction.java:1804) [jbossjts-4.16.1.Final.jar:]
                at com.arjuna.ats.internal.jts.recovery.transactions.RecoveredTransaction.replayPhase2(RecoveredTransaction.java:197) [jbossjts-4.16.1.Final.jar:]
                at com.arjuna.ats.internal.jts.recovery.transactions.TransactionCache.replayPhase2(TransactionCache.java:233) [jbossjts-4.16.1.Final.jar:]
                at com.arjuna.ats.internal.jts.recovery.transactions.CachedRecoveredTransaction.replayPhase2(CachedRecoveredTransaction.java:173) [jbossjts-4.16.1.Final.jar:]
                at com.arjuna.ats.internal.jts.recovery.transactions.RecoveredTransactionReplayer.run(RecoveredTransactionReplayer.java:118) [jbossjts-4.16.1.Final.jar:]

            15:55:55,179 WARN  [com.arjuna.ats.jts] (Periodic Recovery) ARJUNA022223: ExtendedResourceRecord.topLevelCommit caught exception: org.omg.CORBA.OBJECT_NOT_EXIST: Server-side Exception: unknown oid
                at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) [:1.6.0_22]
                at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:57) [:1.6.0_22]
                at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) [:1.6.0_22]
                at java.lang.reflect.Constructor.newInstance(Constructor.java:532) [:1.6.0_22]
                at org.jacorb.orb.SystemExceptionHelper.read(SystemExceptionHelper.java:223) [jacorb-2.3.1.jbossorg-1.jar:]
                at org.jacorb.orb.ReplyReceiver.getReply(ReplyReceiver.java:319) [jacorb-2.3.1.jbossorg-1.jar:]
                at org.jacorb.orb.Delegate.invoke_internal(Delegate.java:1090) [jacorb-2.3.1.jbossorg-1.jar:]
                at org.jacorb.orb.Delegate.invoke(Delegate.java:957) [jacorb-2.3.1.jbossorg-1.jar:]
                at org.omg.CORBA.portable.ObjectImpl._invoke(ObjectImpl.java:80) [jacorb-2.3.1.jbossorg-1.jar:]
                at com.arjuna.ArjunaOTS._ArjunaSubtranAwareResourceStub.commit(_ArjunaSubtranAwareResourceStub.java:252) [jbossjts-4.16.1.Final.jar:]
                at com.arjuna.ats.internal.jts.resources.ExtendedResourceRecord.topLevelCommit(ExtendedResourceRecord.java:502) [jbossjts-4.16.1.Final.jar:]
                at com.arjuna.ats.arjuna.coordinator.BasicAction.doCommit(BasicAction.java:2753) [jbossjts-4.16.1.Final.jar:]
                at com.arjuna.ats.arjuna.coordinator.BasicAction.doCommit(BasicAction.java:2669) [jbossjts-4.16.1.Final.jar:]
                at com.arjuna.ats.arjuna.coordinator.BasicAction.phase2Commit(BasicAction.java:1804) [jbossjts-4.16.1.Final.jar:]
                at com.arjuna.ats.internal.jts.recovery.transactions.RecoveredTransaction.replayPhase2(RecoveredTransaction.java:197) [jbossjts-4.16.1.Final.jar:]
                at com.arjuna.ats.internal.jts.recovery.transactions.TransactionCache.replayPhase2(TransactionCache.java:233) [jbossjts-4.16.1.Final.jar:]
                at com.arjuna.ats.internal.jts.recovery.transactions.CachedRecoveredTransaction.replayPhase2(CachedRecoveredTransaction.java:173) [jbossjts-4.16.1.Final.jar:]
                at com.arjuna.ats.internal.jts.recovery.transactions.TransactionRecoveryModule.recoverTransaction(TransactionRecoveryModule.java:217) [jbossjts-4.16.1.Final.jar:]
                at com.arjuna.ats.internal.jts.recovery.transactions.TransactionRecoveryModule.periodicWorkSecondPass(TransactionRecoveryModule.java:161) [jbossjts-4.16.1.Final.jar:]
                at com.arjuna.ats.internal.jts.recovery.transactions.TopLevelTransactionRecoveryModule.periodicWorkSecondPass(TopLevelTransactionRecoveryModule.java:81) [jbossjts-4.16.1.Final.jar:]
                at com.arjuna.ats.internal.arjuna.recovery.PeriodicRecovery.doWorkInternal(PeriodicRecovery.java:789) [jbossjts-4.16.1.Final.jar:]
                at com.arjuna.ats.internal.arjuna.recovery.PeriodicRecovery.run(PeriodicRecovery.java:371) [jbossjts-4.16.1.Final.jar:]
    * The easiest way to check when JBoss EAP server 1 is recovered is to look in the object store and check that all the records are now cleaned up. The records that should be cleared are the ones in the defaultStore/CosTransactions/XAResourceRecord and defaultStore/StateManager/BasicAction/TwoPhaseCoordinator/ArjunaTransactionImple. 
    * Records will remain in defaultStore/Recovery/FactoryContact and defaultStore/RecoveryCoordinator and that is to be expected. Run:

            tree server*/standalone/data/tx-object-store
    * You should see this output:

            server1/standalone/data/tx-object-store
            `-- ShadowNoFileLockStore
                `-- defaultStore
                    |-- CosTransactions
                    |   `-- XAResourceRecord
                    |-- Recovery
                    |   `-- FactoryContact
                    |       |-- 0_ffffc0a8013c_38e104bd_4f280cdb_17
                    |       |-- 0_ffffc0a8013c_-671009a_4f280e7e_17
                    |       `-- 0_ffffc0a8013c_6d5d82b5_4f280a16_f
                    |-- RecoveryCoordinator
                    |   `-- 0_ffff52e38d0c_c91_4140398c_0
                    `-- StateManager
                        `-- BasicAction
                            `-- TwoPhaseCoordinator
                              `-- ArjunaTransactionImple
            server2/standalone/data/tx-object-store
            `-- ShadowNoFileLockStore
                `-- defaultStore
                    |-- CosTransactions
                    |   `-- XAResourceRecord
                    |-- Recovery
                    |   `-- FactoryContact
                    |       |-- 0_ffffc0a8013c_-2eb1158b_4f280ce3_18
                    |       `-- 0_ffffc0a8013c_4f6459f0_4f280a24_f
                    |-- RecoveryCoordinator
                    |   `-- 0_ffff52e38d0c_c91_4140398c_0
                    `-- StateManager
                        `-- BasicAction
                            `-- TwoPhaseCoordinator
                                `-- ArjunaTransactionImple
                                    `-- ServerTransaction

7. After recovery is complete, access the application URL <http://localhost:8080/jboss-jts-application-component-1/customers.jsf>. The user you created should now appear in the list.

8. Do NOT forget to [Disable the Byteman script](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_BYTEMAN.md#disable-the-byteman-script) by restoring the backup server configuration file. The Byteman rule must be removed to ensure that your application server will be able to commit 2PC transactions!
