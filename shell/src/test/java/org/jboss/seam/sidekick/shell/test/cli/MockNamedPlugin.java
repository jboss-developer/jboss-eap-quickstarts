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
package org.jboss.seam.sidekick.shell.test.cli;

import javax.inject.Named;

import org.jboss.seam.sidekick.shell.plugins.plugins.Command;
import org.jboss.seam.sidekick.shell.plugins.plugins.DefaultCommand;
import org.jboss.seam.sidekick.shell.plugins.plugins.Option;
import org.jboss.seam.sidekick.shell.plugins.plugins.Plugin;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@Named("mnp")
public class MockNamedPlugin implements Plugin
{
   @Command(help = "A mock run command")
   public void run()
   {

   }

   @Command
   public void helpless()
   {

   }

   @DefaultCommand(help = "This is a mock default command")
   public void defaultCommand(@Option final String option)
   {

   }

   @Command
   public void normal(@Option(description = "THE OPTION") final String option)
   {

   }

   @Command("named")
   public void named(@Option(value = "named", defaultValue = "true") final String option)
   {

   }

   @Command
   public void multiOption(@Option("named") final String option,
            @Option(value = "foo") final boolean foo)
   {

   }

   public void notCommand()
   {

   }
}
