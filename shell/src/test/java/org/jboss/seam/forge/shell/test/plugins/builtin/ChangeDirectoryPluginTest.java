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

package org.jboss.seam.forge.shell.test.plugins.builtin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.io.File;

import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.services.ResourceFactory;
import org.jboss.seam.forge.resources.DirectoryResource;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.test.AbstractShellTest;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@RunWith(Arquillian.class)
public class ChangeDirectoryPluginTest extends AbstractShellTest
{
   @Inject
   private ResourceFactory factory;

   @Test
   public void testTildeAliasesHomeDir() throws Exception
   {

      DirectoryResource home = new DirectoryResource(factory, new File(System.getProperty("user.home")));

      Shell shell = getShell();
      Resource<?> currentDirectory = shell.getCurrentResource();
      assertNotSame(home.getFullyQualifiedName(), currentDirectory.getFullyQualifiedName());

      shell.execute("cd ~");

      currentDirectory = shell.getCurrentResource();
      assertEquals(home.getFullyQualifiedName(), currentDirectory.getFullyQualifiedName());
   }

   @Test
   public void testDotMeansSameDirectory() throws Exception
   {
      Shell shell = getShell();
      shell.execute("cd ~");
      Resource<?> currentDirectory = shell.getCurrentResource();

      shell.execute("cd ./");

      Resource<?> newDir = shell.getCurrentResource();
      assertEquals(currentDirectory.getFullyQualifiedName(), newDir.getFullyQualifiedName());
   }

   @Test
   public void testDoubleDotMeansParentDirectory() throws Exception
   {
      Shell shell = getShell();
      shell.execute("cd ~");

      Resource<?> parentDir = shell.getCurrentResource().getParent();

      shell.execute("cd ../");

      Resource<?> newDir = shell.getCurrentResource();
      assertEquals(newDir.getFullyQualifiedName(), parentDir.getFullyQualifiedName());
   }

   @Test
   public void testReturnToLastDirectory() throws Exception
   {
      Shell shell = getShell();
      shell.execute("cd ~");

      Resource<?> base = shell.getCurrentResource();

      shell.execute("cd ../");
      shell.execute("cd -");

      Resource<?> newDir = shell.getCurrentResource();
      assertEquals(base, newDir);
   }

   @Test
   public void testAbsolutePath() throws Exception
   {
      Shell shell = getShell();
      shell.execute("cd ~");

      Resource<?> currentDirectory = shell.getCurrentResource();
      Resource<?> parent = currentDirectory.getParent();

      String parentPath = parent.getFullyQualifiedName();

      shell.execute("cd " + parentPath);

      Resource<?> newDir = shell.getCurrentResource();
      assertEquals(newDir.getFullyQualifiedName(), parent.getFullyQualifiedName());
   }

}
