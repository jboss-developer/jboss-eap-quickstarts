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
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.jboss.seam.forge.parser.JavaParser;
import org.jboss.seam.forge.parser.java.Annotation;
import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.parser.java.JavaSource;
import org.jboss.seam.forge.parser.java.Method;
import org.jboss.seam.forge.parser.java.Parameter;
import org.jboss.seam.forge.parser.java.Visibility;
import org.jboss.seam.forge.parser.java.ast.AnnotationAccessor;
import org.jboss.seam.forge.parser.java.ast.ModifierAccessor;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class MethodImpl<O extends JavaSource<O>> implements Method<O>
{
   private final AnnotationAccessor<O, Method<O>> annotations = new AnnotationAccessor<O, Method<O>>();
   private final ModifierAccessor modifiers = new ModifierAccessor();

   private O parent = null;
   private AST ast = null;
   private CompilationUnit cu = null;
   private final MethodDeclaration method;

   private void init(final O parent)
   {
      this.parent = parent;
      cu = (CompilationUnit) parent.getInternal();
      ast = cu.getAST();
   }

   public MethodImpl(final O parent)
   {
      init(parent);
      method = ast.newMethodDeclaration();
      method.setConstructor(false);
   }

   public MethodImpl(final O parent, final Object internal)
   {
      init(parent);
      method = (MethodDeclaration) internal;
   }

   public MethodImpl(final O parent, final String method)
   {
      init(parent);

      String stub = "public class Stub { " + method + " }";
      JavaClass temp = (JavaClass) JavaParser.parse(stub);
      List<Method<JavaClass>> methods = temp.getMethods();
      MethodDeclaration newMethod = (MethodDeclaration) methods.get(0).getInternal();
      MethodDeclaration subtree = (MethodDeclaration) ASTNode.copySubtree(cu.getAST(), newMethod);
      this.method = subtree;
   }

   @Override
   public String toSignature()
   {
      String signature = (Visibility.PACKAGE_PRIVATE.equals(this.getVisibility().scope()) ? "" : this.getVisibility()
               .scope()) + " ";
      signature += this.getName() + "(";

      List<Parameter> parameters = this.getParameters();
      for (Parameter p : parameters)
      {
         signature += p.getType();
         if (parameters.indexOf(p) < parameters.size() - 1)
         {
            signature += ", ";
         }
      }

      signature += ") : " + (this.getReturnType() == null ? "null" : this.getReturnType());
      return signature;
   }

   /*
    * Annotation<O> Modifiers
    */

   @Override
   public Annotation<O> addAnnotation()
   {
      return annotations.addAnnotation(this, method);
   }

   @Override
   public Annotation<O> addAnnotation(final Class<? extends java.lang.annotation.Annotation> clazz)
   {
      if (!parent.hasImport(clazz))
      {
         parent.addImport(clazz);
      }
      return annotations.addAnnotation(this, method, clazz.getSimpleName());
   }

   @Override
   public Annotation<O> addAnnotation(final String className)
   {
      return annotations.addAnnotation(this, method, className);
   }

   @Override
   public List<Annotation<O>> getAnnotations()
   {
      return annotations.getAnnotations(this, method);
   }

   @Override
   public boolean hasAnnotation(final Class<? extends java.lang.annotation.Annotation> type)
   {
      return annotations.hasAnnotation(this, method, type.getName());
   }

   @Override
   public boolean hasAnnotation(final String type)
   {
      return annotations.hasAnnotation(this, method, type);
   }

   @Override
   public Method<O> removeAnnotation(final Annotation<O> annotation)
   {
      return annotations.removeAnnotation(this, method, annotation);
   }

   @Override
   public Annotation<O> getAnnotation(final Class<? extends java.lang.annotation.Annotation> type)
   {
      return annotations.getAnnotation(this, method, type);
   }

   @Override
   public Annotation<O> getAnnotation(final String type)
   {
      return annotations.getAnnotation(this, method, type);
   }

   /*
    * Method<O> Modifiers
    */

   @Override
   @SuppressWarnings("unchecked")
   public String getBody()
   {
      String result = "";

      List<Statement> statements = (List<Statement>) method.getBody().getStructuralProperty(Block.STATEMENTS_PROPERTY);
      for (Statement statement : statements)
      {
         result += statement + " ";
      }

      return result;
   }

   @Override
   public Method<O> setBody(final String body)
   {
      String stub = "public class Stub { public void method() {" + body + "} }";
      JavaClass temp = (JavaClass) JavaParser.parse(stub);
      List<Method<JavaClass>> methods = temp.getMethods();
      Block block = ((MethodDeclaration) methods.get(0).getInternal()).getBody();

      block = (Block) ASTNode.copySubtree(method.getAST(), block);
      method.setBody(block);

      return this;
   }

   @Override
   public Method<O> setConstructor(final boolean constructor)
   {
      method.setConstructor(constructor);
      if (isConstructor())
      {
         method.setName(ast.newSimpleName(parent.getName()));
      }
      return this;
   }

   @Override
   public boolean isConstructor()
   {
      return method.isConstructor();
   }

   @Override
   public String getReturnType()
   {
      String result = null;
      if (!isConstructor() && (method.getReturnType2() != null))
      {
         result = method.getReturnType2().toString();
      }
      return result;
   }

   @Override
   public Method<O> setReturnType(final Class<?> type)
   {
      return setReturnType(type.getSimpleName());
   }

   @Override
   public Method<O> setReturnType(final String typeName)
   {
      String stub = "public class Stub { public " + typeName + " method() {} }";
      JavaClass temp = (JavaClass) JavaParser.parse(stub);
      List<Method<JavaClass>> methods = temp.getMethods();
      Type returnType = ((MethodDeclaration) methods.get(0).getInternal()).getReturnType2();

      returnType = (Type) ASTNode.copySubtree(method.getAST(), returnType);
      method.setReturnType2(returnType);

      return this;
   }

   @Override
   public boolean isReturnTypeVoid()
   {
      return getReturnType() == null;
   }

   @Override
   public Method<O> setReturnTypeVoid()
   {
      method.setReturnType2(null);
      return this;
   }

   /*
    * Abstract Modifiers
    */

   @Override
   public boolean isAbstract()
   {
      return modifiers.hasModifier(method, ModifierKeyword.ABSTRACT_KEYWORD);
   }

   @Override
   public Method<O> setAbstract(final boolean abstrct)
   {
      if (abstrct)
      {
         modifiers.addModifier(method, ModifierKeyword.ABSTRACT_KEYWORD);
      }
      else
      {
         modifiers.removeModifier(method, ModifierKeyword.ABSTRACT_KEYWORD);
      }
      return this;
   }

   @Override
   public Method<O> setFinal()
   {
      modifiers.addModifier(method, ModifierKeyword.FINAL_KEYWORD);
      return this;
   }

   @Override
   public String getName()
   {
      return method.getName().getFullyQualifiedName();
   }

   @Override
   public Method<O> setName(final String name)
   {
      if (method.isConstructor())
      {
         throw new IllegalStateException("Cannot set the name of a constructor.");
      }
      method.setName(ast.newSimpleName(name));
      return this;
   }

   @Override
   @SuppressWarnings("unchecked")
   public Method<O> setParameters(final String parameters)
   {
      String stub = "public class Stub { public void method( " + parameters + " ) {} }";
      JavaClass temp = (JavaClass) JavaParser.parse(stub);
      List<Method<JavaClass>> methods = temp.getMethods();
      List<VariableDeclaration> astParameters = ((MethodDeclaration) methods.get(0).getInternal()).parameters();

      method.parameters().clear();
      for (VariableDeclaration declaration : astParameters)
      {
         VariableDeclaration copy = (VariableDeclaration) ASTNode.copySubtree(method.getAST(), declaration);
         method.parameters().add(copy);
      }

      return this;
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<Parameter> getParameters()
   {
      List<Parameter> results = new ArrayList<Parameter>();
      List<VariableDeclaration> parameters = method.parameters();
      for (VariableDeclaration param : parameters)
      {
         results.add(new ParameterImpl(param));
      }
      return results;
   }

   /*
    * Visibility Modifiers
    */

   @Override
   public boolean isPackagePrivate()
   {
      return (!isPublic() && !isPrivate() && !isProtected());
   }

   @Override
   public Method<O> setPackagePrivate()
   {
      modifiers.clearVisibility(method);
      return this;
   }

   @Override
   public boolean isPublic()
   {
      return modifiers.hasModifier(method, ModifierKeyword.PUBLIC_KEYWORD);
   }

   @Override
   public Method<O> setPublic()
   {
      modifiers.clearVisibility(method);
      modifiers.addModifier(method, ModifierKeyword.PUBLIC_KEYWORD);
      return this;
   }

   @Override
   public boolean isPrivate()
   {
      return modifiers.hasModifier(method, ModifierKeyword.PRIVATE_KEYWORD);
   }

   @Override
   public Method<O> setPrivate()
   {
      modifiers.clearVisibility(method);
      modifiers.addModifier(method, ModifierKeyword.PRIVATE_KEYWORD);
      return this;
   }

   @Override
   public boolean isProtected()
   {
      return modifiers.hasModifier(method, ModifierKeyword.PROTECTED_KEYWORD);
   }

   @Override
   public Method<O> setProtected()
   {
      modifiers.clearVisibility(method);
      modifiers.addModifier(method, ModifierKeyword.PROTECTED_KEYWORD);
      return this;
   }

   @Override
   public Visibility getVisibility()
   {
      return Visibility.getFrom(this);
   }

   @Override
   public Method<O> setVisibility(final Visibility scope)
   {
      return Visibility.set(this, scope);
   }

   /*
    * Interfaces
    */

   @Override
   public String toString()
   {
      return method.toString();
   }

   @Override
   public Object getInternal()
   {
      return method;
   }

   @Override
   public O getOrigin()
   {
      return parent.getOrigin();
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((method == null) ? 0 : method.hashCode());
      return result;
   }

   @Override
   public boolean equals(final Object obj)
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
      MethodImpl<?> other = (MethodImpl<?>) obj;
      if (method == null)
      {
         if (other.method != null)
         {
            return false;
         }
      }
      else if (!method.equals(other.method))
      {
         return false;
      }
      return true;
   }
}
