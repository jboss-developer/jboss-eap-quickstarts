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

import javax.inject.Inject;

import org.jboss.seam.sidekick.shell.command.PluginMetadata;
import org.jboss.seam.sidekick.shell.command.PluginRegistry;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class PluginCommandCompletionHandler implements CommandCompleter
{
   @Inject
   private PluginRegistry registry;

   @Override
   public int complete(final String buffer, final int cursor, final List<CharSequence> candidates)
   {
      Map<String, PluginMetadata> plugins = registry.getPlugins();

      List<CharSequence> results = new ArrayList<CharSequence>();

      String[] tokens = buffer.substring(0, cursor).split("\\s+");
      if (tokens.length == 1)
      {
         String token = tokens[0];
         for (Entry<String, PluginMetadata> p : plugins.entrySet())
         {
            if (p.getValue().hasCommands())
            {
               String pluginName = p.getValue().getName();
               if (isPotentialMatch(pluginName, token))
               {
                  results.add(pluginName + " ");
               }
            }
         }
      }
      candidates.addAll(results);
      if (results.isEmpty())
      {
         return -1;
      }
      return 0;
   }

   private boolean isPotentialMatch(final String name, final String token)
   {
      return name.matches("(?i)" + token + ".*");
   }
}
