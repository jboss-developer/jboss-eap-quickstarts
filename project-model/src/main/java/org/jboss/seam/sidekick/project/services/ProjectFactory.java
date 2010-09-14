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
package org.jboss.seam.sidekick.project.services;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jboss.seam.sidekick.project.Facet;
import org.jboss.seam.sidekick.project.Project;
import org.jboss.seam.sidekick.project.ProjectModelException;
import org.jboss.seam.sidekick.project.model.ProjectImpl;
import org.jboss.weld.extensions.util.service.ServiceLoader;

/**
 * Responsible for instantiating project instances through CDI.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Singleton
public class ProjectFactory
{
   private final FacetFactory facetFactory;
   private List<ProjectLocator> locators = null;

   @Inject
   public ProjectFactory(final FacetFactory facetFactory)
   {
      this.facetFactory = facetFactory;
   }

   public Project createProject(final File targetDir, final Class<? extends Facet>... facetTypes)
   {
      Project project = new ProjectImpl(targetDir);
      installFacets(project, facetTypes);
      return project;
   }

   private void installFacets(final Project project, final Class<? extends Facet>... facetTypes)
   {
      for (Class<? extends Facet> type : facetTypes)
      {
         Facet facet = facetFactory.getFacet(type);
         installSingleFacet(project, facet);
      }
   }

   private void installSingleFacet(final Project project, final Facet facet)
   {
      Set<Class<? extends Facet>> dependencies = facet.getDependencies();

      if (dependencies != null)
      {
         for (Class<? extends Facet> dep : dependencies)
         {
            if (!project.hasFacet(dep))
            {
               Facet depFacet = facetFactory.getFacet(dep);
               installSingleFacet(project, depFacet);
            }
         }
      }

      facet.init(project).install();
   }

   public Project findProject(final File startingPath)
   {
      Project project = null;
      List<ProjectLocator> locators = getLocators();
      for (ProjectLocator locator : locators)
      {
         project = locator.findProject(startingPath.getAbsoluteFile());
      }

      if (project == null)
      {
         throw new ProjectModelException("Could not locate project in folder or any of its parents: ["
                  + startingPath.getAbsolutePath() + "]");
      }
      registerFacets(project);

      return project;
   }

   private void registerFacets(final Project project)
   {
      if (project != null)
      {
         List<Facet> facets = facetFactory.getFacets();

         for (Facet facet : facets)
         {
            registerSingleFacet(project, facet);
         }
      }
   }

   private void registerSingleFacet(final Project project, final Facet facet)
   {
      Set<Class<? extends Facet>> dependencies = facet.getDependencies();

      if (dependencies != null)
      {
         for (Class<? extends Facet> dep : dependencies)
         {
            if (!project.hasFacet(dep))
            {
               Facet depFacet = facetFactory.getFacet(dep);
               registerSingleFacet(project, depFacet);
            }
         }
      }

      facet.init(project);
      if (facet.isInstalled() && !project.hasFacet(facet.getClass()))
      {
         project.registerFacet(facet);
      }
   }

   private void loadServices()
   {
      Iterator<ProjectLocator> providers = ServiceLoader.load(ProjectLocator.class).iterator();
      while (providers.hasNext())
      {
         locators.add(providers.next());
      }
   }

   public List<ProjectLocator> getLocators()
   {
      if (locators == null)
      {
         locators = new ArrayList<ProjectLocator>();
         loadServices();
      }
      return locators;
   }
}
