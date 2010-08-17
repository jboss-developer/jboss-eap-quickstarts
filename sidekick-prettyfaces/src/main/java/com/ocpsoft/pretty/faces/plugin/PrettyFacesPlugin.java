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

package com.ocpsoft.pretty.faces.plugin;

import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.sidekick.project.model.MavenProject;
import org.jboss.seam.sidekick.shell.Shell;
import org.jboss.seam.sidekick.shell.plugins.plugins.DefaultCommand;
import org.jboss.seam.sidekick.shell.plugins.plugins.Help;
import org.jboss.seam.sidekick.shell.plugins.plugins.Plugin;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@Named("prettyfaces")
@Help("Manages the OcpSoft PrettyFaces Plugin and Configuration")
public class PrettyFacesPlugin implements Plugin
{

   @Inject
   private Shell shell;

   @Inject
   private MavenProject project;

   @DefaultCommand(help = "displays the status of the plugin")
   public void status()
   {
      if (project.hasDependency("com.ocpsoft", "prettyfaces-jsf(1?2)"))
      {
         shell.write("Status: INSTALLED");
      }
      else
      {
         shell.write("Status: NOT-INSTALLED");
      }
   }

}
