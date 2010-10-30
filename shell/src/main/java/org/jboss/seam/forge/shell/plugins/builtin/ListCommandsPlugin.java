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

import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.command.CommandMetadata;
import org.jboss.seam.forge.shell.command.PluginMetadata;
import org.jboss.seam.forge.shell.command.PluginRegistry;
import org.jboss.seam.forge.shell.plugins.DefaultCommand;
import org.jboss.seam.forge.shell.plugins.Help;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.shell.util.GeneralUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mike Brock
 */
@Named("list-commands")
@Help("Lists executable shell commands")
public class ListCommandsPlugin implements Plugin
{
   final PluginRegistry registry;
   final Shell shell;

   @Inject
   public ListCommandsPlugin(final PluginRegistry registry, final Shell shell)
   {
      this.registry = registry;
      this.shell = shell;
   }

   @DefaultCommand
   public void listCommands(@Option(name = "all", shortName = "a", flagOnly = true) boolean showAll)
   {
      List<String> listData = new ArrayList<String>();
      Class currResource = shell.getCurrentResource().getClass();
      for (PluginMetadata pluginMetaData : registry.getPlugins().values())
      {
         for (CommandMetadata commandMetadata : pluginMetaData.getCommands())
         {
            render(listData, showAll, currResource, commandMetadata);
         }
      }

      GeneralUtils.printOutColumns(listData, shell, true);
   }

   private static void render(List<String> listData, boolean showAll, Class currResource, CommandMetadata cmdMeta)
   {
      boolean contextual = cmdMeta.usableWithScope(currResource);

      if (showAll)
      {
         if (!cmdMeta.isDefault())
         {
            listData.add((contextual ? "*" : " ") + cmdMeta.getPluginMetadata().getName() + ":" + cmdMeta.getName());
         }
         else
         {
            listData.add((contextual ? "*" : " ") + cmdMeta.getName());
         }
      }
      else if (contextual)
      {
         listData.add(cmdMeta.getName());
      }
   }
}
