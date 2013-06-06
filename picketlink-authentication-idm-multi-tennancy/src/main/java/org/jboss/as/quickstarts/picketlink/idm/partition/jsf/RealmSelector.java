/*
 * JBoss, Home of Professional Open Source
 *
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.picketlink.idm.partition.jsf;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import org.picketlink.annotations.PicketLink;
import org.picketlink.idm.internal.IdentityManagerFactory;
import org.picketlink.idm.model.Realm;

/**
 * @author pedroigor
 */
@SessionScoped
public class RealmSelector {

    public enum REALM {ACME_CORP, UMBRELLA_CORP, WAYNE_ENT}

    @Inject
    private IdentityManagerFactory identityManagerFactory;

    private Realm realmName;

    @Produces
    @PicketLink
    public Realm select() {
        if (this.realmName == null) {
            this.realmName = Realm.DEFAULT_REALM;
        }

        return this.identityManagerFactory.getRealm(this.realmName);
    }

}
