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

package org.jboss.seam.forge.parser.java.impl;

import org.jboss.seam.forge.parser.java.ValuePair;
import org.jboss.seam.forge.parser.java.util.Strings;

/**
 * Represents an annotation value pair
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class ValuePairImpl implements ValuePair
{
   private final String name;
   private final String value;

   public ValuePairImpl(String name, String value)
   {
      this.name = name;
      this.value = value;
   }

   @Override
   public String getName()
   {
      return name;
   }

   @Override
   public String getLiteralValue()
   {
      return value;
   }

   @Override
   public String getStringValue()
   {
      return Strings.unquote(getLiteralValue());
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((name == null) ? 0 : name.hashCode());
      result = prime * result + ((value == null) ? 0 : value.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (obj == null)
      {
         return false;
      }
      if (getClass() != obj.getClass())
      {
         return false;
      }
      ValuePairImpl other = (ValuePairImpl) obj;
      if (name == null)
      {
         if (other.name != null)
         {
            return false;
         }
      }
      else if (!name.equals(other.name))
      {
         return false;
      }
      if (value == null)
      {
         if (other.value != null)
         {
            return false;
         }
      }
      else if (!value.equals(other.value))
      {
         return false;
      }
      return true;
   }

}
