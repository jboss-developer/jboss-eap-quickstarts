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

import org.jboss.seam.forge.parser.Origin;

import java.util.List;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@SuppressWarnings("rawtypes")
public interface JavaSource<T extends JavaSource<T>> extends
         Packaged<T>,
         Importer<T>,
         Named<T>,
         VisibilityScoped<T>,
         AnnotationTarget<T, T>,
         MemberHolder<T, Member>,
         Origin<T>
{
   /**
    * Set the qualified-name of this {@link T} instance, where the qualified-name contains both the Java package and
    * simple class name of the type represented by this {@link T} instance.
    * <p/>
    * <strong>For example</strong>, calling: {@link #getQualifiedName()} is equivalent to calling "{@link #getPackage()}
    * + "." + {@link #getName()}", which in turn is equivalent to calling: {@link Class#getName()}
    */
   public String getQualifiedName();

   /**
    * Get a list of all {@link SyntaxError}s detected in the current {@link T}. Note that when errors are present, the
    * class may still be modified, but changes may not be completely accurate.
    */
   public List<SyntaxError> getSyntaxErrors();

   /**
    * Return whether or not this {@link T} currently has any {@link SyntaxError} s.
    */
   public boolean hasSyntaxErrors();

   /**
    * Return true if this {@link JavaSource} represents a {@link JavaClass}
    */
   public boolean isClass();

   /**
    * Return true if this {@link JavaSource} represents a {@link JavaEnum}
    */
   public boolean isEnum();

   /**
    * Return true if this {@link JavaSource} represents a {@link JavaClass} interface.
    */
   public boolean isInterface();

   /**
    * Return true if this {@link JavaSource} represents a {@link JavaAnnotation}
    */
   public boolean isAnnotation();
}
