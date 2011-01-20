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
package org.jboss.seam.forge.shell.constraint;

import java.util.List;

import org.jboss.seam.forge.project.Facet;
import org.jboss.seam.forge.project.PackagingType;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.constraints.ConstraintInspector;
import org.jboss.seam.forge.project.facets.PackagingFacet;
import org.jboss.seam.forge.shell.command.CommandMetadata;
import org.jboss.seam.forge.shell.command.PluginMetadata;
import org.jboss.seam.forge.shell.plugins.Plugin;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class ConstraintEnforcer
{
   public void verifyAvailable(final Project currentProject, final CommandMetadata command) throws NoProjectException,
            UnsatisfiedPackagingTypeException, UnsatisfiedFacetDependencyException
   {

      Class<? extends Plugin> type = command.getPluginMetadata().getType();
      if (ConstraintInspector.requiresProject(type) && (currentProject == null))
      {
         throw new NoProjectException(
                  "Oops! That command needs an active project, but you don't seem to be working on one. " +
                           "Perhaps you should open a project or create a new one?");
      }
      else if (currentProject != null)
      {
         List<PackagingType> compatiblePackagingTypes = ConstraintInspector.getCompatiblePackagingTypes(type);
         PackagingType currentPackagingType = currentProject.getFacet(PackagingFacet.class).getPackagingType();
         if (!compatiblePackagingTypes.isEmpty()
                  && !compatiblePackagingTypes.contains(currentPackagingType))
         {
            throw new UnsatisfiedPackagingTypeException("Oops! The command [" + command.getName()
                     + "] requires one of the following packaging types " + compatiblePackagingTypes
                     + ", but the current packaging type is [" + currentPackagingType + "]");
         }

         List<Facet> currentFacets = currentProject.getFacets();
         List<Class<? extends Facet>> facetDependencies = ConstraintInspector.getFacetDependencies(type);
         if (!currentProject.hasAllFacets(facetDependencies))
         {
            facetDependencies.removeAll(currentFacets);
            throw new UnsatisfiedFacetDependencyException("Oops! The command [" + command.getName()
                     + "] depends on one or more Facet that is not installed " + facetDependencies + "");
         }
      }
   }

   public void verifyAvailable(final Project currentProject, final PluginMetadata plugin) throws NoProjectException,
            UnsatisfiedPackagingTypeException, UnsatisfiedFacetDependencyException
   {
      Class<? extends Plugin> type = plugin.getType();

      // check to make sure that if the plugin requires a project, we have one
      if (ConstraintInspector.requiresProject(type) && (currentProject == null))
      {
         throw new NoProjectException(
                  "Oops! The [" + plugin.getName()
                           + "] plugin needs an active project, but you don't seem to be working on one. " +
                           "Perhaps you should open a project or create a new one?");
      }
      else if (currentProject != null)
      {
         // check to make sure that the project packaging type is appropriate
         List<PackagingType> compatiblePackagingTypes = ConstraintInspector.getCompatiblePackagingTypes(type);
         PackagingType currentPackagingType = currentProject.getFacet(PackagingFacet.class).getPackagingType();
         if (!compatiblePackagingTypes.isEmpty()
                  && !compatiblePackagingTypes.contains(currentPackagingType))
         {
            throw new UnsatisfiedPackagingTypeException("Oops! The [" + plugin.getName()
                     + "] plugin requires one of the following packaging types " + compatiblePackagingTypes
                     + ", but the current packaging type is [" + currentPackagingType + "]");
         }

         // check to make sure all required facets are registered in the Project
         List<Facet> currentFacets = currentProject.getFacets();
         List<Class<? extends Facet>> facetDependencies = ConstraintInspector.getFacetDependencies(type);
         if (!currentProject.hasAllFacets(facetDependencies))
         {
            facetDependencies.removeAll(currentFacets);
            throw new UnsatisfiedFacetDependencyException("Oops! The [" + plugin.getName()
                     + "] plugin depends on one or more Facet that is not installed " + facetDependencies + "");
         }

         // check to make sure all required Facets are in an installed state
         for (Class<? extends Facet> facet : facetDependencies)
         {
            if (!currentProject.getFacet(facet).isInstalled())
            {
               throw new UnsatisfiedFacetDependencyException("Oops! The [" + plugin.getName()
                        + "] plugin depends on one or more Facet that is not registered but not correctly installed ["
                        + ConstraintInspector.getName(facet) + "]");
            }
         }
      }
   }

   public boolean isAvailable(final Project currentProject, final PluginMetadata plugin)
   {
      try
      {
         verifyAvailable(currentProject, plugin);
      }
      catch (ConstraintException e)
      {
         return false;
      }
      return true;
   }
}
