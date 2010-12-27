package org.jboss.seam.forge.shell.completer;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.jboss.seam.forge.shell.command.CommandMetadata;
import org.jboss.seam.forge.shell.command.OptionMetadata;

public class OptionResolverCompleter implements CommandCompleter
{

   @Override
   public void complete(final CommandCompleterState st)
   {
      PluginCommandCompleterState state = ((PluginCommandCompleterState) st);

      CommandMetadata command = state.getCommand();
      if (command != null)
      {
         if (command.hasOptions())
         {
            List<String> optionCandidates = getOptionCandidates(command, state);
            state.getCandidates().addAll(optionCandidates);
         }
      }
   }

   private List<String> getOptionCandidates(final CommandMetadata command, final PluginCommandCompleterState state)
   {
      Queue<String> tokens = state.getTokens();
      ArrayList<String> results = new ArrayList<String>();
      List<OptionMetadata> options = command.getOptions();
      List<OptionMetadata> removedOptions = new ArrayList<OptionMetadata>();

      for (OptionMetadata option : options)
      {
         if ((tokens.size() == 1) && !tokens.peek().equals("--"))
         {
            String potentialOption = tokens.remove();
            if (potentialOption.startsWith("--"))
            {
               potentialOption = potentialOption.substring(2);
            }

            if (!potentialOption.isEmpty() && option.getName().startsWith(potentialOption))
            {
               String suggestion = "--" + option.getName() + " ";
               results.add(suggestion);
               state.setIndex(state.getIndex() - potentialOption.length() - 2);
               if (state.isFinalTokenComplete())
               {
                  state.setIndex(state.getIndex() - 1);
               }
            }
         }
         else if (tokens.size() > 1)
         {
            String removed = tokens.remove();
            if (removed.startsWith("--"))
            {
               removed = removed.substring(2);
            }

            if (!removed.isEmpty() && option.getName().equals(removed))
            {
               removedOptions.add(option);
            }
         }
      }

      if (!state.hasSuggestions())
      {
         for (OptionMetadata option : options)
         {
            if (option.isNamed() && !removedOptions.contains(option))
            {
               if (option.isRequired())
               {
                  String prefix = "";
                  if (!state.isFinalTokenComplete())
                  {
                     prefix = " ";
                  }
                  results.add(prefix + "--" + option.getName() + " ");
               }
               else if (!option.isRequired() && state.getBuffer().equals(state.getLastBuffer()))
               {
                  // only do optional options if they really want it (double tab)
                  // TODO this state comparison should be state.isRepeat() or something

                  String prefix = "";
                  if (!state.isFinalTokenComplete())
                  {
                     prefix = " ";
                  }
                  results.add(prefix + "--" + option.getName() + " ");
               }
            }
         }
      }
      return results;
   }
}
