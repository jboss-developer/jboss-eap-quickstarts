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

import org.jboss.seam.forge.parser.Origin;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface Method<O> extends Abstractable<Method<O>>, Member<O, Method<O>>, Origin<O>
{
   public String getBody();

   public Method<O> setBody(final String body);

   /**
    * Toggle this method as a constructor. If true, and the name of the
    * {@link Method} is not the same as the name of its parent {@link JavaClass}
    * , update the name of the to match.
    */
   public Method<O> setConstructor(final boolean constructor);

   public boolean isConstructor();

   public Method<O> setFinal();

   public Method<O> setName(final String name);

   /**
    * Get the return type of this {@link Method} or return null if the return
    * type is void.
    */
   public String getReturnType();

   public Method<O> setReturnType(final Class<?> type);

   public Method<O> setReturnType(final String type);

   public boolean isReturnTypeVoid();

   public Method<O> setReturnTypeVoid();

   public Method<O> setParameters(String string);

   public List<Parameter<O>> getParameters();

}