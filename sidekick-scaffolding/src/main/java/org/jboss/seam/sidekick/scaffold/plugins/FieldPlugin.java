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

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.Column;

import org.jboss.seam.sidekick.parser.java.Field;
import org.jboss.seam.sidekick.parser.java.JavaClass;
import org.jboss.seam.sidekick.project.Project;
import org.jboss.seam.sidekick.project.facets.JavaSourceFacet;
import org.jboss.seam.sidekick.scaffold.ScaffoldingFacet;
import org.jboss.seam.sidekick.shell.PromptType;
import org.jboss.seam.sidekick.shell.Shell;
import org.jboss.seam.sidekick.shell.plugins.Command;
import org.jboss.seam.sidekick.shell.plugins.Help;
import org.jboss.seam.sidekick.shell.plugins.Option;
import org.jboss.seam.sidekick.shell.plugins.Plugin;
import org.jboss.seam.sidekick.shell.plugins.RequiresFacet;
import org.jboss.seam.sidekick.shell.plugins.RequiresProject;

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
   private final Project project;
   private final Shell shell;
   private JavaClass entity;

   @Inject
   public FieldPlugin(final Project project, final Shell shell, final @LastEntity JavaClass entity)
   {
      this.project = project;
      this.shell = shell;
      this.entity = entity;
   }

   @Command(value = "int", help = "Add an int field to an existing @Entity class")
   public void newField(
         @Option(required = true,
               description = "The field name",
               type = PromptType.JAVA_VARIABLE_NAME) final String fieldName,
         @Option(value = "entity",
               required = false,
               description = "The @Entity name") final String entityName) throws FileNotFoundException
   {
      JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);

      findEntity(entityName);

      Field field = entity.addField();
      field.setName(fieldName).setPrivate().setType(int.class.getName()).addAnnotation(Column.class);

      java.saveJavaClass(entity);
      shell.println("Added field to " + entity.getQualifiedName() + ": " + field);
   }

   private void findEntity(final String entityName) throws FileNotFoundException
   {
      ScaffoldingFacet scaffold = project.getFacet(ScaffoldingFacet.class);
      JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);

      if (entityName != null)
      {
         entity = java.getJavaClass(scaffold.getEntityPackage() + "." + entityName);
      }
      else if (entity == null)
      {
         entity = promptForEntity();
      }
   }

   private JavaClass promptForEntity()
   {
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
