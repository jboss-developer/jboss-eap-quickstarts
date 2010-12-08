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
public interface Importer<T>
{
   /**
    * Add an import by qualified class name. (E.g: "com.example.Imported")
    */
   public Import<T> addImport(final String className);

   /**
    * Add an import for the given {@link Class} type;
    */
   public Import<T> addImport(final Class<?> type);

   /**
    * Add an import for each of the given {@link Class} types.
    */
   public T addImports(final Class<?>... types);

   /**
    * Add an import for each of the given fully-qualified class names.
    */
   public T addImports(final String... types);

   /**
    * Return whether or not this {@link T} has an import for the given
    * {@link Class} type.
    */
   boolean hasImport(Class<?> type);

   /**
    * Return whether or not this {@link T} has an import for the given
    * fully-qualified class name.
    */
   boolean hasImport(String type);

   /**
    * Get the {@link Import} for the given fully-qualified class name, if it
    * exists; otherwise, return null;
    */
   public Import<T> getImport(String literalValue);

   /**
    * Get the {@link Import} for the given {@link Class} type, if it exists;
    * otherwise, return null;
    */
   public Import<T> getImport(Class<?> type);

   /**
    * Remove any {@link Import} for the given fully-qualified class name, if it
    * exists; otherwise, do nothing;
    */
   public T removeImport(String name);

   /**
    * Remove any {@link Import} for the given {@link Class} type, if it exists;
    * otherwise, do nothing;
    */
   public T removeImport(Class<?> type);

   /**
    * Remove the given {@link Import} from this {@link T} instance, if it
    * exists; otherwise, do nothing;
    */
   public T removeImport(Import<T> imprt);

   /**
    * Get an immutable list of all {@link Import}s currently imported by this
    * {@link T}
    */
   public List<Import<T>> getImports();
}
