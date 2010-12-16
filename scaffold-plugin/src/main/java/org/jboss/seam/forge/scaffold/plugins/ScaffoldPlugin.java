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
import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Entity;

import org.jboss.seam.forge.parser.JavaParser;
import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.parser.java.JavaSource;
import org.jboss.seam.forge.persistence.PersistenceFacet;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.constraints.RequiresFacets;
import org.jboss.seam.forge.project.constraints.RequiresProject;
import org.jboss.seam.forge.project.dependencies.Dependency;
import org.jboss.seam.forge.project.dependencies.DependencyBuilder;
import org.jboss.seam.forge.project.facets.DependencyFacet;
import org.jboss.seam.forge.project.facets.JavaSourceFacet;
import org.jboss.seam.forge.project.facets.WebResourceFacet;
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
import org.jboss.seam.render.TemplateCompiler;
import org.jboss.seam.render.template.CompiledTemplateResource;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@Named("scaffold")
@Topic("UI Generation & Scaffolding")
@Help("Metawidget UI scaffolding")
@RequiresFacets({ MavenWebResourceFacet.class, PersistenceFacet.class, JavaSourceFacet.class, DependencyFacet.class })
@RequiresProject
public class ScaffoldPlugin implements Plugin
{
   private static final String VIEW_BEAN_TEMPLATE = "org/jboss/seam/forge/scaffold/templates/ViewBean.java";
   private static final String VIEW_TEMPLATE = "org/jboss/seam/forge/scaffold/templates/view.xhtml";

   @Inject
   private Shell shell;

   @Inject
   private TemplateCompiler compiler;
   private final Dependency metawidget = DependencyBuilder.create("org.metawidget:metawidget:1.0.5");

   @Command("generate")
   public void generateUI(
         @Option(required = false) Resource<?>[] targets, PipeOut out) throws FileNotFoundException
   {
      Project project = shell.getCurrentProject();
      JavaSourceFacet jsf = project.getFacet(JavaSourceFacet.class);
      WebResourceFacet wrf = project.getFacet(WebResourceFacet.class);
      DependencyFacet df = project.getFacet(DependencyFacet.class);

      if (!df.hasDependency(metawidget))
      {
         df.addDependency(metawidget);
      }

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
            JavaSource<?> entity = ((JavaResource) r).getJavaSource();
            if (entity instanceof JavaClass)
            {
               if (entity.hasAnnotation(Entity.class))
               {
                  CompiledTemplateResource viewBeanTemplate = compiler.compile(VIEW_BEAN_TEMPLATE);
                  HashMap<Object, Object> context = new HashMap<Object, Object>();
                  context.put("entity", entity);

                  JavaClass viewBean = JavaParser.parse(JavaClass.class, viewBeanTemplate.render(context));
                  viewBean.setPackage(jsf.getBasePackage() + ".view");

                  CompiledTemplateResource viewTemplate = compiler.compile(VIEW_TEMPLATE);
                  context = new HashMap<Object, Object>();

                  String name = viewBean.getName();
                  name = name.substring(0, 1).toLowerCase() + name.substring(1);
                  context.put("beanName", name);

                  wrf.createWebResource(viewTemplate.render(context), entity.getName().toLowerCase() + ".xhtml");

                  jsf.saveJavaClass(viewBean);

                  out.println("Generating UI for [" + entity.getQualifiedName() + "]");
               }
               else
               {
                  displaySkippingResourceMsg(out, entity);
               }
            }
            else
            {
               displaySkippingResourceMsg(out, entity);
            }
         }
      }
   }

   private void displaySkippingResourceMsg(PipeOut out, JavaSource<?> entity)
   {
      if (!out.isPiped())
      {
         out.println(out.renderColor(ShellColor.RED, "Notice:") + " skipped non-@Entity class [" + entity.getQualifiedName() + "]");
      }
   }
}
