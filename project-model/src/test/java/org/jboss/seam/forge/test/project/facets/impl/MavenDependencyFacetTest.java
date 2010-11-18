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

package org.jboss.seam.forge.test.project.facets.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.dependencies.Dependency;
import org.jboss.seam.forge.project.dependencies.DependencyBuilder;
import org.jboss.seam.forge.project.facets.DependencyFacet;
import org.jboss.seam.forge.project.services.ProjectFactory;
import org.jboss.seam.forge.test.project.util.ProjectModelTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@Singleton
@RunWith(Arquillian.class)
public class MavenDependencyFacetTest extends ProjectModelTest
{
   private static final String PKG = MavenDependencyFacetTest.class.getSimpleName().toLowerCase();

   @Inject
   private ProjectFactory projectFactory;

   private static Project testProject;

   @Before
   @Override
   public void postConstruct() throws IOException
   {
      super.postConstruct();

      if (testProject == null)
      {
         testProject = projectFactory.findProjectRecursively(new File("src/test/resources/test-pom"));
      }
   }

   @Test
   public void testHasDependency() throws Exception
   {
      DependencyFacet deps = testProject.getFacet(DependencyFacet.class);

      DependencyBuilder prettyfaces = DependencyBuilder.create("com.ocpsoft:prettyfaces-jsf2:3.0.2-SNAPSHOT");
      assertTrue(deps.hasDependency(prettyfaces));
   }

   @Test
   public void testAddDependency() throws Exception
   {
      Dependency dependency =
               DependencyBuilder.create("org.jboss:test-dependency:1.0.0.Final");

      Project project = getProject();
      DependencyFacet deps = project.getFacet(DependencyFacet.class);
      assertFalse(deps.hasDependency(dependency));
      deps.addDependency(dependency);
      assertTrue(deps.hasDependency(dependency));
   }

   @Test
   public void testRemoveDependency() throws Exception
   {
      Dependency dependency =
               DependencyBuilder.create("org.jboss:test-dependency:1.0.1.Final");

      Project project = getProject();
      DependencyFacet deps = project.getFacet(DependencyFacet.class);

      assertFalse(deps.hasDependency(dependency));
      deps.addDependency(dependency);
      assertTrue(deps.hasDependency(dependency));
      deps.removeDependency(dependency);
      assertFalse(deps.hasDependency(dependency));
   }

   public void testAddProperty() throws Exception
   {
      String version = "1.0.2.Final";

      Project project = getProject();
      DependencyFacet deps = project.getFacet(DependencyFacet.class);

      deps.setProperty("version", version);

      assertEquals(version, deps.getProperty("version"));
   }
}
