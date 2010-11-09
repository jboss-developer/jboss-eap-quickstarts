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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.jboss.seam.forge.parser.JavaParser;
import org.jboss.seam.forge.parser.java.Field;
import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.parser.java.Method;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.ResourceHandles;
import org.jboss.seam.forge.project.resources.FileResource;
import org.jboss.seam.forge.project.services.ResourceFactory;

/**
 * @author Mike Brock
 */
@ResourceHandles("*.java")
public class JavaResource extends FileResource
{
   private volatile JavaClass javaClass;

   @Inject
   public JavaResource(final ResourceFactory factory)
   {
      super(factory, null);
   }

   public JavaResource(final ResourceFactory factory, final File file)
   {
      super(factory, file);
   }

   @Override
   public synchronized List<Resource<?>> listResources()
   {
      try
      {
         lazyInitialize();
      }
      catch (FileNotFoundException e)
      {
         return Collections.emptyList();
      }

      List<Resource<?>> list = new LinkedList<Resource<?>>();

      for (Field field : javaClass.getFields())
      {
         list.add(new JavaFieldResource(this, field));
      }

      for (Method method : javaClass.getMethods())
      {
         list.add(new JavaMethodResource(this, method));
      }

      return list;
   }

   private void lazyInitialize() throws FileNotFoundException
   {
      if (javaClass == null)
      {
         javaClass = JavaParser.parse(file);
      }
   }

   public JavaClass getJavaClass() throws FileNotFoundException
   {
      lazyInitialize();
      return javaClass;
   }

   @Override
   public JavaResource createFrom(final File file)
   {
      return new JavaResource(resourceFactory, file);
   }

   @Override
   public String toString()
   {
      try
      {
         lazyInitialize();
      }
      catch (FileNotFoundException e)
      {
         return "[File not found: " + file + "]";
      }
      return javaClass.getName() + ".java";
   }
}
