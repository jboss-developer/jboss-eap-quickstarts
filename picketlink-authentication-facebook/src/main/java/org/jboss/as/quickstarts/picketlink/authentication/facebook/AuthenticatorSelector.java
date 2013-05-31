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
package org.jboss.as.quickstarts.picketlink.authentication.facebook;


import org.picketlink.annotations.PicketLink;
import org.picketlink.authentication.Authenticator;
import org.picketlink.social.auth.FacebookAuthenticator;
import org.picketlink.social.auth.conf.FacebookConfiguration;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Bean that allows selection of {@link Authenticator}
 * @author Anil Saldhana
 * @since May 30, 2013
 */
@Named
public class AuthenticatorSelector {

    @Inject
    private FacebookConfiguration facebookConfiguration;

    @PicketLink
    @Produces
    @RequestScoped
    public Authenticator chooseAuthenticator() {
        HttpServletRequest httpServletRequest = (HttpServletRequest) ThreadLocalUtils.currentRequest.get();
        HttpServletResponse httpServletResponse = (HttpServletResponse) ThreadLocalUtils.currentResponse.get();

        String login = httpServletRequest.getParameter("login");
        Authenticator authenticator = null;

        if(login == null || login.equals("facebook")){
            FacebookAuthenticator facebookAuthenticator = new FacebookAuthenticator();
            facebookAuthenticator.setHttpServletRequest(httpServletRequest);
            facebookAuthenticator.setHttpServletResponse(httpServletResponse);
            facebookAuthenticator.setConfiguration(facebookConfiguration);
            authenticator = facebookAuthenticator;
        }
        return authenticator;
    }
}