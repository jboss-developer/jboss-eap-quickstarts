FROM    jboss/wildfly
RUN     /opt/jboss/wildfly/bin/add-user.sh admin admin --silent
ADD     ./helloworld-html5/target/jboss-helloworld-html5.war /opt/jboss/wildfly/standalone/deployments/
