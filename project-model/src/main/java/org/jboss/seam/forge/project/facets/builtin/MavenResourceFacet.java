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
package org.jboss.seam.forge.project.facets.builtin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.Dependent;

import org.jboss.seam.forge.project.Facet;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.facets.FacetNotFoundException;
import org.jboss.seam.forge.project.facets.MavenCoreFacet;
import org.jboss.seam.forge.project.facets.ResourceFacet;
import org.jboss.seam.forge.resources.DirectoryResource;
import org.jboss.seam.forge.resources.FileResource;
import org.jboss.seam.forge.shell.plugins.Alias;
import org.jboss.seam.forge.shell.plugins.RequiresFacet;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Dependent
@Alias("forge.maven.ResourceFacet")
@RequiresFacet(MavenCoreFacet.class)
public class MavenResourceFacet implements ResourceFacet, Facet
{
   private Project project;

   @Override
   public List<DirectoryResource> getResourceFolders()
   {
      List<DirectoryResource> result = new ArrayList<DirectoryResource>();
      result.add(getResourceFolder());
      result.add(getTestResourceFolder());
      return result;
   }

   @Override
   public DirectoryResource getResourceFolder()
   {
      return project.getProjectRoot().getChildDirectory("src" + File.separator + "main"
               + File.separator + "resources");
   }

   @Override
   public DirectoryResource getTestResourceFolder()
   {
      return project.getProjectRoot().getChildDirectory("src" + File.separator + "test"
               + File.separator + "resources");
   }

   @Override
   public Project getProject()
   {
      return project;
   }

   @Override
   public void setProject(final Project project)
   {
      this.project = project;
   }

   @Override
   public boolean isInstalled()
   {
      try
      {
         project.getFacet(MavenCoreFacet.class);
         return getResourceFolder().exists();
      }
      catch (FacetNotFoundException e)
      {
         return false;
      }
   }

   @Override
   public boolean install()
   {
      if (!this.isInstalled())
      {
         for (DirectoryResource folder : this.getResourceFolders())
         {
            folder.mkdirs();
         }
      }
      return true;
   }

   @Override
   public FileResource<?> getResource(final String relativePath)
   {
      return (FileResource<?>) getResourceFolder().getChild(relativePath);
   }

   @Override
   public FileResource<?> getTestResource(final String relativePath)
   {
      return (FileResource<?>) getTestResourceFolder().getChild(relativePath);
   }

   @Override
   public FileResource<?> createResource(final char[] bytes, final String relativeFilename)
   {
      FileResource<?> file = (FileResource<?>) getResourceFolder().getChild(relativeFilename);
      file.setContents(bytes);
      return file;
   }

   @Override
   public FileResource<?> createTestResource(final char[] bytes, final String relativeFilename)
   {
      FileResource<?> file = (FileResource<?>) getTestResourceFolder().getChild(relativeFilename);
      file.setContents(bytes);
      return file;
   }
}
