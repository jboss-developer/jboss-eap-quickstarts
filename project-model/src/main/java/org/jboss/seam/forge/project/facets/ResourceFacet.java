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
package org.jboss.seam.forge.project.facets;

import java.io.File;
import java.util.List;

import org.jboss.seam.forge.project.Facet;
import org.jboss.seam.forge.project.Project;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface ResourceFacet extends Facet
{
   /**
    * Get a list of {@link File}s representing the directories this project uses
    * to contain {@link Project} non-source documents (such as configuration
    * files.)
    */
   public List<File> getResourceFolders();

   /**
    * Get the {@link File} representing the folder this {@link Project} uses to
    * store package-able, non-source documents (such as configuration files.)
    */
   public File getResourceFolder();

   /**
    * Get the {@link File} representing the folder this {@link Project} uses to
    * store test-scoped non-source documents (such as configuration files.)
    * Files in this directory will never be packaged or deployed except when
    * running Unit Tests.
    */
   public File getTestResourceFolder();

   /**
    * At the given path/filename relative to the project resources directory:
    * {@link #getResourceFolder()} - create a file containing the given bytes.
    * 
    * @return a handle to the {@link File} that was created.
    */
   File createResource(char[] bytes, String relativeFilename);

   /**
    * At the given path/filename relative to the project test resources
    * directory: {@link #getTestResourceFolder()} - create a file containing the
    * given bytes.
    * 
    * @return a handle to the {@link File} that was created.
    */
   File createTestResource(char[] bytes, String relativeFilename);

   /**
    * Return the {@link File} at the given path relative to
    * {@link #getResourceFolder()}. The {@link File} object is returned
    * regardless of whether the target actually exists. To determine if the file
    * exists, you should call {@link File#exists()} on the return value of this
    * method.
    */
   public File getResource(String relativePath);

   /**
    * Attempt to locate a {@link File} at the given path relative to
    * {@link #getTestResourceFolder()}. The {@link File} object is returned
    * regardless of whether the target actually exists. To determine if the file
    * exists, you should call {@link File#exists()} on the return value of this
    * method.
    */
   public File getTestResource(String relativePath);
}
