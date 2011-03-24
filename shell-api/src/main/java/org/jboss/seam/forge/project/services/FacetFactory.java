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
package org.jboss.seam.forge.project.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.seam.forge.project.Facet;
import org.jboss.seam.forge.project.facets.FacetNotFoundException;
import org.jboss.seam.forge.shell.util.ConstraintInspector;

/**
 * Responsible for instantiating {@link Facet}s through CDI.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Dependent
public class FacetFactory
{
   private List<Facet> facets;

   private final Instance<? extends Facet> instances;

   @Inject
   public FacetFactory(@Any final Instance<Facet> instances)
   {
      this.instances = instances;
   }

   public List<Facet> getFacets()
   {
      if (facets == null)
      {
         Iterator<? extends Facet> iterator = instances.iterator();
         List<Facet> result = new ArrayList<Facet>();
         while (iterator.hasNext())
         {
            Facet element = iterator.next();
            result.add(element);
         }
         facets = result;
      }
      return facets;
   }

   @SuppressWarnings("unchecked")
   public <T extends Facet> T getFacet(final Class<T> type) throws FacetNotFoundException
   {
      T result = null;

      for (Facet facet : getFacets())
      {
         if (type.isAssignableFrom(facet.getClass()))
         {
            result = (T) facet;
            break;
         }
      }

      if (result == null)
      {
         throw new FacetNotFoundException("The requested Facet of type [" + type.getName() + "] could not be loaded.");
      }

      return result;
   }

   public Facet getFacetByName(final String facetName) throws FacetNotFoundException
   {
      Facet result = null;
      for (Facet facet : getFacets())
      {
         String name = ConstraintInspector.getName(facet.getClass());
         if (name.equals(facetName))
         {
            result = facet;
            break;
         }
      }

      if (result == null)
      {
         throw new FacetNotFoundException("The requested Facet named [" + facetName + "] could not be found.");
      }

      return result;
   }
}
