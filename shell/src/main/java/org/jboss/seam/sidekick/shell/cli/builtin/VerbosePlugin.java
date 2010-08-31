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
package org.jboss.seam.sidekick.shell.cli.builtin;

import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.sidekick.shell.Shell;
import org.jboss.seam.sidekick.shell.plugins.plugins.DefaultCommand;
import org.jboss.seam.sidekick.shell.plugins.plugins.Help;
import org.jboss.seam.sidekick.shell.plugins.plugins.Option;
import org.jboss.seam.sidekick.shell.plugins.plugins.Plugin;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Named("verbose")
@Help("Display or toggle the current verbose mode.")
public class VerbosePlugin implements Plugin
{
   @Inject
   Shell shell;

   @DefaultCommand
   public void execute(@Option(required = false, help = "Toggle verbose mode [on/off]") final String verbose)
   {
      if ("on".equalsIgnoreCase(verbose))
      {
         shell.setVerbose(true);
      }
      else if ("off".equalsIgnoreCase(verbose))
      {
         shell.setVerbose(false);
      }

      if (shell.isVerbose())
      {
         shell.println("Shell IS running in verbose mode.");
      }
      else
      {
         shell.println("Shell is NOT running in verbose mode.");
      }
   }
}
