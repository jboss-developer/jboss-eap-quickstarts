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
package org.jboss.seam.forge.shell.events;

import org.jboss.seam.forge.project.Project;

/**
 * An event that notifies observers when the current {@link Project} has changed.
 * <p>
 * <strong>For example:</strong>
 * <p>
 * <code>public void myObserver(@Observes {@link ProjectChanged} event)<br/>
 * {<br/>
 *    // do something<br/>
 * }<br/>
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public final class ProjectChanged
{
   private final Project oldProject;
   private final Project newProject;

   public ProjectChanged(final Project oldProject, final Project newProject)
   {
      this.oldProject = oldProject;
      this.newProject = newProject;
   }

   /**
    * @return the old {@link Project}
    */
   public Project getOldProject()
   {
      return oldProject;
   }

   /**
    * @return the new {@link Project}
    */
   public Project getNewProject()
   {
      return newProject;
   }
}
