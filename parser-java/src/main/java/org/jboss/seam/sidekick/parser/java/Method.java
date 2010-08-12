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


/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface Method extends Abstractable<Method>, VisibilityScoped<Method>, AnnotationTarget<Method>
{
   public String getBody();

   public Method setBody(final String body);

   /**
    * Toggle this method as a constructor. If true, and the name of the
    * {@link Method} is not the same as the name of its parent {@link JavaClass}
    * , update the name of the to match.
    */
   public Method setConstructor(final boolean constructor);

   public boolean isConstructor();

   public Method setFinal();

   public String getName();

   public Method setName(final String name);

   public String getReturnType();

   public Method setReturnType(final Class<?> type);

   public Method setReturnType(final String type);

   public boolean isReturnTypeVoid();

   public Method setReturnTypeVoid();

}