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
package org.jboss.seam.forge.spec.cdi;

import java.io.File;

import javax.inject.Named;

import org.jboss.seam.forge.project.Facet;
import org.jboss.seam.forge.project.constraints.RequiresFacets;
import org.jboss.seam.forge.project.facets.BaseFacet;
import org.jboss.seam.forge.project.facets.WebResourceFacet;
import org.jboss.seam.forge.project.resources.FileResource;
import org.jboss.seam.forge.project.resources.builtin.DirectoryResource;
import org.jboss.shrinkwrap.descriptor.api.DescriptorImporter;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.spec.cdi.beans.BeansDescriptor;
import org.jboss.shrinkwrap.descriptor.impl.spec.cdi.beans.BeansDescriptorImpl;
import org.jboss.shrinkwrap.descriptor.impl.spec.cdi.beans.BeansModel;
import org.jboss.shrinkwrap.descriptor.spi.SchemaDescriptorProvider;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Named("forge.spec.cdi")
@RequiresFacets({ WebResourceFacet.class })
public class CDIFacet extends BaseFacet
{
   private FileResource<?> getConfigFile()
   {
      // TODO this needs to observe (PackagingChangedEvent) and be able to switch between JAR/WAR
      DirectoryResource webRoot = project.getFacet(WebResourceFacet.class).getWebRootDirectory();
      return (FileResource<?>) webRoot.getChild("WEB-INF" + File.separator + "beans.xml");
   }

   @SuppressWarnings("unchecked")
   public BeansModel getConfig()
   {
      DescriptorImporter<BeansDescriptor> importer = Descriptors.importAs(BeansDescriptor.class);
      BeansDescriptor descriptor = importer.from(getConfigFile().getResourceInputStream());
      BeansModel model = ((SchemaDescriptorProvider<BeansModel>) descriptor).getSchemaModel();
      return model;
   }

   public void saveConfig(final BeansModel model)
   {
      BeansDescriptor descriptor = new BeansDescriptorImpl(model);
      String output = descriptor.exportAsString();
      getConfigFile().setContents(output);
   }

   /*
    * Facet Methods
    */
   @Override
   public boolean isInstalled()
   {
      return getConfigFile().exists();
   }

   @Override
   public Facet install()
   {
      if (!isInstalled())
      {
         FileResource<?> descriptor = getConfigFile();
         if (!descriptor.createNewFile())
         {
            throw new RuntimeException("Failed to create required [" + getConfigFile().getFullyQualifiedName() + "]");
         }

         descriptor.setContents(getClass()
                     .getResourceAsStream("/org/jboss/seam/forge/web/beans.xml"));

      }
      project.registerFacet(this);
      return this;
   }
}
