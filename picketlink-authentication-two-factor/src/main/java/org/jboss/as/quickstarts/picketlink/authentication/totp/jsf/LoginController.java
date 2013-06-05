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
package org.jboss.as.quickstarts.picketlink.authentication.totp.jsf;

import java.io.Serializable;
import java.util.UUID;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.picketlink.Identity;
import org.picketlink.common.util.Base32;
import org.picketlink.credential.DefaultLoginCredentials;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.credential.TOTPCredential;
import org.picketlink.idm.credential.TOTPCredentials;
import org.picketlink.idm.model.Attribute;
import org.picketlink.idm.model.User;

/**
 * We control the authentication process from this bean, so that in the event of a failed authentication we can add an
 * appropriate FacesMessage to the response.
 *
 * @author Pedro Igor
 */
@Named
@RequestScoped
public class LoginController {

    @Inject
    private Identity identity;

    @Inject
    private DefaultLoginCredentials loginCredentials;

    @Inject
    private IdentityManager identityManager;

    @Inject
    private FacesContext facesContext;

    private String token;

    private String totpSecret;

    public String login() {
        String userName = this.loginCredentials.getUserId();

        if (userName == null || userName.isEmpty()) {
            this.facesContext.addMessage(null, new FacesMessage("Please, provide your username."));
            return null;
        }

        User user = this.identityManager.getUser(userName);
        String result = null;

        if (user != null) {
            Attribute<Serializable> totpSecretAttribute = user.getAttribute("TOTP_SECRET");

            // if the user does not have a secret, try to login and generate a secret.
            if (totpSecretAttribute == null) {
                this.identity.login();

                if (this.identity.isLoggedIn()) {
                    generateUserSecret(user);
                    result = "/secretSetup.xhtml";
                }
            } else {
                // otherwise, we try to login using the provided token
                loginUsingToken();
            }
        }

        if (!this.identity.isLoggedIn()) {
            this.facesContext.addMessage(null, new FacesMessage(
                    "Authentication was unsuccessful.  Please check your username and password " + "before trying again."));
        }

        return result;
    }

    private void generateUserSecret(User user) {
        String totpSecret = UUID.randomUUID().toString();

        // we're only doing this to show you how you can retrieve the secret for your users.
        user.setAttribute(new Attribute<String>("TOTP_SECRET", totpSecret));

        this.identityManager.update(user);

        // we need Base32.encode to support Google Authenticator
        setTotpSecret(Base32.encode(totpSecret.getBytes()));

        TOTPCredential credential = new TOTPCredential(this.loginCredentials.getPassword(), totpSecret);

        this.identityManager.updateCredential(user, credential);
    }

    private void loginUsingToken() {
        if (this.token == null || this.token.isEmpty()) {
            this.facesContext.addMessage(null, new FacesMessage(
                    "You have configured two-factor authentication. Please, provide your token."));
        } else {
            TOTPCredentials credential = new TOTPCredentials();

            credential.setUsername(this.loginCredentials.getUserId());
            credential.setPassword((Password) this.loginCredentials.getCredential());
            credential.setToken(this.token);

            // we override the credential to set a totp credential
            this.loginCredentials.setCredential(credential);

            identity.login();
        }
    }

    public String logout() {
        this.identity.logout();
        return "/home.xhtml";
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTotpSecret() {
        return this.totpSecret;
    }

    public void setTotpSecret(String totpSecret) {
        this.totpSecret = totpSecret;
    }
}