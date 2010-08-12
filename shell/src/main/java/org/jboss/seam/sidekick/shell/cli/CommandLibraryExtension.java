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
import org.jboss.seam.sidekick.shell.plugins.plugins.Default;
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

      Class<? extends Plugin> plugin = (Class<? extends Plugin>) bean.getBeanClass();
      if (Plugin.class.isAssignableFrom(plugin))
      {
         System.out.println("\t> " + plugin);

         String name = getPluginName(plugin);

         PluginMetadata pluginMeta = new PluginMetadata();
         pluginMeta.setName(name);
         pluginMeta.setType(plugin);

         if (Annotations.isAnnotationPresent(plugin, Help.class))
         {
            pluginMeta.setHelp(Annotations.getAnnotation(plugin, Help.class).value());
         }

         List<CommandMetadata> commands = getPluginCommands(pluginMeta, plugin);
         pluginMeta.setCommands(commands);

         plugins.put(name, pluginMeta);
      }
   }

   private List<CommandMetadata> getPluginCommands(final PluginMetadata pluginMeta, final Class<?> plugin)
   {
      List<CommandMetadata> results = new ArrayList<CommandMetadata>();

      for (Method m : plugin.getMethods())
      {
         if (Annotations.isAnnotationPresent(m, Command.class))
         {
            Command command = Annotations.getAnnotation(m, Command.class);
            CommandMetadata commandMeta = new CommandMetadata();
            commandMeta.setMethod(m);
            commandMeta.setHelp(command.help());
            commandMeta.setParent(pluginMeta);

            if (Annotations.isAnnotationPresent(m, Default.class))
            {
               commandMeta.setDefault(true);
            }

            // Default commands are invoked via the name of the plug-in, not by
            // plug-in + command
            if (!commandMeta.isDefault())
            {
               if (command.value().length == 0)
               {
                  commandMeta.setNames(m.getName().toLowerCase());
               }
               else
               {
                  commandMeta.setNames(command.value());
               }
            }

            Class<?>[] parameterTypes = m.getParameterTypes();
            Annotation[][] parameterAnnotations = m.getParameterAnnotations();

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
