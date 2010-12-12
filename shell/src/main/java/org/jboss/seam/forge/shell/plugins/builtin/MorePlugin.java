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
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.*;
import org.jboss.seam.forge.shell.util.ShellColor;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Mike Brock .
 */
@Named("more")
@Topic("Shell Environment")
public class MorePlugin implements Plugin
{
   private static final String MOREPROMPT = "--[SPACE to PageFwd, ENTER to Advance Line, Q to Quit]--";

   private final Shell shell;

   @Inject
   public MorePlugin(Shell shell)
   {
      this.shell = shell;
   }

   @DefaultCommand
   public void run(@PipeIn InputStream pipeIn,
                   final Resource<?> file,
                   final PipeOut pipeOut)
         throws IOException
   {
      if (file != null)
      {
         more(file.getResourceInputStream(), pipeOut);
      }
      else if (pipeIn != null)
      {
         more(pipeIn, pipeOut);
      }
   }

   private void more(InputStream stream, PipeOut out) throws IOException
   {
      byte[] buffer = new byte[1024];
      int read;

      byte c;

      int height = shell.getHeight();
      int y = 0;


      Mainloop: while ((read = stream.read(buffer)) != -1)
      {
         Bufferloop: for (int i = 0; i < read; i++)
         {
            switch (c = buffer[i])
            {
            case '\r':
               i++;
            case '\n':
               if (++y == height)
               {
                  out.println();
                  out.print(ShellColor.BOLD, MOREPROMPT);

                  do
                  {
                     int code;
                     switch (code = shell.scan())
                     {
                     case '\n':
                        y--;
                        shell.clearLine();
                        shell.cursorLeft(MOREPROMPT.length());
                        height = shell.getHeight();
                        continue Bufferloop;
                     case ' ':
                        y = 0;
                        shell.clearLine();
                        shell.cursorLeft(MOREPROMPT.length());
                        height = shell.getHeight();
                        continue Bufferloop;
                     case 'q':
                     case 'Q':
                        out.println();
                        break Mainloop;
                     default:
                        System.out.println("unknown command code:" + code);
                     }

                  }
                  while (true);
               }
            }

            out.write(c);
         }
      }
   }
}
