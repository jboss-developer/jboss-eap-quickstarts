/*
 * JBoss, by Red Hat.
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

package org.jboss.seam.forge.shell.plugins.builtin;

import static org.jboss.seam.forge.shell.util.GeneralUtils.printOutColumns;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.jboss.seam.forge.parser.java.Field;
import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.parser.java.JavaSource;
import org.jboss.seam.forge.parser.java.Method;
import org.jboss.seam.forge.parser.java.Parameter;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.resources.builtin.java.JavaFieldResource;
import org.jboss.seam.forge.project.resources.builtin.java.JavaMemberResource;
import org.jboss.seam.forge.project.resources.builtin.java.JavaMethodResource;
import org.jboss.seam.forge.project.resources.builtin.java.JavaResource;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.color.JavaColorizer;
import org.jboss.seam.forge.shell.plugins.DefaultCommand;
import org.jboss.seam.forge.shell.plugins.Help;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.OverloadedName;
import org.jboss.seam.forge.shell.plugins.PipeOut;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.shell.plugins.ResourceScope;
import org.jboss.seam.forge.shell.plugins.Topic;
import org.jboss.seam.forge.shell.util.GeneralUtils;
import org.jboss.seam.forge.shell.util.ShellColor;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * @author Mike Brock
 */
@OverloadedName("ls")
@ResourceScope({ JavaResource.class, JavaMethodResource.class, JavaFieldResource.class })
@Topic("File & Resources")
@Help("Prints the contents current Java file")
public class LsJavaPlugin implements Plugin
{
   private static final String DELIM = "::";

   @Inject
   private Shell shell;

   @DefaultCommand
   public void run(@Option(flagOnly = true, name = "all", shortName = "a", required = false) final boolean showAll,
                   @Option(flagOnly = true, name = "list", shortName = "l", required = false) final boolean list,
                   @Option(description = "path", defaultValue = ".") final Resource<?>[] paths,
                   final PipeOut out) throws FileNotFoundException
   {

      for (Resource<?> resource : paths)
      {
         if (resource instanceof JavaResource)
         {
            if (showAll)
            {
               out.print(JavaColorizer.format(out, ((JavaResource) resource).getJavaSource().toString()));
            }
            else
            {
               JavaResource javaResource = (JavaResource) resource;
               JavaSource<?> javaSource = javaResource.getJavaSource();
               List<String> output = new ArrayList<String>();

               if (!out.isPiped())
               {
                  out.println();
                  out.println(ShellColor.RED, "[fields]");
               }

               if (javaSource instanceof JavaClass)
               {
                  JavaClass javaClass = (JavaClass) javaSource;
                  List<Field<JavaClass>> fields = javaClass.getFields();

                  for (Field<JavaClass> field : fields)
                  {
                     String entry = out.renderColor(ShellColor.BLUE, field.getVisibility().scope());
                     entry += out.renderColor(ShellColor.GREEN, DELIM + field.getType() + "");
                     entry += DELIM + field.getName() + ";";
                     output.add(entry);
                  }

                  if (out.isPiped())
                  {
                     GeneralUtils.OutputAttributes attr = new GeneralUtils.OutputAttributes(120, 1);
                     printOutColumns(output, ShellColor.NONE, out, attr, null, false);
                  }
                  else
                  {
                     GeneralUtils.printOutColumns(output, out, shell, true);
                     out.println();
                  }

                  // rinse and repeat for methods
                  output = new ArrayList<String>();
                  List<Method<JavaClass>> methods = javaClass.getMethods();

                  if (!out.isPiped())
                  {
                     out.println(ShellColor.RED, "[methods]");
                  }

                  for (Method<JavaClass> method : methods)
                  {
                     String entry = out.renderColor(ShellColor.BLUE, method.getVisibility().scope());
                     String parameterString = "(";

                     for (Parameter<JavaClass> param : method.getParameters())
                     {
                        parameterString += param.toString();
                     }
                     parameterString += ")";

                     entry += DELIM + method.getName() + parameterString;

                     String returnType = method.getReturnType() == null ? "void" : method.getReturnType();
                     entry += out.renderColor(ShellColor.GREEN, DELIM + returnType + "");
                     output.add(entry);
                  }

                  if (out.isPiped())
                  {
                     GeneralUtils.OutputAttributes attr = new GeneralUtils.OutputAttributes(120, 1);
                     printOutColumns(output, ShellColor.NONE, out, attr, null, false);
                  }
                  else
                  {
                     GeneralUtils.printOutColumns(output, out, shell, true);
                     out.println();
                  }
               }
            }
         }
         else if (resource instanceof JavaMemberResource<?>)
         {
            out.println();
            out.println(JavaColorizer.format(out, resource.toString()));
         }
      }
   }
}