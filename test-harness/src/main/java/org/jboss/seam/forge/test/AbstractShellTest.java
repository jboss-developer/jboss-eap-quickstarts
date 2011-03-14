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

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.forge.Root;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.services.ResourceFactory;
import org.jboss.seam.forge.resources.DirectoryResource;
import org.jboss.seam.forge.resources.FileResource;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.events.Startup;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@RunWith(Arquillian.class)
public abstract class AbstractShellTest
{
   @Deployment
   public static JavaArchive getDeployment()
   {

      JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "test.jar")
               .addPackages(true, Root.class.getPackage())
               .addManifestResource(new ByteArrayAsset("<beans/>".getBytes()), ArchivePaths.create("beans.xml"));

      return archive;
   }

   @Inject
   private Shell shell;

   @Inject
   private BeanManager beanManager;

   private Queue<String> inputQueue = new LinkedList<String>();

   private final List<FileResource<?>> tempFolders = new ArrayList<FileResource<?>>();

   private static final String PKG = AbstractShellTest.class.getSimpleName().toLowerCase();

   @Inject
   private Instance<Project> project;

   @Inject
   private ResourceFactory factory;

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
      shell.setVerbose(true);
      shell.setCurrentResource(createTempFolder());
      beanManager.fireEvent(new Startup());

      resetInputQueue();
      shell.setOutputWriter(new PrintWriter(System.out));
   }

   protected DirectoryResource createTempFolder() throws IOException
   {
      File tempFolder = File.createTempFile(PKG, null);
      tempFolder.delete();
      tempFolder.mkdirs();
      DirectoryResource resource = factory.getResourceFrom(tempFolder).reify(DirectoryResource.class);
      tempFolders.add(resource);
      return resource;
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

   @After
   public void afterTest() throws IOException
   {
      for (FileResource<?> file : tempFolders)
      {
         if (file.exists())
         {
            assertTrue(file.delete(true));
         }
      }
   }

   protected void queueInputLines(final String... inputs)
   {
      for (String input : inputs)
      {
         if (input == null || "\0".equals(input))
         {
            inputQueue.add(input);
         }
         else
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
      getShell().setCurrentResource(createTempFolder());
      queueInputLines("", "");
      getShell().execute("new-project --named test --topLevelPackage com.test");
      return getProject();
   }

}
