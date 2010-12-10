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

import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.jboss.seam.forge.parser.java.Annotation;
import org.jboss.seam.forge.parser.java.Import;
import org.jboss.seam.forge.parser.java.JavaSource;
import org.jboss.seam.forge.parser.java.Member;
import org.jboss.seam.forge.parser.java.SyntaxError;
import org.jboss.seam.forge.parser.java.Visibility;
import org.jboss.seam.forge.parser.java.ast.AnnotationAccessor;
import org.jboss.seam.forge.parser.java.ast.ModifierAccessor;
import org.jboss.seam.forge.parser.java.ast.TypeDeclarationFinderVisitor;

/**
 * Represents a Java Source File
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public abstract class AbstractJavaSource<T extends JavaSource<T>> implements
         JavaSource<T>
{
   private final AnnotationAccessor<T, T> annotations = new AnnotationAccessor<T, T>();
   private final ModifierAccessor modifiers = new ModifierAccessor();

   private final Document document;
   protected CompilationUnit unit;

   public AbstractJavaSource(final Document document, final CompilationUnit unit)
   {
      this.document = document;
      this.unit = unit;
   }

   /*
    * Annotation modifiers
    */
   @Override
   public Annotation<T> addAnnotation()
   {
      return annotations.addAnnotation(this, getBodyDeclaration());
   }

   @Override
   public Annotation<T> addAnnotation(final Class<? extends java.lang.annotation.Annotation> clazz)
   {
      if (!this.hasImport(clazz))
      {
         this.addImport(clazz);
      }
      return annotations.addAnnotation(this, getBodyDeclaration(), clazz.getSimpleName());
   }

   @Override
   public Annotation<T> addAnnotation(final String className)
   {
      return annotations.addAnnotation(this, getBodyDeclaration(), className);
   }

   @Override
   public List<Annotation<T>> getAnnotations()
   {
      return annotations.getAnnotations(this, getBodyDeclaration());
   }

   @Override
   public boolean hasAnnotation(final Class<? extends java.lang.annotation.Annotation> type)
   {
      return annotations.hasAnnotation(this, getBodyDeclaration(), type.getName());
   }

   @Override
   public boolean hasAnnotation(final String type)
   {
      return annotations.hasAnnotation(this, getBodyDeclaration(), type);
   }

   @Override
   public T removeAnnotation(final Annotation<T> annotation)
   {
      return (T) annotations.removeAnnotation(this, getBodyDeclaration(), annotation);
   }

   @Override
   public Annotation<T> getAnnotation(final Class<? extends java.lang.annotation.Annotation> type)
   {
      return annotations.getAnnotation(this, getBodyDeclaration(), type);
   }

   @Override
   public Annotation<T> getAnnotation(final String type)
   {
      return annotations.getAnnotation(this, getBodyDeclaration(), type);
   }

   /*
    * Import modifiers
    */
   @Override
   @SuppressWarnings("unchecked")
   public Import<T> addImport(final String className)
   {
      Import<T> imprt = null;
      if (!hasImport(className))
      {
         imprt = new ImportImpl(this).setName(className);
         unit.imports().add(imprt.getInternal());
      }
      else
      {
         imprt = getImport(className);
      }
      return imprt;
   }

   @Override
   public Import<T> getImport(final String className)
   {
      List<Import<T>> imports = getImports();
      for (Import<T> imprt : imports)
      {
         if (imprt.getName().equals(className))
         {
            return imprt;
         }
      }
      return null;
   }

   @Override
   public Import<T> getImport(final Class<?> type)
   {
      return getImport(type.getName());
   }

   @Override
   public Import<T> addImport(final Class<?> type)
   {
      return addImport(type.getName());
   }

   @Override
   public T addImports(final Class<?>... types)
   {
      for (Class<?> type : types)
      {
         addImport(type.getName());
      }
      return (T) this;
   }

   @Override
   public T addImports(final String... types)
   {
      for (String type : types)
      {
         addImport(type);
      }
      return (T) this;
   }

   @Override
   public boolean hasImport(final Class<?> type)
   {
      return hasImport(type.getName());
   }

   @Override
   public boolean hasImport(final String type)
   {
      return getImport(type) != null;
   }

   @Override
   public T removeImport(final String name)
   {
      for (Import<T> i : getImports())
      {
         if (i.getName().equals(name))
         {
            removeImport(i);
            break;
         }
      }
      return (T) this;
   }

   @Override
   public T removeImport(final Class<?> clazz)
   {
      return removeImport(clazz.getName());
   }

   @Override
   public T removeImport(final Import<T> imprt)
   {
      Object internal = imprt.getInternal();
      if (unit.imports().contains(internal))
      {
         unit.imports().remove(internal);
      }
      return (T) this;
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<Import<T>> getImports()
   {
      List<Import<T>> results = new ArrayList<Import<T>>();

      for (ImportDeclaration i : (List<ImportDeclaration>) unit.imports())
      {
         results.add(new ImportImpl(this, i));
      }

      return Collections.unmodifiableList(results);
   }

   @Override
   public List<Member<T, ?>> getMembers()
   {
      List<Member<T, ?>> result = new ArrayList<Member<T, ?>>();

      return result;
   }

   protected AbstractTypeDeclaration getBodyDeclaration()
   {
      TypeDeclarationFinderVisitor typeDeclarationFinder = new TypeDeclarationFinderVisitor();
      unit.accept(typeDeclarationFinder);
      AbstractTypeDeclaration declaration = typeDeclarationFinder.getTypeDeclaration();
      if (declaration == null)
      {
         throw new RuntimeException(
                  "A type-declaration is required in order to complete the current operation, but no type-declaration exists in compilation unit: "
                           + unit.toString());
      }
      return declaration;
   }

   /*
    * Name modifiers
    */
   @Override
   public String getName()
   {
      return getBodyDeclaration().getName().getIdentifier();
   }

   @Override
   public T setName(final String name)
   {
      getBodyDeclaration().setName(unit.getAST().newSimpleName(name));
      return updateTypeNames(name);
   }

   /**
    * Call-back to allow updating of any necessary internal names with the given name.
    */
   protected abstract T updateTypeNames(String name);

   @Override
   public String getQualifiedName()
   {
      String packg = getPackage();
      String name = getName();
      if ((packg != null) && !packg.isEmpty())
      {
         return packg + "." + name;
      }
      return name;
   }

   /*
    * Package modifiers
    */
   @Override
   public String getPackage()
   {
      PackageDeclaration pkg = unit.getPackage();
      if (pkg != null)
      {
         return pkg.getName().getFullyQualifiedName();
      }
      else
      {
         return null;
      }
   }

   @Override
   public T setPackage(final String name)
   {
      if (unit.getPackage() == null)
      {
         unit.setPackage(unit.getAST().newPackageDeclaration());
      }
      unit.getPackage().setName(unit.getAST().newName(name));
      return (T) this;
   }

   @Override
   public T setDefaultPackage()
   {
      unit.setPackage(null);
      return (T) this;
   }

   @Override
   public boolean isDefaultPackage()
   {
      return unit.getPackage() == null;
   }

   /*
    * Visibility modifiers
    */
   @Override
   public boolean isPackagePrivate()
   {
      return (!isPublic() && !isPrivate() && !isProtected());
   }

   @Override
   public T setPackagePrivate()
   {
      modifiers.clearVisibility(getBodyDeclaration());
      return (T) this;
   }

   @Override
   public boolean isPublic()
   {
      return modifiers.hasModifier(getBodyDeclaration(), ModifierKeyword.PUBLIC_KEYWORD);
   }

   @Override
   public T setPublic()
   {
      modifiers.clearVisibility(getBodyDeclaration());
      modifiers.addModifier(getBodyDeclaration(), ModifierKeyword.PUBLIC_KEYWORD);
      return (T) this;
   }

   @Override
   public boolean isPrivate()
   {
      return modifiers.hasModifier(getBodyDeclaration(), ModifierKeyword.PRIVATE_KEYWORD);
   }

   @Override
   public T setPrivate()
   {
      modifiers.clearVisibility(getBodyDeclaration());
      modifiers.addModifier(getBodyDeclaration(), ModifierKeyword.PRIVATE_KEYWORD);
      return (T) this;
   }

   @Override
   public boolean isProtected()
   {
      return modifiers.hasModifier(getBodyDeclaration(), ModifierKeyword.PROTECTED_KEYWORD);
   }

   @Override
   public T setProtected()
   {
      modifiers.clearVisibility(getBodyDeclaration());
      modifiers.addModifier(getBodyDeclaration(), ModifierKeyword.PROTECTED_KEYWORD);
      return (T) this;
   }

   @Override
   public Visibility getVisibility()
   {
      return Visibility.getFrom(this);
   }

   @Override
   public T setVisibility(final Visibility scope)
   {
      return (T) Visibility.set(this, scope);
   }

   /*
    * Non-manipulation methods.
    */
   @Override
   public String toString()
   {
      return unit.toString();
   }

   @Override
   public Object getInternal()
   {
      return unit;
   }

   @Override
   public T getOrigin()
   {
      try
      {
         TextEdit edit = unit.rewrite(document, null);
         edit.apply(document);
      }
      catch (MalformedTreeException e)
      {
         throw new RuntimeException(e);
      }
      catch (BadLocationException e)
      {
         throw new RuntimeException(e);
      }

      return (T) this;
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
      if (!this.toString().equals(obj.toString()))
      {
         return false;
      }
      return true;
   }

   @Override
   public List<SyntaxError> getSyntaxErrors()
   {
      List<SyntaxError> result = new ArrayList<SyntaxError>();

      IProblem[] problems = unit.getProblems();
      if (problems != null)
      {
         for (IProblem problem : problems)
         {
            result.add(new SyntaxErrorImpl(this, problem));
         }
      }
      return result;
   }

   @Override
   public boolean hasSyntaxErrors()
   {
      return !getSyntaxErrors().isEmpty();
   }

   @Override
   public boolean isClass()
   {
      AbstractTypeDeclaration declaration = getBodyDeclaration();
      return (declaration instanceof TypeDeclaration)
               && !((TypeDeclaration) declaration).isInterface();

   }

   @Override
   public boolean isEnum()
   {
      AbstractTypeDeclaration declaration = getBodyDeclaration();
      return declaration instanceof EnumDeclaration;
   }

   @Override
   public boolean isInterface()
   {
      AbstractTypeDeclaration declaration = getBodyDeclaration();
      return (declaration instanceof TypeDeclaration)
               && ((TypeDeclaration) declaration).isInterface();
   }

   @Override
   public boolean isAnnotation()
   {
      AbstractTypeDeclaration declaration = getBodyDeclaration();
      return declaration instanceof AnnotationTypeDeclaration;
   }
}
