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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.persistence.Entity;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.persistence.PersistenceFacet;
import org.jboss.seam.forge.persistence.test.plugins.util.AbstractJPATest;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.facets.JavaSourceFacet;
import org.jboss.seam.forge.project.util.Packages;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@RunWith(Arquillian.class)
public class NewEntityPluginTest extends AbstractJPATest
{

   @Test
   public void testNewEntity() throws Exception
   {
      Project project = getProject();

      String entityName = "Goofy";
      queueInputLines("");
      getShell().execute("new-entity --named " + entityName);

      String pkg = project.getFacet(PersistenceFacet.class).getEntityPackage() + "." + entityName;
      String path = Packages.toFileSyntax(pkg) + ".java";
      JavaClass javaClass = (JavaClass) project.getFacet(JavaSourceFacet.class).getJavaResource(path).getJavaSource();

      assertFalse(javaClass.hasSyntaxErrors());
      javaClass = (JavaClass) project.getFacet(JavaSourceFacet.class).getJavaResource(javaClass).getJavaSource();
      assertTrue(javaClass.hasAnnotation(Entity.class));
      assertFalse(javaClass.hasSyntaxErrors());

      assertTrue(javaClass.hasImport(Entity.class));
      assertTrue(javaClass.hasField("id"));
      assertTrue(javaClass.hasField("version"));
   }

   @Test
   public void testNewEntityCorrectsInvalidInput() throws Exception
   {
      Project project = getProject();
      JavaClass javaClass = generateEntity(project);

      queueInputLines("gamesWon");
      getShell().execute("new-field int --fieldName int");

      queueInputLines("gamesLost");
      getShell().execute("new-field int --fieldName #$%#");

      javaClass = (JavaClass) project.getFacet(JavaSourceFacet.class).getJavaResource(javaClass).getJavaSource();
      assertTrue(javaClass.hasAnnotation(Entity.class));
      assertTrue(javaClass.hasImport(Entity.class));
      assertTrue(javaClass.hasField("gamesWon"));
      assertTrue(javaClass.hasField("gamesLost"));

      assertFalse(javaClass.hasSyntaxErrors());
   }

}
