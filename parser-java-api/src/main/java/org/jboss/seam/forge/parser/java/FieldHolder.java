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
@SuppressWarnings("rawtypes")
public interface FieldHolder<T> extends MemberHolder<T, Member>
{
   /**
    * Add a new Java {@link Field} to this {@link T} instance. This field will be a stub until further modified.
    */
   public Field<T> addField();

   /**
    * Add a new {@link Field} declaration to this {@link T} instance, using the given {@link String} as the declaration.
    * <p/>
    * <strong>For example:</strong><br>
    * <code>Field f = javaClass.addField("private String newField;");</code>
    */
   public Field<T> addField(final String declaration);

   /**
    * Return whether or not this {@link T} declares a {@link Field} with the given name.
    */
   public boolean hasField(String name);

   /**
    * Return whether or not this {@link T} declares the given {@link Field} instance.
    */
   public boolean hasField(Field<T> field);

   /**
    * Get the {@link Field} with the given name and return it, otherwise, return null.
    */
   public Field<T> getField(String name);

   /**
    * Get a list of all {@link Field}s declared by this {@link T}, or return an empty list if no {@link Field}s are
    * declared.
    */
   public List<Field<T>> getFields();

   /**
    * Remove the given {@link Field} from this {@link T} instance, if it exists; otherwise, do nothing.
    */
   public T removeField(final Field<T> method);
}
