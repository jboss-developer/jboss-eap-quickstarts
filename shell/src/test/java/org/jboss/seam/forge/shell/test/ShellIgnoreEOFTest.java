/*
 * JBoss, by Red Hat.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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

package org.jboss.seam.forge.shell.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.forge.shell.events.PostStartup;
import org.jboss.seam.forge.shell.events.PreShutdown;
import org.jboss.seam.forge.test.AbstractShellTest;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@RunWith(Arquillian.class)
public class ShellIgnoreEOFTest extends AbstractShellTest
{
   private static boolean shutdown = false;

   public void observeShutdown(@Observes PreShutdown event)
   {
      ShellIgnoreEOFTest.shutdown = true;
   }

   public void observeShutdown(@Observes PostStartup event)
   {
      ShellIgnoreEOFTest.shutdown = false;
   }

   @Inject
   private MockAbortingPlugin plugin;

   @Test
   public void testIgnoreEOFDefaultsToOne() throws Exception
   {
      assertFalse(shutdown);
      queueInputLines(null, null, null, null, null);
      String line = getShell().readLine();
      assertNull(line);
      line = getShell().readLine();
      assertNull(line);
      assertTrue(shutdown);
   }

   @Test
   public void testSetIgnoreEOF() throws Exception
   {
      assertFalse(shutdown);
      getShell().execute("set IGNOREEOF 7");
      queueInputLines("", null, null, null, null, null);
      getShell().readLine();
      assertNull(getShell().readLine());
      assertNull(getShell().readLine());
      assertNull(getShell().readLine());
      assertNull(getShell().readLine());
      assertNull(getShell().readLine());
      assertFalse(shutdown);
   }

   @Test
   public void testSetIgnoreEOFBadValueDefaultsToOne() throws Exception
   {
      assertFalse(shutdown);
      getShell().execute("set IGNOREEOF foo");
      queueInputLines("", null, null, null, null, null);

      assertEquals("", getShell().readLine());
      assertNull(getShell().readLine());
      assertNull(getShell().readLine());

      assertTrue(shutdown);
   }

   @Test
   public void testEOFAbortsPluginPromptButDoesNotShutdown() throws Exception
   {
      assertFalse(shutdown);
      queueInputLines(null, null);
      assertFalse(plugin.isAborted());
      assertFalse(plugin.isExecuted());
      getShell().execute("test-map");
      assertFalse(shutdown);
      assertTrue(plugin.isExecuted());
      assertTrue(plugin.isAborted());
   }
}
