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

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.services.ResourceFactory;
import org.jboss.seam.forge.project.util.ResourceUtil;
import org.jboss.seam.forge.shell.plugins.events.InitProject;
import org.jboss.seam.forge.shell.plugins.events.ProjectChange;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Singleton
public class CurrentProject
{
   // TODO separate project-specific shell impl into separate project!
   private Project currentProject;

   @Inject
   private CurrentResource currentResource;
   @Inject
   private ResourceFactory factory;
   @Inject
   private Event<InitProject> init;
   @Inject
   private Event<ProjectChange> projectChanged;

   @Produces
   @Default
   @Dependent
   public Project getCurrent()
   {
      return currentProject;
   }

   public void setCurrentProject(final Project project)
   {
      if (((this.currentProject != null)
               && (project != null)
               && !this.currentProject.getProjectRoot().equals(project.getProjectRoot()))
               || ((this.currentProject == null) && (project != null)))
      {
         this.currentResource.setCurrent(factory.getResourceFrom(project.getProjectRoot()));
      }

      ProjectChange event = new ProjectChange(currentProject, project);
      this.currentProject = project;
      projectChanged.fire(event);
   }

   public void setCurrentResource(final Resource<?> resource)
   {
      this.currentResource.setCurrent(resource);
      if (currentProject != null)
      {
         Resource<?> projectRoot = factory.getResourceFrom(currentProject.getProjectRoot());
         if (!projectRoot.equals(resource) && !ResourceUtil.isChildOf(projectRoot, resource))
         {
            init.fire(new InitProject());
         }
      }
      else
      {
         init.fire(new InitProject());
      }
   }

   public Resource<?> getCurrentResource()
   {
      return this.currentResource.getCurrent();
   }
}
