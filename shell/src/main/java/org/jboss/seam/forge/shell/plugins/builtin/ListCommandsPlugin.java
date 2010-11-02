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
import org.jboss.seam.forge.shell.plugins.*;
import org.jboss.seam.forge.shell.util.GeneralUtils;
import org.jboss.seam.forge.shell.util.ShellColor;

import java.util.*;

import static org.jboss.seam.forge.shell.util.GeneralUtils.printOutColumns;

/**
 * @author Mike Brock
 */
@Named("list-commands")
@Topic("Shell Environment")
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
      List<String> listData;
      Map<String, List<String>> listGroups = new TreeMap<String, List<String>>();

      Class currResource = shell.getCurrentResource().getClass();

      for (List<PluginMetadata> lpm : registry.getPlugins().values())
      {
         for (PluginMetadata pluginMetadata : lpm)
         {
            if (!listGroups.containsKey(pluginMetadata.getTopic()))
            {
               listGroups.put(pluginMetadata.getTopic(), listData = new ArrayList<String>());
            }
            else
            {
               listData = listGroups.get(pluginMetadata.getTopic());
            }

            for (CommandMetadata commandMetadata : pluginMetadata.getAllCommands())
            {
               String name = render(showAll, currResource, commandMetadata);

               /**
                * Deal with overloaded plugins.
                */
               if (name.endsWith("*"))
               {
                  listData.remove(name.substring(0, name.length() - 1));
               }
               listData.remove(name);

               if (!"".equals(name)) listData.add(name);
            }

            if (!listGroups.containsKey(pluginMetadata.getTopic()))
            {
               listGroups.put(pluginMetadata.getTopic(), listData);
            }

         }
      }

      GeneralUtils.OutputAttributes attr = null;
      for (Map.Entry<String, List<String>> entry : listGroups.entrySet())
      {
         attr = GeneralUtils.calculateOutputAttributs(entry.getValue(), shell, attr);
      }

      for (Map.Entry<String, List<String>> entry : listGroups.entrySet())
      {
         shell.println();
         shell.println(ShellColor.BLUE, "[" + entry.getKey().toUpperCase() + "]");

         printOutColumns(entry.getValue(), null, ShellColor.BOLD, shell, attr, true);
      }

      shell.println();

      if (showAll) shell.println("(* = command accessible from current context)");
      else shell.println("(only commands in relevant scope displayed. use --all to see all commands.)");
   }

   private static String render(boolean showAll, Class currResource,
                                CommandMetadata cmdMeta)
   {
      boolean contextual = cmdMeta.usableWithScope(currResource);

      if (showAll)
      {
         if (!cmdMeta.isDefault())
         {
            return (cmdMeta.getPluginMetadata().getName() + ":" + cmdMeta.getName()
                  + (contextual ? "*" : ""));
         }
         else
         {
            return (cmdMeta.getName()
                  + (contextual ? "*" : ""));
         }
      }
      else if (contextual)
      {
         return cmdMeta.getName();
      }

      return "";
   }
}
