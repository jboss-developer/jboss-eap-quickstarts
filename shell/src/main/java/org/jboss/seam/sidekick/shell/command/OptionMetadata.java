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

import org.jboss.seam.sidekick.shell.PromptType;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class OptionMetadata
{
   private CommandMetadata parent;
   private Class<?> type;
   private int index;

   private String name = "";
   private String defaultValue = "";
   private String description = "";
   private String help = "";
   private boolean required = false;
   private PromptType promptType;

   /**
    * Return whether this option is to be mapped via name or via parameter
    * order.
    */
   public boolean isNamed()
   {
      return (name != null) && !"".equals(name);
   }

   public Class<?> getType()
   {
      return type;
   }

   public void setType(final Class<?> type)
   {
      this.type = type;
   }

   public String getName()
   {
      return name;
   }

   public void setName(final String name)
   {
      this.name = name;
   }

   public String getDescription()
   {
      return description;
   }

   public void setDescription(final String description)
   {
      this.description = description;
   }

   public int getIndex()
   {
      return index;
   }

   public void setIndex(final int index)
   {
      this.index = index;
   }

   public String getHelp()
   {
      return help;
   }

   public void setHelp(final String help)
   {
      this.help = help;
   }

   public boolean isRequired()
   {
      return required;
   }

   public void setRequired(final boolean required)
   {
      this.required = required;
   }

   @Override
   public String toString()
   {
      return name;
   }

   public CommandMetadata getParent()
   {
      return parent;
   }

   public void setParent(final CommandMetadata parent)
   {
      this.parent = parent;
   }

   public boolean isBoolean()
   {
      return (Boolean.TYPE.equals(getType()) || Boolean.class.equals(getType()));
   }

   public boolean isVarargs()
   {
      return getType().isArray();
   }

   public String getDefaultValue()
   {
      return defaultValue;
   }

   public void setDefaultValue(final String defaultValue)
   {
      this.defaultValue = defaultValue;
   }

   public boolean hasDefaultValue()
   {
      return (defaultValue != null) && !"".equals(defaultValue);
   }

   public PromptType getPromptType()
   {
      return promptType;
   }

   public void setPromptType(PromptType type)
   {
      this.promptType = type;
   }

}
