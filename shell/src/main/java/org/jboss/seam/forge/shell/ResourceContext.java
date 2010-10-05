package org.jboss.seam.forge.shell;

import org.jboss.seam.forge.project.Resource;

import javax.inject.Singleton;

/**
 * @author Mike Brock <cbrock@redhat.com>
 */
@Singleton
public class ResourceContext
{
   private Resource current;

   public Resource getCurrent()
   {
      return current;
   }

   public void setCurrent(Resource current)
   {
      this.current = current;
   }

   
}
