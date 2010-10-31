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

import java.util.*;

import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.mvel2.sh.ShellSession;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * @author Mike Brock
 */
public class PluginMetadata
{
   private String help = "";
   private String name = "";
   private Class<? extends Plugin> type;

   @SuppressWarnings({"unchecked"})
   private Set<Class<? extends Resource<?>>> resourceScopes = Collections.emptySet();

   private Map<String, List<CommandMetadata>> commandMap = new HashMap<String, List<CommandMetadata>>();
   private Map<String, Map<Class, CommandMetadata>> commandAccessCache
         = new HashMap<String, Map<Class, CommandMetadata>>();

   private CommandMetadata defaultCommand;

   private boolean scopeOverloaded = false;


   public CommandMetadata getCommand(final String name)
   {
      return getCommand(name, (Class<? extends Resource>) null);
   }

   public CommandMetadata getCommand(final String name, final Shell shell)
   {
      return getCommand(name, shell.getCurrentResourceScope());
   }

   /**
    * Get the command matching the given name, or return null.
    */
   public CommandMetadata getCommand(final String name, final Class<? extends Resource> scope)
   {
      if (scope == null)
      {
         if (commandMap.containsKey(name) && commandMap.get(name).size() > 1)
         {
            throw new RuntimeException("ambiguous query: overloaded commands exist. you must specify a scope.");
         }
         else
         {
            return commandMap.get(name).iterator().next();
         }
      }

      if (commandAccessCache.containsKey(name) && commandAccessCache.get(name).containsKey(scope))
      {
         return commandAccessCache.get(name).get(scope);
      }

      List<CommandMetadata> cmdMetadata = commandMap.get(name);
      if (cmdMetadata == null)
      {
         return null;
      }

      for (CommandMetadata c : cmdMetadata)
      {
         if (c.usableWithScope(scope))
         {
            return c;
         }
      }

      return null;
   }

   public boolean hasCommand(final String name, Shell shell)
   {
      return getCommand(name, shell.getCurrentResourceScope()) != null;
   }


   public boolean hasCommand(final String name, final Class<? extends Resource> scope)
   {
      return getCommand(name, scope) != null;
   }

   public boolean hasDefaultCommand()
   {
      return getDefaultCommand() != null;
   }

   public CommandMetadata getDefaultCommand()
   {
      return defaultCommand;
   }

   public void addCommands(List<CommandMetadata> commands)
   {
      for (CommandMetadata c : commands)
      {
         addCommand(c);
      }
   }

   public void addCommand(CommandMetadata command)
   {
      if (command.isDefault())
      {
         if (defaultCommand != null)
         {
            throw new RuntimeException("default command already defined: " + command.getName() + "; for plugin: " + name);
         }
         defaultCommand = command;
      }

      if (!commandMap.containsKey(command.getName()))
      {
         commandMap.put(command.getName(), new ArrayList<CommandMetadata>());
      }
      else
      {
         scopeOverloaded = true;
      }

      commandMap.get(command.getName()).add(command);
   }

   public List<CommandMetadata> getCommands()
   {
      return getCommands((Class<? extends Resource>) null);
   }

   public List<CommandMetadata> getCommands(final Shell shell)
   {
      return getCommands(shell.getCurrentResourceScope());
   }

   public List<CommandMetadata> getCommands(final Class<? extends Resource> scope)
   {
      if (scope == null && scopeOverloaded)
      {
         throw new RuntimeException("ambiguous query: overloaded commands exist. you must specify a scope.");
      }

      List<CommandMetadata> result = new ArrayList<CommandMetadata>();
      for (List<CommandMetadata> cl : commandMap.values())
      {
         for (CommandMetadata c : cl)
         {
            if (scope == null || c.usableWithScope(scope))
            {
               result.add(c);
            }
         }
      }
      return Collections.unmodifiableList(result);
   }

   public List<CommandMetadata> getAllCommands()
   {
      List<CommandMetadata> result = new ArrayList<CommandMetadata>();
      for (List<CommandMetadata> cl : commandMap.values())
      {
         for (CommandMetadata c : cl)
         {
            result.add(c);
         }
      }
      return Collections.unmodifiableList(result);
   }

   public boolean isCommandOverloaded(String name) {
      return commandMap.containsKey(name) && commandMap.get(name).size() > 1;
   }

   @Override
   public String toString()
   {
      return name;
   }

   public String getName()
   {
      return name;
   }

   public void setName(final String name)
   {
      this.name = name;
   }

   public Class<? extends Plugin> getType()
   {
      return type;
   }

   public void setType(final Class<? extends Plugin> type)
   {
      this.type = type;
   }

   public String getHelp()
   {
      return help;
   }

   public void setHelp(final String help)
   {
      this.help = help;
   }

   public boolean hasCommands()
   {
      return !commandMap.isEmpty();
   }

   public boolean usableWithScope(Class<? extends Resource> scope)
   {
      return resourceScopes.isEmpty() || resourceScopes.contains(scope);
   }

   public Set<Class<? extends Resource<?>>> getResourceScopes()
   {
      return resourceScopes;
   }

   public void setResourceScopes(List<Class<? extends Resource<?>>> resourceScopes)
   {
      this.resourceScopes = new HashSet<Class<? extends Resource<?>>>(resourceScopes);
   }
}
