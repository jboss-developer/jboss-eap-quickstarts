/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
* as indicated by the @author tags. See the copyright.txt file in the
* distribution for a full listing of individual contributors.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/
package com.acme.corp.tracker.extension;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DESCRIBE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import org.jboss.as.controller.Extension;
import org.jboss.as.controller.ExtensionContext;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.SubsystemRegistration;
import org.jboss.as.controller.descriptions.DescriptionProvider;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.descriptions.common.CommonDescriptions;
import org.jboss.as.controller.parsing.ExtensionParsingContext;
import org.jboss.as.controller.parsing.ParseUtils;
import org.jboss.as.controller.persistence.SubsystemMarshallingContext;
import org.jboss.as.controller.registry.AttributeAccess.Storage;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.OperationEntry;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;
import org.jboss.staxmapper.XMLElementReader;
import org.jboss.staxmapper.XMLElementWriter;
import org.jboss.staxmapper.XMLExtendedStreamReader;
import org.jboss.staxmapper.XMLExtendedStreamWriter;


/**
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
public class SubsystemExtension implements Extension {

    /** The name space used for the {@code subsystem} element */
    public static final String NAMESPACE = "urn:com.acme.corp.tracker:1.0";

    /** The name of our subsystem within the model. */
    public static final String SUBSYSTEM_NAME = "tracker";

    /** The parser used for parsing our subsystem */
    private final SubsystemParser parser = new SubsystemParser();

    @Override
    public void initializeParsers(ExtensionParsingContext context) {
        context.setSubsystemXmlMapping(NAMESPACE, parser);
    }

    @Override
    public void initialize(ExtensionContext context) {
        final SubsystemRegistration subsystem = context.registerSubsystem(SUBSYSTEM_NAME);
        final ManagementResourceRegistration registration = subsystem.registerSubsystemModel(SubsystemProviders.SUBSYSTEM);
        //We always need to add an 'add' operation
        registration.registerOperationHandler(ADD, SubsystemAdd.INSTANCE, SubsystemProviders.SUBSYSTEM_ADD, false);
        //We always need to add a 'describe' operation
        registration.registerOperationHandler(DESCRIBE, SubsystemDescribeHandler.INSTANCE, SubsystemDescribeHandler.INSTANCE, false, OperationEntry.EntryType.PRIVATE);

        //Add the type child
        ManagementResourceRegistration typeChild = registration.registerSubModel(PathElement.pathElement("type"), SubsystemProviders.TYPE_CHILD);
        typeChild.registerOperationHandler(ModelDescriptionConstants.ADD, TypeAddHandler.INSTANCE, TypeAddHandler.INSTANCE);
        typeChild.registerOperationHandler(ModelDescriptionConstants.REMOVE, TypeRemoveHandler.INSTANCE, TypeRemoveHandler.INSTANCE);
        typeChild.registerReadWriteAttribute("tick", null, TrackerTickHandler.INSTANCE, Storage.CONFIGURATION);

        subsystem.registerXMLElementWriter(parser);
    }

    private static ModelNode createAddSubsystemOperation() {
        final ModelNode subsystem = new ModelNode();
        subsystem.get(OP).set(ADD);
        subsystem.get(OP_ADDR).add(SUBSYSTEM, SUBSYSTEM_NAME);
        return subsystem;
    }

    /**
     * The subsystem parser, which uses stax to read and write to and from xml
     */
    private static class SubsystemParser implements XMLStreamConstants, XMLElementReader<List<ModelNode>>, XMLElementWriter<SubsystemMarshallingContext> {

        /** {@inheritDoc} */
        @Override
        public void readElement(XMLExtendedStreamReader reader, List<ModelNode> list) throws XMLStreamException {
            // Require no attributes
            ParseUtils.requireNoAttributes(reader);

            //Add the main subsystem 'add' operation
            list.add(createAddSubsystemOperation());

            //Read the children
            while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
                if (!reader.getLocalName().equals("deployment-types")) {
                    throw ParseUtils.unexpectedElement(reader);
                }
                while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
                    if (reader.isStartElement()) {
                        readDeploymentType(reader, list);
                    }
                }
            }
        }

        private void readDeploymentType(XMLExtendedStreamReader reader, List<ModelNode> list) throws XMLStreamException {
            if (!reader.getLocalName().equals("deployment-type")) {
                throw ParseUtils.unexpectedElement(reader);
            }
            String suffix = null;
            Long tick = null;
            for (int i = 0 ; i < reader.getAttributeCount() ; i++) {
                String attr = reader.getAttributeLocalName(i);
                if (attr.equals("tick")){
                    tick = Long.valueOf(reader.getAttributeValue(i));
                } else if (attr.equals("suffix")){
                    suffix = reader.getAttributeValue(i);
                } else {
                    throw ParseUtils.unexpectedAttribute(reader, i);
                }
            }
            ParseUtils.requireNoContent(reader);
            if (suffix == null) {
                throw ParseUtils.missingRequiredElement(reader, Collections.singleton("suffix"));
            }

            //Add the 'add' operation for each 'type' child
            ModelNode addType = new ModelNode();
            addType.get(OP).set(ModelDescriptionConstants.ADD);
            PathAddress addr = PathAddress.pathAddress(PathElement.pathElement(SUBSYSTEM, SUBSYSTEM_NAME), PathElement.pathElement("type", suffix));
            addType.get(OP_ADDR).set(addr.toModelNode());
            if (tick != null) {
                addType.get("tick").set(tick);
            }
            list.add(addType);
        }

        /** {@inheritDoc} */
        @Override
        public void writeContent(final XMLExtendedStreamWriter writer, final SubsystemMarshallingContext context) throws XMLStreamException {

            //Write out the main subsystem element
            context.startSubsystemElement(SubsystemExtension.NAMESPACE, false);

            writer.writeStartElement("deployment-types");

            ModelNode node = context.getModelNode();
            ModelNode type = node.get("type");
            for (Property property : type.asPropertyList()) {

                //write each child element to xml
                writer.writeStartElement("deployment-type");
                writer.writeAttribute("suffix", property.getName());
                ModelNode entry = property.getValue();
                if (entry.hasDefined("tick")) {
                    writer.writeAttribute("tick", entry.get("tick").asString());
                }
                writer.writeEndElement();
            }

            //End deployment-types
            writer.writeEndElement();
            //End subsystem
            writer.writeEndElement();
        }
    }


    /**
     * Recreate the steps to put the subsystem in the same state it was in.
     * This is used in domain mode to query the profile being used, in order to
     * get the steps needed to create the servers
     */
    private static class SubsystemDescribeHandler implements OperationStepHandler, DescriptionProvider {
        static final SubsystemDescribeHandler INSTANCE = new SubsystemDescribeHandler();

        public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {
            //Add the main operation
            context.getResult().add(createAddSubsystemOperation());

            //Add the operations to create each child

            ModelNode node = context.readModel(PathAddress.EMPTY_ADDRESS);
            for (Property property : node.get("type").asPropertyList()) {

                ModelNode addType = new ModelNode();
                addType.get(OP).set(ModelDescriptionConstants.ADD);
                PathAddress addr = PathAddress.pathAddress(PathElement.pathElement(SUBSYSTEM, SUBSYSTEM_NAME), PathElement.pathElement("type", property.getName()));
                addType.get(OP_ADDR).set(addr.toModelNode());
                if (property.getValue().hasDefined("tick")) {
                    addType.get("tick").set(property.getValue().get("tick").asLong());
                }
                context.getResult().add(addType);
            }
            context.completeStep();
        }

        @Override
        public ModelNode getModelDescription(Locale locale) {
            return CommonDescriptions.getSubsystemDescribeOperation(locale);
        }
    }

}
