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

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface JavaClass extends Abstractable<JavaClass>, VisibilityScoped<JavaClass>, AnnotationTarget<JavaClass>
{
   /*
    * Compilation apis
    */
   public List<SyntaxError> getSyntaxErrors();

   public boolean hasSyntaxErrors();

   /*
    * Package modifiers
    */
   public String getPackage();

   public JavaClass setPackage(String name);

   public JavaClass setDefaultPackage();

   public boolean isDefaultPackage();

   /*
    * Import modifiers
    */
   public Import addImport(final String className);

   public Import addImport(final Class<?> type);

   public JavaClass addImports(final Class<?>... types);

   public JavaClass addImports(final String... types);

   boolean hasImport(Class<?> type);

   boolean hasImport(String type);

   public JavaClass removeImport(String name);

   public JavaClass removeImport(Class<?> type);

   public JavaClass removeImport(Import imprt);

   public List<Import> getImports();

   /*
    * Fields & Methods
    */

   public Field addField();

   /**
    * Add a field using the given {@link String} as the declaration, for example:
    * <p>
    * <code>javaClass.addField("private String newField;");</code>
    * 
    * @param declaration
    * @return
    */
   public Field addField(final String declaration);

   public boolean hasField(String string);

   public boolean hasField(Field field);

   public Field getField(String string);

   public List<Field> getFields();

   public JavaClass removeField(final Field method);

   public Method addMethod();

   public Method addMethod(final String method);

   public List<Method> getMethods();

   public JavaClass removeMethod(final Method method);

   public String getName();

   public JavaClass setName(String name);

   public String getQualifiedName();

}