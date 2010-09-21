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
package org.jboss.seam.sidekick.scaffold.plugins;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Entity;

import org.apache.maven.model.Dependency;
import org.jboss.seam.sidekick.parser.JavaParser;
import org.jboss.seam.sidekick.project.PackagingType;
import org.jboss.seam.sidekick.project.Project;
import org.jboss.seam.sidekick.project.facets.JavaSourceFacet;
import org.jboss.seam.sidekick.project.services.FacetFactory;
import org.jboss.seam.sidekick.project.util.DependencyBuilder;
import org.jboss.seam.sidekick.scaffold.ScaffoldingFacet;
import org.jboss.seam.sidekick.shell.PromptType;
import org.jboss.seam.sidekick.shell.Shell;
import org.jboss.seam.sidekick.shell.plugins.Command;
import org.jboss.seam.sidekick.shell.plugins.Help;
import org.jboss.seam.sidekick.shell.plugins.MavenPlugin;
import org.jboss.seam.sidekick.shell.plugins.Option;
import org.jboss.seam.sidekick.shell.plugins.RequiresFacet;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@Named("scaffold")
@RequiresFacet(ScaffoldingFacet.class)
@Help("A plugin to manage simple @Entity and View creation; a basic MVC framework plugin.")
public class ScaffoldPlugin extends MavenPlugin
{
   private final Project project;
   private final Shell shell;
   private final FacetFactory factory;

   @Inject
   public ScaffoldPlugin(final Project project, final Shell shell, final FacetFactory factory)
   {
      this.project = project;
      this.shell = shell;
      this.factory = factory;
   }

   @Command(value = "new-entity", help = "Create a new scaffold @Entity, and trigger view-generation")
   public void newEntity(@Option(required = true, description = "The @Entity name") final String entityName)
   {
      ScaffoldingFacet scaffold = project.getFacet(ScaffoldingFacet.class);
      JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);

      String entityPackage = shell.promptCommon(
               "In which package you'd like to create this @Entity, or enter for default:",
               PromptType.JAVA_PACKAGE, scaffold.getEntityPackage());

      java.createJavaClass(JavaParser.createClass()
               .setPackage(entityPackage)
               .setName(entityName)
               .setPublic()
               .addAnnotation(Entity.class)
               .getOrigin());

      shell.println("Created @Entity [" + entityName + "]");
   }

   @Command(value = "new-field", help = "Add a field to an existing @Entity class")
   public void newField(@Option(required = true, description = "The @Entity name") final String entityName)
   {
      ScaffoldingFacet scaffold = project.getFacet(ScaffoldingFacet.class);
      JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);
   }

   @Override
   public List<PackagingType> getCompatiblePackagingTypes()
   {
      return Arrays.asList(PackagingType.WAR);
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
      ScaffoldingFacet facet = factory.getFacet(ScaffoldingFacet.class);
      project.installFacet(facet);
      super.install(project);
   }
}
