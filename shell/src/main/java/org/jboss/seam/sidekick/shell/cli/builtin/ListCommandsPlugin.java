package org.jboss.seam.sidekick.shell.cli.builtin;

import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.sidekick.shell.Shell;
import org.jboss.seam.sidekick.shell.cli.CommandMetadata;
import org.jboss.seam.sidekick.shell.cli.PluginMetadata;
import org.jboss.seam.sidekick.shell.cli.PluginRegistry;
import org.jboss.seam.sidekick.shell.plugins.plugins.DefaultCommand;
import org.jboss.seam.sidekick.shell.plugins.plugins.Help;
import org.jboss.seam.sidekick.shell.plugins.plugins.Option;
import org.jboss.seam.sidekick.shell.plugins.plugins.Plugin;

/**
 * User: christopherbrock Date: 30-Aug-2010 Time: 6:31:57 PM
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
   public void listCommands(@Option(required = false) final String pluginName)
   {
      for (PluginMetadata plugin : registry.getPlugins().values())
      {
         for (CommandMetadata command : plugin.getCommands())
         {
            if (command.isDefault())
            {
               shell.println(plugin.getName() + "*");
            }
            else
            {
               shell.println(pluginName + " " + command.getName());
            }
         }
      }
      shell.println();
   }
}
