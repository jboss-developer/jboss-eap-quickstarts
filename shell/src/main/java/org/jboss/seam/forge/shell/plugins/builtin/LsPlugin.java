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
package org.jboss.seam.forge.shell.plugins.builtin;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.DefaultCommand;
import org.jboss.seam.forge.shell.plugins.Help;
import org.jboss.seam.forge.shell.plugins.Plugin;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Named("ls")
@Help("Prints the contents current directory.")
public class LsPlugin implements Plugin
{
   @Inject
   Shell shell;

   DateFormat format = new SimpleDateFormat("MMM WW HH:mm");

   @DefaultCommand
   public void run()
   {
      File currentDir = shell.getCurrentDirectory();

      LinkedList<File> files = new LinkedList<File>(Arrays.asList(currentDir.listFiles()));

      File parent = currentDir.getParentFile();
      if (parent != null)
      {
         files.addFirst(new File(".."));
      }

      files.addFirst(new File("."));

      shell.println("total " + files.size());

      int longest = 0;
      for (File file : files)
      {
         int size = String.valueOf(file.length()).length();
         if (size > longest)
         {
            longest = size;
         }
      }

      for (File file : files)
      {
         String path = file.getPath().replaceFirst(currentDir.getPath() + "/?", "");

         String dir = file.isDirectory() ? "d" : "-";
         String read = file.canRead() ? "r" : "-";
         String write = file.canWrite() ? "w" : "-";
         String execute = file.canExecute() ? "x" : "-";

         String size = String.valueOf(file.length());
         while (size.length() < longest + 1)
         {
            size += " ";
         }

         String perms = read + write + execute;
         String lastModified = format.format(new Date(file.lastModified()));

         shell.println(dir + perms + " " + size + " " + lastModified + " " + path);
      }
   }
}
