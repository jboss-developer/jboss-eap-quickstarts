/*
 * JBoss, Home of Professional Open Source
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
package org.jboss.seam.sidekick.shell;

import java.io.IOException;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;

import jline.console.ConsoleReader;
import jline.console.completer.Completer;

import org.jboss.seam.sidekick.project.ProjectModelException;
import org.jboss.seam.sidekick.project.model.MavenProject;
import org.jboss.seam.sidekick.shell.cli.Execution;
import org.jboss.seam.sidekick.shell.cli.ExecutionParser;
import org.jboss.seam.sidekick.shell.exceptions.CommandException;
import org.jboss.seam.sidekick.shell.exceptions.CommandExecutionException;
import org.jboss.seam.sidekick.shell.plugins.events.AcceptUserInput;
import org.jboss.seam.sidekick.shell.plugins.events.Shutdown;
import org.jboss.seam.sidekick.shell.plugins.events.Startup;
import org.jboss.weld.environment.se.bindings.Parameters;
import org.slf4j.Logger;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@Singleton
public class ShellImpl implements Shell
{
   private String prompt = "sidekick> ";

   @Inject
   @Parameters
   private List<String> parameters;

   @Inject
   Logger log;

   @Inject
   private Completer completer;
   @Inject
   private ExecutionParser parser;

   private ConsoleReader reader;
   private boolean exitRequested = false;

   private boolean verbose = false;

   void init(@Observes final Startup event, final Instance<MavenProject> projectInstance) throws Exception
   {
      log.info("Seam Sidekick Shell - Starting up.");

      System.out.println("                                    _          _ _ ");
      System.out.println("     ___  ___  __ _ _ __ ___    ___| |__   ___| | |");
      System.out.println("    / __|/ _ \\/ _` | '_ ` _ \\  / __| '_ \\ / _ \\ | |   \\\\");
      System.out.println("    \\__ \\  __/ (_| | | | | | | \\__ \\ | | |  __/ | |   //");
      System.out.println("    |___/\\___|\\__,_|_| |_| |_| |___/_| |_|\\___|_|_|");
      System.out.println("");

      try
      {
         MavenProject currentProject = projectInstance.get();
         if (currentProject.exists())
         {
            prompt = currentProject.getPOM().getArtifactId() + "> ";
         }
      }
      catch (ProjectModelException e)
      {
         log.warn("No active projects were detected in this directory. Shell is booting standalone.");
      }

      reader = new ConsoleReader();
      reader.setHistoryEnabled(true);
      reader.setPrompt(prompt);
      reader.addCompleter(completer);

      if (parameters.contains("--verbose"))
      {
         verbose = true;
      }

   }

   void doShell(@Observes final AcceptUserInput event)
   {
      String line;
      try
      {
         while ((exitRequested != true) && ((line = reader.readLine()) != null))
         {
            if ("".equals(line))
            {
               continue;
            }

            execute(line);
         }
      }
      catch (IOException e)
      {
         throw new IllegalStateException("Shell input stream failure", e);
      }
   }

   private void execute(final String line)
   {
      Execution execution = null;
      try
      {
         execution = parser.parse(line);
         execution.perform();
      }
      catch (CommandExecutionException e)
      {
         write(e.getMessage());
         if (true)
         {
            e.printStackTrace();
         }
      }
      catch (CommandException e)
      {
         e.printStackTrace();
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }

   void teardown(@Observes final Shutdown event)
   {
      exitRequested = true;
   }

   /**
    * Prompt the user for input, using {@link message} as the prompt text.
    */
   @Override
   public String prompt(final String message)
   {
      write(message);
      try
      {
         String currentPrompt = reader.getPrompt();
         reader.setPrompt(" ");
         reader.removeCompleter(completer);
         String line = reader.readLine();
         reader.setPrompt(currentPrompt);
         reader.addCompleter(completer);
         return line;
      }
      catch (IOException e)
      {
         throw new IllegalStateException("Shell input stream failure", e);
      }
   }

   /**
    * Write the given {@link line} to the console output.
    */
   @Override
   public void write(final String line)
   {
      System.out.println(line);
   }

   @Override
   public String prompt()
   {
      return prompt("");
   }

   @Override
   public void clear()
   {
      try
      {
         String currentPrompt = reader.getPrompt();
         reader.setPrompt("");
         reader.clearScreen();
         reader.setPrompt(currentPrompt);
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
   }
}