package org.jboss.seam.forge.shell.completer;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.jboss.seam.forge.shell.command.CommandMetadata;
import org.jboss.seam.forge.shell.command.OptionMetadata;

public class OptionResolverCompleter implements CommandCompleter
{

   @Override
   public void complete(CommandCompleterState st)
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

      // suggest when we have a named param and no value
      for (OptionMetadata option : options)
      {
         if (tokens.size() == 1)
         {
            String potentialOption = tokens.remove();
            if (potentialOption.startsWith("--"))
            {
               potentialOption = potentialOption.substring(2);
            }

            if (option.getName().startsWith(potentialOption))
            {
               results.add("--" + option.getName() + " ");
               state.setIndex(state.getIndex() - potentialOption.length());
            }
         }
         else
         {
            if (!option.isRequired() && state.getBuffer().equals(state.getLastBuffer()))
            {
               if (option.isNamed())
               {
                  String prefix = "";
                  if (!state.isFinalTokenComplete())
                  {
                     prefix = " ";
                  }
                  results.add(prefix + "--" + option.getName() + " ");
               }
               else
               {
                  results.add("");
               }
               break;
            }
         }
      }

      return results;
   }

}
