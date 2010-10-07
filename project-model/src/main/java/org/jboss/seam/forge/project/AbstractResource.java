package org.jboss.seam.forge.project;

/**
 * @author Mike Brock <cbrock@redhat.com>
 */
public abstract class AbstractResource<T> implements Resource<T>
{
   protected boolean scratch;
   protected Resource parent;

   protected AbstractResource()
   {
   }

   /**
    * Get the parent of the current resource. Returns null if the current resource is the project root.
    *
    * @return An instance of the resource parent.
    */
   public Resource getParent()
   {
      return parent;
   }
}
