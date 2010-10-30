package org.jboss.seam.forge.shell.plugins.builtin;


import org.jboss.seam.forge.project.resources.FileResource;
import org.jboss.seam.forge.project.resources.builtin.DirectoryResource;
import org.jboss.seam.forge.project.services.ResourceFactory;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.*;

import javax.inject.Inject;
import javax.inject.Named;

import static org.jboss.seam.forge.project.util.ResourceUtil.parsePathspec;

@Named("rm")
@ResourceScope(DirectoryResource.class)
@Help("Removes a file or directory")
public class RmPlugin implements Plugin
{
   private final Shell shell;
   private final ResourceFactory factory;

   @Inject
   public RmPlugin(Shell shell, ResourceFactory factory)
   {
      this.shell = shell;
      this.factory = factory;
   }

   @DefaultCommand
   public void rm(@Option(name = "recursive", shortName = "r", help = "recursively delete files and directories", flagOnly = true) boolean recursive,
                  @Option(name = "force", shortName = "f", help = "do not prompt to confirm operations") boolean force,
                  @Option(description = "path", required = true) String path)
   {

      FileResource resource = (FileResource) parsePathspec(factory, shell.getCurrentResource(), path);

      if (force || shell.promptBoolean("delete: " + resource.toString() + ": are you sure?"))
      {
         if (!resource.delete(recursive)) {
            throw new RuntimeException("error deleting files.");
         }
      }
   }
}
