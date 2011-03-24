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
package org.jboss.seam.forge.spec.jsf;

import java.io.File;

import org.jboss.seam.forge.project.facets.BaseFacet;
import org.jboss.seam.forge.project.facets.WebResourceFacet;
import org.jboss.seam.forge.resources.DirectoryResource;
import org.jboss.seam.forge.resources.FileResource;
import org.jboss.seam.forge.shell.plugins.Alias;
import org.jboss.seam.forge.shell.plugins.RequiresFacet;
import org.jboss.seam.forge.spec.servlet.ServletFacet;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Alias("forge.spec.jsf")
@RequiresFacet(ServletFacet.class)
public class FacesFacet extends BaseFacet
{
   public FileResource<?> getConfigFile()
   {
      DirectoryResource webRoot = project.getFacet(WebResourceFacet.class).getWebRootDirectory();
      return (FileResource<?>) webRoot.getChild("WEB-INF" + File.separator + "faces-config.xml");
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
   public boolean install()
   {
      if (!isInstalled())
      {
         // Create faces-config
         if (!getConfigFile().createNewFile())
         {
            throw new RuntimeException("Failed to create required [" + getConfigFile().getFullyQualifiedName() + "]");
         }
         getConfigFile().setContents(getClass()
                  .getResourceAsStream("/org/jboss/seam/forge/web/faces-config.xml"));
      }
      return true;
   }
}
