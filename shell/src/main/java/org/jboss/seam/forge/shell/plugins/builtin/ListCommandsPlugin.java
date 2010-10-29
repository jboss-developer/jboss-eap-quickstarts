package org.jboss.seam.forge.shell.plugins.builtin;

import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.command.CommandMetadata;
import org.jboss.seam.forge.shell.command.PluginMetadata;
import org.jboss.seam.forge.shell.command.PluginRegistry;
import org.jboss.seam.forge.shell.plugins.DefaultCommand;
import org.jboss.seam.forge.shell.plugins.Help;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.shell.util.GeneralUtils;

import java.util.ArrayList;
import java.util.List;

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
   public void listCommands(@Option(name = "all", shortName = "a", flagOnly = true) boolean showAll)
   {
      List<String> listData = new ArrayList<String>();
      Class currResource = shell.getCurrentResource().getClass();
      for (PluginMetadata pluginMetaData : registry.getPlugins().values())
      {
         for (CommandMetadata commandMetadata : pluginMetaData.getCommands())
         {
            render(listData, showAll, currResource, commandMetadata);
         }
      }

      GeneralUtils.printOutColumns(listData, shell, true);
   }

   private static void render(List<String> listData, boolean showAll, Class currResource, CommandMetadata cmdMeta)
   {
      boolean contextual = cmdMeta.usableWithScope(currResource);

      if (showAll)
      {
         if (!cmdMeta.isDefault())
         {
            listData.add((contextual ? "*" : "") + cmdMeta.getPluginMetadata().getName() + ":" + cmdMeta.getName());
         }
         else
         {
            listData.add((contextual ? "*" : "") + cmdMeta.getName());
         }
      }
      else if (contextual)
      {
         listData.add(cmdMeta.getName());
      }
   }
}
