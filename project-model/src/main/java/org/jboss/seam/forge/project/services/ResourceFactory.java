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

package org.jboss.seam.forge.project.services;

import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.ResourceHandles;
import org.jboss.seam.forge.project.resources.builtin.DirectoryResource;
import org.jboss.seam.forge.project.resources.builtin.UnknownFileResource;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessBean;
import javax.inject.Singleton;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Mike Brock <cbrock@redhat.com>
 */
@Singleton
public class ResourceFactory implements Extension
{
   private final List<ResourceGenerator> resourceGenerators = new ArrayList<ResourceGenerator>();
   /**
    * Most directories will tend to contain the same type of file (such as .java, .jar, .xml, etc). So we will remember
    * the last resource type we tested against and always re-try on subsequent queries before doing a comprehensive
    * match.
    */
   private volatile ResourceGenerator lastTypeLoaded = new ResourceGenerator()
   {
      @Override
      public boolean matches(final String name)
      {
         return false;
      }
   };

   public void scan(@Observes final ProcessBean<?> event, final BeanManager manager)
   {
      Bean<?> bean = event.getBean();
      Class<?> clazz = bean.getBeanClass();

      if (Resource.class.isAssignableFrom(clazz) && clazz.isAnnotationPresent(ResourceHandles.class))
      {
         for (String pspec : clazz.getAnnotation(ResourceHandles.class).value())
         {
            Pattern p = Pattern.compile(pathspecToRegEx(pspec));
            CreationalContext<?> creationalCtx = manager.createCreationalContext(bean);
            Resource<?> rInst = (Resource<?>) manager.getReference(bean, bean.getBeanClass(), creationalCtx);

            resourceGenerators.add(new ResourceGenerator(p, rInst));
         }
      }
   }
   
   @SuppressWarnings("unchecked")
   public <E, T extends Resource<E>> T createFromType(Class<T> type, E underlyingResource)
   {
      synchronized (this)
      {
         for (ResourceGenerator gen : resourceGenerators)
         {
            Resource<?> resource = gen.getResource();
            if (type.isAssignableFrom(resource.getClass()))
            {
               /*
                * This little <T> hack is required due to bug in javac:
                * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6302954
                */
               T result = (lastTypeLoaded = gen).<T>getResource();
               return (T) result.createFrom(underlyingResource);
            }
         }
      }
      return null;
   }

   public Resource<File> getResourceFrom(File file)
   {
      /**
       * Special case for directories required.
       */
      file = file.getAbsoluteFile();
      if (file.isDirectory())
      {
         return new DirectoryResource(this, file);
      }

      final String name = file.getName();

      synchronized (this)
      {
         if (lastTypeLoaded.matches(name))
         {
            return lastTypeLoaded.getResource(File.class).createFrom(file);
         }

         for (ResourceGenerator gen : resourceGenerators)
         {
            if (gen.matches(name))
            {
               return (lastTypeLoaded = gen).getResource(File.class).createFrom(file);
            }
         }
      }

      return new UnknownFileResource(this, file);
   }

   static class ResourceGenerator
   {
      private Pattern pattern;
      private Resource<?> resource;

      ResourceGenerator()
      {
      }

      ResourceGenerator(final Pattern pattern, final Resource<?> resource)
      {
         this.pattern = pattern;
         this.resource = resource;

      }

      public boolean matches(final String name)
      {
         return pattern.matcher(name).matches();
      }

      @SuppressWarnings("unchecked")
      public <T> T getResource()
      {
         return (T) resource;
      }
      
      @SuppressWarnings({"unchecked"})
      public <T> Resource<T> getResource(final Class<T> type)
      {
         return (Resource<T>) resource;
      }
   }

   private static String pathspecToRegEx(final String pathSpec)
   {
      return "^" + pathSpec.replaceAll("\\*", "\\.\\*").replaceAll("\\?", "\\.") + "$";
   }
}
