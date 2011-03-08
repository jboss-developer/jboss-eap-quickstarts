/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc., and individual contributors
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
package org.jboss.seam.forge.test.parser.java.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.Serializable;

import org.jboss.seam.forge.parser.JavaParser;
import org.jboss.seam.forge.parser.java.JavaInterface;
import org.jboss.seam.forge.parser.java.JavaType;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public abstract class InterfacedTestBase<T extends JavaType<T>>
{

   private JavaType<T> source;

   @Before
   public void reset()
   {
      this.source = getSource();
   }

   protected abstract JavaType<T> getSource();

   @Test
   public void testAddInterfaceString() throws Exception
   {
      assertFalse(this.source.hasInterface("com.foo.Bar"));
      this.source.addInterface(Serializable.class);
      assertTrue(this.source.hasInterface(Serializable.class));
   }

   @Test
   public void testAddInterfaceClass() throws Exception
   {
      assertFalse(this.source.hasInterface(Serializable.class));
      this.source.addInterface(Serializable.class);
      assertTrue(this.source.hasInterface(Serializable.class));
   }

   @Test
   public void testAddInterfaceJavaInterface() throws Exception
   {
      JavaInterface i2 = JavaParser.parse(JavaInterface.class, "package com.foo; public interface Bar<T> {}");
      assertFalse(this.source.hasInterface(i2));
      this.source.addInterface(i2);
      assertTrue(this.source.hasImport(i2));
      assertTrue(this.source.hasInterface(i2));
      assertTrue(this.source.hasInterface(i2.getQualifiedName()));
   }

   @Test(expected = IllegalArgumentException.class)
   public void testAddImproperInterface() throws Exception
   {
      this.source.addInterface("43 23omg.omg.omg");
      fail();
   }

   @Test
   public void testGetInterfaces() throws Exception
   {
      this.source.addInterface(Serializable.class);
      this.source.addInterface("com.example.Custom");
      this.source.addInterface("com.other.Custom");
      assertEquals(3, this.source.getInterfaces().size());
      assertTrue(this.source.hasInterface("com.example.Custom"));
      assertTrue(this.source.hasInterface("com.other.Custom"));
      assertTrue(this.source.hasImport(Serializable.class));
      assertTrue(this.source.hasImport("com.example.Custom"));
      assertFalse(this.source.hasImport("com.other.Custom"));
   }

   @Test
   public void testRemoveInterface() throws Exception
   {
      this.source.addInterface(Serializable.class);
      this.source.addInterface("com.example.Custom");
      this.source.addInterface("com.other.Custom");
      assertEquals(3, this.source.getInterfaces().size());
      assertTrue(this.source.hasInterface("com.example.Custom"));
      assertTrue(this.source.hasInterface("com.other.Custom"));
      assertTrue(this.source.hasImport(Serializable.class));

      this.source.removeInterface(Serializable.class);
      assertFalse(this.source.hasInterface(Serializable.class));
      assertEquals(2, this.source.getInterfaces().size());
   }
}
