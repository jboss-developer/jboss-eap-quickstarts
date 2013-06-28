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
package org.jboss.as.quickstarts.picketlink.idm.ldap.jsf;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import org.picketlink.idm.config.IdentityConfiguration;
import org.picketlink.idm.config.IdentityConfigurationBuilder;

@ApplicationScoped
public class IDMConfiguration {

    private static final String BASE_DN = "dc=jboss,dc=org";
    private static final String LDAP_URL = "ldap://localhost:10389";
    private static final String ROLES_DN_SUFFIX = "ou=Roles,dc=jboss,dc=org";
    private static final String GROUP_DN_SUFFIX = "ou=Groups,dc=jboss,dc=org";
    private static final String USER_DN_SUFFIX = "ou=People,dc=jboss,dc=org";
    private static final String AGENT_DN_SUFFIX = "ou=Agent,dc=jboss,dc=org";

    /**
     * <p>
     *     We use this method to produce a {@link IdentityConfiguration} configured with a LDAP store.
     * </p>
     *
     * @param event
     */
    @Produces
    public IdentityConfiguration configure() {
        IdentityConfigurationBuilder builder = new IdentityConfigurationBuilder();

        builder
            .stores()
                .ldap()
                .baseDN(BASE_DN)
                .bindDN("uid=admin,ou=system")
                .bindCredential("secret")
                .url(LDAP_URL)
                .userDNSuffix(USER_DN_SUFFIX)
                .roleDNSuffix(ROLES_DN_SUFFIX)
                .agentDNSuffix(AGENT_DN_SUFFIX)
                .groupDNSuffix(GROUP_DN_SUFFIX)
                .supportAllFeatures();

        return builder.build();
    }

}
