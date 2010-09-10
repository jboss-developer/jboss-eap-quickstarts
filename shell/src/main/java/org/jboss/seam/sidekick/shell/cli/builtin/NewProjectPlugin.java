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
import org.jboss.seam.sidekick.parser.java.JavaParser;
import org.jboss.seam.sidekick.project.ProjectModelException;
import org.jboss.seam.sidekick.project.model.MavenProject;
import org.jboss.seam.sidekick.shell.CurrentProjectHolder;
import org.jboss.seam.sidekick.shell.PromptType;
import org.jboss.seam.sidekick.shell.Shell;
import org.jboss.seam.sidekick.shell.plugins.DefaultCommand;
import org.jboss.seam.sidekick.shell.plugins.Help;
import org.jboss.seam.sidekick.shell.plugins.Option;
import org.jboss.seam.sidekick.shell.plugins.Plugin;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Named("new-project")
@Help("Create a new project in an empty directory.")
public class NewProjectPlugin implements Plugin
{
   @Inject
   private Shell shell;

   @Inject
   private CurrentProjectHolder cp;

   @DefaultCommand
   public void create(@Option(description = "The name of the new project", required = true) final String name)
   {
      File cwd = shell.getCurrentDirectory();

      File dir = new File(cwd.getAbsolutePath() + "/" + name);
      if (containsProject(dir) || !shell.promptBoolean("Use [" + dir.getAbsolutePath() + "] as project directory?"))
      {
         if (containsProject(dir))
         {
            shell.println("***ERROR*** That directory already contains a project; please use a different folder.");
         }

         File newDir = cwd;
         do
         {
            shell.println();
            shell.print("What would you like to call the project folder? ");
            if (!containsProject(newDir))
            {
               shell.println();
               shell.print("[Press ENTER to use the current directory: " + cwd + "] ");
            }
            String folder = shell.prompt("");
            newDir = new File(cwd.getAbsolutePath() + "/" + folder);
            if (containsProject(newDir))
            {
               newDir = null;
            }
         }
         while (newDir == null);

         dir = newDir;
      }

      String groupId = shell.promptCommon("Please enter your base package [e.g: \"com.example.project\"] ", PromptType.JAVA_PACKAGE);

      if (!dir.exists())
      {
         dir.mkdirs();
      }

      MavenProject project = new MavenProject(dir, true);
      Model pom = project.getPOM();
      pom.setArtifactId(name);
      pom.setGroupId(groupId);
      pom.setPackaging("jar");

      project.setPOM(pom);

      for (File folder : project.getSourceFolders())
      {
         folder.mkdirs();
      }

      for (File folder : project.getResourceFolders())
      {
         folder.mkdirs();
      }

      project.createJavaFile(JavaParser.createClass()
            .setPackage(groupId)
            .setName("HelloWorld")
            .addMethod("public void String sayHello() {}")
            .setBody("System.out.println(\"Hi there! I was created as part of the project you call " + name + ".\");")
            .applyChanges());

      // project.createResource("<beans/>".toCharArray(), "META-INF/beans.xml");

      /*
       * Only change the environment after success!
       */
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
