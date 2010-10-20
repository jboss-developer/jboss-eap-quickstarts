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

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Singleton
public class ProjectContext
{
   // TODO separate project-specific shell impl into separate project!
   private Project currentProject;

   @Inject
   private ResourceContext resourceContext;
   @Inject
   private ResourceFactory factory;
   @Inject
   private Event<InitProject> init;

   @Produces
   @Default
   @Dependent
   public Project getCurrentProject()
   {
      return currentProject;
   }

   public void setCurrentProject(final Project newProject)
   {
      if (((this.currentProject != null)
               && (newProject != null)
               && !this.currentProject.getProjectRoot().equals(newProject.getProjectRoot()))
               || ((this.currentProject == null) && (newProject != null)))
      {
         this.resourceContext.setCurrent(factory.getResourceFrom(newProject.getProjectRoot()));
      }

      this.currentProject = newProject;
   }

   public void setCurrentResource(final Resource<?> currentResource)
   {
      this.resourceContext.setCurrent(currentResource);
      if (((currentProject != null)
               && !ResourceUtil.isChildOf(
                        factory.getResourceFrom(currentProject.getProjectRoot()),
                        currentResource)) || (currentProject == null))
      {
         init.fire(new InitProject());
      }
   }

   public Resource<?> getCurrentResource()
   {
      return this.resourceContext.getCurrent();
   }
}
