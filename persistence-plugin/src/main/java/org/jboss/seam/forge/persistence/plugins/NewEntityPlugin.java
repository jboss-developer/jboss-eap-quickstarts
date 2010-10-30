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

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

import org.jboss.seam.forge.parser.JavaParser;
import org.jboss.seam.forge.parser.java.Field;
import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.parser.java.util.Refactory;
import org.jboss.seam.forge.persistence.PersistenceFacet;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.constraints.RequiresFacet;
import org.jboss.seam.forge.project.constraints.RequiresProject;
import org.jboss.seam.forge.project.facets.JavaSourceFacet;
import org.jboss.seam.forge.project.resources.builtin.DirectoryResource;
import org.jboss.seam.forge.shell.PromptType;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.*;

import java.io.File;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@Singleton
@Named("new-entity")
@RequiresProject
@RequiresFacet(PersistenceFacet.class)
@ResourceScope(DirectoryResource.class)
@Help("A plugin to manage simple @Entity and View creation; a basic MVC framework plugin.")
public class NewEntityPlugin implements Plugin
{
   private final Instance<Project> projectInstance;

   private final Shell shell;
   private JavaClass lastEntity;
   private Project lastProject;

   @Inject
   public NewEntityPlugin(final Instance<Project> projectInstance, final Shell shell)
   {
      this.projectInstance = projectInstance;
      this.shell = shell;
   }

   @Produces
   @Dependent
   @LastEntity
   JavaClass getLastEntity()
   {
      // TODO this needs to be replaced once Mike's contextuals are working.
      if (projectInstance.get() != this.lastProject)
      {
         lastEntity = null;
      }
      return lastEntity;
   }

   @DefaultCommand(help = "Create a JPA @Entity")
   public void newEntity(
            @Option(required = true,
                     name = "named",
                     description = "The @Entity name") final String entityName)
   {
      // TODO this should accept a qualified name as a parameter instead of
      // prompting for the package later
      Project project = projectInstance.get();
      PersistenceFacet scaffold = project.getFacet(PersistenceFacet.class);
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

      Field id = javaClass.addField("private long id = 0;");
      id.addAnnotation(Id.class);
      id.addAnnotation(GeneratedValue.class)
            .setEnumValue("strategy", GenerationType.AUTO);
      id.addAnnotation(Column.class)
            .setStringValue("name", "id")
            .setLiteralValue("updatable", "false")
            .setLiteralValue("nullable", "false");

      Field version = javaClass.addField("private int version = 0;");
      version.addAnnotation(Version.class);
      version.addAnnotation(Column.class).setStringValue("name", "version");

      Refactory.createGetterAndSetter(javaClass, id);
      Refactory.createGetterAndSetter(javaClass, version);

      File javaFileLocation = java.saveJavaClass(javaClass);

      this.lastEntity = javaClass;
      this.lastProject = project;

      shell.println("Created @Entity [" + javaClass.getQualifiedName() + "]");

      /**
       * Pick up the generated resource.
       */
      shell.execute("pick-up " + javaFileLocation.getAbsolutePath());
   }
}
