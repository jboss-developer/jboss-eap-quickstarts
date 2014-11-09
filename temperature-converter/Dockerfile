# Use latest jbosseapquickstarts/base image as the base
# This base image includes JBoss EAP
FROM jbosseapquickstarts/base:latest

# Apply any updates from the filesystem since the base image was build
WORKDIR /opt/jboss/jboss-eap-quickstarts/temperature-converter
ADD . /opt/jboss/jboss-eap-quickstarts/temperature-converter

# Fix permissions on jboss-eap-quickstarts/temperature-converter
USER root
RUN chown jboss /opt/jboss/jboss-eap-quickstarts/temperature-converter
USER jboss

# Build the quickstart
RUN mvn clean package

# Copy the created war file to the JBoss EAP deployments file, so that is deployed when JBoss EAP starts
RUN cp target/jboss-temperature-converter.war /opt/jboss/jboss-eap/standalone/deployments/

