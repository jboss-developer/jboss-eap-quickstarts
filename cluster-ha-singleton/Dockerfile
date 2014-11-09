# Use latest jbosseapquickstarts/base image as the base
# This base image includes JBoss EAP
FROM jbosseapquickstarts/base:latest

# Apply any updates from the filesystem since the base image was build
WORKDIR /opt/jboss/jboss-eap-quickstarts/cluster-ha-singleton
ADD . /opt/jboss/jboss-eap-quickstarts/cluster-ha-singleton

# Fix permissions on jboss-eap-quickstarts/cluster-ha-singleton
USER root
RUN chown jboss /opt/jboss/jboss-eap-quickstarts/cluster-ha-singleton
USER jboss

# Build the quickstart
RUN mvn clean package

# Copy the created war file to the JBoss EAP deployments file, so that is deployed when JBoss EAP starts
RUN cp target/jboss-cluster-ha-singleton.war /opt/jboss/jboss-eap/standalone/deployments/

