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
package org.jboss.seam.forge.spec.jpa.test.plugins.util;

import static org.junit.Assert.assertFalse;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.facets.JavaSourceFacet;
import org.jboss.seam.forge.project.util.Packages;
import org.jboss.seam.forge.spec.jpa.PersistenceFacet;
import org.jboss.seam.forge.test.SingletonAbstractShellTest;
import org.junit.Before;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public abstract class AbstractJPATest extends SingletonAbstractShellTest
{
   private int count = 0;

   @Before
   @Override
   public void beforeTest() throws IOException
   {
      super.beforeTest();
      initializeJavaProject();
      if ((getProject() != null) && !getProject().hasFacet(PersistenceFacet.class))
      {
         getShell().execute("install forge.spec.jpa");
      }
   }

   protected JavaClass generateEntity(final Project project) throws FileNotFoundException
   {
      String entityName = "Goofy" + count++;
      queueInputLines("");
      getShell().execute("new-entity --named " + entityName);

      String pkg = project.getFacet(PersistenceFacet.class).getEntityPackage() + "." + entityName;
      String path = Packages.toFileSyntax(pkg) + ".java";
      JavaClass javaClass = (JavaClass) project.getFacet(JavaSourceFacet.class).getJavaResource(path).getJavaSource();

      assertFalse(javaClass.hasSyntaxErrors());
      return javaClass;
   }
}
