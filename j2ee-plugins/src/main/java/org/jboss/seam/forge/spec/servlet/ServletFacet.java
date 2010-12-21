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
package org.jboss.seam.forge.spec.servlet;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import org.jboss.seam.forge.project.Facet;
import org.jboss.seam.forge.project.PackagingType;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.constraints.RequiresFacets;
import org.jboss.seam.forge.project.constraints.RequiresPackagingTypes;
import org.jboss.seam.forge.project.dependencies.Dependency;
import org.jboss.seam.forge.project.dependencies.DependencyBuilder;
import org.jboss.seam.forge.project.facets.DependencyFacet;
import org.jboss.seam.forge.project.facets.MetadataFacet;
import org.jboss.seam.forge.project.facets.WebResourceFacet;
import org.jboss.seam.forge.project.resources.FileResource;
import org.jboss.seam.forge.project.resources.builtin.DirectoryResource;
import org.jboss.shrinkwrap.descriptor.api.DescriptorImporter;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.spec.servlet.web.WebAppDescriptor;
import org.jboss.shrinkwrap.descriptor.impl.spec.servlet.web.WebAppDescriptorImpl;
import org.jboss.shrinkwrap.descriptor.impl.spec.servlet.web.WebAppModel;
import org.jboss.shrinkwrap.descriptor.spi.SchemaDescriptorProvider;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Named("forge.spec.servlet")
@RequiresFacets({ WebResourceFacet.class, DependencyFacet.class })
@RequiresPackagingTypes({ PackagingType.WAR })
public class ServletFacet implements Facet
{

   private static final Dependency dep =
         DependencyBuilder.create("org.jboss.spec:jboss-javaee-6.0:1.0.0.CR1:provided:basic");

   private Project project;

   @SuppressWarnings("unchecked")
   public WebAppModel getConfig()
   {
      DescriptorImporter<WebAppDescriptor> importer = Descriptors.importAs(WebAppDescriptor.class);
      WebAppDescriptor descriptor = importer.from(getConfigFile().getResourceInputStream());
      WebAppModel model = ((SchemaDescriptorProvider<WebAppModel>) descriptor).getSchemaModel();
      return model;
   }

   public void saveConfig(final WebAppModel model)
   {
      WebAppDescriptor descriptor = new WebAppDescriptorImpl(model);
      String output = descriptor.exportAsString();
      getConfigFile().setContents(output);
   }

   private FileResource<?> getConfigFile()
   {
      DirectoryResource webRoot = project.getFacet(WebResourceFacet.class).getWebRootDirectory();
      return (FileResource<?>) webRoot.getChild("WEB-INF" + File.separator + "web.xml");
   }

   /**
    * List all servlet resource files.
    */
   public List<Resource<?>> getResources()
   {
      DirectoryResource webRoot = project.getFacet(WebResourceFacet.class).getWebRootDirectory();
      return listChildrenRecursively(webRoot);
   }

   public List<Resource<?>> getResources(final FileFilter filter)
   {
      DirectoryResource webRoot = project.getFacet(WebResourceFacet.class).getWebRootDirectory();
      return listChildrenRecursively(webRoot);
   }

   private List<Resource<?>> listChildrenRecursively(final DirectoryResource current)
   {
      List<Resource<?>> result = new ArrayList<Resource<?>>();
      List<Resource<?>> list = current.listResources();
      if (list != null)
      {
         for (Resource<?> file : list)
         {
            if (file instanceof DirectoryResource)
            {
               result.addAll(listChildrenRecursively((DirectoryResource) file));
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
      DirectoryResource webRoot = project.getFacet(WebResourceFacet.class).getWebRootDirectory();
      return webRoot.exists() && getConfigFile().exists() && project.getFacet(DependencyFacet.class).hasDependency(dep);
   }

   @Override
   public Facet install()
   {
      if (!isInstalled())
      {
         String projectName = project.getFacet(MetadataFacet.class).getProjectName();

         project.getFacet(DependencyFacet.class).addDependency(dep);

         DirectoryResource webRoot = project.getFacet(WebResourceFacet.class).getWebRootDirectory();
         if (!webRoot.exists())
         {
            webRoot.mkdirs();
         }

         FileResource<?> descriptor = getConfigFile();
         if (!descriptor.exists())
         {
            WebAppDescriptor unit = Descriptors.create(WebAppDescriptor.class)
                  .displayName(projectName)
                  .sessionTimeout(30)
                  .welcomeFile("/index.html");

            descriptor.setContents(unit.exportAsString());
         }

         FileResource<?> welcomePage = (FileResource<?>) webRoot.getChild("index.html");
         welcomePage.setContents("<html><head><title>Welcome to Seam Forge</title></head>" +
               "<body>" +
               "<h1> [" + projectName + "] is Online</h1>" +
               "Powered by <a href=\"http://bit.ly/seamforge\">Seam Forge</a>" +
               "</body>" +
               "</html>");
      }
      project.registerFacet(this);
      return this;
   }

}
