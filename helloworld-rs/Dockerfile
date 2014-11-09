# Use latest jbosseapquickstarts/base image as the base
# This base image includes JBoss EAP
FROM jbosseapquickstarts/base:latest

# Apply any updates from the filesystem since the base image was build
WORKDIR /opt/jboss/jboss-eap-quickstarts/helloworld-rs
ADD . /opt/jboss/jboss-eap-quickstarts/helloworld-rs

# Fix permissions on jboss-eap-quickstarts/helloworld-rs
USER root
RUN chown jboss /opt/jboss/jboss-eap-quickstarts/helloworld-rs
USER jboss

# Build the quickstart
RUN mvn clean package

# Copy the created war file to the JBoss EAP deployments file, so that is deployed when JBoss EAP starts
RUN cp target/jboss-helloworld-rs.war /opt/jboss/jboss-eap/standalone/deployments/

