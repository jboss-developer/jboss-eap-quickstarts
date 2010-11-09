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
import org.jboss.seam.forge.project.services.ResourceFactory;
import org.jboss.seam.forge.project.util.ResourceUtil;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.*;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;

/**
 * @author Mike Brock <cbrock@redhat.com>
 */
@Named("pick-up")
@Topic("File & Resources")
@Help("Picks up a specific resource to work with.")
@Singleton
public class PickupResource implements Plugin
{
   private final Shell shell;
   private final ResourceFactory resourceFactory;

   @Inject
   public PickupResource(Shell shell, ResourceFactory factory)
   {
      this.shell = shell;
      this.resourceFactory = factory;
   }

   @DefaultCommand
   public void run(@Option final Resource<?> resource)
   {
      shell.setCurrentResource(resource);

      if (shell.getCurrentResource() == null)
      {
         shell.println("No such resource: " + resource);
         return;
      }

      shell.println("Picked up type <" + shell.getCurrentResource().getClass().getSimpleName() + ">: " + resource);
   }
}
