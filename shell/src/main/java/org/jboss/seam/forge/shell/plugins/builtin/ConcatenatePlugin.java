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

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.resources.FileResource;
import org.jboss.seam.forge.shell.plugins.Alias;
import org.jboss.seam.forge.shell.plugins.DefaultCommand;
import org.jboss.seam.forge.shell.plugins.Help;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.PipeIn;
import org.jboss.seam.forge.shell.plugins.PipeOut;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.shell.plugins.Topic;

/**
 * @author Mike Brock .
 */
@Alias("cat")
@Topic("File & Resources")
@Help("Concatenate and print files")
public class ConcatenatePlugin implements Plugin
{
   @DefaultCommand
   public void run(
            @PipeIn final InputStream in, // pipe in
            @Option(description = "path", required = false) Resource<?>[] paths, // params
            final PipeOut out // pipe out
   ) throws IOException
   {
      String lastBuf = null;
      if (in != null)
      {
         lastBuf = writeOutToConsole(in, out);
      }

      if (paths != null)
      {

         for (Resource<?> res : paths)
         {
            if (res instanceof FileResource)
            {
               InputStream istream = null;
               try
               {
                  istream = new BufferedInputStream(new FileInputStream(res.getFullyQualifiedName()));
                  lastBuf = writeOutToConsole(istream, out);
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
      }

      if (lastBuf == null || lastBuf.charAt(lastBuf.length() - 1) != '\n')
      {
         out.println();
      }
   }

   private static String writeOutToConsole(InputStream istream, PipeOut out) throws IOException
   {
      byte[] buf = new byte[10];
      int read;
      String s = null;
      while ((read = istream.read(buf)) != -1)
      {
         out.print(s = new String(buf, 0, read));
      }

      return s;

   }
}
