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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.InputStream;
import java.util.List;

import org.jboss.seam.forge.parser.JavaParser;
import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.parser.java.Member;
import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class JavaInterfaceTest
{
   @Test
   public void testCanParseInterface() throws Exception
   {
      InputStream stream = JavaInterfaceTest.class.getResourceAsStream("/org/jboss/seam/forge/grammar/java/MockInterface.java");
      JavaClass javaClass = JavaParser.parse(stream);
      String name = javaClass.getName();
      assertEquals("MockInterface", name);
   }

   @Test
   public void testCanParseBigInterface() throws Exception
   {
      InputStream stream = JavaInterfaceTest.class.getResourceAsStream("/org/jboss/seam/forge/grammar/java/BigInterface.java");
      JavaClass javaClass = JavaParser.parse(stream);
      String name = javaClass.getName();
      assertEquals("BigInterface", name);
      List<Member<JavaClass, ?>> members = javaClass.getMembers();
      assertFalse(members.isEmpty());
   }

}
