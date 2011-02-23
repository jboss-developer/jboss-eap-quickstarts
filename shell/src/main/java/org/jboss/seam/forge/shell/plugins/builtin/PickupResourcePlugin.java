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

import java.util.List;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.services.ResourceFactory;
import org.jboss.seam.forge.project.util.PathspecParser;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.events.PickupResource;
import org.jboss.seam.forge.shell.plugins.DefaultCommand;
import org.jboss.seam.forge.shell.plugins.Help;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.shell.plugins.Topic;

/**
 * @author Mike Brock <cbrock@redhat.com>
 */
@Named("pick-up")
@Topic("File & Resources")
@Help("Picks up a specific resource to work with.")
@Singleton
public class PickupResourcePlugin implements Plugin
{
   private final Shell shell;
   private final ResourceFactory resourceFactory;

   @Inject
   public PickupResourcePlugin(final Shell shell, final ResourceFactory factory)
   {
      this.shell = shell;
      this.resourceFactory = factory;
   }

   void pickup(@Observes final PickupResource event)
   {
      run(event.getResource(), null);
   }

   @DefaultCommand
   public void run(@Option(required = false) Resource<?> resource,
                   @Option(required = false, name = "find", shortName = "f") final String path)
   {

      if (path != null)
      {
         PathspecParser pathspecParser = new PathspecParser(resourceFactory, shell.getCurrentResource(), path);
         List<Resource<?>> targets = pathspecParser.search();

         if (targets.isEmpty())
         {
            shell.println("No such resource");
         }
         else if (targets.size() > 1)
         {
            shell.println("Multiple targets");
            shell.println("----------------");

            int offset = shell.getCurrentResource().getFullyQualifiedName().length();

            for (Resource<?> r : targets)
            {
               shell.println(" --> ." + r.getFullyQualifiedName().substring(offset));
            }
            return;
         }
         else
         {
            resource = targets.get(0);

         }
      }

      shell.setCurrentResource(resource);

      if (shell.getCurrentResource() == null)
      {
         shell.println("No such resource: " + resource);
         return;
      }

      shell.println("Picked up type <" + shell.getCurrentResource().getClass().getSimpleName() + ">: " + resource);
   }
}
