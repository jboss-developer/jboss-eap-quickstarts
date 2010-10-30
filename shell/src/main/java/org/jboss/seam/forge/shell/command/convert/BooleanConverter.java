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

package org.jboss.seam.forge.shell.command.convert;

import static java.lang.String.valueOf;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mvel2.ConversionException;
import org.mvel2.ConversionHandler;
import org.mvel2.conversion.Converter;

/**
 * User: christopherbrock Date: 31-Aug-2010 Time: 10:58:26 PM
 */
public class BooleanConverter implements ConversionHandler
{
   private static final Map<Class, Converter> CNV =
         new HashMap<Class, Converter>();

   private static final Set<Object> TRUE = new HashSet<Object>();
   private static final Set<Object> FALSE = new HashSet<Object>();

   static
   {
      TRUE.add("true");
      TRUE.add("yes");
      TRUE.add("y");
      TRUE.add("on");

      FALSE.add("false");
      FALSE.add("no");
      FALSE.add("n");
      FALSE.add("off");
   }

   private static Converter stringConverter = new Converter()
   {
      @Override
      public Object convert(Object o)
      {
         final String s = ((String) o).toLowerCase();
         if (TRUE.contains(s))
         {
            return true;
         }
         else if (FALSE.contains(s))
         {
            return false;
         }
         throw new IllegalArgumentException("not a valid boolean: '" + s + "'");
      }
   };

   @Override
   public Object convertFrom(Object in)
   {
      if (!CNV.containsKey(in.getClass()))
      {
         throw new ConversionException("cannot convert type: "
               + in.getClass().getName() + " to: " + Boolean.class.getName());
      }
      return CNV.get(in.getClass()).convert(in);
   }

   @Override
   public boolean canConvertFrom(Class cls)
   {
      return CNV.containsKey(cls);
   }

   static
   {
      CNV.put(String.class,
            stringConverter
            );

      CNV.put(Object.class,
            new Converter()
            {
               @Override
               public Object convert(Object o)
               {
                  return stringConverter.convert(valueOf(o));
               }
            }
            );

      CNV.put(Boolean.class,
            new Converter()
            {
               @Override
               public Object convert(Object o)
               {
                  return o;
               }
            }
            );

      CNV.put(Integer.class,
            new Converter()
            {
               @Override
               public Boolean convert(Object o)
               {
                  return (((Integer) o) > 0);
               }
            }
            );

      CNV.put(Float.class,
            new Converter()
            {
               @Override
               public Boolean convert(Object o)
               {
                  return (((Float) o) > 0);
               }
            }
            );

      CNV.put(Double.class,
            new Converter()
            {
               @Override
               public Boolean convert(Object o)
               {
                  return (((Double) o) > 0);
               }
            }
            );

      CNV.put(Short.class,
            new Converter()
            {
               @Override
               public Boolean convert(Object o)
               {
                  return (((Short) o) > 0);
               }
            }
            );

      CNV.put(Long.class,
            new Converter()
            {
               @Override
               public Boolean convert(Object o)
               {
                  return (((Long) o) > 0);
               }
            }
            );

      CNV.put(boolean.class,
            new Converter()
            {

               @Override
               public Boolean convert(Object o)
               {
                  return Boolean.valueOf((Boolean) o);
               }
            }
            );

      CNV.put(BigDecimal.class,
            new Converter()
            {

               @Override
               public Boolean convert(Object o)
               {
                  return Boolean.valueOf(((BigDecimal) o).doubleValue() > 0);
               }
            }
            );

   }
}
