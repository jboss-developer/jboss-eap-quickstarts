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
package org.jboss.seam.sidekick.parser.java.impl;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.Statement;
import org.jboss.seam.sidekick.parser.JavaParser;
import org.jboss.seam.sidekick.parser.java.Annotation;
import org.jboss.seam.sidekick.parser.java.JavaClass;
import org.jboss.seam.sidekick.parser.java.Method;
import org.jboss.seam.sidekick.parser.java.ast.AnnotationAccessor;
import org.jboss.seam.sidekick.parser.java.ast.ModifierAccessor;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class MethodImpl implements Method
{
   private static AnnotationAccessor util = new AnnotationAccessor();
   private final ModifierAccessor ma = new ModifierAccessor();

   private JavaClass parent = null;
   private AST ast = null;
   private CompilationUnit cu = null;
   private final MethodDeclaration method;

   private void init(final JavaClass parent)
   {
      this.parent = parent;
      cu = (CompilationUnit) parent.getInternal();
      ast = cu.getAST();
   }

   public MethodImpl(final JavaClass parent)
   {
      init(parent);
      method = ast.newMethodDeclaration();
      method.setConstructor(false);
   }

   public MethodImpl(final JavaClass parent, final Object internal)
   {
      init(parent);
      method = (MethodDeclaration) internal;
   }

   public MethodImpl(final JavaClass parent, final String method)
   {
      init(parent);

      String stub = "public class Stub { " + method + " }";
      JavaClass temp = JavaParser.parse(stub);
      List<Method> methods = temp.getMethods();
      MethodDeclaration newMethod = (MethodDeclaration) methods.get(0).getInternal();
      MethodDeclaration subtree = (MethodDeclaration) ASTNode.copySubtree(cu.getAST(), newMethod);
      this.method = subtree;
   }

   /*
    * Annotation Modifiers
    */

   @Override
   public Annotation addAnnotation()
   {
      return util.addAnnotation(this, method);
   }

   @Override
   public Annotation addAnnotation(Class<?> clazz)
   {
      return util.addAnnotation(this, method, clazz);
   }

   @Override
   public Annotation addAnnotation(final String className)
   {
      return util.addAnnotation(this, method, className);
   }

   @Override
   public List<Annotation> getAnnotations()
   {
      return util.getAnnotations(this, method);
   }

   @Override
   public Method removeAnnotation(Annotation annotation)
   {
      return util.removeAnnotation(this, method, annotation);
   }

   /*
    * Method Modifiers
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
   public Method setBody(final String body)
   {
      String stub = "public class Stub { public void method() {" + body + "} }";
      JavaClass temp = JavaParser.parse(stub);
      List<Method> methods = temp.getMethods();
      Block block = ((MethodDeclaration) methods.get(0).getInternal()).getBody();

      block = (Block) ASTNode.copySubtree(method.getAST(), block);
      method.setBody(block);

      return this;
   }

   @Override
   public Method setConstructor(final boolean constructor)
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
   public Method setReturnType(final Class<?> type)
   {
      return setReturnType(type.getSimpleName());
   }

   @Override
   public Method setReturnType(final String type)
   {
      method.setReturnType2(ast.newSimpleType(ast.newSimpleName(type)));
      return this;
   }

   @Override
   public boolean isReturnTypeVoid()
   {
      return getReturnType() == null;
   }

   @Override
   public Method setReturnTypeVoid()
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
      return ma.hasModifier(method, ModifierKeyword.ABSTRACT_KEYWORD);
   }

   @Override
   public Method setAbstract(boolean abstrct)
   {
      if (abstrct)
      {
         ma.addModifier(method, ModifierKeyword.ABSTRACT_KEYWORD);
      }
      else
      {
         ma.removeModifier(method, ModifierKeyword.ABSTRACT_KEYWORD);
      }
      return this;
   }

   @Override
   public Method setFinal()
   {
      ma.addModifier(method, ModifierKeyword.FINAL_KEYWORD);
      return this;
   }

   @Override
   public String getName()
   {
      return method.getName().getFullyQualifiedName();
   }

   @Override
   public Method setName(final String name)
   {
      if (method.isConstructor())
      {
         throw new IllegalStateException("Cannot set the name of a constructor.");
      }
      method.setName(ast.newSimpleName(name));
      return this;
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
   public Method setPackagePrivate()
   {
      ma.clearVisibility(method);
      return this;
   }

   @Override
   public boolean isPublic()
   {
      return ma.hasModifier(method, ModifierKeyword.PUBLIC_KEYWORD);
   }

   @Override
   public Method setPublic()
   {
      ma.clearVisibility(method);
      ma.addModifier(method, ModifierKeyword.PUBLIC_KEYWORD);
      return this;
   }

   @Override
   public boolean isPrivate()
   {
      return ma.hasModifier(method, ModifierKeyword.PRIVATE_KEYWORD);
   }

   @Override
   public Method setPrivate()
   {
      ma.clearVisibility(method);
      ma.addModifier(method, ModifierKeyword.PRIVATE_KEYWORD);
      return this;
   }

   @Override
   public boolean isProtected()
   {
      return ma.hasModifier(method, ModifierKeyword.PROTECTED_KEYWORD);
   }

   @Override
   public Method setProtected()
   {
      ma.clearVisibility(method);
      ma.addModifier(method, ModifierKeyword.PROTECTED_KEYWORD);
      return this;
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
   public JavaClass applyChanges()
   {
      return parent.applyChanges();
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
      MethodImpl other = (MethodImpl) obj;
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
