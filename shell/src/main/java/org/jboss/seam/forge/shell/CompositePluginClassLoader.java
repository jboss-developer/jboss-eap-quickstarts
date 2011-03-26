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

package org.jboss.seam.forge.shell;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CompositePluginClassLoader extends ClassLoader
{
   private final List<ClassLoader> classLoaders = Collections.synchronizedList(new ArrayList<ClassLoader>());

   public CompositePluginClassLoader()
   {
   }

   public CompositePluginClassLoader(ClassLoader parent)
   {
      super(parent);
   }

   /**
    * Add a loader to the internal List of loaders. Loaders will be used in the reverse order from which they were
    * added.
    */
   private void addFront(ClassLoader loader)
   {
      if (loader != null && !classLoaders.contains(loader) && !this.equals(loader))
      {
         classLoaders.add(0, loader);
      }
   }

   public void addStandard(ClassLoader loader)
   {
      if (loader instanceof PluginClassLoader)
      {
         add((PluginClassLoader) loader);
      }
      else
      {
         addFront(loader);
      }
   }

   // prevent duplicates
   public void add(PluginClassLoader loader)
   {
      if (!contains(loader))
      {
         addFront(loader);
      }
      else
      {
         for (ClassLoader cl : classLoaders)
         {
            if (cl instanceof PluginClassLoader)
            {
               PluginClassLoader curr = (PluginClassLoader) cl;
               if (curr.getPluginName().equals(loader.getPluginName()))
               {
                  if (curr.getPluginVersion() < loader.getPluginVersion())
                  {
                     addFront(loader);
                     remove(curr);
                  }
                  return;
               }
            }
         }
      }
   }

   public boolean remove(PluginClassLoader loader)
   {
      if (classLoaders.remove(loader))
      {
         return true;
      }

      for (ClassLoader cl : classLoaders)
      {
         if (loader instanceof PluginClassLoader)
         {
            if (((PluginClassLoader) cl).getPluginName().equals(loader.getPluginName()))
            {
               return classLoaders.remove(cl);
            }
         }
      }
      return false;
   }

   public boolean contains(PluginClassLoader loader)
   {
      if (classLoaders.contains(loader))
      {
         return true;
      }

      for (ClassLoader cl : classLoaders)
      {
         if (cl instanceof PluginClassLoader)
         {
            if (((PluginClassLoader) cl).getPluginName().equals(loader.getPluginName()))
            {
               return true;
            }
         }
      }
      return false;
   }

   /**
    * Get an unmodifiable view of this Loader's internal list.
    */
   protected List<ClassLoader> getClassLoaders()
   {
      return Collections.unmodifiableList(classLoaders);
   }

   /*
    * Overrides
    */
   @Override
   public Class<?> loadClass(String name) throws ClassNotFoundException
   {
      for (ClassLoader loader : classLoaders)
      {
         try
         {
            return loader.loadClass(name);
         }
         catch (ClassNotFoundException e)
         {
            // I don't care yet...
         }
      }
      return super.loadClass(name);
   }

   @Override
   public synchronized void clearAssertionStatus()
   {
      for (ClassLoader loader : classLoaders)
      {
         loader.clearAssertionStatus();
      }
      super.clearAssertionStatus();
   }

   @Override
   public synchronized void setClassAssertionStatus(String className, boolean enabled)
   {
      for (ClassLoader loader : classLoaders)
      {
         loader.setClassAssertionStatus(className, enabled);
      }
      super.setClassAssertionStatus(className, enabled);
   }

   @Override
   public synchronized void setDefaultAssertionStatus(boolean enabled)
   {
      for (ClassLoader loader : classLoaders)
      {
         loader.setDefaultAssertionStatus(enabled);
      }
      super.setDefaultAssertionStatus(enabled);
   }

   @Override
   public synchronized void setPackageAssertionStatus(String packageName, boolean enabled)
   {
      for (ClassLoader loader : classLoaders)
      {
         loader.setPackageAssertionStatus(packageName, enabled);
      }
      super.setPackageAssertionStatus(packageName, enabled);
   }

   @Override
   public URL getResource(String name)
   {
      for (ClassLoader loader : classLoaders)
      {
         URL resource = loader.getResource(name);
         if (resource != null)
            return resource;
      }
      return super.getResource(name);
   }

   @Override
   public InputStream getResourceAsStream(String name)
   {
      for (ClassLoader loader : classLoaders)
      {
         InputStream stream = loader.getResourceAsStream(name);
         if (stream != null)
         {
            return stream;
         }
      }
      return super.getResourceAsStream(name);
   }

   @Override
   public Enumeration<URL> getResources(String name) throws IOException
   {
      Set<URL> urls = new HashSet<URL>();
      for (ClassLoader loader : classLoaders)
      {
         Enumeration<URL> resources = loader.getResources(name);
         while (resources.hasMoreElements())
         {
            urls.add(resources.nextElement());
         }
      }

      Enumeration<URL> resources = super.getResources(name);
      while (resources.hasMoreElements())
      {
         urls.add(resources.nextElement());
      }
      return Collections.enumeration(urls);
   }

   @Override
   public String toString()
   {
      return "CompositePluginClassLoader [classLoaders=" + classLoaders + "]";
   }
}
