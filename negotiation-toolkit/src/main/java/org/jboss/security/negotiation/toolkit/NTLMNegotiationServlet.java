/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat, Inc. and/or its affiliates, and individual
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.logging.Logger;
import org.jboss.security.negotiation.MessageFactory;
import org.jboss.security.negotiation.NegotiationException;
import org.jboss.security.negotiation.NegotiationMessage;
import org.jboss.security.negotiation.ntlm.encoding.NTLMField;
import org.jboss.security.negotiation.ntlm.encoding.NegotiateMessage;
import org.picketbox.commons.cipher.Base64;

/**
 * A basic servlet to specifically test the NTLM negotiation.
 * 
 * @author darran.lofthouse@jboss.com
 * @version $Revision: 111912 $
 */
public class NTLMNegotiationServlet extends HttpServlet
{

   private static final long serialVersionUID = -3291448937864587130L;

   private static final Logger log = Logger.getLogger(NTLMNegotiationServlet.class.getName());

   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
   {
      String authHeader = req.getHeader("Authorization");
      NegotiationMessage message = (NegotiationMessage) req.getAttribute("message");

      if (message == null && authHeader == null)
      {
         log.info("No Authorization Header, sending 401");
         resp.setHeader("WWW-Authenticate", "NTLM");
         resp.sendError(401);

         return;
      }

      log.info("Authorization header received - decoding token.");

      Object response = null;
      if (message == null)
      {
         String requestHeader = "";
         if (authHeader.startsWith("Negotiate "))
         {
            // Drop the 'Negotiate ' from the header.
            requestHeader = authHeader.substring(10);
         }
         else if (authHeader.startsWith("NTLM "))
         {
            // Drop the 'NTLM ' from the header.
            requestHeader = authHeader.substring(5);
         }

         if (requestHeader.length() > 0)
         {
            byte[] reqToken = Base64.decode(requestHeader);
            ByteArrayInputStream bais = new ByteArrayInputStream(reqToken);
            MessageFactory mf = null;

            try
            {
               mf = MessageFactory.newInstance();
            }
            catch (NegotiationException e)
            {
               throw new ServletException("Unable to create MessageFactory", e);
            }

            if (mf.accepts(bais))
            {
               message = mf.createMessage(bais);
               if (message instanceof NegotiateMessage)
               {
                  response = message;
               }
               else
               {
                  response = "<p><b>Unsuported negotiation token.</b></p>";
               }

            }
            else
            {
               response = "<p><b>Unsuported negotiation token.</b></p>";
            }

         }

      }
      else
      {
         log.info("Using existing message.");
         response = message;
      }

      /* At this stage no further negotiation will take place so the information */
      /* can be output in the servlet response.                                  */

      PrintWriter writer = resp.getWriter();

      writer.println("<html>");
      writer.println("  <head>");
      writer.println("    <title>Negotiation Toolkit</title>");
      writer.println("  </head>");
      writer.println("  <body>");
      writer.println("    <h1>Negotiation Toolkit</h1>");
      writer.println("    <h2>NTLM Negotiation</h2>");

      // Output the raw header.
      writer.println("    <p>WWW-Authenticate - ");
      writer.println(authHeader);
      writer.println("    </p>");

      try
      {
         writeHeaderDetail(response, writer);
      }
      catch (Exception e)
      {
         if (e instanceof RuntimeException)
         {
            throw (RuntimeException) e;
         }
         else
         {
            throw new ServletException("Unable to writeHeaderDetail", e);
         }
      }

      writer.println("  </body>");
      writer.println("</html>");
      writer.flush();
   }

   @Override
   protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
         IOException
   {
      // Handle POST as GET.
      doGet(req, resp);
   }

   private void writeHeaderDetail(final Object response, final PrintWriter writer) throws IOException
   {

      if (response instanceof String)
      {
         writer.println((String) response);
      }
      else if (response instanceof NegotiateMessage)
      {
         NegotiateMessage message = (NegotiateMessage) response;
         writer.println("<h3>NTLM - Negotiate_Message</h3>");

         writer.write("<h4><font color='red'>"
               + "Warning, this is NTLM, only SPNEGO is supported!</font></h4>");

         writer.write("<b>Negotiate Flags</b> - ");
         writer.write(String.valueOf(message.getNegotiateFlags()));
         writer.write("<br>");

         writeNTLMField("Domain Name", message.getDomainName(), message.getDomainNameFields(), writer);
         writeNTLMField("Workstation Name", message.getWorkstationName(), message.getWorkstationFields(), writer);

         if (message.getVersion() != null && message.getVersion().length > 0)
         {
            writer.write("<b>Version </b> - ");
            writer.write(new String(message.getVersion()));
            writer.write("<br>");
         }
      }

   }

   private void writeNTLMField(final String name, final String value, final NTLMField field, final PrintWriter writer)
   {
      writer.write("<b>");
      writer.write(name);
      writer.write("</b> = ");
      writer.write(String.valueOf(value));
      writer.write(" - <i>");
      writer.write(String.valueOf(field));
      writer.write("</i><br>");
   }

}
