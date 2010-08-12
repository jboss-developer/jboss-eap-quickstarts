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

package org.jboss.seam.sidekick.parser.java;

import java.util.List;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface JavaClass extends Abstractable<JavaClass>, VisibilityScoped<JavaClass>, AnnotationTarget<JavaClass>
{
   /*
    * Annotation modifiers
    */
   public abstract Import addImport(final String className);

   public abstract Import addImport(final Class<?> type);

   public abstract JavaClass addImports(final Class<?>... types);

   public abstract JavaClass addImports(final String... types);

   public abstract JavaClass removeImport(String name);

   public abstract JavaClass removeImport(Class<?> clazz);

   public abstract JavaClass removeImport(Import imprt);

   public abstract List<Import> getImports();

   /*
    * Fields & Methods
    */

   public abstract Field addField();

   public abstract Field addField(final String field);

   public abstract List<Field> getFields();

   public abstract JavaClass removeField(final Field method);

   public abstract Method addMethod();

   public abstract Method addMethod(final String method);

   public abstract List<Method> getMethods();

   public abstract JavaClass removeMethod(final Method method);

   public abstract String getName();

   public abstract JavaClass setName(String name);

   public abstract String getPackage();

   public abstract JavaClass setPackage(String name);

   public abstract JavaClass setDefaultPackage();

   public abstract boolean isDefaultPackage();

   @Override
   public abstract int hashCode();

   @Override
   public abstract boolean equals(final Object obj);

}