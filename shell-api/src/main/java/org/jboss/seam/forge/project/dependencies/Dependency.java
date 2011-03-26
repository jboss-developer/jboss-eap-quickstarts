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

package org.jboss.seam.forge.project.dependencies;

import java.util.List;

import org.jboss.seam.forge.project.packaging.PackagingType;

/**
 * Represents a project library dependency.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface Dependency
{
   /**
    * Get the minor-identifier for this {@link Dependency}.
    */
   String getArtifactId();

   /**
    * Get the major identifier for this {@link Dependency}.
    */
   String getGroupId();

   /**
    * Get the version of this {@link Dependency}.
    */
   String getVersion();

   /**
    * Get the specified packaging type of this {@link Dependency}
    */
   String getPackagingType();

   /**
    * Get the {@link PackagingType} of this {@link Dependency}, if the type is not one of the default supported types,
    * {@link PackagingType#OTHER} will be returned;
    */
   PackagingType getPackagingTypeEnum();

   /**
    * Get the scope type of this {@link Dependency}
    */
   String getScopeType();

   /**
    * Get the {@link ScopeType} of this {@link Dependency}, if the type is not one of the default supported types,
    * {@link ScopeType#OTHER} will be returned;
    */
   ScopeType getScopeTypeEnum();

   /**
    * Get a list of {@link Dependency} objects to be excluded from this {@link Dependency}'s list of inclusions when it
    * is included in a project.
    */
   List<Dependency> getExcludedDependencies();

   /**
    * Return a string represenging this dependency in the form of a standard identifier. E.g:
    * "groupId : artifactId : version"
    */
   String toCoordinates();

}
