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

package org.jboss.seam.forge.project.dependencies;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.forge.project.facets.DependencyFacet.KnownRepository;
import org.jboss.seam.forge.resources.DependencyResource;
import org.jboss.seam.forge.test.project.util.ProjectModelTest;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Singleton
@RunWith(Arquillian.class)
public class RepositoryLookupTest extends ProjectModelTest
{
   @Deployment
   public static JavaArchive getTestArchive()
   {
      return createTestArchive()
               .addManifestResource(
                        "META-INF/services/org.jboss.seam.forge.project.dependencies.DependencyResolverProvider");
   }

   @Inject
   private DependencyResolver resolver;

   @Test
   public void testResolveVersionsWithResolver() throws Exception
   {
      Dependency dep = DependencyBuilder.create("com.ocpsoft:prettyfaces-jsf2");
      DependencyRepository repo = new DependencyRepositoryImpl(KnownRepository.CENTRAL);
      List<Dependency> versions = resolver.resolveVersions(dep, Arrays.asList(repo));
      assertTrue(versions.size() > 4);
   }

   @Test
   public void testResolveVersionsWithResolverStaticVersion() throws Exception
   {
      Dependency dep = DependencyBuilder.create("com.ocpsoft:prettyfaces-jsf2:3.2.0");
      DependencyRepository repo = new DependencyRepositoryImpl(KnownRepository.CENTRAL);
      List<Dependency> versions = resolver.resolveVersions(dep, Arrays.asList(repo));
      assertTrue(versions.size() >= 1);
   }

   @Test
   public void testResolveVersionsWithResolverStaticVersionSnapshot() throws Exception
   {
      Dependency dep = DependencyBuilder.create("org.jboss.errai.forge:forge-errai:1.0-SNAPSHOT");
      DependencyRepository repo = new DependencyRepositoryImpl(KnownRepository.JBOSS_NEXUS);
      List<Dependency> versions = resolver.resolveVersions(dep, repo);
      assertTrue(versions.size() >= 1);
   }

   @Test
   public void testResolveArtifactsWithResolver() throws Exception
   {
      Dependency dep = DependencyBuilder.create("org.jboss.errai.forge:forge-errai");
      DependencyRepository repo = new DependencyRepositoryImpl(KnownRepository.JBOSS_NEXUS);
      List<DependencyResource> artifacts = resolver.resolveArtifacts(dep, Arrays.asList(repo));
      assertTrue(artifacts.size() >= 1);
   }

   @Test
   public void testResolveArtifactsWithResolverSnapshotStaticVersion() throws Exception
   {
      Dependency dep = DependencyBuilder.create("org.jboss.errai.forge:forge-errai:[1.0-SNAPSHOT]");
      DependencyRepository repo = new DependencyRepositoryImpl(KnownRepository.JBOSS_NEXUS);
      List<DependencyResource> artifacts = resolver.resolveArtifacts(dep, Arrays.asList(repo));
      assertTrue(artifacts.size() >= 1);
   }

   @Test
   public void testResolveArtifactsWithResolverStaticVersion() throws Exception
   {
      Dependency dep = DependencyBuilder.create("com.ocpsoft:prettyfaces-jsf2:[3.2.0]");
      DependencyRepository repo = new DependencyRepositoryImpl(KnownRepository.CENTRAL);
      List<DependencyResource> artifacts = resolver.resolveArtifacts(dep, Arrays.asList(repo));
      assertTrue(artifacts.size() >= 1);
   }
}
