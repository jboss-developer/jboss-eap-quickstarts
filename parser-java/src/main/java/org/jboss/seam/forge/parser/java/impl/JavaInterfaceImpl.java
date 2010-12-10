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
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.text.Document;
import org.jboss.seam.forge.parser.java.Field;
import org.jboss.seam.forge.parser.java.JavaInterface;
import org.jboss.seam.forge.parser.java.Member;
import org.jboss.seam.forge.parser.java.Method;
import org.jboss.seam.forge.parser.java.ast.MethodFinderVisitor;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class JavaInterfaceImpl extends AbstractJavaSource<JavaInterface> implements JavaInterface
{

   public JavaInterfaceImpl(final Document document, final CompilationUnit unit)
   {
      super(document, unit);
   }

   /*
    * Field & Method modifiers
    */
   @Override
   @SuppressWarnings("unchecked")
   public Field<JavaInterface> addField()
   {
      Field<JavaInterface> field = new FieldImpl<JavaInterface>(this);
      getBodyDeclaration().bodyDeclarations().add(field.getInternal());
      return field;
   }

   @Override
   @SuppressWarnings("unchecked")
   public Field<JavaInterface> addField(final String declaration)
   {
      Field<JavaInterface> field = new FieldImpl<JavaInterface>(this, declaration);
      getBodyDeclaration().bodyDeclarations().add(field.getInternal());
      return field;
   }

   @Override
   public List<Field<JavaInterface>> getFields()
   {
      List<Field<JavaInterface>> result = new ArrayList<Field<JavaInterface>>();

      for (FieldDeclaration field : ((TypeDeclaration) getBodyDeclaration()).getFields())
      {
         result.add(new FieldImpl<JavaInterface>(this, field));
      }

      return Collections.unmodifiableList(result);
   }

   @Override
   public Field<JavaInterface> getField(final String name)
   {
      for (Field<JavaInterface> field : getFields())
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
      for (Field<JavaInterface> field : getFields())
      {
         if (field.getName().equals(name))
         {
            return true;
         }
      }
      return false;
   }

   @Override
   public boolean hasField(final Field<JavaInterface> field)
   {
      return getFields().contains(field);
   }

   @Override
   public JavaInterface removeField(final Field<JavaInterface> field)
   {
      getBodyDeclaration().bodyDeclarations().remove(field.getInternal());
      return this;
   }

   @Override
   @SuppressWarnings("unchecked")
   public Method<JavaInterface> addMethod()
   {
      Method<JavaInterface> m = new MethodImpl<JavaInterface>(this);
      getBodyDeclaration().bodyDeclarations().add(m.getInternal());
      return m;
   }

   @Override
   @SuppressWarnings("unchecked")
   public Method<JavaInterface> addMethod(final String method)
   {
      Method<JavaInterface> m = new MethodImpl<JavaInterface>(this, method);
      getBodyDeclaration().bodyDeclarations().add(m.getInternal());
      return m;
   }

   @Override
   public List<Method<JavaInterface>> getMethods()
   {
      List<Method<JavaInterface>> result = new ArrayList<Method<JavaInterface>>();

      MethodFinderVisitor methodFinderVisitor = new MethodFinderVisitor();
      unit.accept(methodFinderVisitor);

      List<MethodDeclaration> methods = methodFinderVisitor.getMethods();
      for (MethodDeclaration methodDeclaration : methods)
      {
         result.add(new MethodImpl<JavaInterface>(this, methodDeclaration));
      }
      return Collections.unmodifiableList(result);
   }

   @Override
   public JavaInterface removeMethod(final Method<JavaInterface> method)
   {
      getBodyDeclaration().bodyDeclarations().remove(method.getInternal());
      return this;
   }

   @Override
   public List<Member<JavaInterface, ?>> getMembers()
   {
      List<Member<JavaInterface, ?>> result = new ArrayList<Member<JavaInterface, ?>>();

      for (Field<JavaInterface> member : getFields())
      {
         result.add(member);
      }
      result.addAll(getFields());
      result.addAll(getMethods());

      return result;
   }

   @Override
   protected JavaInterface updateTypeNames(final String name)
   {
      return this;
   }

}
