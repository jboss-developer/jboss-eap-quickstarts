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
package org.jboss.as.quickstarts.picketlink.authentication.jsf;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.picketlink.annotations.PicketLink;
import org.picketlink.authentication.BaseAuthenticator;
import org.picketlink.credential.DefaultLoginCredentials;
import org.picketlink.idm.model.SimpleUser;

/**
 * A simple PicketLink authenticator that will accept a hard coded username/password value. The
 * @PicketLink annotation is required to indicate to PicketLink that this is the default Authenticator
 * to be used.
 * 
 * @author Shane Bryzak
 *
 */
@PicketLink
public class SimpleAuthenticator extends BaseAuthenticator {

    @Inject 
    private DefaultLoginCredentials credentials;

    @Inject
    private FacesContext facesContext;

    @Override
    public void authenticate() {
        if ("jsmith".equals(credentials.getUserId()) &&
                "abc123".equals(credentials.getPassword())) {
            setStatus(AuthenticationStatus.SUCCESS);
            setAgent(new SimpleUser("jsmith"));
        } else {
            setStatus(AuthenticationStatus.FAILURE);
            facesContext.addMessage(null, new FacesMessage(
                    "Authentication Failure - The username or password you provided were invalid."));
        }
    }



}
