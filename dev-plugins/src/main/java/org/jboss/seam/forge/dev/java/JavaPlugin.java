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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.forge.parser.JavaParser;
import org.jboss.seam.forge.parser.java.Import;
import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.parser.java.JavaSource;
import org.jboss.seam.forge.parser.java.SyntaxError;
import org.jboss.seam.forge.parser.java.util.Strings;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.constraints.RequiresFacet;
import org.jboss.seam.forge.project.facets.JavaSourceFacet;
import org.jboss.seam.forge.project.resources.builtin.java.JavaResource;
import org.jboss.seam.forge.shell.PromptType;
import org.jboss.seam.forge.shell.ShellPrintWriter;
import org.jboss.seam.forge.shell.ShellPrompt;
import org.jboss.seam.forge.shell.color.JavaColorizer;
import org.jboss.seam.forge.shell.plugins.Command;
import org.jboss.seam.forge.shell.plugins.Current;
import org.jboss.seam.forge.shell.plugins.DefaultCommand;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.PipeIn;
import org.jboss.seam.forge.shell.plugins.PipeOut;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.shell.plugins.ResourceScope;
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
   @Current
   private JavaResource resource;

   @Inject
   private Project project;

   @Inject
   private ShellPrompt prompt;

   @Inject
   private ShellPrintWriter writer;

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
            @PipeIn final InputStream in,
            @Option(required = false,
                     help = "the package in which to build this Class",
                     description = "source package",
                     type = PromptType.JAVA_PACKAGE,
                     name = "package") final String pckg,
            @Option(required = false,
                     help = "the class definition: surround with quotes",
                     description = "class definition") final String... def) throws FileNotFoundException
   {

      JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);

      JavaClass jc = null;
      if (def != null)
      {
         String classDef = Strings.join(Arrays.asList(def), " ");
         jc = JavaParser.parse(JavaClass.class, classDef);
      }
      else if (in != null)
      {
         jc = JavaParser.parse(JavaClass.class, in);
      }
      else
      {
         throw new RuntimeException("arguments required");
      }

      if (pckg != null)
      {
         jc.setPackage(pckg);
      }

      if (!jc.hasSyntaxErrors())
      {
         java.saveJavaSource(jc);
      }
      else
      {
         writer.println(ShellColor.RED, "Syntax Errors:");
         for (SyntaxError error : jc.getSyntaxErrors())
         {
            writer.println(error.toString());
         }
         writer.println();

         if (prompt.promptBoolean("Your class has syntax errors, create anyway?", true))
         {
            java.saveJavaSource(jc);
         }
      }
   }

   @Command("list-imports")
   @ResourceScope(JavaResource.class)
   public void listImports(
            final PipeOut out) throws FileNotFoundException
   {
      List<Import> imports = resource.getJavaSource().getImports();
      for (Import i : imports)
      {
         String str = "import " + (i.isStatic() ? "static " : "") + i.getQualifiedName() + ";";
         str = JavaColorizer.format(out, str);
         out.println(str);
      }
   }

   @Command("new-field")
   @ResourceScope(JavaResource.class)
   public void newField(
            @PipeIn final String in,
            final PipeOut out,
            @Option(required = false,
                     help = "the method definition: surround with single quotes",
                     description = "class definition") final String... def) throws FileNotFoundException
   {
      JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);

      String fieldDef = null;
      if (def != null)
      {
         fieldDef = Strings.join(Arrays.asList(def), " ");
      }
      else if (in != null)
      {
         fieldDef = in;
      }
      else
      {
         throw new RuntimeException("arguments required");
      }

      JavaSource<?> source = resource.getJavaSource();
      if (source instanceof JavaClass)
      {
         JavaClass clazz = ((JavaClass) source);
         clazz.addField(fieldDef);
         java.saveJavaSource(clazz);
      }
   }
}
