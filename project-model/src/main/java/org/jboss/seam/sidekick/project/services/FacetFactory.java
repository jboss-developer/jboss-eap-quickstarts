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
package org.jboss.seam.sidekick.project.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessBean;
import javax.inject.Singleton;

import org.jboss.seam.sidekick.project.Facet;

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

   private final Set<Bean<?>> facetTypes = new HashSet<Bean<?>>();

   // @Inject
   // public FacetFactory(final BeanManager manager)
   // {
   // this.manager = manager;
   // }

   //
   // @PostConstruct
   // public void loadFacetTypes()
   // {
   // facetTypes = manager.getBeans(Facet.class, new Annotation[] {});
   // }

   public void scan(@Observes final ProcessBean<?> event, final BeanManager manager)
   {
      this.manager = manager;

      Bean<?> bean = event.getBean();
      Class<?> clazz = bean.getBeanClass();

      if (Facet.class.isAssignableFrom(clazz))
      {
         facetTypes.add(bean);
      }
   }

   public List<Facet> getFacets()
   {
      List<Facet> facets = new ArrayList<Facet>();

      for (Bean<?> bean : facetTypes)
      {
         facets.add((Facet) getContextualInstance(manager, bean));
      }

      return facets;
   }

   @SuppressWarnings("unchecked")
   public <T extends Facet> T getFacet(final Class<T> type)
   {
      T result = null;

      for (Bean<?> bean : facetTypes)
      {
         if (type.isAssignableFrom(bean.getBeanClass()))
         {
            result = (T) getContextualInstance(manager, bean);
         }
      }

      return result;
   }

   @SuppressWarnings("unchecked")
   private <T> T getContextualInstance(final BeanManager manager, final Bean<T> bean)
   {
      T result = null;
      if (bean != null)
      {
         CreationalContext<T> context = manager.createCreationalContext(bean);
         if (context != null)
         {
            result = (T) manager.getReference(bean, bean.getBeanClass(), context);
         }
      }
      return result;
   }
}
