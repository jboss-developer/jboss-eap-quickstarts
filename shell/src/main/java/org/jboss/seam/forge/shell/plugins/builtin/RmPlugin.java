/*
 * JBoss, by Red Hat.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.seam.forge.shell.plugins.builtin;


import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.resources.FileResource;
import org.jboss.seam.forge.project.resources.builtin.DirectoryResource;
import org.jboss.seam.forge.project.services.ResourceFactory;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.*;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Mike Brock
 */
@Named("rm")
@Topic("File & Resources")
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
                  @Option(name = "force", shortName = "f", help = "do not prompt to confirm operations", flagOnly = true) boolean force,
                  @Option(description = "path", required = true) Resource<?>[] paths)
   {
      for (Resource<?> resource : paths)
      {
         if (resource instanceof FileResource)
         {
            FileResource fResource = (FileResource) resource;

            if (force || shell.promptBoolean("delete: " + resource.toString() + ": are you sure?"))
            {
               if (!fResource.delete(recursive))
               {
                  throw new RuntimeException("error deleting files.");
               }
            }
         }
      }
   }
}
