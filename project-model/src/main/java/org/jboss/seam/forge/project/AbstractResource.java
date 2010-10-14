package org.jboss.seam.forge.project;

import org.jboss.seam.forge.project.services.ResourceFactory;

import java.io.File;

/**
 * @author Mike Brock <cbrock@redhat.com>
 */
public abstract class AbstractResource<T> implements Resource<T>
{
   protected boolean scratch;

   protected AbstractResource()
   {
   }


}
