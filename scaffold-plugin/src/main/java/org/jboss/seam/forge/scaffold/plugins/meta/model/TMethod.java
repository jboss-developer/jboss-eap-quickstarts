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

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mike Brock .
 */
public class TMethod extends TMember
{
   protected final TClassType returnType;
   protected final List<TParameter> parameters;
   protected String methodContents;

   public TMethod(TClassType declaringClass, String name, TClassType returnType)
   {
      super(declaringClass, name);
      this.parameters = new ArrayList<TParameter>();
      this.returnType = returnType == null ? TClassType.getClass("void") : returnType;
   }


   public TMethod(TClassType declaringClass, String name, TClassType returnType, int modifiers)
   {
      super(declaringClass, name);
      this.parameters = new ArrayList<TParameter>();
      this.returnType = returnType == null ? TClassType.getClass("void") : returnType;
      this.modifiers = modifiers;
   }

   public boolean isSynchronized()
   {
      return (this.modifiers & Modifier.SYNCHRONIZED) != 0;
   }

   public TClassType getReturnType()
   {
      return returnType;
   }

   public List<TParameter> getParameters()
   {
      return parameters;
   }

   public void addParameter(String name, TClassType type, int modifiers)
   {
      parameters.add(new TParameter(name, type, modifiers));
   }

   public void addParameter(String name, String type, int modifiers)
   {
      addParameter(name, TClassType.getClass(type), modifiers);
   }

   public void addParameter(String name, TClassType type)
   {
      addParameter(name, type, 0);
   }

   public void addParameter(String name, String type)
   {
      addParameter(name, type, 0);
   }

   public String getMethodContents()
   {
      return methodContents;
   }

   public void setMethodContents(String methodContents)
   {
      this.methodContents = methodContents;
   }

   public boolean isReturnTypeVoid()
   {
      return returnType.getName().equals("void");
   }

   @Override
   public String render()
   {
      StringBuilder sb = new StringBuilder();
      return sb.append(getModifierString()).append(" ").append(getName()).append("(")
            .append(TParameter.renderParameterList(parameters)).append(") {\n")
            .append(methodContents)
            .append("}\n").toString();
   }
}
