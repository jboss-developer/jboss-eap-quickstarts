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
package org.jboss.as.quickstarts.picketlink.idm.partition.jsf;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.internal.IdentityManagerFactory;
import org.picketlink.idm.model.Realm;
import org.picketlink.idm.model.SimpleUser;
import org.picketlink.idm.model.User;
import static org.jboss.as.quickstarts.picketlink.idm.partition.jsf.IDMConfiguration.REALM.acme;
import static org.jboss.as.quickstarts.picketlink.idm.partition.jsf.IDMConfiguration.REALM.umbrella;
import static org.jboss.as.quickstarts.picketlink.idm.partition.jsf.IDMConfiguration.REALM.wayne;

@Startup
@Singleton
public class IDMInitializer {

    @Inject
    private IdentityManagerFactory identityManagerFactory;

    /**
     * <p>Creates some default users for each realm/company.</p>
     */
    @PostConstruct
    public void createDefaultUsers() {
        createUserForRealm(acme.name(), "bugs");
        createUserForRealm(umbrella.name(), "jill");
        createUserForRealm(wayne.name(), "bruce");
    }

    private void createUserForRealm(String realmName, String loginName) {
        Realm acmeRealm = this.identityManagerFactory.getRealm(realmName);
        IdentityManager identityManager = this.identityManagerFactory.createIdentityManager(acmeRealm);

        User user = new SimpleUser(loginName);
        Password password = new Password(user.getLoginName() + "123");

        identityManager.add(user);
        identityManager.updateCredential(user, password);
    }

}
