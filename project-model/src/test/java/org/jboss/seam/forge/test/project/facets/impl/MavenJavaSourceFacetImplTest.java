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

package org.jboss.seam.forge.test.project.facets.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.inject.Singleton;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.forge.parser.JavaParser;
import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.facets.JavaSourceFacet;
import org.jboss.seam.forge.project.resources.FileResource;
import org.jboss.seam.forge.project.resources.builtin.JavaResource;
import org.jboss.seam.forge.test.project.util.ProjectModelTest;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Singleton
@RunWith(Arquillian.class)
public class MavenJavaSourceFacetImplTest extends ProjectModelTest
{
   private static final String PKG = "com.test";

   @Test
   public void testCreateJavaFile() throws Exception
   {
      Project project = getProject();

      String name = "JustCreated";
      JavaClass clazz = JavaParser.createClass().setName(name).setPackage(PKG);
      JavaResource file = project.getFacet(JavaSourceFacet.class).saveJavaClass(clazz);
      assertEquals(name + ".java", file.getName());

      JavaClass result = file.getJavaClass();
      assertEquals(name, result.getName());
      assertEquals(PKG, result.getPackage());
      assertTrue(file.delete());
      assertEquals(clazz, result);
   }

   @Test
   public void testGetJavaClassReparsesJavaClass() throws Exception
   {
      Project project = getProject();
      JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);

      String name = "JustCreated";
      JavaClass clazz = JavaParser.createClass().setName(name).setPackage(PKG);
      FileResource file = java.saveJavaClass(clazz);
      assertEquals(name + ".java", file.getName());

      JavaClass parsed = java.getJavaClass(clazz);
      assertEquals(parsed.getName(), clazz.getName());
      assertEquals(parsed.getPackage(), clazz.getPackage());
      assertEquals(parsed, clazz);
   }

   @Test
   public void testGetTestJavaClassReparsesJavaClass() throws Exception
   {
      Project project = getProject();
      JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);

      String name = "JustCreated";
      JavaClass clazz = JavaParser.createClass().setName(name).setPackage(PKG);
      FileResource file = java.saveTestJavaClass(clazz);
      assertEquals(name + ".java", file.getName());

      JavaClass parsed = java.getTestJavaClass(clazz);
      assertEquals(parsed.getName(), clazz.getName());
      assertEquals(parsed.getPackage(), clazz.getPackage());
      assertEquals(parsed, clazz);
   }
}
