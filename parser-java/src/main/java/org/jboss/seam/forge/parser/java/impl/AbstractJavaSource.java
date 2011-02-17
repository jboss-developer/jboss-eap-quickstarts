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
@SuppressWarnings("unchecked")
public abstract class AbstractJavaSource<O extends JavaSource<O>> implements
         JavaSource<O>
{
   private final AnnotationAccessor<O, O> annotations = new AnnotationAccessor<O, O>();
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
   public Annotation<O> addAnnotation()
   {
      return annotations.addAnnotation(this, getBodyDeclaration());
   }

   @Override
   public Annotation<O> addAnnotation(final Class<? extends java.lang.annotation.Annotation> clazz)
   {
      return annotations.addAnnotation(this, getBodyDeclaration(), clazz.getName());
   }

   @Override
   public Annotation<O> addAnnotation(final String className)
   {
      return annotations.addAnnotation(this, getBodyDeclaration(), className);
   }

   @Override
   public List<Annotation<O>> getAnnotations()
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
   public O removeAnnotation(final Annotation<O> annotation)
   {
      return (O) annotations.removeAnnotation(this, getBodyDeclaration(), annotation);
   }

   @Override
   public Annotation<O> getAnnotation(final Class<? extends java.lang.annotation.Annotation> type)
   {
      return annotations.getAnnotation(this, getBodyDeclaration(), type);
   }

   @Override
   public Annotation<O> getAnnotation(final String type)
   {
      return annotations.getAnnotation(this, getBodyDeclaration(), type);
   }

   /*
    * Import modifiers
    */

   @Override
   public Import<O> addImport(final Class<?> type)
   {
      return addImport(type.getName());
   }

   @Override
   public <T extends JavaSource<?>> Import<O> addImport(T type)
   {
      return this.addImport(type.getQualifiedName());
   }

   @Override
   public Import<O> addImport(Import<?> imprt)
   {
      return addImport(imprt.getQualifiedName()).setStatic(imprt.isStatic());
   }

   @Override
   public Import<O> addImport(final String className)
   {
      Import<O> imprt;
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
   public O addImports(final Class<?>... types)
   {
      for (Class<?> type : types)
      {
         addImport(type.getName());
      }
      return (O) this;
   }

   @Override
   public <T extends JavaSource<?>> O addImports(T... types)
   {
      for (T t : types)
      {
         addImport(t);
      }
      return (O) this;
   };

   @Override
   public O addImports(final String... types)
   {
      for (String type : types)
      {
         addImport(type);
      }
      return (O) this;
   }

   @Override
   public O addImports(Import<?>... imprt)
   {
      for (Import<?> i : imprt)
      {
         addImport(i);
      }
      return (O) this;
   };

   @Override
   public Import<O> getImport(final String className)
   {
      List<Import<O>> imports = getImports();
      for (Import<O> imprt : imports)
      {
         if (imprt.getQualifiedName().equals(className))
         {
            return imprt;
         }
      }
      return null;
   }

   @Override
   public Import<O> getImport(final Class<?> type)
   {
      return getImport(type.getName());
   }

   @Override
   public <T extends JavaSource<?>> Import<O> getImport(T type)
   {
      return getImport(type.getQualifiedName());
   };

   @Override
   public Import<O> getImport(Import<?> imprt)
   {
      return getImport(imprt.getQualifiedName());
   }

   @Override
   public List<Import<O>> getImports()
   {
      List<Import<O>> results = new ArrayList<Import<O>>();

      for (ImportDeclaration i : (List<ImportDeclaration>) unit.imports())
      {
         results.add(new ImportImpl(this, i));
      }

      return Collections.unmodifiableList(results);
   }

   @Override
   public boolean hasImport(final Class<?> type)
   {
      return hasImport(type.getName());
   }

   @Override
   public <T extends JavaSource<?>> boolean hasImport(T type)
   {
      return hasImport(type.getQualifiedName());
   };

   @Override
   public boolean hasImport(Import<?> imprt)
   {
      return hasImport(imprt.getQualifiedName());
   }

   @Override
   public boolean hasImport(final String type)
   {
      return getImport(type) != null;
   }

   @Override
   public O removeImport(final String name)
   {
      for (Import<O> i : getImports())
      {
         if (i.getQualifiedName().equals(name))
         {
            removeImport(i);
            break;
         }
      }
      return (O) this;
   }

   @Override
   public O removeImport(final Class<?> clazz)
   {
      return removeImport(clazz.getName());
   }

   @Override
   public <T extends JavaSource<?>> O removeImport(T type)
   {
      return removeImport(type.getQualifiedName());
   };

   @Override
   public O removeImport(final Import<O> imprt)
   {
      Object internal = imprt.getInternal();
      if (unit.imports().contains(internal))
      {
         unit.imports().remove(internal);
      }
      return (O) this;
   }

   @Override
   public List<Member<O, ?>> getMembers()
   {
      List<Member<O, ?>> result = new ArrayList<Member<O, ?>>();

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
   public O setName(final String name)
   {
      getBodyDeclaration().setName(unit.getAST().newSimpleName(name));
      return updateTypeNames(name);
   }

   /**
    * Call-back to allow updating of any necessary internal names with the given name.
    */
   protected abstract O updateTypeNames(String name);

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
   public O setPackage(final String name)
   {
      if (unit.getPackage() == null)
      {
         unit.setPackage(unit.getAST().newPackageDeclaration());
      }
      unit.getPackage().setName(unit.getAST().newName(name));
      return (O) this;
   }

   @Override
   public O setDefaultPackage()
   {
      unit.setPackage(null);
      return (O) this;
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
   public O setPackagePrivate()
   {
      modifiers.clearVisibility(getBodyDeclaration());
      return (O) this;
   }

   @Override
   public boolean isPublic()
   {
      return modifiers.hasModifier(getBodyDeclaration(), ModifierKeyword.PUBLIC_KEYWORD);
   }

   @Override
   public O setPublic()
   {
      modifiers.clearVisibility(getBodyDeclaration());
      modifiers.addModifier(getBodyDeclaration(), ModifierKeyword.PUBLIC_KEYWORD);
      return (O) this;
   }

   @Override
   public boolean isPrivate()
   {
      return modifiers.hasModifier(getBodyDeclaration(), ModifierKeyword.PRIVATE_KEYWORD);
   }

   @Override
   public O setPrivate()
   {
      modifiers.clearVisibility(getBodyDeclaration());
      modifiers.addModifier(getBodyDeclaration(), ModifierKeyword.PRIVATE_KEYWORD);
      return (O) this;
   }

   @Override
   public boolean isProtected()
   {
      return modifiers.hasModifier(getBodyDeclaration(), ModifierKeyword.PROTECTED_KEYWORD);
   }

   @Override
   public O setProtected()
   {
      modifiers.clearVisibility(getBodyDeclaration());
      modifiers.addModifier(getBodyDeclaration(), ModifierKeyword.PROTECTED_KEYWORD);
      return (O) this;
   }

   @Override
   public Visibility getVisibility()
   {
      return Visibility.getFrom(this);
   }

   @Override
   public O setVisibility(final Visibility scope)
   {
      return (O) Visibility.set(this, scope);
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
   public O getOrigin()
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

      return (O) this;
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
