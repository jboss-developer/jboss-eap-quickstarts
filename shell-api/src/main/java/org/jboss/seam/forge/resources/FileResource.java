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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jboss.seam.forge.project.AbstractResource;
import org.jboss.seam.forge.project.ProjectModelException;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.ResourceFlag;
import org.jboss.seam.forge.project.services.ResourceFactory;
import org.jboss.seam.forge.shell.util.OSUtils;

/**
 * A standard, built-in resource for representing files on the filesystem.
 * 
 * @author Mike Brock
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public abstract class FileResource<T extends FileResource<?>> extends AbstractResource<File>
{
   protected boolean scratch;

   protected File file;
   protected File scratchFile;

   protected long lastModification;

   protected FileResource(final ResourceFactory factory, final File file)
   {
      super(factory, null);
      setFlag(ResourceFlag.File);

      if ((this.file = file) != null)
      {
         this.lastModification = file.lastModified();
      }
   }

   @Override
   public String getName()
   {
      return file.getName();
   }

   /**
    * Get the actual underlying file resource that this resource instance represents, whether existing or non-existing.
    * 
    * @return An instance of {@link File}
    */
   @Override
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

   @Override
   public InputStream getResourceInputStream()
   {
      try
      {
         return new BufferedInputStream(new FileInputStream(file));
      }
      catch (FileNotFoundException e)
      {
         throw new ResourceException("cannot obtain stream to file: file does not exist: " + file.getAbsolutePath());
      }
   }

   /**
    * Get the parent of the current resource. Returns null if the current resource is the project root.
    * 
    * @return An instance of the resource parent.
    */
   @Override
   public Resource<?> getParent()
   {
      return new DirectoryResource(resourceFactory, file.getParentFile());
   }

   @Override
   public Resource<?> getChild(final String name)
   {
      throw new ResourceException("[" + this.getClass().getSimpleName() + "] can have no children");
   }

   /**
    * Create a new {@link Resource} instance for the target file. The new {@link Resource} should be of the same type as
    * <b>this</b>.
    * 
    * @param file The file to create the resource instance from.
    * @return A new resource.
    */
   @Override
   public abstract Resource<File> createFrom(File file);

   @Override
   public boolean exists()
   {
      return getUnderlyingResourceObject().exists();
   }

   /**
    * Return true if this {@link FileResource} exists and is actually a directory, otherwise return false;
    */
   public boolean isDirectory()
   {
      return file.isDirectory();
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

   /**
    * Delete this file, recursively. TODO this should not be recursive?
    */
   public boolean delete()
   {
      return delete(true);
   }

   public boolean delete(final boolean recursive)
   {
      if (recursive)
      {
         return _deleteRecursive(file, true);
      }

      if ((file.listFiles() != null) && (file.listFiles().length != 0))
      {
         throw new RuntimeException("directory not empty");
      }

      if (OSUtils.isWindows())
      {
         System.gc(); // ensure no lingering handles that would prevent deletion
      }
      return file.delete();
   }

   private static boolean _deleteRecursive(final File file, final boolean collect)
   {
      if (collect && OSUtils.isWindows())
      {
         System.gc(); // ensure no lingering handles that would prevent deletion
      }

      if (file == null)
      {
         return false;
      }

      File[] children = file.listFiles();
      if (children != null)
      {
         for (File sf : children)
         {
            if (sf.isDirectory())
            {
               _deleteRecursive(sf, false);
            }
            else
            {
               if (!sf.delete())
               {
                  throw new RuntimeException("failed to delete: " + sf.getAbsolutePath());
               }
            }
         }
      }

      return file.delete();
   }

   public T setContents(String data)
   {
      if (data == null)
      {
         data = "";
      }
      return setContents(data.toCharArray());
   }

   public T setContents(final char[] data)
   {
      return setContents(new ByteArrayInputStream(new String(data).getBytes()));
   }

   @SuppressWarnings("unchecked")
   public T setContents(final InputStream data)
   {
      T temp = null;
      try
      {
         if (!exists())
         {
            mkdirs();
            delete();
            if (!createNewFile())
            {
               throw new IOException("Failed to create file: " + file);
            }
         }

         file.delete();

         OutputStream out = new FileOutputStream(file);
         try
         {
            byte buf[] = new byte[1024];
            int len;
            while ((len = data.read(buf)) > 0)
            {
               out.write(buf, 0, len);
            }
         }
         finally
         {
            data.close();
            out.close();
         }

         System.out.println("Wrote " + getFullyQualifiedName());
      }
      catch (IOException e)
      {
         throw new ProjectModelException(e);
      }
      finally
      {
         if (temp != null)
         {
            temp.delete();
         }
      }
      return (T) this;
   }

   /**
    * Create the file in the underlying resource system.
    */
   public boolean createNewFile()
   {
      try
      {
         if (file.mkdirs())
         {
            file.delete();
         }
         return file.createNewFile();
      }
      catch (IOException e)
      {
         throw new ProjectModelException(e);
      }
   }

   @SuppressWarnings("unchecked")
   public T createTempResource()
   {
      try
      {
         return (T) createFrom(File.createTempFile("forgetemp", ""));
      }
      catch (IOException e)
      {
         throw new ProjectModelException(e);
      }
   }

   @Override
   @SuppressWarnings("unchecked")
   public <R> R reify(final Class<? extends Resource<?>> type)
   {
      Resource<?> queryResult = resourceFactory.getResourceFrom(file);
      if (type.isAssignableFrom(queryResult.getClass()))
      {
         return (R) queryResult;
      }
      else
      {
         return null;
      }
   }

   public boolean renameTo(final String pathspec)
   {
      return file.renameTo(new File(pathspec));
   }

   public boolean renameTo(final FileResource<?> target)
   {
      return file.renameTo(target.getUnderlyingResourceObject());
   }
}
