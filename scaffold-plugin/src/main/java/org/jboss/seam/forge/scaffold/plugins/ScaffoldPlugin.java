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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.Entity;

import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.parser.java.JavaSource;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.facets.WebResourceFacet;
import org.jboss.seam.forge.resources.FileResource;
import org.jboss.seam.forge.resources.Resource;
import org.jboss.seam.forge.resources.java.JavaResource;
import org.jboss.seam.forge.scaffold.ScaffoldProvider;
import org.jboss.seam.forge.scaffold.providers.MetawidgetScaffold;
import org.jboss.seam.forge.scaffold.shell.ScaffoldProviderCompleter;
import org.jboss.seam.forge.shell.ShellMessages;
import org.jboss.seam.forge.shell.ShellPrintWriter;
import org.jboss.seam.forge.shell.ShellPrompt;
import org.jboss.seam.forge.shell.events.InstallFacets;
import org.jboss.seam.forge.shell.plugins.Alias;
import org.jboss.seam.forge.shell.plugins.Command;
import org.jboss.seam.forge.shell.plugins.Current;
import org.jboss.seam.forge.shell.plugins.Help;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.PipeOut;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.shell.plugins.RequiresProject;
import org.jboss.seam.forge.shell.plugins.Topic;
import org.jboss.seam.forge.shell.util.ConstraintInspector;
import org.jboss.seam.forge.spec.cdi.CDIFacet;
import org.jboss.seam.forge.spec.jpa.PersistenceFacet;
import org.jboss.seam.forge.spec.jsf.FacesFacet;
import org.jboss.seam.forge.spec.servlet.ServletFacet;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@Alias("scaffold")
@Topic("UI Generation & Scaffolding")
@Help("Metawidget UI scaffolding")
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
   private MetawidgetScaffold defaultScaffold;

   @Inject
   private Event<InstallFacets> install;

   @Command("setup")
   @SuppressWarnings("unchecked")
   public void setup(PipeOut out)
   {
      /*
       * TODO this should probably accept a scaffold type object itself other methods should check to see if any
       * scaffold providers have been installed
       */
      if (!(project.hasFacet(WebResourceFacet.class) && project.hasFacet(PersistenceFacet.class)
               && project.hasFacet(CDIFacet.class) && project.hasFacet(FacesFacet.class)))
      {
         install.fire(new InstallFacets(WebResourceFacet.class, PersistenceFacet.class, CDIFacet.class,
                  FacesFacet.class));

         if (project.hasFacet(WebResourceFacet.class) && project.hasFacet(PersistenceFacet.class)
                  && project.hasFacet(CDIFacet.class) && project.hasFacet(FacesFacet.class))
         {
            ShellMessages.success(out, "Scaffolding installed.");
         }
      }
   }

   @Command("create-indexes")
   public void createIndex(
            PipeOut out,
            @Option(flagOnly = true, name = "overwrite") final boolean overwrite)
   {
      setup(out);
      WebResourceFacet web = project.getFacet(WebResourceFacet.class);

      project.getFacet(ServletFacet.class).getConfig().welcomeFile("index.html");

      createOrOverwrite(writer, web.getWebResource("index.html"), getClass()
               .getResourceAsStream("/org/jboss/seam/forge/jsf/index.html"), overwrite);

      createOrOverwrite(writer, web.getWebResource("index.xhtml"),
               getClass().getResourceAsStream("/org/jboss/seam/forge/jsf/index.xhtml"), overwrite);

      createDefaultTemplate(out, overwrite);
   }

   @Command("create-default-template")
   public void createDefaultTemplate(
            final PipeOut out,
            @Option(flagOnly = true, name = "overwrite") final boolean overwrite)
   {
      setup(out);
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
      setup(out);
      if (((targets == null) || (targets.length < 1))
               && (currentResource instanceof JavaResource))
      {
         targets = new JavaResource[] { (JavaResource) currentResource };
      }

      List<JavaResource> javaTargets = selectTargets(out, targets);
      if (javaTargets.isEmpty())
      {
         ShellMessages.error(out, "Must specify a domain entity on which to operate.");
         return;
      }

      ScaffoldProvider provider = getScaffoldType(scaffoldType);
      if (provider == null)
      {
         ShellMessages.error(out, "Aborted - no scaffold type selected.");
         return;
      }

      if (!provider.isInstalledIn(project))
      {
         provider.installInto(project);
      }

      createDefaultTemplate(out, overwrite);

      for (JavaResource jr : javaTargets)
      {
         JavaClass entity = (JavaClass) (jr).getJavaSource();
         provider.fromEntity(project, entity, overwrite);
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
