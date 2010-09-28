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
package org.jboss.seam.sidekick.shell.completer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.jboss.seam.sidekick.shell.Shell;
import org.jboss.seam.sidekick.shell.command.CommandMetadata;
import org.jboss.seam.sidekick.shell.command.OptionMetadata;
import org.jboss.seam.sidekick.shell.command.PluginMetadata;
import org.jboss.seam.sidekick.shell.command.PluginRegistry;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class PluginCommandCompleter implements CommandCompleter
{
   private static final String PLUGIN_CHARS = "[a-zA-Z0-9-_]";

   @Inject
   private PluginRegistry registry;

   @Inject
   private Shell shell;

   @Override
   public int complete(final String buffer, final int cursor, final List<CharSequence> candidates)
   {
      // TODO this needs to work for default commands
      List<PluginMetadata> plugins = getPluginCandidates(buffer);
      for (PluginMetadata pluginMetadata : plugins)
      {
         candidates.add(convertPlugin(pluginMetadata));
      }

      if (plugins.isEmpty())
      {
         List<CommandMetadata> commands = getCommandCandidates(buffer);
         for (CommandMetadata commandMetadata : commands)
         {
            candidates.add(convertCommand(commandMetadata));
         }

         if (commands.isEmpty())
         {
            List<OptionMetadata> options = getOptionCandidates(buffer);
            for (OptionMetadata optionMetadata : options)
            {
               candidates.add(convertOption(optionMetadata));
            }
         }
      }

      return 0;
   }

   private List<PluginMetadata> getPluginCandidates(final String buffer)
   {
      Map<String, PluginMetadata> plugins = registry.getPlugins();
      List<PluginMetadata> results = new ArrayList<PluginMetadata>();

      Matcher partialMatcher = Pattern.compile("^\\s*(" + PLUGIN_CHARS + "+)$").matcher(buffer);

      if (partialMatcher.find())
      {
         String pluginToken = partialMatcher.group(1);

         for (Entry<String, PluginMetadata> p : plugins.entrySet())
         {
            PluginMetadata pluginMeta = p.getValue();
            if (pluginMeta.hasCommands())
            {
               String pluginName = pluginMeta.getName();
               if (isPotentialMatch(pluginName, pluginToken))
               {
                  results.add(pluginMeta);
               }
            }
         }
      }

      return results;
   }

   private List<CommandMetadata> getCommandCandidates(final String buffer)
   {
      List<CommandMetadata> results = new ArrayList<CommandMetadata>();

      Matcher matcher = Pattern.compile("^\\s*(" + PLUGIN_CHARS + "+)\\s+(.*)$").matcher(buffer);
      Queue<String> tokens = new LinkedList<String>();
      tokens.addAll(Arrays.asList(buffer.split("\\s+")));
      if (!tokens.isEmpty())
      {
         String plugin = tokens.remove();
         PluginMetadata pluginMeta = registry.getPluginMetadata(plugin);

         if (pluginMeta != null)
         {
            if (buffer.contains(pluginMeta.getName()))
            {
               if (tokens.isEmpty())
               {
                  for (CommandMetadata commandMeta : pluginMeta.getCommands())
                  {
                     if (!commandMeta.isDefault())
                     {
                        results.add(commandMeta);
                     }
                  }
               }
               else if (tokens.size() == 1)
               {
                  String command = tokens.remove();
                  for (CommandMetadata commandMeta : pluginMeta.getCommands())
                  {
                     if (commandMeta.getName().startsWith(command))
                     {
                        results.add(commandMeta);
                     }
                  }
               }
            }
         }
      }
      return results;
   }

   private List<OptionMetadata> getOptionCandidates(final String buffer)
   {
      List<OptionMetadata> results = new ArrayList<OptionMetadata>();

      Queue<String> tokens = new LinkedList<String>();
      tokens.addAll(Arrays.asList(buffer.split("\\s+")));
      if (!tokens.isEmpty())
      {
         String plugin = tokens.remove();
         List<PluginMetadata> pluginCandidates = getPluginCandidates(plugin);
         if (pluginCandidates.size() == 1)
         {
            PluginMetadata pluginMetadata = pluginCandidates.get(0);

            if (!tokens.isEmpty())
            {
               String command = tokens.remove();
               List<CommandMetadata> commandCandidates = getCommandCandidates(plugin + " " + command);
               if (commandCandidates.size() == 1)
               {
                  CommandMetadata commandMetadata = commandCandidates.get(0);

                  if (tokens.isEmpty())
                  {
                     // TODO we have a selected plugin and command, pick something random and required?
                     for (OptionMetadata optionMetadata : commandMetadata.getOptions())
                     {
                        if (optionMetadata.isNamed())
                        {
                           results.add(optionMetadata);
                        }
                     }
                  }
                  else if (tokens.size() == 1)
                  {
                     String option = tokens.remove();
                     for (OptionMetadata optionMetadata : commandMetadata.getOptions())
                     {
                        if (optionMetadata.isNamed() && optionMetadata.getName().startsWith(option))
                        {
                           results.add(optionMetadata);
                        }
                     }
                  }
               }
            }
         }
      }
      return results;
   }

   private CharSequence convertOption(final OptionMetadata optionMetadata)
   {
      String result = convertCommand(optionMetadata.getParent());

      if (optionMetadata.isNamed())
      {
         result += "--" + optionMetadata.getName() + " ";
      }

      return result;
   }

   private String convertCommand(final CommandMetadata commandMetadata)
   {
      return convertPlugin(commandMetadata.getPluginMetadata()) + commandMetadata.getName() + " ";
   }

   private String convertPlugin(final PluginMetadata pluginMetadata)
   {
      return pluginMetadata.getName() + " ";
   }

   private boolean isPotentialMatch(final String name, final String token)
   {
      return name.matches("(?i)" + token + ".*");
   }
}
