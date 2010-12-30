package org.jboss.seam.forge.shell.completer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import org.jboss.seam.forge.shell.command.CommandMetadata;
import org.jboss.seam.forge.shell.command.OptionMetadata;
import org.jboss.seam.forge.shell.command.parser.CommandParser;
import org.jboss.seam.forge.shell.command.parser.CommandParserContext;
import org.jboss.seam.forge.shell.command.parser.CompositeCommandParser;
import org.jboss.seam.forge.shell.command.parser.NamedBooleanOptionParser;
import org.jboss.seam.forge.shell.command.parser.NamedValueOptionParser;
import org.jboss.seam.forge.shell.command.parser.NamedValueVarargsOptionParser;
import org.jboss.seam.forge.shell.command.parser.OrderedValueOptionParser;
import org.jboss.seam.forge.shell.command.parser.OrderedValueVarargsOptionParser;

public class OptionResolverCompleter implements CommandCompleter
{

   private final CommandParser commandParser = new CompositeCommandParser(
            new NamedBooleanOptionParser(),
            new NamedValueOptionParser(),
            new NamedValueVarargsOptionParser(),
            new OrderedValueOptionParser(),
            new OrderedValueVarargsOptionParser());

   @Override
   public void complete(final CommandCompleterState st)
   {
      PluginCommandCompleterState state = ((PluginCommandCompleterState) st);

      CommandMetadata command = state.getCommand();
      if (command != null)
      {
         if (command.hasOptions())
         {
            getOptionCandidates(command, state);
         }
      }
   }

   private void getOptionCandidates(final CommandMetadata command, final PluginCommandCompleterState state)
   {
      ArrayList<String> results = new ArrayList<String>();
      Map<OptionMetadata, Object> optionValueMap = commandParser.parse(state.getCommand(), state.getTokens(),
               new CommandParserContext());
      List<OptionMetadata> options = command.getOptions();

      Queue<String> tokens = state.getOriginalTokens();
      if (tokens.size() > 0)
      {
         while (tokens.size() > 1)
         {
            tokens.remove();
         }

         String finalToken = tokens.peek();
         int finalTokenIndex = state.getBuffer().lastIndexOf(finalToken);

         boolean tailOptionValued = true;
         boolean finalTokenIsValue = false;
         if (!finalToken.startsWith("-"))
         {
            finalTokenIsValue = true;
            finalTokenIndex = state.getIndex();
         }
         else
         {
            boolean shortOption = finalToken.matches("^-[^\\-]+$")
                     && (finalToken.length() > 1);
            finalToken = finalToken.replaceFirst("^[-]+", "");
            for (Entry<OptionMetadata, Object> entry : optionValueMap.entrySet())
            {
               if (entry.getValue() == null)
               {
                  tailOptionValued = false;
               }
            }

            if (tailOptionValued)
            {
               for (OptionMetadata option : options)
               {
                  if (option.isNamed())
                  {
                     if (option.getName().equals(finalToken) && optionValueMap.containsKey(option))
                     {
                        if (!state.isFinalTokenComplete())
                        {
                           results.add(" ");
                        }
                        break;
                     }
                     if (option.getName().startsWith(finalToken) && !optionValueMap.containsKey(option))
                     {
                        results.add("--" + option.getName() + " ");
                     }
                  }
               }
            }
         }

         if (!results.isEmpty())
         {
            tokens.remove();
         }

         /*
          * If we haven't gotten any suggestions yet, then we just need to add everything we havent seen yet
          */
         if (results.isEmpty() && tailOptionValued)
         {
            for (OptionMetadata option : options)
            {
               if (option.isNamed() && !optionValueMap.containsKey(option))
               {
                  results.add("--" + option.getName() + " ");
               }
            }
         }

         // add to state
         if (!state.isFinalTokenComplete() && finalTokenIsValue)
         {
            results.clear();
            if (state.getBuffer().equals(state.getLastBuffer()))
            {
               // wait until the buffer remains the same until we append a space
               results.add(" ");
            }
         }
         else if (!results.isEmpty() && tailOptionValued)
         {
            state.setIndex(finalTokenIndex);
         }

         state.getCandidates().addAll(results);
      }
   }
}
