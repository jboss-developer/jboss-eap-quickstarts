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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.Document;
import org.jboss.seam.forge.parser.java.Field;
import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.parser.java.JavaInterface;
import org.jboss.seam.forge.parser.java.JavaType;
import org.jboss.seam.forge.parser.java.Member;
import org.jboss.seam.forge.parser.java.SourceType;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class JavaInterfaceImpl extends AbstractJavaSourceMethodHolder<JavaInterface> implements JavaInterface
{

   public JavaInterfaceImpl(final Document document, final CompilationUnit unit)
   {
      super(document, unit);
   }

   @Override
   public List<Member<JavaInterface, ?>> getMembers()
   {
      List<Member<JavaInterface, ?>> result = new ArrayList<Member<JavaInterface, ?>>();

      for (Field<JavaInterface> member : getFields())
      {
         result.add(member);
      }
      result.addAll(getFields());
      result.addAll(getMethods());

      return result;
   }

   @Override
   protected JavaInterface updateTypeNames(final String name)
   {
      return this;
   }

   @Override
   public String getSuperType()
   {
      return null;
   }

   @Override
   public <T extends JavaType<?>> JavaClass setSuperType(final T type)
   {
      return null;
   }

   @Override
   public JavaClass setSuperType(final String type)
   {
      return null;
   }

   @Override
   public JavaClass setSuperType(final Class<?> type)
   {
      return null;
   }

   @Override
   public SourceType getSourceType()
   {
      return SourceType.INTERFACE;
   }
}
