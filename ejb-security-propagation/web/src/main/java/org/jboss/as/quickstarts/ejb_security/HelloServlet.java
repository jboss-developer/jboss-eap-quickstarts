/** 
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
package org.jboss.as.quickstarts.ejb_security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A simple secured Servlet which calls a secured EJB. Upon successful
 * authentication and authorization the EJB will return the principal's name.
 * Servlet security is implemented using annotations.
 * 
 * @author <a href="mailto:claudio@redhat.com">Claudio Miranda</a>
 * 
 */
@SuppressWarnings("serial")
@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    private static String PAGE_HEADER = "<html><head><title>hello</title></head><body>";
    private static String PAGE_FOOTER = "</body></html>";
    // Inject the Secured EJB
    private Hello helloEJB;

    Object lookup(String s) {
        Object o = null;
        try {
            Properties props = new Properties();
            props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            Context ctx = new InitialContext(props);
            o = ctx.lookup(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o;
    }

    /**
     * Servlet entry point method which calls helloEJB.getSecurityInfo()
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        String principal = null;
        String authType = null;
        String remoteUser = null;

        Object o = lookup("ejb:/jboss-as-propagation-ejb/HelloEJB!org.jboss.as.quickstarts.ejb_security.Hello");
        System.out.println("ejb: " + o);
        helloEJB = (Hello) o;

        // Get security principal
        principal = helloEJB.getSecurityInfo();
        // Get user name from login principal
        remoteUser = req.getRemoteUser();
        // Get authentication type
        authType = req.getAuthType();

        writer.println(PAGE_HEADER);
        writer.println("<h1>" + "Successfully called Hello EJB " + "</h1>");
        writer.println("<p>" + "Hello Principal  : " + principal + "</p>");
        writer.println("<p>" + "Hello Remote User : " + remoteUser + "</p>");
        writer.println("<p>" + "Hello Authentication Type : " + authType
                + "</p>");
        writer.println(PAGE_FOOTER);
        writer.close();
    }
}
