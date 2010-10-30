/*
 * JBoss, by Red Hat.
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

package org.jboss.seam.forge.project.resources.builtin;

import java.util.Collections;
import java.util.List;

import org.jboss.seam.forge.parser.java.Method;
import org.jboss.seam.forge.parser.java.Parameter;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.resources.ClassMemberResource;

/**
 * @author Mike Brock
 */
public class JavaMethodResource extends ClassMemberResource<Method>
{
   private Method method;

   public JavaMethodResource()
   {
      super(null);
   }

   public JavaMethodResource(final Resource<?> parent, final Method method)
   {
      super(parent);
      this.method = method;
   }

   @Override
   public Resource<Method> createFrom(final Method file)
   {
      throw new RuntimeException("not implemented");
   }

   public List<Resource<?>> listResources()
   {
      return Collections.emptyList();
   }

   @Override
   public Method getUnderlyingResourceObject()
   {
      return method;
   }

   @Override
   public String toString()
   {
      String params = "";
      for (Parameter param : method.getParameters())
      {
         params += param;
         if (method.getParameters().indexOf(param) < method.getParameters().size() - 1)
         {
            params += ", ";
         }
      }
      return method.getName() + "(" + params + ")" + " : "
               + (method.getReturnType() == null ? "void" : method.getReturnType());
   }
}
