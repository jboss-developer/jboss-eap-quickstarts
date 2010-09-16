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
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.project.ProjectBuildingResult;
import org.jboss.seam.sidekick.parser.JavaParser;
import org.jboss.seam.sidekick.parser.java.JavaClass;
import org.jboss.seam.sidekick.project.Facet;
import org.jboss.seam.sidekick.project.Project;
import org.jboss.seam.sidekick.project.facets.JavaSourceFacet;
import org.jboss.seam.sidekick.project.facets.MavenFacet;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class MavenJavaSourceFacet extends AbstractJavaSourceFacet implements JavaSourceFacet
{
   private Project project;

   @Override
   public List<File> getSourceFolders()
   {
      List<File> result = new ArrayList<File>();
      result.add(getSourceFolder());
      result.add(getTestSourceFolder());
      return result;
   }

   @Override
   public File getSourceFolder()
   {
      ProjectBuildingResult result = project.getFacet(MavenFacet.class).getProjectBuildingResult();
      String directory = result.getProject().getBuild().getSourceDirectory();
      return new File(directory).getAbsoluteFile();
   }

   @Override
   public File getTestSourceFolder()
   {
      ProjectBuildingResult result = project.getFacet(MavenFacet.class).getProjectBuildingResult();
      String directory = result.getProject().getBuild().getTestSourceDirectory();
      return new File(directory).getAbsoluteFile();
   }

   @Override
   public Project getProject()
   {
      return project;
   }

   @Override
   public Facet init(final Project project)
   {
      this.project = project;
      return this;
   }

   @Override
   public boolean isInstalled()
   {
      MavenFacet mavenFacet = project.getFacet(MavenFacet.class);
      return getSourceFolder().exists() && (mavenFacet != null) && mavenFacet.isInstalled();
   }

   @Override
   public Facet install()
   {
      if (!this.isInstalled())
      {
         for (File folder : this.getSourceFolders())
         {
            folder.mkdirs();
         }
      }
      project.registerFacet(this);
      return this;
   }

   @Override
   public Set<Class<? extends Facet>> getDependencies()
   {
      Set<Class<? extends Facet>> result = new HashSet<Class<? extends Facet>>();
      result.add(MavenFacet.class);
      return result;
   }

   @Override
   public File getSourceFile(final String relativePath)
   {
      return new File(getSourceFolder() + File.separator + relativePath).getAbsoluteFile();
   }

   @Override
   public File getTestSourceFile(final String relativePath)
   {
      return new File(getTestSourceFolder() + File.separator + relativePath).getAbsoluteFile();
   }

   @Override
   public JavaClass getJavaClass(final String relativePath) throws FileNotFoundException
   {
      return JavaParser.parse(getSourceFile(relativePath));
   }

   @Override
   public JavaClass getTestJavaClass(final String relativePath) throws FileNotFoundException
   {
      return JavaParser.parse(getTestSourceFile(relativePath));
   }
}
