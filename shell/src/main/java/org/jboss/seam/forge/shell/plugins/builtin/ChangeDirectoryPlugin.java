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
import java.io.IOException;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.DefaultCommand;
import org.jboss.seam.forge.shell.plugins.Help;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.shell.plugins.events.InitProject;
import org.jboss.seam.forge.shell.util.Files;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Named("cd")
@Help("Change the current directory")
@Singleton
public class ChangeDirectoryPlugin implements Plugin
{
   private final Shell shell;
   private final Event<InitProject> init;

   private File lastDirectory = null;

   @Inject
   public ChangeDirectoryPlugin(final Shell shell, final Event<InitProject> init)
   {
      this.shell = shell;
      this.init = init;
   }

   @DefaultCommand
   public void run(@Option(defaultValue = "~",
            description = "The new directory") final File path) throws IOException
   {
      String target = path.getPath();

      File cwd = shell.getCurrentDirectory();
      target = Files.canonicalize(target);

      if ("-".equals(target))
      {
         if (lastDirectory != null)
         {
            target = lastDirectory.getAbsolutePath();
         }
         else
         {
            target = "";
         }
      }

      while (target.startsWith(".."))
      {
         target = target.replaceFirst("\\.\\." + File.separatorChar + "?", "");
         if (cwd.getParentFile() != null)
         {
            cwd = cwd.getParentFile();
         }
         else
         {
            break;
         }
      }

      if (!target.isEmpty())
      {
         File file = null;
         if (target.startsWith(File.separator))
         {
            file = new File(target).getAbsoluteFile();
         }
         else
         {
            file = new File(cwd.getAbsolutePath() + File.separatorChar + target).getAbsoluteFile();
         }

         boolean found = false;

         if (file.exists() && file.isDirectory())
         {
            cwd = file.getCanonicalFile();
            found = true;
         }

         if (!found)
         {
            shell.println(path + ": Not a directory");
         }
      }

      if (!cwd.equals(shell.getCurrentDirectory()))
      {
         lastDirectory = shell.getCurrentDirectory();
         shell.setCurrentDirectory(cwd.getAbsoluteFile());
         init.fire(new InitProject());
      }
   }
}
