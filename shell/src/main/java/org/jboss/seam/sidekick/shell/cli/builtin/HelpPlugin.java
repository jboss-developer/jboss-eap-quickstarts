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
import org.jboss.seam.sidekick.shell.cli.CommandMetadata;
import org.jboss.seam.sidekick.shell.cli.OptionMetadata;
import org.jboss.seam.sidekick.shell.cli.PluginMetadata;
import org.jboss.seam.sidekick.shell.cli.PluginRegistry;
import org.jboss.seam.sidekick.shell.plugins.plugins.DefaultCommand;
import org.jboss.seam.sidekick.shell.plugins.plugins.Help;
import org.jboss.seam.sidekick.shell.plugins.plugins.Option;
import org.jboss.seam.sidekick.shell.plugins.plugins.Plugin;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Named("help")
@Help("Displays help text for specified plugins & commands.")
public class HelpPlugin implements Plugin
{
   @Inject
   PluginRegistry registry;

   @Inject
   Shell shell;

   @DefaultCommand
   public void help(@Option final String... tokens)
   {
      if ((tokens == null) || (tokens.length == 0))
      {
         shell.println("");
         shell.println("Welcome to Seam Sidekick! Type \"help {plugin} {command}\" to learn more about what this shell can do.");
         shell.println("");
      }
      else
      {
         String pluginName = tokens[0];
         PluginMetadata plugin = registry.getPlugins().get(pluginName);
         if (plugin != null)
         {
            writePluginHelp(plugin);

            if (tokens.length >= 2)
            {
               String commandName = tokens[1];
               if (plugin.hasCommand(commandName))
               {
                  CommandMetadata command = plugin.getCommand(commandName);
                  writeCommandHelp(command);
               }
               else
               {
                  shell.println("Unknown command [" + commandName + "]");
               }
            }
            else if (tokens.length >= 1)
            {
               if (plugin.getCommands().size() > 0)
               {
                  shell.println("");
                  shell.println("Commands:");
                  for (CommandMetadata command : plugin.getCommands())
                  {
                     writeCommandHelp(command);
                  }
               }
            }
         }
         else
         {
            shell.println("I couldn't find a help topic for: " + tokens[0]);
         }
         shell.println("");
      }

   }

   private void writePluginHelp(final PluginMetadata plugin)
   {
      shell.println("[" + plugin.getName() + "] " + plugin.getHelp());
   }

   private void writeCommandHelp(final CommandMetadata command)
   {
      if (command.isDefault())
      {
         shell.print("[default] " + command.getName() + " ");
         writeCommandUsage(command);
         shell.println(" - " + command.getHelp());
      }
      else
      {
         shell.print(command.getName() + " ");
         writeCommandUsage(command);
         shell.println(" - " + command.getHelp());
      }
      shell.println();
   }

   private void writeCommandUsage(final CommandMetadata command)
   {
      for (OptionMetadata option : command.getOptions())
      {
         if (option.isRequired())
         {
            shell.print("[");
         }
         else
         {
            shell.print("{");
         }

         if (option.isBoolean())
         {
            shell.print("--" + option.getName());
         }
         else if (option.isNamed())
         {
            shell.print("--" + option.getName() + "=[...]");
         }
         else if (option.isVarargs())
         {
            shell.print("value value ... values");
         }
         else
         {
            shell.print("value");
         }

         if (option.isRequired())
         {
            shell.print("]");
         }
         else
         {
            shell.print("}");
         }
      }
   }
}
