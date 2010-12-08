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
public interface MethodHolder<T> extends MemberHolder<T, Member>
{
   /**
    * Add an uninitialized {@link Method} declaration to this {@link T}
    * instance. This {@link Method} will be a stub until further modified.
    */
   public Method<T> addMethod();

   /**
    * Add a new {@link Method} declaration to this {@link T} instance, using the
    * given {@link String} as the method declaration.
    * <p/>
    * <strong>For example:</strong><br>
    * <code>Method m = javaClass.addMethod("public String method() {return \"hello!\";}")</code>
    */
   public Method<T> addMethod(final String method);

   /**
    * Get a {@link List} of all {@link Method}s declared by this {@link T}
    * instance, if any; otherwise, return an empty {@link List}
    */
   public List<Method<T>> getMethods();

   /**
    * Remove the given {@link Method} declaration from this {@link T} instance,
    * if it exists; otherwise, do nothing.
    */
   public T removeMethod(final Method<T> method);

}
