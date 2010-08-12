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

package org.jboss.seam.sidekick.parser.java;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.jboss.seam.sidekick.parser.java.impl.JavaClassImpl;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public abstract class JavaParser
{
   public static JavaClass parse(char[] data)
   {
      return new JavaClassImpl(data);
   }

   public static JavaClass parse(String data)
   {
      return new JavaClassImpl(data);
   }

   public static JavaClass parse(InputStream data)
   {
      return new JavaClassImpl(data);
   }

   public static JavaClass createClass()
   {
      return new JavaClassImpl();
   }

   public static JavaClass parse(File file)
   {
      try
      {
         FileInputStream stream = new FileInputStream(file);
         return parse(stream);
      }
      catch (FileNotFoundException e)
      {
         throw new RuntimeException(e);
      }
   }
}
