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
package org.jboss.seam.sidekick.test.grammar.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;

import org.jboss.seam.sidekick.parser.java.Field;
import org.jboss.seam.sidekick.parser.java.JavaClass;
import org.jboss.seam.sidekick.parser.java.JavaParser;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class FieldTest
{
   private InputStream stream;
   private JavaClass javaClass;
   private Field field;

   @Before
   public void reset()
   {
      stream = FieldTest.class.getResourceAsStream("/org/jboss/encore/grammar/java/MockAnnotatedField.java");
      javaClass = JavaParser.parse(stream);
      field = javaClass.getFields().get(javaClass.getFields().size() - 1);
   }

   @Test
   public void testParse() throws Exception
   {
      assertTrue(field instanceof Field);
      assertEquals("field", field.getName());
      assertEquals("String", field.getType());
   }

   @Test
   public void testSetName() throws Exception
   {
      assertEquals("field", field.getName());
      field.setName("newName");
      field.applyChanges();
      assertTrue(field.toString().contains("newName;"));
      assertEquals("newName", field.getName());
   }

   @Test
   public void testSetType() throws Exception
   {
      assertEquals("field", field.getName());
      field.setType(FieldTest.class);
      field.applyChanges();
      assertTrue(field.toString().contains("FieldTest"));
      assertEquals(FieldTest.class.getSimpleName(), field.getType());
   }

   @Test
   public void testSetTypeString() throws Exception
   {
      assertEquals("field", field.getName());
      field.setType("FooBarType");
      field.applyChanges();
      assertTrue(field.toString().contains("FooBarType"));
      assertEquals("FooBarType", field.getType());
   }

   @Test
   public void testAddField() throws Exception
   {
      javaClass.addField("public Boolean flag = false;");
      Field fld = javaClass.getFields().get(javaClass.getFields().size() - 1);
      fld.applyChanges();

      assertTrue(fld.toString().contains("Boolean"));
      assertEquals("Boolean", fld.getType());
      assertEquals("flag", fld.getName());
      assertEquals("false", fld.getLiteralInitializer());
   }

   @Test
   public void testAddFieldInitializerLiteral() throws Exception
   {
      javaClass.addField("public int flag;").setLiteralInitializer("1234").setPrivate();
      Field fld = javaClass.getFields().get(javaClass.getFields().size() - 1);
      fld.applyChanges();

      assertEquals("int", fld.getType());
      assertEquals("flag", fld.getName());
      assertEquals("1234", fld.getLiteralInitializer());
      assertEquals("1234", fld.getStringInitializer());
      assertEquals("private int flag=1234;", fld.toString().trim());
   }

   @Test
   public void testAddFieldInitializerString() throws Exception
   {
      javaClass.addField("public String flag;").setStringInitializer("american");
      Field fld = javaClass.getFields().get(javaClass.getFields().size() - 1);
      fld.applyChanges();

      assertEquals("String", fld.getType());
      assertEquals("flag", fld.getName());
      assertEquals("\"american\"", fld.getLiteralInitializer());
      assertEquals("american", fld.getStringInitializer());
      assertEquals("public String flag=\"american\";", fld.toString().trim());
   }

   @Test
   public void testAddQualifiedFieldType() throws Exception
   {
      javaClass.addField().setName("flag").setType(String.class.getName()).setStringInitializer("american").setPrivate();
      Field fld = javaClass.getFields().get(javaClass.getFields().size() - 1);
      fld.applyChanges();

      assertEquals(String.class.getName(), fld.getType());
      assertEquals("flag", fld.getName());
      assertEquals("\"american\"", fld.getLiteralInitializer());
      assertEquals("american", fld.getStringInitializer());
      assertEquals("private java.lang.String flag=\"american\";", fld.toString().trim());
   }
}
