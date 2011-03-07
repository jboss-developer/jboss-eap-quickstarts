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
package org.jboss.seam.forge.project.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.seam.forge.project.Facet;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.locator.ProjectLocator;
import org.jboss.seam.forge.resources.DirectoryResource;
import org.jboss.seam.forge.shell.util.ConstraintInspector;
import org.jboss.seam.forge.shell.util.ResourceUtil;

/**
 * Responsible for instantiating project instances through CDI.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Dependent
public class ProjectFactory
{
   private final FacetFactory facetFactory;
   private final List<ProjectLocator> locators;

   @Inject
   public ProjectFactory(final FacetFactory facetFactory,
                         final Instance<ProjectLocator> locatorInstance)
   {
      this.facetFactory = facetFactory;
      Iterator<ProjectLocator> iterator = locatorInstance.iterator();
      List<ProjectLocator> result = new ArrayList<ProjectLocator>();
      while (iterator.hasNext())
      {
         ProjectLocator element = iterator.next();
         result.add(element);
      }
      this.locators = result;

   }

   public DirectoryResource findProjectRootRecusively(final DirectoryResource currentDirectory)
   {
      DirectoryResource root = null;
      List<ProjectLocator> locators = getLocators();
      for (ProjectLocator locator : locators)
      {
         root = locateRecursively(currentDirectory, locator);
         if (root != null)
         {
            break;
         }
      }
      return root;
   }

   public DirectoryResource locateRecursively(final DirectoryResource startingDirectory, final ProjectLocator locator)
   {
      DirectoryResource root = startingDirectory;

      Project project = locator.createProject(root);
      while ((project == null) && (root.getParent() instanceof DirectoryResource))
      {
         root = (DirectoryResource) root.getParent();
         project = locator.createProject(root);
      }

      if (project == null)
      {
         root = null;
      }

      return root;
   }

   public Project findProjectRecursively(final DirectoryResource startingPath)
   {
      Project project = null;
      List<ProjectLocator> locators = getLocators();
      for (ProjectLocator locator : locators)
      {
         DirectoryResource root = locateRecursively(startingPath, locator);

         if ((root != null) && locator.containsProject(root))
         {
            project = locator.createProject(root);
            if (project != null)
            {
               break;
            }
         }
      }

      if (project != null)
      {
         registerFacets(project);
      }

      return project;
   }

   public Project createProject(final DirectoryResource root, final Class<? extends Facet>... facetTypes)
   {
      Project project = null;
      List<ProjectLocator> locators = getLocators();
      for (ProjectLocator locator : locators)
      {
         if (root != null)
         {
            project = locator.createProject(root);
            if (project != null)
            {
               break;
            }
         }
      }

      for (Class<? extends Facet> type : facetTypes)
      {
         installSingleFacet(project, type);
      }

      registerFacets(project);

      return project;
   }

   public void installSingleFacet(final Project project, final Class<? extends Facet> type)
   {
      Facet facet = facetFactory.getFacet(type);

      List<Class<? extends Facet>> dependencies = ConstraintInspector.getFacetDependencies(facet.getClass());
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
      List<Class<? extends Facet>> dependencies = ConstraintInspector.getFacetDependencies(facet.getClass());
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

   /**
    * An exception-safe method of determining whether a directory contains a project.
    */
   public boolean containsProject(final DirectoryResource dir)
   {
      Project project = findProject(dir);
      return project != null;
   }

   public Project findProject(final DirectoryResource dir)
   {
      Project project = null;
      if (dir != null)
      {
         List<ProjectLocator> locators = getLocators();
         for (ProjectLocator locator : locators)
         {
            if (locator.containsProject(ResourceUtil.getContextDirectory(dir)))
            {
               project = locator.createProject(dir);
               break;
            }
         }

         if (project != null)
         {
            registerFacets(project);
         }
      }
      return project;
   }

   private List<ProjectLocator> getLocators()
   {
      return locators;
   }
}
