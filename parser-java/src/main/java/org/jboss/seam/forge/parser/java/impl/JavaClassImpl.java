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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.text.Document;
import org.jboss.seam.forge.parser.java.Field;
import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.parser.java.JavaType;
import org.jboss.seam.forge.parser.java.Member;
import org.jboss.seam.forge.parser.java.Method;
import org.jboss.seam.forge.parser.java.ast.MethodFinderVisitor;
import org.jboss.seam.forge.parser.java.ast.ModifierAccessor;
import org.jboss.seam.forge.parser.java.util.Types;

/**
 * Represents a Java Source File
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class JavaClassImpl extends AbstractJavaSource<JavaClass> implements JavaClass
{
   private final ModifierAccessor modifiers = new ModifierAccessor();

   public JavaClassImpl(final Document document, final CompilationUnit unit)
   {
      super(document, unit);
   }

   /*
    * Field & Method modifiers
    */
   @Override
   @SuppressWarnings("unchecked")
   public Field<JavaClass> addField()
   {
      Field<JavaClass> field = new FieldImpl<JavaClass>(this);
      getBodyDeclaration().bodyDeclarations().add(field.getInternal());
      return field;
   }

   @Override
   @SuppressWarnings("unchecked")
   public Field<JavaClass> addField(final String declaration)
   {
      Field<JavaClass> field = new FieldImpl<JavaClass>(this, declaration);
      getBodyDeclaration().bodyDeclarations().add(field.getInternal());
      return field;
   }

   @Override
   public List<Field<JavaClass>> getFields()
   {
      List<Field<JavaClass>> result = new ArrayList<Field<JavaClass>>();

      for (FieldDeclaration field : ((TypeDeclaration) getBodyDeclaration()).getFields())
      {
         result.add(new FieldImpl<JavaClass>(this, field));
      }

      return Collections.unmodifiableList(result);
   }

   @Override
   public Field<JavaClass> getField(final String name)
   {
      for (Field<JavaClass> field : getFields())
      {
         if (field.getName().equals(name))
         {
            return field;
         }
      }
      return null;
   }

   @Override
   public boolean hasField(final String name)
   {
      for (Field<JavaClass> field : getFields())
      {
         if (field.getName().equals(name))
         {
            return true;
         }
      }
      return false;
   }

   @Override
   public boolean hasField(final Field<JavaClass> field)
   {
      return getFields().contains(field);
   }

   @Override
   public JavaClass removeField(final Field<JavaClass> field)
   {
      getBodyDeclaration().bodyDeclarations().remove(field.getInternal());
      return this;
   }

   @Override
   @SuppressWarnings("unchecked")
   public Method<JavaClass> addMethod()
   {
      Method<JavaClass> m = new MethodImpl<JavaClass>(this);
      getBodyDeclaration().bodyDeclarations().add(m.getInternal());
      return m;
   }

   @Override
   @SuppressWarnings("unchecked")
   public Method<JavaClass> addMethod(final String method)
   {
      Method<JavaClass> m = new MethodImpl<JavaClass>(this, method);
      getBodyDeclaration().bodyDeclarations().add(m.getInternal());
      return m;
   }

   @Override
   public boolean hasMethod(String name)
   {
      for (Method<JavaClass> method : getMethods())
      {
         if (method.getName().equals(name))
         {
            return true;
         }
      }
      return false;
   }

   @Override
   public Method<JavaClass> getMethod(String name)
   {
      for (Method<JavaClass> method : getMethods())
      {
         if (method.getName().equals(name))
         {
            return method;
         }
      }
      return null;
   }

   @Override
   public List<Method<JavaClass>> getMethods()
   {
      List<Method<JavaClass>> result = new ArrayList<Method<JavaClass>>();

      MethodFinderVisitor methodFinderVisitor = new MethodFinderVisitor();
      unit.accept(methodFinderVisitor);

      List<MethodDeclaration> methods = methodFinderVisitor.getMethods();
      for (MethodDeclaration methodDeclaration : methods)
      {
         result.add(new MethodImpl<JavaClass>(this, methodDeclaration));
      }
      return Collections.unmodifiableList(result);
   }

   @Override
   public JavaClass removeMethod(final Method<JavaClass> method)
   {
      getBodyDeclaration().bodyDeclarations().remove(method.getInternal());
      return this;
   }

   @Override
   public List<Member<JavaClass, ?>> getMembers()
   {
      List<Member<JavaClass, ?>> result = new ArrayList<Member<JavaClass, ?>>();

      for (Field<JavaClass> member : getFields())
      {
         result.add(member);
      }
      result.addAll(getFields());
      result.addAll(getMethods());

      return result;
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
      return this == obj || obj != null && getClass() == obj.getClass() && this.toString().equals(obj.toString());
   }

   @Override
   public String getSuperType()
   {
      Object superType = getBodyDeclaration().getStructuralProperty(TypeDeclaration.SUPERCLASS_TYPE_PROPERTY);
      return superType.toString();
   }

   @Override
   public <T extends JavaType<?>> JavaClass setSuperType(T type)
   {
      return setSuperType(type.getQualifiedName());
   }

   @Override
   public JavaClass setSuperType(Class<?> type)
   {
      return setSuperType(type.getName());
   }

   @Override
   public JavaClass setSuperType(String type)
   {
      if (!hasImport(type))
      {
         addImport(type);
      }
      SimpleType simpleType = unit.getAST().newSimpleType(unit.getAST().newSimpleName(Types.toSimpleName(type)));
       getBodyDeclaration().setStructuralProperty(TypeDeclaration.SUPERCLASS_TYPE_PROPERTY, simpleType);
      return this;
   }
}
