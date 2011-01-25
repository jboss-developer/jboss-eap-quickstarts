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

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.constraints.RequiresFacet;
import org.jboss.seam.forge.project.constraints.RequiresProject;
import org.jboss.seam.forge.project.dependencies.DependencyBuilder;
import org.jboss.seam.forge.project.facets.MavenCoreFacet;
import org.jboss.seam.forge.project.resources.FileResource;
import org.jboss.seam.forge.project.resources.builtin.DirectoryResource;
import org.jboss.seam.forge.project.services.ProjectFactory;
import org.jboss.seam.forge.project.services.ResourceFactory;
import org.jboss.seam.forge.project.util.PathspecParser;
import org.jboss.seam.forge.shell.PromptType;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.Command;
import org.jboss.seam.forge.shell.plugins.DefaultCommand;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.PipeOut;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.shell.plugins.ResourceScope;
import org.jboss.seam.forge.shell.plugins.Topic;
import org.jboss.seam.forge.shell.util.GeneralUtils;
import org.jboss.seam.forge.shell.util.ShellColor;

/**
 * TODO this should probably be pulled into a maven plugins module
 * 
 * @author Mike Brock .
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Named("mvn")
@Topic("Project")
@RequiresProject
@RequiresFacet(MavenCoreFacet.class)
@ResourceScope(DirectoryResource.class)
public class MvnShellPlugin implements Plugin
{
   private final Shell shell;
   private final Project project;
   private final ProjectFactory factory;
   private final ResourceFactory resources;

   @Inject
   public MvnShellPlugin(final Shell shell, final Project project, final ProjectFactory factory,
            final ResourceFactory resources)
   {
      this.shell = shell;
      this.project = project;
      this.factory = factory;
      this.resources = resources;
   }

   @DefaultCommand
   public void run(final PipeOut out, final String... parms) throws IOException
   {
      GeneralUtils.nativeCommandCallFromPath("mvn", parms, out, project.getProjectRoot());
   }

   @Command("set-parent")
   public void setParent(
            @Option(name = "parentId",
                     help = "dependency identifier of parent, ex: \"org.jboss.seam.forge:forge-parent:1.0.0\"",
                     required = false) final String gav,
            @Option(name = "parentRelativePath",
                     help = "relative location from the current project to the parent project root folder",
                     type = PromptType.FILE_PATH,
                     required = false) final String relativePath,
            @Option(name = "parentProjectRoot",
                     help = "absolute location of a project to use as this project's direct parent",
                     required = false) final Resource<?> path,
            final PipeOut out) throws Exception
   {
      MavenCoreFacet mvn = project.getFacet(MavenCoreFacet.class);
      Parent parent = null;
      if (gav != null)
      {
         DependencyBuilder dep = DependencyBuilder.create(gav);
         parent = new Parent();
         parent.setArtifactId(dep.getArtifactId());
         parent.setGroupId(dep.getGroupId());
         parent.setVersion(dep.getVersion());

         if (relativePath != null)
         {
            parent.setRelativePath(relativePath);
         }

         Model pom = mvn.getPOM();
         pom.setParent(parent);
         mvn.setPOM(pom);
      }
      else if ((path != null) && factory.containsProject((FileResource<?>) path))
      {
         Project parentProject = factory.findProject((FileResource<?>) path);
         MavenCoreFacet parentCore = parentProject.getFacet(MavenCoreFacet.class);

         parent = new Parent();
         parent.setArtifactId(parentCore.getMavenProject().getArtifactId());
         parent.setGroupId(parentCore.getMavenProject().getGroupId());
         parent.setVersion(parentCore.getMavenProject().getVersion());

         if (relativePath != null)
         {
            parent.setRelativePath(relativePath);
         }

         Model pom = mvn.getPOM();
         pom.setParent(parent);
         mvn.setPOM(pom);
      }
      else if (relativePath != null)
      {
         PathspecParser parser = new PathspecParser(resources, shell.getCurrentProject().getProjectRoot(), relativePath);
         List<Resource<?>> resolvedResources = parser.resolve();
         if (!resolvedResources.isEmpty() && factory.containsProject((FileResource<?>) resolvedResources.get(0)))
         {
            Project parentProject = factory.findProject((FileResource<?>) resolvedResources.get(0));
            MavenCoreFacet parentCore = parentProject.getFacet(MavenCoreFacet.class);

            parent = new Parent();
            parent.setArtifactId(parentCore.getMavenProject().getArtifactId());
            parent.setGroupId(parentCore.getMavenProject().getGroupId());
            parent.setVersion(parentCore.getMavenProject().getVersion());
            parent.setRelativePath(relativePath);

            Model pom = mvn.getPOM();
            pom.setParent(parent);
            mvn.setPOM(pom);
         }
         else
         {
            out.print(ShellColor.RED, "***ERROR***");
            out.println(" relative path did not resolve to a Project [" + relativePath + "]");
         }
      }
      else
      {
         out.print(ShellColor.RED, "***ERROR***");
         out.println(" you must specify a path to or dependency id of the parent project.");
      }

      if (parent != null)
      {
         String parentId = parent.getGroupId() + ":" + parent.getArtifactId() + ":"
                  + parent.getVersion() + " ("
                  + (parent.getRelativePath() == null ? " " : parent.getRelativePath() + ")");

         out.println("Set parent [ " + parentId + " ]");
      }
   }

   @Command("remove-parent")
   public void removeParent(final PipeOut out)
   {
      MavenCoreFacet mvn = project.getFacet(MavenCoreFacet.class);

      Model pom = mvn.getPOM();
      Parent parent = pom.getParent();

      if (parent != null)
      {
         String parentId = parent.getGroupId() + ":" + parent.getArtifactId() + ":"
                  + parent.getVersion() + " ("
                  + (parent.getRelativePath() == null ? " " : parent.getRelativePath() + ")");

         if (shell.promptBoolean("Are you sure you want to remove all parent information from this project? [ "
                  + parentId + "]", false))
         {
            out.println("Removed parent [ " + parentId + " ]");
            pom.setParent(null);
            mvn.setPOM(pom);
         }
         else
         {
            out.println("Aborted...");
         }
      }
      else
      {
         out.println("Nothing to remove...");
      }
   }
}
