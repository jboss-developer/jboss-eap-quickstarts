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

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.text.Document;
import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.parser.java.Method;
import org.jboss.seam.forge.parser.java.SourceType;
import org.jboss.seam.forge.parser.java.ast.ModifierAccessor;
import org.jboss.seam.forge.parser.java.util.Types;

/**
 * Represents a Java Source File
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class JavaClassImpl extends AbstractJavaSourceMemberHolder<JavaClass> implements JavaClass
{
   private final ModifierAccessor modifiers = new ModifierAccessor();

   public JavaClassImpl(final Document document, final CompilationUnit unit)
   {
      super(document, unit);
   }

   @Override
   protected JavaClass updateTypeNames(final String newName)
   {
      for (Method<JavaClass> m : getMethods())
      {
         if (m.isConstructor())
         {
            m.setConstructor(false);
            m.setConstructor(true);
         }
      }
      return this;
   }

   /*
    * Type modifiers
    */
   @Override
   public boolean isAbstract()
   {
      return modifiers.hasModifier(getBodyDeclaration(), ModifierKeyword.ABSTRACT_KEYWORD);
   }

   @Override
   public JavaClass setAbstract(final boolean abstrct)
   {
      if (abstrct)
      {
         modifiers.addModifier(getBodyDeclaration(), ModifierKeyword.ABSTRACT_KEYWORD);
      }
      else
      {
         modifiers.removeModifier(getBodyDeclaration(), ModifierKeyword.ABSTRACT_KEYWORD);
      }
      return this;
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((toString() == null) ? 0 : unit.toString().hashCode());
      return result;
   }

   @Override
   public boolean equals(final Object obj)
   {
      return (this == obj)
               || ((obj != null) && (getClass() == obj.getClass()) && this.toString().equals(obj.toString()));
   }

   @Override
   public String getSuperType()
   {
      Object superType = getBodyDeclaration().getStructuralProperty(TypeDeclaration.SUPERCLASS_TYPE_PROPERTY);
      return superType.toString();
   }

   @Override
   public JavaClass setSuperType(final JavaClass type)
   {
      return setSuperType(type.getQualifiedName());
   }

   @Override
   public JavaClass setSuperType(final Class<?> type)
   {
      if (type.isAnnotation() || type.isEnum() || type.isInterface() || type.isPrimitive())
      {
         throw new IllegalArgumentException("Super-type must be a Class type, but was [" + type.getName() + "]");
      }
      return setSuperType(type.getName());
   }

   @Override
   public JavaClass setSuperType(final String type)
   {
      if (!hasImport(type))
      {
         addImport(type);
      }
      SimpleType simpleType = unit.getAST().newSimpleType(unit.getAST().newSimpleName(Types.toSimpleName(type)));
      getBodyDeclaration().setStructuralProperty(TypeDeclaration.SUPERCLASS_TYPE_PROPERTY, simpleType);
      return this;
   }

   @Override
   public SourceType getSourceType()
   {
      return SourceType.CLASS;
   }
}
