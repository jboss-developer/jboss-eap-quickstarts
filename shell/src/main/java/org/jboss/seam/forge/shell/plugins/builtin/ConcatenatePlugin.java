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
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.*;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Mike Brock .
 */
@Named("cat")
@Topic("File & Resources")
@ResourceScope(DirectoryResource.class)
@Help("Concatenate and print files")
public class ConcatenatePlugin implements Plugin
{
   private final Shell shell;

   @Inject
   public ConcatenatePlugin(Shell shell)
   {
      this.shell = shell;
   }

   @DefaultCommand
   public void run(@Option(description = "path", required = true) Resource<?>[] paths) throws IOException
   {
      for (Resource res : paths)
      {
         if (res instanceof FileResource)
         {
            InputStream istream = null;
            try
            {
               istream = new FileInputStream(res.getFullyQualifiedName());

               byte[] buf = new byte[10];
               int read;
               while ((read = istream.read(buf)) != -1)
               {
                  shell.print(new String(buf, 0, read));
               }

            }
            catch (IOException e)
            {
               throw new RuntimeException("error opening file: " + res.getName());
            }
            finally
            {
               if (istream != null)
               {
                  istream.close();
               }
            }
         }
      }
    //  shell.println();
   }
}
