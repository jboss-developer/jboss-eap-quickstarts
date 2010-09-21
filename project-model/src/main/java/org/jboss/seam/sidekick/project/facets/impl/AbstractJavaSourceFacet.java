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
package org.jboss.seam.sidekick.project.facets.impl;

import java.io.File;

import org.jboss.seam.sidekick.parser.java.JavaClass;
import org.jboss.seam.sidekick.project.facets.JavaSourceFacet;
import org.jboss.seam.sidekick.project.util.Packages;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public abstract class AbstractJavaSourceFacet implements JavaSourceFacet
{
   // TODO the impl part of the project model API needs to be split into a
   // separate package so that plugin authors see a clean API
   private File createJavaFile(final File sourceFolder, final String classPackage, final String className,
            final char[] data)
   {
      String path = sourceFolder.getAbsolutePath() + File.separator + Packages.toFileSyntax(classPackage);
      File file = new File(path + File.separator + className + ".java");

      getProject().writeFile(data, file);
      // TODO event.fire(Created new Java file);
      return file;
   }

   @Override
   public File createJavaClass(final JavaClass clazz)
   {
      return createJavaFile(getSourceFolder(), clazz.getPackage(), clazz.getName(), clazz.toString().toCharArray());
   }

   @Override
   public File createTestJavaClass(final JavaClass clazz)
   {
      return createJavaFile(getTestSourceFolder(), clazz.getPackage(), clazz.getName(), clazz.toString().toCharArray());
   }
}
