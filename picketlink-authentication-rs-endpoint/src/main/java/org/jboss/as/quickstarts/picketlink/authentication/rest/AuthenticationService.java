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
package org.jboss.as.quickstarts.picketlink.authentication.rest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.picketlink.Identity;
import org.picketlink.credential.DefaultLoginCredentials;
import org.picketlink.idm.model.User;

/**
 *  <p>
 *      Simple JAX-RS Authentication Service that knows how to handle different credential types.
 *  </p>
 */
@Path("/authenticate")
public class AuthenticationService {

    public static final String USERNAME_PASSWORD_CREDENTIAL_CONTENT_TYPE = "application/x-authc-username-password+json";
    public static final String TOKEN_CONTENT_CREDENTIAL_TYPE = "application/x-authc-token";

    @Inject
    private Identity identity;

    @Inject
    private DefaultLoginCredentials credentials;

    @POST
    @Consumes({USERNAME_PASSWORD_CREDENTIAL_CONTENT_TYPE})
    public Response authenticate(DefaultLoginCredentials credential) {
        if (!this.identity.isLoggedIn()) {
            this.credentials.setUserId(credential.getUserId());
            this.credentials.setPassword(credential.getPassword());
            this.identity.login();
        }

        User user = (User) this.identity.getAgent();

        return Response.ok().entity(user).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    @POST
    @Consumes({TOKEN_CONTENT_CREDENTIAL_TYPE})
    public Response authenticate(String token) {
        if (!this.identity.isLoggedIn()) {
            SimpleTokenCredential credential = new SimpleTokenCredential(token);

            this.credentials.setCredential(credential);

            this.identity.login();
        }

        User user = (User) this.identity.getAgent();

        return Response.ok().entity(user).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    @POST
    @Consumes({ "*/*" })
    public Response unsupportedCredentialType() {
        return Response.status(Response.Status.UNSUPPORTED_MEDIA_TYPE).build();
    }

}
