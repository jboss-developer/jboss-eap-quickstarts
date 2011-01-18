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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mike Brock .
 */
public class TConstructor extends TMember
{
   protected final List<TParameter> parameters;

   public TConstructor(TClassType declaringClass)
   {
      super(declaringClass, declaringClass.getName());
      this.parameters = new ArrayList<TParameter>();
   }

   public List<TParameter> getParameters()
   {
      return parameters;
   }

   public void addParameter(String name, TClassType type) {
      parameters.add(new TParameter(name, type));
   }

   public void addParameter(String name, String type) {
      addParameter(name, TClassType.getClass(type));
   }

   @Override
   public String render()
   {
      StringBuilder sb = new StringBuilder(RenderUtil.tab());
      sb.append(getModifierString()).append(' ').append(getDeclaringClass().getSimpleName()).append('(')
      .append(TParameter.renderParameterList(parameters)).append(") {\n");

      for (TParameter tp : parameters) {
         sb.append(RenderUtil.tab(2))
               .append("this.").append(tp.getName()).append(" = ").append(tp.getName()).append(";\n");
      }

      return sb.append(RenderUtil.tab()).append("}").toString();
   }


}
