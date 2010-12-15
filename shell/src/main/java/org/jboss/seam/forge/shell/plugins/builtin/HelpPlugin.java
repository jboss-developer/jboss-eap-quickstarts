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

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.ShellPrintWriter;
import org.jboss.seam.forge.shell.command.CommandMetadata;
import org.jboss.seam.forge.shell.command.OptionMetadata;
import org.jboss.seam.forge.shell.command.PluginMetadata;
import org.jboss.seam.forge.shell.command.PluginRegistry;
import org.jboss.seam.forge.shell.plugins.DefaultCommand;
import org.jboss.seam.forge.shell.plugins.Help;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.PipeOut;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.shell.plugins.Topic;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Named("help")
@Topic("Shell Environment")
@Help("Displays help text for specified plugins & commands.")
public class HelpPlugin implements Plugin
{
   PluginRegistry registry;
   private final Shell shell;

   @Inject
   public HelpPlugin(final PluginRegistry registry, final Shell shell)
   {
      this.registry = registry;
      this.shell = shell;
   }

   @DefaultCommand
   public void help(@Option final String[] tokens, PipeOut out)
   {
      if ((tokens == null) || (tokens.length == 0))
      {
         out.println("");
         out.println("Welcome to Seam Forge. Type \"help {plugin} {command}\" to learn more about what this shell can do.");
         out.println("");
      }
      else
      {
         String pluginName = tokens[0];
         PluginMetadata plugin = registry.getPluginMetadataForScopeAndConstraints(pluginName, shell);
         if (plugin != null)
         {
            writePluginHelp(plugin, out);

            if (tokens.length >= 2)
            {
               String commandName = tokens[1];
               if (plugin.hasCommand(commandName, shell))
               {
                  CommandMetadata command = plugin.getCommand(commandName, shell);
                  writeCommandHelp(command, out);
               }
               else
               {
                  out.println("Unknown command [" + commandName + "]");
               }
            }
            else if (tokens.length >= 1)
            {
               List<CommandMetadata> ctxCommands = plugin.getCommands(shell);
               if (ctxCommands.size() > 0)
               {
                  out.println("");
                  out.println("Commands:");
                  for (CommandMetadata command : ctxCommands)
                  {
                     writeCommandHelp(command, out);
                  }
               }
            }
         }
         else
         {
            out.println("I couldn't find a help topic for: " + tokens[0]);
         }
         out.println("");
      }

   }

   private void writePluginHelp(final PluginMetadata plugin, ShellPrintWriter out)
   {
      out.println("[" + plugin.getName() + "] " + plugin.getHelp());
   }

   private void writeCommandHelp(final CommandMetadata command, ShellPrintWriter out)
   {
      if (command.isDefault())
      {
         out.print("[default] " + command.getName() + " ");
         writeCommandUsage(command, out);
         out.println(" - " + command.getHelp());
      }
      else
      {
         out.print(command.getName() + " ");
         writeCommandUsage(command, out);
         out.println(" - " + command.getHelp());
      }
      out.println();
   }

   private void writeCommandUsage(final CommandMetadata command, ShellPrintWriter out)
   {
      for (OptionMetadata option : command.getOptions())
      {
         if (option.isRequired())
         {
            out.print("[");
         }
         else
         {
            out.print("{");
         }

         if (option.isBoolean())
         {
            out.print("--" + option.getName());
         }
         else if (option.isNamed())
         {
            out.print("--" + option.getName() + "=...");
         }
         else if (option.isVarargs())
         {
            out.print(option.getDescription() + " ...");
         }
         else
         {
            out.print(option.getDescription());
         }

         if (option.isRequired())
         {
            out.print("]");
         }
         else
         {
            out.print("}");
         }
      }
   }
}
