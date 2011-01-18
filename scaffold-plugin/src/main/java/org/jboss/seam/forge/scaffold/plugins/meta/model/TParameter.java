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

import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.List;

/**
 * @author Mike Brock .
 */
public class TParameter
{
   private final String name;
   private final TClassType classType;
   private int modifiers;

   public TParameter(String name, TClassType classType)
   {
      this.name = name;
      this.classType = classType;
   }

   public TParameter(String name, TClassType classType, int modifiers)
   {
      this.name = name;
      this.classType = classType;
      this.modifiers = modifiers;
   }

   public String getName()
   {
      return name;
   }

   public TClassType getClassType()
   {
      return classType;
   }

   public int getModifiers()
   {
      return modifiers;
   }

   public void setModifiers(int modifiers)
   {
      this.modifiers = modifiers;
   }

   public static String renderParameterList(List<TParameter> parameterList)
   {
      Iterator<TParameter> iter = parameterList.iterator();
      TParameter p;
      StringBuilder sb = new StringBuilder();
      while (iter.hasNext())
      {
         p = iter.next();

         String modifierString = Modifier.toString(p.getModifiers());

         if (!modifierString.isEmpty()) {
            sb.append(modifierString).append(" ");
         }

         sb.append(p.getClassType().getSimpleName()).append(' ')
               .append(p.getName());

         if (iter.hasNext())
         {
            sb.append(", ");
         }
      }

      return sb.toString();
   }
}
