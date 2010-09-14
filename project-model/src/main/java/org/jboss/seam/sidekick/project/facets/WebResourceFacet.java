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
import java.util.List;

import org.jboss.seam.sidekick.project.Facet;

/**
 * A {@link Facet} containing APIs to interact with Java Web Projects
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface WebResourceFacet extends Facet
{
   /**
    * Get the default Web Root directory (this is the directory containing resources to be deployed to the web-root URL
    * when the application is published. (E.g. In a maven project, files in the <code>/project/src/main/webapp</code>
    * directory are typically published to the root URL: <code>http://localhost:8080/project/</code> root directory. In
    * an eclipse project, this folder is typically located by default at: <code>/project/WebContent/</code>.)
    */
   File getWebRootDirectory();

   /**
    * Get a list containing all possible Web Root directories for the current project.
    */
   List<File> getWebRootDirectories();

   /**
    * At the given path/filename relative to the project Web Root directory: {@link #getWebRootDirectory()} - create a
    * file containing the given bytes.
    * 
    * @return a handle to the {@link File} that was created.
    */
   File createWebResource(char[] bytes, String relativeFilename);
}
