/*
 * JBoss, by Red Hat.
 * Copyright 2011, Red Hat, Inc., and individual contributors
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

package org.jboss.seam.forge.scaffold.plugins.meta.model;

import org.jboss.seam.forge.scaffold.plugins.meta.Renderable;

import java.lang.reflect.Modifier;

/**
 * @author Mike Brock .
 */
public abstract class TMember implements Renderable
{
   protected final TClassType declaringClass;
   protected final String name;
   protected int modifiers;

   protected TMember(TClassType declaringClass, String name)
   {
      this.declaringClass = declaringClass;
      this.name = name;
   }

   public TClassType getDeclaringClass()
   {
      return declaringClass;
   }

   public String getName()
   {
      return name;
   }

   public int getModifiers()
   {
      return modifiers;
   }

   public void setModifiers(int modifiers)
   {
      this.modifiers = modifiers;
   }

   public boolean isPublic()
   {
      return (this.modifiers & Modifier.PUBLIC) != 0;
   }

   public boolean isProtected()
   {
      return (this.modifiers & Modifier.PROTECTED) != 0;
   }

   public boolean isPrivate()
   {
      return (this.modifiers & Modifier.PRIVATE) != 0;
   }

   public boolean isStatic()
   {
      return (this.modifiers & Modifier.STATIC) != 0;
   }

   public boolean isFinal()
   {
      return (this.modifiers & Modifier.FINAL) != 0;
   }

   public String getModifierString() {
      return Modifier.toString(this.modifiers);
   }

   public abstract String render();
}
