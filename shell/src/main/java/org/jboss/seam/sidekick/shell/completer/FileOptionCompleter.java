/*
 * JBoss, Home of Professional Open Source
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
package org.jboss.seam.sidekick.shell.completer;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import jline.console.completer.FileNameCompleter;

import org.jboss.seam.sidekick.shell.Shell;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class FileOptionCompleter extends FileNameCompleter
{
   private final Shell shell;

   @Inject
   public FileOptionCompleter(final Shell shell)
   {
      this.shell = shell;
   }

   @Override
   public int complete(final String buffer, final int cursor, final List<CharSequence> candidates)
   {
      String[] tokens = buffer.substring(0, cursor).split("\\s+");
      if (cursor < buffer.length() - 1)
      {

      }
      return super.complete(buffer, cursor, candidates);
   }

   @Override
   protected File getUserDir()
   {
      return shell.getCurrentDirectory();
   }
}
