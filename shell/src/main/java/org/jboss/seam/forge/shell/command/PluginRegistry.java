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

import org.jboss.seam.forge.project.Facet;
import org.jboss.seam.forge.project.PackagingType;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.shell.plugins.ResourceScope;

import javax.annotation.PostConstruct;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

/**
 * Stores the current registry of all installed & loaded plugins.
 *
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Singleton
public class PluginRegistry
{
   private Map<String, List<PluginMetadata>> plugins;
   private Map<String, Map<Class, PluginMetadata>> accessCache;

   private final CommandLibraryExtension library;
   private final BeanManager manager;

   @Inject
   public PluginRegistry(final CommandLibraryExtension library, final BeanManager manager)
   {
      this.library = library;
      this.manager = manager;
   }

   @PostConstruct
   public void init()
   {
      plugins = library.getPlugins();
      accessCache = new HashMap<String, Map<Class, PluginMetadata>>();
      sanityCheck();
   }

   public Map<String, List<PluginMetadata>> getPlugins()
   {
      return plugins;
   }

   public void addPlugin(final PluginMetadata plugin)
   {
      if (!plugins.containsKey(plugin.getName()))
      {
         plugins.put(plugin.getName(), new ArrayList<PluginMetadata>());
      }

      plugins.get(plugin.getName()).add(plugin);
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

   /**
    * Get {@link PluginMetadata} for the plugin with the given name.
    *
    * @return the metadata, or null if no plugin with given name exists.
    */
   public List<PluginMetadata> getPluginMetadata(final String plugin)
   {
      return Collections.unmodifiableList(plugins.get(plugin));
   }

   /**
    * Get {@link PluginMetadata} matching the given name, {@link ResourceScope}, {@link Project}, {@link PackagingType},
    * and {@link Facet} constraints. Return null if no match for the given constraints can be found.
    */
   public PluginMetadata getPluginMetadataForScopeAndConstraints(final String name, final Shell shell)
   {
      Class<? extends Resource<?>> scope = shell.getCurrentResourceScope();
      if (accessCache.containsKey(name) && accessCache.get(name).containsKey(scope))
      {
         return accessCache.get(name).get(scope);
      }

      List<PluginMetadata> pluginMetadataList = plugins.get(name);
      if (pluginMetadataList == null)
      {
         return null;
      }

      PluginMetadata pmd = null;
      for (PluginMetadata p : pluginMetadataList)
      {
         if (p.constrantsSatisfied(shell))
         {
            pmd = p;
            break;
         }
      }

      return pmd;
   }

   private void sanityCheck()
   {
      for (Map.Entry<String, List<PluginMetadata>> entry : plugins.entrySet())
      {
         Set<Class<? extends Resource<?>>> scopes = null;

         for (PluginMetadata metaData : entry.getValue())
         {
            if (scopes == null)
            {
               scopes = metaData.getResourceScopes();
            }
            else
            {
               for (Class<? extends Resource<?>> r : metaData.getResourceScopes())
               {
                  if (scopes.contains(r))
                  {
                     throw new RuntimeException("failed sanity check. overlapping scopes for overloaded plugin name: "
                           + entry.getKey() + " [" + entry.getValue() + "]");
                  }
               }
            }
         }
      }
   }

}
