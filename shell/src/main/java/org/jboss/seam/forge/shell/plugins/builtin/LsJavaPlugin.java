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

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.jboss.seam.forge.parser.java.Field;
import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.parser.java.Member;
import org.jboss.seam.forge.parser.java.Method;
import org.jboss.seam.forge.parser.java.Parameter;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.resources.builtin.JavaResource;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.color.JavaColorizer;
import org.jboss.seam.forge.shell.plugins.DefaultCommand;
import org.jboss.seam.forge.shell.plugins.Help;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.OverloadedName;
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
@ResourceScope(JavaResource.class)
@Topic("File & Resources")
@Help("Prints the contents current Java file")
public class LsJavaPlugin implements Plugin
{
   @Inject
   private Shell shell;

   @DefaultCommand
   public void run(@Option(flagOnly = true, name = "all", shortName = "a", required = false) final boolean showAll,
                   @Option(flagOnly = true, name = "list", shortName = "l", required = false) final boolean list,
                   @Option(description = "path", defaultValue = ".") final String... path) throws FileNotFoundException
   {

      Resource<?> currentResource = shell.getCurrentResource();
      JavaClass javaClass = null;

      if (currentResource instanceof JavaResource)
      {
         JavaResource javaSource = (JavaResource) currentResource;
         javaClass = javaSource.getJavaClass();

         for (String p : path)
         {
            if (!".".equals(p))
            {
               List<Member<?>> members = javaClass.getMembers();
               if (members.size() > 0)
               {
                  shell.println();
               }
               for (Member<?> member : members)
               {
                  if (p.equals(member.getName()))
                  {
                     shell.println(JavaColorizer.format(shell, member.toString()));
                  }
               }
            }
            else
            {
               List<String> output = new ArrayList<String>();

               shell.println();
               shell.println(ShellColor.RED, "[fields]");
               List<Field> fields = javaClass.getFields();

               for (Field field : fields)
               {
                  String entry = shell.renderColor(ShellColor.BLUE, field.getVisibility().scope());
                  entry += shell.renderColor(ShellColor.GREEN, " " + field.getType() + "");
                  entry += " " + field.getName() + ";";
                  output.add(entry);
               }
               GeneralUtils.printOutColumns(output, shell, true);
               shell.println();

               // rinse and repeat for methods
               output = new ArrayList<String>();
               List<Method> methods = javaClass.getMethods();
               shell.println(ShellColor.RED, "[methods]");
               for (Method method : methods)
               {
                  String entry = shell.renderColor(ShellColor.BLUE, method.getVisibility().scope());
                  String parameterString = "(";

                  for (Parameter param : method.getParameters())
                  {
                     parameterString += param.toString();
                  }
                  parameterString += ")";

                  entry += " : " + method.getName() + parameterString;
                  entry += shell.renderColor(ShellColor.GREEN, " : " + method.getReturnType() + "");
                  output.add(entry);
               }
               GeneralUtils.printOutColumns(output, shell, true);
               shell.println();
            }
         }
      }
   }
}