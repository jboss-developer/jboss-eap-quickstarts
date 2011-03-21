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
import java.util.Arrays;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.apache.maven.model.Model;
import org.jboss.seam.forge.project.Facet;
import org.jboss.seam.forge.project.facets.BaseFacet;
import org.jboss.seam.forge.project.facets.FacetNotFoundException;
import org.jboss.seam.forge.project.facets.MavenCoreFacet;
import org.jboss.seam.forge.project.facets.PackagingFacet;
import org.jboss.seam.forge.project.packaging.PackagingType;
import org.jboss.seam.forge.project.services.ResourceFactory;
import org.jboss.seam.forge.resources.Resource;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.events.PackagingChanged;
import org.jboss.seam.forge.shell.plugins.Alias;
import org.jboss.seam.forge.shell.plugins.RequiresFacet;
import org.jboss.shrinkwrap.descriptor.impl.base.Strings;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Dependent
@Alias("forge.maven.PackagingFacet")
@RequiresFacet(MavenCoreFacet.class)
public class MavenPackagingFacet extends BaseFacet implements PackagingFacet, Facet
{
   @Inject
   private Event<PackagingChanged> event;

   @Inject
   ResourceFactory factory;

   @Inject
   Shell shell;

   @Override
   public void setPackagingType(final PackagingType type)
   {
      PackagingType oldType = getPackagingType();

      if (!oldType.equals(type))
      {
         MavenCoreFacet mavenFacet = project.getFacet(MavenCoreFacet.class);
         Model pom = mavenFacet.getPOM();
         pom.setPackaging(type.getType());
         mavenFacet.setPOM(pom);

         event.fire(new PackagingChanged(project, oldType, type));
      }
   }

   @Override
   public PackagingType getPackagingType()
   {
      MavenCoreFacet mavenFacet = project.getFacet(MavenCoreFacet.class);
      Model pom = mavenFacet.getPOM();
      return PackagingType.from(pom.getPackaging());
   }

   @Override
   public boolean isInstalled()
   {
      try
      {
         project.getFacet(MavenCoreFacet.class);
         return true;
      }
      catch (FacetNotFoundException e)
      {
         return false;
      }
   }

   @Override
   public boolean install()
   {
      if (PackagingType.NONE.equals(getPackagingType()))
      {
         setPackagingType(PackagingType.BASIC);
      }
      return true;
   }

   @Override
   public Resource<?> getFinalArtifact()
   {
      MavenCoreFacet mvn = project.getFacet(MavenCoreFacet.class);
      String directory = mvn.getProjectBuildingResult().getProject().getBuild().getDirectory();
      String finalName = mvn.getProjectBuildingResult().getProject().getBuild().getFinalName();

      if (Strings.isNullOrEmpty(directory))
      {
         throw new IllegalStateException("Project build directory is not configured");
      }
      if (Strings.isNullOrEmpty(finalName))
      {
         throw new IllegalStateException("Project final artifact name is not configured");
      }
      return factory.getResourceFrom(new File(directory.trim() + "/" + finalName + "."
               + getPackagingType().name().toLowerCase()));
   }

   @Override
   public void executeBuild(String... args)
   {
      // FIXME this references an upstream shell function from dev-plugins. should build via java
      if (args == null)
      {
         args = new String[] {};
      }
      shell.execute("mvn clean package " + Strings.join(Arrays.asList(args), " "));
   }

}
