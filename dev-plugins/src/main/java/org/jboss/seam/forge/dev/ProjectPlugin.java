/*
 * JBoss, by Red Hat.
 * Copyright 2011, Red Hat, Inc., and individual contributors
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

package org.jboss.seam.forge.dev;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.forge.dev.dependencies.DependencyPropertyCompleter;
import org.jboss.seam.forge.dev.dependencies.RepositoryCompleter;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.constraints.RequiresFacet;
import org.jboss.seam.forge.project.constraints.RequiresProject;
import org.jboss.seam.forge.project.dependencies.Dependency;
import org.jboss.seam.forge.project.dependencies.DependencyRepository;
import org.jboss.seam.forge.project.dependencies.ScopeType;
import org.jboss.seam.forge.project.facets.DependencyFacet;
import org.jboss.seam.forge.project.facets.MetadataFacet;
import org.jboss.seam.forge.shell.PromptType;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.Command;
import org.jboss.seam.forge.shell.plugins.DefaultCommand;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.PipeOut;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.shell.plugins.Topic;
import org.jboss.seam.forge.shell.util.ShellColor;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Named("project")
@Topic("Project")
@RequiresProject
@RequiresFacet(DependencyFacet.class)
public class ProjectPlugin implements Plugin
{
   private Project project;
   private Shell shell;

   public ProjectPlugin()
   {
   }

   @Inject
   public ProjectPlugin(final Project project, final Shell shell)
   {
      this.project = project;
      this.shell = shell;
   }

   @DefaultCommand
   public void info(final PipeOut out)
   {
      out.print(ShellColor.BOLD, "Project name: ");
      out.println(project.getFacet(MetadataFacet.class).getProjectName());
      out.print(ShellColor.BOLD, "Project dir:  ");
      out.println(project.getProjectRoot().getFullyQualifiedName());
   }

   /*
    * Dependency manipulation
    */
   @Command(value = "dependency-add", help = "Add a dependency to this project.")
   public void addDep(
            @Option(required = true,
                     type = PromptType.DEPENDENCY_ID,
                     description = "[ groupId :artifactId {:version :scope :packaging} ]",
                     help = "dependency identifier, ex: \"org.jboss.seam.forge:forge-api:1.0.0\"") final Dependency gav,
            final PipeOut out
            )
   {
      DependencyFacet deps = project.getFacet(DependencyFacet.class);
      if (!deps.hasDependency(gav))
      {
         deps.addDependency(gav);
         out.println("Added dependency [" + gav + "]");
      }
      else
      {
         Dependency dependency = deps.getDependency(gav);
         if (shell.promptBoolean("Dependency already exists [" + dependency + "], replace with [" + gav + "]?", false))
         {
            deps.removeDependency(dependency);
            deps.addDependency(gav);
         }
      }
   }

   @Command(value = "dependency-remove", help = "Remove a dependency from this project")
   public void removeDep(
            @Option(required = true,
                     type = PromptType.DEPENDENCY_ID,
                     description = "[ groupId :artifactId {:version :scope :packaging} ]",
                     help = "dependency identifier, ex: \"org.jboss.seam.forge:forge-api:1.0.0\"") final Dependency gav,
            final PipeOut out
            )
   {
      DependencyFacet deps = project.getFacet(DependencyFacet.class);
      if (deps.hasDependency(gav))
      {
         deps.removeDependency(gav);
         out.println("Removed dependency [" + gav + "]");
      }
      else
      {
         out.println("Dependency not found in project... ");
      }
   }

   @Command(value = "dependency-list", help = "List all dependencies this project includes")
   public void listDeps(final PipeOut out)
   {
      DependencyFacet deps = project.getFacet(DependencyFacet.class);

      for (Dependency dep : deps.getDependencies())
      {
         printDep(out, dep);
      }
   }

   /*
    * Property manipulation
    */
   @Command("property-set")
   public void addProp(
            @Option(required = true,
                     name = "name",
                     completer = DependencyPropertyCompleter.class) final String name,
            @Option(required = true,
                     name = "value") final String value,
            final PipeOut out)
   {
      DependencyFacet deps = project.getFacet(DependencyFacet.class);

      if (deps.getProperties().containsKey(name) &&
               shell.promptBoolean("Update property [" + name + "=" + deps.getProperty(name) + "] to new value ["
                        + value + "]", true))
      {
         deps.setProperty(name, value);
         out.println("Updated...");
      }
      else
      {
         deps.setProperty(name, value);
         out.println("Set property [" + name + "=" + value + "]");
      }
   }

   @Command("property-remove")
   public void removeProp(
            @Option(required = true, description = "propname",
                     completer = DependencyPropertyCompleter.class) final String name,
            final PipeOut out)
   {
      DependencyFacet deps = project.getFacet(DependencyFacet.class);
      if (deps.getProperties().containsKey(name))
      {
         String value = deps.removeProperty(name);
         out.println("Removed property [" + name + "=" + value + "]");
      }
      else
      {
         out.println("No such property [" + name + "]");
      }
   }

   @Command("property-list")
   public void listProps(final PipeOut out)
   {
      DependencyFacet deps = project.getFacet(DependencyFacet.class);
      Map<String, String> properties = deps.getProperties();

      for (Entry<String, String> entry : properties.entrySet())
      {
         out.print(entry.getKey() + "=");
         out.println(ShellColor.BLUE, entry.getValue());
      }
   }

   /*
    * Repositories
    */
   @Command("repository-add")
   public void repoAdd(
            @Option(required = true, description = "name...") final String name,
            @Option(required = true, description = "url...") final String url,
            final PipeOut out)
   {
      DependencyFacet deps = project.getFacet(DependencyFacet.class);
      if (deps.hasRepository(url))
      {
         out.println("Repository exists [" + name + "->" + url + "]");
      }
      else
      {
         deps.addRepository(name, url);
         out.println("Added repository [" + name + "->" + url + "]");
      }
   }

   @Command("repository-remove")
   public void repoRemove(
            @Option(required = true, description = "repo url...",
                     completer = RepositoryCompleter.class) final String url,
            final PipeOut out)
   {
      DependencyFacet deps = project.getFacet(DependencyFacet.class);
      if (deps.hasRepository(url))
      {
         DependencyRepository repo = deps.removeRepository(url);
         out.println("Removed repository [" + repo.getId() + "->" + repo.getUrl() + "]");
      }
      else
      {
         out.println("No repository with url [" + url + "]");
      }
   }

   @Command("repository-list")
   public void repoList(final PipeOut out)
   {
      DependencyFacet deps = project.getFacet(DependencyFacet.class);
      List<DependencyRepository> repos = deps.getRepositories();

      for (DependencyRepository repo : repos)
      {
         out.print(repo.getId() + "->");
         out.println(ShellColor.BLUE, repo.getUrl());
      }
   }

   /*
    * Utils
    */
   private void printDep(final PipeOut out, final Dependency dep)
   {
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
