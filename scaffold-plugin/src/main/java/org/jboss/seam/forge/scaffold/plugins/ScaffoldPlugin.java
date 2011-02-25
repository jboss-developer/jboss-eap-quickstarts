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

import org.jboss.seam.forge.parser.JavaParser;
import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.parser.java.JavaSource;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.constraints.ConstraintInspector;
import org.jboss.seam.forge.project.constraints.RequiresFacets;
import org.jboss.seam.forge.project.constraints.RequiresProject;
import org.jboss.seam.forge.project.dependencies.Dependency;
import org.jboss.seam.forge.project.dependencies.DependencyBuilder;
import org.jboss.seam.forge.project.facets.DependencyFacet;
import org.jboss.seam.forge.project.facets.JavaSourceFacet;
import org.jboss.seam.forge.project.facets.WebResourceFacet;
import org.jboss.seam.forge.project.facets.builtin.MavenWebResourceFacet;
import org.jboss.seam.forge.project.resources.FileResource;
import org.jboss.seam.forge.project.resources.builtin.java.JavaResource;
import org.jboss.seam.forge.scaffold.ScaffoldProvider;
import org.jboss.seam.forge.scaffold.ScaffoldProviderCompleter;
import org.jboss.seam.forge.shell.ShellMessages;
import org.jboss.seam.forge.shell.ShellPrintWriter;
import org.jboss.seam.forge.shell.ShellPrompt;
import org.jboss.seam.forge.shell.plugins.*;
import org.jboss.seam.forge.spec.cdi.CDIFacet;
import org.jboss.seam.forge.spec.jpa.PersistenceFacet;
import org.jboss.seam.forge.spec.jsf.FacesFacet;
import org.jboss.seam.forge.spec.servlet.ServletFacet;
import org.jboss.shrinkwrap.descriptor.api.spec.cdi.beans.BeansDescriptor;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Entity;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
   @Inject
   @Current
   private Resource<?> currentResource;

   @Inject
   private Project project;
   @Inject
   private ShellPrintWriter writer;
   @Inject
   private ShellPrompt prompt;

   @Inject
   private Instance<ScaffoldProvider> impls;

   @Inject
   private ForgeDefaultScaffold defaultScaffold;

   private final Dependency metawidget = DependencyBuilder.create("org.metawidget:metawidget:1.0.5");
   private final Dependency seamPersist = DependencyBuilder
            .create("org.jboss.seam.persistence:seam-persistence-impl:3.0.0.Beta4");

   @Command("create-persistence-utils")
   public void createPersistenceUtils(
            @Option(flagOnly = true, name = "overwrite") final boolean overwrite)
   {
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      JavaClass util = JavaParser.parse(JavaClass.class,
               loader.getResourceAsStream("org/jboss/seam/forge/jpa/PersistenceUtil.jtpl"));
      JavaClass producer = JavaParser.parse(JavaClass.class,
               loader.getResourceAsStream("org/jboss/seam/forge/jpa/DatasourceProducer.jtpl"));

      JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);

      try
      {
         JavaResource producerResource = java.getJavaResource(producer);
         JavaResource utilResource = java.getJavaResource(util);

         createOrOverwrite(writer, producerResource, producer.toString(), overwrite);
         createOrOverwrite(writer, utilResource, util.toString(), overwrite);
      }
      catch (FileNotFoundException e)
      {
         throw new RuntimeException(e);
      }
   }

   @Command("create-indexes")
   public void createIndex(
            @Option(flagOnly = true, name = "overwrite") final boolean overwrite)
   {
      WebResourceFacet web = project.getFacet(WebResourceFacet.class);

      project.getFacet(ServletFacet.class).getConfig().welcomeFile("index.html");

      createOrOverwrite(writer, web.getWebResource("index.html"), getClass()
               .getResourceAsStream("/org/jboss/seam/forge/jsf/index.html"), overwrite);

      createOrOverwrite(writer, web.getWebResource("index.xhtml"),
               getClass().getResourceAsStream("/org/jboss/seam/forge/jsf/index.xhtml"), overwrite);

      createDefaultTemplate(overwrite);
   }

   @Command("create-default-template")
   public void createDefaultTemplate(
            @Option(flagOnly = true, name = "overwrite") final boolean overwrite)
   {
      WebResourceFacet web = project.getFacet(WebResourceFacet.class);

      createOrOverwrite(writer, web.getWebResource("/resources/forge-template.xhtml"),
               getClass().getResourceAsStream("/org/jboss/seam/forge/jsf/forge-template.xhtml"), overwrite);

      createOrOverwrite(writer, web.getWebResource("/resources/forge.css"),
               getClass().getResourceAsStream("/org/jboss/seam/forge/jsf/forge.css"), overwrite);

      createOrOverwrite(writer, web.getWebResource("/resources/favicon.ico"),
               getClass().getResourceAsStream("/org/jboss/seam/forge/web/favicon.ico"), overwrite);
   }

   @Command("from-entity")
   public void generateFromEntity(
            @Option(name = "scaffoldType", required = false,
                     completer = ScaffoldProviderCompleter.class) final String scaffoldType,
            @Option(flagOnly = true, name = "overwrite") final boolean overwrite,
            @Option(required = false) JavaResource[] targets,
            final PipeOut out) throws FileNotFoundException
   {
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
            ShellMessages.error(out, "Must specify a domain entity on which to operate.");
         }
         return;
      }

      ScaffoldProvider scaffoldImpl = getScaffoldType(scaffoldType);

      if (scaffoldImpl == null)
      {
         ShellMessages.error(out, "Aborted - no scaffold type selected.");
         return;
      }

      createDefaultTemplate(overwrite);
      createPersistenceUtils(overwrite);
      addDependencies();

      for (JavaResource jr : javaTargets)
      {
         JavaClass entity = (JavaClass) (jr).getJavaSource();
         scaffoldImpl.fromEntity(entity, overwrite);
         ShellMessages.success(out, "Generated UI for [" + entity.getQualifiedName() + "]");
      }

   }

   private ScaffoldProvider getScaffoldType(final String scaffoldType)
   {
      ScaffoldProvider scaffoldImpl = null;
      for (ScaffoldProvider type : impls)
      {
         if (ConstraintInspector.getName(type.getClass()).equals(scaffoldType))
         {
            scaffoldImpl = type;
         }
      }

      if ((scaffoldImpl == null) && (scaffoldType != null)
               && prompt.promptBoolean("No scaffold found for [" + scaffoldType + "], use Forge default?", true))
      {
         scaffoldImpl = defaultScaffold;
      }
      else if ((scaffoldImpl == null)
               && prompt.promptBoolean("No scaffold type was provided, use Forge default?", true))
      {
         scaffoldImpl = defaultScaffold;
      }
      return scaffoldImpl;
   }

   public Project addDependencies()
   {
      DependencyFacet df = project.getFacet(DependencyFacet.class);
      CDIFacet cdi = project.getFacet(CDIFacet.class);
      if (!df.hasDependency(metawidget))
      {
         df.addDependency(metawidget);
      }
      if (!df.hasDependency(seamPersist))
      {
         df.addDependency(seamPersist);
         BeansDescriptor config = cdi.getConfig();
         config.interceptor("org.jboss.seam.persistence.transaction.TransactionInterceptor");
         cdi.saveConfig(config);
      }
      return project;
   }

   private List<JavaResource> selectTargets(final PipeOut out, Resource<?>[] targets)
            throws FileNotFoundException
   {
      List<JavaResource> results = new ArrayList<JavaResource>();
      if (targets == null)
      {
         targets = new Resource<?>[] {};
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

   private void displaySkippingResourceMsg(final PipeOut out, final JavaSource<?> entity)
   {
      if (!out.isPiped())
      {
         ShellMessages.info(out, "Skipped non-@Entity Java resource ["
                  + entity.getQualifiedName() + "]");
      }
   }

   public static void createOrOverwrite(final ShellPrintWriter writer, final FileResource<?> resource,
            final InputStream contents,
            final boolean overwrite)
   {
      if (!resource.exists() || overwrite)
      {
         resource.createNewFile();
         resource.setContents(contents);
      }
      else
      {
         ShellMessages.info(writer, "[" + resource.getFullyQualifiedName()
                           + "] File exists, re-run with `--overwrite` to replace existing files.");
      }
   }

   public static void createOrOverwrite(final ShellPrintWriter writer, final FileResource<?> resource,
            final String contents,
            final boolean overwrite)
   {
      if (!resource.exists() || overwrite)
      {
         resource.createNewFile();
         resource.setContents(contents);
      }
      else
      {
         ShellMessages.info(writer, "[" + resource.getFullyQualifiedName()
                           + "] File exists, re-run with `--overwrite` to replace existing files.");
      }
   }
}
