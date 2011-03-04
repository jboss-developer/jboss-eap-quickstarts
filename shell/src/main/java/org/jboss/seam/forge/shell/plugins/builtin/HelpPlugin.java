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

package org.jboss.seam.forge.shell.plugins.builtin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.inject.Inject;

import org.jboss.seam.forge.parser.java.util.Strings;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.ShellColor;
import org.jboss.seam.forge.shell.ShellMessages;
import org.jboss.seam.forge.shell.command.CommandMetadata;
import org.jboss.seam.forge.shell.command.OptionMetadata;
import org.jboss.seam.forge.shell.command.PluginMetadata;
import org.jboss.seam.forge.shell.command.PluginRegistry;
import org.jboss.seam.forge.shell.plugins.Alias;
import org.jboss.seam.forge.shell.plugins.DefaultCommand;
import org.jboss.seam.forge.shell.plugins.Help;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.PipeOut;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.shell.plugins.Topic;
import org.jboss.seam.forge.shell.util.GeneralUtils;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Alias("help")
@Topic("Shell Environment")
@Help("Displays help text for specified plugins & commands.")
public class HelpPlugin implements Plugin
{
   private final PluginRegistry registry;
   private final Shell shell;

   @Inject
   public HelpPlugin(final Shell shell, final PluginRegistry registry)
   {
      this.shell = shell;
      this.registry = registry;
   }

   @DefaultCommand
   public void help(@Option final String[] tokens,
            @Option(name = "all", shortName = "a") final boolean all,
            final PipeOut out)
   {
      Queue<String> q = new LinkedList<String>(Arrays.asList(tokens == null ? new String[] {} : tokens));

      if (q.isEmpty())
      {
         printGeneralHelp(out);
      }
      else
      {
         Map<String, List<PluginMetadata>> plugins = registry.getPlugins();
         String tok = q.remove();

         List<PluginMetadata> list = plugins.get(tok.trim());
         if (q.isEmpty())
         {
            printAllMessage(all, out, tok, list);

            if (all)
            {
               for (PluginMetadata p : list)
               {
                  printPlugin(out, p, all);
               }
            }
            else
            {
               PluginMetadata p = registry.getPluginMetadataForScopeAndConstraints(tok, shell);
               if (p == null)
               {
                  p = list.get(0);
               }
               printPlugin(out, p, all);
            }
         }

         if (!q.isEmpty())
         {
            PluginMetadata p = registry.getPluginMetadataForScopeAndConstraints(tok, shell);
            if (p == null)
            {
               p = list.get(0);
            }

            tok = q.remove();

            out.println();
            if (p.hasCommand(tok, shell))
            {
               CommandMetadata c = p.getCommand(tok);
               out.print(ShellColor.BOLD, "[" + p.getName() + " " + c.getName() + "] ");
               out.println("- "
                        + (!Strings.isNullOrEmpty(c.getHelp()) ? c.getHelp() : out.renderColor(ShellColor.ITALIC,
                                 "no help text available")));

               printOptions(out, c);
            }
            else
            {
               out.println("No such command [" + tok + "] for the active Resource scope.");
            }
            out.println();
         }
      }

   }

   private void printAllMessage(final boolean all, final PipeOut out, final String tok, final List<PluginMetadata> list)
   {
      if (list.size() > 1)
      {
         if (all)
         {
            ShellMessages.info(out, "The plugin [" + tok
                     + "] is overloaded. Listing all candidates and their corresponding Resource scopes.");
         }
         else
         {
            ShellMessages
                     .info(out,
                              "The plugin ["
                                       + tok
                                       + "] is overloaded. Showing only the plugin for the first or active scope. Re-run with "
                                       + out.renderColor(ShellColor.BOLD, "'--all'") + " to display all scopes.");
         }
      }
   }

   private void printPlugin(final PipeOut out, final PluginMetadata p, final boolean all)
   {
      out.println();
      out.println(out.renderColor(ShellColor.BOLD, "[" + p.getName() + "]")
               + " - "
               + (Strings.isNullOrEmpty(p.getHelp()) ? out.renderColor(ShellColor.ITALIC, "no help text available") : p
                        .getHelp()));
      if (!p.getResourceScopes().isEmpty() && all)
      {
         out.println(p.getResourceScopes().toString());
      }

      if (p.hasDefaultCommand())
      {
         CommandMetadata def = p.getDefaultCommand();
         printOptions(out, def);
      }

      List<CommandMetadata> commands = p.getCommands();
      if (commands.size() > 1)
      {
         out.println();
         out.println(ShellColor.RED, "[COMMANDS]");
         List<String> commandNames = new ArrayList<String>();
         for (CommandMetadata c : commands)
         {
            if (!c.isDefault())
               commandNames.add(c.getName());
         }
         GeneralUtils.printOutColumns(commandNames, out, shell, true);
      }

      out.println();
   }

   private void printOptions(final PipeOut out, final CommandMetadata def)
   {
      List<OptionMetadata> options = def.getOptions();

      if (!options.isEmpty())
      {
         out.println();
         out.println(ShellColor.RED, "[OPTIONS]");

         int i = 1;
         for (OptionMetadata opt : options)
         {
            if (opt.isOrdered())
            {
               out.print(ShellColor.BOLD, "\t[" + (opt.isVarargs() ? "Args..." : "Arg #" + i) + "]");
               out.print(Strings.isNullOrEmpty(opt.getDescription()) ? " - " : " - " + opt.getDescription() + " - ");
               out.println((!Strings.isNullOrEmpty(opt.getHelp()) ? opt.getHelp() : out.renderColor(ShellColor.ITALIC,
                        "no help text available")));
               i++;
            }
         }

         for (OptionMetadata opt : options)
         {
            if (opt.isNamed())
            {
               out.print(ShellColor.BOLD, "\t[--" + opt.getName());
               if (!Strings.isNullOrEmpty(opt.getShortName()))
               {
                  out.print(", " + out.renderColor(ShellColor.BOLD, "-" + opt.getShortName()));
               }
               out.print(ShellColor.BOLD, "] ");
               out.println(opt.getDescription());
            }
         }
      }
   }

   private void printGeneralHelp(final PipeOut out)
   {
      out.println("Welcome to " + out.renderColor(ShellColor.YELLOW, "Seam Forge") + ", a next-generation " +
               "interactive Shell and project-generation tool. If you find yourself lost, or uncertain how " +
               "to complete an operation, you may press the " +
               out.renderColor(ShellColor.BOLD, "<TAB>") + " key for command-completion, or " +
               out.renderColor(ShellColor.BOLD, "<TAB><TAB>") + " for hints while typing a command.");

      out.println();
      out.println("Type " + out.renderColor(ShellColor.BOLD, "'list-commands'") + " for a list of available " +
               "commands in the current Resource context.");

      out.println();
      Project currentProject = shell.getCurrentProject();
      if (currentProject != null)
      {
         out.println("Currently operating on the Project located at ["
                  + currentProject.getProjectRoot().getFullyQualifiedName() + "]");
      }
      else
      {
         out.println("You are not working on a project. Type " + out.renderColor(ShellColor.BOLD, "'help new-project'")
                  + " to get started.");
      }
   }

}
