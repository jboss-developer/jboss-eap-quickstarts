package org.jboss.seam.forge.shell.completer;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import javax.inject.Inject;

import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.command.CommandMetadata;
import org.jboss.seam.forge.shell.command.PluginMetadata;

public class CommandResolverCompleter implements CommandCompleter
{
   @Inject
   private Shell shell;

   @Override
   public void complete(final CommandCompleterState st)
   {
      PluginCommandCompleterState state = ((PluginCommandCompleterState) st);

      if (state.getPlugin() != null)
      {
         Queue<String> tokens = state.getTokens();

         state.setIndex(state.getBuffer().length());
         PluginMetadata plugin = state.getPlugin();
         if (tokens.size() > 1)
         {
            // there must be a command, or a string of arguments for the
            // default command
            String peek = tokens.peek();
            if (plugin.hasCommand(peek, shell))
            {
               CommandMetadata command = plugin.getCommand(tokens.remove());
               state.setCommand(command);

               // TODO this should probably be tokenComplete`?` sensitive
               // complete the command, remove the last token
            }
            else if (plugin.hasDefaultCommand())
            {
               CommandMetadata defaultCommand = plugin.getDefaultCommand();
               state.setCommand(defaultCommand);

            }
            else
            {
               // bad input, not a command and there is no default command
            }
         }
         else if (!tokens.isEmpty())
         {
            // just one more token, it's either a command or an argument
            // for the default command
            String peek = tokens.peek();
            if (plugin.hasCommand(peek, shell))
            {
               CommandMetadata command = plugin.getCommand(tokens.remove());
               state.setCommand(command);
            }
            else if (couldBeCommand(plugin, peek))
            {
               state.setIndex(state.getBuffer().indexOf(peek));
               addCommandCandidates(plugin, state);
            }
         }

         if (state.getCommand() == null)
         {
            if (plugin.hasDefaultCommand())
            {
               CommandMetadata defaultCommand = plugin.getDefaultCommand();
               state.setCommand(defaultCommand);
            }
            else if (plugin.hasCommands())
            {
               addCommandCandidates(plugin, state);
            }
         }
      }
   }

   /**
    * Add command completions for the given plugin, with or without tokens
    */
   private void addCommandCandidates(final PluginMetadata plugin, final PluginCommandCompleterState state)
   {
      Queue<String> tokens = state.getTokens();
      List<String> results = new ArrayList<String>();
      if (plugin.hasCommands())
      {
         List<CommandMetadata> commands = plugin.getCommands(shell);
         if (tokens.isEmpty())
         {
            for (CommandMetadata command : commands)
            {
               if (!command.isDefault())
               {
                  results.add(command.getName() + " ");
               }
            }
         }
         else
         {
            String pluginBase = tokens.remove();
            for (CommandMetadata command : commands)
            {
               if (!command.isDefault() && PluginCommandCompleter.isPotentialMatch(command.getName(), pluginBase))
               {
                  results.add(command.getName() + " ");
               }
            }
         }
      }
      state.getCandidates().addAll(results);
   }

   private boolean couldBeCommand(final PluginMetadata plugin, final String potentialCommand)
   {
      List<CommandMetadata> commands = plugin.getCommands(shell);
      if ((commands != null) && !commands.isEmpty())
      {
         for (CommandMetadata commandMetadata : commands)
         {
            if (!commandMetadata.isDefault()
                     && PluginCommandCompleter.isPotentialMatch(commandMetadata.getName(), potentialCommand))
            {
               return true;
            }
         }
      }
      return false;
   }

}
