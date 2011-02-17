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

package org.jboss.seam.forge.project.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.jboss.seam.forge.parser.java.util.Strings;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.facets.JavaSourceFacet;
import org.jboss.seam.forge.project.resources.ResourceFilter;
import org.jboss.seam.forge.project.resources.builtin.DirectoryResource;
import org.jboss.seam.forge.project.resources.builtin.UnknownFileResource;
import org.jboss.seam.forge.project.services.ResourceFactory;

/**
 * Parser accepts a Java path, and provides a result set of resources based on the relative path provided.
 * <p/>
 * 
 * Example:<br/>
 * <code>
 *    List<Resource<?>> res = new PathspecParser(factoryInstance, javaSourceFacet, "~.project.shell");
 * </code>
 * 
 * Where <tt>factoryInstance</tt> is an instance of {@link ResourceFactory}, <tt>javaSourceFacet</tt> is a
 * {@link JavaSourceFacet} instance, from the current project, with which the relative result for <tt>~.*</tt> will be
 * calculated.
 * <p/>
 * 
 * Wildcards <tt>*</tt> and <tt>?</tt> are accepted.
 * 
 * @author Lincoln Baxter
 */
public class JavaPathspecParser
{
   private final JavaSourceFacet java;
   private final String path;

   List<Resource<?>> results = new LinkedList<Resource<?>>();

   public JavaPathspecParser(JavaSourceFacet java, final String path)
   {
      this.java = java;
      this.path = path == null ? "" : path.trim();
   }

   /**
    * Resolve the results.
    * 
    * @return A list of resources that match the path. Empty if there are no matches.
    */
   public List<Resource<?>> resolve(ResourceFilter filter)
   {
      Resource<?> r = java.getSourceFolder();

      String p = path;

      if (path.startsWith("~"))
      {
         DirectoryResource base = java.getBasePackageResource();

         if (path.length() == 1)
         {
            if (filter.accept(base))
            {
               return singleResult(base);
            }
         }
         else
         {
            r = base;
         }

         p = p.substring(1);
         if (p.startsWith("."))
         {
            p = p.substring(1);
         }
      }

      String[] split = p.split("\\.");
      for (int i = 0; i < split.length; i++)
      {
         String s = split[i];
         if (i == split.length - 1)
         {
            while (s.endsWith("*"))
            {
               s = s.substring(0, s.lastIndexOf("*"));
            }

            if (s.isEmpty())
            {
               results.addAll(r.listResources(filter));
               break;
            }
            else
            {
               List<Resource<?>> resources = r.listResources();
               for (Resource<?> resource : resources)
               {
                  if (resource.getName().startsWith(s))
                  {
                     if (filter.accept(resource))
                     {
                        results.add(resource);
                     }
                  }
               }
               break;
            }
         }

         Resource<?> child = r.getChild(s);
         if (child instanceof UnknownFileResource)
         {
            String[] join = new String[split.length - i];
            System.arraycopy(split, i, join, 0, join.length);
            String joined = Strings.join(Arrays.asList(join), ".");
            child = r.getChild(joined);
            if (filter.accept(child))
            {
               results.add(child);
            }
            break;
         }
         if (child != null)
         {
            r = child;
         }
         else
         {
            break;
         }
      }
      return results;
   }

   private static List<Resource<?>> singleResult(Resource<?> item)
   {
      return Collections.<Resource<?>> singletonList(item);
   }

   public List<? extends Resource<?>> resolve()
   {
      return resolve(new ResourceFilter()
      {
         @Override
         public boolean accept(Resource<?> resource)
         {
            return true;
         }
      });
   }
}
