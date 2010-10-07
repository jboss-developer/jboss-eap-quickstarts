package org.jboss.seam.forge.scaffold.test.plugins;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.facets.JavaSourceFacet;
import org.jboss.seam.forge.scaffold.ScaffoldingFacet;
import org.jboss.seam.forge.scaffold.test.plugins.util.AbstractScaffoldTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@RunWith(Arquillian.class)
public class NewFieldPluginTest extends AbstractScaffoldTest
{
   @Before
   @Override
   public void beforeTest() throws IOException
   {
      super.beforeTest();
      initializeJavaProject();
      if ((getProject() != null) && !getProject().hasFacet(ScaffoldingFacet.class))
      {
         queueInputLines("y");
         getShell().execute("install scaffold");
      }
   }

   @Test
   public void testNewIntFieldObject() throws Exception
   {
      Project project = getProject();
      JavaClass javaClass = generateEntity(project);

      getShell().execute("new-field int --fieldName gamesPlayed --primitive false");

      javaClass = project.getFacet(JavaSourceFacet.class).getJavaClass(javaClass);
      assertTrue(javaClass.hasAnnotation(Entity.class));
      assertTrue(javaClass.hasField("gamesPlayed"));
      assertFalse(javaClass.getField("gamesPlayed").isPrimitive());
      assertEquals("Integer", javaClass.getField("gamesPlayed").getType());
      assertFalse(javaClass.hasImport(Integer.class));
      assertFalse(javaClass.hasSyntaxErrors());
   }

   @Test
   public void testNewIntFieldPrimitive() throws Exception
   {
      Project project = getProject();
      JavaClass javaClass = generateEntity(project);

      getShell().execute("new-field int --fieldName gamesPlayed");

      javaClass = project.getFacet(JavaSourceFacet.class).getJavaClass(javaClass);
      assertTrue(javaClass.hasAnnotation(Entity.class));
      assertTrue(javaClass.hasField("gamesPlayed"));
      assertTrue(javaClass.getField("gamesPlayed").isPrimitive());
      assertEquals("int", javaClass.getField("gamesPlayed").getType());
      assertFalse(javaClass.hasImport(int.class));
      assertFalse(javaClass.hasSyntaxErrors());
   }

   @Test
   public void testNewNumberField() throws Exception
   {
      Project project = getProject();
      JavaClass javaClass = generateEntity(project);

      getShell().execute("new-field number --fieldName gamesPlayed --type java.math.BigDecimal");

      javaClass = project.getFacet(JavaSourceFacet.class).getJavaClass(javaClass);
      assertTrue(javaClass.hasAnnotation(Entity.class));
      assertTrue(javaClass.hasField("gamesPlayed"));
      assertFalse(javaClass.getField("gamesPlayed").isPrimitive());
      assertEquals("BigDecimal", javaClass.getField("gamesPlayed").getType());
      assertTrue(javaClass.hasImport(BigDecimal.class));
      assertFalse(javaClass.hasSyntaxErrors());
   }

   @Test
   public void testNewNumberFieldNotAddedIfClassNotValid() throws Exception
   {
      Project project = getProject();
      JavaClass javaClass = generateEntity(project);

      getShell().execute("new-field number --fieldName gamesPlayed --type org.jboss.NotANumber");

      javaClass = project.getFacet(JavaSourceFacet.class).getJavaClass(javaClass);
      assertEquals(0, javaClass.getFields().size());
      assertFalse(javaClass.hasImport("org.jboss.NotANumber"));
      assertFalse(javaClass.hasSyntaxErrors());
   }

   @Test
   public void testNewOneToOneRelationship() throws Exception
   {
      Project project = getProject();
      JavaClass field = generateEntity(project);
      JavaClass entity = generateEntity(project);

      getShell().execute(
               "new-field reference --fieldName right --fieldType ~.domain." + field.getName()
                        + " --inverseFieldName left");

      entity = project.getFacet(JavaSourceFacet.class).getJavaClass(entity);

      assertTrue(entity.hasAnnotation(Entity.class));
      assertTrue(entity.hasField("right"));
      assertTrue(entity.getField("right").getType().equals(field.getName()));
      assertTrue(entity.getField("right").hasAnnotation(OneToOne.class));
      assertTrue(entity.hasImport(field.getQualifiedName()));
      assertTrue(entity.hasImport(OneToOne.class));
      assertFalse(entity.hasSyntaxErrors());

      field = project.getFacet(JavaSourceFacet.class).getJavaClass(field);

      assertTrue(field.hasField("left"));
      assertTrue(field.getField("left").getType().equals(entity.getName()));
      assertTrue(field.getField("left").hasAnnotation(OneToOne.class));
      assertTrue(field.hasImport(entity.getQualifiedName()));
      assertTrue(field.hasImport(OneToOne.class));
      assertFalse(field.hasSyntaxErrors());
   }

}
