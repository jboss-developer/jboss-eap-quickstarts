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

package org.jboss.seam.forge.resources;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jboss.seam.forge.project.ProjectModelException;
import org.jboss.seam.forge.project.services.ResourceFactory;

/**
 * A standard, build-in, resource for representing directories on the file-system.
 * 
 * @author Mike Brock
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class DirectoryResource extends FileResource<DirectoryResource>
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

         File[] files = file.listFiles();
         if (files != null)
         {
            for (File f : files)
            {
               listCache.add(resourceFactory.getResourceFrom(f));
            }
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

   /**
    * Obtain a reference to the child {@link DirectoryResource}. If that resource does not exist, return a new instance.
    * If the resource exists and is not a {@link DirectoryResource}, throw {@link ResourceException}
    */
   public DirectoryResource getChildDirectory(final String name) throws ResourceException
   {
      Resource<?> result = getChild(name);
      if (!(result instanceof DirectoryResource))
      {
         if (result.exists())
         {
            throw new ResourceException("The resource [" + result.getFullyQualifiedName()
                     + "] is not a DirectoryResource");
         }
      }

      if (!(result instanceof DirectoryResource))
      {
         result = new DirectoryResource(resourceFactory, new File(file.getAbsoluteFile() + File.separator + name));
      }
      return (DirectoryResource) result;
   }

   public DirectoryResource getOrCreateChildDirectory(String name)
   {
      DirectoryResource child = getChildDirectory(name);
      if (!child.exists())
      {
         child.mkdir();
      }
      return child;
   }

   /**
    * Using the given type, obtain a reference to the child resource of the given type. If the result is not of the
    * requested type and does not exist, return null. If the result is not of the requested type and exists, throw
    * {@link ResourceException}
    */
   @SuppressWarnings("unchecked")
   public <E, T extends Resource<E>> T getChildOfType(final Class<T> type, final String name) throws ResourceException
   {
      T result;
      Resource<?> child = getChild(name);
      if (type.isAssignableFrom(child.getClass()))
      {
         result = (T) child;
      }
      else if (child.exists())
      {
         throw new ResourceException("Requested resource [" + name + "] was not of type [" + type.getName()
                  + "], but was instead [" + child.getClass().getName() + "]");
      }
      else
      {
         E underlyingResource = (E) child.getUnderlyingResourceObject();
         result = resourceFactory.createFromType(type, underlyingResource);
      }
      return result;
   }

   @Override
   public DirectoryResource createTempResource()
   {
      try
      {
         File tempFile = File.createTempFile("forgetemp", "");
         tempFile.delete();
         return createFrom(tempFile);
      }
      catch (IOException e)
      {
         throw new ProjectModelException(e);
      }
   }

   @Override
   public DirectoryResource createFrom(final File file)
   {
      if (file.exists() && !file.isDirectory())
      {
         throw new ResourceException("File reference is not a directory: " + file.getAbsolutePath());
      }
      else if (!file.exists())
      {
         file.mkdirs();
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
