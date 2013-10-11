package org.picketlink.identity.federation.app.sts;
/*
 * JBoss, Home of Professional Open Source. Copyright 2009, Red Hat Middleware LLC, and individual contributors as
 * indicated by the @author tags. See the copyright.txt file in the distribution for a full listing of individual
 * contributors.
 * 
 * This is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any
 * later version.
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this software; if not, write to
 * the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF site:
 * http://www.fsf.org.
 */


import javax.annotation.Resource;
import javax.xml.ws.Service;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceProvider;

import org.apache.log4j.Logger;
import org.picketlink.identity.federation.core.wstrust.PicketLinkSTS;

/**
 * <p>
 * Default implementation of the {@code SecurityTokenService} interface.
 * </p>
 * 
 * @author <a href="mailto:sguilhen@redhat.com">Stefan Guilhen</a>
 * @author <a href="mailto:pskopek@redhat.com">Peter Skopek</a>
 */
@WebServiceProvider(serviceName = "PicketLinkSTS", portName = "PicketLinkSTSPort", targetNamespace = "urn:picketlink:identity-federation:sts", wsdlLocation = "WEB-INF/wsdl/PicketLinkSTS.wsdl")
@ServiceMode(value = Service.Mode.MESSAGE)
public class PicketLinkSTService extends PicketLinkSTS 
{
   private static Logger log = Logger.getLogger(PicketLinkSTService.class);
	
   @Resource
   public void setWSC(WebServiceContext wctx) {
	   log.debug("Setting WebServiceContext = " + wctx);
	   this.context = wctx;
   }

}