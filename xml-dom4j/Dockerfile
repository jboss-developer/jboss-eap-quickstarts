# Use latest jbosseapquickstarts/base image as the base
# This base image includes JBoss EAP
FROM jbosseapquickstarts/base:latest

# Apply any updates from the filesystem since the base image was build
WORKDIR /opt/jboss/jboss-eap-quickstarts/xml-dom4j
ADD . /opt/jboss/jboss-eap-quickstarts/xml-dom4j

# Fix permissions on jboss-eap-quickstarts/xml-dom4j
USER root
RUN chown jboss /opt/jboss/jboss-eap-quickstarts/xml-dom4j
USER jboss

# Build the quickstart
RUN mvn clean package

# Copy the created war file to the JBoss EAP deployments file, so that is deployed when JBoss EAP starts
RUN cp target/jboss-xml-dom4j.war /opt/jboss/jboss-eap/standalone/deployments/

