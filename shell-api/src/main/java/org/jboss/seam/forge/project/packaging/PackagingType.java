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

package org.jboss.seam.forge.project.packaging;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public enum PackagingType
{
   NONE("", "None"),
   BASIC("pom", "Basic Project"),
   JAR("jar", "Java Application"),
   WAR("war", "Java Web Application"),
   OTHER("", "Other packaging type");

   private String type;
   private String description;

   private PackagingType(final String type, final String description)
   {
      setType(type);
      setDescription(description);
   }

   public String getType()
   {
      return type;
   }

   private void setType(String type)
   {
      if (type != null)
      {
         type = type.trim().toLowerCase();
      }
      this.type = type;
   }

   @Override
   public String toString()
   {
      return type;
   }

   public String getDescription()
   {
      return description;
   }

   private void setDescription(final String description)
   {
      this.description = description;
   }

   public static PackagingType from(String type)
   {
      PackagingType result = OTHER;
      if ((type != null) && !type.trim().isEmpty())
      {
         type = type.trim();
         for (PackagingType p : values())
         {
            if (p.getType().equals(type) || p.name().equalsIgnoreCase(type))
            {
               result = p;
               break;
            }
         }
      }
      return result;
   }
}
