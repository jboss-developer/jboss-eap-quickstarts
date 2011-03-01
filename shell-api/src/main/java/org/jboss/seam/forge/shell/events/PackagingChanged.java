/*
 * JBoss, Home of Professional Open Source
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
package org.jboss.seam.forge.shell.events;

import org.jboss.seam.forge.project.PackagingType;
import org.jboss.seam.forge.project.Project;

/**
 * This event is fired when the current {@link Project}'s {@link PackagingType} is changed.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public final class PackagingChanged
{
   private final PackagingType oldPackagingType;
   private final PackagingType newPackagingType;
   private final Project project;

   public PackagingChanged(final Project project, final PackagingType old, final PackagingType newType)
   {
      this.project = project;
      this.oldPackagingType = old;
      this.newPackagingType = newType;
   }

   public PackagingType getOldPackagingType()
   {
      return oldPackagingType;
   }

   public PackagingType getNewPackagingType()
   {
      return newPackagingType;
   }

   public Project getProject()
   {
      return project;
   }
}
