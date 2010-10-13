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
package org.jboss.seam.forge.persistence;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;

import org.jboss.seam.forge.parser.JavaParser;
import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.project.Facet;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.facets.JavaSourceFacet;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class PersistenceFacet implements Facet
{
   private Project project;

   private final FilenameFilter entityFileFilter = new FilenameFilter()
   {
      @Override
      public boolean accept(File dir, String name)
      {
         return name.endsWith(".java");
      }
   };

   private final FileFilter directoryFilter = new FileFilter()
   {
      @Override
      public boolean accept(File file)
      {
         return file.isDirectory();
      }
   };

   @Override
   public Set<Class<? extends Facet>> getDependencies()
   {
      Set<Class<? extends Facet>> result = new HashSet<Class<? extends Facet>>();
      result.add(JavaSourceFacet.class);
      return result;
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
   public Facet install()
   {
      if (!isInstalled())
      {
         File entityRoot = getEntityPackageFile();
         if (!entityRoot.exists())
         {
            entityRoot.mkdirs();
         }
      }
      project.registerFacet(this);
      return this;
   }

   public String getEntityPackage()
   {
      JavaSourceFacet sourceFacet = project.getFacet(JavaSourceFacet.class);
      return sourceFacet.getBasePackage() + ".domain";
   }

   public File getEntityPackageFile()
   {
      JavaSourceFacet sourceFacet = project.getFacet(JavaSourceFacet.class);
      return new File(sourceFacet.getBasePackageFile() + File.separator + "domain").getAbsoluteFile();
   }

   @Override
   public boolean isInstalled()
   {
      return getEntityPackageFile().exists();
   }

   public List<JavaClass> getAllEntities()
   {
      File packageFile = getEntityPackageFile();
      return findEntitiesInFolder(packageFile);
   }

   private List<JavaClass> findEntitiesInFolder(File packageFile)
   {                        
      List<JavaClass> result = new ArrayList<JavaClass>();
      if (packageFile.exists())
      {
         for (File source : packageFile.listFiles(entityFileFilter))
         {
            try
            {
               JavaClass javaClass = JavaParser.parse(source);
               if (javaClass.hasAnnotation(Entity.class))
               {
                  result.add(javaClass);
               }
            }
            catch (FileNotFoundException e)
            {
               // Meh, oh well.
            }
         }

         for (File source : packageFile.listFiles(directoryFilter))
         {
            List<JavaClass> subResults = findEntitiesInFolder(source);
            result.addAll(subResults);
         }
      }
      return result;
   }
}
