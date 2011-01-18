/*
 * JBoss, by Red Hat.
 * Copyright 2011, Red Hat, Inc., and individual contributors
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

package org.jboss.seam.forge.scaffold.plugins.meta.model;

import org.mvel2.util.ParseTools;

/**
 * @author Mike Brock .
 */
public class RenderUtil
{
   public static String tab(int amount)
   {
      if (amount < 1)
      {
         return "";
      }
      char[] str = new char[amount * 4];
      for (int i = str.length - 1; i != -1; i--)
      {
         str[i] = ' ';
      }
      return new String(str);
   }

   public static String tab()
   {
      return tab(1);
   }

   public static String getGetterName(TClassType type, String name)
   {
      String n = name.substring(0, 1).toUpperCase() + name.substring(1);
      if (type.getName().equals("java.lang.Boolean") || type.getName().equals("boolean"))
      {
         return "is" + n;
      }
      else
      {
         return "get" + n;
      }
   }

   public static String getSetterName(String name)
   {
      return "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
   }

   public static String prettyPrint(String input)
   {
      StringBuilder out = new StringBuilder();
      char[] chars = input.toCharArray();

      int tabDepth = 0;
      boolean ig = false;
      int blankLines = 0;

      int start = 0;
      for (int i = 0; i < chars.length; i++)
      {
         switch (chars[i])
         {
         case '\"':
         case '\'':
            i = ParseTools.balancedCapture(chars, start, chars[i]);
            break;
         case '{':
            tabDepth++;
            ig = true;
            break;
         case '}':
            ig = false;
            tabDepth--;
            break;
         case '\n':
            String line = input.substring(start, start = i).trim();

            if (line.isEmpty()) {
               blankLines++;
            }
            else {
               out.append(tab(tabDepth - (ig ? 1 : 0))).append(line);
               blankLines = 0;
            }

            if (blankLines < 2)
            {
               out.append('\n');
            }

            ig = false;
            break;
         }
      }

      if (start < chars.length - 1)
      {
         out.append(tab(tabDepth)).append(input.substring(start, chars.length - 1).trim()).append("\n");
      }

      return out.toString();
   }
}
