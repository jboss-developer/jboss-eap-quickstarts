package org.jboss.seam.forge.project.resources;

import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.services.ResourceFactory;

/**
 * @author Mike Brock <cbrock@redhat.com>
 */
public abstract class ClassMemberResource<T> implements Resource<T>
{
   protected Resource parent;

   protected ClassMemberResource(Resource parent)
   {
      this.parent = parent;
   }

   @Override
   public Resource getParent()
   {
      return parent;
   }

   @Override
   public Resource getChild(ResourceFactory fac, String name)
   {
      throw new RuntimeException("not implemented");
   }
}
