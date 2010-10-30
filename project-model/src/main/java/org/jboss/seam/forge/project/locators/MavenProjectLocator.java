/*
 * JBoss, by Red Hat.
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
package org.jboss.seam.forge.project.locators;

import java.io.File;

import org.jboss.seam.forge.project.Facet;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.facets.builtin.MavenCoreFacetImpl;
import org.jboss.seam.forge.project.model.ProjectImpl;

/**
 * Locate a Maven project starting in the current directory, and progressing up the chain of parent directories until a
 * project is found, or the root directory is found. If a project is found, return the {@link File} referring to the
 * directory containing that project, or return null if no projects were found.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class MavenProjectLocator implements ProjectLocator
{
   @Override
   public Project findProjectRecursively(final File startingDirectory)
   {
      File root = startingDirectory.getAbsoluteFile();
      while ((findProject(root) == null) && (root.getParentFile() != null))
      {
         root = root.getParentFile();
      }

      Project result = findProject(root);
      return result;
   }

   @Override
   public Project findProject(final File directory)
   {
      File pom = new File(directory.getAbsolutePath() + File.separator + "pom.xml");

      Project result = null;
      if (pom.exists())
      {
         result = new ProjectImpl(directory.getAbsoluteFile());
         Facet facet = new MavenCoreFacetImpl();
         facet.setProject(result);
         result.registerFacet(facet);
      }
      return result;
   }
}
