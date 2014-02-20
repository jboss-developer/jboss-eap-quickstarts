/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
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
package org.jboss.web.tomcat.security;

import java.io.IOException;
import java.security.Principal;
import java.util.StringTokenizer;

import javax.management.JMException;
import javax.management.ObjectName;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Realm;
import org.apache.catalina.Session;
import org.apache.catalina.authenticator.Constants;
import org.apache.catalina.connector.Request;
import org.apache.catalina.deploy.LoginConfig;
import org.jboss.logging.Logger;

import org.jboss.as.web.security.ExtendedFormAuthenticator;
import org.jboss.security.plugins.HostThreadLocal;

/**
 * JBAS-2283: Provide custom header based authentication support
 * 
 * Header Authenticator that deals with userid from the request header Requires
 * two attributes configured on the Tomcat Service - one for the http header
 * denoting the authenticated identity and the other is the SESSION cookie
 * 
 * @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 * @author <a href="mailto:sguilhen@redhat.com">Stefan Guilhen</a>
 * @version $Revision$
 * @since Sep 11, 2006
 */
public class GenericHeaderAuthenticator extends ExtendedFormAuthenticator {
    protected static Logger log = Logger.getLogger(GenericHeaderAuthenticator.class);

    // JBAS-4804: GenericHeaderAuthenticator injection of ssoid and
    // sessioncookie name.
    private String httpHeaderForSSOAuth = null;

    private String sessionCookieForSSOAuth = null;

    /**
     * <p>
     * Obtain the value of the <code>httpHeaderForSSOAuth</code> attribute. This
     * attribute is used to indicate the request header ids that have to be
     * checked in order to retrieve the SSO identity set by a third party
     * security system.
     * </p>
     * 
     * @return a <code>String</code> containing the value of the
     *         <code>httpHeaderForSSOAuth</code> attribute.
     */
    public String getHttpHeaderForSSOAuth() {
        return httpHeaderForSSOAuth;
    }

    /**
     * <p>
     * Set the value of the <code>httpHeaderForSSOAuth</code> attribute. This
     * attribute is used to indicate the request header ids that have to be
     * checked in order to retrieve the SSO identity set by a third party
     * security system.
     * </p>
     * 
     * @param httpHeaderForSSOAuth
     *            a <code>String</code> containing the value of the
     *            <code>httpHeaderForSSOAuth</code> attribute.
     */
    public void setHttpHeaderForSSOAuth(String httpHeaderForSSOAuth) {
        this.httpHeaderForSSOAuth = httpHeaderForSSOAuth;
    }

    /**
     * <p>
     * Obtain the value of the <code>sessionCookieForSSOAuth</code> attribute.
     * This attribute is used to indicate the names of the SSO cookies that may
     * be present in the request object.
     * </p>
     * 
     * @return a <code>String</code> containing the names (separated by a
     *         <code>','</code>) of the SSO cookies that may have been set by a
     *         third party security system in the request.
     */
    public String getSessionCookieForSSOAuth() {
        return sessionCookieForSSOAuth;
    }

    /**
     * <p>
     * Set the value of the <code>sessionCookieForSSOAuth</code> attribute. This
     * attribute is used to indicate the names of the SSO cookies that may be
     * present in the request object.
     * </p>
     * 
     * @param sessionCookieForSSOAuth
     *            a <code>String</code> containing the names (separated by a
     *            <code>','</code>) of the SSO cookies that may have been set by
     *            a third party security system in the request.
     */
    public void setSessionCookieForSSOAuth(String sessionCookieForSSOAuth) {
        this.sessionCookieForSSOAuth = sessionCookieForSSOAuth;
    }

    /**
     * <p>
     * Creates an instance of <code>GenericHeaderAuthenticator</code>.
     * </p>
     */
    public GenericHeaderAuthenticator() {
        super();
    }

    public boolean authenticate(Request request, HttpServletResponse response,
            LoginConfig config) throws IOException {
        // set remote host value
        HostThreadLocal.set(request.getRemoteAddr());

        log.trace("Authenticating user");

        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            log.trace("Already authenticated '" + principal.getName() + "'");
            return true;
        }

        Realm realm = context.getRealm();
        Session session = request.getSessionInternal(true);

        String username = getUserId(request);
        String password = getSessionCookie(request);

        // Check if there is sso id as well as sessionkey
        if (username == null || password == null) {
            log.trace("Username is null or password(sessionkey) is null:fallback to form auth");
            return super.authenticate(request, response, config);
        }
        principal = realm.authenticate(username, password);

        if (principal == null) {
            forwardToErrorPage(request, response, config);
            return false;
        }

        session.setNote(Constants.SESS_USERNAME_NOTE, username);
        session.setNote(Constants.SESS_PASSWORD_NOTE, password);
        request.setUserPrincipal(principal);

        register(request, response, principal, HttpServletRequest.FORM_AUTH, username, password);
        return true;
    }

    /**
     * Get the username from the request header
     * 
     * @param request
     * @return
     */
    protected String getUserId(Request request) {
        String ssoid = null;
        // We can have a comma-separated ids
        String ids = "";
        try {
            ids = this.getIdentityHeaderId();
        } catch (JMException e) {
            log.trace("getUserId exception", e);
        }
        if (ids == null || ids.length() == 0)
            throw new IllegalStateException(
                    "Http headers configuration in tomcat service missing");

        StringTokenizer st = new StringTokenizer(ids, ",");
        while (st.hasMoreTokens()) {
            ssoid = request.getHeader(st.nextToken());
            if (ssoid != null)
                break;
        }
        log.trace("SSOID-" + ssoid);
        return ssoid;
    }

    /**
     * Obtain the session cookie from the request
     * 
     * @param request
     * @return
     */
    protected String getSessionCookie(Request request) {
        Cookie[] cookies = request.getCookies();
        log.trace("Cookies:" + cookies);
        int numCookies = cookies != null ? cookies.length : 0;

        // We can have comma-separated ids
        String ids = "";
        try {
            ids = this.getSessionCookieId();
            log.trace("Session Cookie Ids=" + ids);
        } catch (JMException e) {
            log.trace("checkSessionCookie exception", e);
        }
        if (ids == null || ids.length() == 0)
            throw new IllegalStateException(
                    "Session cookies configuration in tomcat service missing");

        StringTokenizer st = new StringTokenizer(ids, ",");
        while (st.hasMoreTokens()) {
            String cookieToken = st.nextToken();
            String val = getCookieValue(cookies, numCookies, cookieToken);
            if (val != null)
                return val;
        }
        log.trace("Session Cookie not found");
        return null;
    }

    /**
     * Get the configured header identity id in the tomcat service
     * 
     * @return
     * @throws JMException
     */
    protected String getIdentityHeaderId() throws JMException {
        if (this.httpHeaderForSSOAuth != null)
            return this.httpHeaderForSSOAuth;
        return (String) mserver.getAttribute(new ObjectName(
                "jboss.web:service=WebServer"), "HttpHeaderForSSOAuth");
    }

    /**
     * Get the configured session cookie id in the tomcat service
     * 
     * @return
     * @throws JMException
     */
    protected String getSessionCookieId() throws JMException {
        if (this.sessionCookieForSSOAuth != null)
            return this.sessionCookieForSSOAuth;
        return (String) mserver.getAttribute(new ObjectName(
                "jboss.web:service=WebServer"), "SessionCookieForSSOAuth");
    }

    /**
     * Get the value of a cookie if the name matches the token
     * 
     * @param cookies
     *            array of cookies
     * @param numCookies
     *            number of cookies in the array
     * @param token
     *            Key
     * @return value of cookie
     */
    protected String getCookieValue(Cookie[] cookies, int numCookies,
            String token) {
        for (int i = 0; i < numCookies; i++) {
            Cookie cookie = cookies[i];
            log.trace("Matching cookieToken:" + token + " with cookie name="
                    + cookie.getName());
            if (token.equals(cookie.getName())) {
                log.trace("Cookie-" + token + " value=" + cookie.getValue());
                return cookie.getValue();
            }
        }
        return null;
    }
}
