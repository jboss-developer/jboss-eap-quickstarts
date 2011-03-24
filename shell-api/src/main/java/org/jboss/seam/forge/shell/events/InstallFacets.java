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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.seam.forge.parser.java.util.Assert;
import org.jboss.seam.forge.project.Facet;
import org.jboss.seam.forge.project.Project;

/**
 * Instruct Forge to install the given {@link Facet} into the current {@link Project}
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class InstallFacets
{
   private final List<Class<? extends Facet>> facetTypes;

   public InstallFacets(Class<? extends Facet> facetType)
   {
      Assert.notNull(facetType, "Facet type may not be null.");
      this.facetTypes = new ArrayList<Class<? extends Facet>>();
      facetTypes.add(facetType);
   }

   public InstallFacets(Class<? extends Facet>... facetTypes)
   {
      // FIXME This method causes warnings when used as intended... fix?
      Assert.notNull(facetTypes, "Facet types may not be null.");
      this.facetTypes = Arrays.asList(facetTypes);
   }

   public List<Class<? extends Facet>> getFacetTypes()
   {
      return facetTypes;
   }
}
