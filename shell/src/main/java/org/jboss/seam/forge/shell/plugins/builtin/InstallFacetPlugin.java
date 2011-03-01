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

package org.jboss.seam.forge.shell.plugins.builtin;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.seam.forge.project.Facet;
import org.jboss.seam.forge.project.PackagingType;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.constraints.ConstraintInspector;
import org.jboss.seam.forge.project.constraints.RequiresProject;
import org.jboss.seam.forge.project.facets.FacetNotFoundException;
import org.jboss.seam.forge.project.facets.PackagingFacet;
import org.jboss.seam.forge.project.services.FacetFactory;
import org.jboss.seam.forge.project.services.ProjectFactory;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.ShellMessages;
import org.jboss.seam.forge.shell.completer.AvailableFacetsCompleter;
import org.jboss.seam.forge.shell.events.InstallFacets;
import org.jboss.seam.forge.shell.plugins.Alias;
import org.jboss.seam.forge.shell.plugins.DefaultCommand;
import org.jboss.seam.forge.shell.plugins.Help;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.shell.plugins.Topic;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Alias("install")
@Help("Installs a facet into a project.")
@Topic("Project")
@RequiresProject
public class InstallFacetPlugin implements Plugin
{
   @Inject
   private FacetFactory factory;

   @Inject
   private ProjectFactory projectFactory;

   @Inject
   private Shell shell;

   @Inject
   private Project project;

   public void installRequest(@Observes InstallFacets request)
   {
      shell.printlnVerbose("Received Facet installation request " + request.getFacetTypes());
      for (Class<? extends Facet> type : request.getFacetTypes())
      {
         Facet facet = factory.getFacet(type);
         if (!project.hasFacet(type))
         {
            performInstallation(facet);
         }
         else
         {
            shell.printlnVerbose("Facet type already installed" + type);
         }
      }
   }

   @DefaultCommand
   public void install(@Option(required = true,
            completer = AvailableFacetsCompleter.class,
            description = "Name of the facet to install") final String facetName)
   {
      try
      {
         Facet facet = factory.getFacetByName(facetName);
         performInstallation(facet);
      }
      catch (FacetNotFoundException e)
      {
         shell.println("Could not find a facet with the name: " + facetName
                  + "; you can use the [" + ConstraintInspector.getName(ListFacetsPlugin.class)
                  + "] command to see if the facet is available.");
      }
   }

   private void performInstallation(Facet facet)
   {
      facet.setProject(project);

      /*
       * Verify Facet Dependencies
       */
      List<Class<? extends Facet>> deps = ConstraintInspector.getFacetDependencies(facet.getClass());
      String facetName = ConstraintInspector.getName(facet.getClass());
      if (!project.hasAllFacets(deps))
      {
         List<String> facetNames = new ArrayList<String>();
         for (Class<? extends Facet> f : deps)
         {
            facetNames.add(ConstraintInspector.getName(f));
         }

         if (shell.promptBoolean("The ["
                  + facetName
                  + "] facet depends on the following missing facets: "
                  + facetNames
                  + ". Would you like to attempt installation of these facets as well?"))
         {
            projectFactory.installSingleFacet(project, facet.getClass());
         }
         else
         {
            abort();
            return;
         }
      }

      if (!facet.isInstalled() || !project.hasFacet(facet.getClass()))
      {
         project.installFacet(facet);
      }

      if (!updatePackaging(facet))
      {
         abort();
         return;
      }

      if (facet.isInstalled())
      {
         ShellMessages.success(shell, "Installed [" + facetName + "] successfully.");
      }
      else
      {
         ShellMessages.error(shell, "Failed to install [" + facetName + "]; there may be a mess!");
      }
   }

   private boolean updatePackaging(final Facet facet)
   {

      List<PackagingType> types = ConstraintInspector.getCompatiblePackagingTypes(facet.getClass());
      String facetName = ConstraintInspector.getName(facet.getClass());

      /*
       * Verify Packaging Dependencies
       */
      PackagingType packaging = project.getFacet(PackagingFacet.class).getPackagingType();
      if (!types.isEmpty() && !types.contains(packaging))
      {
         if (types.size() == 1)
         {
            if (shell.promptBoolean("The ["
                     + facetName
                     + "] facet requires the following packaging type "
                     + types
                     + ", but is currently ["
                     + packaging
                     + "], would you like to change the packaging to " + types
                     + "? (Note: this could break other plugins in your project.)"))
            {
               project.getFacet(PackagingFacet.class).setPackagingType(types.get(0));
               shell.println("Packaging updated to " + types + "");
            }
            else
            {
               return false;
            }
         }
         else if (types.size() > 1)
         {
            if (shell.promptBoolean("The ["
                     + facetName
                     + "] plugin requires one of the following packaging types: "
                     + types
                     + ", but is currently ["
                     + packaging
                     + "], would you like to change the packaging? (Note: this could break other plugins in your project.)"))
            {
               PackagingType type = shell.promptChoiceTyped("Select a new packaging type:", types);
               project.getFacet(PackagingFacet.class).setPackagingType(type);
               shell.println("Packaging updated to [" + type + "]");
            }
            else
            {
               return false;
            }
         }

      }
      return true;
   }

   private void abort()
   {
      shell.println("Installation cancelled!");
   }
}
