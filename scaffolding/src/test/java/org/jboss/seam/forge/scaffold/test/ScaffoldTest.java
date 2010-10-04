package org.jboss.seam.forge.scaffold.test;

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

import java.io.FileNotFoundException;

import javax.persistence.Entity;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.facets.JavaSourceFacet;
import org.jboss.seam.forge.project.util.Packages;
import org.jboss.seam.forge.scaffold.ScaffoldingFacet;
import org.jboss.seam.forge.shell.ShellImpl;
import org.jboss.seam.forge.test.AbstractShellTest;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@RunWith(Arquillian.class)
public class ScaffoldTest extends AbstractShellTest
{
   @Deployment
   public static JavaArchive extendDeployment()
   {
      return getDeployment().addClass(ShellImpl.class)
            .addPackages(true, ScaffoldingFacet.class.getPackage());
   }

   @Test
   public void testNewEntity() throws Exception
   {
      Project project = initializeJavaProject();

      queueInputLines("y");
      getShell().execute("install scaffold");

      String entityName = "Goofy";
      queueInputLines("");
      getShell().execute("new-entity " + entityName);

      String pkg = project.getFacet(ScaffoldingFacet.class).getEntityPackage() + "." + entityName;
      String path = Packages.toFileSyntax(pkg) + ".java";
      JavaClass javaClass = project.getFacet(JavaSourceFacet.class).getJavaClass(path);

      assertFalse(javaClass.hasSyntaxErrors());

      getShell().execute("new-field int gamesPlayed");
      getShell().execute("new-field int achievementsEarned");

      queueInputLines("gamesWon");
      getShell().execute("new-field int int");

      queueInputLines("gamesLost");
      getShell().execute("new-field int #$%#");

      javaClass = project.getFacet(JavaSourceFacet.class).getJavaClass(path);
      assertTrue(javaClass.hasAnnotation(Entity.class));
      assertTrue(javaClass.hasField("gamesPlayed"));
      assertTrue(javaClass.hasField("achievementsEarned"));
      assertTrue(javaClass.hasField("gamesWon"));
      assertTrue(javaClass.hasField("gamesLost"));

      assertFalse(javaClass.hasSyntaxErrors());
   }

   @Test
   public void testNewStringField() throws Exception
   {
      Project project = initializeJavaProject();

      queueInputLines("y");
      getShell().execute("install scaffold");

      String entityName = "Goofy";
      queueInputLines("");
      getShell().execute("new-entity " + entityName);

      String pkg = project.getFacet(ScaffoldingFacet.class).getEntityPackage() + "." + entityName;
      String path = Packages.toFileSyntax(pkg) + ".java";
      JavaClass javaClass = project.getFacet(JavaSourceFacet.class).getJavaClass(path);

      assertFalse(javaClass.hasSyntaxErrors());

      getShell().execute("new-field int gamesPlayed");
      getShell().execute("new-field int achievementsEarned");

      queueInputLines("gamesWon");
      getShell().execute("new-field int int");

      queueInputLines("gamesLost");
      getShell().execute("new-field int #$%#");

      javaClass = project.getFacet(JavaSourceFacet.class).getJavaClass(path);
      assertTrue(javaClass.hasAnnotation(Entity.class));
      assertTrue(javaClass.hasField("gamesPlayed"));
      assertTrue(javaClass.hasField("achievementsEarned"));
      assertTrue(javaClass.hasField("gamesWon"));
      assertTrue(javaClass.hasField("gamesLost"));

      assertFalse(javaClass.hasSyntaxErrors());
   }

   @Test(expected = FileNotFoundException.class)
   public void testNewFieldWithoutEntityDoesNotCreateFile() throws Exception
   {
      Project project = initializeJavaProject();

      queueInputLines("y");
      getShell().execute("install scaffold");

      String entityName = "Goofy";

      queueInputLines(entityName);
      getShell().execute("new-field int gamesPlayed");

      String pkg = project.getFacet(ScaffoldingFacet.class).getEntityPackage() + "." + entityName;
      String path = Packages.toFileSyntax(pkg) + ".java";

      JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);
      JavaClass javaClass = java.getJavaClass(path);
   }
}
