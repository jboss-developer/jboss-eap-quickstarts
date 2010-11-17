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

import java.io.FileNotFoundException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.jboss.seam.forge.parser.java.Field;
import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.parser.java.util.Refactory;
import org.jboss.seam.forge.parser.java.util.Types;
import org.jboss.seam.forge.persistence.PersistenceFacet;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.constraints.RequiresFacet;
import org.jboss.seam.forge.project.constraints.RequiresProject;
import org.jboss.seam.forge.project.facets.JavaSourceFacet;
import org.jboss.seam.forge.project.resources.builtin.JavaResource;
import org.jboss.seam.forge.shell.PromptType;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.Command;
import org.jboss.seam.forge.shell.plugins.DefaultCommand;
import org.jboss.seam.forge.shell.plugins.Help;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.shell.plugins.ResourceScope;
import org.jboss.seam.forge.shell.plugins.Topic;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@Named("new-field")
@Topic("File & Resources")
@Singleton
@RequiresProject
@RequiresFacet(PersistenceFacet.class)
@ResourceScope(JavaResource.class)
@Help("A plugin to manage simple @Entity and View creation; a basic MVC framework plugin.")
public class NewFieldPlugin implements Plugin
{
   private final Instance<Project> projectInstance;
   private final Shell shell;
   private final Instance<JavaClass> entityInstance;

   @Inject
   public NewFieldPlugin(final Instance<Project> project, final Shell shell,
            final @LastEntity Instance<JavaClass> entity)
   {
      this.projectInstance = project;
      this.shell = shell;
      this.entityInstance = entity;
   }

   @DefaultCommand(help = "Add many custom field to an existing @Entity class")
   public void newExpressionField(
            @Option(required = true, description = "The field descriptor") final String... fields)
   {
      System.out.println(Arrays.asList(fields));
   }

   @Command(value = "custom", help = "Add a custom field to an existing @Entity class")
   public void newCustomField(
            @Option(name = "fieldName",
                     required = true,
                     description = "The field name",
                     type = PromptType.JAVA_VARIABLE_NAME) final String fieldName,
            @Option(name = "type",
                     required = true,
                     type = PromptType.JAVA_CLASS,
                     description = "The qualified Class to be used as this field's type") final String type
            )
   {
      try
      {
         JavaClass entity = getJavaClass();
         addFieldTo(entity, type, fieldName, Column.class);
      }
      catch (FileNotFoundException e)
      {
         shell.println("Could not locate the @Entity requested. No update was made.");
      }
   }

   @Command(value = "boolean", help = "Add a boolean field to an existing @Entity class")
   public void newBooleanField(
            @Option(name = "fieldName",
                     required = true,
                     description = "The field name",
                     type = PromptType.JAVA_VARIABLE_NAME) final String fieldName,
            @Option(name = "primitive",
                     required = false,
                     defaultValue = "true",
                     description = "Marks this field to be created as a primitive.",
                     type = PromptType.JAVA_VARIABLE_NAME) final boolean primitive)
   {
      try
      {
         JavaClass entity = getJavaClass();
         if (primitive)
         {
            addFieldTo(entity, boolean.class, fieldName, Column.class);
         }
         else
         {
            addFieldTo(entity, Boolean.class, fieldName, Column.class);
         }
      }
      catch (FileNotFoundException e)
      {
         shell.println("Could not locate the @Entity requested. No update was made.");
      }
   }

   @Command(value = "int", help = "Add an int field to an existing @Entity class")
   public void newIntField(
            @Option(name = "fieldName",
                     required = true,
                     description = "The field name",
                     type = PromptType.JAVA_VARIABLE_NAME) final String fieldName,

            @Option(name = "primitive",
                     required = false,
                     defaultValue = "true",
                     description = "Marks this field to be created as a primitive.",
                     type = PromptType.JAVA_VARIABLE_NAME) final boolean primitive)
   {
      try
      {
         JavaClass entity = getJavaClass();
         if (primitive)
         {
            addFieldTo(entity, int.class, fieldName, Column.class);
         }
         else
         {
            addFieldTo(entity, Integer.class, fieldName, Column.class);
         }
      }
      catch (FileNotFoundException e)
      {
         shell.println("Could not locate the @Entity requested. No update was made.");
      }
   }

   @Command(value = "long", help = "Add a long field to an existing @Entity class")
   public void newLongField(
            @Option(name = "fieldName",
                     required = true,
                     description = "The field name",
                     type = PromptType.JAVA_VARIABLE_NAME) final String fieldName,
            @Option(name = "primitive",
                     required = false,
                     defaultValue = "true",
                     description = "Marks this field to be created as a primitive.",
                     type = PromptType.JAVA_VARIABLE_NAME) final boolean primitive)
   {
      try
      {
         JavaClass entity = getJavaClass();
         if (primitive)
         {
            addFieldTo(entity, long.class, fieldName, Column.class);
         }
         else
         {
            addFieldTo(entity, Long.class, fieldName, Column.class);
         }
      }
      catch (FileNotFoundException e)
      {
         shell.println("Could not locate the @Entity requested. No update was made.");
      }
   }

   @Command(value = "number", help = "Add a number field to an existing @Entity class")
   public void newNumberField(
            @Option(name = "fieldName",
                     required = true,
                     description = "The field name",
                     type = PromptType.JAVA_VARIABLE_NAME) final String fieldName,
            @Option(name = "type",
                     required = true,
                     type = PromptType.JAVA_CLASS,
                     description = "The qualified Class to be used as this field's type") final String type)
   {
      try
      {
         JavaClass entity = getJavaClass();
         addFieldTo(entity, Class.forName(type), fieldName, Column.class);
      }
      catch (FileNotFoundException e)
      {
         shell.println("Could not locate the @Entity requested. No update was made.");
      }
      catch (ClassNotFoundException e)
      {
         shell.println("Sorry, I don't think [" + type
                  + "] is a valid Java number type. Try something in the 'java.lang.* or java.math*' packages.");
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
         JavaClass entity = getJavaClass();
         addFieldTo(entity, String.class, fieldName, Column.class);
      }
      catch (FileNotFoundException e)
      {
         shell.println("Could not locate the @Entity requested. No update was made.");
      }
   }

   @Command(value = "oneToOne", help = "Add a One-to-one relationship field to an existing @Entity class")
   public void newOneToOneRelationship(
            @Option(name = "fieldName",
                     required = true,
                     description = "The field name",
                     type = PromptType.JAVA_VARIABLE_NAME) final String fieldName,
            @Option(name = "fieldType",
                     required = true,
                     description = "The @Entity type to which this field is a relationship",
                     type = PromptType.JAVA_CLASS) final String fieldType,
            @Option(name = "inverseFieldName",
                     required = false,
                     description = "Create a bi-directional relationship, using this value as the name of the inverse field.",
                     type = PromptType.JAVA_VARIABLE_NAME) final String inverseFieldName)
   {

      try
      {
         JavaClass field = findEntity(fieldType);
         JavaClass entity = getJavaClass();
         addFieldTo(entity, field, fieldName, OneToOne.class);
         if ((inverseFieldName != null) && !inverseFieldName.isEmpty())
         {
            addFieldTo(field, entity, inverseFieldName, OneToOne.class);
         }
      }
      catch (FileNotFoundException e)
      {
         shell.println("Could not locate the @Entity requested. No update was made.");
      }
   }

   @Command(value = "manyToMany", help = "Add a many-to-many relationship field (java.lang.Set<?>) to an existing @Entity class")
   public void newManyToManyRelationship(
            @Option(name = "fieldName",
                     required = true,
                     description = "The field name",
                     type = PromptType.JAVA_VARIABLE_NAME) final String fieldName,
            @Option(name = "fieldType",
                     required = true,
                     description = "The @Entity type to which this field is a relationship",
                     type = PromptType.JAVA_CLASS) final String fieldType,
            @Option(name = "inverseFieldName",
                     required = false,
                     description = "Create an bi-directional relationship, using this value as the name of the inverse field.",
                     type = PromptType.JAVA_VARIABLE_NAME) final String inverseFieldName)
   {

      Project project = getCurrentProject();
      JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);

      try
      {
         JavaClass entity = getJavaClass();
         JavaClass otherEntity = findEntity(fieldType);

         entity.addImport(Set.class);
         entity.addImport(HashSet.class);
         entity.addImport(otherEntity.getQualifiedName());
         Field field = entity.addField("private Set<" + otherEntity.getName() + "> " + fieldName + "= new HashSet<"
                  + otherEntity.getName() + ">();");
         org.jboss.seam.forge.parser.java.Annotation annotation = field.addAnnotation(ManyToMany.class);
         Refactory.createGetterAndSetter(entity, field);

         if ((inverseFieldName != null) && !inverseFieldName.isEmpty())
         {
            annotation.setStringValue("mappedBy", inverseFieldName);

            otherEntity.addImport(Set.class);
            otherEntity.addImport(HashSet.class);
            otherEntity.addImport(entity.getQualifiedName());
            Field otherField = otherEntity.addField("private Set<" + entity.getName() + "> " + inverseFieldName
                              + "= new HashSet<" + entity.getName() + ">();");
            otherField.addAnnotation(ManyToMany.class);
            Refactory.createGetterAndSetter(otherEntity, otherField);

            java.saveJavaClass(otherEntity);
         }
         java.saveJavaClass(entity);
      }
      catch (FileNotFoundException e)
      {
         shell.println("Could not locate the @Entity requested. No update was made.");
      }

   }

   @Command(value = "oneToMany", help = "Add a one-to-many relationship field (java.lang.Set<?>) to an existing @Entity class")
   public void newOneToManyRelationship(
            @Option(name = "fieldName",
                     required = true,
                     description = "The field name",
                     type = PromptType.JAVA_VARIABLE_NAME) final String fieldName,
            @Option(name = "fieldType",
                     required = true,
                     description = "The @Entity representing the 'many' side of the relationship.",
                     type = PromptType.JAVA_CLASS) final String fieldType,
            @Option(name = "inverseFieldName",
                     required = false,
                     description = "Create an bi-directional relationship, using this value as the name of the inverse field.",
                     type = PromptType.JAVA_VARIABLE_NAME) final String inverseFieldName)
   {

      Project project = getCurrentProject();
      JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);

      try
      {
         JavaClass entity = getJavaClass();
         JavaClass otherEntity = findEntity(fieldType);

         entity.addImport(Set.class);
         entity.addImport(HashSet.class);
         entity.addImport(otherEntity.getQualifiedName());
         Field field = entity.addField("private Set<" + otherEntity.getName() + "> " + fieldName + "= new HashSet<"
                  + otherEntity.getName() + ">();");
         org.jboss.seam.forge.parser.java.Annotation annotation = field.addAnnotation(OneToMany.class);
         Refactory.createGetterAndSetter(entity, field);

         if ((inverseFieldName != null) && !inverseFieldName.isEmpty())
         {
            annotation.setStringValue("mappedBy", inverseFieldName);

            otherEntity.addImport(Set.class);
            otherEntity.addImport(HashSet.class);
            otherEntity.addImport(entity.getQualifiedName());
            otherEntity.addField("private Set<" + entity.getName() + "> " + inverseFieldName
                              + "= new HashSet<" + entity.getName() + ">();")
                     .addAnnotation(ManyToOne.class).setStringValue("mappedBy", fieldName);

            java.saveJavaClass(otherEntity);
         }
         java.saveJavaClass(entity);
      }
      catch (FileNotFoundException e)
      {
         shell.println("Could not locate the @Entity requested. No update was made.");
      }
   }

   /*
    * Helpers
    */
   private void addFieldTo(final JavaClass targetEntity, final JavaClass fieldEntity, final String fieldName,
            final Class<? extends Annotation> annotation)
   {
      Project project = getCurrentProject();
      JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);

      Field field = targetEntity.addField();
      field.setName(fieldName).setPrivate().setType(fieldEntity.getName()).addAnnotation(annotation);
      targetEntity.addImport(fieldEntity.getQualifiedName());
      Refactory.createGetterAndSetter(targetEntity, field);
      java.saveJavaClass(targetEntity);
      shell.println("Added field to " + targetEntity.getQualifiedName() + ": " + field);
   }

   private void addFieldTo(final JavaClass targetEntity, final String fieldType, final String fieldName,
            final Class<Column> annotation)
   {
      Project project = getCurrentProject();
      JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);

      Field field = targetEntity.addField();
      field.setName(fieldName).setPrivate().setType(Types.toSimpleName(fieldType)).addAnnotation(annotation);
      targetEntity.addImport(fieldType);
      Refactory.createGetterAndSetter(targetEntity, field);
      java.saveJavaClass(targetEntity);
      shell.println("Added field to " + targetEntity.getQualifiedName() + ": " + field);
   }

   private void addFieldTo(final JavaClass targetEntity, final Class<?> fieldType, final String fieldName,
            final Class<? extends Annotation> annotation)
   {
      Project project = getCurrentProject();
      JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);

      Field field = targetEntity.addField();
      field.setName(fieldName).setPrivate().setType(fieldType).addAnnotation(annotation);
      if (!fieldType.getName().startsWith("java.lang.") && !fieldType.isPrimitive())
      {
         targetEntity.addImport(fieldType);
      }
      Refactory.createGetterAndSetter(targetEntity, field);
      java.saveJavaClass(targetEntity);
      shell.println("Added field to " + targetEntity.getQualifiedName() + ": " + field);
   }

   public Project getCurrentProject()
   {
      return projectInstance.get();
   }

   private JavaClass getJavaClass() throws FileNotFoundException
   {
      if (shell.getCurrentResource() instanceof JavaResource)
      {
         return ((JavaResource) shell.getCurrentResource()).getJavaClass();
      }
      else
      {
         throw new RuntimeException("current resource is not a JavaResource!");
      }

   }

   private JavaClass findEntity(final String entity) throws FileNotFoundException
   {
      JavaClass result = null;

      Project project = getCurrentProject();
      PersistenceFacet scaffold = project.getFacet(PersistenceFacet.class);
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
      PersistenceFacet scaffold = project.getFacet(PersistenceFacet.class);
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
