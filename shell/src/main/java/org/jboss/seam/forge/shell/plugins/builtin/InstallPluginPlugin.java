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

package org.jboss.seam.forge.shell.plugins.builtin;

import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.events.ReinitializeEnvironment;
import org.jboss.seam.forge.shell.plugins.DefaultCommand;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.PipeOut;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.shell.util.PluginRef;
import org.jboss.seam.forge.shell.util.PluginUtil;
import org.jboss.seam.forge.shell.util.ShellColor;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;
import java.util.List;

/**
 * @author Mike Brock .
 */
@Named("install-plugin")
@Singleton
public class InstallPluginPlugin implements Plugin
{
   private Shell shell;
   private Event<ReinitializeEnvironment> reinitializeEvent;

   @Inject
   public InstallPluginPlugin(Shell shell, Event<ReinitializeEnvironment> reinitializeEvent)
   {
      this.shell = shell;
      this.reinitializeEvent = reinitializeEvent;
   }

   @DefaultCommand
   public void install(@Option(description = "plugin-name") String pluginName, final PipeOut out) throws Exception
   {
      String defaultRepo = (String) shell.getProperty("DEFFAULT_PLUGIN_REPO");
      String pluginPath = shell.getProperty("FORGE_CONFIG_DIR") + "plugins/";

      if (defaultRepo == null)
      {
         out.println("no default repository set: (to set, type: set DEFFAULT_PLUGIN_REPO <repository>)");
         return;
      }

      List<PluginRef> plugins = PluginUtil.findPlugin(defaultRepo, pluginName, out);

      if (plugins.isEmpty())
      {
         out.println("no plugin found: " + pluginName);
      }
      else if (plugins.size() > 1)
      {
         out.println("ambiguous plugin query: multiple matches.");
      }
      else
      {
         PluginRef ref = plugins.get(0);
         out.println(ShellColor.BOLD, "*** Preparing to install plugin: " + ref.getName());
         File file = PluginUtil.downloadPlugin(ref, out, pluginPath);
         if (file == null)
         {
            out.println(ShellColor.RED, "*** Could not install plugin: " + ref.getName());
            return;
         }
         else {
            PluginUtil.loadPluginJar(file);
            out.println("*** Reinitializing and installing pluggin (Forge will now restart)");

            reinitializeEvent.fire(new ReinitializeEnvironment());
         }
      }
   }

}
