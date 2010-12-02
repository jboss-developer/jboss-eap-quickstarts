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

package org.jboss.seam.forge.project.resources.builtin;

import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.ResourceFlag;
import org.jboss.seam.forge.project.resources.FileResource;
import org.jboss.seam.forge.project.services.ResourceFactory;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * A standard, build-in, resource for representing directories on the
 * filesystem.
 * 
 * @author Mike Brock
 */
public class DirectoryResource extends FileResource
{
   private volatile List<Resource<?>> listCache;

   public DirectoryResource(final ResourceFactory factory, final File file)
   {
      super(factory, file);
      setFlag(ResourceFlag.Node);
   }

   @Override
   public synchronized List<Resource<?>> listResources()
   {
      if (isStale())
      {
         listCache = null;
      }

      if (listCache == null)
      {
         listCache = new LinkedList<Resource<?>>();

         for (File f : file.listFiles())
         {
            listCache.add(resourceFactory.getResourceFrom(f));
         }
      }

      return listCache;
   }

   /**
    * Obtain a reference to the child resource.
    */
   @Override
   public Resource<?> getChild(final String name)
   {
      return resourceFactory.getResourceFrom(new File(file.getAbsolutePath() + File.separator + name));
   }

   @Override
   public DirectoryResource createFrom(final File file)
   {
      if (!file.isDirectory())
      {
         throw new RuntimeException("File reference is not a directory: " + file.getAbsolutePath());
      }

      return new DirectoryResource(resourceFactory, file);
   }

   @Override
   public synchronized Resource<?> getParent()
   {
      if (parent == null)
      {
         File parentFile = file.getParentFile();
         if (parentFile == null)
         {
            return null;
         }

         parent = createFrom(parentFile);
      }
      return parent;
   }

   @Override
   public String getName()
   {
      return file.getName();
   }

   @Override
   public String toString()
   {
      return getName();
   }

   @Override
   public boolean equals(final Object obj)
   {
      return (obj instanceof DirectoryResource) && ((DirectoryResource) obj).file.equals(file);
   }
}
