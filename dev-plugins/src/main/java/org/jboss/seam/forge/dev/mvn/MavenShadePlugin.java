/*
 * JBoss, by Red Hat.
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

package org.jboss.seam.forge.dev.mvn;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.apache.maven.model.Model;
import org.apache.maven.model.PluginExecution;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.jboss.seam.forge.parser.xml.XMLParser;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.dependencies.Dependency;
import org.jboss.seam.forge.project.dependencies.DependencyBuilder;
import org.jboss.seam.forge.project.facets.JavaSourceFacet;
import org.jboss.seam.forge.project.facets.MavenCoreFacet;
import org.jboss.seam.forge.shell.PromptType;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.ShellMessages;
import org.jboss.seam.forge.shell.plugins.Alias;
import org.jboss.seam.forge.shell.plugins.Command;
import org.jboss.seam.forge.shell.plugins.Help;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.PipeOut;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.shell.plugins.RequiresFacet;
import org.jboss.seam.forge.shell.plugins.RequiresProject;
import org.jboss.seam.forge.shell.plugins.Topic;
import org.jboss.shrinkwrap.descriptor.api.DescriptorExportException;
import org.jboss.shrinkwrap.descriptor.api.Node;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */

@Alias("shade")
@Topic("Project")
@RequiresProject
@RequiresFacet({ MavenCoreFacet.class, JavaSourceFacet.class })
@Help("Provides quick configurations for the maven-shade-plugin - " +
         "http://maven.apache.org/plugins/maven-shade-plugin/")
public class MavenShadePlugin implements Plugin
{
   private final Shell shell;
   private final Project project;
   private final Dependency dep = DependencyBuilder.create("org.apache.maven.plugins:maven-shade-plugin:1.4");

   @Inject
   public MavenShadePlugin(final Shell shell, final Project project)
   {
      this.shell = shell;
      this.project = project;
   }

   @Command(help = "Setup the basic shade configuration (includes all dependencies)")
   public void setup(final PipeOut out) throws XmlPullParserException, IOException
   {
      if (!isInstalled())
      {
         install();
      }

      if (isInstalled())
      {
         ShellMessages.success(out, "Shade plugin is installed.");
      }
   }

   @Command(help = "Reset the current shade configuration (includes all dependencies).")
   public void reset() throws XmlPullParserException, IOException
   {
      assertInstalled();
      if (shell.promptBoolean("Really reset configuration?", false))
      {
         install();
      }
   }

   @Command(help = "Remove all shade configurations from the POM")
   public void remove() throws XmlPullParserException, IOException
   {
      assertInstalled();
      if (shell.promptBoolean("Really remove all shade configuration?", false))
      {
         MavenCoreFacet mvn = project.getFacet(MavenCoreFacet.class);
         Model pom = mvn.getPOM();

         pom.getBuild().removePlugin(getPlugin(pom));
         mvn.setPOM(pom);
         ShellMessages.info(shell, "Removed all shade configuration from POM.");
      }
      else if (!isInstalled())
         ShellMessages.success(shell, "Shade is not configured.");
      else
         ShellMessages.info(shell, "Aborted.");
   }

   @Command(help = "Relocates bundled dependency classes to a new package")
   public void relocate(
            @Option(name = "pattern",
                     help = "the original package",
                     type = PromptType.JAVA_PACKAGE,
                     required = true) String pattern,
            @Option(name = "shadedPattern",
                     help = "the renamed \"shaded\" package",
                     type = PromptType.JAVA_PACKAGE,
                     required = true) String shadedPattern,
            @Option(name = "excludes",
                     help = "packages to exclude from shading",
                     type = PromptType.JAVA_PACKAGE) String... excludes)
            throws DescriptorExportException, XmlPullParserException, IOException
   {
      assertInstalled();

      MavenCoreFacet mvn = project.getFacet(MavenCoreFacet.class);
      Model pom = mvn.getPOM();

      org.apache.maven.model.Plugin plugin = getPlugin(pom);
      PluginExecution execution = plugin.getExecutions().get(0);

      Node configuration = getConfiguration(execution);
      Node relocationNode = configuration.getOrCreate("relocations").create("relocation");
      relocationNode.create("pattern").text(pattern);
      relocationNode.create("shadedPattern").text(shadedPattern);

      String excludeMsg = "";
      if (excludes != null && excludes.length > 0)
      {
         Node excludesNode = relocationNode.create("excludes");
         for (String e : excludes)
         {
            excludesNode.create("exclude").text(e);
         }
         excludeMsg = ", excluding " + Arrays.asList(excludes);
      }

      setConfiguration(execution, configuration);

      mvn.setPOM(pom);
      ShellMessages.success(shell, "Relocating [" + pattern + "] to [" + shadedPattern + "]" + excludeMsg);
   }

   @Command(value = "make-executable", help = "Make the resulting jar executable by specifying the target Main class")
   public void makeExecutable(
            @Option(name = "mainClass",
                     description = "the fully qualified main class [e.g: com.example.Main]",
                     type = PromptType.JAVA_CLASS,
                     required = true) String mainClass) throws XmlPullParserException, IOException
   {
      assertInstalled();

      MavenCoreFacet mvn = project.getFacet(MavenCoreFacet.class);
      Model pom = mvn.getPOM();

      org.apache.maven.model.Plugin plugin = getPlugin(pom);
      PluginExecution execution = plugin.getExecutions().get(0);

      Node configuration = getConfiguration(execution);
      Node relocationNode = configuration.getOrCreate("transformers").getOrCreate("transformer")
               .attribute("implementation", "org.apache.maven.plugins.shade.resource.ManifestResourceTransformer");
      relocationNode.getOrCreate("mainClass").text(mainClass);

      setConfiguration(execution, configuration);

      mvn.setPOM(pom);
   }

   /*
    * Helpers
    */

   private void assertInstalled()
   {
      if (!isInstalled())
      {
         throw new RuntimeException("Shade plugin not installed. Run 'shade setup' to continue.");
      }
   }

   private Node getConfiguration(PluginExecution execution)
   {
      return XMLParser.parse(((Xpp3Dom) execution.getConfiguration()).toUnescapedString());
   }

   private void setConfiguration(PluginExecution execution, Node node)
            throws XmlPullParserException, IOException
   {
      execution.setConfiguration(Xpp3DomBuilder.build(XMLParser.toXMLInputStream(node), "UTF-8"));
   }

   private void install() throws XmlPullParserException, IOException
   {
      MavenCoreFacet mvn = project.getFacet(MavenCoreFacet.class);
      Model pom = mvn.getPOM();

      org.apache.maven.model.Plugin plugin = new org.apache.maven.model.Plugin();

      plugin.setArtifactId(dep.getArtifactId());
      plugin.setGroupId(dep.getGroupId());
      plugin.setVersion(dep.getVersion());

      Xpp3Dom dom = Xpp3DomBuilder.build(new ByteArrayInputStream("<configuration></configuration>".getBytes()),
               "UTF-8");

      List<PluginExecution> executions = plugin.getExecutions();
      PluginExecution execution = new PluginExecution();
      execution.setPhase("package");
      execution.addGoal("shade");
      execution.setConfiguration(dom);
      executions.add(execution);

      pom.getBuild().getPlugins().add(plugin);
      mvn.setPOM(pom);
   }

   private boolean isInstalled()
   {
      MavenCoreFacet mvn = project.getFacet(MavenCoreFacet.class);
      Model pom = mvn.getPOM();
      return getPlugin(pom) != null;
   }

   private org.apache.maven.model.Plugin getPlugin(Model pom)
   {
      for (org.apache.maven.model.Plugin p : pom.getBuild().getPlugins())
      {
         if (dep.getGroupId().equals(p.getGroupId()) && dep.getArtifactId().equals(p.getArtifactId()))
         {
            return p;
         }
      }
      return null;
   }
}
