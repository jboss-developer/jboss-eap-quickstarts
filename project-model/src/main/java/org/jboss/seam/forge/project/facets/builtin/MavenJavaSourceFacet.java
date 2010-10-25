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
package org.jboss.seam.forge.project.facets.builtin;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.jboss.seam.forge.parser.JavaParser;
import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.parser.java.util.Formatter;
import org.jboss.seam.forge.project.Facet;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.ProjectModelException;
import org.jboss.seam.forge.project.constraints.RequiresFacets;
import org.jboss.seam.forge.project.facets.JavaSourceFacet;
import org.jboss.seam.forge.project.facets.MavenCoreFacet;
import org.jboss.seam.forge.project.util.Packages;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@RequiresFacets({ MavenCoreFacet.class })
public class MavenJavaSourceFacet implements JavaSourceFacet, Facet
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
      return new File(project.getProjectRoot().getAbsolutePath() + File.separator + "src" + File.separator + "main"
               + File.separator + "java").getAbsoluteFile();
   }

   @Override
   public File getTestSourceFolder()
   {
      return new File(project.getProjectRoot().getAbsolutePath() + File.separator + "src" + File.separator + "test"
               + File.separator + "java").getAbsoluteFile();
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
      MavenCoreFacet mavenFacet = project.getFacet(MavenCoreFacet.class);
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

         // FIXME WOW this needs to be simplified somehow...
         MavenCoreFacet maven = project.getFacet(MavenCoreFacet.class);
         Model pom = maven.getPOM();
         Build build = pom.getBuild();
         if (build == null)
         {
            build = new Build();
         }
         List<Plugin> plugins = build.getPlugins();
         Plugin javaSourcePlugin = null;
         for (Plugin plugin : plugins)
         {
            if ("org.apache.maven.plugins".equals(plugin.getGroupId())
                     && "maven-compiler-plugin".equals(plugin.getArtifactId()))
            {
               javaSourcePlugin = plugin;
            }
         }

         if (javaSourcePlugin == null)
         {
            javaSourcePlugin = new Plugin();
            javaSourcePlugin.setGroupId("org.apache.maven.plugins");
            javaSourcePlugin.setArtifactId("maven-compiler-plugin");

            try
            {
               Xpp3Dom dom = Xpp3DomBuilder.build(
                        new ByteArrayInputStream(
                                 "<configuration><source>1.6</source><target>1.6</target></configuration>".getBytes()),
                        "UTF-8");

               javaSourcePlugin.setConfiguration(dom);
            }
            catch (Exception e)
            {
               throw new ProjectModelException(e);
            }
         }

         build.addPlugin(javaSourcePlugin);
         pom.setBuild(build);
         maven.setPOM(pom);

      }
      project.registerFacet(this);
      return this;
   }

   @Override
   public File getSourceFile(final String relativePath)
   {
      File target = new File(getSourceFolder() + File.separator + relativePath).getAbsoluteFile();
      return target;
   }

   @Override
   public File getTestSourceFile(final String relativePath)
   {
      return new File(getTestSourceFolder() + File.separator + relativePath).getAbsoluteFile();
   }

   @Override
   public JavaClass getJavaClass(final String relativePath) throws FileNotFoundException
   {
      File target = getSourceFile(relativePath);

      if (!target.exists())
      {
         File secondary = getSourceFile(Packages.toFileSyntax(relativePath) + ".java");
         if (secondary.exists())
         {
            target = secondary;
         }
      }

      return JavaParser.parse(target);
   }

   @Override
   public JavaClass getTestJavaClass(final String relativePath) throws FileNotFoundException
   {
      File target = getTestSourceFile(relativePath);

      if (!target.exists())
      {
         File secondary = getTestSourceFile(Packages.toFileSyntax(relativePath) + ".java");
         if (secondary.exists())
         {
            target = secondary;
         }
      }

      return JavaParser.parse(target);
   }

   @Override
   public String getBasePackage()
   {
      return project.getFacet(MavenCoreFacet.class).getMavenProject().getGroupId();
   }

   @Override
   public File getBasePackageFile()
   {
      String base = Packages.toFileSyntax(getBasePackage());
      return new File(getSourceFolder().getAbsolutePath() + File.separator + base).getAbsoluteFile();
   }

   @Override
   public JavaClass getJavaClass(final JavaClass javaClass) throws FileNotFoundException
   {
      String pkg = javaClass.getPackage() + "." + javaClass.getName();
      String path = Packages.toFileSyntax(pkg) + ".java";
      return JavaParser.parse(getSourceFile(path));
   }

   @Override
   public JavaClass getTestJavaClass(final JavaClass javaClass) throws FileNotFoundException
   {
      String pkg = javaClass.getPackage() + "." + javaClass.getName();
      String path = Packages.toFileSyntax(pkg) + ".java";
      return JavaParser.parse(getTestSourceFile(path));
   }

   private File saveJavaFile(final File sourceFolder, final JavaClass clazz)
   {
      String path = sourceFolder.getAbsolutePath() + File.separator + Packages.toFileSyntax(clazz.getPackage());
      File file = new File(path + File.separator + clazz.getName() + ".java");

      getProject().writeFile(Formatter.format(clazz).toCharArray(), file);
      // TODO event.fire(Created new Java file);
      return file;
   }

   @Override
   public File saveJavaClass(final JavaClass clazz)
   {
      return saveJavaFile(getSourceFolder(), clazz);
   }

   @Override
   public File saveTestJavaClass(final JavaClass clazz)
   {
      return saveJavaFile(getTestSourceFolder(), clazz);
   }
}
