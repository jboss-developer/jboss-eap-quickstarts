package org.jboss.seam.forge.project.resources.builtin;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.resources.FileResource;
import org.jboss.seam.forge.project.services.ResourceFactory;

/**
 * @author Mike Brock <cbrock@redhat.com>
 */
public class UnknownFileResource extends FileResource
{
   public UnknownFileResource(final ResourceFactory factory)
   {
      super(factory);
   }

   public UnknownFileResource(final ResourceFactory factory, final File file)
   {
      super(factory, file);
   }

   @Override
   public UnknownFileResource createFrom(final File file)
   {
      return new UnknownFileResource(resourceFactory, file);
   }

   @Override
   public List<Resource<?>> listResources()
   {
      return Collections.emptyList();
   }

   @Override
   public String toString()
   {
      return file.getName();
   }
}
