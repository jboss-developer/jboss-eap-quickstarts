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

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;

import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.ResourceFlag;
import org.jboss.seam.forge.project.resources.builtin.DirectoryResource;
import org.jboss.seam.forge.project.services.ResourceFactory;

public class PathspecParser
{
   private int cursor;
   private final int length;

   private final ResourceFactory factory;
   private final Resource<?> res;
   private final String path;

   List<Resource<?>> results = new LinkedList<Resource<?>>();

   public PathspecParser(final ResourceFactory factory, final Resource<?> res, final String path)
   {
      this.factory = factory;
      this.res = res;
      this.path = path;
      this.length = path.length();
   }

   private PathspecParser(final ResourceFactory factory, final Resource<?> res, final String path, int cursor)
   {
      this.factory = factory;
      this.res = res;
      this.path = path;
      this.length = path.length();
      this.cursor = cursor;
   }

   public List<Resource<?>> resolve()
   {
      Resource<?> r = res;
      String tk;

      if (path.startsWith("~"))
      {
         File homeDir = new File(System.getProperty("user.home")).getAbsoluteFile();

         if (path.length() == 1)
         {
            return singleResult(new DirectoryResource(factory, homeDir));
         }
         else
         {
            cursor++;
            r = new DirectoryResource(factory, homeDir);
         }
      }

      while (cursor < length)
      {
         switch (path.charAt(cursor++))
         {
         case '.':
            if (read() == '.')
            {
               cursor++;
               Resource<?> parent = r.getParent();
               if (parent != null)
               {
                  r = parent;
               }
            }
            break;
         case '/':
            if (cursor - 1 == 0)
            {
               r = factory.getResourceFrom(new File("/"));
            }
            continue;

         default:
//            if (read() == '.')
//            {
//               continue;
//            }

            boolean first = --cursor == 0;

            tk = capture();

            if (tk.contains("*"))
            {
               String regex = pathspecToRegEx(tk.startsWith("/") ? tk.substring(1) : tk);
               Pattern p = Pattern.compile(regex);

               List<Resource<?>> res = new LinkedList<Resource<?>>();

               for (Resource<?> child : r.listResources())
               {
                  if (p.matcher(child.getName()).matches())
                  {
                     child.setFlag(ResourceFlag.AmbiguouslyQualified);
                     res.add(child);
                  }
               }

               if (cursor != length)
               {
                  for (Resource<?> child : res)
                  {
                     results.addAll(new PathspecParser(factory, child, path, cursor).resolve());
                  }
               }
               else
               {
                  results.addAll(res);
               }

               return results;
            }

            if (tk.startsWith("/"))
            {
               if (first)
               {
                  r = factory.getResourceFrom(new File(tk));
                  cursor++;
                  continue;
               }
               else
               {
                  tk = tk.substring(1);
               }
            }

            Resource<?> child = r.getChild(tk);
            if (child == null)
            {
               throw new RuntimeException("no such child: " + child);
            }
            r = child;
            break;
         }
      }

      return singleResult(r);
   }


   public List<Resource<?>> search()
   {
      return match(path.split(String.valueOf(File.separatorChar)), 0, res, new LinkedList<Resource<?>>());
   }

   private static List<Resource<?>> match(String[] matchLevels,
                                          int nestStart,
                                          Resource<?> res,
                                          List<Resource<?>> candidates)
   {
      String regex = pathspecToRegEx(matchLevels[nestStart]);
      Pattern matchPattern = Pattern.compile(regex);

      if (matchPattern.matcher(res.getName()).matches())
      {
         if (nestStart < matchLevels.length && res.isFlagSet(ResourceFlag.Node))
         {
            return match(matchLevels, nestStart + 1, res, candidates);
         }
         else
         {
            candidates.add(res);
            return candidates;
         }
      }

      /**
       * Check to see if this type of node can have children, or if we're exhausted the nest match depth.
       * Otherwise, bail.
       */
      if (!res.isFlagSet(ResourceFlag.Node) || nestStart == matchLevels.length)
      {
         return candidates;
      }

      /**
       * Let's iterate the child nodes.
       */
      for (Resource<?> r : res.listResources())
      {
         match(matchLevels, nestStart, r, candidates);
      }

      return candidates;
   }

   private static List<Resource<?>> singleResult(Resource<?> item)
   {
      return Collections.<Resource<?>>singletonList(item);
   }

   private char read()
   {
      if (cursor != length)
      {
         return path.charAt(cursor);
      }
      return (char) 0;
   }

   private String capture()
   {
      int start = cursor;

      // capture can start with a '/'
      if (path.charAt(cursor) == '/')
      {
         cursor++;
      }

      while ((cursor < length) && (path.charAt(cursor) != '/'))
      {
         cursor++;
      }
      return path.substring(start, cursor);
   }

   private static String pathspecToRegEx(final String pathSpec)
   {
      return "^" + pathSpec.replaceAll("\\*", "\\.\\*").replaceAll("\\?", "\\.") + "$";
   }
}
