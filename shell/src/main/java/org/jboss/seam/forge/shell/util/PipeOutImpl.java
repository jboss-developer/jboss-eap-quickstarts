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

package org.jboss.seam.forge.shell.util;

import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.PipeOut;

/**
 * @author Mike Brock .
 */
public class PipeOutImpl implements PipeOut
{
   private final StringBuilder buffer = new StringBuilder();
   private final Shell shell;
   private boolean piped = false;

   public PipeOutImpl(final Shell shell)
   {
      this.shell = shell;
   }

   @Override
   public void write(final byte b)
   {
      if (piped)
      {
         buffer.append((char) b);
      }
      else
      {
         shell.print(String.valueOf((char) b));
      }
   }

   @Override
   public void print(final String s)
   {
      if (piped)
      {
         buffer.append(s);
      }
      else
      {
         shell.print(s);
      }
   }

   @Override
   public void println(final String s)
   {
      if (piped)
      {
         buffer.append(s).append("\n");
      }
      else
      {
         shell.println(s);
      }
   }

   @Override
   public void println()
   {
      if (piped)
      {
         buffer.append("\n");
      }
      else
      {
         shell.println();
      }
   }

   @Override
   public void print(final ShellColor color, final String s)
   {
      print(renderColor(color, s));
   }

   @Override
   public void println(final ShellColor color, final String s)
   {
      println(renderColor(color, s));
   }

   @Override
   public String renderColor(final ShellColor color, final String s)
   {
      return piped ? s : shell.renderColor(color, s);
   }

   @Override
   public boolean isPiped()
   {
      return piped;
   }

   @Override
   public void setPiped(final boolean v)
   {
      this.piped = v;
   }

   @Override
   public String getBuffer()
   {
      return buffer.toString();
   }
}
