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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Entity;

import org.jboss.seam.forge.parser.JavaParser;
import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.parser.java.JavaSource;
import org.jboss.seam.forge.parser.java.util.Refactory;
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
import org.jboss.seam.forge.web.CDIFacet;
import org.jboss.seam.forge.web.FacesFacet;
import org.jboss.seam.render.TemplateCompiler;
import org.jboss.seam.render.template.CompiledTemplateResource;
import org.jboss.shrinkwrap.descriptor.impl.spec.cdi.beans.BeansModel;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@Named("scaffold")
@Topic("UI Generation & Scaffolding")
@Help("Metawidget UI scaffolding")
@RequiresFacets({ MavenWebResourceFacet.class,
      PersistenceFacet.class,
      CDIFacet.class,
      FacesFacet.class })
@RequiresProject
public class ScaffoldPlugin implements Plugin
{
   private static final String BACKING_BEAN_TEMPLATE = "org/jboss/seam/forge/scaffold/templates/BackingBean.jtpl";
   private static final String VIEW_TEMPLATE = "org/jboss/seam/forge/scaffold/templates/view.xhtml";
   private static final String CREATE_TEMPLATE = "org/jboss/seam/forge/scaffold/templates/create.xhtml";
   private static final String LIST_TEMPLATE = "org/jboss/seam/forge/scaffold/templates/list.xhtml";

   @Inject
   private Shell shell;

   @Inject
   private TemplateCompiler compiler;
   private final Dependency metawidget = DependencyBuilder.create("org.metawidget:metawidget:1.0.5");
   private final Dependency seamPersist = DependencyBuilder.create("org.jboss.seam.persistence:seam-persistence-impl:3.0.0.Beta1");
   private final Dependency weldX = DependencyBuilder.create("org.jboss.weld:weld-extensions:1.0.0.Beta1");

   private CompiledTemplateResource viewTemplate;
   private CompiledTemplateResource createTemplate;
   private CompiledTemplateResource listTemplate;

   @PostConstruct
   public void init()
   {
      viewTemplate = compiler.compile(VIEW_TEMPLATE);
      createTemplate = compiler.compile(CREATE_TEMPLATE);
      listTemplate = compiler.compile(LIST_TEMPLATE);

      // TODO remove this once seamPersist no longer pulls in bad WeldX dep
      seamPersist.getExcludedDependencies().add(weldX);
      weldX.getExcludedDependencies().add(DependencyBuilder.create("javax.el:el-api"));
      weldX.getExcludedDependencies().add(DependencyBuilder.create("javax.transaction:jta"));
   }

   @Command("generate-metawidget-jsf")
   public void generateUI(
         @Option(required = false) Resource<?>[] targets, PipeOut out) throws FileNotFoundException
   {
      Resource<?> currentResource = shell.getCurrentResource();
      if (((targets == null) || (targets.length < 1))
            && (currentResource instanceof JavaResource))
      {
         targets = new JavaResource[] { (JavaResource) currentResource };
      }

      List<JavaResource> javaTargets = selectTargets(out, targets);
      if (javaTargets.isEmpty())
      {
         if (!out.isPiped())
         {
            out.println("***ERROR*** Must specify a domain entity on which to operate.");
         }
         return;
      }

      addDependencies();

      for (JavaResource jr : javaTargets)
      {
         JavaClass entity = (JavaClass) (jr).getJavaSource();
         createViews(entity);
         out.println("***SUCCESS*** Generated UI for [" + entity.getQualifiedName() + "]");
      }

   }

   public Project addDependencies()
   {
      Project project = shell.getCurrentProject();
      DependencyFacet df = project.getFacet(DependencyFacet.class);
      CDIFacet cdi = project.getFacet(CDIFacet.class);
      if (!df.hasDependency(metawidget))
      {
         df.addDependency(metawidget);
      }
      if (!df.hasDependency(seamPersist))
      {
         df.addDependency(seamPersist);
         BeansModel config = cdi.getConfig();
         String persistenceInterceptor = "org.jboss.seam.persistence.transaction.TransactionInterceptor";
         List<String> interceptors = config.getInterceptors();
         if (!interceptors.contains(persistenceInterceptor))
         {
            interceptors.add(persistenceInterceptor);
         }
         cdi.saveConfig(config);
      }
      if (!df.hasDependency(weldX))
      {
         df.addDependency(weldX);
      }
      return project;
   }

   private List<JavaResource> selectTargets(PipeOut out, Resource<?>[] targets) throws FileNotFoundException
   {
      List<JavaResource> results = new ArrayList<JavaResource>();
      for (Resource<?> r : targets)
      {
         if (r instanceof JavaResource)
         {
            JavaSource<?> entity = ((JavaResource) r).getJavaSource();
            if (entity instanceof JavaClass)
            {
               if (entity.hasAnnotation(Entity.class))
               {
                  results.add((JavaResource) r);
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
      return results;
   }

   private void createViews(JavaClass entity) throws FileNotFoundException
   {
      Project project = shell.getCurrentProject();
      JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);
      WebResourceFacet web = project.getFacet(WebResourceFacet.class);

      if (!entity.hasMethod("toString"))
      {
         Refactory.createToStringFromFields(entity);
         java.saveJavaClass(entity);
      }

      CompiledTemplateResource backingBeanTemplate = compiler.compile(BACKING_BEAN_TEMPLATE);
      HashMap<Object, Object> context = new HashMap<Object, Object>();
      context.put("entity", entity);

      // Create the Backing Bean for this entity
      JavaClass viewBean = JavaParser.parse(JavaClass.class, backingBeanTemplate.render(context));
      viewBean.setPackage(java.getBasePackage() + ".view");
      java.saveJavaClass(viewBean);

      // Set context for view generation
      context = new HashMap<Object, Object>();
      String name = viewBean.getName();
      name = name.substring(0, 1).toLowerCase() + name.substring(1);
      context.put("beanName", name);
      context.put("entity", entity);

      // Generate views
      String type = entity.getName().toLowerCase();
      web.createWebResource(viewTemplate.render(context), "scaffold/" + type + "/view.xhtml");
      web.createWebResource(createTemplate.render(context), "scaffold/" + type + "/create.xhtml");
      web.createWebResource(listTemplate.render(context), "scaffold/" + type + "/list.xhtml");
   }

   private void displaySkippingResourceMsg(PipeOut out, JavaSource<?> entity)
   {
      if (!out.isPiped())
      {
         out.println(out.renderColor(ShellColor.RED, "Notice:") + " skipped non-@Entity class [" + entity.getQualifiedName() + "]");
      }
   }
}
