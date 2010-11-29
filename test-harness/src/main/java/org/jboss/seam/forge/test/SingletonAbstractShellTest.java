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

package org.jboss.seam.forge.test;

import org.jboss.arquillian.api.Deployment;
import org.jboss.seam.forge.BasePackageMarker;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.model.ProjectImpl;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.events.Startup;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public abstract class SingletonAbstractShellTest
{
   @Deployment
   public static JavaArchive getDeployment()
   {

      JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "test.jar")
            .addPackages(true, BasePackageMarker.class.getPackage())
            .addManifestResource(new ByteArrayAsset("<beans/>".getBytes()), ArchivePaths.create("beans.xml"));

      return archive;
   }

   @Inject
   private BeanManager beanManager;
   @Inject
   private Shell shell;

   private File tempFolder;
   private static final List<File> tempFolders = new ArrayList<File>();
   private static final String PKG = SingletonAbstractShellTest.class.getSimpleName().toLowerCase();
   private static Queue<String> inputQueue = new LinkedList<String>();

   @Inject
   private Instance<Project> project;

   @Before
   public void beforeTest() throws IOException
   {
      if (shell == null)
      {
         throw new IllegalStateException("Failed to initialize Shell instance for test.");
      }

      if (tempFolder == null)
      {
         shell.setVerbose(true);
         beanManager.fireEvent(new Startup(), new Annotation[]{});

         resetInputQueue();
         shell.setOutputWriter(new PrintWriter(System.out));
      }
   }

   @After
   public void afterTest() throws IOException
   {
      resetInputQueue();
   }

   @AfterClass
   public static void afterClass() throws IOException
   {
      for (File file : tempFolders)
      {
         if (file.exists())
         {
            assertTrue(new ProjectImpl(file).delete(file));
         }
      }
   }

   /**
    * @throws IOException
    */
   protected static File createTempFolder() throws IOException
   {
      File tempFolder = File.createTempFile(PKG, null);
      tempFolder.delete();
      tempFolder.mkdirs();
      return tempFolder;
   }

   /**
    * Reset the shell input queue (called automatically before each test.)
    */
   protected void resetInputQueue() throws IOException
   {
      inputQueue = new LinkedList<String>();
      QueuedInputStream is = new QueuedInputStream(inputQueue);
      shell.setInputStream(is);
   }

   protected void queueInputLines(final String... inputs)
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

   protected Project getProject()
   {
      return project.get();
   }

   protected Project initializeJavaProject() throws IOException
   {
      File folder = createTempFolder();
      tempFolders.add(folder);
      getShell().execute("cd " + folder.getAbsolutePath());
      queueInputLines("", "");
      getShell().execute("new-project --named test --topLevelPackage com.test");

      Project project = getProject();
      tempFolder = project.getProjectRoot();
      tempFolders.add(tempFolder);
      return project;
   }

}
