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

package org.jboss.seam.sidekick.test.project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.inject.Inject;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.eclipse.core.internal.resources.Project;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.sidekick.parser.java.JavaClass;
import org.jboss.seam.sidekick.parser.java.JavaParser;
import org.jboss.seam.sidekick.project.model.LocatedAt;
import org.jboss.seam.sidekick.project.model.MavenProject;
import org.jboss.seam.sidekick.project.model.maven.DependencyBuilder;
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
public class MavenProjectTest
{
   @Deployment
   public static JavaArchive createTestArchive()
   {
      return ShrinkWrap.create(JavaArchive.class, "test.jar")
               .addPackages(true, Project.class.getPackage())
               .addClass(MavenProject.class)
               .addClass(DependencyBuilder.class)
               .addManifestResource(new ByteArrayAsset("<beans/>".getBytes()), ArchivePaths.create("beans.xml"));
   }

   private static final String PKG = MavenProjectTest.class.getSimpleName().toLowerCase();
   private static File tempFolder;
   private static MavenProject project;

   @Inject
   private MavenProject thisProject;

   @Inject
   @LocatedAt("src/test/resources/test-pom")
   private MavenProject testProject;

   @Inject
   @LocatedAt("/tmp")
   private MavenProject unknownProject;

   @Inject
   private DependencyBuilder dependencyBuilder;

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
   public void testGetDefaultSourceDir() throws Exception
   {
      assertEquals(new File(project.getProjectRoot() + "/src/main/java/"), project.getDefaultSourceFolder());
   }

   @Test
   public void testCreateJavaFile() throws Exception
   {
      String name = "JustCreated";
      JavaClass clazz = JavaParser.createClass().setName(name).setPackage(PKG);
      clazz.applyChanges();
      File file = project.createJavaFile(clazz);
      assertEquals(name + ".java", file.getName());

      JavaClass result = JavaParser.parse(file);
      assertEquals(name, result.getName());
      assertEquals(PKG, result.getPackage());
      assertTrue(project.delete(file));
      assertEquals(clazz, result);
   }

   @Test
   public void testCreatePOM() throws Exception
   {
      Model pom = project.getPOM();
      pom.setGroupId("org.jboss.seam");
      pom.setArtifactId("seam-scaffolding");
      pom.setVersion("X-SNAPSHOT");
      project.setPOM(pom);
      File file = pom.getPomFile();
      assertTrue(file.exists());

      MavenXpp3Reader reader = new MavenXpp3Reader();
      Model result = reader.read(new FileInputStream(file));
      assertEquals(pom.getArtifactId(), result.getArtifactId());
   }

   @Test
   public void testHasDependency() throws Exception
   {
      assertTrue(testProject.hasDependency(dependencyBuilder.setGroupId("com.ocpsoft")
               .setArtifactId("prettyfaces-jsf2").setVersion("3.0.2-SNAPSHOT").build()));
   }

   @Test
   public void testInjectedProjectIsCurrentProject() throws Exception
   {
      Model pom = thisProject.getPOM();
      assertEquals("sidekick-project-model", pom.getArtifactId());
   }

   @Test
   public void testInjectedAbsoluteProjectIsResolvedCorrectly() throws Exception
   {
      Model pom = testProject.getPOM();
      assertEquals("socialpm", pom.getArtifactId());
   }

   @Test
   public void testInjectedAbsoluteUnknownProjectCannotInstantiate() throws Exception
   {
      Model pom = unknownProject.getPOM();
      assertNull(pom.getArtifactId());
   }
}
