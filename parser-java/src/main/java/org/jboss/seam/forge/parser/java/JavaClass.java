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

package org.jboss.seam.forge.parser.java;

import java.util.List;

import org.jboss.seam.forge.parser.JavaParser;

/**
 * Represents a Java Class source file as an in-memory modifiable element. See
 * {@link JavaParser} for various options in generating {@link JavaClass}
 * instances.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface JavaClass extends Abstractable<JavaClass>, VisibilityScoped<JavaClass>, AnnotationTarget<JavaClass>
{
   /*
    * Compilation apis
    */
   /**
    * Get a list of all {@link SyntaxError}s detected in the current
    * {@link JavaClass}. Note that when errors are present, the class may still
    * be modified, but changes may not be completely accurate.
    */
   public List<SyntaxError> getSyntaxErrors();

   /**
    * Return whether or not this {@link JavaClass} currently has any
    * {@link SyntaxError}s.
    */
   public boolean hasSyntaxErrors();

   /*
    * Package modifiers
    */
   /**
    * Get the package of this {@link JavaClass}, or return null if it is in the
    * default package.
    */
   public String getPackage();

   /**
    * Set this {@link JavaClass}' package.
    */
   public JavaClass setPackage(String name);

   /**
    * Set this {@link JavaClass} to be in the default package (removes any
    * current package declaration.)
    */
   public JavaClass setDefaultPackage();

   /**
    * Return whether or not this {@link JavaClass} is in the default package.
    */
   public boolean isDefaultPackage();

   /*
    * Import modifiers
    */
   /**
    * Add an import by qualified class name. (E.g: "org.jboss.Imported")
    */
   public Import addImport(final String className);

   /**
    * Add an import for the given {@link Class} type;
    */
   public Import addImport(final Class<?> type);

   /**
    * Add an import for each of the given {@link Class} types.
    */
   public JavaClass addImports(final Class<?>... types);

   /**
    * Add an import for each of the given fully-qualified class names.
    */
   public JavaClass addImports(final String... types);

   /**
    * Return whether or not this {@link JavaClass} has an import for the given
    * {@link Class} type.
    */
   boolean hasImport(Class<?> type);

   /**
    * Return whether or not this {@link JavaClass} has an import for the given
    * fully-qualified class name.
    */
   boolean hasImport(String type);

   /**
    * Get the {@link Import} for the given fully-qualified class name, if it
    * exists; otherwise, return null;
    */
   public Import getImport(String literalValue);

   /**
    * Get the {@link Import} for the given {@link Class} type, if it exists;
    * otherwise, return null;
    */
   public Import getImport(Class<?> type);

   /**
    * Remove any {@link Import} for the given fully-qualified class name, if it
    * exists; otherwise, do nothing;
    */
   public JavaClass removeImport(String name);

   /**
    * Remove any {@link Import} for the given {@link Class} type, if it exists;
    * otherwise, do nothing;
    */
   public JavaClass removeImport(Class<?> type);

   /**
    * Remove the given {@link Import} from this {@link JavaClass} instance, if
    * it exists; otherwise, do nothing;
    */
   public JavaClass removeImport(Import imprt);

   /**
    * Get an immutable list of all {@link Import}s currently imported by this
    * {@link JavaClass}
    */
   public List<Import> getImports();

   /*
    * Fields & Methods
    */

   /**
    * Add a new Java {@link Field} to this {@link JavaClass} instance. This
    * field will be a stub until further modified.
    */
   public Field addField();

   /**
    * Add a new {@link Field} declaration to this {@link JavaClass} instance,
    * using the given {@link String} as the declaration.
    * <p>
    * <strong>For example:</strong><br>
    * <code>Field f = javaClass.addField("private String newField;");</code>
    */
   public Field addField(final String declaration);

   /**
    * Return whether or not this {@link JavaClass} declares a {@link Field} with
    * the given name.
    */
   public boolean hasField(String name);

   /**
    * Return whether or not this {@link JavaClass} declares the given
    * {@link Field} instance.
    */
   public boolean hasField(Field field);

   /**
    * Get the {@link Field} with the given name and return it, otherwise, return
    * null.
    */
   public Field getField(String name);

   /**
    * Get a list of all {@link Field}s declared by this {@link JavaClass}, or
    * return an empty list if no {@link Field}s are declared.
    */
   public List<Field> getFields();

   /**
    * Remove the given {@link Field} from this {@link JavaClass} instance, if it
    * exists; otherwise, do nothing.
    */
   public JavaClass removeField(final Field method);

   /**
    * Add an uninitialized {@link Method} declaration to this {@link JavaClass}
    * instance. This {@link Method} will be a stub until further modified.
    */
   public Method addMethod();

   /**
    * Add a new {@link Method} declaration to this {@link JavaClass} instance,
    * using the given {@link String} as the method declaration.
    * <p>
    * <strong>For example:</strong><br>
    * <code>Method m = javaClass.addMethod("public String method() {return \"hello!\";}")</code>
    */
   public Method addMethod(final String method);

   /**
    * Get a {@link List} of all {@link Method}s declared by this
    * {@link JavaClass} instance, if any; otherwise, return an empty
    * {@link List}
    */
   public List<Method> getMethods();

   /**
    * Remove the given {@link Method} declaration from this {@link JavaClass}
    * instance, if it exists; otherwise, do nothing.
    */
   public JavaClass removeMethod(final Method method);

   /**
    * Get the simple name of this {@link JavaClass} instance. (E.g: this would
    * be equivalent to calling, <code>Class.class.getSimpleName();</code>, where
    * "Class" is the type represented by this {@link JavaClass} instance.
    */
   public String getName();

   /**
    * Set the simple-name of this {@link JavaClass} instance.
    * 
    * @see #getName()
    */
   public JavaClass setName(String name);

   /**
    * Set the qualified-name of this {@link JavaClass} instance, where the
    * qualified-name contains both the Java package and simple class name of the
    * type represented by this {@link JavaClass} instance.
    * <p>
    * <strong>For example</strong>, calling:<br>
    * 
    * <code>
    * javaClass.setQualifiedName("org.jboss.Example");</code>
    * <p>
    * Is equivalent to calling:
    * <p>
    * <code>javaClass.setPackage("org.jboss").setName("Example");</code>
    */
   public String getQualifiedName();

   /**
    * Return a list of all class members (fields, methods, etc.)
    */
   public List<Member<?>> getMembers();

}