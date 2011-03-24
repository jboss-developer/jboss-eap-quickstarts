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

import jline.console.completer.Completer;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Singleton
public class PluginCommandCompleter implements Completer
{

   List<CommandCompleter> completers = new ArrayList<CommandCompleter>();

   private String lastBuffer = null;

   private final CompletedCommandHolder optionHolder;

   @Inject
   public PluginCommandCompleter(final PluginResolverCompleter plugin,
            final CommandResolverCompleter command,
            final OptionResolverCompleter option,
            final OptionValueResolverCompleter value,
            final CompletedCommandHolder optionHolder)
   {
      this.optionHolder = optionHolder;
      completers.add(plugin);
      completers.add(command);
      completers.add(option);
      completers.add(value);
   }

   @Override
   public int complete(final String buffer, final int cursor, final List<CharSequence> candidates)
   {
      optionHolder.setState(null);

      PluginCommandCompleterState state = new PluginCommandCompleterState(buffer, lastBuffer, cursor);

      // TODO replace lastBuffer with a lastState object?
      lastBuffer = buffer;

      for (CommandCompleter c : completers)
      {
         if (!state.hasSuggestions())
         {
            c.complete(state);
         }
      }

      if ((state.getPlugin() != null) && !state.isFinalTokenComplete() && !state.hasSuggestions()
               && state.isDuplicateBuffer())
      {
         candidates.add(" ");
      }

      candidates.addAll(state.getCandidates());

      // ensure the completer is triggered always
      if ((state.getPlugin() != null) && state.isFinalTokenComplete() && !state.hasSuggestions()
               && state.isDuplicateBuffer() && state.getCandidates().isEmpty())
      {
         candidates.add("");
      }

      optionHolder.setState(state);

      return state.getIndex();
   }

   /**
    * Add option completions for the given command, with or without argument tokens
    */
   public static boolean isPotentialMatch(final String full, final String partial)
   {
      return full.matches("(?i)" + partial + ".*");
   }

}
