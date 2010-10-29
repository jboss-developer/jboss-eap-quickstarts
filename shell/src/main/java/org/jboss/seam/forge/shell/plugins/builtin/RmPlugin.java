package org.jboss.seam.forge.shell.plugins.builtin;


import org.jboss.seam.forge.project.resources.builtin.DirectoryResource;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.Help;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.shell.plugins.ResourceScope;

import javax.inject.Inject;
import javax.inject.Named;

@Named("rm")
@ResourceScope(DirectoryResource.class)
@Help("Removes a file or directory")
public class RmPlugin implements Plugin
{
   private Shell shell;

   @Inject
   public RmPlugin(Shell shell)
   {
      this.shell = shell;
   }

   public void rm(@Option(name = "recursive", shortName = "r", help = "recursively delete files and directories", flagOnly = true) boolean recursive,
                  @Option(name = "force", shortName = "f", help = "do not prompt to confirm operations") boolean force,
                  @Option(description = "path") String path) {


   }
}
