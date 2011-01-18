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

/**
 * @author Mike Brock .
 */
public class TField extends TMember
{
   protected TClassType classType;

   public TField(TClassType declaringClass, String name, TClassType classType)
   {
      super(declaringClass, name);
      this.classType = classType;
   }

   public TField(TClassType declaringClass, String name, TClassType classType, int modifiers)
   {
      super(declaringClass, name);
      this.classType = classType;
      this.modifiers = modifiers;
   }

   public TClassType getClassType()
   {
      return classType;
   }

   @Override
   public String render()
   {
      return getModifierString() + " " + classType.getSimpleName() + " " + getName() + ";";
   }
}
