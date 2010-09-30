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
package org.jboss.seam.forge.scaffold.plugins;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.Entity;

import org.jboss.seam.forge.parser.JavaParser;
import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.facets.JavaSourceFacet;
import org.jboss.seam.forge.scaffold.ScaffoldingFacet;
import org.jboss.seam.forge.shell.PromptType;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.DefaultCommand;
import org.jboss.seam.forge.shell.plugins.Help;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.shell.plugins.RequiresFacet;
import org.jboss.seam.forge.shell.plugins.RequiresProject;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@Singleton
@Named("new-entity")
@RequiresProject
@RequiresFacet(ScaffoldingFacet.class)
@Help("A plugin to manage simple @Entity and View creation; a basic MVC framework plugin.")
public class EntityPlugin implements Plugin
{
   private final Project project;
   private final Shell shell;
   private JavaClass lastEntity;

   @Inject
   public EntityPlugin(final Project project, final Shell shell)
   {
      this.project = project;
      this.shell = shell;
   }

   @Produces
   @Dependent
   @LastEntity
   JavaClass getLastEntity()
   {
      return lastEntity;
   }

   @DefaultCommand(help = "Create a JPA @Entity")
   public void newEntity(
         @Option(required = true,
               description = "The @Entity name") final String entityName,
         @Option(required = false,
               description = "Trigger scaffolding generation for this @Entity",
               defaultValue = "false") final boolean buildScaffold)
   {
      ScaffoldingFacet scaffold = project.getFacet(ScaffoldingFacet.class);
      JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);

      String entityPackage = shell.promptCommon(
               "In which package you'd like to create this @Entity, or enter for default:",
               PromptType.JAVA_PACKAGE, scaffold.getEntityPackage());

      JavaClass javaClass = JavaParser.createClass()
               .setPackage(entityPackage)
               .setName(entityName)
               .setPublic()
               .addAnnotation(Entity.class)
               .getOrigin();
      java.saveJavaClass(javaClass);
      this.lastEntity = javaClass;

      shell.println("Created @Entity [" + entityName + "]");
   }
}
