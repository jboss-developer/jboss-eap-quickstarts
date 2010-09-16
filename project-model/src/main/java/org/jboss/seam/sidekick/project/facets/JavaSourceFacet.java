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
package org.jboss.seam.sidekick.project.facets;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.jboss.seam.sidekick.parser.java.JavaClass;
import org.jboss.seam.sidekick.project.Facet;
import org.jboss.seam.sidekick.project.Project;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface JavaSourceFacet extends Facet
{
   /**
    * Get a list of {@link File}s representing the directories this project uses to contain {@link Project} source
    * documents (such as .java files.)
    */
   public List<File> getSourceFolders();

   /**
    * Get the {@link File} representing the folder this {@link Project} uses to store package-able source documents
    * (such as .java files.)
    */
   public File getSourceFolder();

   /**
    * Get the {@link File} representing the folder this {@link Project} uses to store test-scoped source documents (such
    * as .java files.) Files in this directory will never be packaged or deployed except when running Unit Tests.
    */
   public File getTestSourceFolder();

   /**
    * Return the {@link File} at the given path relative to {@link #getSourceFolder()}. The {@link File} object is
    * returned regardless of whether the target actually exists. To determine if the file exists, you should call
    * {@link File#exists()} on the return value of this method.
    */
   public File getSourceFile(String relativePath);

   /**
    * Return the {@link File} at the given path relative to {@link #getTestSourceFolder()}. The {@link File} object is
    * returned regardless of whether the target actually exists. To determine if the file exists, you should call
    * {@link File#exists()} on the return value of this method.
    */
   public File getTestSourceFile(String relativePath);

   /**
    * Create a Java file in the primary source directory: {@link #getSourceFolder()} - use information in the given
    * {@link JavaClass} to determine the appropriate package; packages will be created if necessary.
    * 
    * @param clazz The java class to create
    */
   public File createJavaClass(JavaClass clazz);

   /**
    * Create a Java file in the primary test source directory: {@link #getTestSourceFolder()} - use information in the
    * given {@link JavaClass} to determine the appropriate package; packages will be created if necessary.
    * 
    * @param clazz The java class to create
    */
   public File createTestJavaClass(JavaClass clazz);

   /**
    * Return the {@link JavaClass} at the given path relative to {@link #getSourceFolder()}.
    * 
    * @param relativePath The file or package path of the target Java source file.
    * @throws FileNotFoundException if the target file does not exist
    */
   public JavaClass getJavaClass(String relativePath) throws FileNotFoundException;

   /**
    * Return the {@link JavaClass} at the given path relative to {@link #getTestSourceFolder()}.
    * 
    * @param relativePath The file or package path of the target Java source file.
    */
   public JavaClass getTestJavaClass(String relativePath) throws FileNotFoundException;
}
