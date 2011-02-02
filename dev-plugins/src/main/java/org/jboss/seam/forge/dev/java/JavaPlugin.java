/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc., and individual contributors
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
package org.jboss.seam.forge.dev.java;

import java.util.Map.Entry;

import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.constraints.RequiresFacet;
import org.jboss.seam.forge.project.facets.JavaSourceFacet;
import org.jboss.seam.forge.shell.PromptType;
import org.jboss.seam.forge.shell.plugins.Command;
import org.jboss.seam.forge.shell.plugins.DefaultCommand;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.PipeOut;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.shell.util.ShellColor;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@Named("java")
@RequiresFacet(JavaSourceFacet.class)
public class JavaPlugin implements Plugin
{
   @Inject
   private Project project;

   @DefaultCommand(help = "Prints all Java system property information.")
   public void info(final PipeOut out)
   {
      for (Entry<Object, Object> entry : System.getProperties().entrySet())
      {
         if (entry.getKey().toString().startsWith("java"))
         {
            out.print(ShellColor.BOLD, entry.getKey().toString() + ": ");
            out.println(entry.getValue().toString());
         }
      }
   }

   @Command("new-class")
   public void newClass(
            @Option(required = true,
                     type = PromptType.JAVA_CLASS,
                     description = "class name...") final String entityName)
   {
      JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);

   }
}
