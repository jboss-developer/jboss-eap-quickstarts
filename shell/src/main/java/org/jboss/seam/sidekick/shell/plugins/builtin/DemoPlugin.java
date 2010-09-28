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
package org.jboss.seam.sidekick.shell.plugins.builtin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.maven.model.Dependency;
import org.jboss.seam.sidekick.project.PackagingType;
import org.jboss.seam.sidekick.project.util.DependencyBuilder;
import org.jboss.seam.sidekick.shell.plugins.Command;
import org.jboss.seam.sidekick.shell.plugins.Help;
import org.jboss.seam.sidekick.shell.plugins.MavenPlugin;
import org.jboss.seam.sidekick.shell.plugins.Option;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Named("demoplugin")
@Help("A very basic installable plugin")
public class DemoPlugin extends MavenPlugin
{
   @Inject
   private DependencyBuilder builder;

   // @DefaultCommand
   // public void deDefault(@Option(required = true) final int value)
   // {
   // // TODO test for conflicts between default commands w/arguments and other
   // // commands
   // }

   @Command(value = "count", help = "a basic do-nothing kind of command")
   public void doSomething(@Option(required = true, description = "count") final int count,
         @Option(value = "enableOption") boolean flag)
   {
      for (int i = 0; i < count; i++)
      {
         System.out.println(i + "!");
      }
   }

   @Override
   public List<Dependency> getDependencies()
   {
      List<Dependency> result = new ArrayList<Dependency>();

      result.add(builder.setGroupId("com.demo").setArtifactId("demo-library").setVersion("1.0.0").build());

      return result;
   }

   @Override
   public List<PackagingType> getCompatiblePackagingTypes()
   {
      return Arrays.asList(
               new PackagingType("jar"),
               new PackagingType("war"));
   }
}
