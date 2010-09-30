package org.jboss.seam.forge.shell.plugins.builtin;

import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.command.CommandMetadata;
import org.jboss.seam.forge.shell.command.PluginMetadata;
import org.jboss.seam.forge.shell.command.PluginRegistry;
import org.jboss.seam.forge.shell.plugins.DefaultCommand;
import org.jboss.seam.forge.shell.plugins.Help;
import org.jboss.seam.forge.shell.plugins.Plugin;

/**
 * User: christopherbrock Date: 30-Aug-2010 Time: 6:31:57 PM
 */
@Named("list-commands")
@Help("Lists executable shell commands")
public class ListCommandsPlugin implements Plugin
{
   final PluginRegistry registry;
   final Shell shell;

   @Inject
   public ListCommandsPlugin(final PluginRegistry registry, final Shell shell)
   {
      this.registry = registry;
      this.shell = shell;
   }

   @DefaultCommand
   public void listCommands()
   {
      for (PluginMetadata pluginMetaData : registry.getPlugins().values())
      {
         for (CommandMetadata commandMetadata : pluginMetaData.getCommands())
         {
            shell.print(commandMetadata.getName());
            shell.print("  ");
         }
      }
      shell.println();
   }
}
