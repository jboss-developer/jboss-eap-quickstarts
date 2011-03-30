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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jboss.seam.forge.parser.JavaParser;
import org.jboss.seam.forge.parser.java.Field;
import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.parser.java.Method;
import org.jboss.seam.forge.parser.java.util.Refactory;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class RefactoryTest
{
   JavaClass javaClass;

   @Before
   public void before()
   {
      javaClass = JavaParser.parse(JavaClass.class, "public class Foo { private int foo; private String firstName; }");
   }

   @Test
   public void testAddGettersAndSetters() throws Exception
   {
      Field<JavaClass> field = javaClass.getField("foo");
      Refactory.createGetterAndSetter(javaClass, field);

      List<Method<JavaClass>> methods = javaClass.getMethods();
      Method<JavaClass> getter = methods.get(0);
      Method<JavaClass> setter = methods.get(1);

      assertEquals("getFoo", getter.getName());
      assertTrue(getter.getParameters().isEmpty());
      assertEquals("setFoo", setter.getName());
   }

   @Test
   public void testAddGettersAndSettersCamelCase() throws Exception
   {
      Field<JavaClass> field = javaClass.getField("firstName");
      Refactory.createGetterAndSetter(javaClass, field);

      List<Method<JavaClass>> methods = javaClass.getMethods();
      Method<JavaClass> getter = methods.get(0);
      Method<JavaClass> setter = methods.get(1);

      assertEquals("getFirstName", getter.getName());
      assertTrue(getter.getParameters().isEmpty());
      assertEquals("setFirstName", setter.getName());
   }

   @Test
   public void testCreateToSrtingFromFields() throws Exception
   {
      assertFalse(javaClass.hasMethodSignature("toString"));
      Refactory.createToStringFromFields(javaClass);
      assertTrue(javaClass.hasMethodSignature("toString"));
      assertTrue(javaClass.getMethod("toString").getBody().contains("return"));
   }
}
