# Use latest jbosseapquickstarts/base image as the base
# This base image includes EAP
FROM jbosseapquickstarts/base:latest

# Apply any updates from the filesystem since the base image was build
WORKDIR /opt/jboss/jboss-eap-quickstarts/kitchensink
ADD . /opt/jboss/jboss-eap-quickstarts/kitchensink/

# Fix permissions on jboss-eap-quickstarts/kitchensink
USER root
RUN chown jboss /opt/jboss/jboss-eap-quickstarts/kitchensink
USER jboss

# Build the quickstart
RUN mvn clean package

# Copy the created war file to the EAP deployments file, so that is deployed when EAP starts
RUN cp target/jboss-kitchensink.war /opt/jboss/jboss-eap/standalone/deployments/

