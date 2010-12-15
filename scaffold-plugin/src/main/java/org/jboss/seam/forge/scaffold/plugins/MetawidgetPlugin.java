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

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Entity;

import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.parser.java.JavaSource;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.constraints.RequiresFacets;
import org.jboss.seam.forge.project.constraints.RequiresProject;
import org.jboss.seam.forge.project.facets.JavaSourceFacet;
import org.jboss.seam.forge.project.facets.builtin.MavenWebResourceFacet;
import org.jboss.seam.forge.project.resources.builtin.JavaResource;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.Command;
import org.jboss.seam.forge.shell.plugins.Help;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.PipeOut;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.shell.plugins.Topic;
import org.jboss.seam.forge.shell.util.ShellColor;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@Named("mw")
@Topic("File & Resources")
@Help("Metawidget UI scaffolding")
@RequiresFacets({ MavenWebResourceFacet.class, JavaSourceFacet.class })
@RequiresProject
public class MetawidgetPlugin implements Plugin
{
   @Inject
   private Shell shell;

   @Command("generate")
   public void generateUI(
         @Option(required = false) Resource<?>[] targets, PipeOut out) throws FileNotFoundException
   {
      Resource<?> currentResource = shell.getCurrentResource();
      if ((targets == null) && (currentResource instanceof JavaResource))
      {
         targets = new JavaResource[] { (JavaResource) currentResource };
      }
      else if (targets == null)
      {
         if (!out.isPiped())
         {
            out.println("Error: Must specify a domain entity on which to operate.");
         }
         return;
      }

      for (Resource<?> r : targets)
      {
         if (r instanceof JavaResource)
         {
            JavaSource<?> source = ((JavaResource) r).getJavaSource();
            if (source instanceof JavaClass)
            {
               if (source.hasAnnotation(Entity.class))
               {
                  out.println("Generating UI for [" + source.getQualifiedName() + "]");
               }
            }
            else
            {
               if (!out.isPiped())
               {
                  out.println(out.renderColor(ShellColor.RED, "Notice:") + " skipped non-@Entity class [" + source.getQualifiedName() + "]");
               }
            }
         }
      }
   }

}
