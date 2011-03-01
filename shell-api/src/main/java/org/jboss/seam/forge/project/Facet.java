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
package org.jboss.seam.forge.project;

import org.jboss.seam.forge.shell.plugins.RequiresFacet;
import org.jboss.seam.forge.shell.plugins.RequiresPackagingType;

/**
 * Represents a standardized piece of a project on which certain {@link Plugin} types may rely for a set of
 * domain-specific operations. A {@link Facet} is an access point to common functionality, file manipulations,
 * descriptors that extend a {@link Project} instance.
 * <p>
 * Facets may be annotated with any of the following constraints in order to ensure proper dependencies are satisfied at
 * runtime: {@link RequiresFacet}, {@link RequiresPackagingType}
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface Facet
{
   /**
    * Return the {@link Project} instance on which this {@link Facet} operates.
    */
   Project getProject();

   /**
    * Initialize this {@link Facet} for operation on the given {@link Project}. This method is responsible for ensuring
    * that the {@link Facet} instance is ready for use, and must be called before any other methods.
    */
   void setProject(Project project);

   /**
    * Perform necessary setup for this {@link Facet} to be considered installed in the given {@link Project}. If
    * installation is successful, the {@link Facet} should be registered in the {@link Project} by calling
    * {@link Project#registerFacet(Facet)}
    * 
    * @return true if installation was successful, false if not.
    */
   boolean install();

   /**
    * Return true if the {@link Facet} is available for the given {@link Project}, false if otherwise.
    */
   boolean isInstalled();

}
