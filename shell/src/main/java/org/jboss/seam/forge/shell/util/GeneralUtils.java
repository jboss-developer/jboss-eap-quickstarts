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

package org.jboss.seam.forge.shell.util;

import org.jboss.seam.forge.shell.Shell;

import java.util.*;

public class GeneralUtils
{
   public static <T> List<T> concatArraysToList(T[]... arrays)
   {
      List<T> newList = new ArrayList<T>();
      for (T[] elArray : arrays)
      {
         newList.addAll(Arrays.asList(elArray));
      }

      return newList;
   }

   public static String elementListSimpleTypesToString(List<Class> list)
   {
      StringBuilder sbuild = new StringBuilder();
      for (int i = 0; i < list.size(); i++)
      {
         sbuild.append(list.get(0).getSimpleName());
         if (i < list.size()) sbuild.append(", ");
      }
      return sbuild.toString();
   }

   public static String elementSetSimpleTypesToString(Set<Class> set)
   {
      StringBuilder sbuild = new StringBuilder();

      for (Iterator<Class> iter = set.iterator(); iter.hasNext();)
      {
         sbuild.append(iter.next().getSimpleName());
         if (iter.hasNext()) sbuild.append(", ");
      }
      return sbuild.toString();
   }

   public static void printOutColumns(List<String> rawList, Shell shell, boolean sort)
   {
      printOutColumns(rawList, null, ShellColor.NONE, shell, sort);
   }

   public static void printOutColumns(List<String> rawList, List<String> coloredList, Shell shell, boolean sort)
   {
      printOutColumns(rawList, coloredList, ShellColor.NONE, shell, sort);
   }

   public static void printOutColumns(List<String> rawList, List<String> coloredList, ShellColor color, Shell shell, boolean sort)
   {
      int width = shell.getWidth();
      int maxLength = 0;

      for (String s : rawList)
      {
         if (s.length() > maxLength) maxLength = s.length();
      }

      if (sort)
      {
         Collections.sort(rawList);
      }

      int cols = width / (maxLength + 4);
      int colSize = width / cols;

      if (cols == 0)
      {
         colSize = width;
         cols = 1;
      }

      int i = 0;
      int count = 0;
      for (String s : rawList)
      {
         String out;
         if (color == ShellColor.NONE)
         {
            out = coloredList != null ? coloredList.get(count) : s;
            shell.print(out);
         }
         else
         {
            out = coloredList != null ? coloredList.get(count) : s;
            shell.print(color, out);
         }

         shell.print(pad(colSize - s.length()));
         if (++i == cols)
         {
            shell.println();
            i = 0;
         }
         count++;
      }
      shell.println();
   }

   public static void printOutTables(List<String> list, boolean[] columns, Shell shell)
   {
      int cols = columns.length;
      int[] colSizes = new int[columns.length];

      Iterator<String> iter = list.iterator();

      String el;
      while (iter.hasNext())
      {
         for (int i = 0; i < cols; i++)
         {
            if (colSizes[i] < (el = iter.next()).length())
               colSizes[i] = el.length();
         }
      }

      iter = list.iterator();

      while (iter.hasNext())
      {
         for (int i = 0; i < cols; i++)
         {
            el = iter.next();
            if (columns[i])
            {
               shell.print(pad(colSizes[i] - el.length()));
               shell.print(el);
            }
            else
            {
               shell.print(el);
               shell.print(pad(colSizes[i] - el.length()));
            }
            shell.print(" ");
         }
         shell.println();
      }
   }

   public static String pad(final int amount)
   {
      char[] padding = new char[amount];
      for (int i = 0; i < amount; i++)
      {
         padding[i] = ' ';
      }
      return new String(padding);
   }

}
