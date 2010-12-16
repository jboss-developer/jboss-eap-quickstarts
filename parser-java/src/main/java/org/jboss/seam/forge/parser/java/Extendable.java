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

/**
 * Represents a {@link JavaType} that can extend other types. (Java inheritance
 * and interfaces.)
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface Extendable<O extends JavaType<?>>
{
   /**
    * Get this type's super class.
    * 
    * @see #setSuperType(String)
    */
   public String getSuperType();

   /**
    * Set this type's super class.
    * 
    * @see #setSuperType(String)
    */
   public <T extends JavaType<?>> O setSuperType(T type);

   /**
    * Set this type's super class.
    * 
    * @see #setSuperType(String)
    */
   public O setSuperType(Class<?> type);

   /**
    * Set this type's super class.
    * <p>
    * <strong>For example:</strong><br/>
    * In the case of " <code>public class Foo extends Bar {}</code>" -
    * <code>Foo</code> is the base type, and <code>Bar</code> is the super
    * class.)
    * <p>
    * Attempt to add an import statement to this object's {@link O} if required.
    * (Note that the given className must be fully-qualified in order to
    * properly import required classes)
    */
   public O setSuperType(String type);
}
