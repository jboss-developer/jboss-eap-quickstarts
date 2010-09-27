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
package org.jboss.seam.sidekick.shell.command;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.sidekick.shell.plugins.Plugin;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class PluginMetadata
{
   private String help = "";
   private String name = "";
   private Class<? extends Plugin> type;
   private List<CommandMetadata> commands = new ArrayList<CommandMetadata>();

   /**
    * Get the command matching the given name, or return null.
    */
   public CommandMetadata getCommand(final String name)
   {
      CommandMetadata result = null;

      List<CommandMetadata> commands = this.getCommands();
      for (CommandMetadata commandMetadata : commands)
      {
         if (commandMetadata.getName().equals(name))
         {
            result = commandMetadata;
         }
      }

      return result;
   }

   public boolean hasCommand(final String name)
   {
      return getCommand(name) != null;
   }

   public boolean hasDefaultCommand()
   {
      return getDefaultCommand() != null;
   }

   public CommandMetadata getDefaultCommand()
   {
      CommandMetadata result = null;
      for (CommandMetadata command : commands)
      {
         if (command.isDefault())
         {
            result = command;
            break;
         }
      }
      return result;
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

   public List<CommandMetadata> getCommands()
   {
      return commands;
   }

   public void setCommands(final List<CommandMetadata> commands)
   {
      this.commands = commands;
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
      return (commands != null) && (commands.size() > 0);
   }
}
