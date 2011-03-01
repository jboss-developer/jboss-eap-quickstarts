/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc., and individual contributors
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
package org.jboss.seam.forge.shell.util;

import java.io.IOException;
import java.io.InputStream;

import org.jboss.seam.forge.resources.DirectoryResource;
import org.jboss.seam.forge.shell.ShellPrintWriter;

/**
 * Executes native system commands.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class NativeSystemCall
{
   /**
    * Execute a native system command as if it were run from the given path.
    * 
    * @param command the system command to execute
    * @param parms the command parameters
    * @param out a print writer to which command output will be streamed
    * @param path the path from which to execute the command
    */
   public static int execFromPath(final String command, final String[] parms, final ShellPrintWriter out,
            final DirectoryResource path) throws IOException
   {
      try
      {
         String[] commandTokens = parms == null ? new String[1] : new String[parms.length + 1];
         commandTokens[0] = command;

         if (commandTokens.length > 1)
         {
            System.arraycopy(parms, 0, commandTokens, 1, parms.length);
         }

         Process p = Runtime.getRuntime().exec(commandTokens, null,
                  path.getUnderlyingResourceObject());

         InputStream stdout = p.getInputStream();
         InputStream stderr = p.getErrorStream();

         byte[] buf = new byte[10];
         int read;
         while ((read = stdout.read(buf)) != -1)
         {
            for (int i = 0; i < read; i++)
            {
               out.write(buf[i]);
            }
         }

         while ((read = stderr.read(buf)) != -1)
         {
            for (int i = 0; i < read; i++)
            {
               out.write(buf[i]);
            }
         }

         return p.waitFor();

      }
      catch (InterruptedException e)
      {
         e.printStackTrace();
         return -1;
      }
   }
}
