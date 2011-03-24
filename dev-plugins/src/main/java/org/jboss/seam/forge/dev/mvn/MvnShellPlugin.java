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

import java.io.IOException;

import javax.inject.Inject;

import org.jboss.seam.forge.project.facets.MavenCoreFacet;
import org.jboss.seam.forge.resources.DirectoryResource;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.Alias;
import org.jboss.seam.forge.shell.plugins.DefaultCommand;
import org.jboss.seam.forge.shell.plugins.PipeOut;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.shell.plugins.RequiresFacet;
import org.jboss.seam.forge.shell.plugins.RequiresProject;
import org.jboss.seam.forge.shell.plugins.RequiresResource;
import org.jboss.seam.forge.shell.plugins.Topic;
import org.jboss.seam.forge.shell.util.NativeSystemCall;

/**
 * @author Mike Brock .
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Alias("mvn")
@Topic("Project")
@RequiresProject
@RequiresFacet(MavenCoreFacet.class)
@RequiresResource(DirectoryResource.class)
public class MvnShellPlugin implements Plugin
{
   private final Shell shell;

   @Inject
   public MvnShellPlugin(final Shell shell)
   {
      this.shell = shell;
   }

   @DefaultCommand
   public void run(final PipeOut out, final String... parms) throws IOException
   {
      if (shell.getCurrentProject() != null)
      {
         NativeSystemCall.execFromPath("mvn", parms, out, shell.getCurrentProject().getProjectRoot());
      }
      else
      {
         NativeSystemCall.execFromPath("mvn", parms, out, shell.getCurrentDirectory());
      }
   }
}
