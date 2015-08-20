batch-processing: Chunk oriented Batch 1.0 processing
=====================================================
Author: Rafael Benevides  
Level: Intermediate  
Technologies: CDI, Batch 1.0, JSF  
Summary: Shows how to use chunk oriented batch jobs to import a file to a database.  
Target Product: EAP  
Source: <https://github.com/jboss-developer/jboss-eap-quickstarts>   


What is it?
-----------

This quickstart simulates a file importation using batch jobs. To make it easy, this quickstart offers the user a way to generate files. The generate file can have its name and the number of records customized. The user may also specify if the file contains an error or not.

The Job contains two steps (tasks):

1. Import the file (Chunk oriented) - The chunk size was set to `3`. The `RecordsReaderRecordsReader` is responsible for parsing the file and create an instance of `Contact`. The `ContactsFormatter` applies the proper case to the Contact name and it also applies a mask to the phone number. Finally, `ContactsPersister` will send the Contact instance to the Database. 
2. Log the number of records imported

The database schema defines that the column for name is unique. For that reason, any atempt to persist a duplicate value will throw an exception. On the second attempt to run the job, the `ChunkCheckpoint` will provide information to skip the Contacts that were already persisted.  

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 7 or later. 

All you need to build this project is Java 8.0 (Java SDK 1.8) or later and Maven 3.1.1 or later. See [Configure Maven for JBoss EAP 7](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


Use of EAP7_HOME
---------------

In the following instructions, replace `EAP7_HOME` with the actual path to your JBoss EAP installation. The installation path is described in detail here: [Use of EAP7_HOME and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_EAP7_HOME.md#use-of-eap_home-and-jboss_home-variables).



Start the JBoss EAP Server
-------------------------

1. Open a command line and navigate to the root of the  JBoss EAP directory.
2. The following shows the command line to start the server with the default profile:

        For Linux:   EAP7_HOME/bin/standalone.sh
        For Windows: EAP7_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn package wildfly:deploy
4. This will deploy `target/jboss-batch-processing.war` to the running instance of the server.
 


Access the application
---------------------

Access the running application in a browser at the following URL:  <http://localhost:8080/jboss-batch-processing/>

You're presented with a simple form that allows you to generate sample files to be imported. 

### Usage 1: Import the file without any errors ###

Click on `Generate a new file and start import job` button. This will generate a new file with 10 unique records to be imported. After the file is generated, the import job will start.

You will see a table containing information about the task that was just started. You can click on `Update jobs list` button and realize that the job was completed.

#### Investigate the Console Output ####

At the logs you will see that the files with 10 records were processed using 3 records at a time. 

    15:57:40,313 INFO  [org.jboss.as.quickstarts.batch.controller.BatchController] (default task-3) Starting to generate 10 in file /var/folders/j8/63sgdmbn5tqdkyw0tz6df53r0000gn/T/temp-file.txt
    15:57:40,315 INFO  [org.jboss.as.quickstarts.batch.controller.BatchController] (default task-3) File generated at /var/folders/j8/63sgdmbn5tqdkyw0tz6df53r0000gn/T/temp-file.txt
    15:57:40,404 INFO  [org.jboss.as.quickstarts.batch.job.listener.JobListener] (Batch Thread - 1) Job import-file - Execution #1 starting.
    15:57:40,468 INFO  [org.jboss.as.quickstarts.batch.job.ContactsPersister] (Batch Thread - 1) No checkpoint detected. Cleaning the Database
    15:57:40,753 INFO  [org.jboss.as.quickstarts.batch.job.ContactsFormatter] (Batch Thread - 1) Register #1 - Changing name ZIqYKITxiM -> Ziqykitxim | phone  978913851 -> (978)-913-851
    15:57:40,754 INFO  [org.jboss.as.quickstarts.batch.job.ContactsFormatter] (Batch Thread - 1) Register #2 - Changing name JbHjnaThps -> Jbhjnathps | phone  095108018 -> (095)-108-018
    15:57:40,755 INFO  [org.jboss.as.quickstarts.batch.job.ContactsFormatter] (Batch Thread - 1) Register #3 - Changing name FJTlXRtCdR -> Fjtlxrtcdr | phone  286847939 -> (286)-847-939
    15:57:40,755 INFO  [org.jboss.as.quickstarts.batch.job.listener.PersistListener] (Batch Thread - 1) Prearing to persist 3 contacts
    15:57:40,802 INFO  [org.jboss.as.quickstarts.batch.job.listener.PersistListener] (Batch Thread - 1) Persisting 3 contacts
    15:57:40,807 INFO  [org.jboss.as.quickstarts.batch.job.ContactsFormatter] (Batch Thread - 1) Register #4 - Changing name mlmBABWzfL -> Mlmbabwzfl | phone  744478648 -> (744)-478-648
    15:57:40,807 INFO  [org.jboss.as.quickstarts.batch.job.ContactsFormatter] (Batch Thread - 1) Register #5 - Changing name jVlTYiBRMP -> Jvltyibrmp | phone  135063841 -> (135)-063-841
    15:57:40,808 INFO  [org.jboss.as.quickstarts.batch.job.ContactsFormatter] (Batch Thread - 1) Register #6 - Changing name DwEFbSjfQE -> Dwefbsjfqe | phone  404572175 -> (404)-572-175
    15:57:40,809 INFO  [org.jboss.as.quickstarts.batch.job.listener.PersistListener] (Batch Thread - 1) Prearing to persist 3 contacts
    15:57:40,820 INFO  [org.jboss.as.quickstarts.batch.job.listener.PersistListener] (Batch Thread - 1) Persisting 3 contacts
    15:57:40,849 INFO  [org.jboss.as.quickstarts.batch.job.ContactsFormatter] (Batch Thread - 1) Register #7 - Changing name niDXWwGJuQ -> Nidxwwgjuq | phone  949448390 -> (949)-448-390
    15:57:40,850 INFO  [org.jboss.as.quickstarts.batch.job.ContactsFormatter] (Batch Thread - 1) Register #8 - Changing name VZBArfowSe -> Vzbarfowse | phone  902370961 -> (902)-370-961
    15:57:40,851 INFO  [org.jboss.as.quickstarts.batch.job.ContactsFormatter] (Batch Thread - 1) Register #9 - Changing name aSpyWCWwje -> Aspywcwwje | phone  246977695 -> (246)-977-695
    15:57:40,852 INFO  [org.jboss.as.quickstarts.batch.job.listener.PersistListener] (Batch Thread - 1) Prearing to persist 3 contacts
    15:57:40,861 INFO  [org.jboss.as.quickstarts.batch.job.listener.PersistListener] (Batch Thread - 1) Persisting 3 contacts
    15:57:40,864 INFO  [org.jboss.as.quickstarts.batch.job.ContactsFormatter] (Batch Thread - 1) Register #10 - Changing name TofTfbRBzI -> Toftfbrbzi | phone  868339088 -> (868)-339-088
    15:57:40,865 INFO  [org.jboss.as.quickstarts.batch.job.listener.PersistListener] (Batch Thread - 1) Prearing to persist 1 contacts
    15:57:40,868 INFO  [org.jboss.as.quickstarts.batch.job.listener.PersistListener] (Batch Thread - 1) Persisting 1 contacts
    15:57:40,918 INFO  [org.jboss.as.quickstarts.batch.job.ReportBatchelet] (Batch Thread - 1) Imported 10 to Database
    15:57:40,921 INFO  [org.jboss.as.quickstarts.batch.job.listener.JobListener] (Batch Thread - 1) Job import-file - Execution #1 finished. Status: COMPLETED

### Usage 2: Import an error file and fix it ###

Now we will simulate a file with duplicate records. This will raise an exception and stop the processing. After that, we will fix the file and continue the importing where it stopped.

Mark the `Generate a duplicate record` checkbox and click on `Generate a new file and start import job` button. If you click on `Update jobs list` button, you will see that the job failed with the following Exit Status: `Error : org.hibernate.exception.ConstraintViolationException: could not execute statement`. This was caused because the job tried to insert a duplicate record at the Database.

Now we will fix the file and restart that job execution. Uncheck the `Generate a duplicate record` checkbox and click on `Generate a new file` button. This will generate file without errors.

Click on `Restart` button on the `List of Jobs` table. If you  click on `Update jobs list` button, you will realize that the job was completed.

Analyze the logs and check that the job started from the last checkpoint.

    16:08:56,323 INFO  [org.jboss.as.quickstarts.batch.job.RecordsReader] (Batch Thread - 3) Skipping to line 3 as marked by previous checkpoint

#### Investigate the Console Output ####

    16:08:56,317 INFO  [org.jboss.as.quickstarts.batch.job.listener.JobListener] (Batch Thread - 3) Job import-file - Execution #3 starting.
    16:08:56,323 INFO  [org.jboss.as.quickstarts.batch.job.RecordsReader] (Batch Thread - 3) Skipping to line 3 as marked by previous checkpoint
    16:08:56,323 INFO  [org.jboss.as.quickstarts.batch.job.ContactsFormatter] (Batch Thread - 3) Register #4 - Changing name HdeqwzEjbA -> Hdeqwzejba | phone  686417040 -> (686)-417-040
    16:08:56,324 INFO  [org.jboss.as.quickstarts.batch.job.ContactsFormatter] (Batch Thread - 3) Register #5 - Changing name veEEbtpYTJ -> Veeebtpytj | phone  367981821 -> (367)-981-821
    16:08:56,324 INFO  [org.jboss.as.quickstarts.batch.job.ContactsFormatter] (Batch Thread - 3) Register #6 - Changing name bQIKTUyqMW -> Bqiktuyqmw | phone  103363182 -> (103)-363-182
    16:08:56,324 INFO  [org.jboss.as.quickstarts.batch.job.listener.PersistListener] (Batch Thread - 3) Prearing to persist 3 contacts
    16:08:56,330 INFO  [org.jboss.as.quickstarts.batch.job.listener.PersistListener] (Batch Thread - 3) Persisting 3 contacts
    16:08:56,332 INFO  [org.jboss.as.quickstarts.batch.job.ContactsFormatter] (Batch Thread - 3) Register #7 - Changing name KVLIGXhCry -> Kvligxhcry | phone  117327691 -> (117)-327-691
    16:08:56,332 INFO  [org.jboss.as.quickstarts.batch.job.ContactsFormatter] (Batch Thread - 3) Register #8 - Changing name PBAZgernHy -> Pbazgernhy | phone  066203468 -> (066)-203-468
    16:08:56,333 INFO  [org.jboss.as.quickstarts.batch.job.ContactsFormatter] (Batch Thread - 3) Register #9 - Changing name DGtNZdteGB -> Dgtnzdtegb | phone  908779587 -> (908)-779-587
    16:08:56,333 INFO  [org.jboss.as.quickstarts.batch.job.listener.PersistListener] (Batch Thread - 3) Prearing to persist 3 contacts
    16:08:56,339 INFO  [org.jboss.as.quickstarts.batch.job.listener.PersistListener] (Batch Thread - 3) Persisting 3 contacts
    16:08:56,341 INFO  [org.jboss.as.quickstarts.batch.job.ContactsFormatter] (Batch Thread - 3) Register #10 - Changing name mhmIHhZMhv -> Mhmihhzmhv | phone  094518410 -> (094)-518-410
    16:08:56,341 INFO  [org.jboss.as.quickstarts.batch.job.listener.PersistListener] (Batch Thread - 3) Prearing to persist 1 contacts
    16:08:56,343 INFO  [org.jboss.as.quickstarts.batch.job.listener.PersistListener] (Batch Thread - 3) Persisting 1 contacts
    16:08:56,344 WARN  [org.jberet] (Batch Thread - 3) JBERET000018: Could not find the original step execution to restart.  Current step execution id: 0, step name: reportBatchelet
    16:08:56,348 INFO  [org.jboss.as.quickstarts.batch.job.ReportBatchelet] (Batch Thread - 3) Imported 10 to Database
    16:08:56,350 INFO  [org.jboss.as.quickstarts.batch.job.listener.JobListener] (Batch Thread - 3) Job import-file - Execution #3 finished. Status: COMPLETED

### Usage 3: Import an error file and keep it wrong ###

Check the `Generate a duplicate record` checkbox and click on `Generate a new file ans start import job` button. If you click on `Update jobs list` button, you will see that the job failed with the following Exit Status: `Error : org.hibernate.exception.ConstraintViolationException: could not execute statement`. This was caused because we tried to insert a duplicate record at the Database.

This time we won't generate the fixed file. Just click on `Restart` button again. If you  click on `Update jobs list` button, you will realize that the job was marked as `ABANDONED` this time because it was restarted once (Realize that there's a new parameter restartedOnce=true). This behavior was implemented at `JobListener` for demonstration purpose to avoid that a `FAILED` job that was already restarted once, to be restarted twice. 

Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------


You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 


Debug the Application
------------------------------------

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

    mvn dependency:sources
   

<!-- Build and Deploy the Quickstart to OpenShift - Coming soon! -->


