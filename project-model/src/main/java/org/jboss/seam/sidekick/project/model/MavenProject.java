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
package org.jboss.seam.sidekick.project.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.jboss.seam.sidekick.project.ProjectModelException;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class MavenProject extends AbstractProject
{
   private File projectRoot = null;

   public MavenProject()
   {
      this(getCurrentWorkingDir());
      File originalRoot = projectRoot.getAbsoluteFile();
      File pom = new File(projectRoot + "/pom.xml");
      while (!pom.exists())
      {
         projectRoot = projectRoot.getParentFile();
         pom = new File(projectRoot + "/pom.xml");
      }

      if (!pom.exists())
      {
         projectRoot = originalRoot;
         throw new ProjectModelException("Could not locate pom.xml in this directory or any parent directories of: " + originalRoot);
      }
      verify();
   }

   private void verify()
   {
      this.getPOM();
   }

   public MavenProject(File directory)
   {
      if (!directory.isDirectory())
      {
         throw new ProjectModelException("Cannot load project from directory that does not exist.");
      }
      projectRoot = directory.getAbsoluteFile();
   }

   public MavenProject(String path)
   {
      this(new File(path));
   }

   @Override
   public List<File> getSourceFolders()
   {
      List<File> result = new ArrayList<File>();
      result.add(getDefaultSourceFolder());
      result.add(new File(getProjectRoot().getAbsolutePath() + "/src/test/java"));
      return result;
   }

   @Override
   public File getDefaultSourceFolder()
   {
      return new File(getProjectRoot().getAbsolutePath() + "/src/main/java");
   }

   @Override
   public File getProjectRoot()
   {
      return projectRoot;
   }

   public Model getPOM()
   {
      try
      {
         Model result = new Model();
         result.setPomFile(getPOMFile());

         MavenXpp3Reader reader = new MavenXpp3Reader();
         FileInputStream stream = new FileInputStream(getPOMFile());
         if (stream.available() > 0)
         {
            result = reader.read(stream);
         }
         stream.close();

         return result;
      }
      catch (IOException e)
      {
         throw new ProjectModelException("Could not open POM file: " + getPOMFile(), e);
      }
      catch (XmlPullParserException e)
      {
         throw new ProjectModelException("Could not parse POM file: " + getPOMFile(), e);
      }
   }

   public void setPOM(Model pom)
   {
      try
      {
         MavenXpp3Writer writer = new MavenXpp3Writer();
         FileWriter fw = new FileWriter(getPOMFile());
         writer.write(fw, pom);
         fw.close();
      }
      catch (IOException e)
      {
         throw new ProjectModelException("Could not write POM file: " + getPOMFile(), e);
      }
   }

   private File getPOMFile()
   {
      File file = new File(getProjectRoot() + "/pom.xml");
      if (!file.exists())
      {
         try
         {
            if (!file.createNewFile())
            {
               throw new ProjectModelException("Could not create POM file because it already exists: " + file);
            }
         }
         catch (IOException e)
         {
            throw new ProjectModelException("Could not create POM file: " + file, e);
         }
      }
      return file;
   }

   /*
    * Producers
    */
   private static File getCurrentWorkingDir()
   {
      return new File("").getAbsoluteFile();
   }

   @Produces
   @LocatedAt
   public static MavenProject getCurrentDirectoryProject(InjectionPoint ip)
   {
      String path = ip.getAnnotated().getAnnotation(LocatedAt.class).value();
      if ("".equals(path))
      {
         path = ip.getMember().getName();
      }
      MavenProject result = new MavenProject(path);
      return result;
   }
}
