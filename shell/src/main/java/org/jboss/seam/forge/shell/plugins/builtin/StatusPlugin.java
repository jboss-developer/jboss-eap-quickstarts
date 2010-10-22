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
package org.jboss.seam.forge.shell.plugins.builtin;

import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.forge.project.Facet;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.services.FacetFactory;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.DefaultCommand;
import org.jboss.seam.forge.shell.plugins.Help;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.Plugin;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Named("status")
@Help("Check the current project configuration")
public class StatusPlugin implements Plugin
{
   @Inject
   private FacetFactory factory;

   @Inject
   private Shell shell;

   @Inject
   private Project project;

   @DefaultCommand
   public void status(
            @Option(help = "The name of the facet.",
                     name = "facet",
                     required = false) final String facetName)
   {
      if ((facetName == null) || facetName.isEmpty())
      {
         shell.println("Currently operating on the project located in: " + project.getProjectRoot());
      }
      else
      {
         Facet facet = factory.getFacetByName(facetName);
         if (facet != null)
         {

            try
            {
               facet = project.getFacet(facet.getClass());
               if (facet.isInstalled())
               {
                  shell.println("Status: INSTALLED");
               }
               else
               {
                  throw new Exception();
               }
            }
            catch (Exception e)
            {
               shell.println("Status: NOT-INSTALLED (you may run \"install " + facetName
                           + "\" to install this facet.");
            }
         }
         else
         {
            shell.println("Could not find a facet with the name: " + facetName
                     + "; are you sure that's the correct name?");
         }
      }
   }
}
