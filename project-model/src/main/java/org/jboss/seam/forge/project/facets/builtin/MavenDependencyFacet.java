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

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.model.Model;
import org.jboss.seam.forge.project.Facet;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.constraints.RequiresFacets;
import org.jboss.seam.forge.project.dependencies.Dependency;
import org.jboss.seam.forge.project.dependencies.MavenDependencyAdapter;
import org.jboss.seam.forge.project.facets.DependencyFacet;
import org.jboss.seam.forge.project.facets.FacetNotFoundException;
import org.jboss.seam.forge.project.facets.MavenCoreFacet;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@Dependent
@RequiresFacets({ MavenCoreFacet.class })
public class MavenDependencyFacet implements DependencyFacet, Facet
{
   private Project project;

   @Override
   public Project getProject()
   {
      return project;
   }

   @Override
   public void setProject(final Project project)
   {
      this.project = project;
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
   public Facet install()
   {
      project.registerFacet(this);
      return this;
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

   @SuppressWarnings("unchecked")
   private boolean areEquivalent(final Dependency left, final Dependency right)
   {
      boolean result = false;
      if (left.getGroupId().equals(right.getGroupId()) && left.getArtifactId().equals(right.getArtifactId()))
      {
         ArtifactVersion lversion = new DefaultArtifactVersion(left.getVersion());
         ArtifactVersion rversion = new DefaultArtifactVersion(right.getVersion());

         if (lversion.compareTo(rversion) == 0)
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

}
