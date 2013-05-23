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
package org.jboss.as.quickstarts.picketlink.deltaspike.authorization;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.Role;
import org.picketlink.idm.model.SimpleRole;
import org.picketlink.idm.model.SimpleUser;
import org.picketlink.idm.model.User;

/**
 * This startup bean creates the default users, groups and roles when the application is started.
 * 
 * @author Shane Bryzak
 */
@Singleton
@Startup
public class IDMInitializer {

    @Inject
    private IdentityManager identityManager;

    @PostConstruct
    public void create() {

        // Create user john
        User john = new SimpleUser("john");
        john.setEmail("john@acme.com");
        john.setFirstName("John");
        john.setLastName("Smith");
        identityManager.add(john);
        identityManager.updateCredential(john, new Password("demo"));

        // Create user mary
        User mary = new SimpleUser("mary");
        mary.setEmail("mary@acme.com");
        mary.setFirstName("Mary");
        mary.setLastName("Jones");
        identityManager.add(mary);
        identityManager.updateCredential(mary, new Password("demo"));

        // Create role "employee"
        Role employee = new SimpleRole("employee");
        identityManager.add(employee);

        // Create application role "superuser"
        Role admin = new SimpleRole("admin");
        identityManager.add(admin);

        // Grant the "employee" application role to john
        identityManager.grantRole(john, employee);

        // Grant the "admin" application role to jane
        identityManager.grantRole(mary, admin);
    }
}
