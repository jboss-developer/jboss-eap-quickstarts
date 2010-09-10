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

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.jboss.seam.sidekick.shell.plugins.Plugin;

/**
 * Stores the current registry of all installed & loaded plugins.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@Singleton
public class PluginRegistry
{
   private Map<String, PluginMetadata> plugins;

   @Inject
   private CommandLibraryExtension library;

   @Inject
   private BeanManager manager;

   @PostConstruct
   public void init()
   {
      plugins = library.getPlugins();
   }

   public Map<String, PluginMetadata> getPlugins()
   {
      return plugins;
   }

   public void addPlugin(final PluginMetadata plugin)
   {
      plugins.put(plugin.getName(), plugin);
   }

   @Override
   public String toString()
   {
      return "PluginRegistry [plugins=" + plugins + "]";
   }

   public Plugin instanceOf(final PluginMetadata meta)
   {
      return getContextualInstance(manager, meta.getType());
   }

   @SuppressWarnings("unchecked")
   public static <T> T getContextualInstance(final BeanManager manager, final Class<T> type)
   {
      T result = null;
      Bean<T> bean = (Bean<T>) manager.resolve(manager.getBeans(type));
      if (bean != null)
      {
         CreationalContext<T> context = manager.createCreationalContext(bean);
         if (context != null)
         {
            result = (T) manager.getReference(bean, type, context);
         }
      }
      return result;
   }

}
