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
package org.jboss.as.quickstarts.picketlink.idm.totp.jsf;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import org.jboss.as.quickstarts.picketlink.idm.totp.jsf.model.CredentialObject;
import org.jboss.as.quickstarts.picketlink.idm.totp.jsf.model.CredentialObjectAttribute;
import org.jboss.as.quickstarts.picketlink.idm.totp.jsf.model.IdentityObject;
import org.jboss.as.quickstarts.picketlink.idm.totp.jsf.model.IdentityObjectAttribute;
import org.jboss.as.quickstarts.picketlink.idm.totp.jsf.model.PartitionObject;
import org.jboss.as.quickstarts.picketlink.idm.totp.jsf.model.RelationshipIdentityObject;
import org.jboss.as.quickstarts.picketlink.idm.totp.jsf.model.RelationshipObject;
import org.jboss.as.quickstarts.picketlink.idm.totp.jsf.model.RelationshipObjectAttribute;
import org.picketlink.IdentityConfigurationEvent;
import org.picketlink.idm.config.IdentityConfigurationBuilder;

@ApplicationScoped
public class IDMConfiguration {

    public enum REALM {acme, umbrella, wayne}

    /**
     * <p>
     *     We use this method to observe for {@link IdentityConfigurationEvent} to manually provide the IDM configuration
     *     with the realms for each company.
     * </p>
     *
     * @param event
     */
    public void configure(@Observes IdentityConfigurationEvent event) {
        IdentityConfigurationBuilder builder = event.getConfig();

        builder
            .stores()
                .jpa()
                    // here we configure the realms for each company
                    .addRealm(REALM.acme.name(), REALM.umbrella.name(), REALM.wayne.name())
                    .identityClass(IdentityObject.class)
                    .credentialClass(CredentialObject.class)
                    .credentialAttributeClass(CredentialObjectAttribute.class)
                    .attributeClass(IdentityObjectAttribute.class)
                    .relationshipClass(RelationshipObject.class)
                    .relationshipIdentityClass(RelationshipIdentityObject.class)
                    .relationshipAttributeClass(RelationshipObjectAttribute.class)
                    .partitionClass(PartitionObject.class)
                    .supportAllFeatures();
    }

    @Produces
    @Named("supportedRealms")
    public Enum[] supportedRealms() {
        return REALM.values();
    }
}
