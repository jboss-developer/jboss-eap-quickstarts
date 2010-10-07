package org.jboss.seam.forge.project;

/**
 * @author Mike Brock <cbrock@redhat.com>
 */
public interface Resource<T>
{


   /**
    * Get the parent of the current resource. Returns null if the current resource is the project root.
    *
    * @return An instance of the resource parent.
    */
   public Resource getParent();
   /**
    * Create a new resource instance for the target resource reference of the type that this current resource is.
    *
    * @param file The target reference to create the resource instance from.
    * @return A new resource.
    */
   public abstract Resource<T> createFrom(T file);
}
