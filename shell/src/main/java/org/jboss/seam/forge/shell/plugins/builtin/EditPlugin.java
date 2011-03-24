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

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import org.jboss.seam.forge.resources.FileResource;
import org.jboss.seam.forge.resources.Resource;
import org.jboss.seam.forge.shell.plugins.Alias;
import org.jboss.seam.forge.shell.plugins.DefaultCommand;
import org.jboss.seam.forge.shell.plugins.Help;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.shell.plugins.Topic;
import org.jboss.seam.forge.shell.util.NativeSystemCall;
import org.jboss.seam.forge.shell.util.OSUtils;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * @author Mike Brock
 */
@Alias("edit")
@Topic("File & Resources")
@Help("Edit files with the default system editor")
public class EditPlugin implements Plugin
{
   @Inject
   public EditPlugin()
   {
   }

   @DefaultCommand
   public void run(@Option(description = "The files to edit", defaultValue = ".") final Resource<?>[] dirs)
            throws IOException
   {
      for (Resource<?> resource : dirs)
      {
         if (resource instanceof FileResource<?>)
         {
            Desktop dt = Desktop.getDesktop();
            try
            {
               dt.edit((File) resource.getUnderlyingResourceObject());
            }
            catch (UnsupportedOperationException e)
            {
               if (OSUtils.isLinux())
               {
                  NativeSystemCall.exec(true, "gedit", resource.getFullyQualifiedName());
               }
               else
                  throw e;
            }
         }
      }
   }
}
