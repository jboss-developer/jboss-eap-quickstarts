package org.jboss.seam.forge.project;

import org.jboss.seam.forge.project.services.ResourceFactory;

import java.io.File;
import java.util.List;

/**
 * @author Mike Brock <cbrock@redhat.com>
 */
public abstract class FileResource extends Resource<File>
{
     protected boolean scratch;

   protected Resource parent;

   protected File file;
   protected File scratchFile;

   protected long lastModification;

   protected FileResource()
   {
   }

   protected FileResource(File file)
   {
      this.file = file;
      this.lastModification = file.lastModified();
   }

   /**
    * Get the actual underlying file resource that this resource instance represents, whether existing or non-existing.
    *
    * @return An instance of {@link File}
    */
   public File getFile()
   {
      return scratch ? file : scratchFile;
   }


   /**
    * Return a list of child resources of the current resource.
    * @return A list of child resources.
    */
   public abstract List<Resource> listResources(ResourceFactory factory);

   /**
    * Create a new resource instance for the target file of the type that this current resource is.
    * @param file The file to create the resource instance from.
    * @return A new resource.
    */
   public abstract Resource createFrom(File file);

   /**
    * Returns true if the underlying resource has been modified on the file system since it was initially loaded.
    * @return boolean true if resource is changed.
    */
   public boolean isStale() {
      return lastModification != getFile().lastModified();
   }

   public void markUpToDate() {
      lastModification = getFile().lastModified();
   }
}
