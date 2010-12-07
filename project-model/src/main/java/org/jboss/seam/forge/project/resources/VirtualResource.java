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

package org.jboss.seam.forge.project.resources;

import java.io.InputStream;

import org.jboss.seam.forge.project.AbstractResource;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.services.ResourceFactory;

/**
 * @author Mike Brock
 */
public abstract class VirtualResource<T> extends AbstractResource<T>
{
   protected VirtualResource(final Resource<?> parent)
   {
      super(null, parent);
   }

   protected VirtualResource(ResourceFactory factory, final Resource<?> parent)
   {
      super(factory, parent);
   }

   @Override
   public Resource<?> getChild(final String name)
   {
      return null;
   }

   @Override
   public Resource<T> createFrom(T resource)
   {
      throw new RuntimeException("not implemented");
   }

   @Override
   public InputStream getResourceInputStream()
   {
      throw new ResourceException("not supported");
   }

   @Override
   public boolean exists()
   {
      return true;
   }
}
