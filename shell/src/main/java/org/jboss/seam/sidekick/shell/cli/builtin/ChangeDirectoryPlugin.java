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
package org.jboss.seam.sidekick.shell.cli.builtin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.sidekick.shell.Shell;
import org.jboss.seam.sidekick.shell.plugins.DefaultCommand;
import org.jboss.seam.sidekick.shell.plugins.Help;
import org.jboss.seam.sidekick.shell.plugins.Option;
import org.jboss.seam.sidekick.shell.plugins.Plugin;
import org.jboss.seam.sidekick.shell.plugins.events.InitProject;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Named("cd")
@Help("Prints the current directory.")
public class ChangeDirectoryPlugin implements Plugin
{
   Shell shell;
   private final Event<InitProject> init;

   @Inject
   public ChangeDirectoryPlugin(Shell shell, Event<InitProject> init)
   {
      this.shell = shell;
      this.init = init;
   }

   @DefaultCommand
   public void run(@Option(defaultValue = "") final String path)
   {
      String target = path.trim();
      File cwd = shell.getCurrentDirectory();

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

      while (target.startsWith("."))
      {
         target = target.replaceFirst("\\." + File.separatorChar + "?", "");
      }

      if (!target.isEmpty())
      {
         List<File> attempts = new ArrayList<File>();
         attempts.add(new File(cwd.getAbsolutePath() + File.separatorChar + target).getAbsoluteFile());
         attempts.add(new File(target).getAbsoluteFile());

         boolean found = false;

         for (File file : attempts)
         {
            if (file.exists() && file.isDirectory())
            {
               cwd = file;
               found = true;
               break;
            }
         }

         if (!found)
         {
            shell.println(path + ": Not a directory");
         }
      }

      if (!cwd.equals(shell.getCurrentDirectory()))
      {
         shell.setCurrentDirectory(cwd.getAbsoluteFile());
         init.fire(new InitProject());
      }
   }
}
