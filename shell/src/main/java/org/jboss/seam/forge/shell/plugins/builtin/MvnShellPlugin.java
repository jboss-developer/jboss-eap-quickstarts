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

import org.jboss.seam.forge.project.constraints.RequiresProject;
import org.jboss.seam.forge.project.resources.builtin.DirectoryResource;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.*;
import org.mvel2.util.StringAppender;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Mike Brock .
 */
@Named("mvn")
@Topic("Project")
@RequiresProject
@ResourceScope(DirectoryResource.class)
public class MvnShellPlugin implements Plugin
{
   private final Shell shell;

   @Inject
   public MvnShellPlugin(Shell shell)
   {
      this.shell = shell;
   }

   @DefaultCommand
   public void run(final PipeOut out, String... parms) throws IOException
   {
      StringAppender appender = new StringAppender();

      if (parms != null)
      {
         for (String s : parms)
         {
            appender.append(s).append(" ");
         }
      }

      try
      {
         Process p = Runtime.getRuntime().exec("mvn " + appender.toString(), null,
               shell.getCurrentDirectory().getUnderlyingResourceObject());


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

         p.waitFor();

      }
      catch (InterruptedException e)
      {
         e.printStackTrace();
      }
   }

}
