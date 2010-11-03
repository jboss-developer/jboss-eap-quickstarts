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

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.maven.model.Model;
import org.jboss.seam.forge.parser.JavaParser;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.facets.JavaSourceFacet;
import org.jboss.seam.forge.project.facets.MavenCoreFacet;
import org.jboss.seam.forge.project.facets.ResourceFacet;
import org.jboss.seam.forge.project.services.ProjectFactory;
import org.jboss.seam.forge.shell.PromptType;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.*;
import org.jboss.seam.forge.shell.project.ProjectContext;
import org.jboss.seam.forge.shell.util.Files;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Named("new-project")
@Topic("Project")
@Help("Create a new project in an empty directory.")
public class NewProjectPlugin implements Plugin
{
   @Inject
   private Shell shell;

   @Inject
   private ProjectContext cp;

   @Inject
   private ProjectFactory projectFactory;

   @SuppressWarnings("unchecked")
   @DefaultCommand
   public void create(
         @Option(name = "named",
               description = "The name of the new project",
               required = true) final String name,
         @Option(name = "topLevelPackage",
               description = "The top level package for your Java source files [e.g: \"com.example.project\"] ",
               required = true,
               type = PromptType.JAVA_PACKAGE) String groupId,
         @Option(name = "projectFolder",
               description = "The folder in which to create this project [e.g: \"~/Desktop/...\"] ",
               required = false) File projectFolder
         ) throws IOException
   {
      File cwd = shell.getCurrentDirectory();

      File dir = new File(cwd.getAbsolutePath() + File.separator + name);
      if (projectFactory.containsProject(dir) || !shell.promptBoolean("Use [" + dir.getAbsolutePath() + "] as project directory?"))
      {
         if (projectFactory.containsProject(dir))
         {
            shell.println("***ERROR*** [" + dir.getAbsolutePath() + "] already contains a project; please use a different folder.");
         }

         File defaultDir;

         if (shell.getCurrentResource() == null)
         {
            defaultDir = Files.getWorkingDirectory();
         }
         else if (shell.getCurrentResource().getUnderlyingResourceObject() instanceof File)
         {
            defaultDir = ((File) shell.getCurrentResource().getUnderlyingResourceObject());
            if (!defaultDir.isDirectory())
            {
               defaultDir = defaultDir.getParentFile();
            }
         }
         else
         {
            defaultDir = Files.getWorkingDirectory();
         }

         File newDir = cwd;
         do
         {
            shell.println();
            if (!projectFactory.containsProject(cwd))
            {
               newDir = shell.promptFile("Where would you like to create the project? [Press ENTER to use the current directory: " + cwd + "]", defaultDir);
            }
            else
            {
               newDir = shell.promptFile("Where would you like to create the project?");
            }
            if (projectFactory.containsProject(newDir))
            {
               newDir = null;
            }
         }
         while (newDir == null);

         dir = newDir.getCanonicalFile();
      }

      if (!dir.exists())
      {
         dir.mkdirs();
      }

      Project project = projectFactory.createProject(dir, MavenCoreFacet.class, JavaSourceFacet.class, ResourceFacet.class);
      Model pom = project.getFacet(MavenCoreFacet.class).getPOM();
      pom.setArtifactId(name);
      pom.setGroupId(groupId);
      pom.setPackaging("jar");

      project.getFacet(MavenCoreFacet.class).setPOM(pom);

      project.getFacet(JavaSourceFacet.class).saveJavaClass(JavaParser
            .createClass()
            .setPackage(groupId)
            .setName("HelloWorld")
            .addMethod("public void String sayHello() {}")
            .setBody("System.out.println(\"Hi there! I was created as part of the project you call " + name
                  + ".\");")
            .getOrigin());

      project.getFacet(ResourceFacet.class).createResource("<forge/>".toCharArray(), "META-INF/forge.xml");

      /*
       * Only change the environment after success!
       */
      cp.setCurrentProject(project);

      shell.println("***SUCCESS*** Created project [" + name + "] in new working directory [" + dir + "]");
   }
}
