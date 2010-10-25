package org.jboss.seam.forge.shell.project;

import javax.inject.Singleton;

import org.jboss.seam.forge.project.Resource;

/**
 * @author Mike Brock <cbrock@redhat.com>
 */
@Singleton
public class ResourceContext
{
   // FIXME Resource API needs to be separated from project API
   private Resource<?> current;

   public Resource<?> getCurrent()
   {
      return current;
   }

   public void setCurrent(final Resource<?> current)
   {
      this.current = current;
   }

   @Override
   public String toString()
   {
      return "ResourceContext [" + current + "]";
   }

}
