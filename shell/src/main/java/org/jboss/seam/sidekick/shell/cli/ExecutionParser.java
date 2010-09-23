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
package org.jboss.seam.sidekick.shell.cli;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.seam.sidekick.shell.PromptType;
import org.jboss.seam.sidekick.shell.Shell;
import org.jboss.seam.sidekick.shell.cli.parser.CommandParser;
import org.jboss.seam.sidekick.shell.cli.parser.CompositeCommandParser;
import org.jboss.seam.sidekick.shell.cli.parser.NamedBooleanOptionParser;
import org.jboss.seam.sidekick.shell.cli.parser.NamedValueOptionParser;
import org.jboss.seam.sidekick.shell.cli.parser.NamedValueVarargsOptionParser;
import org.jboss.seam.sidekick.shell.cli.parser.OrderedValueOptionParser;
import org.jboss.seam.sidekick.shell.cli.parser.OrderedValueVarargsOptionParser;
import org.jboss.seam.sidekick.shell.cli.parser.ParseErrorParser;
import org.jboss.seam.sidekick.shell.cli.parser.Tokenizer;
import org.jboss.seam.sidekick.shell.exceptions.PluginExecutionException;
import org.jboss.seam.sidekick.shell.util.ShellUtils;
import org.mvel2.util.ParseTools;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class ExecutionParser
{
   private final PluginRegistry registry;
   private final Instance<Execution> executionInstance;
   private final Tokenizer tokenizer;
   private final Shell shell;

   @Inject
   public ExecutionParser(PluginRegistry registry, Instance<Execution> executionInstance,
                          Tokenizer tokenizer, Shell shell)
   {
      this.registry = registry;
      this.executionInstance = executionInstance;
      this.tokenizer = tokenizer;
      this.shell = shell;
   }

   public Execution parse(final String line)
   {
      Queue<String> tokens = tokenizer.tokenize(line);

      Map<String, PluginMetadata> plugins = registry.getPlugins();
      Execution execution = executionInstance.get();
      execution.setOriginalStatement(line);
      CommandMetadata command = null;

      if (tokens.size() > 0)
      {
         String first = tokens.remove();
         PluginMetadata plugin = plugins.get(first);

         if (plugin != null)
         {
            if (tokens.size() > 0)
            {
               String second = tokens.peek();
               command = plugin.getCommand(second);
               if (command != null)
               {
                  tokens.remove();
               }
            }

            if (plugin.hasDefaultCommand())
            {
               command = plugin.getDefaultCommand();
            }

            if (command != null)
            {
               execution.setCommand(command);

               // parse parameters and set order / nulls for command invocation

               Object[] parameters = parseParameters(command, tokens);
               execution.setParameterArray(parameters);
            }
            else
            {
               throw new PluginExecutionException(plugin, "Missing command for plugin [" + plugin.getName()
                     + "], available commands: " + plugin.getCommands());
            }
         }
      }

      return execution;
   }

   private Object[] parseParameters(final CommandMetadata command, final Queue<String> tokens)
   {
      Map<OptionMetadata, Object> valueMap = new HashMap<OptionMetadata, Object>();

      CommandParser commandParser = new CompositeCommandParser(new NamedBooleanOptionParser(),
            new NamedValueOptionParser(), new NamedValueVarargsOptionParser(), new OrderedValueOptionParser(),
            new OrderedValueVarargsOptionParser(), new ParseErrorParser());

      commandParser.parse(command, valueMap, tokens);

      Object[] parameters = new Object[command.getOptions().size()];
      for (OptionMetadata option : command.getOptions())
      {
         Object value = valueMap.get(option);
         PromptType promptType = option.getPromptType();
         String optionDescriptor = ShellUtils.getOptionDescriptor(option) + ": ";
         if ((value != null) && !value.toString().matches(promptType.getPattern()))
         {
            // make sure the current option value is OK
            shell.println("Could not parse [" + value + "]... please try again...");
            value = shell.promptCommon(optionDescriptor, promptType);
         }
         else if (option.isRequired() && (value == null))
         {
            while (value == null)
            {
               if (isBooleanOption(option))
               {
                  value = shell.promptBoolean(optionDescriptor);
               }
               else if (!PromptType.ANY.equals(promptType))
               {
                  // make sure an omitted required option value is OK
                  value = shell.promptCommon(optionDescriptor, promptType);
               }
               else
               {
                  value = shell.prompt(optionDescriptor);
               }

               if (String.valueOf(value).trim().length() == 0)
               {
                  shell.println("The option is required to execute this command.");
                  value = null;
               }
            }
         }

         parameters[option.getIndex()] = value;
      }

      return parameters;
   }

   private static boolean isBooleanOption(OptionMetadata option)
   {
      return ParseTools.unboxPrimitive(option.getType()) == boolean.class;
   }
}
