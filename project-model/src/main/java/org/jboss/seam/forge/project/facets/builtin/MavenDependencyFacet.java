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
package org.jboss.seam.forge.project.facets.builtin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.model.Model;
import org.apache.maven.model.Repository;
import org.jboss.seam.forge.project.Facet;
import org.jboss.seam.forge.project.dependencies.Dependency;
import org.jboss.seam.forge.project.dependencies.DependencyBuilder;
import org.jboss.seam.forge.project.dependencies.DependencyRepository;
import org.jboss.seam.forge.project.dependencies.DependencyRepositoryImpl;
import org.jboss.seam.forge.project.dependencies.MavenDependencyAdapter;
import org.jboss.seam.forge.project.facets.BaseFacet;
import org.jboss.seam.forge.project.facets.DependencyFacet;
import org.jboss.seam.forge.project.facets.FacetNotFoundException;
import org.jboss.seam.forge.project.facets.MavenCoreFacet;
import org.jboss.seam.forge.shell.plugins.Alias;
import org.jboss.seam.forge.shell.plugins.RequiresFacet;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Dependent
@Alias("forge.maven.MavenDependencyFacet")
@RequiresFacet(MavenCoreFacet.class)
public class MavenDependencyFacet extends BaseFacet implements DependencyFacet, Facet
{
   private final RepositoryLookup lookup;

   @Inject
   public MavenDependencyFacet(final RepositoryLookup lookup)
   {
      this.lookup = lookup;
   }

   @Override
   public boolean isInstalled()
   {
      try
      {
         project.getFacet(MavenCoreFacet.class);
         return true;
      }
      catch (FacetNotFoundException e)
      {
         return false;
      }
   }

   @Override
   public boolean install()
   {
      project.registerFacet(this);
      return true;
   }

   @Override
   public void addDependency(final Dependency dep)
   {
      MavenCoreFacet maven = project.getFacet(MavenCoreFacet.class);
      if (!hasDependency(dep))
      {
         Model pom = maven.getPOM();
         List<Dependency> dependencies = MavenDependencyAdapter.fromMavenList(pom.getDependencies());
         dependencies.add(dep);
         pom.setDependencies(MavenDependencyAdapter.toMavenList(dependencies));
         maven.setPOM(pom);
      }
   }

   @Override
   public boolean hasDependency(final Dependency dep)
   {
      MavenCoreFacet maven = project.getFacet(MavenCoreFacet.class);
      List<Dependency> dependencies = MavenDependencyAdapter.fromMavenList(maven.getProjectBuildingResult()
               .getProject().getDependencies());

      for (Dependency dependency : dependencies)
      {
         if (areEquivalent(dependency, dep))
         {
            return true;
         }
      }
      return false;
   }

   @Override
   public boolean hasDirectDependency(final Dependency dependency)
   {
      MavenCoreFacet maven = project.getFacet(MavenCoreFacet.class);
      Model pom = maven.getPOM();
      List<Dependency> dependencies = MavenDependencyAdapter.fromMavenList(pom.getDependencies());

      for (Dependency dep : dependencies)
      {
         if (areEquivalent(dependency, dep))
         {
            return true;
         }
      }
      return false;
   }

   @Override
   public void removeDependency(final Dependency dep)
   {
      MavenCoreFacet maven = project.getFacet(MavenCoreFacet.class);
      Model pom = maven.getPOM();
      List<Dependency> dependencies = MavenDependencyAdapter.fromMavenList(pom.getDependencies());

      List<Dependency> toBeRemoved = new ArrayList<Dependency>();
      for (Dependency dependency : dependencies)
      {
         if (areEquivalent(dependency, dep))
         {
            toBeRemoved.add(dependency);
         }
      }
      dependencies.removeAll(toBeRemoved);
      pom.setDependencies(MavenDependencyAdapter.toMavenList(dependencies));
      maven.setPOM(pom);
   }

   @Override
   public List<Dependency> getDependencies()
   {
      MavenCoreFacet maven = project.getFacet(MavenCoreFacet.class);
      Model pom = maven.getPOM();
      List<Dependency> dependencies = MavenDependencyAdapter.fromMavenList(pom.getDependencies());
      return Collections.unmodifiableList(dependencies);
   }

   @Override
   public Dependency getDependency(final Dependency dep)
   {
      MavenCoreFacet maven = project.getFacet(MavenCoreFacet.class);
      List<Dependency> dependencies = MavenDependencyAdapter.fromMavenList(maven.getProjectBuildingResult()
               .getProject().getDependencies());

      for (Dependency dependency : dependencies)
      {
         if (areEquivalent(dependency, dep))
         {
            return dependency;
         }
      }
      return null;
   }

   private boolean areEquivalent(final Dependency left, final Dependency right)
   {
      // FIXME version checking needs to be much more robust
      boolean result = false;
      if (left.getGroupId().equals(right.getGroupId()) && left.getArtifactId().equals(right.getArtifactId()))
      {
         if ((left.getVersion() != null) && (right.getVersion() != null))
         {
            ArtifactVersion lversion = new DefaultArtifactVersion(left.getVersion());
            ArtifactVersion rversion = new DefaultArtifactVersion(right.getVersion());

            if (lversion.compareTo(rversion) == 0)
            {
               result = true;
            }
         }
         else
         {
            result = true;
         }
      }
      return result;
   }

   @Override
   public Map<String, String> getProperties()
   {
      MavenCoreFacet maven = project.getFacet(MavenCoreFacet.class);
      Model pom = maven.getPOM();

      Properties properties = pom.getProperties();
      Map<String, String> result = new HashMap<String, String>();
      for (Entry<Object, Object> o : properties.entrySet())
      {
         result.put((String) o.getKey(), (String) o.getValue());
      }
      return result;
   }

   @Override
   public void setProperty(final String name, final String value)
   {
      MavenCoreFacet maven = project.getFacet(MavenCoreFacet.class);
      Model pom = maven.getPOM();

      Properties properties = pom.getProperties();
      properties.put(name, value);
      maven.setPOM(pom);
   }

   @Override
   public String getProperty(final String name)
   {
      MavenCoreFacet maven = project.getFacet(MavenCoreFacet.class);
      Model pom = maven.getPOM();

      Properties properties = pom.getProperties();
      maven.setPOM(pom);
      return (String) properties.get(name);
   }

   @Override
   public String removeProperty(final String name)
   {
      MavenCoreFacet maven = project.getFacet(MavenCoreFacet.class);
      Model pom = maven.getPOM();

      Properties properties = pom.getProperties();
      String result = (String) properties.remove(name);
      maven.setPOM(pom);
      return result;
   }

   @Override
   public List<Dependency> resolveAvailableVersions(final String gavs)
   {
      return resolveAvailableVersions(DependencyBuilder.create(gavs));
   }

   @Override
   public List<Dependency> resolveAvailableVersions(final Dependency dep)
   {
      List<Dependency> results = new ArrayList<Dependency>();

      List<String> versions = lookup.getAvailableVersions(dep.getGroupId() + ":" + dep.getArtifactId() + ":"
               + dep.getVersion(), getRepositories());
      for (String version : versions)
      {
         results.add(DependencyBuilder.create(dep).setVersion(version));
      }
      return results;
   }

   @Override
   public void addRepository(final String name, final String url)
   {
      MavenCoreFacet maven = project.getFacet(MavenCoreFacet.class);
      Model pom = maven.getPOM();
      Repository repo = new Repository();
      repo.setId(name);
      repo.setUrl(url);
      pom.getRepositories().add(repo);
      maven.setPOM(pom);
   }

   @Override
   public void addRepository(KnownRepository repository)
   {
      addRepository(repository.name(), repository.getUrl());
   }

   @Override
   public List<DependencyRepository> getRepositories()
   {
      List<DependencyRepository> results = new ArrayList<DependencyRepository>();
      MavenCoreFacet maven = project.getFacet(MavenCoreFacet.class);
      Model pom = maven.getPOM();
      List<Repository> repos = pom.getRepositories();
      for (Repository repo : repos)
      {
         results.add(new DependencyRepositoryImpl(repo.getId(), repo.getUrl()));
      }
      return Collections.unmodifiableList(results);
   }

   @Override
   public boolean hasRepository(final String url)
   {
      if (url != null)
      {
         MavenCoreFacet maven = project.getFacet(MavenCoreFacet.class);
         Model pom = maven.getPOM();
         List<Repository> repositories = pom.getRepositories();
         for (Repository repo : repositories)
         {
            if (repo.getUrl().trim().equals(url.trim()))
            {
               repositories.remove(repo);
               maven.setPOM(pom);
               return true;
            }
         }
      }
      return false;
   }

   @Override
   public boolean hasRepository(KnownRepository repository)
   {
      return hasRepository(repository.getUrl());
   }

   @Override
   public DependencyRepository removeRepository(final String url)
   {
      if (url != null)
      {
         MavenCoreFacet maven = project.getFacet(MavenCoreFacet.class);
         Model pom = maven.getPOM();
         List<Repository> repos = pom.getRepositories();
         for (Repository repo : repos)
         {
            if (repo.getUrl().equals(url.trim()))
            {
               repos.remove(repo);
               maven.setPOM(pom);
               return new DependencyRepositoryImpl(repo.getId(), repo.getUrl());
            }
         }
      }
      return null;

   }
}
