package org.jboss.seam.forge.project.resources;

import org.jboss.seam.forge.project.AbstractResource;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.resources.builtin.DirectoryResource;
import org.jboss.seam.forge.project.services.ResourceFactory;

import java.io.File;
import java.util.List;

/**
 * @author Mike Brock <cbrock@redhat.com>
 */
public abstract class FileResource extends AbstractResource<File>
{
   protected boolean scratch;

   protected File file;
   protected File scratchFile;

   protected long lastModification;

   protected FileResource(ResourceFactory factory)
   {
      super(factory);
   }

   protected FileResource(ResourceFactory factory, File file)
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
   public Resource getParent()
   {
      return new DirectoryResource(resourceFactory, file.getParentFile());
   }

   public Resource getChild(String name)
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
}
