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

import org.jboss.seam.forge.shell.PromptType;
import org.jboss.seam.forge.shell.completer.CommandCompleter;
import org.jboss.seam.forge.shell.completer.NullCommandCompleter;
import org.jboss.seam.forge.shell.plugins.PipeIn;
import org.jboss.seam.forge.shell.plugins.PipeOut;
import org.jboss.seam.forge.shell.util.Types;
import org.mvel2.util.ParseTools;
import org.mvel2.util.StringAppender;

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
   private String shortName = "";
   private String defaultValue = "";
   private String description = "";
   private String help = "";

   private boolean flagOnly = false;
   private boolean required = false;
   private PromptType promptType;

   private boolean pipeOut;
   private boolean pipeIn;
   private Class<? extends CommandCompleter> completerType;

   public OptionMetadata()
   {
   }

   /**
    * Get an informational string describing this Option
    */
   public String getOptionDescriptor()
   {
      StringAppender appender = new StringAppender("[");
      if (isNamed())
      {
         appender.append(name).append("=");
      }

      if (getDescription().equals(""))
      {
         appender.append("ARG").append(" (").append(Types.getTypeDescriptor(type)).append(")");
      }
      else
      {
         appender.append(description).append(" (").append(Types.getTypeDescriptor(type)).append(")");
      }

      return appender.append(']').toString();
   }

   /**
    * Return whether this option is to be mapped via name or via parameter order.
    */
   public boolean isNamed()
   {
      return (name != null) && !"".equals(name);
   }

   /**
    * Return the Boxed type of this Option, e.g: If the option is actual an <code>int.class</code>, return
    * <code>Integer.class</code> instead.
    */
   public Class<?> getBoxedType()
   {
      return ParseTools.boxPrimitive(getType());
   }

   /**
    * Return the literal type represented by this Option, e.g: the actual method parameter type.
    */
   public Class<?> getType()
   {
      return type;
   }

   public void setType(final Class<?> type)
   {
      this.type = type;
   }

   /**
    * Return the name of this Option, if it has one
    */
   public String getName()
   {
      return name;
   }

   public void setName(final String name)
   {
      this.name = name;
   }

   /**
    * Get the short name of this option, if it has one.
    */
   public String getShortName()
   {
      return shortName;
   }

   public void setShortName(final String shortName)
   {
      this.shortName = shortName;
   }

   /**
    * Return whether or not this option is purely a boolean flag.
    */
   public boolean isFlagOnly()
   {
      return flagOnly;
   }

   public void setFlagOnly(final boolean flagOnly)
   {
      this.flagOnly = flagOnly;
   }

   /**
    * Return the description of this Option
    */
   public String getDescription()
   {
      return description;
   }

   public void setDescription(final String description)
   {
      this.description = description;
   }

   /**
    * Get the index of this Option in the receiving method parameter list.
    */
   public int getIndex()
   {
      return index;
   }

   public void setIndex(final int index)
   {
      this.index = index;
   }

   /**
    * Get the help text for this Option
    */
   public String getHelp()
   {
      return help;
   }

   public void setHelp(final String help)
   {
      this.help = help;
   }

   /**
    * Return whether or not this option requires a value at execution time.
    */
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
      return name + ":" + description;
   }

   /**
    * Get the parent Command of this Option
    */
   public CommandMetadata getParent()
   {
      return parent;
   }

   public void setParent(final CommandMetadata parent)
   {
      this.parent = parent;
   }

   /**
    * Return whether or not this option represents a {@link Boolean} type
    */
   public boolean isBoolean()
   {
      return (Boolean.TYPE.equals(getType()) || Boolean.class.equals(getType()));
   }

   /**
    * Return whether or not this option represents an {@link Enum} type.
    */
   public boolean isEnum()
   {
      return getType() != null && getType().isEnum();
   }

   /**
    * Return whether or not this option represents a Varargs parameter type
    */
   public boolean isVarargs()
   {
      return getType().isArray();
   }

   /**
    * Return the default value for this Option, if specified
    */
   public String getDefaultValue()
   {
      return defaultValue;
   }

   public void setDefaultValue(final String defaultValue)
   {
      this.defaultValue = defaultValue;
   }

   /**
    * Return whether or not this Option provides a default value
    */
   public boolean hasDefaultValue()
   {
      return (defaultValue != null) && !"".equals(defaultValue);
   }

   /**
    * Return the selected {@link PromptType} for this Option.
    */
   public PromptType getPromptType()
   {
      return promptType;
   }

   public void setPromptType(final PromptType type)
   {
      this.promptType = type;
   }

   /**
    * Return whether or not this Option is a {@link PipeOut}
    */
   public boolean isPipeOut()
   {
      return pipeOut;
   }

   public void setPipeOut(final boolean pipeOut)
   {
      this.pipeOut = pipeOut;
   }

   /**
    * Return whether or not this Option is a {@link PipeIn}
    */
   public boolean isPipeIn()
   {
      return pipeIn;
   }

   public void setPipeIn(final boolean pipeIn)
   {
      this.pipeIn = pipeIn;
   }

   /**
    * Return whether or not this Option is not ordered, e.g: It might have a name, or be an input/output pipe.
    */
   public boolean notOrdered()
   {
      return pipeIn || pipeOut || isNamed();
   }

   /**
    * Return whether or not this option has specified a custom {@link CommandCompleter}
    */
   public boolean hasCustomCompleter()
   {
      return (completerType != null) && !completerType.equals(NullCommandCompleter.class);
   }

   public void setCompleterType(final Class<? extends CommandCompleter> type)
   {
      this.completerType = type;
   }

   /**
    * Get the custom {@link CommandCompleter} for this Option, if specified.
    */
   public Class<? extends CommandCompleter> getCompleterType()
   {
      return completerType;
   }
}
