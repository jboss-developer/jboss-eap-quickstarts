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

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.forge.parser.java.Field;
import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.parser.java.Method;

/**
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class Refactory
{
   public static void createGetterAndSetter(final JavaClass entity, final Field<JavaClass> field)
   {
      if (!entity.hasField(field))
      {
         throw new IllegalArgumentException("Entity did not contain the given field [" + field + "]");
      }

      entity.getMethods();

      String fieldName = field.getName();
      String methodNameSuffix = Strings.capitalize(fieldName);
      entity.addMethod().setReturnType(field.getType()).setName("get" + methodNameSuffix).setPublic()
               .setBody("return this." + fieldName + ";");
      entity.addMethod().setReturnTypeVoid().setName("set" + methodNameSuffix).setPublic()
               .setParameters("final " + field.getType() + " " + fieldName)
               .setBody("this." + fieldName + " = " + fieldName + ";");
   }

   public static void createToStringFromFields(final JavaClass entity)
   {
      List<Field<JavaClass>> fields = entity.getFields();
      createToStringFromFields(entity, fields);
   }

   public static void createToStringFromFields(final JavaClass entity, final List<Field<JavaClass>> fields)
   {
      Method<JavaClass> method = entity.addMethod().setName("toString").setReturnType(String.class).setPublic();
      List<String> list = new ArrayList<String>();

      String delimeter = "+ \", \" + ";
      for (Field<JavaClass> field : fields)
      {
         if (entity.hasField(field))
         {
            list.add(field.getName());
         }
      }
      String body = "return this.getClass().getSimpleName() + \"[\" + " + Strings.join(list, delimeter) + " + \"]\";";
      method.setBody(body);
   }
}
