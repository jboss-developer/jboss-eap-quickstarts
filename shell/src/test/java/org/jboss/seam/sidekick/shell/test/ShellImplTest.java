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

package org.jboss.seam.sidekick.shell.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.sidekick.project.model.MavenProject;
import org.jboss.seam.sidekick.project.model.maven.DependencyBuilder;
import org.jboss.seam.sidekick.shell.ShellImpl;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@RunWith(Arquillian.class)
public class ShellImplTest
{
   @Deployment
   public static JavaArchive createTestArchive()
   {
      return ShrinkWrap.create(JavaArchive.class, "test.jar")
               .addPackages(true, ShellImpl.class.getPackage())
               .addClass(DependencyBuilder.class)
               .addManifestResource(new ByteArrayAsset("<beans/>".getBytes()), ArchivePaths.create("beans.xml"));
   }

   private static final String PKG = ShellImplTest.class.getSimpleName().toLowerCase();
   private static File tempFolder;
   private static MavenProject project;

   @BeforeClass
   public static void before() throws IOException
   {
      tempFolder = File.createTempFile(PKG, null);
      tempFolder.delete();
      tempFolder.mkdirs();
      project = new MavenProject(tempFolder);
   }

   @AfterClass
   public static void after()
   {
      if (tempFolder.exists())
      {
         assertTrue(project.delete(tempFolder));
      }
   }

   @Test
   public void testStub() throws Exception
   {

   }
}
