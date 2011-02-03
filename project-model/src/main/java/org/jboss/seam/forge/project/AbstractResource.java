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

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import org.jboss.seam.forge.project.services.ResourceFactory;

/**
 * @author Mike Brock <cbrock@redhat.com>
 */
public abstract class AbstractResource<T> implements Resource<T>
{
   protected final ResourceFactory resourceFactory;
   protected Resource<?> parent;

   protected EnumSet<ResourceFlag> flags;

   protected AbstractResource(final ResourceFactory factory, final Resource<?> parent)
   {
      this.resourceFactory = factory;
      this.parent = parent;
   }

   @Override
   public String getFullyQualifiedName()
   {
      return getParent() != null ? getParent().getFullyQualifiedName() + "/" + getName() : getName();
   }

   @Override
   public Resource<?> getParent()
   {
      return parent;
   }

   @Override
   public void setFlag(final ResourceFlag flag)
   {
      if (flags == null)
      {
         flags = EnumSet.of(flag);
      }
      else
      {
         flags.add(flag);
      }
   }

   @Override
   public void unsetFlag(final ResourceFlag flag)
   {
      if (flags != null)
      {
         flags.remove(flag);
      }
   }

   @Override
   public boolean isFlagSet(final ResourceFlag flag)
   {
      return (flags != null) && flags.contains(flag);
   }

   @Override
   public Set<ResourceFlag> getFlags()
   {
      return Collections.unmodifiableSet(flags);
   }

   @Override
   @SuppressWarnings("unchecked")
   public <R> R reify(final Class<? extends Resource<?>> type)
   {
      if (type.isAssignableFrom(this.getClass()))
      {
         return (R) this;
      }
      else
      {
         return null;
      }
   }
}
