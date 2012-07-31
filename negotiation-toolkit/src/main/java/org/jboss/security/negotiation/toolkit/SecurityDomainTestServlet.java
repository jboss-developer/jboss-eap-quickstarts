/*
 * JBoss, Home of Professional Open Source
 * Copyright 2007, Red Hat, Inc. and/or its affiliates, and individual
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

package org.jboss.security.negotiation.toolkit;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.logging.Logger;

/**
 * A servlet to test that the security domain required by the authenticator
 * can successfully authenticate.
 * 
 * @author darran.lofthouse@jboss.com
 * @version $Revision: 111538 $
 */
public class SecurityDomainTestServlet extends HttpServlet
{

   private static final long serialVersionUID = -3129778766905747055L;

   private static final Logger log = Logger.getLogger(SecurityDomainTestServlet.class.getName());

   @Override
   protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
         IOException
   {
      String securityDomain = req.getParameter("securityDomain");

      PrintWriter writer = resp.getWriter();

      writer.println("<html>");
      writer.println("  <head>");
      writer.println("    <title>Negotiation Toolkit</title>");
      writer.println("  </head>");
      writer.println("  <body>");
      writer.println("    <h1>Negotiation Toolkit</h1>");
      writer.println("    <h2>Security Domain Test</h2>");

      if (securityDomain == null)
      {
         displayForm(writer);
      }
      else
      {
         testDomain(securityDomain, writer);
      }

      writer.println("  </body>");
      writer.println("</html>");
      writer.flush();
   }

   private void displayForm(final PrintWriter writer)
   {
      writer
            .println("    <p>Please enter the name of the security-domain used for the server to authenticate itself.</p>");
      writer.println("    <p>");
      writer.println("      <form method='get'>");
      writer.println("        Security Domain <input type='text' name='securityDomain' value='host'><br>");
      writer.println("        <br><input type='submit' value='Test'>");
      writer.println("      </form>");
      writer.println("    </p>");
   }

   private void testDomain(final String securityDomain, final PrintWriter writer)
   {
      writer.print("<p>Testing security-domain '");
      writer.print(securityDomain);
      writer.println("'</p>");

      try
      {
         LoginContext context = new LoginContext(securityDomain);
         log.fine("Obtained LoginContext for '" + securityDomain + "' security-domain.");

         context.login();
         writer.println("<h4>Authenticated</h4>");         

         Subject subject = context.getSubject();

         ByteArrayInputStream bais = new ByteArrayInputStream(String.valueOf(subject).getBytes());
         InputStreamReader isr = new InputStreamReader(bais);
         BufferedReader br = new BufferedReader(isr);

         writer.println("<code>");
         String currentLine;
         while ((currentLine = br.readLine()) != null)
         {
            writer.print(currentLine);
            writer.println("<br>");
         }
         writer.println("</code>");
         
         context.logout();
         log.fine("logged out.");
      }
      catch (Exception e)
      {
         // TODO - Output full exception detail.
         writer.println("<h5>Failed!</h5>");
         writer.print("<p>");
         writer.print(e.getClass().getName());
         writer.print(" - ");
         writer.print(e.getMessage());
         writer.println("</p>");

         log.severe("testDomain Failed " + e.toString());
      }
   }

   @Override
   protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
         IOException
   {
      // Handle POST the same as GET.
      doGet(req, resp);
   }
}
