package org.jboss.seam.forge.shell.plugins.builtin;

import org.jboss.seam.forge.project.services.ResourceFactory;
import org.jboss.seam.forge.project.util.ResourceUtil;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.DefaultCommand;
import org.jboss.seam.forge.shell.plugins.Help;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.Plugin;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;

/**
 * @author Mike Brock <cbrock@redhat.com>
 */
@Named("pick-up")
@Help("Picks up a specific resource to work with.")
@Singleton
public class PickupResource implements Plugin
{
   private final Shell shell;
   private final ResourceFactory resourceFactory;

   @Inject
   public PickupResource(Shell shell, ResourceFactory factory)
   {
      this.shell = shell;
      this.resourceFactory = factory;
   }

   @DefaultCommand
   public void run(@Option final String path)
   {
      shell.setCurrentResource(ResourceUtil.parsePathspec(resourceFactory, shell.getCurrentResource(), path));

      if (shell.getCurrentResource() == null)
      {
         shell.println("No such resource: " + path);
         return;
      }

      shell.println("Picked up type <" + shell.getCurrentResource().getClass().getSimpleName() + ">: " + path);
   }
}
