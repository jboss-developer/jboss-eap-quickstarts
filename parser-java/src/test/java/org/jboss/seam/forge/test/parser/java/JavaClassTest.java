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
package org.jboss.seam.forge.test.parser.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.jboss.seam.forge.parser.JavaParser;
import org.jboss.seam.forge.parser.java.Import;
import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.parser.java.Method;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class JavaClassTest
{
   private InputStream stream;
   private JavaClass javaClass;

   @Before
   public void reset()
   {
      stream = JavaClassTest.class.getResourceAsStream("/org/jboss/seam/forge/grammar/java/MockClass.java");
      javaClass = JavaParser.parse(JavaClass.class, stream);
   }

   @Test
   public void testApplyChangesNotRequiredForModification() throws Exception
   {
      assertEquals("MockClass", javaClass.getName());
      javaClass.setName("Telephone");
      assertEquals("Telephone", javaClass.getName());
      assertTrue(javaClass.toString().contains("Telephone"));
      assertFalse(javaClass.toString().contains("MockClass"));
   }

   @Test
   public void testParse() throws Exception
   {
      assertEquals(URL.class.getName(), javaClass.getImports().get(0).getQualifiedName());
      assertEquals(1, javaClass.getMethods().size());
      assertEquals("MockClass", javaClass.getName());
      assertTrue(javaClass.isPublic());
      assertFalse(javaClass.isAbstract());
   }

   @Test
   public void testSetName() throws Exception
   {
      assertEquals("MockClass", javaClass.getName());
      javaClass.setName("Telephone");
      assertEquals("Telephone", javaClass.getName());
   }

   @Test
   public void testSetNameUpdatesConstructorNames() throws Exception
   {
      Method<JavaClass> constructor = javaClass.addMethod().setConstructor(true).setPublic();
      assertEquals("MockClass", javaClass.getName());
      assertEquals("MockClass", constructor.getName());
      javaClass.setName("Telephone");
      assertEquals("Telephone", javaClass.getName());
      assertEquals("Telephone", constructor.getName());
   }

   @Test
   public void testSetPackage() throws Exception
   {
      javaClass.setPackage("org.lincoln");
      assertEquals("org.lincoln", javaClass.getPackage());
      assertFalse(javaClass.isDefaultPackage());
   }

   @Test
   public void testSetAbstract() throws Exception
   {
      javaClass.setAbstract(true);
      assertTrue(javaClass.isAbstract());
   }

   @Test
   public void testSetPackageDefault() throws Exception
   {
      javaClass.setDefaultPackage();
      assertNull(javaClass.getPackage());
      assertTrue(javaClass.isDefaultPackage());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testAddImportPrimitiveThrowsException() throws Exception
   {
      javaClass.addImport(boolean.class);
   }

   @Test
   public void testAddImport() throws Exception
   {
      javaClass.addImport(List.class.getName());
      assertEquals(2, javaClass.getImports().size());
      assertEquals(URL.class.getName(), javaClass.getImports().get(0).getQualifiedName());
      assertEquals(List.class.getName(), javaClass.getImports().get(1).getQualifiedName());
   }

   @Test
   public void testAddImportsClasses() throws Exception
   {
      assertEquals(1, javaClass.getImports().size());

      javaClass.addImports(List.class, Map.class);

      assertEquals(3, javaClass.getImports().size());
      assertEquals(Map.class.getName(), javaClass.getImports().get(2).getQualifiedName());
   }

   @Test
   public void testAddImportStatic() throws Exception
   {
      assertEquals(1, javaClass.getImports().size());
      javaClass.addImport(List.class).setStatic(true);
      assertEquals(2, javaClass.getImports().size());
      assertTrue(javaClass.getImports().get(1).isStatic());
   }

   @Test
   public void testHasImport() throws Exception
   {
      assertEquals(1, javaClass.getImports().size());
      assertFalse(javaClass.hasImport(List.class));
      javaClass.addImport(List.class);
      assertEquals(2, javaClass.getImports().size());
      assertTrue(javaClass.hasImport(List.class));
   }

   @Test
   public void testCannotAddDuplicateImport() throws Exception
   {
      assertEquals(1, javaClass.getImports().size());
      assertFalse(javaClass.hasImport(List.class));
      javaClass.addImport(List.class);
      javaClass.addImport(List.class);
      assertEquals(2, javaClass.getImports().size());
      assertTrue(javaClass.hasImport(List.class));
   }

   @Test
   public void testRemoveImportByClass() throws Exception
   {
      List<Import> imports = javaClass.getImports();
      assertEquals(1, imports.size());
      assertEquals(URL.class.getName(), imports.get(0).getQualifiedName());

      javaClass.removeImport(URL.class);
      imports = javaClass.getImports();
      assertEquals(0, imports.size());
   }

   @Test
   public void testRemoveImportByName() throws Exception
   {
      assertEquals(1, javaClass.getImports().size());
      assertEquals(URL.class.getName(), javaClass.getImports().get(0).getQualifiedName());

      javaClass.removeImport(URL.class.getName());
      assertEquals(0, javaClass.getImports().size());
   }

   @Test
   public void testRemoveImportByReference() throws Exception
   {
      assertEquals(1, javaClass.getImports().size());
      assertEquals(URL.class.getName(), javaClass.getImports().get(0).getQualifiedName());

      javaClass.removeImport(javaClass.getImports().get(0));
      assertEquals(0, javaClass.getImports().size());
   }

   @Test
   public void testRequiresImport() throws Exception
   {
      assertFalse(javaClass.hasImport(JavaClassTest.class));
      assertTrue(javaClass.requiresImport(JavaClassTest.class));
      javaClass.addImport(JavaClassTest.class);
      assertTrue(javaClass.hasImport(JavaClassTest.class));
      assertFalse(javaClass.requiresImport(JavaClassTest.class));
   }

   @Test
   public void testAddImportAcceptsJavaLangPackage() throws Exception
   {
      assertFalse(javaClass.hasImport(String.class));
      assertFalse(javaClass.requiresImport(String.class));
      javaClass.addImport(String.class);
      assertTrue(javaClass.hasImport(String.class));
      assertFalse(javaClass.requiresImport(String.class));
   }

   @Test
   public void testAddMethod() throws Exception
   {
      int size = javaClass.getMethods().size();
      Method<JavaClass> method = javaClass.addMethod().setName("testMethod").setReturnTypeVoid().setBody("");
      List<Method<JavaClass>> methods = javaClass.getMethods();
      assertEquals(size + 1, methods.size());
      assertNull(method.getReturnType());
   }

   @Test
   public void testAddMethodFromString() throws Exception
   {
      int size = javaClass.getMethods().size();
      Method<JavaClass> method = javaClass.addMethod(
               "public URL rewriteURL(String pattern, String replacement) { return null; }")
               .setPackagePrivate();
      List<Method<JavaClass>> methods = javaClass.getMethods();
      assertEquals(size + 1, methods.size());
      assertEquals("URL", method.getReturnType());
      assertEquals("rewriteURL", method.getName());

      String body = method.getBody();
      assertEquals("return null;".replaceAll("\\s+", ""), body.replaceAll("\\s+", ""));
   }

   @Test
   public void testRemoveMethod() throws Exception
   {
      int size = javaClass.getMethods().size();
      List<Method<JavaClass>> methods = javaClass.getMethods();
      javaClass.removeMethod(methods.get(0));
      methods = javaClass.getMethods();
      assertEquals(size - 1, methods.size());
   }

   @Test
   public void testAddConstructor() throws Exception
   {
      int size = javaClass.getMethods().size();
      Method<JavaClass> method = javaClass.addMethod().setName("testMethod").setConstructor(true).setProtected()
               .setReturnType(String.class)
               .setBody("System.out.println(\"I am a constructor!\");");
      assertEquals(size + 1, javaClass.getMethods().size());
      assertEquals(javaClass.getName(), method.getName());
      assertTrue(method.isProtected());
      assertTrue(method.isConstructor());
      assertNull(method.getReturnType());
      String body = method.getBody();
      assertEquals("System.out.println(\"I am a constructor!\");".replaceAll("\\s+", ""), body.replaceAll("\\s+", ""));
   }

   @Test
   public void testAddConstructorIngoresReturnTypeAndName() throws Exception
   {
      int size = javaClass.getMethods().size();
      Method<JavaClass> method = javaClass.addMethod().setName("testMethod").setConstructor(true).setPrivate()
               .setReturnType(String.class)
               .setBody("System.out.println(\"I am a constructor!\");");
      assertEquals(size + 1, javaClass.getMethods().size());
      assertTrue(method.isPrivate());
      assertTrue(method.isConstructor());
      assertNull(method.getReturnType());
      assertEquals(javaClass.getName(), method.getName());
      String body = method.getBody();
      assertEquals("System.out.println(\"I am a constructor!\");".replaceAll("\\s+", ""), body.replaceAll("\\s+", ""));
   }

   @Test
   public void testSuperType() throws Exception
   {
      JavaClass source = JavaParser.parse(JavaClass.class, "public class Base extends Super {}");
      assertEquals("Super", source.getSuperType());

      source.setSuperType(getClass());
      assertEquals(getClass().getSimpleName(), source.getSuperType());
      assertTrue(source.hasImport(getClass()));
   }

}
