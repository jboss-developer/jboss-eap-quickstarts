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
package org.jboss.seam.forge.web;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import org.jboss.seam.forge.project.Facet;
import org.jboss.seam.forge.project.PackagingType;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.constraints.RequiresFacets;
import org.jboss.seam.forge.project.constraints.RequiresPackagingTypes;
import org.jboss.seam.forge.project.dependencies.Dependency;
import org.jboss.seam.forge.project.dependencies.DependencyBuilder;
import org.jboss.seam.forge.project.facets.DependencyFacet;
import org.jboss.seam.forge.project.facets.MetadataFacet;
import org.jboss.seam.forge.project.facets.WebResourceFacet;
import org.jboss.shrinkwrap.descriptor.api.DescriptorImporter;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.spec.servlet.web.WebAppDescriptor;
import org.jboss.shrinkwrap.descriptor.impl.spec.servlet.web.WebAppDescriptorImpl;
import org.jboss.shrinkwrap.descriptor.impl.spec.servlet.web.WebAppModel;
import org.jboss.shrinkwrap.descriptor.spi.SchemaDescriptorProvider;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@Named("servlet")
@RequiresFacets({ WebResourceFacet.class, DependencyFacet.class })
@RequiresPackagingTypes({ PackagingType.WAR })
public class ServletFacet implements Facet
{

   private static final Dependency dep =
            DependencyBuilder.create("org.jboss.spec:jboss-javaee-6.0:1.0.0.CR1:provided:basic");

   private Project project;

   /*
    * Servlet Facet Methods
    */
   @SuppressWarnings("unchecked")
   public WebAppModel getConfig()
   {
      DescriptorImporter<WebAppDescriptor> importer = Descriptors.importAs(WebAppDescriptor.class);
      WebAppDescriptor descriptor = importer.from(getConfigFile());
      WebAppModel model = ((SchemaDescriptorProvider<WebAppModel>) descriptor).getSchemaModel();
      return model;
   }

   public void saveConfig(final WebAppModel model)
   {
      WebAppDescriptor descriptor = new WebAppDescriptorImpl(model);
      String output = descriptor.exportAsString();
      project.writeFile(output, getConfigFile());
   }

   private File getConfigFile()
   {
      File webRoot = project.getFacet(WebResourceFacet.class).getWebRootDirectory();
      return new File(webRoot + File.separator + "WEB-INF" + File.separator + "web.xml");
   }

   /**
    * List all servlet resource files.
    */
   public List<File> getResources()
   {
      File webRoot = project.getFacet(WebResourceFacet.class).getWebRootDirectory();
      return listChildrenRecursively(webRoot, new FileFilter()
      {
         @Override
         public boolean accept(final File file)
         {
            return true;
         }
      });
   }

   public List<File> getResources(final FileFilter filter)
   {
      File webRoot = project.getFacet(WebResourceFacet.class).getWebRootDirectory();
      return listChildrenRecursively(webRoot, filter);
   }

   private List<File> listChildrenRecursively(final File current, final FileFilter filter)
   {
      List<File> result = new ArrayList<File>();
      File[] list = current.listFiles(filter);
      if (list != null)
      {
         for (File file : list)
         {
            if (file.isDirectory())
            {
               result.addAll(listChildrenRecursively(file, filter));
            }
            result.add(file);
         }
      }
      return result;
   }

   /*
    * Facet Methods
    */
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
      File webRoot = project.getFacet(WebResourceFacet.class).getWebRootDirectory();
      return webRoot.exists() && getConfigFile().exists() && project.getFacet(DependencyFacet.class).hasDependency(dep);
   }

   @Override
   public Facet install()
   {
      if (!isInstalled())
      {
         String projectName = project.getFacet(MetadataFacet.class).getProjectName();

         project.getFacet(DependencyFacet.class).addDependency(dep);

         File webRoot = project.getFacet(WebResourceFacet.class).getWebRootDirectory();
         if (!webRoot.exists())
         {
            project.mkdirs(webRoot);
            webRoot.mkdirs();
         }

         File descriptor = getConfigFile();
         if (!descriptor.exists())
         {
            WebAppDescriptor unit = Descriptors.create(WebAppDescriptor.class)
                     .displayName(projectName)
                     .sessionTimeout(30)
                     .welcomeFile("/index.html");

            project.writeFile(unit.exportAsString(), descriptor);
         }

         File welcomePage = new File(webRoot + File.separator + "index.html");
         project.writeFile("<html><head><title>Welcome to Seam Forge</title></head>" +
                     "<body>" +
                     "<h1> [" + projectName + "] is Online</h1>" +
                           "Powered by <a href=\"http://bit.ly/seamforge\">Seam Forge</a>" +
                     "</body>" +
                     "</html>", welcomePage);
      }
      project.registerFacet(this);
      return this;
   }

   private FilenameFilter getEntityFileFilter(final String extension)
   {
      return new FilenameFilter()
      {
         @Override
         public boolean accept(final File dir, final String name)
         {
            return name.endsWith(extension);
         }
      };
   }

}
