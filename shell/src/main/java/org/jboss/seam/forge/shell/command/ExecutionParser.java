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

package org.jboss.seam.forge.shell.command;

import java.io.File;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.seam.forge.shell.PromptType;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.command.parser.CommandParser;
import org.jboss.seam.forge.shell.command.parser.CompositeCommandParser;
import org.jboss.seam.forge.shell.command.parser.NamedBooleanOptionParser;
import org.jboss.seam.forge.shell.command.parser.NamedValueOptionParser;
import org.jboss.seam.forge.shell.command.parser.NamedValueVarargsOptionParser;
import org.jboss.seam.forge.shell.command.parser.OrderedValueOptionParser;
import org.jboss.seam.forge.shell.command.parser.OrderedValueVarargsOptionParser;
import org.jboss.seam.forge.shell.command.parser.ParseErrorParser;
import org.jboss.seam.forge.shell.command.parser.Tokenizer;
import org.jboss.seam.forge.shell.exceptions.PluginExecutionException;
import org.jboss.seam.forge.shell.util.GeneralUtils;
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
   private final PromptTypeConverter promptTypeConverter;

   @Inject
   public ExecutionParser(final PluginRegistry registry, final Instance<Execution> executionInstance,
                          final Tokenizer tokenizer, final Shell shell,
                          final PromptTypeConverter promptTypeConverter)
   {
      this.registry = registry;
      this.executionInstance = executionInstance;
      this.tokenizer = tokenizer;
      this.shell = shell;
      this.promptTypeConverter = promptTypeConverter;
   }

   public Execution parse(final String line)
   {
      return parse(tokenizer.tokenize(line));

   }

   public Execution parse(final Queue<String> tokens)
   {
      Execution execution = executionInstance.get();
     // execution.setOriginalStatement(line);
      CommandMetadata command = null;

      if (!tokens.isEmpty())
      {
         String first = tokens.remove();
         execution.setOriginalStatement(first);
         PluginMetadata plugin = registry.getPluginMetadataForScopeAndConstraints(first, shell);

         if (plugin != null)
         {
            if (!tokens.isEmpty())
            {
               String second = tokens.peek();
               command = plugin.getCommand(second, shell);

               if (command != null)
               {
                  tokens.remove();
               }
               else if (plugin.hasDefaultCommand())
               {
                  command = plugin.getDefaultCommand();
               }
            }
            else if (plugin.hasDefaultCommand())
            {
               command = plugin.getDefaultCommand();
            }

            if (command != null)
            {
               if (!command.usableWithResource(shell.getCurrentResource().getClass()))
               {
                  // noinspection unchecked
                  throw new PluginExecutionException(plugin, "command '"
                        + command.getName()
                        + "' is not usable in current scope ["
                        + shell.getCurrentResource().getClass().getSimpleName()
                        + "]"
                        + " -- usable scopes: "
                        + GeneralUtils.elementSetSimpleTypesToString((Set) command.getResourceScopes()));
               }

               execution.setCommand(command);

               // parse parameters and set order / nulls for command invocation

               Object[] parameters = parseParameters(command, tokens);
               execution.setParameterArray(parameters);
            }
            else
            {
               throw new PluginExecutionException(plugin, "Missing command for plugin [" + plugin.getName()
                     + "], available commands: " + plugin.getCommands(shell));
            }
         }
      }

      return execution;
   }

   private Object[] parseParameters(final CommandMetadata command, final Queue<String> tokens)
   {
      CommandParser commandParser = new CompositeCommandParser(new NamedBooleanOptionParser(),
            new NamedValueOptionParser(), new NamedValueVarargsOptionParser(), new OrderedValueOptionParser(),
            new OrderedValueVarargsOptionParser(), new ParseErrorParser());

      Map<OptionMetadata, Object> valueMap = commandParser.parse(command, tokens);

      Object[] parameters = new Object[command.getOptions().size()];
      for (OptionMetadata option : command.getOptions())
      {
         Object value = valueMap.get(option);

         PromptType promptType = option.getPromptType();
         String defaultValue = option.getDefaultValue();
         Class<?> optionType = option.getBoxedType();
         String optionDescriptor = option.getOptionDescriptor() + ": ";

         // TODO Is this really where we want to do PromptType conversion?
         value = doPromptTypeConversions(value, promptType);

         if ((value != null) && !value.toString().matches(promptType.getPattern()))
         {
            // make sure the current option value is OK
            shell.println("Could not parse [" + value + "]... please try again...");
            value = shell.promptCommon(optionDescriptor, promptType);
         }
         else if (option.isRequired() && (value == null) && (!option.hasDefaultValue()))
         {
            while (value == null)
            {
               if (isBooleanOption(option))
               {
                  value = shell.promptBoolean(optionDescriptor);
               }
               else if (isFileOption(optionType))
               {
                  value = shell.promptFile(optionDescriptor);
               }
               else if (promptType != null)
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
         else if ((value == null) && (option.hasDefaultValue()))
         {
            value = defaultValue;
         }

         parameters[option.getIndex()] = value;
      }

      return parameters;
   }

   private Object doPromptTypeConversions(Object value, final PromptType promptType)
   {
      if ((value != null) && value.getClass().isArray())
      {
         Object[] values = (Object[]) value;
         if (values != null)
         {
            for (int i = 0; i < values.length; i++)
            {
               values[i] = promptTypeConverter.convert(promptType, (String) values[i]);
            }
         }
         value = values;
      }
      else
      {
         value = promptTypeConverter.convert(promptType, (String) value);
      }
      return value;
   }

   public boolean isFileOption(final Class<?> optionType)
   {
      return File.class.isAssignableFrom(optionType);
   }

   private static boolean isBooleanOption(final OptionMetadata option)
   {
      return ParseTools.unboxPrimitive(option.getType()) == boolean.class;
   }
}
