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
package org.jboss.as.quickstarts.picketlink.authorization.rest.rbac;

import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.picketlink.Identity;
import org.picketlink.credential.DefaultLoginCredentials;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.model.Role;
import org.picketlink.idm.model.User;
import org.picketlink.idm.query.IdentityQuery;

/**
 *  <p>
 *      Simple JAX-RS Authentication Service that supports username/password credential.
 *  </p>
 */
@Path("/")
public class AuthenticationService {

    @Inject
    private Identity identity;

    @Inject
    private IdentityManager identityManager;

    @Inject
    private DefaultLoginCredentials credentials;

    @POST
    @Path("/authenticate")
    public Response authenticate(DefaultLoginCredentials credential) {
        if (!this.identity.isLoggedIn()) {
            this.credentials.setUserId(credential.getUserId());
            this.credentials.setPassword(credential.getPassword());
            this.identity.login();
        }

        User user = (User) this.identity.getAgent();
        List<Role> roles = getUserRoles(user);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse(user, roles);

        return Response.ok().entity(authenticationResponse).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    private List<Role> getUserRoles(User user) {
        IdentityQuery<Role> query = this.identityManager.createIdentityQuery(Role.class);

        query.setParameter(Role.ROLE_OF, user);

        return query.getResultList();
    }

    private class AuthenticationResponse implements Serializable {

        private static final long serialVersionUID = 1297387771821869087L;

        private User user;
        private List<Role> roles;

        public AuthenticationResponse(User user, List<Role> roles) {
            this.user = user;
            this.roles = roles;
        }

        public User getUser() {
            return this.user;
        }

        public List<Role> getRoles() {
            return this.roles;
        }
    }

}
