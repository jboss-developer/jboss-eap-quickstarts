/*
 * JBoss, Home of Professional Open Source
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
package org.jboss.seam.forge.shell.util;

import java.util.Arrays;
import java.util.List;

/**
 * Utility methods for enum types.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class Enums
{
   public static Enum<?> valueOf(final Class<?> type, final Object value)
   {
      List<?> enums = Arrays.asList(type.getEnumConstants());
      for (Object e : enums)
      {
         if (e.toString().equals(value.toString()))
         {
            return (Enum<?>) e;
         }
      }
      return null;
   }

   public static boolean hasValue(final Class<?> type, final Object value)
   {
      return valueOf(type, value) != null;
   }

   public static <T extends Enum<T>> List<T> getValues(final Class<T> type)
   {
      return Arrays.asList(type.getEnumConstants());
   }
}
