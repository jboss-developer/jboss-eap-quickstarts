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

import javax.ejb.Remote;

/**
 * Interface for the demo application One.
 * 
 * @author <a href="mailto:wfink@redhat.com">Wolf-Dieter Fink</a>
 */
@Remote
public interface AppTwo {

  /**
   * Unsecured invocation, will return the name of application, principal and JBoss node.
   * 
   * @param text Simple text written to to the logfile to identify the invocation
   * @return app1[&lt;PrincipalName&gt;]@&lt;jboss.node.name&gt;
   */
  String invoke(String text);

  /**
   * @return The property of jboss.node.name, pattern &lt;host&gt;:&lt;server&gt;
   */
  String getJBossNodeName();

  /**
   * Secured invocation for Roles ( AppTwo, Intern ). See {@link #invoke(String)}
   * 
   * @param text Simple text written to to the logfile to identify the invocation
   * @return app1[&lt;PrincipalName&gt;]@&lt;jboss.node.name&gt;
   */
  String invokeSecured(String text);

}
