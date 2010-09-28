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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import javax.inject.Inject;

import org.jboss.seam.sidekick.shell.Shell;
import org.jboss.seam.sidekick.shell.command.CommandMetadata;
import org.jboss.seam.sidekick.shell.command.OptionMetadata;
import org.jboss.seam.sidekick.shell.command.PluginMetadata;
import org.jboss.seam.sidekick.shell.command.PluginRegistry;
import org.jboss.seam.sidekick.shell.command.parser.CommandParser;
import org.jboss.seam.sidekick.shell.command.parser.CompositeCommandParser;
import org.jboss.seam.sidekick.shell.command.parser.NamedBooleanOptionParser;
import org.jboss.seam.sidekick.shell.command.parser.NamedValueOptionParser;
import org.jboss.seam.sidekick.shell.command.parser.NamedValueVarargsOptionParser;
import org.jboss.seam.sidekick.shell.command.parser.OrderedValueOptionParser;
import org.jboss.seam.sidekick.shell.command.parser.OrderedValueVarargsOptionParser;
import org.jboss.seam.sidekick.shell.command.parser.Tokenizer;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class PluginCommandCompleter implements CommandCompleter
{
   private final CommandParser commandParser = new CompositeCommandParser(
         new NamedBooleanOptionParser(),
         new NamedValueOptionParser(),
         new NamedValueVarargsOptionParser(),
         new OrderedValueOptionParser(),
         new OrderedValueVarargsOptionParser());

   @Inject
   private PluginRegistry registry;

   @Inject
   private Shell shell;

   @Override
   public int complete(final String buffer, final int cursor, final List<CharSequence> candidates)
   {
      int index = -1;
      Queue<String> tokens = new Tokenizer().tokenize(buffer);
      boolean finalTokenComplete = buffer.matches("^.*\\s+$");
      if (!tokens.isEmpty())
      {
         String pluginName = tokens.remove();
         PluginMetadata plugin = registry.getPluginMetadata(pluginName);

         if (plugin != null)
         {
            // found plugin
            if (tokens.isEmpty())
            {
               // there's only a plugin so far
               if (finalTokenComplete)
               {
                  // they chose this command, start at the end for command
                  // completion
                  index = buffer.length();
                  if (plugin.hasCommands())
                  {
                     List<String> commandCandidates = getCommandCandidates(plugin, tokens);
                     candidates.addAll(commandCandidates);
                  }
                  else
                  {
                     // this plugin has no commands -- bad input
                  }
               }
               else
               {
                  // they haven't yet chosen a plugin, start at the beginning
                  index = 0;
                  List<String> pluginCandidates = getPluginCandidates(registry, pluginName);
                  candidates.addAll(pluginCandidates);
               }
            }
            else
            {
               // we have a plugin, there are options or a command to be parsed
               index = buffer.length();
               if (tokens.size() > 1)
               {
                  // there must be a command, or a string of arguments for the
                  // default command
                  String peek = tokens.peek();
                  if (plugin.hasCommand(peek))
                  {
                     // TODO this should probably be tokenComplete`?` sensitive
                     // complete the command, remove the last token
                     String command = tokens.remove();
                     List<String> optionCandidates = getOptionCandidates(plugin.getCommand(command), tokens);
                     candidates.addAll(optionCandidates);
                  }
                  else if (plugin.hasDefaultCommand())
                  {
                     CommandMetadata defaultCommand = plugin.getDefaultCommand();
                     if (defaultCommand.hasOptions())
                     {
                        List<String> optionCandidates = getOptionCandidates(defaultCommand, tokens);
                        candidates.addAll(optionCandidates);
                     }
                     else
                     {
                        // bad input, not a command and default command does not
                        // take options
                        // TODO remove the bad input and complete to a shorter
                        // string??? :)
                     }
                  }
                  else
                  {
                     // bad input, not a command and there is no default command
                  }
               }
               else
               {
                  // just one more token, it's either a command or an argument
                  // for the default command
                  String peek = tokens.peek();
                  if (plugin.hasCommand(peek))
                  {
                     // complete the command, remove the last token
                     String command = tokens.remove();
                     List<String> optionCandidates = getOptionCandidates(plugin.getCommand(command), tokens);
                     candidates.addAll(optionCandidates);
                  }
                  else if (couldBeCommand(plugin, peek))
                  {
                     index = buffer.indexOf(peek);
                     List<String> commandCandidates = getCommandCandidates(plugin, tokens);
                     candidates.addAll(commandCandidates);
                  }
                  else if (plugin.hasDefaultCommand())
                  {
                     CommandMetadata defaultCommand = plugin.getDefaultCommand();
                     if (defaultCommand.hasOptions())
                     {
                        List<String> optionCandidates = getOptionCandidates(defaultCommand, tokens);
                        candidates.addAll(optionCandidates);
                     }
                     else
                     {
                        // bad input, not a command and default command does not
                        // take options
                     }
                  }
               }
            }
         }
         else
         {
            // no plugin found
            index = 0;
            if (tokens.isEmpty())
            {
               List<String> pluginCandidates = getPluginCandidates(registry, pluginName);
               candidates.addAll(pluginCandidates);
               // TODO add file completion candidates
            }
            else
            {
               // bad input, must always begin with a plugin
               // try to add file completion
            }
         }
      }
      else
      {
         List<String> pluginCandidates = getPluginCandidates(registry, "");
         candidates.addAll(pluginCandidates);
      }

      // System.out.println();
      // System.out.println("Candidates " + candidates + ", index=" + index +
      // ", bufferSize=" + buffer.length() + ", cursor=" + cursor);

      return index;
   }

   /**
    * Add option completions for the given command, with or without argument
    * tokens
    */
   private List<String> getOptionCandidates(CommandMetadata command, Queue<String> tokens)
   {
      ArrayList<String> results = new ArrayList<String>();

      Map<OptionMetadata, Object> valueMap = commandParser.parse(command, tokens);

      List<OptionMetadata> options = command.getOptions();
      for (OptionMetadata option : options)
      {
         if (option.isNamed() && !valueMap.containsKey(option))
         {
            results.add("--" + option.getName() + " ");
         }
      }

      return results;
   }

   /**
    * Add plugin completions for the given word
    */
   private List<String> getPluginCandidates(PluginRegistry registry, String pluginBase)
   {
      Map<String, PluginMetadata> plugins = registry.getPlugins();

      List<String> results = new ArrayList<String>();
      for (Entry<String, PluginMetadata> p : plugins.entrySet())
      {
         PluginMetadata pluginMeta = p.getValue();
         if (pluginMeta.hasCommands())
         {
            String pluginName = pluginMeta.getName();
            if (isPotentialMatch(pluginName, pluginBase))
            {
               results.add(pluginName + " ");
            }
         }
      }

      return results;
   }

   /**
    * Add command completions for the given plugin, with or without tokens
    */
   private List<String> getCommandCandidates(PluginMetadata plugin, Queue<String> tokens)
   {
      List<String> results = new ArrayList<String>();
      if (plugin.hasCommands())
      {
         List<CommandMetadata> commands = plugin.getCommands();
         if (tokens.isEmpty())
         {
            for (CommandMetadata command : commands)
            {
               if (command.isDefault())
               {
                  results.add("");
               }
               else
               {
                  results.add(command.getName() + " ");
               }
            }
         }
         else
         {
            String pluginBase = tokens.remove();
            for (CommandMetadata command : commands)
            {
               if (!command.isDefault() && isPotentialMatch(command.getName(), pluginBase))
               {
                  results.add(command.getName() + " ");
               }
            }
         }
      }
      return results;
   }

   private boolean isPotentialMatch(final String full, final String partial)
   {
      return full.matches("(?i)" + partial + ".*");
   }

   /**
    * Return true if the given string could begin command, return false if not.
    */
   private boolean couldBeCommand(PluginMetadata plugin, String potentialCommand)
   {
      List<CommandMetadata> commands = plugin.getCommands();
      if ((commands != null) && !commands.isEmpty())
      {
         for (CommandMetadata commandMetadata : commands)
         {
            if (!commandMetadata.isDefault() && isPotentialMatch(commandMetadata.getName(), potentialCommand))
            {
               return true;
            }
         }
      }
      return false;
   }
}
