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
package org.jboss.seam.forge.project.dependencies;

import java.util.List;

/**
 * Represents meta-information for a given {@link Dependency} including its dependency chain, configured
 * {@link DependencyRepository} instances, and any managed dependency version information.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface DependencyMetadata
{
   /**
    * The {@link Dependency} for which this {@link DependencyMetadata} was retrieved.
    */
   Dependency getDependency();

   /**
    * The managed dependencies of the {@link Dependency} for which this {@link DependencyMetadata} was retrieved. These
    * dependencies are not included in downstream projects unless also specified as a direct dependency.
    * 
    * @see {@link #getDependencies()}
    */
   List<Dependency> getManagedDependencies();

   /**
    * The direct dependencies of the {@link Dependency} for which this {@link DependencyMetadata} was retrieved. These
    * dependencies are included in downstream projects unless explicitly excluded.
    * 
    * @see {@link Dependency#getExcludedDependencies()}
    */
   List<Dependency> getDependencies();

   /**
    * The {@link DependencyRepository} instances used when building the {@link Dependency}, or any projects which
    * inherit from it, for which this {@link DependencyMetadata} was retrieved.
    */
   List<DependencyRepository> getRepositories();
}
