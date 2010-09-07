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

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.maven.model.Model;
import org.jboss.seam.sidekick.project.ProjectModelException;
import org.jboss.seam.sidekick.project.model.MavenProject;
import org.jboss.seam.sidekick.shell.CurrentProjectHolder;
import org.jboss.seam.sidekick.shell.Shell;
import org.jboss.seam.sidekick.shell.plugins.plugins.DefaultCommand;
import org.jboss.seam.sidekick.shell.plugins.plugins.Help;
import org.jboss.seam.sidekick.shell.plugins.plugins.Option;
import org.jboss.seam.sidekick.shell.plugins.plugins.Plugin;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Named("create-project")
@Help("Create a new project in an empty directory.")
public class CreateProjectPlugin implements Plugin
{
   @Inject
   private Shell shell;

   @Inject
   private MavenProject project;

   @Inject
   private CurrentProjectHolder cp;

   @DefaultCommand
   public void create(@Option(description = "The name of the new project", required = true) final String name)
   {
      shell.println("Creating project: " + name);

      File cwd = shell.getCurrentDirectory();

      File dir = new File(cwd.getAbsolutePath() + "/" + name);
      if (!containsProject(dir) && shell.promptBoolean("Use [" + dir.getAbsolutePath() + "] as project directory? "))
      {
         dir.mkdirs();
      }
      else
      {
         if (containsProject(dir))
         {
            shell.println("***ERROR*** That directory already contains a project; please use a different folder.");
         }

         File newDir = cwd;
         do
         {
            shell.println();
            shell.println("What would you like to call the project folder? ");
            if (!containsProject(newDir))
            {
               shell.print("[Press ENTER to use the current directory: " + cwd + "] ");
            }
            String folder = shell.prompt("");
            newDir = new File(cwd.getAbsolutePath() + "/" + folder);
            if (containsProject(newDir))
            {
               newDir = null;
            }
            else if (!newDir.exists())
            {
               newDir.mkdirs();
            }
         }
         while (newDir == null);

         dir = newDir;
      }

      MavenProject project = new MavenProject(dir, true);
      Model pom = project.getPOM();
      pom.setArtifactId(name);
      project.setPOM(pom);

      cp.setCurrentProject(project);
      shell.setCurrentDirectory(dir);

      shell.println("***SUCCESS*** Created project [" + name + "] in new working directory [" + dir + "]");
   }

   private boolean containsProject(File newDir)
   {
      try
      {
         new MavenProject(newDir);
         return true;
      }
      catch (ProjectModelException e)
      {
         return false;
      }
   }
}
