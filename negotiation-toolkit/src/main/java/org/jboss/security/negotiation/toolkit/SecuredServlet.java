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
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ietf.jgss.GSSCredential;
import org.jboss.security.SecurityContextAssociation;
import org.jboss.security.SubjectInfo;
import org.jboss.security.identity.Identity;
import org.jboss.security.identity.Role;
import org.jboss.security.negotiation.DelegationCredentialContext;

//import org.jboss.security.SecurityAssociation;

/**
 * A simple servlet to be secured and output information on the
 * authenticated user. 
 * 
 * @author darran.lofthouse@jboss.com
 * @version $Revision: 113294 $
 */
public class SecuredServlet extends HttpServlet
{

   private static final long serialVersionUID = 4708999345009728352L;

   @Override
   protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
           IOException {
       PrintWriter writer = resp.getWriter();

       writer.println("<html>");
       writer.println("  <head>");
       writer.println("    <title>Negotiation Toolkit</title>");
       writer.println("  </head>");
       writer.println("  <body>");
       writer.println("    <h1>Negotiation Toolkit</h1>");
       writer.println("    <h2>Secured</h2>");

       writer.println("    <h5>Auth Type</h5>");
       writeObject(req.getAuthType(), writer);

       writer.println("    <h5>User Principal</h5>");
       writeObject(req.getUserPrincipal(), writer);

       SubjectInfo info = SecurityContextAssociation.getSecurityContext().getSubjectInfo();
       Set<Identity> identities = info.getIdentities();
       writer.println("    <h5>Identities</h5>");
       for (Identity current : identities) {
           writer.println(" " + current.getName() + "<br>");
       }
       
       writeDelegationCredential(writer);            

       writer.println("    <h5>Subject</h5>");
       writeObject(info.getAuthenticatedSubject(), writer);

       List<Role> roles = info.getRoles().getRoles();
       writer.println("    <h5>Roles</h5>");
       for (Role current : roles) {
           writer.println(" " + current.getRoleName() + "<br>");
       }

       writer.println("  </body>");
       writer.println("</html>");
       writer.flush();
   }
   
   private void writeDelegationCredential(final PrintWriter writer) throws IOException {
       try {
          GSSCredential credential = DelegationCredentialContext.getDelegCredential();       
          writer.println("    <h5>Delegation Credential</h5>");       
          if (credential == null) {
             writer.println("    <p>None</p>");
          }
       else {
          writeObject(credential, writer);
       } 
       } catch (NoClassDefFoundError ignored) {
           // Just ignore this and skip the output.           
           // This will happen in AS versions that don't support this API.
       }
   }

   private void writeObject(final Object obj, final PrintWriter writer) throws IOException
   {
      ByteArrayInputStream bais = new ByteArrayInputStream(String.valueOf(obj).getBytes());
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
   }

   @Override
   protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
         IOException
   {
      // Handle POST the same as GET.
      doGet(req, resp);
   }

}
