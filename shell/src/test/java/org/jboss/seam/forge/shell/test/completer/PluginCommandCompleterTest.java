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
package org.jboss.seam.forge.shell.test.completer;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.forge.shell.completer.PluginCommandCompleter;
import org.jboss.seam.forge.test.AbstractShellTest;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@RunWith(Arquillian.class)
public class PluginCommandCompleterTest extends AbstractShellTest
{
   @Deployment
   public static JavaArchive getDeployment()
   {
      return AbstractShellTest.getDeployment().addClass(MockCompleterPlugin.class);
   }

   @Inject
   private PluginCommandCompleter completer;

   @Test
   public void testCompleteNothing() throws Exception
   {
      ArrayList<CharSequence> candidates = new ArrayList<CharSequence>();
      int index = completer.complete("", 0, candidates);
      assertEquals(0, index);
      assertTrue(candidates.contains("mockcompleterplugin "));
      assertTrue(candidates.contains("mockcompleterplugin2 "));
   }

   @Test
   public void testPartialCompletion() throws Exception
   {
      ArrayList<CharSequence> candidates = new ArrayList<CharSequence>();
      int index = completer.complete("mock", 0, candidates);
      assertEquals(0, index);
      assertTrue(candidates.contains("mockcompleterplugin "));
   }

   @Test
   public void testFullPluginCompletionAddsSpace() throws Exception
   {
      ArrayList<CharSequence> candidates = new ArrayList<CharSequence>();
      String input = "mockcompleterplugin2";
      int index = completer.complete(input, 0, candidates);
      assertEquals(0, index);
      assertTrue(candidates.contains("mockcompleterplugin2 "));
   }

   @Test
   public void testDefaultCommandCompletion() throws Exception
   {
      ArrayList<CharSequence> candidates = new ArrayList<CharSequence>();
      String input = "mockcompleterplugin2 ";
      int index = completer.complete(input, 0, candidates);
      assertEquals(input.length(), index);
      assertTrue(candidates.contains(""));
   }

   @Test
   public void testCommandCompletion() throws Exception
   {
      ArrayList<CharSequence> candidates = new ArrayList<CharSequence>();
      String input = "mockcompleterplugin ";
      int index = completer.complete(input, 0, candidates);
      assertEquals(input.length(), index);
      assertTrue(candidates.contains("command1 "));
   }

   @Test
   public void testPartialCommandCompletion() throws Exception
   {
      ArrayList<CharSequence> candidates = new ArrayList<CharSequence>();
      String input = "mockcompleterplugin comm";
      int index = completer.complete(input, 0, candidates);
      assertEquals(input.indexOf("comm"), index);
      assertTrue(candidates.contains("command1 "));
      assertTrue(candidates.contains("command2 "));
   }

   @Test
   public void testRequiredNamedOptionCompletion() throws Exception
   {
      ArrayList<CharSequence> candidates = new ArrayList<CharSequence>();
      String input = "mockcompleterplugin command2 ";
      int index = completer.complete(input, 0, candidates);
      assertEquals(input.length(), index);
      assertTrue(candidates.contains("--option "));
   }
}
