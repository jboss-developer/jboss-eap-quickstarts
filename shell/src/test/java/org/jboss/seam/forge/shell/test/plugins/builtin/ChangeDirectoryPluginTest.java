/*
 * JBoss, Home of Professional Open Source
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

package org.jboss.seam.forge.shell.test.plugins.builtin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.io.File;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.test.AbstractShellTest;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@RunWith(Arquillian.class)
public class ChangeDirectoryPluginTest extends AbstractShellTest
{
   @Test
   public void testTildeAliasesHomeDir() throws Exception
   {
      File home = new File(System.getProperty("user.home")).getAbsoluteFile();

      Shell shell = getShell();
      File currentDirectory = shell.getCurrentDirectory();
      assertNotSame(home, currentDirectory.getAbsoluteFile());

      shell.execute("cd ~");

      currentDirectory = shell.getCurrentDirectory();
      assertEquals(home, currentDirectory.getAbsoluteFile());
   }

   @Test
   public void testDotMeansSameDirectory() throws Exception
   {
      Shell shell = getShell();
      shell.execute("cd ~");
      File currentDirectory = shell.getCurrentDirectory();

      shell.execute("cd ./");

      File newDir = shell.getCurrentDirectory();
      assertEquals(newDir.getAbsoluteFile(), currentDirectory.getAbsoluteFile());
   }

   @Test
   public void testDoubleDotMeansParentDirectory() throws Exception
   {
      Shell shell = getShell();
      shell.execute("cd ~");

      File parentDir = shell.getCurrentDirectory().getParentFile();

      shell.execute("cd ../");

      File newDir = shell.getCurrentDirectory();
      assertEquals(newDir.getAbsoluteFile(), parentDir.getAbsoluteFile());
   }

   @Test
   public void testReturnToLastDirectory() throws Exception
   {
      Shell shell = getShell();
      shell.execute("cd ~");

      File base = shell.getCurrentDirectory();

      shell.execute("cd ../");
      shell.execute("cd -");

      File newDir = shell.getCurrentDirectory();
      assertEquals(base, newDir);
   }

   @Test
   public void testAbsolutePath() throws Exception
   {
      Shell shell = getShell();
      shell.execute("cd ~");

      File currentDirectory = shell.getCurrentDirectory().getAbsoluteFile();
      File parentDir = currentDirectory.getParentFile().getAbsoluteFile();

      shell.execute("cd " + parentDir.getAbsolutePath());

      File newDir = shell.getCurrentDirectory().getAbsoluteFile();
      assertEquals(newDir.getAbsoluteFile(), parentDir.getAbsoluteFile());
   }

}
