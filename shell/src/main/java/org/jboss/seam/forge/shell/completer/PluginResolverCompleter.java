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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import javax.inject.Inject;

import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.command.PluginMetadata;
import org.jboss.seam.forge.shell.command.PluginRegistry;

/**
 * @author Lincoln
 * 
 */
public class PluginResolverCompleter implements CommandCompleter
{
   @Inject
   private Shell shell;

   @Inject
   private PluginRegistry registry;

   @Override
   public void complete(CommandCompleterState st)
   {
      PluginCommandCompleterState state = ((PluginCommandCompleterState) st);
      Queue<String> tokens = state.getTokens();
      
      if (!tokens.isEmpty())
      {
         String pluginName = tokens.remove();
         PluginMetadata plugin = registry.getPluginMetadataForScopeAndConstraints(pluginName, shell);

         if ((plugin != null))
         {
            // found a plugin match directly
            if (tokens.isEmpty())
            {
               // there's only a plugin so far
               if (state.isFinalTokenComplete())
               {
                  // they chose this command, start at the end for command
                  // completion
                  state.setPlugin(plugin);
                  state.setIndex(state.getBuffer().length());
               }
               else
               {
                  // they haven't yet chosen a plugin, start at the beginning
                  state.setIndex(0);
               }
            }
         }
         else
         {
            // no plugin found, but we have a partial name with which to attempt
            // suggestion
            state.setIndex(0);
            if (tokens.isEmpty())
            {
               List<String> pluginCandidates = getPluginCandidates(registry, pluginName);
               state.getCandidates().addAll(pluginCandidates);
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
         state.setIndex(0);
         List<String> pluginCandidates = getPluginCandidates(registry, "");
         state.getCandidates().addAll(pluginCandidates);
      }
   }

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
               if (PluginCommandCompleter.isPotentialMatch(pluginName, pluginBase) && pluginMeta.constrantsSatisfied(shell))
               {
                  results.add(pluginName + " ");
               }
            }
         }
      }

      return results;
   }

}
