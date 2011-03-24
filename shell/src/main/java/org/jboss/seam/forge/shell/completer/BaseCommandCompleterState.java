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
import java.util.Queue;

import org.jboss.seam.forge.shell.command.parser.Tokenizer;

/**
 * Holds state during TAB completion.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class BaseCommandCompleterState implements CommandCompleterState
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
   private final List<String> candidates = new ArrayList<String>();
   private int index;

   public BaseCommandCompleterState(final String initialBuffer, final String lastBuffer, final int initialIndex)
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
   @Override
   public int getOriginalIndex()
   {
      return originalIndex;
   }

   @Override
   public Queue<String> getOriginalTokens()
   {
      return new Tokenizer().tokenize(buffer);
   }

   @Override
   public String getBuffer()
   {
      return buffer;
   }

   @Override
   public String getLastBuffer()
   {
      return lastBuffer;
   }

   @Override
   public boolean isFinalTokenComplete()
   {
      return tokenComplete;
   }

   /*
    * Inquisitors
    */
   @Override
   public boolean hasSuggestions()
   {
      return !candidates.isEmpty();
   }

   /*
    * Modifiers
    */
   /**
    * Set the position where completion candidates should begin.
    */
   @Override
   public void setIndex(final int newIndex)
   {
      this.index = newIndex;
   }

   @Override
   public int getIndex()
   {
      return index;
   }

   @Override
   public Queue<String> getTokens()
   {
      return tokens;
   }

   @Override
   public List<String> getCandidates()
   {
      return candidates;
   }

   public boolean isDuplicateBuffer()
   {
      return (buffer != null) && buffer.equals(lastBuffer);
   }
}
