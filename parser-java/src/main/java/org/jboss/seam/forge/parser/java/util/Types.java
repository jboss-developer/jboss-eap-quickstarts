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

package org.jboss.seam.forge.parser.java.util;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class Types
{
   public static boolean areEquivalent(final String longType, final String type)
   {
      String shortType = longType.replaceFirst("^(.*\\.)?([^.]+)$", "$2");
      return shortType.equals(type) ||
            longType.equals(type);
   }

   public static String toSimpleName(final String fieldType)
   {
      String result = fieldType;
      if (result != null)
      {
         String[] tokens = tokenizeClassName(result);
         if (tokens != null)
         {
            result = tokens[tokens.length - 1];
         }
      }
      return result;
   }

   public static String[] tokenizeClassName(final String className)
   {
      String[] result = null;
      if (className != null)
      {
         result = className.split("\\.");
      }
      return result;
   }

   public static boolean isQualified(String className)
   {
      String[] tokens = tokenizeClassName(className);
      return (tokens != null) && (tokens.length > 1);
   }
}
