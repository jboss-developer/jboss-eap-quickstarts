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
package org.jboss.seam.forge.parser.java.impl;

import java.util.List;

import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.WildcardType;

/**
 * Help with Eclipse JDT common operations.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class JDTHelper
{
   /**
    * Get the name of the given {@link Type}. Return the qualified name if possible.
    */
   public static String getTypeName(final Type type)
   {
      if (type instanceof SimpleType)
      {
         return ((SimpleType) type).getName().toString();
      }
      else if (type instanceof ArrayType)
      {
         return ((ArrayType) type).getStructuralProperty(ArrayType.COMPONENT_TYPE_PROPERTY).toString();
      }
      else if (type instanceof QualifiedType)
      {
         return ((QualifiedType) type).toString();
      }
      else if (type instanceof PrimitiveType)
      {
         return ((PrimitiveType) type).toString();
      }
      else if (type instanceof ParameterizedType)
      {
         return ((ParameterizedType) type).getType().toString();
      }
      else if (type instanceof WildcardType)
      {
         return ((WildcardType) type).getBound().toString();
      }

      return null;
   }

   @SuppressWarnings("unchecked")
   public static List<Type> getInterfaces(final BodyDeclaration dec)
   {
      return (List<Type>) dec.getStructuralProperty(
               TypeDeclaration.SUPER_INTERFACE_TYPES_PROPERTY);
   }

}
