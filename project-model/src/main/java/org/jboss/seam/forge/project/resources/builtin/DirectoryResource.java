package org.jboss.seam.forge.project.resources.builtin;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.resources.FileResource;
import org.jboss.seam.forge.project.services.ResourceFactory;

/**
 * @author Mike Brock <cbrock@redhat.com>
 */
public class DirectoryResource extends FileResource
{
   private Resource<?> parent;
   private volatile List<Resource<?>> listCache;

   @Inject
   public DirectoryResource(final ResourceFactory factory)
   {
      super(factory);
   }

   public DirectoryResource(final ResourceFactory factory, final File file)
   {
      super(factory, file);
   }

   @Override
   public synchronized List<Resource<?>> listResources()
   {
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

   @Override
   public Resource<?> getChild(final String name)
   {
      return resourceFactory.getResourceFrom(new File(file.getAbsolutePath() + "/" + name));
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
   public String toString()
   {
      return file.getName() + "/";
   }

   @Override
   public boolean equals(final Object obj)
   {
      return (obj instanceof DirectoryResource) && ((DirectoryResource) obj).file.equals(file);
   }

}

