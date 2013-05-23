package org.jboss.as.quickstarts.picketlink.authorization.idm.jpa;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.jboss.as.quickstarts.picketlink.authorization.idm.jpa.model.CredentialObject;
import org.jboss.as.quickstarts.picketlink.authorization.idm.jpa.model.CredentialObjectAttribute;
import org.jboss.as.quickstarts.picketlink.authorization.idm.jpa.model.IdentityObject;
import org.jboss.as.quickstarts.picketlink.authorization.idm.jpa.model.IdentityObjectAttribute;
import org.jboss.as.quickstarts.picketlink.authorization.idm.jpa.model.PartitionObject;
import org.jboss.as.quickstarts.picketlink.authorization.idm.jpa.model.RelationshipIdentityObject;
import org.jboss.as.quickstarts.picketlink.authorization.idm.jpa.model.RelationshipObject;
import org.jboss.as.quickstarts.picketlink.authorization.idm.jpa.model.RelationshipObjectAttribute;
import org.picketlink.idm.config.IdentityConfiguration;
import org.picketlink.idm.config.IdentityConfigurationBuilder;

/**
 * This bean produces the configuration for PicketLink IDM
 * 
 * 
 * @author Shane Bryzak
 *
 */
@ApplicationScoped
public class IDMConfiguration {

    private IdentityConfiguration identityConfig = null;

    @Produces IdentityConfiguration createConfig() {
        if (identityConfig == null) {
            initConfig();
        }
        return identityConfig;
    }

    private void initConfig() {
        IdentityConfigurationBuilder builder = new IdentityConfigurationBuilder();
        builder
            .stores()
                .jpa()
                   .identityClass(IdentityObject.class)
                   .credentialClass(CredentialObject.class)
                   .credentialAttributeClass(CredentialObjectAttribute.class)
                   .attributeClass(IdentityObjectAttribute.class)
                   .relationshipClass(RelationshipObject.class)
                   .relationshipIdentityClass(RelationshipIdentityObject.class)
                   .relationshipAttributeClass(RelationshipObjectAttribute.class)
                   .partitionClass(PartitionObject.class)
                   .supportAllFeatures();

        identityConfig = builder.build();
    }
}
