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

package org.jboss.seam.forge.shell.plugins.builtin;

import java.io.File;
import java.util.List;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.dependencies.DependencyBuilder;
import org.jboss.seam.forge.project.facets.DependencyFacet;
import org.jboss.seam.forge.project.facets.MetadataFacet;
import org.jboss.seam.forge.project.facets.PackagingFacet;
import org.jboss.seam.forge.resources.DirectoryResource;
import org.jboss.seam.forge.resources.FileResource;
import org.jboss.seam.forge.resources.Resource;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.ShellColor;
import org.jboss.seam.forge.shell.ShellMessages;
import org.jboss.seam.forge.shell.events.ReinitializeEnvironment;
import org.jboss.seam.forge.shell.plugins.Alias;
import org.jboss.seam.forge.shell.plugins.Command;
import org.jboss.seam.forge.shell.plugins.DefaultCommand;
import org.jboss.seam.forge.shell.plugins.Help;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.PipeOut;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.shell.plugins.Topic;
import org.jboss.seam.forge.shell.util.PluginRef;
import org.jboss.seam.forge.shell.util.PluginUtil;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Alias("forge")
@Topic("Shell Environment")
@Help("Forge control and shell environment commands.")
public class ForgePlugin implements Plugin
{
   private final Event<ReinitializeEnvironment> reinitializeEvent;
   private final Shell shell;

   @Inject
   public ForgePlugin(Event<ReinitializeEnvironment> reinitializeEvent, Shell shell)
   {
      this.reinitializeEvent = reinitializeEvent;
      this.shell = shell;
   }

   @DefaultCommand
   public void about(PipeOut out)
   {
      out.println("   ____                          _____                    ");
      out.println("  / ___|  ___  __ _ _ __ ___    |  ___|__  _ __ __ _  ___ ");
      out.println("  \\___ \\ / _ \\/ _` | '_ ` _ \\   | |_ / _ \\| '__/ _` |/ _ \\  "
               + out.renderColor(ShellColor.YELLOW, "\\\\"));
      out.println("   ___) |  __/ (_| | | | | | |  |  _| (_) | | | (_| |  __/  "
               + out.renderColor(ShellColor.YELLOW, "//"));
      out.println("  |____/ \\___|\\__,_|_| |_| |_|  |_|  \\___/|_|  \\__, |\\___| ");
      out.println("                                                |___/      ");
      out.println("");
      String version = getClass().getPackage().getImplementationVersion();
      out.println("Seam Forge, version [ " + version + " ] - JBoss, by Red Hat, Inc. [ http://jboss.org ]");
   }

   @Command(value = "ansi-test", help = "Display a list of all known ANSI color codes, in their corresponding color")
   public void run(PipeOut out)
   {
      for (ShellColor c : ShellColor.values())
      {
         out.println(c, c.name());
      }
   }

   @Command("info")
   public void info(PipeOut out)
   {
      about(out);
   }

   @Command("restart")
   public void restart(final PipeOut out) throws Exception
   {
      reinitializeEvent.fire(new ReinitializeEnvironment());
   }

   @Command("plugin-search")
   public void find(@Option(description = "search string") String searchString, final PipeOut out) throws Exception
   {
      String defaultRepo = (String) shell.getProperty("DEFFAULT_PLUGIN_REPO");

      if (defaultRepo == null)
      {
         out.println("no default repository set: (to set, type: set DEFFAULT_PLUGIN_REPO <repository>)");
         return;
      }

      List<PluginRef> pluginList = PluginUtil.findPlugin(defaultRepo, searchString, out);

      for (PluginRef ref : pluginList)
      {
         out.println(" - " + out.renderColor(ShellColor.BOLD, ref.getName()) + " (" + ref.getArtifact() + ")");
      }
   }

   @Command("plugin-install")
   public void install(@Option(description = "plugin-name") String pluginName,
            @Option(name = "keepSources", defaultValue = "false", shortName = "k") boolean keepSources,
            final PipeOut out) throws Exception
   {
      String defaultRepo = (String) shell.getProperty("DEFFAULT_PLUGIN_REPO");
      String pluginPath = shell.getProperty("FORGE_CONFIG_DIR") + "plugins/";

      if (defaultRepo == null)
      {
         throw new RuntimeException("no default repository set: (to set, type: set DEFAULT_PLUGIN_REPO <repository>)");
      }

      List<PluginRef> plugins = PluginUtil.findPlugin(defaultRepo, pluginName, out);

      if (plugins.isEmpty())
      {
         throw new RuntimeException("no plugin found with name [" + pluginName + "]");
      }
      else if (plugins.size() > 1)
      {
         throw new RuntimeException("ambiguous plugin query: multiple matches for [" + pluginName + "]");
      }
      else
      {
         PluginRef ref = plugins.get(0);
         ShellMessages.info(out, "Preparing to install plugin: " + ref.getName());

         if (!ref.isGit())
         {
            File file = PluginUtil.downloadPlugin(ref, out, pluginPath);
            if (file == null)
            {
               throw new RuntimeException("Could not install plugin [" + ref.getName() + "]");
            }
            else
            {
               PluginUtil.loadPluginJar(file);
            }

            ShellMessages.info(out, "Reinitializing and installing [" + ref.getName() + "]");
            reinitializeEvent.fire(new ReinitializeEnvironment());
            ShellMessages.success(out, "Installed successfully.");
         }
         else if (ref.isGit())
         {
            installGit(ref.getGitRepo(), ref.getGitTag(), ref.getGitBranch(), null, out);
         }
      }
   }

   @Command(value = "plugin-git", help = "Install a plugin from a git repository")
   public void installGit(
            @Option(description = "git repo") String gitRepo,
            @Option(name = "tag", description = "git tag to build") String tagName,
            @Option(name = "branch", description = "git branch to build") String branchName,
            @Option(name = "checkoutDir", description = "directory to check out sources into") Resource<?> checkoutDir,
            final PipeOut out) throws Exception
   {
      String pluginPath = shell.getProperty("FORGE_CONFIG_DIR") + "plugins/";

      DirectoryResource savedLocation = shell.getCurrentDirectory();
      DirectoryResource workspace = savedLocation.createTempResource();

      try
      {
         shell.setCurrentResource(workspace);

         DirectoryResource buildDir = workspace.getChildDirectory("repo");
         if (checkoutDir != null)
         {
            if (!checkoutDir.exists() && checkoutDir instanceof FileResource<?>)
            {
               ((FileResource<?>) checkoutDir).mkdirs();
            }
            buildDir = checkoutDir.reify(DirectoryResource.class);
         }

         if (buildDir.exists())
         {
            buildDir.delete(true);
            buildDir.mkdir();
         }

         String gitCommand = "git clone '" + gitRepo + "' " + buildDir.getFullyQualifiedName() + " '--depth' 1";
         ShellMessages.info(out, "Checking out plugin source files via 'git'...");
         out.println(gitCommand);

         if (tagName != null && branchName != null)
         {
            throw new IllegalStateException("Cannot specify both a branch and a tag reference.");
         }

         shell.execute(gitCommand);
         shell.setCurrentResource(buildDir);

         if (tagName != null)
         {
            shell.execute("git checkout -b " + tagName + "_branch origin/" + tagName);
         }
         else if (branchName != null)
         {
            shell.execute("git checkout -b " + branchName + "_branch origin/" + branchName);
         }

         if (!buildDir.getChild(".git").exists())
         {
            throw new RuntimeException("Failed to clone git repository to workspace ["
                           + workspace.getFullyQualifiedName() + "]");
         }

         Project project = shell.getCurrentProject();
         if (project == null)
         {
            throw new IllegalStateException("Unable to recognise plugin project in ["
                     + buildDir.getFullyQualifiedName() + "]");
         }

         DependencyFacet deps = project.getFacet(DependencyFacet.class);
         if (!deps.hasDependency(DependencyBuilder.create("org.jboss.seam.forge:forge-shell-api"))
                  && !shell.promptBoolean("The project does not appear to be a Forge Plugin Project, install anyway?",
                           false))
         {
            ShellMessages.info(out, "Aborted installation.");
            return;
         }

         PackagingFacet packaging = project.getFacet(PackagingFacet.class);
         ShellMessages.info(out, "Invoking build with underlying build system.");
         packaging.executeBuild();
         Resource<?> artifact = packaging.getFinalArtifact();
         if (artifact.exists())
         {
            if (artifact instanceof FileResource<?>)
            {
               MetadataFacet meta = project.getFacet(MetadataFacet.class);
               String jarFileName = pluginPath + meta.getGroupId() + "-" + meta.getProjectName() + ".jar";

               ShellMessages.info(out, "Copying build artifact to: " + jarFileName);
               PluginUtil.saveFile(jarFileName, artifact.getResourceInputStream());
               File jarFile = new File(jarFileName);

               PluginUtil.loadPluginJar(jarFile);
            }
         }
         else
         {
            throw new IllegalStateException("Build artifact [" + artifact.getFullyQualifiedName()
                           + "] is missing and cannot be installed. Please resolve build errors and try again.");
         }
      }
      finally
      {
         shell.setCurrentResource(savedLocation);
         if (checkoutDir != null)
         {
            ShellMessages.info(out,
                     "Cleaning up temp workspace [" + workspace.getFullyQualifiedName()
                              + "]");
            workspace.delete(true);
         }
      }

      reinitializeEvent.fire(new ReinitializeEnvironment());
      ShellMessages.success(out, "Installed successfully.");
   }
}
