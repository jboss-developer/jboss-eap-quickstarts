package org.jboss.seam.forge.shell.plugins.builtin;

import org.jboss.seam.forge.project.resources.FileResource;
import org.jboss.seam.forge.project.resources.builtin.DirectoryResource;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.*;

import javax.inject.Inject;
import javax.inject.Named;

@Named("mkdir")
@ResourceScope(DirectoryResource.class)
@Help("Create a new directory")
public class MkdirPlugin implements Plugin
{
   private Shell shell;

   @Inject
   public MkdirPlugin(Shell shell)
   {
      this.shell = shell;
   }

   @DefaultCommand
   public void mkdir(@Option(help = "name of directory to be created", required = true) String name)
   {
      DirectoryResource dr = (DirectoryResource) shell.getCurrentResource();

      FileResource newResource = (FileResource) dr.getChild(name);
      if (!newResource.mkdir())
      {
         throw new RuntimeException("failed to create directory: " + name);
      }
   }
}
