/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc., and individual contributors
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

import java.util.ArrayList;
import java.util.List;

import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.repository.RemoteRepository;
import org.sonatype.aether.resolution.ArtifactDescriptorResult;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class DependencyMetadataImpl implements DependencyMetadata
{

   private final Dependency dependency;
   private final List<DependencyRepository> repositories;
   private final List<Dependency> managedDependencies;
   private final List<Dependency> dependencies;

   public DependencyMetadataImpl(Dependency query, ArtifactDescriptorResult descriptor)
   {
      this.dependency = query;

      this.repositories = new ArrayList<DependencyRepository>();
      for (RemoteRepository r : descriptor.getRepositories())
      {
         repositories.add(new DependencyRepositoryImpl(r.getId(), r.getUrl()));
      }

      managedDependencies = new ArrayList<Dependency>();
      for (org.sonatype.aether.graph.Dependency d : descriptor.getManagedDependencies())
      {
         managedDependencies.add(convertToForge(d));
      }

      dependencies = new ArrayList<Dependency>();
      for (org.sonatype.aether.graph.Dependency d : descriptor.getDependencies())
      {
         dependencies.add(convertToForge(d));
      }
   }

   private Dependency convertToForge(org.sonatype.aether.graph.Dependency d)
   {
      Artifact a = d.getArtifact();
      Dependency dep = DependencyBuilder.create()
               .setArtifactId(a.getArtifactId())
               .setGroupId(a.getGroupId())
               .setVersion(a.getBaseVersion());
      return dep;
   }

   @Override
   public String toString()
   {
      return "[dependency=" + dependency + ", repositories=" + repositories
               + ", managedDependencies=" + managedDependencies + ", dependencies=" + dependencies + "]";
   }

   @Override
   public Dependency getDependency()
   {
      return dependency;
   }

   @Override
   public List<Dependency> getManagedDependencies()
   {
      return managedDependencies;
   }

   @Override
   public List<Dependency> getDependencies()
   {
      return dependencies;
   }

   @Override
   public List<DependencyRepository> getRepositories()
   {
      return repositories;
   }

}
