/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

    /**
     * This method uses the IdentityConfigurationBuilder to create an IdentityConfiguration, which 
     * defines how PicketLink stores identity-related data.  In this particular example, a 
     * JPAIdentityStore is configured to allow the identity data to be stored in a relational database
     * using JPA.
     */
    private void initConfig() {
        IdentityConfigurationBuilder builder = new IdentityConfigurationBuilder();
        builder
            .stores()
                .jpa()
                    //Each entity bean that is designed to hold identity data is configured via a 
                    //specific method, as follows:

                    // Specify the entity bean class used to hold user, group and role records
                   .identityClass(IdentityObject.class)

                   // Specify the entity bean class used to hold credential values
                   .credentialClass(CredentialObject.class)

                   // Specify the entity bean class used to hold credential attributes
                   .credentialAttributeClass(CredentialObjectAttribute.class)

                   // Specify the entity bean class used to hold ad-hoc identity attribute values
                   .attributeClass(IdentityObjectAttribute.class)

                   // Specify the entity bean class used to define inter-identity relationships
                   .relationshipClass(RelationshipObject.class)

                   // Specify the entity bean class used to hold references to the identities that
                   // take part in a relationship
                   .relationshipIdentityClass(RelationshipIdentityObject.class)

                   // Specify the entity bean class used to hold relationship attribute values
                   .relationshipAttributeClass(RelationshipObjectAttribute.class)

                   // Specify the entity bean class used to hold partition (i.e. realm and tier) related
                   // data
                   .partitionClass(PartitionObject.class)

                   // Specify that this identity store configuration supports all features 
                   .supportAllFeatures();

        identityConfig = builder.build();
    }
}
