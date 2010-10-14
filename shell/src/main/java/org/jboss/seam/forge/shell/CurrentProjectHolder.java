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

package org.jboss.seam.forge.shell;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.services.ResourceFactory;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Singleton
public class CurrentProjectHolder
{
   private Project currentProject;
   private Resource currentResource;
   private ResourceFactory factory;

   @Produces
   @Default
   @Dependent
   public Project getCurrentProject()
   {
      return currentProject;
   }

   public void setCurrentProject(final Project currentProject)
   {
      if (factory == null)
      {
         throw new RuntimeException("ResourceFactory not supplied");
      }

      if (currentProject != null)
      {
         this.currentResource = factory.getResourceFrom(currentProject.getProjectRoot());
      }

      this.currentProject = currentProject;
   }

   public ResourceFactory getResourceFactory()
   {
      return factory;
   }

   public void setResourceFactory(ResourceFactory factory)
   {
      this.factory = factory;
   }
}
