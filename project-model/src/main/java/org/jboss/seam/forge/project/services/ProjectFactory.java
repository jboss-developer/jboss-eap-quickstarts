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
package org.jboss.seam.forge.project.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jboss.seam.forge.project.Facet;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.model.ProjectImpl;
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
   private final ResourceFactory resourceFactory;
   private List<ProjectLocator> locators = null;

   @Inject
   public ProjectFactory(final FacetFactory facetFactory, final ResourceFactory resourceFactory)
   {
      this.facetFactory = facetFactory;
      this.resourceFactory = resourceFactory;
   }

   public Project findProjectRecursively(final File startingPath) throws FileNotFoundException
   {
      Project project = null;
      List<ProjectLocator> locators = getLocators();
      for (ProjectLocator locator : locators)
      {
         project = locator.findProjectRecursively(startingPath.getAbsoluteFile());

         if (project != null)
         {
            break;
         }
      }

      if (project == null)
      {
         throw new FileNotFoundException("Could not locate project in folder or any of its parents: ["
                  + startingPath.getAbsolutePath() + "]");
      }
      registerFacets(project);
      return project;
   }

   public Project createProject(final File targetDir, final Class<? extends Facet>... facetTypes)
   {
      Project project = new ProjectImpl(targetDir);

      for (Class<? extends Facet> type : facetTypes)
      {
         installSingleFacet(project, type);
      }

      registerFacets(project);

      return project;
   }

   private void installSingleFacet(final Project project, final Class<? extends Facet> type)
   {
      Facet facet = facetFactory.getFacet(type);

      Set<Class<? extends Facet>> dependencies = facet.getDependencies();
      if (dependencies != null)
      {
         for (Class<? extends Facet> dep : dependencies)
         {
            if (!project.hasFacet(dep))
            {
               installSingleFacet(project, dep);
            }
         }
      }

      project.installFacet(facet);
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
               if (!project.hasFacet(dep))
               {
                  return;
               }
            }
         }
      }

      project.registerFacet(facet);
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

   /**
    * An exception-safe method of determining whether a directory contains a
    * project.
    */
   public boolean containsProject(File dir)
   {
      try
      {
         findProject(dir);
         return true;
      }
      catch (FileNotFoundException e)
      {
         return false;
      }
   }

   public Project findProject(File path) throws FileNotFoundException
   {
      Project project = null;
      List<ProjectLocator> locators = getLocators();
      for (ProjectLocator locator : locators)
      {
         project = locator.findProject(path.getAbsoluteFile());
         if (project != null)
         {
            break;
         }
      }

      if (project == null)
      {
         throw new FileNotFoundException("Could not locate project in folder or any of its parents: ["
                  + path.getAbsolutePath() + "]");
      }
      registerFacets(project);
      return project;
   }

   public ResourceFactory getResourceFactory()
   {
      return resourceFactory;
   }
}
