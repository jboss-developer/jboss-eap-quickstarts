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

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.jboss.seam.sidekick.project.PackagingType;
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
@Named("install")
@Help("Installs a plugin into a project.")
public class InstallPlugin implements Plugin
{
   @Inject
   private PluginRegistry registry;

   @Inject
   private Shell shell;

   @Inject
   private Project project;

   @DefaultCommand
   public void install(@Option(required = true,
            description = "The plugin to install") final String pluginName)
   {
      PluginMetadata meta = registry.getPlugins().get(pluginName);
      if (meta != null)
      {
         Plugin plugin = registry.instanceOf(meta);
         if (plugin instanceof MavenPlugin)
         {
            MavenPlugin installable = (MavenPlugin) plugin;
            if (!isInstalledInProject(installable))
            {
               List<PackagingType> types = installable.getCompatiblePackagingTypes();
               MavenFacet mavenFacet = project.getFacet(MavenFacet.class);
               if (!types.contains(new PackagingType(mavenFacet.getPOM().getPackaging())))
               {
                  String packaging = mavenFacet.getPOM().getPackaging();
                  if (shell.promptBoolean("The ["
                           + meta.getName()
                           + "] plugin requires one of the following packaging types: "
                           + types
                           + ", but is currently ["
                           + packaging
                           + "], would you like to change the packaging? (Note: this could break other plugins in your project.)"))
                  {
                     PackagingType type = shell.promptChoice("Select a new packaging type:", types);
                     Model pom = mavenFacet.getPOM();
                     pom.setPackaging(type.getType());
                     mavenFacet.setPOM(pom);
                     shell.println("Packaging updated to [" + type + "]");
                  }
                  else
                  {
                     abort();
                     return;
                  }

               }

               for (Dependency dep : installable.getDependencies())
               {
                  mavenFacet.addDependency(dep);
               }
            }
            shell.println("Installation completed successfully.");
         }
         else
         {
            shell.println("The plugin [" + pluginName
                     + "] cannot be installed into your project because it is not an installable plugin.");
         }
      }
      else
      {
         shell.println("Could not find a plugin with the name: " + pluginName
                  + "; are you sure that's the correct name?");
      }
   }

   private boolean isInstalledInProject(final MavenPlugin installable)
   {
      for (Dependency d : installable.getDependencies())
      {
         MavenFacet mavenFacet = project.getFacet(MavenFacet.class);
         if (!mavenFacet.hasDependency(d))
         {
            return false;
         }
      }
      return true;
   }

   private void abort()
   {
      shell.println("Installation cancelled!");
   }
}
