/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.as.quickstarts.ejb.multi.server.app;

import java.security.Principal;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import org.jboss.as.quickstarts.ejb.multi.server.app.AppTwo;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.logging.Logger;

/**
 * Simple bean with methods to get the node name of the server and log messages. One method is annotated with a security role.
 * The security-domain is declared by the JBoss specific annotation SecurityDomain.
 * 
 * @author <a href="mailto:wfink@redhat.com">Wolf-Dieter Fink</a>
 */
@SecurityDomain(value = "other")
public @Stateless class AppTwoBean implements AppTwo {
  private static final Logger LOGGER = Logger.getLogger(AppTwoBean.class);

  @Resource
  SessionContext context;

  @Override
  public String getJBossNodeName() {
    return System.getProperty("jboss.node.name");
  }

  @Override
  public String invoke(String text) {
    Principal caller = context.getCallerPrincipal();
    LOGGER.info("[" + caller.getName() + "] " + text);
    return "app2[" + caller.getName() + "]@" + getJBossNodeName();
  }

  @Override
  @RolesAllowed({ "AppTwo", "Intern" })
  public String invokeSecured(String text) {
    Principal caller = context.getCallerPrincipal();
    LOGGER.info("Secured invocation [" + caller.getName() + "] " + text);
    LOGGER.info("Is in Role AppTwo=" + context.isCallerInRole("AppTwo") + " Intern=" + context.isCallerInRole("Intern"));
    return "app2[" + caller.getName() + "]@" + getJBossNodeName();
  }

}
