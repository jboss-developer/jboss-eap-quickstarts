package org.jboss.seam.forge.project.resources.builtin;

import org.jboss.seam.forge.project.FileResource;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.services.ResourceFactory;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mike Brock <cbrock@redhat.com>
 */

public class DirectoryResource extends FileResource
{
   private volatile List<Resource> listCache;

   public DirectoryResource(File file)
   {
      super(file);
   }

   @Override
   public synchronized List<Resource> listResources(ResourceFactory factory)
   {
      if (listCache == null)
      {
         listCache = new LinkedList<Resource>();

         for (File f : file.listFiles())
         {
            listCache.add(factory.getResourceFrom(f));
         }
      }

      return listCache;
   }

   @Override
   public Resource createFrom(File file)
   {
      if (!file.isDirectory())
      {
         throw new RuntimeException("File reference is not a directory: " + file.getAbsolutePath());
      }

      return new DirectoryResource(file);
   }
}
