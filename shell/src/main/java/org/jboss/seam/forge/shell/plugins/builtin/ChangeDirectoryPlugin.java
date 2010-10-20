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

import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.resources.builtin.DirectoryResource;
import org.jboss.seam.forge.project.services.ResourceFactory;
import org.jboss.seam.forge.project.util.ResourceUtil;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.DefaultCommand;
import org.jboss.seam.forge.shell.plugins.Help;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.shell.plugins.events.InitProject;

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
   private final ResourceFactory resFactory;
   private Resource<?> lastResource;

   @Inject
   public ChangeDirectoryPlugin(final Shell shell, final ResourceFactory resourceFactory, final Event<InitProject> init)
   {
      this.shell = shell;
      this.resFactory = resourceFactory;
      this.init = init;
   }

   @DefaultCommand
   public void run(@Option(description = "The new directory") final String path) throws IOException
   {
      Resource<?> curr = shell.getCurrentResource();
      Resource<?> r;

      if ("-".equals(path))
      {
         r = this.lastResource;
      }
      else if (path == null)
      {
         r = new DirectoryResource(resFactory, new File(System.getProperty("user.home")));
      }
      else
      {
         r = ResourceUtil.parsePathspec(resFactory, curr, path);
      }

      if (r != null)
      {
         shell.setCurrentResource(r);
         if (curr != null)
         {
            this.lastResource = curr;
         }

         init.fire(new InitProject());
      }
   }
}
