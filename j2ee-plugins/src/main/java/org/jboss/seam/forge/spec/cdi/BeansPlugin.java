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
package org.jboss.seam.forge.spec.cdi;

import java.io.FileNotFoundException;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.forge.parser.JavaParser;
import org.jboss.seam.forge.parser.java.JavaClass;
import org.jboss.seam.forge.parser.java.Method;
import org.jboss.seam.forge.parser.java.SyntaxError;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.constraints.RequiresFacet;
import org.jboss.seam.forge.project.facets.JavaSourceFacet;
import org.jboss.seam.forge.project.resources.builtin.java.JavaResource;
import org.jboss.seam.forge.shell.PromptType;
import org.jboss.seam.forge.shell.ShellMessages;
import org.jboss.seam.forge.shell.ShellPrompt;
import org.jboss.seam.forge.shell.events.InstallFacet;
import org.jboss.seam.forge.shell.events.PickupResource;
import org.jboss.seam.forge.shell.plugins.Command;
import org.jboss.seam.forge.shell.plugins.Current;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.PipeOut;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.shell.plugins.ResourceScope;
import org.jboss.seam.forge.shell.util.ShellColor;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@Named("beans")
@RequiresFacet(JavaSourceFacet.class)
public class BeansPlugin implements Plugin
{
   @Inject
   private Event<InstallFacet> install;

   @Inject
   private Event<PickupResource> pickup;

   @Inject
   private Project project;

   @Inject
   private ShellPrompt prompt;

   @Inject
   @Current
   private JavaResource resource;

   @Command("setup")
   public void setup(final PipeOut out)
   {
      if (!project.hasFacet(CDIFacet.class))
      {
         install.fire(new InstallFacet(CDIFacet.class));
      }
      if (project.hasFacet(CDIFacet.class))
      {
         ShellMessages.success(out, "CDI [JSR-299] is installed.");
      }

   }

   @Command("list-interceptors")
   public void listInterceptors(final PipeOut out)
   {
      CDIFacet cdi = project.getFacet(CDIFacet.class);
      List<String> interceptors = cdi.getConfig().getInterceptors();
      for (String i : interceptors)
      {
         out.println(i);
      }
   }

   @Command("list-decorators")
   public void listDecorators(final PipeOut out)
   {
      CDIFacet cdi = project.getFacet(CDIFacet.class);
      List<String> decorators = cdi.getConfig().getDecorators();
      for (String d : decorators)
      {
         out.println(d);
      }
   }

   @Command("list-alternatives")
   public void listAlternatives(final PipeOut out)
   {
      CDIFacet cdi = project.getFacet(CDIFacet.class);
      List<String> classes = cdi.getConfig().getAlternativeClasses();
      List<String> stereotypes = cdi.getConfig().getAlternativeStereotypes();

      if (!out.isPiped())
         out.println(ShellColor.BOLD, "Stereotypes:");

      for (String s : stereotypes)
      {
         out.println(s);
      }

      if (!out.isPiped())
         out.println(ShellColor.BOLD, "Classes:");

      for (String c : classes)
      {
         out.println(c);
      }
   }

   @Command("new-conversation")
   @ResourceScope(JavaResource.class)
   public void newConversation(
            @Option(name = "timeout") final Long timeout,
            @Option(name = "named", defaultValue = "") final String name,
            @Option(name = "beginMethodName", defaultValue = "beginConversation") final String beginName,
            @Option(name = "endMethodName", defaultValue = "endConversation") final String endName,
            @Option(name = "conversationFieldName", defaultValue = "conversation") final String fieldName,
            @Option(name = "overwrite") boolean overwrite,
            final PipeOut out) throws FileNotFoundException
   {
      if (resource.exists())
      {
         if (resource.getJavaSource().isClass())
         {
            JavaClass javaClass = (JavaClass) resource.getJavaSource();

            if (javaClass.hasField(fieldName) && !javaClass.getField(fieldName).isType(Conversation.class))
            {
               if (overwrite)
               {
                  javaClass.removeField(javaClass.getField(fieldName));
               }
               else
               {
                  throw new RuntimeException("Field [" + fieldName + "] exists. Re-run with '--overwrite' to continue.");
               }
            }
            if (javaClass.hasMethodSignature(beginName) && javaClass.getMethod(beginName).getParameters().size() == 0)
            {
               if (overwrite)
               {
                  javaClass.removeMethod(javaClass.getMethod(beginName));
               }
               else
               {
                  throw new RuntimeException("Method [" + beginName
                           + "] exists. Re-run with '--overwrite' to continue.");
               }
            }
            if (javaClass.hasMethodSignature(endName) && javaClass.getMethod(endName).getParameters().size() == 0)
            {
               if (overwrite)
               {
                  javaClass.removeMethod(javaClass.getMethod(endName));
               }
               else
               {
                  throw new RuntimeException("Method [" + endName + "] exists. Re-run with '--overwrite' to continue.");
               }
            }

            javaClass.addField().setPrivate().setName(fieldName).setType(Conversation.class)
                     .addAnnotation(Inject.class);

            Method<JavaClass> beginMethod = javaClass.addMethod().setName(beginName).setReturnTypeVoid().setPublic()
                     .setBody(fieldName + ".begin(" + name + ");");

            if (timeout != null)
            {
               beginMethod.setBody(beginMethod.getBody() + "\n" + fieldName + ".setTimeout(" + timeout + ");");
            }

            javaClass.addMethod().setName(endName).setReturnTypeVoid().setPublic()
                     .setBody(fieldName + ".end();");

            if (javaClass.hasSyntaxErrors())
            {
               ShellMessages.info(out, "Modified Java class contains syntax errors:");
               for (SyntaxError error : javaClass.getSyntaxErrors())
               {
                  out.print(error.getDescription());
               }
            }

            resource.setContents(javaClass);
         }
         else
         {
            ShellMessages.error(out, "Must operate on a Java Class file, not an ["
                     + resource.getJavaSource().getSourceType() + "]");
         }
      }
   }

   @Command("new-bean")
   public void newBean(
            @Option(required = true,
                     name = "type") final JavaResource resource,
            @Option(required = true, name = "scoped") final BeanScope scope,
            @Option(required = false, name = "overwrite") final boolean overwrite
            ) throws FileNotFoundException
   {
      if (!resource.exists() || overwrite)
      {
         JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);
         if (resource.createNewFile())
         {
            JavaClass javaClass = JavaParser.create(JavaClass.class);
            javaClass.setName(java.calculateName(resource));
            javaClass.setPackage(java.calculatePackage(resource));

            if (BeanScope.CUSTOM.equals(scope))
            {
               String annoType = prompt.promptCommon("Enter the qualified custom scope type:", PromptType.JAVA_CLASS);
               javaClass.addAnnotation(annoType);
            }
            else if (!BeanScope.DEPENDENT.equals(scope))
            {
               javaClass.addAnnotation(scope.getAnnotation());
            }
            resource.setContents(javaClass);
            pickup.fire(new PickupResource(resource));
         }
      }
      else
      {
         throw new RuntimeException("Type already exists [" + resource.getFullyQualifiedName()
                  + "] Re-run with '--overwrite' to continue.");
      }
   }
}
