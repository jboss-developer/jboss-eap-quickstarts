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

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import jline.console.completer.Completer;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class PluginCommandCompletionHandler implements Completer
{
   @Inject
   private PluginRegistry registry;

   @Override
   public int complete(final String buffer, final int cursor, final List<CharSequence> candidates)
   {
      Map<String, PluginMetadata> plugins = registry.getPlugins();

      String[] tokens = buffer.split("\\s+");
      if (tokens.length == 1)
      {
         String token = tokens[0];
         for (Entry<String, PluginMetadata> p : plugins.entrySet())
         {
            String pluginName = p.getValue().getName();
            if (isPotentialMatch(pluginName, token))
            {
               candidates.add(pluginName);
            }
         }
      }
      return 0;
   }

   private boolean isPotentialMatch(final String name, final String token)
   {
      return name.matches("(?i)" + token + ".*");
   }
}
