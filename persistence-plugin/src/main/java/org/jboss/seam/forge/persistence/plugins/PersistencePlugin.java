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
package org.jboss.seam.forge.persistence.plugins;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.maven.model.Dependency;
import org.jboss.seam.forge.persistence.PersistenceFacet;
import org.jboss.seam.forge.project.PackagingType;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.services.FacetFactory;
import org.jboss.seam.forge.project.util.DependencyBuilder;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.Help;
import org.jboss.seam.forge.shell.plugins.MavenPlugin;
import org.jboss.seam.forge.shell.plugins.RequiresFacet;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@Singleton
@Named("persistence")
@RequiresFacet(PersistenceFacet.class)
@Help("A plugin to manage simple @Entity and database operations.")
public class PersistencePlugin extends MavenPlugin
{
   private final FacetFactory factory;
   private final Shell shell;

   @Inject
   public PersistencePlugin(final FacetFactory factory, final Shell shell)
   {
      this.factory = factory;
      this.shell = shell;
   }

   @Override
   public List<PackagingType> getCompatiblePackagingTypes()
   {
      return Arrays.asList(PackagingType.JAR);
   }

   @Override
   public List<Dependency> getDependencies()
   {
      return Arrays.asList(DependencyBuilder.create()
               .setGroupId("org.hibernate")
               .setArtifactId("hibernate-entitymanager")
               .setVersion("3.4.0.GA").build());
   }

   @Override
   public void install(final Project project)
   {
      PersistenceFacet facet = factory.getFacet(PersistenceFacet.class);
      project.installFacet(facet);
      super.install(project);
   }
}
