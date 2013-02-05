Configuration
=============

Unzip AS 7.1.3

Create user
./bin/add-user.sh

application user
ejb
ejbejb


Add to security-realms:

            <security-realm name="ejb-security-realm">
                <server-identities>
                    <secret value="ZWpiZWpi"/>
                </server-identities>
            </security-realm>


To remoting subsystem:

            <outbound-connections>
                <remote-outbound-connection name="remote-ejb-connection" outbound-socket-binding-ref="remote-ejb" username="ejb" security-realm="ejb-security-realm">
                    <properties>
                        <property name="SASL_POLICY_NOANONYMOUS" value="false"/>
                        <property name="SSL_ENABLED" value="false"/>
                    </properties>
                </remote-outbound-connection>
            </outbound-connections>

To socket outbound:

        <outbound-socket-binding name="remote-ejb">
            <remote-destination host="127.0.0.1" port="4447"/>
        </outbound-socket-binding>


Deploy
======

        mvn clean install jboss-as:deploy
        mvn jboss-as:deploy -Djboss-as.port=10099

then restart, see below


Testing
=======

invocations are done on the same object

http://localhost:8080/jboss-as-cluster-singletonbean-web/
http://localhost:8180/jboss-as-cluster-singletonbean-web/


Known issues
============

concurrent startup doesnt work (hm, why)

https://issues.jboss.org/browse/AS7-4755

ejb client:

invocation takes 5 seconds on non-owning node if the master is down
[rhusar@x220 jboss-as-7.1.3.Final2]$ time curl http://localhost:8080/jboss-as-cluster-singletonbean-web/
You are accessing: org.jboss.as.quickstarts.cluster.singletonbean.service.ClusterwideSingletonBean@7837b716
Number of invocations: 21
Originally created on node: x220-node2

real	0m5.019s

proxy is not updated with current owner