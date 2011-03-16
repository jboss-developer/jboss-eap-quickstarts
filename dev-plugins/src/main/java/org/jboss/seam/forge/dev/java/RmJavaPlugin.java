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

package org.jboss.seam.forge.dev.java;

import javax.inject.Inject;

import org.jboss.seam.forge.parser.java.Field;
import org.jboss.seam.forge.parser.java.FieldHolder;
import org.jboss.seam.forge.parser.java.JavaSource;
import org.jboss.seam.forge.parser.java.Method;
import org.jboss.seam.forge.parser.java.MethodHolder;
import org.jboss.seam.forge.resources.FileResource;
import org.jboss.seam.forge.resources.Resource;
import org.jboss.seam.forge.resources.java.JavaFieldResource;
import org.jboss.seam.forge.resources.java.JavaMemberResource;
import org.jboss.seam.forge.resources.java.JavaMethodResource;
import org.jboss.seam.forge.resources.java.JavaResource;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.Alias;
import org.jboss.seam.forge.shell.plugins.DefaultCommand;
import org.jboss.seam.forge.shell.plugins.Help;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.shell.plugins.RequiresResource;
import org.jboss.seam.forge.shell.plugins.Topic;
import org.jboss.seam.forge.shell.util.JavaColorizer;

/**
 * @author Mike Brock
 */
@Alias("rm")
@Topic("File & Resources")
@RequiresResource(JavaResource.class)
@Help("Removes a java field or method")
public class RmJavaPlugin implements Plugin
{
   private final Shell shell;

   @Inject
   public RmJavaPlugin(final Shell shell)
   {
      this.shell = shell;
   }

   @DefaultCommand
   public void rm(
            @Option(description = "path", required = true) final Resource<?>[] paths,
            @Option(name = "force", shortName = "f", help = "do not prompt to confirm operations", flagOnly = true) final boolean force)
   {
      for (Resource<?> resource : paths)
      {
         String prompt = "delete: " + JavaColorizer.format(shell, resource.getName()) + ": are you sure?";

         if (resource instanceof JavaResource
                  && (force || shell.promptBoolean(prompt)))
         {
            Resource<?> parent = resource.getParent();

            if (parent != null)
               shell.setCurrentResource(parent);

            if (!((FileResource<?>) resource).delete())
            {
               shell.setCurrentResource(resource);
               throw new RuntimeException("Failed to delete [" + resource.getFullyQualifiedName() + "]");
            }

         }
         else if (resource instanceof JavaMemberResource
                  && (force || shell.promptBoolean(prompt)))
         {
            if (resource instanceof JavaMethodResource)
            {
               JavaMethodResource jres = (JavaMethodResource) resource;

               Method<JavaSource<?>> method = jres.getUnderlyingResourceObject();
               JavaSource<?> origin = method.getOrigin();
               ((MethodHolder) origin).removeMethod(method);
            }
            else
            {
               if (resource instanceof JavaFieldResource)
               {
                  JavaFieldResource jres = (JavaFieldResource) resource;

                  Field<JavaSource<?>> field = jres.getUnderlyingResourceObject();
                  JavaSource<?> origin = field.getOrigin();
                  ((FieldHolder) origin).removeField(field);
               }
            }
         }
      }
   }
}
