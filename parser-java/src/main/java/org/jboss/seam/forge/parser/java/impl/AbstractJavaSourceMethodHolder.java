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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.text.Document;
import org.jboss.seam.forge.parser.java.Field;
import org.jboss.seam.forge.parser.java.FieldHolder;
import org.jboss.seam.forge.parser.java.JavaSource;
import org.jboss.seam.forge.parser.java.Method;
import org.jboss.seam.forge.parser.java.MethodHolder;
import org.jboss.seam.forge.parser.java.Parameter;
import org.jboss.seam.forge.parser.java.ast.MethodFinderVisitor;
import org.jboss.seam.forge.parser.java.util.Strings;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public abstract class AbstractJavaSourceMethodHolder<O extends JavaSource<O>> extends AbstractJavaSource<O> implements
         MethodHolder<O>, FieldHolder<O>
{
   public AbstractJavaSourceMethodHolder(Document document, CompilationUnit unit)
   {
      super(document, unit);
   }

   /*
    * Field & Method modifiers
    */
   @Override
   @SuppressWarnings("unchecked")
   public Field<O> addField()
   {
      Field<O> field = new FieldImpl<O>((O) this);
      getBodyDeclaration().bodyDeclarations().add(field.getInternal());
      return field;
   }

   @Override
   @SuppressWarnings("unchecked")
   public Field<O> addField(final String declaration)
   {
      Field<O> field = new FieldImpl<O>((O) this, declaration);
      getBodyDeclaration().bodyDeclarations().add(field.getInternal());
      return field;
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<Field<O>> getFields()
   {
      List<Field<O>> result = new ArrayList<Field<O>>();

      for (FieldDeclaration field : ((TypeDeclaration) getBodyDeclaration()).getFields())
      {
         result.add(new FieldImpl<O>((O) this, field));
      }

      return Collections.unmodifiableList(result);
   }

   @Override
   public Field<O> getField(final String name)
   {
      for (Field<O> field : getFields())
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
      for (Field<O> field : getFields())
      {
         if (field.getName().equals(name))
         {
            return true;
         }
      }
      return false;
   }

   @Override
   public boolean hasField(final Field<O> field)
   {
      return getFields().contains(field);
   }

   @Override
   @SuppressWarnings("unchecked")
   public O removeField(final Field<O> field)
   {
      getBodyDeclaration().bodyDeclarations().remove(field.getInternal());
      return (O) this;
   }

   @Override
   public boolean hasMethod(final Method<O> method)
   {
      return getMethods().contains(method);
   }

   @Override
   public boolean hasMethodSignature(String name)
   {
      return hasMethodSignature(name, new String[] {});
   }

   @Override
   public boolean hasMethodSignature(String name, String... paramTypes)
   {
      for (Method<?> local : getMethods())
      {
         if (local.getName().equals(name))
         {
            List<Parameter> localParams = local.getParameters();
            if (paramTypes != null && localParams.size() == 0
                     || localParams.size() == paramTypes.length)
            {
               for (int i = 0; i < localParams.size(); i++)
               {
                  Parameter localParam = localParams.get(i);
                  String type = paramTypes[i];
                  if (!Strings.areEqual(localParam.getType(), type))
                  {
                     return false;
                  }
                  // TODO this needs to be able to handle Type to SimpleType comparison
               }
               return true;
            }
         }
      }
      return false;
   }

   @Override
   public boolean hasMethodSignature(String name, Class<?>... paramTypes)
   {
      if (paramTypes == null)
      {
         paramTypes = new Class<?>[] {};
      }

      String[] types = new String[paramTypes.length];
      for (int i = 0; i < paramTypes.length; i++)
      {
         types[i] = paramTypes[i].getName();
      }

      return hasMethodSignature(name, types);
   }

   @Override
   public Method<O> getMethod(final String name)
   {
      for (Method<O> method : getMethods())
      {
         if (method.getName().equals(name) && method.getParameters().size() == 0)
         {
            return method;
         }
      }
      return null;
   }

   @Override
   public Method<O> getMethod(String name, String... paramTypes)
   {
      // TODO Auto-generated method stub
      throw new IllegalStateException("Not yet implemented");
   }

   @Override
   public Method<O> getMethod(String name, Class<?>... paramTypes)
   {
      // TODO Auto-generated method stub
      throw new IllegalStateException("Not yet implemented");
   }

   @Override
   public boolean hasMethodSignature(Method<?> method)
   {
      for (Method<?> local : getMethods())
      {
         if (local.getName().equals(method.getName()))
         {
            List<Parameter> localParams = local.getParameters();
            List<Parameter> methodParams = method.getParameters();
            if (localParams.size() == methodParams.size())
            {
               for (int i = 0; i < localParams.size(); i++)
               {
                  Parameter localParam = localParams.get(i);
                  Parameter methodParam = methodParams.get(i);
                  if (!Strings.areEqual(localParam.getType(), methodParam.getType()))
                  {
                     return false;
                  }
               }
               return true;
            }
         }
      }
      return false;
   }

   @Override
   @SuppressWarnings("unchecked")
   public O removeMethod(final Method<O> method)
   {
      getBodyDeclaration().bodyDeclarations().remove(method.getInternal());
      return (O) this;
   }

   @Override
   @SuppressWarnings("unchecked")
   public Method<O> addMethod()
   {
      Method<O> m = new MethodImpl<O>((O) this);
      getBodyDeclaration().bodyDeclarations().add(m.getInternal());
      return m;
   }

   @Override
   @SuppressWarnings("unchecked")
   public Method<O> addMethod(final String method)
   {
      Method<O> m = new MethodImpl<O>((O) this, method);
      getBodyDeclaration().bodyDeclarations().add(m.getInternal());
      return m;
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<Method<O>> getMethods()
   {
      List<Method<O>> result = new ArrayList<Method<O>>();

      MethodFinderVisitor methodFinderVisitor = new MethodFinderVisitor();
      unit.accept(methodFinderVisitor);

      List<MethodDeclaration> methods = methodFinderVisitor.getMethods();
      for (MethodDeclaration methodDeclaration : methods)
      {
         result.add(new MethodImpl<O>((O) this, methodDeclaration));
      }
      return Collections.unmodifiableList(result);
   }
}
