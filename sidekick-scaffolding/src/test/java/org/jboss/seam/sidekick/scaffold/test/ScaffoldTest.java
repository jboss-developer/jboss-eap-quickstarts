package org.jboss.seam.sidekick.scaffold.test;

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

import java.util.List;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.sidekick.parser.java.JavaClass;
import org.jboss.seam.sidekick.parser.java.SyntaxError;
import org.jboss.seam.sidekick.project.Project;
import org.jboss.seam.sidekick.project.facets.JavaSourceFacet;
import org.jboss.seam.sidekick.project.util.Packages;
import org.jboss.seam.sidekick.scaffold.ScaffoldingFacet;
import org.jboss.seam.sidekick.shell.ShellImpl;
import org.jboss.seam.sidekick.test.AbstractShellTest;
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
      initializeJavaProject();

      queueInputLines("", "1");
      getShell().execute("install scaffold");

      String entityName = "Goofy";
      queueInputLines("");
      getShell().execute("scaffold new-entity " + entityName);

      Project project = getProject();
      String pkg = project.getFacet(ScaffoldingFacet.class).getEntityPackage() + "." + entityName;
      String path = Packages.toFileSyntax(pkg) + ".java";
      JavaClass javaClass = project.getFacet(JavaSourceFacet.class).getJavaClass(path);
      List<SyntaxError> syntaxErrors = javaClass.getSyntaxErrors();
      assertFalse(javaClass.hasSyntaxErrors());
   }
}
