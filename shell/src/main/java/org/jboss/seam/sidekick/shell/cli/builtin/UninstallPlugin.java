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
package org.jboss.seam.sidekick.shell.cli.builtin;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.maven.model.Dependency;
import org.jboss.seam.sidekick.project.Project;
import org.jboss.seam.sidekick.project.facets.MavenFacet;
import org.jboss.seam.sidekick.shell.Shell;
import org.jboss.seam.sidekick.shell.cli.PluginMetadata;
import org.jboss.seam.sidekick.shell.cli.PluginRegistry;
import org.jboss.seam.sidekick.shell.plugins.DefaultCommand;
import org.jboss.seam.sidekick.shell.plugins.Help;
import org.jboss.seam.sidekick.shell.plugins.MavenPlugin;
import org.jboss.seam.sidekick.shell.plugins.Option;
import org.jboss.seam.sidekick.shell.plugins.Plugin;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Named("remove")
@Help("Remove a plugin from the current project.")
public class UninstallPlugin implements Plugin
{
   @Inject
   private PluginRegistry registry;

   @Inject
   private Shell shell;

   @Inject
   private Project project;

   @DefaultCommand
   public void remove(@Option(help = "The name of the plugin to remove from the project.") final String pluginName)
   {
      PluginMetadata meta = registry.getPlugins().get(pluginName);
      if (meta != null)
      {
         Plugin plugin = registry.instanceOf(meta);
         if (plugin instanceof MavenPlugin)
         {
            MavenPlugin removable = (MavenPlugin) plugin;
            if (isInstalledInProject(removable))
            {
               shell.println("Removing plugin [" + pluginName + "] from project: "
                        + project.getFacet(MavenFacet.class).getPOM().getArtifactId());
               remove(removable);
            }
            else
            {
               shell.println("The plugin [" + pluginName
                        + "] cannot be removed from your project because it is not installed.");
            }
         }
         else
         {
            shell.println("The plugin [" + pluginName
                     + "] is not installed and cannot be removed; it is not an installable plugin.");

         }
      }
      else
      {
         shell.println("Could not find a plugin with the name: " + pluginName
                  + "; are you sure that's the correct name?");
      }
   }

   private void remove(final MavenPlugin plugin)
   {
      // TODO this needs to be smarter, and not remove dependencies that are depended on by other plugins.
      for (Dependency d : plugin.getDependencies())
      {
         project.getFacet(MavenFacet.class).removeDependency(d);
      }
   }

   private boolean isInstalledInProject(final MavenPlugin installable)
   {
      for (Dependency d : installable.getDependencies())
      {
         if (!project.getFacet(MavenFacet.class).hasDependency(d))
         {
            return false;
         }
      }
      return true;
   }

}
