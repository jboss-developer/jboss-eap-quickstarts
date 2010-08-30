package org.jboss.seam.sidekick.shell.cli.builtin;

import org.jboss.seam.sidekick.shell.Shell;
import org.jboss.seam.sidekick.shell.cli.CommandMetadata;
import org.jboss.seam.sidekick.shell.cli.PluginMetadata;
import org.jboss.seam.sidekick.shell.cli.PluginRegistry;
import org.jboss.seam.sidekick.shell.plugins.plugins.DefaultCommand;
import org.jboss.seam.sidekick.shell.plugins.plugins.Help;
import org.jboss.seam.sidekick.shell.plugins.plugins.Plugin;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * User: christopherbrock
 * Date: 30-Aug-2010
 * Time: 6:31:57 PM
 */
@Named("list-commands")
@Help("Lists executable shell commands")
public class ListCommandsPlugin implements Plugin
{
   @Inject
   PluginRegistry registry;

   @Inject
   Shell shell;

   @DefaultCommand                                                                                 
   public void listCommands()
   {
      for (PluginMetadata pluginMetaData : registry.getPlugins().values())
      {
         for (CommandMetadata commandMetadata : pluginMetaData.getCommands())
         {
            for (String commandName : commandMetadata.getNames())
            {
               shell.print(commandName);
               shell.print("  ");
            }
         }
      }
      shell.println();
   }
}
