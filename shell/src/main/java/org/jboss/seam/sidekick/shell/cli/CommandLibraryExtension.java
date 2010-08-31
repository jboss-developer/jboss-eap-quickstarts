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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessBean;
import javax.inject.Named;

import org.jboss.seam.sidekick.shell.plugins.plugins.Command;
import org.jboss.seam.sidekick.shell.plugins.plugins.DefaultCommand;
import org.jboss.seam.sidekick.shell.plugins.plugins.Help;
import org.jboss.seam.sidekick.shell.plugins.plugins.Option;
import org.jboss.seam.sidekick.shell.plugins.plugins.Plugin;
import org.jboss.seam.sidekick.shell.util.Annotations;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class CommandLibraryExtension implements Extension
{
   private final Map<String, PluginMetadata> plugins = new HashMap<String, PluginMetadata>();

   public Map<String, PluginMetadata> getPlugins()
   {
      return plugins;
   }

   @SuppressWarnings("unchecked")
   public void scan(@Observes final ProcessBean<?> event)
   {
      Bean<?> bean = event.getBean();

      Class<?> clazz = bean.getBeanClass();

      if (Plugin.class.isAssignableFrom(clazz))
      {
         PluginMetadata pluginMeta = getMetadataFor((Class<? extends Plugin>) clazz);
         plugins.put(pluginMeta.getName(), pluginMeta);
      }
   }

   public PluginMetadata getMetadataFor(final Class<? extends Plugin> plugin)
   {
      String name = getPluginName(plugin);

      PluginMetadata pluginMeta = new PluginMetadata();
      pluginMeta.setName(name);
      pluginMeta.setType(plugin);

      if (Annotations.isAnnotationPresent(plugin, Help.class))
      {
         pluginMeta.setHelp(Annotations.getAnnotation(plugin, Help.class).value());
      }

      processPluginCommands(pluginMeta, plugin);

      return pluginMeta;
   }

   private List<CommandMetadata> processPluginCommands(final PluginMetadata pluginMeta, final Class<?> plugin)
   {
      List<CommandMetadata> results = new ArrayList<CommandMetadata>();
      pluginMeta.setCommands(results);

      for (Method method : plugin.getMethods())
      {
         if (Annotations.isAnnotationPresent(method, Command.class))
         {
            Command command = Annotations.getAnnotation(method, Command.class);
            CommandMetadata commandMeta = new CommandMetadata();
            commandMeta.setMethod(method);
            commandMeta.setHelp(command.help());
            commandMeta.setParent(pluginMeta);

            // Default commands are invoked via the name of the plug-in, not by
            // plug-in + command
            if (command.value() == "")
            {
               commandMeta.setName(method.getName().trim().toLowerCase());
            }
            else
            {
               commandMeta.setName(command.value());
            }

            // This works because @DefaultCommand is annotated by @Command
            if (Annotations.isAnnotationPresent(method, DefaultCommand.class))
            {
               if (pluginMeta.hasDefaultCommand())
               {
                  throw new IllegalStateException("Plugins may only have one @"
                           + DefaultCommand.class.getSimpleName()
                           + ", but [" + pluginMeta.getType() + "] has more than one.");
               }

               commandMeta.setDefault(true);
               commandMeta.setName(pluginMeta.getName());

               // favor help text from this annotation over others
               DefaultCommand def = Annotations.getAnnotation(method, DefaultCommand.class);
               if ((def.help() != null) && !def.help().trim().isEmpty())
               {
                  commandMeta.setHelp(def.help());
               }
            }

            // fall back to the pluginMetadata for help text
            if ((commandMeta.getHelp() == null) || commandMeta.getHelp().trim().isEmpty())
            {
               commandMeta.setHelp(pluginMeta.getHelp());
            }

            Class<?>[] parameterTypes = method.getParameterTypes();
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();

            int i = 0;
            for (Class<?> clazz : parameterTypes)
            {
               OptionMetadata option = new OptionMetadata();
               option.setType(clazz);
               option.setIndex(i);

               for (Annotation a : parameterAnnotations[i])
               {
                  if (a instanceof Option)
                  {
                     Option opt = (Option) a;
                     option.setParent(commandMeta);
                     option.setName(opt.value());
                     option.setHelp(opt.help());
                     option.setRequired(opt.required());
                  }
               }
               commandMeta.addOption(option);
               i++;
            }

            results.add(commandMeta);
         }
      }
      return results;
   }

   private String getPluginName(final Class<?> plugin)
   {
      String name = null;
      Named named = Annotations.getAnnotation(plugin, Named.class);
      if (named != null)
      {
         name = named.value();
      }
      if ((name == null) || "".equals(name.trim()))
      {
         name = plugin.getSimpleName();
      }
      return name.toLowerCase();
   }
}
