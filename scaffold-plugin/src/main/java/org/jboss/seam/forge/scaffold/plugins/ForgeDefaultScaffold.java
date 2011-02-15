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
package org.jboss.seam.forge.scaffold.plugins;

import org.jboss.seam.forge.parser.JavaParser;
import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.parser.java.util.Refactory;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.facets.JavaSourceFacet;
import org.jboss.seam.forge.project.facets.WebResourceFacet;
import org.jboss.seam.forge.scaffold.ScaffoldProvider;
import org.jboss.seam.forge.shell.ShellPrintWriter;
import org.jboss.seam.render.TemplateCompiler;
import org.jboss.seam.render.template.CompiledTemplateResource;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.HashMap;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class ForgeDefaultScaffold implements ScaffoldProvider
{
   private static final String BACKING_BEAN_TEMPLATE = "org/jboss/seam/forge/scaffold/templates/BackingBean.jtpl";
   private static final String VIEW_TEMPLATE = "org/jboss/seam/forge/scaffold/templates/view.xhtml";
   private static final String CREATE_TEMPLATE = "org/jboss/seam/forge/scaffold/templates/create.xhtml";
   private static final String LIST_TEMPLATE = "org/jboss/seam/forge/scaffold/templates/list.xhtml";

   private CompiledTemplateResource viewTemplate;
   private CompiledTemplateResource createTemplate;
   private CompiledTemplateResource listTemplate;

   @Inject
   private ShellPrintWriter writer;

   @Inject
   private TemplateCompiler compiler;

   @PostConstruct
   public void init()
   {
      viewTemplate = compiler.compile(VIEW_TEMPLATE);
      createTemplate = compiler.compile(CREATE_TEMPLATE);
      listTemplate = compiler.compile(LIST_TEMPLATE);
   }

   @Inject
   private Project project;

   @Override
   public void fromEntity(final JavaClass entity, final boolean overwrite)
   {
      try
      {
         JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);
         WebResourceFacet web = project.getFacet(WebResourceFacet.class);

         if (!entity.hasMethod("toString"))
         {
            Refactory.createToStringFromFields(entity);
            ScaffoldPlugin.createOrOverwrite(writer, java.getJavaResource(entity), entity.toString(), overwrite);
         }

         CompiledTemplateResource backingBeanTemplate = compiler.compile(BACKING_BEAN_TEMPLATE);
         HashMap<Object, Object> context = new HashMap<Object, Object>();
         context.put("entity", entity);

         // Create the Backing Bean for this entity
         JavaClass viewBean = JavaParser.parse(JavaClass.class, backingBeanTemplate.render(context));
         viewBean.setPackage(java.getBasePackage() + ".view");
         ScaffoldPlugin.createOrOverwrite(writer, java.getJavaResource(viewBean), viewBean.toString(), overwrite);

         // Set context for view generation
         context = new HashMap<Object, Object>();
         String name = viewBean.getName();
         name = name.substring(0, 1).toLowerCase() + name.substring(1);
         context.put("beanName", name);
         context.put("entity", entity);

         // Generate views
         String type = entity.getName().toLowerCase();
         ScaffoldPlugin.createOrOverwrite(writer, web.getWebResource("scaffold/" + type + "/view.xhtml"),
                  viewTemplate.render(context), overwrite);
         ScaffoldPlugin.createOrOverwrite(writer, web.getWebResource("scaffold/" + type + "/create.xhtml"),
                  createTemplate.render(context),
                  overwrite);
         ScaffoldPlugin.createOrOverwrite(writer, web.getWebResource("scaffold/" + type + "/list.xhtml"),
                  listTemplate.render(context), overwrite);
      }
      catch (Exception e)
      {
         throw new RuntimeException("Error generating default scaffolding.", e);
      }
   }

}
