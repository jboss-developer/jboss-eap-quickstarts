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
package org.jboss.seam.forge.shell.command;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.facets.JavaSourceFacet;
import org.jboss.seam.forge.shell.PromptType;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class PromptTypeConverter
{
   private final Instance<Project> projectInstance;

   @Inject
   public PromptTypeConverter(final Instance<Project> projectInstance)
   {
      this.projectInstance = projectInstance;
   }

   public String convert(final PromptType promptType, String value)
   {
      Project project = projectInstance.get();

      if (value != null)
      {
         if (PromptType.JAVA_CLASS.equals(promptType))
         {
            if (value.startsWith("~.") && (project != null))
            {
               if (project.hasFacet(JavaSourceFacet.class))
               {
                  value = value.replaceFirst("~", project.getFacet(JavaSourceFacet.class).getBasePackage());
               }
            }
         }
         else if (PromptType.JAVA_PACKAGE.equals(promptType))
         {
            if (value.startsWith("~.") && (project != null))
            {
               if (project.hasFacet(JavaSourceFacet.class))
               {
                  value = value.replaceFirst("~", project.getFacet(JavaSourceFacet.class).getBasePackage());
               }
            }
         }
      }
      return value;
   }

}
