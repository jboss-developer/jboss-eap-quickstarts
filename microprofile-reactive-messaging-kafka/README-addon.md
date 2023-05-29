# EDITS ADDED to README


The following code has been built using:
- Apache Maven 3.9.1
- Java openjdk version "20.0.1"
- EAP 7.4.10 - TODO: removing legacy security referencies
- EAP XP 4.0.0


To enable MicroProfile Reactive Messaging and Config first install EAP XP (EAP XP T&C and Support Life Cycle properties apply in place of EAP ones), and apply the latest patch.

```
java -jar jboss-eap-xp-4.0.0-manager.jar setup --jboss-home=/path/to/EAP/installation/jboss-eap-7.4 --jboss-config-directory=/path/to/eap_kafka_instance/configuration --xp-patch=/path/to/jboss-eap-xp-4.0.0-patch.zip
```

Then launch the standalone server:

```
sh /path/to/EAP/installation/jboss-eap-7.4/bin/standalone.sh -Djboss.server.base.dir=/path/to/eap_kafka_instance -c standalone-microprofile-ha.xml
```

Once the server has been launched, please enable useful logging and proper extensions via EAP CLI:

```
batch
/subsystem=logging/console-handler=CONSOLE:add(level=INFO, enabled=true)
/subsystem=logging/root-logger=ROOT:add-handler(name=CONSOLE)
/extension=org.wildfly.extension.microprofile.reactive-messaging-smallrye:add
/extension=org.wildfly.extension.microprofile.reactive-streams-operators-smallrye:add
/subsystem=microprofile-reactive-streams-operators-smallrye:add
/subsystem=microprofile-reactive-messaging-smallrye:add
run-batch
reload
```

In a new terminal window, launch Kafka typing:

``` 
docker-compose up
```

and wait for the Kafka instance to be up.



Launch Maven to build to deploy the application:

```
mvn clean package wildfly:deploy
```

and check that messages are consumed by topic *testing*.


After some time, override *src/main/resources/META-INF/microprofile-config.properties* by adding configurations to EAP instance via CLI:

```
/subsystem=microprofile-config-smallrye/config-source=test-config-source:add()
/subsystem=microprofile-config-smallrye/config-source=test-config-source:map-put(name=properties, key=mp.messaging.connector.smallrye-kafka.bootstrap.servers, value=localhost:9092)
/subsystem=microprofile-config-smallrye/config-source=test-config-source:map-put(name=properties,key=mp.messaging.incoming.from-kafka.topic, value=myNewTopic)
/subsystem=microprofile-config-smallrye/config-source=test-config-source:map-put(name=properties,key=mp.messaging.outgoing.to-kafka.topic, value=myNewTopic)
reload
```

and check that messages are now sent and consumed from topic *myNewTopic*.


 
