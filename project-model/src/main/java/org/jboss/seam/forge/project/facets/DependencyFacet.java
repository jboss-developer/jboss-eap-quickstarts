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
package org.jboss.seam.forge.project.facets;

import java.util.List;
import java.util.Map;

import org.jboss.seam.forge.project.Facet;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.dependencies.Dependency;
import org.jboss.seam.forge.project.dependencies.DependencyBuilder;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface DependencyFacet extends Facet
{
   /**
    * Return true if this {@link Project} contains a dependency matching the given {@link Dependency} at any level of
    * the project hierarchy; return false otherwise. This method ignores {@link Dependency#getScopeType()}
    * <p>
    * See also: {@link DependencyBuilder}.
    * <p>
    * <b>Notice:</b> This method checks the entire project dependency structure, meaning that if a dependency is
    * declared somewhere else in the hierarchy, it will not be detected by {@link #hasDirectDependency(Dependency)} and
    * will not be removable via {@link #removeDependency(Dependency)}.
    */
   public boolean hasDependency(Dependency dependency);

   /**
    * Return true if this {@link Project} contains a dependency matching the given {@link Dependency}; return false
    * otherwise. This method ignores {@link Dependency#getScopeType()}
    * <p>
    * See also: {@link DependencyBuilder}.
    * <p>
    * <b>Notice:</b> This method checks only the immediate project dependencies, meaning that if a dependency is
    * declared somewhere else in the hierarchy, it will not be detected by this method, even though by
    * {@link #hasDependency(Dependency)} may return true.
    */
   public boolean hasDirectDependency(Dependency dependency);

   /**
    * Add the given {@link Dependency} to this {@link Project}'s immediate list of dependencies. This method first calls
    * {@link #hasDependency(Dependency)} before making changes to the dependency list.
    * <p>
    * See also: {@link DependencyBuilder}.
    */
   public void addDependency(Dependency dependency);

   /**
    * Remove the given {@link Dependency} from this facet's {@link Project}. This method ignores
    * {@link Dependency#getScopeType()}
    * <p>
    * See also: {@link DependencyBuilder}.
    * <p>
    * <b>Notice:</b> This method operates only the immediate project dependencies, meaning that if a dependency is
    * declared somewhere else in the hierarchy, it will not be removable by this method. You should call
    * {@link #hasDirectDependency(Dependency)} first in order to check if the dependency exists in this projects
    * immediate dependencies.
    */
   public void removeDependency(Dependency dependency);

   /**
    * Return an immutable list of all direct {@link Dependencies} contained within this project. (i.e.: all dependencies
    * for which {@link DependencyFacet#hasDirectDependency(Dependency)} returns true;
    */
   public List<Dependency> getDependencies();

   /**
    * Return a list of all build dependency properties.(Build properties such, as ${my.version}, can be used anywhere in
    * a dependency, and will be expanded during building to their property value.)
    */
   public Map<String, String> getProperties();

   /**
    * Set a build dependency property. (Build properties, such as ${my.version}, can be used anywhere in a dependency,
    * and will be expanded during building to their property value.)
    */
   public void setProperty(String name, String value);

   /**
    * Get a build property by name. (Build properties, such as ${my.version}, can be used anywhere in a dependency, and
    * will be expanded during building to their property value.)
    */
   public String getProperty(String name);

   /**
    * Remove a build property by name. (Build properties, such as ${my.version}, can be used anywhere in a dependency,
    * and will be expanded during building to their property value.)
    */
   public String removeProperty(String name);
}
