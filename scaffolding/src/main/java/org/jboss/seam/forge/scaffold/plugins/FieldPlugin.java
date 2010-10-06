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

import java.io.FileNotFoundException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.Column;
import javax.persistence.OneToOne;

import org.jboss.seam.forge.parser.java.Field;
import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.facets.JavaSourceFacet;
import org.jboss.seam.forge.scaffold.ScaffoldingFacet;
import org.jboss.seam.forge.shell.PromptType;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.Command;
import org.jboss.seam.forge.shell.plugins.Help;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.shell.plugins.RequiresFacet;
import org.jboss.seam.forge.shell.plugins.RequiresProject;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@Named("new-field")
@Singleton
@RequiresProject
@RequiresFacet(ScaffoldingFacet.class)
@Help("A plugin to manage simple @Entity and View creation; a basic MVC framework plugin.")
public class FieldPlugin implements Plugin
{
   private final Instance<Project> projectInstance;
   private final Shell shell;
   private final Instance<JavaClass> entityInstance;

   @Inject
   public FieldPlugin(final Instance<Project> project, final Shell shell, final @LastEntity Instance<JavaClass> entity)
   {
      this.projectInstance = project;
      this.shell = shell;
      this.entityInstance = entity;
   }

   @Command(value = "int", help = "Add an int field to an existing @Entity class")
   public void newIntField(
            @Option(name = "fieldName",
                     required = true,
                     description = "The field name",
                     type = PromptType.JAVA_VARIABLE_NAME) final String fieldName,
            @Option(name = "addToClass",
                     required = false,
                     type = PromptType.JAVA_CLASS,
                     description = "The @Entity to which this field will be added") final String entityName)
   {
      try
      {
         JavaClass entity = findEntity(entityName);
         addField(entity, fieldName, int.class, Column.class);
      }
      catch (FileNotFoundException e)
      {
         shell.println("Could not locate the @Entity requested. No update was made.");
      }
   }

   @Command(value = "string", help = "Add a String field to an existing @Entity class")
   public void newLongField(
            @Option(name = "fieldName",
                     required = true,
                     description = "The field name",
                     type = PromptType.JAVA_VARIABLE_NAME) final String fieldName,
            @Option(name = "addToClass",
                     required = false,
                     type = PromptType.JAVA_CLASS,
                     description = "The @Entity to which this field will be added") final String entityName)
   {
      try
      {
         JavaClass entity = findEntity(entityName);
         addField(entity, fieldName, String.class, Column.class);
      }
      catch (FileNotFoundException e)
      {
         shell.println("Could not locate the @Entity requested. No update was made.");
      }
   }

   @Command(value = "reference", help = "Add a One-to-one relationship field to an existing @Entity class")
   public void newOneToOneRelationship(
            @Option(name = "fieldName",
                     required = true,
                     description = "The field name",
                     type = PromptType.JAVA_VARIABLE_NAME) final String fieldName,
            @Option(name = "fieldType",
                     required = true,
                     description = "The @Entity type to which this field is a relationship",
                     type = PromptType.JAVA_CLASS) final String fieldType,
            @Option(name = "addToClass",
                     required = false,
                     description = "The @Entity to which this field will be added",
                     type = PromptType.JAVA_CLASS) final String targetEntity,
            @Option(name = "inverseFieldName",
                     required = false,
                     description = "The field name",
                     type = PromptType.JAVA_VARIABLE_NAME) final String inverseFieldName)
   {

      try
      {
         JavaClass field = findEntity(fieldType);
         JavaClass entity = findEntity(targetEntity);
         addField(entity, fieldName, field, OneToOne.class);
         if ((inverseFieldName != null) && !inverseFieldName.isEmpty())
         {
            addField(field, inverseFieldName, entity, OneToOne.class);
         }
      }
      catch (FileNotFoundException e)
      {
         shell.println("Could not locate the @Entity requested. No update was made.");
      }

   }

   /*
    * Helpers
    */
   private void addField(final JavaClass targetEntity, final String fieldName, final JavaClass fieldType,
            final Class<? extends Annotation> annotation)
   {
      Project project = getCurrentProject();
      JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);

      Field field = targetEntity.addField();
      field.setName(fieldName).setPrivate().setType(fieldType.getName()).addAnnotation(annotation);
      targetEntity.addImport(fieldType.getQualifiedName());
      java.saveJavaClass(targetEntity);
      shell.println("Added field to " + targetEntity.getQualifiedName() + ": " + field);
   }

   private void addField(final JavaClass targetEntity, final String fieldName, final Class<?> fieldType,
            final Class<? extends Annotation> annotation)
   {
      Project project = getCurrentProject();
      JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);

      Field field = targetEntity.addField();
      field.setName(fieldName).setPrivate().setType(fieldType).addAnnotation(annotation);
      java.saveJavaClass(targetEntity);
      shell.println("Added field to " + targetEntity.getQualifiedName() + ": " + field);
   }

   public Project getCurrentProject()
   {
      return projectInstance.get();
   }

   private JavaClass findEntity(final String entity) throws FileNotFoundException
   {
      JavaClass result = null;

      Project project = getCurrentProject();
      ScaffoldingFacet scaffold = project.getFacet(ScaffoldingFacet.class);
      JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);

      if (entity != null)
      {
         result = java.getJavaClass(entity);
         if (result == null)
         {
            result = java.getJavaClass(scaffold.getEntityPackage() + "." + entity);
         }
      }

      if (result == null)
      {
         result = entityInstance.get();
      }

      if (result == null)
      {
         result = promptForEntity();
      }

      if (result == null)
      {
         throw new FileNotFoundException("Could not locate JavaClass on which to operate.");
      }

      return result;
   }

   private JavaClass promptForEntity()
   {
      Project project = getCurrentProject();
      ScaffoldingFacet scaffold = project.getFacet(ScaffoldingFacet.class);
      List<JavaClass> entities = scaffold.getAllEntities();
      List<String> entityNames = new ArrayList<String>();
      for (JavaClass javaClass : entities)
      {
         String fullName = javaClass.getPackage();
         if (!fullName.isEmpty())
         {
            fullName += ".";
         }
         fullName += javaClass.getName();

         entityNames.add(fullName);
      }

      if (!entityNames.isEmpty())
      {
         int index = shell.promptChoice("Which entity would you like to modify?", entityNames);
         return entities.get(index);
      }
      return null;
   }
}
