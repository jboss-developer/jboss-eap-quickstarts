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
public interface Importer<O extends JavaSource<?>>
{
   /**
    * Add an import by qualified class name. (E.g: "com.example.Imported") unless it is in the provided 'java.lang.*'
    * package.
    */
   public Import addImport(final String className);

   /**
    * Add an import for the given {@link Class} type.
    */
   public Import addImport(final Class<?> type);

   /**
    * Add an import for the given {@link Import} type.
    */
   public Import addImport(Import imprt);

   /**
    * Add an import for the given {@link JavaSource} type.
    */
   public <T extends JavaSource<?>> Import addImport(T type);

   /**
    * Add an import for each given {@link Class} type.
    */
   public O addImports(final Class<?>... types);

   /**
    * Add an import for each given {@link Import} type.
    */
   public O addImports(Import... imprt);

   /**
    * Add an import for each given {@link JavaSource} type.
    */
   public <T extends JavaSource<?>> O addImports(T... types);

   /**
    * Add an import for each given fully-qualified class name.
    */
   public O addImports(final String... types);

   /**
    * Return whether or not this {@link O} has an import for the given {@link Class} type.
    */
   boolean hasImport(Class<?> type);

   /**
    * Return whether or not this {@link O} has an import for the given fully-qualified class name.
    */
   boolean hasImport(String type);

   /**
    * Return whether or not this {@link O} could accept an import for the given {@link Class} type.
    */
   boolean requiresImport(Class<?> type);

   /**
    * Return whether or not this {@link O} could accept an import for the given fully-qualified class name.
    */
   boolean requiresImport(String type);

   /**
    * Return whether or not this {@link O} has an import for the given {@link T} type.
    */
   public <T extends JavaSource<?>> boolean hasImport(T type);

   /**
    * Return whether or not this {@link O} has the given {@link Import} type.
    */
   public boolean hasImport(Import imprt);

   /**
    * Get the {@link Import} for the given fully-qualified class name, if it exists; otherwise, return null;
    */
   public Import getImport(String literalValue);

   /**
    * Get the {@link Import} for the given {@link Class} type, if it exists; otherwise, return null;
    */
   public Import getImport(Class<?> type);

   /**
    * Get the {@link Import} for the given {@link T} type, if it exists; otherwise, return null;
    */
   public <T extends JavaSource<?>> Import getImport(T type);

   /**
    * Get the {@link Import} of the given {@link Import} type, if it exists; otherwise, return null;
    */
   public Import getImport(Import imprt);

   /**
    * Remove any {@link Import} for the given fully-qualified class name, if it exists; otherwise, do nothing;
    */
   public O removeImport(String name);

   /**
    * Remove any {@link Import} for the given {@link Class} type, if it exists; otherwise, do nothing;
    */
   public O removeImport(Class<?> type);

   /**
    * Remove any {@link Import} for the given {@link T} type, if it exists; otherwise, do nothing;
    */
   public <T extends JavaSource<?>> O removeImport(T type);

   /**
    * Remove the given {@link Import} from this {@link O} instance, if it exists; otherwise, do nothing;
    */
   public O removeImport(Import imprt);

   /**
    * Get an immutable list of all {@link Import}s currently imported by this {@link O}
    */
   public List<Import> getImports();
}
