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

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

import jline.console.ConsoleReader;
import jline.console.completer.Completer;

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
   private Logger log;

   @Inject
   private Completer completer;
   @Inject
   private ExecutionParser parser;

   private ConsoleReader reader;

   private MavenProject currentProject;

   private boolean verbose = false;
   private boolean pretend = false;
   private boolean exitRequested = false;

   @Inject
   private Event<Shutdown> shutdown;

   void init(@Observes final Startup event) throws Exception
   {
      log.info("Seam Sidekick Shell - Starting up.");

      setReader(new ConsoleReader());
      initParameters();
      printWelcomeBanner();
      initProject();

   }

   private void initParameters()
   {
      if (parameters.contains("--verbose"))
      {
         verbose = true;
      }
      if (parameters.contains("--pretend"))
      {
         pretend = true;
      }
   }

   private void printWelcomeBanner()
   {
      System.out.println("                                    _          _ _ ");
      System.out.println("     ___  ___  __ _ _ __ ___    ___| |__   ___| | |");
      System.out.println("    / __|/ _ \\/ _` | '_ ` _ \\  / __| '_ \\ / _ \\ | |   \\\\");
      System.out.println("    \\__ \\  __/ (_| | | | | | | \\__ \\ | | |  __/ | |   //");
      System.out.println("    |___/\\___|\\__,_|_| |_| |_| |___/_| |_|\\___|_|_|");
      System.out.println("");
   }

   private void initProject()
   {
      writeVerbose("Parameters: " + parameters);

      String projectPath = "";
      if ((parameters != null) && !parameters.isEmpty())
      {
         for (String path : parameters)
         {
            if ((path != null) && !path.startsWith("--") && !path.startsWith("-"))
            {
               projectPath = path;
               break;
            }
         }
      }

      File targetDirectory = new File(projectPath).getAbsoluteFile();
      writeVerbose("Using project path: [" + targetDirectory.getAbsolutePath() + "]");

      if (targetDirectory.exists())
      {
         currentProject = new MavenProject(targetDirectory);
         if (currentProject.exists())
         {
            prompt = currentProject.getPOM().getArtifactId() + "> ";
            reader.setPrompt(prompt);
         }
      }
      else
      {
         write("The directory [" + targetDirectory.getAbsolutePath() + "] does not exist. Exiting...");
         shutdown.fire(Shutdown.ERROR);
      }
   }

   void teardown(@Observes final Shutdown event)
   {
      exitRequested = true;
   }

   @Produces
   @Default
   public MavenProject getCurrentProject()
   {
      return currentProject;
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
         if (verbose)
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

   @Override
   public boolean promptBoolean(final String message)
   {
      return promptBoolean(message, true);
   }

   @Override
   public boolean promptBoolean(final String message, final boolean defaultIfEmpty)
   {
      String query = "[Y/n]";
      if (!defaultIfEmpty)
      {
         query = "[y/N]";
      }

      String input = "";
      do
      {
         input = prompt(message + " " + query);
         if (input != null)
         {
            input = input.trim();
         }
      }
      while ((input != null) && !input.matches("(?i)^((y(es?)?)|(no?))?$"));

      boolean result = defaultIfEmpty;
      if (input == null)
      {
         // do nothing
      }
      else if (input.matches("(?i)(no?)"))
      {
         result = false;
      }
      else if (input.matches("(?i)(y(es?)?)"))
      {
         result = true;
      }

      return result;
   }

   @Override
   public void write(final String line)
   {
      System.out.println(line);
   }

   @Override
   public void writeVerbose(final String line)
   {
      if (verbose)
      {
         System.out.println(line);
      }
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

   @Override
   public boolean isVerbose()
   {
      return verbose;
   }

   @Override
   public boolean isPretend()
   {
      return pretend;
   }

   /**
    * Set the parameters as if they had been received from the command line.
    */
   public void setParameters(final List<String> parameters)
   {
      this.parameters = parameters;
   }

   public void setReader(final ConsoleReader reader)
   {
      this.reader = reader;
      this.reader.setHistoryEnabled(true);
      this.reader.setPrompt(prompt);
      this.reader.addCompleter(completer);
   }

   public ConsoleReader getReader()
   {
      return reader;
   }
}