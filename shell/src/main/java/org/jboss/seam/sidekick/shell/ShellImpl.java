/*
 * JBoss, Home of Professional Open Sourci
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

import jline.console.ConsoleReader;
import jline.console.completer.Completer;
import org.jboss.seam.sidekick.project.model.MavenProject;
import org.jboss.seam.sidekick.shell.cli.Execution;
import org.jboss.seam.sidekick.shell.cli.ExecutionParser;
import org.jboss.seam.sidekick.shell.exceptions.*;
import org.jboss.seam.sidekick.shell.plugins.events.AcceptUserInput;
import org.jboss.seam.sidekick.shell.plugins.events.Shutdown;
import org.jboss.seam.sidekick.shell.plugins.events.Startup;
import org.jboss.seam.sidekick.shell.util.BooleanConverter;
import org.jboss.weld.environment.se.bindings.Parameters;
import org.mvel2.DataConversion;
import org.mvel2.MVEL;
import org.mvel2.PropertyAccessException;
import org.slf4j.Logger;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
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

   private Map<String, Object> properties = new HashMap<String, Object>();


   void init(@Observes final Startup event) throws Exception
   {
      log.info("Seam Sidekick Shell - Starting up.");

      BooleanConverter booleanConverter = new BooleanConverter();
      DataConversion.addConversionHandler(boolean.class, booleanConverter);
      DataConversion.addConversionHandler(Boolean.class, booleanConverter);

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
      printlnVerbose("Parameters: " + parameters);

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
      printlnVerbose("Using project path: [" + targetDirectory.getAbsolutePath() + "]");

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
         println("The directory [" + targetDirectory.getAbsolutePath() + "] does not exist. Exiting...");
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
         // main program loop
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

   public void execute(final String line)
   {
      Execution execution;
      try
      {
         execution = parser.parse(line);
         execution.perform();
      }
      catch (NoSuchCommandException e)
      {
         String s = execScript(line);
         if (s.length() != 0)
         {
            println(s);
         }
      }
      catch (CommandExecutionException e)
      {
         println("[" + e.getCommand() + "] " + e.getMessage());
         if (verbose)
         {
            e.printStackTrace();
         }
      }
      catch (CommandParserException e)
      {
         println("[" + e.getCommand() + "] " + e.getMessage());
         if (verbose)
         {
            e.printStackTrace();
         }
      }
      catch (PluginExecutionException e)
      {
         println("[" + e.getPlugin() + "] " + e.getMessage());
         if (verbose)
         {
            e.printStackTrace();
         }
      }
      catch (ShellExecutionException e)
      {
         println(e.getMessage());
         if (verbose)
         {
            e.printStackTrace();
         }
      }
      catch (Exception e)
      {
         if (!verbose)
         {
            println("Exception encountered: " + e.getMessage() + " (type \"verbose on\" to enable stack traces)");
         }
         else
         {
            println("Exception encountered: (type \"verbose false\" to disable stack traces)");
            e.printStackTrace();
         }
      }
   }

   private String execScript(String script)
   {
      String[] tokens = script.split("\\s");


      try
      {
         Object retVal = MVEL.eval(script, new ScriptContext(), properties);
         if (retVal != null)
         {
            return String.valueOf(retVal);
         }
      }
      catch (PropertyAccessException e)
      {
         println(tokens[0] + ": command or property not found.");

         if (verbose)
         {
            e.printStackTrace();
         }
      }
      catch (Exception e)
      {
         if (tokens.length == 1)
         {
            println(tokens[0] + ": command or property not found.");
         }
         else
         {
            println(tokens[0] + ": error executing statement: " + e.getMessage());
         }
         
         if (verbose)
         {
            e.printStackTrace();
         }
      }

      return "";
   }

   public class ScriptContext
   {
      public void cmd(String cmd)
      {
         execute(cmd);
      }
   }

   /**
    * Prompt the user for input, using
    *
    * @param message as the prompt text.
    */
   @Override
   public String prompt(final String message)
   {
      print(message);
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
   public void printlnVerbose(final String line)
   {
      if (verbose)
      {
         System.out.println(line);
      }
   }

   @Override
   public void print(final String output)
   {
      System.out.print(output);
   }

   @Override
   public void println(final String output)
   {
      System.out.println(output);
   }

   @Override
   public void println()
   {
      System.out.println();
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
   public void setVerbose(final boolean verbose)
   {
      this.verbose = verbose;
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

   @Override
   public void setProperty(String name, Object value)
   {
      properties.put(name, value);
   }

   @Override
   public Object getProperty(String name)
   {
      return properties.get(name);
   }

   @Override
   public Map<String, Object> getProperties()
   {
      return properties;
   }
}