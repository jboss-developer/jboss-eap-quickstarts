package org.jboss.seam.forge.shell.plugins.builtin;

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

   @Inject
   public PickupResource(Shell shell)
   {
      this.shell = shell;
   }

   @DefaultCommand
   public void run(@Option(defaultValue = "~") final File path)
   {
      shell.setCurrentResource(path);

      if (shell.getCurrentResource() == null)
      {
         shell.println("No such resource: " + path.getAbsolutePath());
         return;
      }

      shell.println("Picked up type <" + shell.getCurrentResource().getClass() + ">: " + path.getAbsolutePath());
   }
}
