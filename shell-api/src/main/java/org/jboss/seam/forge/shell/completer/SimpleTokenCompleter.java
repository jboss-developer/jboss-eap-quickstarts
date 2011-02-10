/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc., and individual contributors
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

import java.util.List;

/**
 * The simplest possible command option completer. Matching of tokens is handled automatically based on comparing the
 * partial token against the beginning of each candidate, but also provides the least control over how completion
 * occurs.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public abstract class SimpleTokenCompleter implements CommandCompleter
{
   /**
    * Return a list of tokens to be considered as possible completions for the current input buffer. Typically, this
    * should be a list of all possible candidates, but might vary depending on current state.
    */
   public abstract List<Object> getCompletionTokens();

   @Override
   public void complete(final CommandCompleterState state)
   {
      List<Object> values = getCompletionTokens();
      String peek = state.getTokens().peek();

      if ((state.getTokens().size() <= 1))
      {
         for (Object val : values)
         {
            if (val != null)
            {
               String prop = val.toString();
               if (prop.startsWith(peek == null ? "" : peek))
               {
                  state.getCandidates().add(prop + " ");
                  state.setIndex(state.getOriginalIndex() - (peek == null ? 0 : peek.length()));
               }
            }
         }
      }
   }

}
