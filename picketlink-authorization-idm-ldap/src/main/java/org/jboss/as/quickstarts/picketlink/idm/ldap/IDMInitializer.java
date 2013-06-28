/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.as.quickstarts.picketlink.idm.ldap;

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
import static org.jboss.as.quickstarts.picketlink.idm.ldap.ApplicationRole.ADMINISTRATOR;
import static org.jboss.as.quickstarts.picketlink.idm.ldap.ApplicationRole.DEVELOPER;
import static org.jboss.as.quickstarts.picketlink.idm.ldap.ApplicationRole.PROJECT_MANAGER;

@Startup
@Singleton
public class IDMInitializer {

    @Inject
    private IdentityManager identityManager;

    /**
     * <p>Initializes the identity store with some default users and roles.</p>
     */
    @PostConstruct
    public void createDefaultUsers() {
        createUser("admin", ADMINISTRATOR);
        createUser("john", PROJECT_MANAGER);
        createUser("kate", DEVELOPER);
    }

    private void createUser(String loginName, ApplicationRole roleName) {
        User user = this.identityManager.getUser(loginName);

        if (user == null) {
            user = new SimpleUser(loginName);

            this.identityManager.add(user);

            Password password = new Password(loginName + "123");

            this.identityManager.updateCredential(user, password);
        }

        Role role = this.identityManager.getRole(roleName.name());

        if (role == null) {
            role = new SimpleRole(roleName.name());

            this.identityManager.add(role);
        }

        if (!this.identityManager.hasRole(user, role)) {
            this.identityManager.grantRole(user, role);
        }
    }

}
