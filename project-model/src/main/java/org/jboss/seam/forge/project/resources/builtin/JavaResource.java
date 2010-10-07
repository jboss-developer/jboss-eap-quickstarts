package org.jboss.seam.forge.project.resources.builtin;

import org.jboss.seam.forge.project.resources.FileResource;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.ResourceHandles;
import org.jboss.seam.forge.project.services.ResourceFactory;

import java.io.File;
import java.util.List;

/**
 * @author Mike Brock <cbrock@redhat.com>
 */
@ResourceHandles("*.java")
public class JavaResource extends FileResource
{
   public JavaResource(File file)
   {
      super(file);
   }

   @Override
   public List<Resource> listResources(ResourceFactory factory)
   {
      return null;
   }

   @Override
   public Resource createFrom(File file)
   {
      return new JavaResource(file);
   }
}


