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
package org.jboss.seam.forge.project.facets;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuildingResult;
import org.jboss.seam.forge.project.Facet;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.util.DependencyBuilder;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface MavenCoreFacet extends Facet
{
   /**
    * Get the current Maven POM file.
    */
   public Model getPOM();

   /**
    * Set the current Maven POM file (overwriting any existing POM)
    */
   public void setPOM(Model pom);

   /**
    * Ask Maven to process this project's POM and return the resulting metadata.
    * <p>
    * <b>**Warning!**</b> Calling this method has serious performance
    * implications! Avoid whenever possible!
    */
   public ProjectBuildingResult getProjectBuildingResult();

   /**
    * Return true if this {@link Project} contains a dependency matching the
    * given {@link Dependency}; return false otherwise. See also:
    * {@link DependencyBuilder}.
    */
   public boolean hasDependency(Dependency dependency);

   /**
    * Add the given {@link Dependency} to this facet's {@link Project}. See
    * also: {@link DependencyBuilder}.
    */
   public void addDependency(Dependency dependency);

   /**
    * Remove the given {@link Dependency} from this facet's {@link Project}. See
    * also: {@link DependencyBuilder}.
    */
   public void removeDependency(Dependency dependency);

   /**
    * Return the fully-resolved POM/{@link MavenProject} for this Maven enabled
    * {@link Project}
    */
   public MavenProject getMavenProject();

}
