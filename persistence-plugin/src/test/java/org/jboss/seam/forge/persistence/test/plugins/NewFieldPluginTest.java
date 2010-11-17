package org.jboss.seam.forge.persistence.test.plugins;

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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.persistence.test.plugins.util.AbstractJPATest;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.facets.JavaSourceFacet;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@RunWith(Arquillian.class)
public class NewFieldPluginTest extends AbstractJPATest
{
   @Test
   public void testNewBoolean() throws Exception
   {
      Project project = getProject();
      JavaClass javaClass = generateEntity(project);

      getShell().execute("new-field boolean --fieldName gamesPlayed --primitive false");
      getShell().execute("new-field boolean --fieldName gamesWon");

      javaClass = project.getFacet(JavaSourceFacet.class).getJavaClass(javaClass);
      assertTrue(javaClass.hasField("gamesPlayed"));
      assertFalse(javaClass.getField("gamesPlayed").isPrimitive());
      assertEquals("Boolean", javaClass.getField("gamesPlayed").getType());
      assertTrue(javaClass.hasField("gamesWon"));
      assertTrue(javaClass.getField("gamesWon").isPrimitive());
      assertFalse(javaClass.hasSyntaxErrors());
   }

   @Test
   public void testNewCustomField() throws Exception
   {
      Project project = getProject();
      JavaClass javaClass = generateEntity(project);

      getShell().execute("new-field custom --fieldName gamesPlayed --type org.jboss.CustomType");

      javaClass = project.getFacet(JavaSourceFacet.class).getJavaClass(javaClass);
      assertTrue(javaClass.hasField("gamesPlayed"));
      assertEquals("CustomType", javaClass.getField("gamesPlayed").getType());
      assertTrue(javaClass.hasImport("org.jboss.CustomType"));
      assertFalse(javaClass.hasSyntaxErrors());
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
      int originalSize = javaClass.getFields().size();

      getShell().execute("new-field number --fieldName gamesPlayed --type org.jboss.NotANumber");

      javaClass = project.getFacet(JavaSourceFacet.class).getJavaClass(javaClass);
      assertEquals(originalSize, javaClass.getFields().size());
      assertFalse(javaClass.hasImport("org.jboss.NotANumber"));
      assertFalse(javaClass.hasSyntaxErrors());
   }

   @Test
   public void testNewOneToOneRelationship() throws Exception
   {
      Project project = getProject();
      JavaClass rightEntity = generateEntity(project);
      JavaClass leftEntity = generateEntity(project);

      getShell().execute(
               "new-field oneToOne --fieldName right --fieldType ~.domain." + rightEntity.getName());

      leftEntity = project.getFacet(JavaSourceFacet.class).getJavaClass(leftEntity);

      assertTrue(leftEntity.hasAnnotation(Entity.class));
      assertTrue(leftEntity.hasField("right"));
      assertTrue(leftEntity.getField("right").getType().equals(rightEntity.getName()));
      assertTrue(leftEntity.getField("right").hasAnnotation(OneToOne.class));
      assertTrue(leftEntity.hasImport(rightEntity.getQualifiedName()));
      assertTrue(leftEntity.hasImport(OneToOne.class));
      assertFalse(leftEntity.hasSyntaxErrors());

      rightEntity = project.getFacet(JavaSourceFacet.class).getJavaClass(rightEntity);

      assertFalse(rightEntity.hasField("left"));
      assertFalse(rightEntity.hasImport(leftEntity.getQualifiedName()));
      assertFalse(rightEntity.hasImport(OneToOne.class));
      assertFalse(rightEntity.hasSyntaxErrors());
   }

   @Test
   public void testNewOneToOneRelationshipInverse() throws Exception
   {
      Project project = getProject();
      JavaClass rightEntity = generateEntity(project);
      JavaClass leftEntity = generateEntity(project);

      getShell().execute(
               "new-field oneToOne --fieldName right --fieldType ~.domain." + rightEntity.getName()
                        + " --inverseFieldName left");

      leftEntity = project.getFacet(JavaSourceFacet.class).getJavaClass(leftEntity);

      assertTrue(leftEntity.hasAnnotation(Entity.class));
      assertTrue(leftEntity.hasField("right"));
      assertTrue(leftEntity.getField("right").getType().equals(rightEntity.getName()));
      assertTrue(leftEntity.getField("right").hasAnnotation(OneToOne.class));
      assertTrue(leftEntity.hasImport(rightEntity.getQualifiedName()));
      assertTrue(leftEntity.hasImport(OneToOne.class));
      assertFalse(leftEntity.hasSyntaxErrors());

      rightEntity = project.getFacet(JavaSourceFacet.class).getJavaClass(rightEntity);

      assertTrue(rightEntity.hasField("left"));
      assertTrue(rightEntity.getField("left").getType().equals(leftEntity.getName()));
      assertTrue(rightEntity.getField("left").hasAnnotation(OneToOne.class));
      assertTrue(rightEntity.hasImport(leftEntity.getQualifiedName()));
      assertTrue(rightEntity.hasImport(OneToOne.class));
      assertFalse(rightEntity.hasSyntaxErrors());
   }

   @Test
   public void testNewManyToManyRelationship() throws Exception
   {
      Project project = getProject();
      JavaClass rightEntity = generateEntity(project);
      JavaClass leftEntity = generateEntity(project);

      getShell().execute(
               "new-field manyToMany --fieldName right --fieldType ~.domain." + rightEntity.getName());

      leftEntity = project.getFacet(JavaSourceFacet.class).getJavaClass(leftEntity);

      assertTrue(leftEntity.hasAnnotation(Entity.class));
      assertTrue(leftEntity.hasField("right"));
      assertTrue(leftEntity.getField("right").getType().equals("Set<" + rightEntity.getName() + ">"));
      assertTrue(leftEntity.getField("right").hasAnnotation(ManyToMany.class));
      assertNull(leftEntity.getField("right").getAnnotation(ManyToMany.class).getStringValue("mappedBy"));
      assertTrue(leftEntity.hasImport(rightEntity.getQualifiedName()));
      assertTrue(leftEntity.hasImport(ManyToMany.class));
      assertFalse(leftEntity.hasSyntaxErrors());

      rightEntity = project.getFacet(JavaSourceFacet.class).getJavaClass(rightEntity);

      assertFalse(rightEntity.hasField("left"));
      assertFalse(rightEntity.hasImport(leftEntity.getQualifiedName()));
      assertFalse(rightEntity.hasImport(ManyToMany.class));
      assertFalse(rightEntity.hasSyntaxErrors());
   }

   @Test
   public void testNewOneToManyRelationship() throws Exception
   {
      Project project = getProject();
      JavaClass rightEntity = generateEntity(project);
      JavaClass leftEntity = generateEntity(project);

      getShell().execute(
               "new-field oneToMany --fieldName right --fieldType ~.domain." + rightEntity.getName());

      leftEntity = project.getFacet(JavaSourceFacet.class).getJavaClass(leftEntity);

      assertTrue(leftEntity.hasAnnotation(Entity.class));
      assertTrue(leftEntity.hasField("right"));
      assertTrue(leftEntity.getField("right").getType().equals("Set<" + rightEntity.getName() + ">"));
      assertTrue(leftEntity.getField("right").hasAnnotation(OneToMany.class));
      assertNull(leftEntity.getField("right").getAnnotation(OneToMany.class).getStringValue("mappedBy"));
      assertTrue(leftEntity.hasImport(rightEntity.getQualifiedName()));
      assertTrue(leftEntity.hasImport(OneToMany.class));
      assertFalse(leftEntity.hasSyntaxErrors());

      rightEntity = project.getFacet(JavaSourceFacet.class).getJavaClass(rightEntity);

      assertFalse(rightEntity.hasField("left"));
      assertFalse(rightEntity.hasImport(leftEntity.getQualifiedName()));
      assertFalse(rightEntity.hasImport(OneToMany.class));
      assertFalse(rightEntity.hasSyntaxErrors());
   }

   @Test
   public void testNewManyToManyRelationshipInverse() throws Exception
   {
      Project project = getProject();
      JavaClass rightEntity = generateEntity(project);
      JavaClass leftEntity = generateEntity(project);

      getShell().execute(
               "new-field manyToMany --fieldName right --fieldType ~.domain." + rightEntity.getName()
                        + " --inverseFieldName left");

      leftEntity = project.getFacet(JavaSourceFacet.class).getJavaClass(leftEntity);

      assertTrue(leftEntity.hasAnnotation(Entity.class));
      assertTrue(leftEntity.hasField("right"));
      assertTrue(leftEntity.getField("right").getType().equals("Set<" + rightEntity.getName() + ">"));
      assertTrue(leftEntity.getField("right").hasAnnotation(ManyToMany.class));
      // assertEquals("left", leftEntity.getField("right").getAnnotation(ManyToMany.class).getStringValue("mappedBy"));
      assertTrue(leftEntity.hasImport(rightEntity.getQualifiedName()));
      assertTrue(leftEntity.hasImport(ManyToMany.class));
      assertFalse(leftEntity.hasSyntaxErrors());

      rightEntity = project.getFacet(JavaSourceFacet.class).getJavaClass(rightEntity);

      assertTrue(rightEntity.hasField("left"));
      assertTrue(rightEntity.getField("left").getType().equals("Set<" + leftEntity.getName() + ">"));
      assertTrue(rightEntity.getField("left").hasAnnotation(ManyToMany.class));
      // assertEquals("right", rightEntity.getField("left").getAnnotation(ManyToMany.class).getStringValue("mappedBy"));
      assertTrue(rightEntity.hasImport(leftEntity.getQualifiedName()));
      assertTrue(rightEntity.hasImport(ManyToMany.class));
      assertFalse(rightEntity.hasSyntaxErrors());
   }

   @Test
   public void testNewOneToManyRelationshipInverse() throws Exception
   {
      Project project = getProject();
      JavaClass rightEntity = generateEntity(project);
      JavaClass leftEntity = generateEntity(project);

      getShell().execute(
               "new-field oneToMany --fieldName right --fieldType ~.domain." + rightEntity.getName()
                        + " --inverseFieldName left");

      leftEntity = project.getFacet(JavaSourceFacet.class).getJavaClass(leftEntity);

      assertTrue(leftEntity.hasAnnotation(Entity.class));
      assertTrue(leftEntity.hasField("right"));
      assertTrue(leftEntity.getField("right").getType().equals("Set<" + rightEntity.getName() + ">"));
      assertTrue(leftEntity.getField("right").hasAnnotation(OneToMany.class));
      assertEquals("left", leftEntity.getField("right").getAnnotation(OneToMany.class).getStringValue("mappedBy"));
      assertTrue(leftEntity.hasImport(rightEntity.getQualifiedName()));
      assertTrue(leftEntity.hasImport(OneToMany.class));
      assertFalse(leftEntity.hasSyntaxErrors());

      rightEntity = project.getFacet(JavaSourceFacet.class).getJavaClass(rightEntity);

      assertTrue(rightEntity.hasField("left"));
      assertTrue(rightEntity.getField("left").getType().equals("Set<" + leftEntity.getName() + ">"));
      assertTrue(rightEntity.getField("left").hasAnnotation(ManyToOne.class));
      assertEquals("right", rightEntity.getField("left").getAnnotation(ManyToOne.class).getStringValue("mappedBy"));
      assertTrue(rightEntity.hasImport(leftEntity.getQualifiedName()));
      assertTrue(rightEntity.hasImport(ManyToOne.class));
      assertFalse(rightEntity.hasSyntaxErrors());
   }

}
