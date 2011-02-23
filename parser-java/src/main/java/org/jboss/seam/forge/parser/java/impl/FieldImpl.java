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

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.PrimitiveType.Code;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.jboss.seam.forge.parser.JavaParser;
import org.jboss.seam.forge.parser.java.Annotation;
import org.jboss.seam.forge.parser.java.Field;
import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.parser.java.JavaSource;
import org.jboss.seam.forge.parser.java.Visibility;
import org.jboss.seam.forge.parser.java.ast.AnnotationAccessor;
import org.jboss.seam.forge.parser.java.ast.ModifierAccessor;
import org.jboss.seam.forge.parser.java.util.Strings;
import org.jboss.seam.forge.parser.java.util.Types;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class FieldImpl<O extends JavaSource<O>> implements Field<O>
{
   private final AnnotationAccessor<O, Field<O>> annotations = new AnnotationAccessor<O, Field<O>>();
   private final ModifierAccessor modifiers = new ModifierAccessor();

   private O parent;
   private AST ast;
   private final FieldDeclaration field;

   private void init(final O parent)
   {
      this.parent = parent;
      ast = ((ASTNode) parent.getInternal()).getAST();
   }

   public FieldImpl(final O parent)
   {
      init(parent);
      this.field = ast.newFieldDeclaration(ast.newVariableDeclarationFragment());
   }

   public FieldImpl(final O parent, final String declaration)
   {
      init(parent);

      String stub = "public class Stub { " + declaration + " }";
      JavaClass temp = (JavaClass) JavaParser.parse(stub);
      List<Field<JavaClass>> fields = temp.getFields();
      FieldDeclaration newField = (FieldDeclaration) fields.get(0).getInternal();
      FieldDeclaration subtree = (FieldDeclaration) ASTNode.copySubtree(ast, newField);
      this.field = subtree;
   }

   public FieldImpl(final O parent, final Object internal)
   {
      init(parent);
      this.field = (FieldDeclaration) internal;
   }

   @Override
   public O getOrigin()
   {
      return parent.getOrigin();
   }

   @Override
   public Object getInternal()
   {
      return field;
   }

   /*
    * Annotation<O> Modifiers
    */
   @Override
   public Annotation<O> addAnnotation()
   {
      return annotations.addAnnotation(this, field);
   }

   @Override
   public Annotation<O> addAnnotation(final Class<? extends java.lang.annotation.Annotation> clazz)
   {
      if (!parent.hasImport(clazz))
      {
         parent.addImport(clazz);
      }
      return annotations.addAnnotation(this, field, clazz.getSimpleName());
   }

   @Override
   public Annotation<O> addAnnotation(final String className)
   {
      return annotations.addAnnotation(this, field, className);
   }

   @Override
   public List<Annotation<O>> getAnnotations()
   {
      return annotations.getAnnotations(this, field);
   }

   @Override
   public boolean hasAnnotation(final Class<? extends java.lang.annotation.Annotation> type)
   {
      return annotations.hasAnnotation(this, field, type.getName());
   }

   @Override
   public boolean hasAnnotation(final String type)
   {
      return annotations.hasAnnotation(this, field, type);
   }

   @Override
   public Annotation<O> getAnnotation(final Class<? extends java.lang.annotation.Annotation> type)
   {
      return annotations.getAnnotation(this, field, type);
   }

   @Override
   public Annotation<O> getAnnotation(final String type)
   {
      return annotations.getAnnotation(this, field, type);
   }

   @Override
   public Field<O> removeAnnotation(final Annotation<O> annotation)
   {
      return annotations.removeAnnotation(this, field, annotation);
   }

   @Override
   public String toString()
   {
      return field.toString();
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
   public Field<O> setPackagePrivate()
   {
      modifiers.clearVisibility(field);
      return this;
   }

   @Override
   public boolean isPublic()
   {
      return modifiers.hasModifier(field, ModifierKeyword.PUBLIC_KEYWORD);
   }

   @Override
   public Field<O> setPublic()
   {
      modifiers.clearVisibility(field);
      modifiers.addModifier(field, ModifierKeyword.PUBLIC_KEYWORD);
      return this;
   }

   @Override
   public boolean isPrivate()
   {
      return modifiers.hasModifier(field, ModifierKeyword.PRIVATE_KEYWORD);
   }

   @Override
   public Field<O> setPrivate()
   {
      modifiers.clearVisibility(field);
      modifiers.addModifier(field, ModifierKeyword.PRIVATE_KEYWORD);
      return this;
   }

   @Override
   public boolean isProtected()
   {
      return modifiers.hasModifier(field, ModifierKeyword.PROTECTED_KEYWORD);
   }

   @Override
   public Field<O> setProtected()
   {
      modifiers.clearVisibility(field);
      modifiers.addModifier(field, ModifierKeyword.PROTECTED_KEYWORD);
      return this;
   }

   @Override
   public Visibility getVisibility()
   {
      return Visibility.getFrom(this);
   }

   @Override
   public Field<O> setVisibility(final Visibility scope)
   {
      return Visibility.set(this, scope);
   }

   /*
    * Field<O> methods
    */

   @Override
   public String getName()
   {
      String result = null;
      for (Object f : field.fragments())
      {
         if (f instanceof VariableDeclarationFragment)
         {
            VariableDeclarationFragment frag = (VariableDeclarationFragment) f;
            result = frag.getName().getFullyQualifiedName();
            break;
         }
      }
      return result;
   }

   @Override
   public Field<O> setName(final String name)
   {
      for (Object f : field.fragments())
      {
         if (f instanceof VariableDeclarationFragment)
         {
            VariableDeclarationFragment frag = (VariableDeclarationFragment) f;
            frag.setName(ast.newSimpleName(name));
            break;
         }
      }
      return this;
   }

   @Override
   public String getType()
   {
      Object type = field.getStructuralProperty(FieldDeclaration.TYPE_PROPERTY);
      return type.toString();
   }

   @Override
   public Field<O> setType(final Class<?> clazz)
   {
      parent.addImport(clazz);
      return setType(clazz.getSimpleName());
   }

   @Override
   public Field<O> setType(final JavaSource<?> source)
   {
      return setType(source.getName());
   }

   @Override
   public Field<O> setType(final String typeName)
   {
      Code primitive = PrimitiveType.toCode(typeName);

      Type type = null;
      if (primitive != null)
      {
         type = ast.newPrimitiveType(primitive);
      }
      else
      {
         String[] className = Types.tokenizeClassName(typeName);
         Name name = ast.newName(className);
         type = ast.newSimpleType(name);
      }
      field.setType(type);
      return this;
   }

   @Override
   public String getLiteralInitializer()
   {
      String result = null;
      for (Object f : field.fragments())
      {
         if (f instanceof VariableDeclarationFragment)
         {
            VariableDeclarationFragment frag = (VariableDeclarationFragment) f;
            result = frag.getInitializer().toString();
            break;
         }
      }
      return result;
   }

   @Override
   public String getStringInitializer()
   {
      String result = null;
      for (Object f : field.fragments())
      {
         if (f instanceof VariableDeclarationFragment)
         {
            VariableDeclarationFragment frag = (VariableDeclarationFragment) f;
            result = Strings.unquote(frag.getInitializer().toString());
            break;
         }
      }
      return result;
   }

   @Override
   public Field<O> setLiteralInitializer(final String value)
   {
      String stub = "public class Stub { private Field<O> stub = " + value + " }";
      JavaClass temp = (JavaClass) JavaParser.parse(stub);
      FieldDeclaration internal = (FieldDeclaration) temp.getFields().get(0).getInternal();

      for (Object f : internal.fragments())
      {
         if (f instanceof VariableDeclarationFragment)
         {
            VariableDeclarationFragment tempFrag = (VariableDeclarationFragment) f;
            VariableDeclarationFragment localFrag = getFragment(field);
            localFrag.setInitializer((Expression) ASTNode.copySubtree(ast, tempFrag.getInitializer()));
            break;
         }
      }

      return this;
   }

   @Override
   public Field<O> setStringInitializer(final String value)
   {
      return setLiteralInitializer(Strings.enquote(value));
   }

   private VariableDeclarationFragment getFragment(final FieldDeclaration field)
   {
      VariableDeclarationFragment result = null;
      for (Object f : field.fragments())
      {
         if (f instanceof VariableDeclarationFragment)
         {
            result = (VariableDeclarationFragment) f;
            break;
         }
      }
      return result;
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((field == null) ? 0 : field.hashCode());
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
      FieldImpl<?> other = (FieldImpl<?>) obj;
      if (field == null)
      {
         if (other.field != null)
         {
            return false;
         }
      }
      else if (!field.equals(other.field))
      {
         return false;
      }
      return true;
   }

   @Override
   public boolean isPrimitive()
   {
      boolean result = false;
      Type type = field.getType();
      if (type != null)
      {
         result = type.isPrimitiveType();
      }
      return result;
   }

}
