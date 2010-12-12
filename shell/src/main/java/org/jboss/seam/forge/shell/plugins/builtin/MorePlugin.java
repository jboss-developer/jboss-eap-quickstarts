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
import org.mvel2.util.StringAppender;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Implementation of more & less, but called more.
 * @author Mike Brock .
 */
@Named("more")
@Topic("Shell Environment")
public class MorePlugin implements Plugin
{
   private static final String MOREPROMPT = "--[SPACE:PageDn U:PageUp ENT:LineDn J:LineUp Q:Quit]--";

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
         InputStream fileInstream = null;
         try
         {
            fileInstream = file.getResourceInputStream();
            more(fileInstream, pipeOut);
         }
         finally
         {
            if (fileInstream != null)
            {
               fileInstream.close();
            }
         }
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

      int height = shell.getHeight() - 1;
      int y = 0;

      LineBuffer lineBuffer = new LineBuffer(stream);

      Mainloop:
      while ((read = lineBuffer.read(buffer)) != -1)
      {
         Bufferloop:
         for (int i = 0; i < read; i++)
         {
            switch (c = buffer[i])
            {
            case '\r':
               i++;
            case '\n':
               lineBuffer.seenLine();

               if (++y == height)
               {
                  out.println();
                  String prompt = MOREPROMPT + "[line:" + lineBuffer.getCurrentLine() + "]--";
                  out.print(ShellColor.BOLD, prompt);

                  do
                  {
                     switch (shell.scan())
                     {
                     case 'j':
                     case 'J':
                     case 16:
                        shell.clear();
                        lineBuffer.rewindBuffer(height = shell.getHeight() - 1, lineBuffer.getCurrentLine() - 1);
                        y = 0;
                        continue Mainloop;
                     case 'u':
                     case 'U':
                        shell.clear();
                        lineBuffer.rewindBuffer(height = shell.getHeight() - 1, lineBuffer.getCurrentLine() - height);
                        y = 0;
                        continue Mainloop;

                     case 14:
                     case '\n':
                        y--;
                        shell.clearLine();
                        shell.cursorLeft(prompt.length());
                        height = shell.getHeight() - 1;
                        continue Bufferloop;
                     case ' ':
                        y = 0;
                        shell.clearLine();
                        shell.cursorLeft(prompt.length());
                        height = shell.getHeight() - 1;
                        continue Bufferloop;
                     case 'q':
                     case 'Q':
                        out.println();
                        break Mainloop;
                     }
                  }
                  while (true);
               }
            }

            out.write(c);
         }
      }
   }


   private static class LineBuffer extends InputStream
   {
      private InputStream stream;
      private StringAppender curr;
      private ArrayList<Integer> index;
      private int bufferPos;
      private int bufferLine;

      private static final int INDEX_MARK_SIZE = 20;

      int totalLines = 0;

      private LineBuffer(InputStream stream)
      {
         this.stream = stream;
         curr = new StringAppender();
         index = new ArrayList<Integer>();
      }


      @Override
      public int read() throws IOException
      {
         int c;
         if (inBuffer())
         {
            return curr.charAt(bufferPos++);
         }
         else
         {
            c = stream.read();
            if (c != -1)
            {
               curr.append((char) c);
               bufferPos++;
               if (c == '\n')
               {
                  markLine();
               }
            }
            return c;
         }
      }

      public void write(byte b)
      {
         if (!inBuffer())
         {
            curr.append((char) b);
         }
      }

      public void seenLine()
      {
         bufferLine++;
      }


      public void markLine()
      {
         if (++totalLines % INDEX_MARK_SIZE == 0)
         {
            index.add(curr.length());
         }
      }

      public int getCurrentLine()
      {
         return bufferLine;
      }

      public int findLine(int line)
      {
         int idxMark = line / INDEX_MARK_SIZE;

         if (idxMark > index.size())
         {
            return curr.length() - 1;
         }
         else
         {
            int cursor = idxMark == 0 ? 0 : index.get(idxMark - 1);
            int currLine = idxMark * INDEX_MARK_SIZE;

            while (cursor < curr.length() && currLine != line)
            {
               switch (curr.charAt(cursor++))
               {
               case '\r':
                  cursor++;
               case '\n':
                  currLine++;
               }
            }

            return cursor;
         }
      }

      public void rewindBuffer(int height, int toLine)
      {
         int renderFrom = toLine - height;
         if (renderFrom < 0)
         {
            bufferLine = 0;
            bufferPos = 0;
         }
         else
         {
            bufferPos = findLine(renderFrom);
            bufferLine = renderFrom;
         }
      }

      public boolean inBuffer()
      {
         return bufferPos < curr.length();
      }
   }
}
