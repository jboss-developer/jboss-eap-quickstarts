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
package org.jboss.seam.sidekick.shell.plugins;

import java.util.List;

import org.jboss.seam.sidekick.project.PackagingType;
import org.jboss.seam.sidekick.project.Project;

/**
 * A plugin that can be installed and removed from a working project.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface InstallablePlugin extends Plugin
{
   /**
    * Get a list of the {@link PackagingType}s this {@link Plugin} is compatible with; at least one of these types must
    * be used in order for this {@link Plugin} to function. Returning an empty list signals that this plugin is
    * compatible with all {@link PackagingType} options.
    */
   public List<PackagingType> getCompatiblePackagingTypes();

   /**
    * Ask this plugin to determine whether or not it has been installed in the given {@link Project}. Return true if the
    * plugin is already installed, return false if not.
    */
   boolean isInstalled(Project project);

   /**
    * Ask this plugin to perform necessary operations for installation in the given {@link Project}.
    */
   void install(Project project);

   /**
    * Ask this plugin to perform necessary operations to safely remove itself from the given {@link Project}
    */
   void remove(Project project);
}
