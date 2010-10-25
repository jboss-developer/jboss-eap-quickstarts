package org.jboss.seam.forge.project;

import org.jboss.seam.forge.project.services.ResourceFactory;

/**
 * @author Mike Brock <cbrock@redhat.com>
 */
public abstract class AbstractResource<T> implements Resource<T>
{
   protected boolean scratch;
   protected final ResourceFactory resourceFactory;

   protected AbstractResource(final ResourceFactory factory)
   {
      this.resourceFactory = factory;
   }
}
