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

package org.jboss.seam.forge.test.parser.java.util;

import org.jboss.seam.forge.parser.JavaParser;
import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.parser.java.util.Formatter;
import org.jboss.seam.forge.test.parser.java.FieldAnnotationTest;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertNotSame;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class FormatterTest
{
   private static JavaClass javaClass;

   @BeforeClass
   public static void resetTests()
   {
      InputStream stream = FieldAnnotationTest.class.getResourceAsStream("/org/jboss/seam/forge/grammar/java/MockUnformattedClass.java");
      javaClass = JavaParser.parse(stream);
   }

   @Test
   public void testFormatSource() throws Exception
   {
      // TODO figure out some sort of way to test that this actually works
      String result = Formatter.format(javaClass);
      assertNotSame(result, javaClass.toString());
   }
}
