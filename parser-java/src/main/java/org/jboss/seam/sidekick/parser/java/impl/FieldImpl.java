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
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.jboss.seam.sidekick.parser.JavaParser;
import org.jboss.seam.sidekick.parser.java.Annotation;
import org.jboss.seam.sidekick.parser.java.Field;
import org.jboss.seam.sidekick.parser.java.JavaClass;
import org.jboss.seam.sidekick.parser.java.ast.AnnotationAccessor;
import org.jboss.seam.sidekick.parser.java.ast.ModifierAccessor;
import org.jboss.seam.sidekick.parser.java.util.Strings;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class FieldImpl implements Field
{
   private static AnnotationAccessor util = new AnnotationAccessor();
   private final ModifierAccessor ma = new ModifierAccessor();

   private JavaClass parent;
   private AST ast;
   private final FieldDeclaration field;

   private void init(final JavaClass parent)
   {
      this.parent = parent;
      ast = ((ASTNode) parent.getInternal()).getAST();
   }

   public FieldImpl(final JavaClass parent)
   {
      init(parent);
      this.field = ast.newFieldDeclaration(ast.newVariableDeclarationFragment());
   }

   public FieldImpl(final JavaClass parent, final String declaration)
   {
      init(parent);

      String stub = "public class Stub { " + declaration + " }";
      JavaClass temp = JavaParser.parse(stub);
      List<Field> fields = temp.getFields();
      FieldDeclaration newField = (FieldDeclaration) fields.get(0).getInternal();
      FieldDeclaration subtree = (FieldDeclaration) ASTNode.copySubtree(ast, newField);
      this.field = subtree;
   }

   public FieldImpl(final JavaClass parent, final Object internal)
   {
      init(parent);
      this.field = (FieldDeclaration) internal;
   }

   @Override
   public JavaClass getOrigin()
   {
      return parent.getOrigin();
   }

   @Override
   public Object getInternal()
   {
      return field;
   }

   /*
    * Annotation Modifiers
    */
   @Override
   public Annotation addAnnotation()
   {
      return util.addAnnotation(this, field);
   }

   @Override
   public Annotation addAnnotation(final Class<?> clazz)
   {
      if (!parent.hasImport(clazz))
      {
         parent.addImport(clazz);
      }
      return util.addAnnotation(this, field, clazz.getSimpleName());
   }

   @Override
   public Annotation addAnnotation(final String className)
   {
      return util.addAnnotation(this, field, className);
   }

   @Override
   public List<Annotation> getAnnotations()
   {
      return util.getAnnotations(this, field);
   }

   @Override
   public Field removeAnnotation(final Annotation annotation)
   {
      return util.removeAnnotation(this, field, annotation);
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
   public Field setPackagePrivate()
   {
      ma.clearVisibility(field);
      return this;
   }

   @Override
   public boolean isPublic()
   {
      return ma.hasModifier(field, ModifierKeyword.PUBLIC_KEYWORD);
   }

   @Override
   public Field setPublic()
   {
      ma.clearVisibility(field);
      ma.addModifier(field, ModifierKeyword.PUBLIC_KEYWORD);
      return this;
   }

   @Override
   public boolean isPrivate()
   {
      return ma.hasModifier(field, ModifierKeyword.PRIVATE_KEYWORD);
   }

   @Override
   public Field setPrivate()
   {
      ma.clearVisibility(field);
      ma.addModifier(field, ModifierKeyword.PRIVATE_KEYWORD);
      return this;
   }

   @Override
   public boolean isProtected()
   {
      return ma.hasModifier(field, ModifierKeyword.PROTECTED_KEYWORD);
   }

   @Override
   public Field setProtected()
   {
      ma.clearVisibility(field);
      ma.addModifier(field, ModifierKeyword.PROTECTED_KEYWORD);
      return this;
   }

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
   public Field setName(final String name)
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
   public Field setType(final Class<?> clazz)
   {
      return setType(clazz.getSimpleName());
   }

   @Override
   public Field setType(final String type)
   {
      Name name = ast.newName(Strings.tokenizeClassName(type));
      SimpleType st = ast.newSimpleType(name);
      field.setType(st);
      return this;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jboss.encore.grammar.java.Field#getInitializer()
    */
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
   public Field setLiteralInitializer(final String value)
   {
      String stub = "public class Stub { private Field stub = " + value + " }";
      JavaClass temp = JavaParser.parse(stub);
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
   public Field setStringInitializer(final String value)
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
      FieldImpl other = (FieldImpl) obj;
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

}
