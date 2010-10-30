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

import java.io.File;
import java.util.List;

import org.jboss.seam.forge.project.facets.FacetNotFoundException;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface Project
{
   /**
    * Return true if this project has a facet of the given type; return false
    * otherwise.
    */
   public boolean hasFacet(Class<? extends Facet> type);

   /**
    * Return the instance of the requested {@link Facet} type, or throw a
    * {@link FacetNotFoundException} if no {@link Facet} of that type is
    * registered.
    */
   public <F extends Facet> F getFacet(Class<F> type) throws FacetNotFoundException;

   /**
    * Return a list of the currently installed {@link Facet}s. Return an empty
    * list if no facets of that type were found.
    */
   public List<Facet> getFacets();

   /**
    * Return a list of the currently installed {@link Facet}s matching the given
    * type.
    */
   public <F extends Facet> List<F> getFacets(Class<F> type);

   /**
    * Install and register the given {@link Facet}. If the facet is already
    * installed, register it instead (See {@link #registerFacet(Facet)}.
    */
   public Project installFacet(Facet facet);

   /**
    * Add the given {@link Facet} to this {@link Project}'s internal collection
    * of installed facets.
    */
   public Project registerFacet(Facet facet);

   /**
    * Get the {@link File} representing the root directory of this
    * {@link Project}
    */
   public File getProjectRoot();

   /**
    * Delete the given {@link File}
    */
   public boolean delete(File file);

   /**
    * Create the requested {@link File} if it does not exist and write the given
    * data to it.
    */
   public void writeFile(char[] data, File file);

   /**
    * Create the requested {@link File} if it does not exist and write the given
    * data to it.
    */
   public void writeFile(String string, File file);

   /**
    * Return true if this project's file-system has been created and
    * initialized; otherwise, return false.
    */
   public boolean exists();

   /**
    * Create all necessary parent directories for the given {@link File}.
    */
   public void mkdirs(File file);
}
