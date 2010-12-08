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

package org.jboss.seam.forge.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.parser.java.impl.JavaClassImpl;

/**
 * Responsible for parsing data into new {@link JavaClass} instances.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public abstract class JavaParser
{

   /**
    * Parse the given character array into a new {@link JavaClass} instance.
    */
   public static JavaClass parse(final char[] data)
   {
      return new JavaClassImpl(data);
   }

   /**
    * Parse the given String data into a new {@link JavaClass} instance.
    */
   public static JavaClass parse(final String data)
   {
      return new JavaClassImpl(data);
   }

   /**
    * Read the given {@link InputStream} and parse the data into a new
    * {@link JavaClass} instance.
    */
   public static JavaClass parse(final InputStream data)
   {
      return new JavaClassImpl(data);
   }

   /**
    * Create a new empty {@link JavaClass} instance.
    */
   public static JavaClass createClass()
   {
      return new JavaClassImpl();
   }

   /**
    * Open the given {@link File}, parsing its contents into a new
    * {@link JavaClass} instance.
    */
   public static JavaClass parse(final File file) throws FileNotFoundException
   {
      FileInputStream stream = new FileInputStream(file);
      return parse(stream);
   }
}
