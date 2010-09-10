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
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import jline.console.ConsoleReader;

import org.jboss.arquillian.api.Deployment;
import org.jboss.seam.sidekick.project.model.MavenProject;
import org.jboss.seam.sidekick.project.model.maven.DependencyBuilder;
import org.jboss.seam.sidekick.shell.Shell;
import org.jboss.seam.sidekick.shell.ShellImpl;
import org.jboss.seam.sidekick.shell.plugins.events.Startup;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public abstract class AbstractShellTest
{
   @Deployment
   public static JavaArchive getDeployment()
   {

      JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "test.jar");
      archive.addPackages(true, ShellImpl.class.getPackage())
             .addClass(DependencyBuilder.class)
             .addManifestResource(new ByteArrayAsset("<beans/>".getBytes()), ArchivePaths.create("beans.xml"));

      return archive;
   }

   @Inject
   private Shell shell;

   @Inject
   private BeanManager beanManager;

   private Queue<String> inputQueue = new LinkedList<String>();

   private static final String PKG = AbstractShellTest.class.getSimpleName().toLowerCase();
   private static File tempFolder;
   private static MavenProject project;

   @BeforeClass
   public static void before() throws IOException
   {
   }

   @AfterClass
   public static void after()
   {
   }

   @Before
   public void beforeTest() throws IOException
   {
      tempFolder = File.createTempFile(PKG, null);
      tempFolder.delete();
      tempFolder.mkdirs();
      project = new MavenProject(tempFolder, true);

      List<String> parameters = new ArrayList<String>();
      parameters.add("--verbose");
      parameters.add("--pretend");
      parameters.add(tempFolder.getAbsolutePath());
      ((ShellImpl) getShell()).setParameters(parameters);

      beanManager.fireEvent(new Startup(), new Annotation[] {});

      inputQueue = new LinkedList<String>();
      QueuedInputStream is = new QueuedInputStream(inputQueue);
      ((ShellImpl) getShell()).setReader(new ConsoleReader(is, new PrintWriter(System.out)));
   }

   @After
   public void afterTest() throws IOException
   {
      if (tempFolder.exists())
      {
         assertTrue(project.delete(tempFolder));
      }
   }

   protected void queueInputLines(String... inputs)
   {
      for (String input : inputs)
      {
         inputQueue.add(input + "\n");
      }
   }

   protected BeanManager getBeanManager()
   {
      return beanManager;
   }

   protected Shell getShell()
   {
      return shell;
   }

}
