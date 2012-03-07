# logging-tools-qs: Demonstrating i18n/l10n logging with JBoss Logging Tools

Authors: Darrin Mison dmison@me.com 

(Derived from helloworld-rs by Gustavo A. Brey and Gaston Coco) 

## System requirements

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on a JBoss AS 7 or EAP 6.
The following instructions target JBoss AS 7, but they also apply to JBoss EAP 6.


## What is it?

This example demonstrates the use of JBoss Logging Tools to create internationalized loggers and
then providing localizations for those loggers. This is done in the context of a simple JAX-RS
service with a bean injected using CDI.

Once the quick start is deployed you can access it using the following URLs where name is a 
parameter:

* http://localhost:8080/logging-tools-qs/rest/greetings/`name`
* http://localhost:8080/logging-tools-qs/rest/greetings/`name`/xml
* http://localhost:8080/logging-tools-qs/rest/greetings/`name`/json

Each of these methods returns a 'Hello `name`!' message in different formats and also logs that a
message was sent. The logging is done using JBoss Logging Tools to create a internationalized logger
that is then localized into several languages (French, German, Japanese, Swedish are included).

Instructions are included below for starting JBoss AS 7 with a different locale than the system 
default.

## Building and deploying the application

Follow these steps to build, deploy and run the quickstart.

1. Optional: Configure AS7/EAP6 to start with a different locale.

   To start AS7/EAP6 with a different locale than the system default
   
    1. Edit the `$JBOSS_HOME/bin/standalone.conf` and append the lines to add the parameters for 
    the required country and language.  
    
      Eg. Germany and German, `DE` and `de`.
    
            JAVA_OPTS="$JAVA_OPTS -Duser.country=DE"
            JAVA_OPTS="$JAVA_OPTS -Duser.language=de"

      Refer to http://java.sun.com/developer/technicalArticles/J2SE/locale/.  
      
      TODO: Add a more current reference.

1. Start JBoss AS 7 (or EAP 6):

         $JBOSS_HOME/bin/standalone.sh

2. Build the application

         mvn clean package
	   
3. Deploy the application 

         mvn jboss-as:deploy
	   



