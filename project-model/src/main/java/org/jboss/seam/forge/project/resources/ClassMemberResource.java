package org.jboss.seam.forge.project.resources;

import org.jboss.seam.forge.project.Resource;

/**
 * @author Mike Brock <cbrock@redhat.com>
 */
public abstract class ClassMemberResource<T> implements Resource<T>
{
   protected Resource<?> parent;

   protected ClassMemberResource(final Resource<?> parent)
   {
      this.parent = parent;
   }

   @Override
   public Resource<?> getParent()
   {
      return parent;
   }

   @Override
   public Resource<?> getChild(final String name)
   {
      throw new RuntimeException("not implemented");
   }
}
