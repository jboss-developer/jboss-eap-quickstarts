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

package org.jboss.seam.forge.shell.project;

import java.io.File;
import java.io.FileNotFoundException;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.services.ProjectFactory;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.events.InitProject;
import org.jboss.seam.forge.shell.plugins.events.PostStartup;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class ProjectInitializer
{
   private final Shell shell;
   private final ProjectContext cp;
   private final Event<InitProject> init;

   private final ProjectFactory projectFactory;

   @Inject
   public ProjectInitializer(final Shell shell, final ProjectContext currentProjectHolder,
                            final Event<InitProject> init, final ProjectFactory projectFactory)
   {
      this.shell = shell;
      this.cp = currentProjectHolder;
      this.init = init;
      this.projectFactory = projectFactory;
   }

   public void postStartupTrigger(@Observes final PostStartup event)
   {
      init.fire(new InitProject());
   }

   public void doInit(@Observes final InitProject event)
   {
      File currentDirectory = shell.getCurrentDirectory();
      Project currentProject;

      try
      {
         currentProject = projectFactory.findProjectRecursively(currentDirectory);
      }
      catch (FileNotFoundException e)
      {
         currentProject = null;
      }

      if (currentProject != null)
      {
         shell.printlnVerbose("Current project: " + currentProject.getProjectRoot().getAbsolutePath());
      }

      cp.setCurrentProject(currentProject);
      shell.setDefaultPrompt();
   }
}
