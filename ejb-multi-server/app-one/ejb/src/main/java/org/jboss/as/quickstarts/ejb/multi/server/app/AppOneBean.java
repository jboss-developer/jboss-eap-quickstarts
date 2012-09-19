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

import org.jboss.logging.Logger;

/**
 * <p>
 * Simple bean with methods to get the node name of the server and log messages. One method is annotated with a security role.
 * The security-domain is declared within the deployment descriptor jboss-ejb3.xml instead of using the annotation.
 * </p>
 * <p>
 * If the security-domain is removed the secured method can be invoked from every user. The shown principal user is 'anonymous'
 * instead of the original logged in user (AS 7.1, 7.2 and EAP6.0)
 * </p>
 * 
 * <p>
 * The EJB is marked as clustered by using the xml deployment descriptor, see <code>jboss-ejb3.xml</code>
 * </p>
 * 
 * @author <a href="mailto:wfink@redhat.com">Wolf-Dieter Fink</a>
 */
@Stateless
public class AppOneBean implements AppOne {
  private static final Logger LOGGER = Logger.getLogger(AppOneBean.class);

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
    return "app1[" + caller.getName() + "]@" + getJBossNodeName();
  }

  @Override
  @RolesAllowed({ "AppOne", "Intern" })
  public String invokeSecured(String text) {
    Principal caller = context.getCallerPrincipal();
    LOGGER.info("Secured invocation [" + caller.getName() + "] " + text);
    LOGGER.info("Is in Role AppOne=" + context.isCallerInRole("AppOne") + " Intern=" + context.isCallerInRole("Intern"));
    return "app1[" + caller.getName() + "]@" + getJBossNodeName();
  }
}
