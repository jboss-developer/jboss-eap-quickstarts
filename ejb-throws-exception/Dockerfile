# Use latest jbosseapquickstarts/base image as the base
# This base image includes JBoss EAP
FROM jbosseapquickstarts/base:latest

# Apply any updates from the filesystem since the base image was build
WORKDIR /opt/jboss/jboss-eap-quickstarts/ejb-throws-exception
ADD . /opt/jboss/jboss-eap-quickstarts/ejb-throws-exception

# Fix permissions on jboss-eap-quickstarts/ejb-throws-exception
USER root
RUN chown jboss /opt/jboss/jboss-eap-quickstarts/ejb-throws-exception
USER jboss

# Build the quickstart
RUN mvn clean package

# Copy the created war file to the JBoss EAP deployments file, so that is deployed when JBoss EAP starts
RUN cp target/jboss-ejb-throws-exception.war /opt/jboss/jboss-eap/standalone/deployments/

