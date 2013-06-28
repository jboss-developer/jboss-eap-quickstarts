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
package org.jboss.as.quickstarts.picketlink.authentication.recaptcha.jsf;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.picketlink.Identity;
import org.picketlink.credential.DefaultLoginCredentials;

/**
 * We control the authentication process from this bean, so that in the event of a failed authentication we can add an
 * appropriate FacesMessage to the response.
 */
@Named
@RequestScoped
public class LoginController {

    @Inject
    private Identity identity;

    @Inject
    private DefaultLoginCredentials loginCredentials;

    @Inject
    private ReCaptchaService reCaptchaService;

    @Inject
    private FacesContext facesContext;

    public String login() throws Exception {
        if (checkCaptcha()) {
            this.identity.login();

            if (!this.identity.isLoggedIn()) {
                this.facesContext.addMessage(null, new FacesMessage(
                        "Authentication failed. Please, try again."));
            }
        } else {
            this.facesContext.addMessage(null, new FacesMessage(
                    "Verification failed. Please, type the text again."));
        }

        return null;
    }

    private boolean checkCaptcha() {
        return this.reCaptchaService.verify(getReCaptchaChallenge(), getReCaptchaResponse());
    }

    private String getReCaptchaResponse() {
        return this.facesContext.getExternalContext().getRequestParameterMap().get("recaptcha_response_field");
    }

    private String getReCaptchaChallenge() {
        return this.facesContext.getExternalContext().getRequestParameterMap().get("recaptcha_challenge_field");
    }

    public String logout() {
        this.identity.logout();
        return "/home.xhtml";
    }

}