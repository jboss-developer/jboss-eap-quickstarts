package org.jboss.seam.forge.project.resources.builtin;

import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.resources.FileResource;
import org.jboss.seam.forge.project.services.ResourceFactory;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * @author Mike Brock <cbrock@redhat.com>
 */
public class UnknownFileResource extends FileResource
{
   public UnknownFileResource()
   {
   }

   public UnknownFileResource(File file)
   {
      super(file);
   }


   @Override
   public UnknownFileResource createFrom(File file)
   {
      return new UnknownFileResource(file);
   }

   @Override
   public List<Resource<?>> listResources(ResourceFactory factory)
   {
      return Collections.emptyList();
   }


   @Override
   public String toString()
   {
      return file.getName();
   }
}
