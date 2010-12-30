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
import java.util.Queue;

import org.jboss.seam.forge.shell.command.CommandMetadata;
import org.jboss.seam.forge.shell.command.OptionMetadata;
import org.jboss.seam.forge.shell.command.PluginMetadata;
import org.jboss.seam.forge.shell.command.parser.Tokenizer;

/**
 * Holds state during TAB completion.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class PluginCommandCompleterState implements CommandCompleterState
{
   /*
    * Immutable state
    */
   private final String buffer;
   private final String lastBuffer;
   private final int originalIndex;
   private final boolean tokenComplete;

   /*
    * Mutable State
    */
   private final Queue<String> tokens;

   private PluginMetadata plugin;
   private CommandMetadata command;
   private OptionMetadata option;
   private Map<OptionMetadata, Object> optionValueMap;

   private final List<String> candidates = new ArrayList<String>();
   private int index;

   public PluginCommandCompleterState(final String initialBuffer, final String lastBuffer, final int initialIndex)
   {
      this.buffer = initialBuffer;
      this.lastBuffer = lastBuffer;
      this.index = initialIndex;
      this.originalIndex = initialIndex;
      this.tokens = new Tokenizer().tokenize(initialBuffer);
      this.tokenComplete = buffer.matches("^.*\\s+$");
   }

   /*
    * Immutable State
    */
   public int getOriginalIndex()
   {
      return originalIndex;
   }

   public Queue<String> getOriginalTokens()
   {
      return new Tokenizer().tokenize(buffer);
   }

   public String getBuffer()
   {
      return buffer;
   }

   public String getLastBuffer()
   {
      return lastBuffer;
   }

   public boolean isFinalTokenComplete()
   {
      return tokenComplete;
   }

   /*
    * Inquisitors
    */
   public boolean hasSuggestions()
   {
      return !candidates.isEmpty();
   }

   /*
    * Modifiers
    */
   public void setIndex(final int newIndex)
   {
      this.index = newIndex;
   }

   /*
    * Mutable state
    */
   public PluginMetadata getPlugin()
   {
      return plugin;
   }

   public void setPlugin(final PluginMetadata plugin)
   {
      this.plugin = plugin;
   }

   public CommandMetadata getCommand()
   {
      return command;
   }

   public void setCommand(final CommandMetadata command)
   {
      this.command = command;
   }

   public OptionMetadata getOption()
   {
      return option;
   }

   public void setOption(final OptionMetadata option)
   {
      this.option = option;
   }

   public int getIndex()
   {
      return index;
   }

   public Queue<String> getTokens()
   {
      return tokens;
   }

   public List<String> getCandidates()
   {
      return candidates;
   }

   public Map<OptionMetadata, Object> getOptionValueMap()
   {
      return optionValueMap;
   }

   public void setOptionValueMap(final Map<OptionMetadata, Object> optionValueMap)
   {
      this.optionValueMap = optionValueMap;
   }
}
