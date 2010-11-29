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
package org.jboss.seam.forge.test.grammar.java;

import org.jboss.seam.forge.parser.JavaParser;
import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.parser.java.Method;
import org.jboss.seam.forge.parser.java.Parameter;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class MethodTest
{
   private InputStream stream;
   private JavaClass javaClass;
   private Method method;

   @Before
   public void reset()
   {
      stream = MethodTest.class.getResourceAsStream("/org/jboss/seam/forge/grammar/java/MockClass.java");
      javaClass = JavaParser.parse(stream);
      javaClass.addMethod("public URL rewriteURL(String pattern, String replacement) { return null; }");
      method = javaClass.getMethods().get(javaClass.getMethods().size() - 1);
   }

   @Test
   public void testSetName() throws Exception
   {
      assertEquals("rewriteURL", method.getName());
      method.setName("doSomething");
      assertEquals("doSomething", method.getName());
   }

   @Test
   public void testSetReturnType() throws Exception
   {
      assertEquals("URL", method.getReturnType());
      method.setReturnType(Class.class);
      assertEquals("Class", method.getReturnType());
      assertFalse(method.isReturnTypeVoid());
   }

   @Test
   public void testSetReturnTypeVoid() throws Exception
   {
      assertEquals("URL", method.getReturnType());
      method.setReturnTypeVoid();
      assertEquals(null, method.getReturnType());
      assertTrue(method.isReturnTypeVoid());
   }

   @Test
   public void testSetConstructor() throws Exception
   {
      assertFalse(method.isConstructor());
      method.setConstructor(true);
      assertTrue(method.isConstructor());
      assertEquals(javaClass.getName(), method.getName());
   }

   @Test
   public void testSetAbstract() throws Exception
   {
      method.setAbstract(true);
      assertTrue(method.isAbstract());
   }

   @Test
   public void testSetParameters() throws Exception
   {
      method.setParameters("final int foo, final String bar");
      List<Parameter> parameters = method.getParameters();
      assertEquals(2, parameters.size());
      assertEquals("foo", parameters.get(0).getName());
      assertEquals("bar", parameters.get(1).getName());
   }
}
