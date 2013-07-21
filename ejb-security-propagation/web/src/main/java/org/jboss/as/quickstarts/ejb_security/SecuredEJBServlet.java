/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and/or its affiliates, and individual
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
package org.jboss.as.quickstarts.ejb_security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.annotation.security.RolesAllowed;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.as.quickstarts.ejb_security_interceptors.ClientSecurityInterceptor;
import org.jboss.ejb.client.EJBClientContext;
import org.jboss.ejb.client.EJBClientContextListener;


/**
 * A simple secured Servlet which calls a secured EJB. Upon successful authentication and authorization the
 * EJB will return the principal's name. Servlet security is implemented using annotations.
 * 
 * @author <a href="mailto:claudio@redhat.com">Claudio Miranda</a>
 * 
 */

@WebServlet("/secure_ejb")
@ServletSecurity(@HttpConstraint(rolesAllowed = {"admin"}))
@RolesAllowed("admin")
//@SecurityDomain("security-propagation-quickstart")
public class SecuredEJBServlet extends HttpServlet {

    private static String PAGE_HEADER = "<html><head><title>ejb-security-propagation</title></head><body>";

    private static String PAGE_FOOTER = "</body></html>";

    private Secured securedEJB;

    Object lookup(String s) {
        Object o = null;
        try {
            Properties props = new Properties();
            props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            props.put("org.jboss.ejb.client.scoped.context", "true");
            props.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
            props.put("remote.connections", "default");
            props.put("remote.connection.default.host", "localhost");
            props.put("remote.connection.default.port", "4597");
            props.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", "false");
            props.put("remote.connection.default.username", "ejbcaller");
            props.put("remote.connection.default.password", "@ejbqs123");
            Context ctx = new InitialContext(props);
            o = ctx.lookup(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o;
    }
   
   /**
    * Servlet entry point method which calls securedEJB.getSecurityInfo()
    */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        String principal = null;
        String authType = null;
        String remoteUser = null;

        Object o = lookup("ejb:/jboss-as-propagation-ejb/SecuredEJB!org.jboss.as.quickstarts.ejb_security.Secured");
        System.out.println("ejb: " + o);
        securedEJB = (Secured) o;

        // Get security principal
        principal = securedEJB.getSecurityInfo();
        // Get user name from login principal
        remoteUser = req.getRemoteUser();
        // Get authentication type
        authType = req.getAuthType();

        writer.println(PAGE_HEADER);
        writer.println("<h1>" + "Successfully called Secured EJB " + "</h1>");
        writer.println("<p>" + "Principal  : " + principal + "</p>");
        writer.println("<p>" + "Remote User : " + remoteUser + "</p>");
        writer.println("<p>" + "Authentication Type : " + authType + "</p>");
        writer.println(PAGE_FOOTER);
        writer.close();
    }

}


/*@WebListener
class StartupWebListener implements ServletContextListener {
 
 
          @Override
          public void contextInitialized(ServletContextEvent sce) {
                    // on startup
                    EJBClientContext.getCurrent().registerInterceptor(0, new ClientSecurityInterceptor());
                    EJBClientContext.getCurrent().registerEJBClientContextListener(new EJBClientContextListener() {
                        
                        @Override
                        public void contextClosed(EJBClientContext ejbClientContext) {
                                System.out.println("=-=-=-=-=-=-=-=   EJB client context closed  =-=-=-=-=---=-=-=-=-=-=        ");                            
                        }
                    });
          }
 
 
          @Override
          public void contextDestroyed(ServletContextEvent sce) {
                    // on shutdown
          }
}*/