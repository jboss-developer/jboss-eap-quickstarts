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
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Typed;
import javax.enterprise.inject.spi.InjectionPoint;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.jboss.seam.sidekick.project.ProjectModelException;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@Typed()
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

   public MavenProject(File directory)
   {
      if (!directory.isDirectory())
      {
         throw new ProjectModelException("Cannot load project from directory that does not exist.");
      }
      projectRoot = directory.getAbsoluteFile();
   }

   private void verify()
   {
      this.getPOM();
   }

   @Produces
   @Default
   @LocatedAt
   public static MavenProject getCurrentDirectoryProject(InjectionPoint ip)
   {
      String path = null;
      if (ip.getAnnotated().getAnnotation(LocatedAt.class) != null)
      {
         path = ip.getAnnotated().getAnnotation(LocatedAt.class).value();
      }
      else
      {
         Set<Annotation> qualifiers = ip.getQualifiers();
         for (Annotation annotation : qualifiers)
         {
            if (annotation instanceof LocatedAt)
            {
               path = ((LocatedAt) annotation).value();
            }
         }
      }

      MavenProject result;
      if (path == null)
      {
         result = new MavenProject();
      }
      else
      {
         result = new MavenProject(new File(path));
      }
      return result;
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

   /**
    * Using the given groupId and artifactId patterns, scan the POM and
    * determine if the project has a dependency that matches.
    * 
    * @param groupId A regular expression to filter the groupId
    * @param artifactId A regular expression to filter the artifactId
    * @return True if the artifact can be located in the POM, false if
    *         otherwise.
    */
   public boolean hasDependency(String groupId, String artifactId)
   {
      for (Dependency dep : getPOM().getDependencies())
      {
         String aid = dep.getArtifactId();
         String gid = dep.getGroupId();
         if ((aid != null) && aid.matches(artifactId))
         {
            if ((gid != null) && gid.matches(groupId))
            {
               return true;
            }
         }
      }
      return false;
   }
}
