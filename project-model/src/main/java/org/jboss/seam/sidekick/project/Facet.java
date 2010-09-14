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
package org.jboss.seam.sidekick.project;

import java.util.Set;

/**
 * Represents a standardized piece of a project, on which certain plugins may rely for a set of domain-specific
 * operations.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface Facet
{
   /**
    * Return a Set of facet {@link Class} types on which this {@link Facet} depends. An instance of each of these facets
    * must exist in the project before this facet can be initialized or installed. If no dependencies are required, this
    * method should return null, or an empty set.
    */
   Set<Class<? extends Facet>> getDependencies();

   /**
    * Return the {@link Project} instance on which this {@link Facet} operates.
    */
   Project getProject();

   /**
    * Initialize this {@link Facet} for operation on the given {@link Project}. This method is responsible for ensuring
    * that the {@link Facet} instance is ready for use, and must be called before any other methods.
    * 
    * @return a builder pattern reference to this {@link Facet}
    */
   Facet init(Project project);

   /**
    * Perform necessary setup for this {@link Facet} to be considered installed in the given {@link Project}. If
    * installation is successful, the {@link Facet} should be registered in the {@link Project} by calling
    * {@link Project#registerFacet(Facet)}
    * 
    * @return a builder pattern reference to this {@link Facet}
    */
   Facet install();

   /**
    * Return true if the {@link Facet} is available for the given {@link Project}, false if otherwise.
    */
   boolean isInstalled();

}
