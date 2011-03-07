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

package org.jboss.seam.forge.shell.test.resources;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.jboss.seam.forge.resources.DirectoryResource;
import org.jboss.seam.forge.resources.Resource;
import org.jboss.seam.forge.resources.java.JavaResource;
import org.jboss.seam.forge.shell.plugins.Alias;
import org.jboss.seam.forge.shell.plugins.Current;
import org.jboss.seam.forge.shell.plugins.DefaultCommand;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.shell.plugins.RequiresResource;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Alias("inject")
@Singleton
@RequiresResource(DirectoryResource.class)
public class MockResourceInjectionPlugin implements Plugin
{
   @Inject
   @Current
   private Resource<?> r;

   @Inject
   @Current
   private JavaResource j;

   @Inject
   @Current
   private DirectoryResource d;

   private Resource<?> observedResource;

   @DefaultCommand
   public void run()
   {

   }

   public void observe(@Observes final MockEvent event, @Current final Resource<?> resource)
   {
      this.observedResource = resource;
   }

   public Resource<?> getR()
   {
      return r;
   }

   public void setR(final Resource<?> r)
   {
      this.r = r;
   }

   public JavaResource getJ()
   {

      return j;
   }

   public void setJ(final JavaResource j)
   {
      this.j = j;
   }

   public DirectoryResource getD()
   {
      return d;
   }

   public void setD(final DirectoryResource d)
   {
      this.d = d;
   }

   public Resource<?> getObservedResource()
   {
      return observedResource;
   }

   public void setObservedResource(final Resource<?> observedResource)
   {
      this.observedResource = observedResource;
   }
}
