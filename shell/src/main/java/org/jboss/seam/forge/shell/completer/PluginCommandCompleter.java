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

package org.jboss.seam.forge.shell.completer;

import java.io.File;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Singleton;

import jline.console.completer.StringsCompleter;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.ResourceFlag;
import org.jboss.seam.forge.project.services.ResourceFactory;
import org.jboss.seam.forge.project.util.PathspecParser;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.command.CommandMetadata;
import org.jboss.seam.forge.shell.command.OptionMetadata;
import org.jboss.seam.forge.shell.command.PluginMetadata;
import org.jboss.seam.forge.shell.command.PluginRegistry;
import org.jboss.seam.forge.shell.command.parser.CommandParser;
import org.jboss.seam.forge.shell.command.parser.CompositeCommandParser;
import org.jboss.seam.forge.shell.command.parser.NamedBooleanOptionParser;
import org.jboss.seam.forge.shell.command.parser.NamedValueOptionParser;
import org.jboss.seam.forge.shell.command.parser.NamedValueVarargsOptionParser;
import org.jboss.seam.forge.shell.command.parser.OrderedValueOptionParser;
import org.jboss.seam.forge.shell.command.parser.OrderedValueVarargsOptionParser;
import org.jboss.seam.forge.shell.command.parser.Tokenizer;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Singleton
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

   @Inject
   private ResourceFactory resourceFactory;

   private String currentBuffer = "";
   private String lastBuffer = "";
   private boolean finalTokenComplete = false;
   private int index = -1;

   @Override
   public int complete(final String buffer, final int cursor, final List<CharSequence> candidates)
   {
      currentBuffer = buffer;
      Queue<String> tokens = new Tokenizer().tokenize(buffer);
      finalTokenComplete = buffer.matches("^.*\\s+$");
      if (!tokens.isEmpty())
      {
         String pluginName = tokens.remove();
         PluginMetadata plugin = registry.getPluginMetadataForScopeAndConstraints(pluginName, shell);

         if ((plugin != null))
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
                  if (plugin.hasCommand(peek, shell))
                  {
                     // TODO this should probably be tokenComplete`?` sensitive
                     // complete the command, remove the last token
                     String command = tokens.remove();
                     List<String> optionCandidates = getOptionCandidates(plugin.getCommand(command, shell), tokens);
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
                  if (plugin.hasCommand(peek, shell))
                  {
                     // complete the command, remove the last token
                     String command = tokens.remove();
                     List<String> optionCandidates = getOptionCandidates(plugin.getCommand(command, shell), tokens);
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
         index = 0;
         List<String> pluginCandidates = getPluginCandidates(registry, "");
         candidates.addAll(pluginCandidates);
      }

      lastBuffer = buffer;
      return index;
   }

   /**
    * Add plugin completions for the given word
    */
   private List<String> getPluginCandidates(final PluginRegistry registry, final String pluginBase)
   {
      Map<String, List<PluginMetadata>> plugins = registry.getPlugins();

      List<String> results = new ArrayList<String>();
      for (Entry<String, List<PluginMetadata>> entry : plugins.entrySet())
      {
         for (PluginMetadata pluginMeta : entry.getValue())
         {
            if (pluginMeta.hasCommands())
            {
               String pluginName = pluginMeta.getName();
               if (isPotentialMatch(pluginName, pluginBase) && pluginMeta.constrantsSatisfied(shell))
               {
                  results.add(pluginName + " ");
               }
            }
         }
      }

      return results;
   }

   /**
    * Add command completions for the given plugin, with or without tokens
    */
   private List<String> getCommandCandidates(final PluginMetadata plugin, final Queue<String> tokens)
   {
      List<String> results = new ArrayList<String>();
      if (plugin.hasCommands())
      {
         List<CommandMetadata> commands = plugin.getCommands(shell);
         if (tokens.isEmpty())
         {
            for (CommandMetadata command : commands)
            {
               if (command.isDefault())
               {
                  if ((commands.size() == 1) || currentBuffer.matches(lastBuffer + "\\s+"))
                  {
                     List<String> optionCandidates = getOptionCandidates(command, tokens);
                     results.addAll(optionCandidates);
                  }
                  else
                  {
                     results.add("");
                  }
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

   /**
    * Add option completions for the given command, with or without argument tokens
    */
   private List<String> getOptionCandidates(final CommandMetadata command, final Queue<String> tokens)
   {
      ArrayList<String> results = new ArrayList<String>();
      Map<OptionMetadata, Object> valueMap = commandParser.parse(command, tokens);
      List<OptionMetadata> options = command.getOptions();

      // TODO determine which option came last, if it had a value, if the value
      // can be hinted

      // suggest when we have a named param and no value
      for (OptionMetadata option : options)
      {
         if (tokens.size() == 1)
         {
            String potentialOption = tokens.peek();
            if (potentialOption.startsWith("--"))
            {
               potentialOption = potentialOption.substring(2);
            }

            if (option.getName().startsWith(potentialOption))
            {
               results.add("--" + option + " ");
               index = index - tokens.peek().length();
            }
         }
         else
         {
            if (valueMap.isEmpty() || valueMap.containsKey(option))
            {
               if (isResourceAssignable(option))
               {
                  String[] values;

                  if (valueMap.isEmpty())
                  {
                     values = new String[]{""};
                  }
                  else if (valueMap.get(option) instanceof String[])
                  {
                     values = (String[]) valueMap.get(option);
                  }
                  else
                  {
                     values = new String[]{String.valueOf(valueMap.get(option))};
                  }

                  String val = values[values.length - 1];

                  for (Resource<?> r :
                        new PathspecParser(resourceFactory, shell.getCurrentResource(), val + "*").parse())
                  {
                     // Add result to the results list, and append a '/' if the resource has children.
                     results.add(r.getName() + (r.isFlagSet(ResourceFlag.Node) ? "/" : ""));
                  }

                  int lastNest = val.lastIndexOf(File.separatorChar);

                  // Record the current index point in the buffer. If we're at the separator char
                  // set the value ahead by 1.
                  index = index - val.length() + (lastNest != -1 ? lastNest + 1 : 0);
               }
            }

            if (!valueMap.containsKey(option) && option.isRequired())
            {
               if (option.isNamed())
               {
                  String prefix = "";
                  if (!finalTokenComplete)
                  {
                     prefix = " ";
                  }
                  results.add(prefix + "--" + option.getName() + " ");
               }
               else
               {
                  results.add("");
               }
               break;
            }

            if (!option.isRequired() && currentBuffer.equals(lastBuffer))
            {
               if (option.isNamed())
               {
                  String prefix = "";
                  if (!finalTokenComplete)
                  {
                     prefix = " ";
                  }
                  results.add(prefix + "--" + option.getName() + " ");
               }
               else
               {
                  results.add("");
               }
               break;
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
   private boolean couldBeCommand(final PluginMetadata plugin, final String potentialCommand)
   {
      List<CommandMetadata> commands = plugin.getCommands(shell);
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

   private boolean isResourceAssignable(OptionMetadata option)
   {
      return Resource[].class.isAssignableFrom(option.getBoxedType()) || Resource.class.isAssignableFrom(option.getBoxedType());
   }
}
