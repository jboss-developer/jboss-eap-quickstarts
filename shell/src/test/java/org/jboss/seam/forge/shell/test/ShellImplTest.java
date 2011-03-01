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
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.forge.test.AbstractShellTest;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@RunWith(Arquillian.class)
public class ShellImplTest extends AbstractShellTest
{
   @Test
   public void testPromptBoolean() throws Exception
   {
      queueInputLines("y");
      assertTrue(getShell().promptBoolean("Would you like cake?"));

      queueInputLines("yes");
      assertTrue(getShell().promptBoolean("Would you like cake?"));

      queueInputLines("n");
      assertFalse(getShell().promptBoolean("Would you like cake?"));

      queueInputLines("no");
      assertFalse(getShell().promptBoolean("Would you like cake?"));
   }

   @Test
   public void testPromptBooleanDefaultsToYes() throws Exception
   {
      queueInputLines("");
      assertTrue(getShell().promptBoolean("Would you like cake?"));
   }

   @Test
   public void testPromptBooleanLoopsIfBadInput() throws Exception
   {
      queueInputLines("asdfdsf\n \n");
      assertFalse(getShell().promptBoolean("Would you like cake?", false));

      queueInputLines("asdfdsf\n n\n");
      assertFalse(getShell().promptBoolean("Would you like cake?", false));

      queueInputLines("asdfdsf\n y\n");
   }

   @Test
   public void testPromptChoiceList() throws Exception
   {
      List<String> choices = Arrays.asList("blue", "green", "red", "yellow");

      queueInputLines("foo", "2");
      int choice = getShell().promptChoice("What is your favorite color?", choices);
      assertEquals(1, choice);
   }

   @Test
   public void testPromptChoiceListTyped() throws Exception
   {
      List<String> choices = Arrays.asList("blue", "green", "red", "yellow");

      queueInputLines("foo", "2");
      String choice = getShell().promptChoiceTyped("What is your favorite color?", choices);
      assertEquals(choices.get(1), choice);
   }

//   @Test
//   public void testExecuteCommand() throws Exception
//   {
//      getShell().setVerbose(true);
//      assertTrue(getShell().isVerbose());
//      getShell().execute("verbose off");
//      assertFalse(getShell().isVerbose());
//      getShell().setVerbose(true);
//   }
}
