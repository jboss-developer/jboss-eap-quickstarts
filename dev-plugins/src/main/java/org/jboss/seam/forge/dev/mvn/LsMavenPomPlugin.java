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

package org.jboss.seam.forge.dev.mvn;

import java.io.FileNotFoundException;
import java.util.List;

import javax.inject.Inject;

import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.dependencies.Dependency;
import org.jboss.seam.forge.project.dependencies.ScopeType;
import org.jboss.seam.forge.project.resources.builtin.maven.MavenDependencyResource;
import org.jboss.seam.forge.project.resources.builtin.maven.MavenPomResource;
import org.jboss.seam.forge.project.resources.builtin.maven.MavenProfileResource;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.DefaultCommand;
import org.jboss.seam.forge.shell.plugins.Help;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.OverloadedName;
import org.jboss.seam.forge.shell.plugins.PipeOut;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.shell.plugins.ResourceScope;
import org.jboss.seam.forge.shell.plugins.Topic;
import org.jboss.seam.forge.shell.util.ShellColor;

/**
 * LsMavenPomPlugin
 * 
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
@OverloadedName("ls")
@ResourceScope(MavenPomResource.class)
@Topic("File & Resources")
@Help("Prints the contents current pom file")
public class LsMavenPomPlugin implements Plugin
{
   @Inject
   private Shell shell;

   @DefaultCommand
   public void run(
            @Option(flagOnly = true, name = "all", shortName = "a", required = false) final boolean showAll,
                   @Option(flagOnly = true, name = "list", shortName = "totalLines", required = false) final boolean list,
                   @Option(description = "path", defaultValue = ".") final Resource<?>[] paths,
                   final PipeOut out)
   {

      // TODO this should not use the shell - rather the paths parameter
      Resource<?> currentResource = shell.getCurrentResource();

      if (currentResource instanceof MavenPomResource)
      {
         MavenPomResource pom = (MavenPomResource) currentResource;

         out.println();
         out.println(out.renderColor(ShellColor.RED, "[dependencies] "));
         List<Resource<?>> children = pom.listResources();
         for (Resource<?> child : children)
         {
            if (child instanceof MavenDependencyResource)
            {
               MavenDependencyResource resource = (MavenDependencyResource) child;
               Dependency dep = resource.getDependency();
               out.println(
                        out.renderColor(ShellColor.BLUE, dep.getGroupId())
                                 +
                                 out.renderColor(ShellColor.BOLD, " : ")
                                 +
                                 out.renderColor(ShellColor.BLUE, dep.getArtifactId())
                                 +
                                 out.renderColor(ShellColor.BOLD, " : ")
                                 +
                                 out.renderColor(ShellColor.NONE, dep.getVersion() == null ? "" : dep.getVersion())
                                 +
                                 out.renderColor(ShellColor.BOLD, " : ")
                                 +
                                 out.renderColor(ShellColor.NONE, dep.getPackagingType() == null ? "" : dep
                                          .getPackagingType().toLowerCase())
                                 +
                                 out.renderColor(ShellColor.BOLD, " : ")
                                 +
                                 out.renderColor(determineDependencyShellColor(dep.getScopeTypeEnum()),
                                          dep.getScopeType() == null ? "compile" : dep.getScopeType()
                                                   .toLowerCase()));
            }
         }

         out.println();
         out.println(out.renderColor(ShellColor.RED, "[profiles] "));

         for (Resource<?> child : children)
         {
            if (child instanceof MavenProfileResource)
            {
               MavenProfileResource resource = (MavenProfileResource) child;
               out.println(out.renderColor(ShellColor.BLUE, resource.getName()));
            }
         }
      }
   }

   private ShellColor determineDependencyShellColor(final ScopeType type)
   {
      if (type == null)
      {
         return ShellColor.YELLOW;
      }
      switch (type)
      {
      case PROVIDED:
         return ShellColor.GREEN;
      case COMPILE:
         return ShellColor.YELLOW;
      case RUNTIME:
         return ShellColor.MAGENTA;
      case OTHER:
         return ShellColor.BLACK;
      case SYSTEM:
         return ShellColor.BLACK;
      case TEST:
         return ShellColor.BLUE;
      }
      return ShellColor.NONE;
   }
}