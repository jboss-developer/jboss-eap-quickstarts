batch-processing: Chunk oriented Batch 1.0 processing
=====================================================
Author: Rafael Benevides  
Level: Intermediate  
Technologies: CDI, Batch 1.0, JSF  
Summary: Shows how to use chunk oriented batch jobs to import a file to a database.  
Target Product: Sandbox  
Source: <https://github.com/jboss-developer/jboss-sandbox-quickstarts>   


What is it?
-----------

This quickstart simulates a file importation using batch jobs. To make it easy, this quickstart offers the user a way to generate files. The generate file can have its name and the number of records customized. The user may also specify if the file contains an error or not.

The Job contains two steps (tasks):

1. Import the file (Chunk oriented) - The chunk size was set to `3`. The `RecordsReaderRecordsReader` is responsible for parsing the file and create an instance of `Contact`. The `ContactsFormatter` applies the proper case to the Contact name and it also applies a mask to the phone number. Finally, `ContactsPersister` will send the Contact instance to the Database. 
2. Log the number of records imported

The database schema defines that the column for name is unique. For that reason, any atempt to persist a duplicate value will throw an exception. On the second attempt to run the job, the `ChunkCheckpoint` will provide information to skip the Contacts that were already persisted.  

System requirements
-------------------

All you need to build this project is Java 7.0 (Java SDK 1.7) or later, Maven 3.0 or later.
 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.


Use of WILDFLY_HOME
---------------

In the following instructions, replace `WILDFLY_HOME` with the actual path to your WildFly installation. The installation path is described in detail here: [Use of EAP_HOME and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_EAP_HOME.md#use-of-eap_home-and-jboss_home-variables).



Start the WildFly Server
-------------------------

1. Open a command line and navigate to the root of the  WildFly directory.
2. The following shows the command line to start the server with the default profile:

        For Linux:   WILDFLY_HOME/bin/standalone.sh
        For Windows: WILDFLY_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------

1. Make sure you have started the WildFly server as described above.
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

1. Make sure you have started the WildFly server as described above.
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
   

Build and Deploy the Quickstart - to OpenShift
-------------------------

### Create an OpenShift Account and Domain

If you do not yet have an OpenShift account and domain, [Sign in to OpenShift](https://openshift.redhat.com/app/login) to create the account and domain. [Get Started with OpenShift](https://openshift.redhat.com/app/getting_started) will show you how to install the OpenShift Express command line interface.

### Create the OpenShift Application

_NOTE_: The domain name for this application will be `batch-YOUR_DOMAIN_NAME.rhcloud.com`. In these instructions, be sure to replace all instances of `YOUR_DOMAIN_NAME` with your own OpenShift account user name.

Open a shell command prompt and change to a directory of your choice. Enter the following command to create a JBoss EAP 6 application:

       rhc create-app batch jboss-wildfly-8

This command creates an OpenShift application named APPLICATION_NAME and will run the application inside the `jboss-wildfly-8`  container. You should see some output similar to the following:

    Application Options
    -------------------
      Namespace:  YOUR_DOMAIN_NAME
      Cartridges: jboss-wildfly-8
      Gear Size:  default
      Scaling:    no

    Creating application 'batch' ... done

    Waiting for your DNS name to be available ... done

    Cloning into 'batch'...
    Warning: Permanently added the RSA host key for IP address '54.237.58.0' to the list of known hosts.

    Your application 'batch' is now available.

      URL:        http://batch-YOUR_DOMAIN_NAME.rhcloud.com/
      SSH to:     52864af85973ca430200006f@batch-YOUR_DOMAIN_NAME.rhcloud.com
      Git remote: ssh://52864af85973ca430200006f@batch-YOUR_DOMAIN_NAME.rhcloud.com/~/git/APPLICATION_NAME.git/
      Cloned to:  CURRENT_DIRECTORY/APPLICATION_NAME

    Run 'rhc show-app batch' for more details about your app.

The create command creates a git repository in the current directory with the same name as the application. Notice that the output also reports the URL at which the application can be accessed. Make sure it is available by typing the published url <http://batch-YOUR_DOMAIN_NAME.rhcloud.com/> into a browser or use command line tools such as curl or wget. Be sure to replace `YOUR_DOMAIN_NAME` with your OpenShift account domain name.

### Migrate the Quickstart Source

Now that you have confirmed it is working you can migrate the quickstart source. You do not need the generated default application, so navigate to the new git repository directory and tell git to remove the source and pom files:

        cd batch
        git rm -r src pom.xml

Copy the source for the QUICKSTART_NAME quickstart into this new git repository:

        cp -r QUICKSTART_HOME/batch-processing/src .
        cp QUICKSTART_HOME/batch-processing/pom.xml .


### Deploy the OpenShift Application

You can now deploy the changes to your OpenShift application using git as follows:

        git add src pom.xml
        git commit -m "batch quickstart on OpenShift"
        git push

The final push command triggers the OpenShift infrastructure to build and deploy the changes. 

Note that the `openshift` profile in `pom.xml` is activated by OpenShift, and causes the WAR build by openshift to be copied to the `deployments/` directory, and deployed without a context path.

### Test the OpenShift Application

When the push command returns you can test the application by getting the following URL either via a browser or using tools such as curl or wget. Be sure to replace the `YOUR_DOMAIN_NAME` in the URL with your OpenShift account domain name.

        http://batch-YOUR_DOMAIN_NAME.rhcloud.com 

You can use the OpenShift command line tools or the OpenShift web console to discover and control the application.

### View the WildFly Server Log on OpenShift

Now you can look at the output of the server by running the following command:

        rhc tail -a batch

This will show the tail of the JBoss EAP server log.

_Note:_ You may see the following error in the log:

        2014/03/17 07:50:36,231 ERROR [org.jboss.as.controller.management-operation] (management-handler-thread - 4) JBAS014613: Operation ("read-resource") failed - address: ([("subsystem" => "deployment-scanner")]) - failure description: "JBAS014807: Management resource '[(\"subsystem\" => \"deployment-scanner\")]' not found"

This is a benign error that occurs when the status of the deployment is checked too early in the process. This process is retried, so you can safely ignore this error.

### Delete the OpenShift Application

When you are finished with the application you can delete it as follows:

        rhc app-delete -a batch

_Note_: There is a limit to the number of applications you can deploy concurrently to OpenShift. If the `rhc app create` command returns an error indicating you have reached that limit, you must delete an existing application before you continue. 

* To view the list of your OpenShift applications, type: `rhc domain show`
* To delete an application from OpenShift, type the following, substituting the application name you want to delete: `rhc app-delete -a APPLICATION_NAME_TO_DELETE`

