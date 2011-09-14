package com.acme.corp.tracker.extension;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ATTRIBUTES;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CHILDREN;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DEFAULT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.HEAD_COMMENT_ALLOWED;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.MAX_OCCURS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.MIN_OCCURS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.MODEL_DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAMESPACE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REQUIRED;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.TAIL_COMMENT_ALLOWED;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.TYPE;

import java.util.Locale;

import org.jboss.as.controller.descriptions.DescriptionProvider;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

/**
 * Contains the description providers. The description providers are what print out the
 * information when you execute the {@code read-resource-description} operation.
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
class SubsystemProviders {

    /**
     * Used to create the description of the subsystem
     */
    public static DescriptionProvider SUBSYSTEM = new DescriptionProvider() {
        public ModelNode getModelDescription(Locale locale) {
            //The locale is passed in so you can internationalize the strings used in the descriptions

            final ModelNode subsystem = new ModelNode();
            //Change the description
            subsystem.get(DESCRIPTION).set("This subsystem tracks deployments of a given type");
            subsystem.get(HEAD_COMMENT_ALLOWED).set(true);
            subsystem.get(TAIL_COMMENT_ALLOWED).set(true);
            subsystem.get(NAMESPACE).set(SubsystemExtension.NAMESPACE);

            //Add information about the 'type' child
            subsystem.get(CHILDREN, "type", DESCRIPTION).set("Deployment types that should be tracked");
            subsystem.get(CHILDREN, "type", MIN_OCCURS).set(0);
            subsystem.get(CHILDREN, "type", MAX_OCCURS).set(Integer.MAX_VALUE);
            subsystem.get(CHILDREN, "type", MODEL_DESCRIPTION);

            return subsystem;
        }
    };

    /**
     * Used to create the description of the subsystem add method
     */
    public static DescriptionProvider SUBSYSTEM_ADD = new DescriptionProvider() {
        public ModelNode getModelDescription(Locale locale) {
            //The locale is passed in so you can internationalize the strings used in the descriptions

            final ModelNode subsystem = new ModelNode();
            subsystem.get(DESCRIPTION).set("Adds the tracker subsystem");

            return subsystem;
        }
    };

    /**
     * Used to create the description of the {@code type} child
     */
    public static DescriptionProvider TYPE_CHILD = new DescriptionProvider() {
        @Override
        public ModelNode getModelDescription(Locale locale) {
            ModelNode node = new ModelNode();
            node.get(DESCRIPTION).set("Contains information about a tracked deployment type");
            node.get(ATTRIBUTES, "tick", DESCRIPTION).set("How often in milliseconds to output the information about the tracked deployments");
            node.get(ATTRIBUTES, "tick", TYPE).set(ModelType.LONG);
            node.get(ATTRIBUTES, "tick", REQUIRED).set(true);
            node.get(ATTRIBUTES, "tick", DEFAULT).set(10000);
            return node;
        }
    };

}
