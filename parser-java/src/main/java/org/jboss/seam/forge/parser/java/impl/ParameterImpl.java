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
package org.jboss.seam.forge.parser.java.impl;

import java.lang.reflect.Field;

import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.jboss.seam.forge.parser.java.Parameter;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class ParameterImpl implements Parameter
{
   private final VariableDeclaration param;

   public ParameterImpl(final Object internal)
   {
      this.param = (VariableDeclaration) internal;
   }

   @Override
   public String toString()
   {
      return param.toString();
   }

   @Override
   public String getName()
   {
      SimpleName name = param.getName();
      if (name != null)
      {
         return name.toString();
      }
      return "";
   }

   @Override
   public String getType()
   {
      Object type;

      try
      {
         // FIXME there *must* be a better way of doing this
         Class<? extends VariableDeclaration> clazz = param.getClass();
         Field field = clazz.getDeclaredField("type");
         field.setAccessible(true);
         type = field.get(param);
         field.setAccessible(false);
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }

      return type.toString();
   }

}
