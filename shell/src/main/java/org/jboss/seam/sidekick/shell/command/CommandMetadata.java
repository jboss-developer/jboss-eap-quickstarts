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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class CommandMetadata
{
   private PluginMetadata parent;
   private Method method;
   private boolean isDefault = false;

   private String name = "";
   private String help = "";
   private List<OptionMetadata> options = new ArrayList<OptionMetadata>();

   public OptionMetadata getNamedOption(final String name) throws IllegalArgumentException
   {
      for (OptionMetadata option : options)
      {
         if (option.isNamed() && option.getName().equals(name))
         {
            return option;
         }
      }
      throw new IllegalArgumentException("No such option [" + name + "] for command: " + this);
   }

   public OptionMetadata getOrderedOptionByIndex(final int index) throws IllegalArgumentException
   {
      int currentIndex = 0;
      for (OptionMetadata option : options)
      {
         if (!option.isNamed() && (index == currentIndex))
         {
            return option;
         }
         else if (!option.isNamed())
         {
            currentIndex++;
         }
      }
      throw new IllegalArgumentException("No option with index [" + index + "] exists for command: " + this);
   }

   public Method getMethod()
   {
      return method;
   }

   public void setMethod(final Method method)
   {
      this.method = method;
   }

   public boolean isDefault()
   {
      return isDefault;
   }

   public void setDefault(final boolean isDefault)
   {
      this.isDefault = isDefault;
   }

   public String getName()
   {
      return name;
   }

   public void setName(final String name)
   {
      this.name = name;
   }

   public List<OptionMetadata> getOptions()
   {
      if (options == null)
      {
         options = new ArrayList<OptionMetadata>();
      }
      return options;
   }

   public void setOptions(final List<OptionMetadata> options)
   {
      this.options = options;
   }

   public void addOption(final OptionMetadata option)
   {
      this.options.add(option);
   }

   public String getHelp()
   {
      return help;
   }

   public void setHelp(final String help)
   {
      this.help = help;
   }

   @Override
   public String toString()
   {
      return name;
   }

   public PluginMetadata getPluginMetadata()
   {
      return parent;
   }

   public void setParent(final PluginMetadata parent)
   {
      this.parent = parent;
   }

   public boolean hasOptions()
   {
      return !getOptions().isEmpty();
   }
}