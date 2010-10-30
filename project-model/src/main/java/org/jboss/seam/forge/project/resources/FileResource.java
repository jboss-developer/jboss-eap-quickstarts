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

import java.io.File;

import org.jboss.seam.forge.project.AbstractResource;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.resources.builtin.DirectoryResource;
import org.jboss.seam.forge.project.services.ResourceFactory;

/**
 * @author Mike Brock
 */
public abstract class FileResource extends AbstractResource<File>
{
   protected boolean scratch;

   protected File file;
   protected File scratchFile;

   protected long lastModification;

   protected FileResource(final ResourceFactory factory)
   {
      super(factory);
   }

   protected FileResource(final ResourceFactory factory, final File file)
   {
      super(factory);
      this.file = file;
      this.lastModification = file.lastModified();
   }

   /**
    * Get the actual underlying file resource that this resource instance represents, whether existing or non-existing.
    *
    * @return An instance of {@link File}
    */
   public File getUnderlyingResourceObject()
   {
      if (scratch)
      {
         return scratchFile;
      }
      else
      {
         return file;
      }
   }

   /**
    * Get the parent of the current resource. Returns null if the current resource is the project root.
    *
    * @return An instance of the resource parent.
    */
   public Resource<?> getParent()
   {
      return new DirectoryResource(resourceFactory, file.getParentFile());
   }

   public Resource<?> getChild(final String name)
   {
      throw new RuntimeException("this resource type can have no children");
   }

   /**
    * Create a new resource instance for the target file of the type that this current resource is.
    *
    * @param file The file to create the resource instance from.
    * @return A new resource.
    */
   public abstract Resource<File> createFrom(File file);

   public boolean exists()
   {
      return getUnderlyingResourceObject().exists();
   }

   /**
    * Returns true if the underlying resource has been modified on the file system since it was initially loaded.
    *
    * @return boolean true if resource is changed.
    */
   public boolean isStale()
   {
      return lastModification != getUnderlyingResourceObject().lastModified();
   }

   public void markUpToDate()
   {
      lastModification = getUnderlyingResourceObject().lastModified();
   }


   public boolean mkdir()
   {
      return file.mkdir();
   }

   public boolean mkdirs()
   {
      return file.mkdirs();
   }

   public boolean delete(boolean recursive)
   {
      if (recursive)
      {
         _deleteRecursive(file);
      }

      if (file.listFiles().length != 0)
      {
         throw new RuntimeException("directory not empty");
      }

      return file.delete();
   }

   private static boolean _deleteRecursive(File file)
   {
      if (file == null) return false;

      for (File sf : file.listFiles())
      {
         if (sf.isDirectory())
         {
            _deleteRecursive(sf);
         }
         else
         {
            if (!sf.delete())
            {
               throw new RuntimeException("failed to delete: " + sf.getAbsolutePath());
            }
         }
      }

      return file.delete();
   }
}
