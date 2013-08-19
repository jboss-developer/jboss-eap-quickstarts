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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;
import org.ietf.jgss.Oid;
import org.jboss.security.negotiation.MessageFactory;
import org.jboss.security.negotiation.NegotiationException;
import org.jboss.security.negotiation.NegotiationMessage;
import org.jboss.security.negotiation.OidNameUtil;
import org.jboss.security.negotiation.common.DebugHelper;
import org.jboss.security.negotiation.ntlm.encoding.NegotiateMessage;
import org.jboss.security.negotiation.spnego.KerberosMessage;
import org.jboss.security.negotiation.spnego.encoding.NegTokenInit;
import org.jboss.security.negotiation.spnego.encoding.NegTokenTarg;
import org.picketbox.commons.cipher.Base64;

/**
 * A basic servlet to test that if prompted the client browser will return a SPNEGO
 * header rather than an NTLM header.
 * 
 * Clients that return an NTLM header do not trust the server sufficiently so the KDC
 * configuration will need to be checked.
 * 
 * NTLM responses received will be forwarded to the NTLMNegotiationServlet for display.
 * 
 * @author darran.lofthouse@jboss.com
 * @version $Revision: 113292 $
 */
public class BasicNegotiationServlet extends HttpServlet
{

   private static final long serialVersionUID = 7269693410644316525L;

   private static final Logger log = Logger.getLogger(BasicNegotiationServlet.class);

   @Override
   protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
         IOException
   {
      String authHeader = req.getHeader("Authorization");
      if (authHeader == null)
      {
         log.info("No Authorization Header, sending 401");
         resp.setHeader("WWW-Authenticate", "Negotiate");
         resp.sendError(401);

         return;
      }

      log.info("Authorization header received - decoding token.");

      Object response = null;

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
            NegotiationMessage message = mf.createMessage(bais);

            if (message instanceof NegotiateMessage)
            {
               req.setAttribute("message", message);

               RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/NTLMNegotiation");
               dispatcher.forward(req, resp);

               return;
            }

            if (message instanceof NegTokenInit || message instanceof KerberosMessage)
            {
               response = message;
            }
            else if (message instanceof NegTokenTarg)
            {
               response = "<p><b>Unexpected NegTokenTarg, first token should be NegTokenInit!</b></p>";
            }

         }
         else
         {
            response = "<p><b>Unsuported negotiation token.</b></p>";
         }

      }
      else
      {
         response = "<p><b>Header WWW-Authenticate does not beging with 'Negotiate' or 'NTLM'!</b></p>";
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
      writer.println("    <h2>Basic Negotiation</h2>");

      // Output the raw header.
      writer.println("    <p>WWW-Authenticate - ");
      writer.println(authHeader);
      writer.println("    </p>");

      writeHeaderDetail(response, writer);

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
      else if (response instanceof NegTokenInit)
      {
         NegTokenInit negTokenInit = (NegTokenInit) response;
         writer.println("<h3>NegTokenInit</h3>");

         writer.print("<b>Message Oid - </b>");
         writer.print(OidNameUtil.getName(negTokenInit.getMessageOid()));
         writer.println("<br>");

         List<Oid> mechTypes = negTokenInit.getMechTypes();
         writer.print("<b>Mech Types -</b>");
         for (Oid current : mechTypes)
         {
            writer.print(" {");
            writer.print(OidNameUtil.getName(current));
            writer.print("}");
         }
         writer.println("<br>");

         writer.print("<b>Req Flags -</b>");
         byte[] reqFlags = negTokenInit.getReqFlags();
         if (reqFlags != null && reqFlags.length > 0)
         {
            writer.print(DebugHelper.convertToHex(reqFlags));
         }
         writer.println("<br>");

         writer.print("<b>Mech Token -</b>");
         byte[] mechToken = negTokenInit.getMechToken();
         if (mechToken != null && mechToken.length > 0)
         {
            writer.print(new String(Base64.encodeBytes(mechToken)));
         }
         writer.println("<br>");

         writer.print("<b>Mech List Mic -</b>");
         byte[] mechTokenMic = negTokenInit.getMechListMIC();
         if (mechTokenMic != null && mechTokenMic.length > 0)
         {
            writer.print(new String(Base64.encodeBytes(mechTokenMic)));
         }
         writer.println("<br>");
      }
      else if (response instanceof KerberosMessage)
      {
         writer.println("<h3>KerberosV5</h3>");
         writer.print("<b>Message Oid - </b>");
         writer.print(OidNameUtil.getName(((KerberosMessage) response).getMessageOid()));
         writer.println("<br>");
      }
   }

}
