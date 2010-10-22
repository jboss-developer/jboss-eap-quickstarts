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
package org.jboss.seam.forge.project.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessBean;
import javax.inject.Singleton;

import org.jboss.seam.forge.project.Facet;
import org.jboss.seam.forge.project.constraints.ConstraintInspector;
import org.jboss.seam.forge.project.util.BeanManagerUtils;

/**
 * Responsible for instantiating {@link Facet}s through CDI.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@Singleton
public class FacetFactory implements Extension
{
   private BeanManager manager;

   private final Set<Bean<?>> facetBeans = new HashSet<Bean<?>>();

   public void scan(@Observes final ProcessBean<?> event, final BeanManager manager)
   {
      this.manager = manager;

      Bean<?> bean = event.getBean();
      Class<?> clazz = bean.getBeanClass();

      if (Facet.class.isAssignableFrom(clazz))
      {
         facetBeans.add(bean);
      }
   }

   public List<Facet> getFacets()
   {
      List<Facet> facets = new ArrayList<Facet>();

      for (Bean<?> bean : facetBeans)
      {
         facets.add((Facet) BeanManagerUtils.getContextualInstance(manager, bean));
      }

      return facets;
   }

   @SuppressWarnings("unchecked")
   public <T extends Facet> T getFacet(final Class<T> type)
   {
      T result = null;

      for (Bean<?> bean : facetBeans)
      {
         if (type.isAssignableFrom(bean.getBeanClass()))
         {
            result = (T) BeanManagerUtils.getContextualInstance(manager, bean);
         }
      }

      return result;
   }

   public Facet getFacetByName(final String facetName)
   {
      Facet result = null;
      for (Bean<?> bean : facetBeans)
      {
         Class<?> facetClass = bean.getBeanClass();
         String name = ConstraintInspector.getName(facetClass);
         if (name.equals(facetName))
         {
            result = (Facet) BeanManagerUtils.getContextualInstance(manager, bean);
         }
      }
      return result;
   }
}
