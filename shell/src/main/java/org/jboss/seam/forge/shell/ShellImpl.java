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
package org.jboss.seam.forge.shell;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import jline.console.ConsoleReader;
import jline.console.completer.AggregateCompleter;
import jline.console.completer.Completer;

import org.jboss.seam.forge.project.facets.MavenFacet;
import org.jboss.seam.forge.shell.PromptType;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.command.Execution;
import org.jboss.seam.forge.shell.command.ExecutionParser;
import org.jboss.seam.forge.shell.command.convert.BooleanConverter;
import org.jboss.seam.forge.shell.command.convert.FileConverter;
import org.jboss.seam.forge.shell.completer.CommandCompleter;
import org.jboss.seam.forge.shell.completer.CommandCompleterAdaptor;
import org.jboss.seam.forge.shell.completer.FileOptionCompleter;
import org.jboss.seam.forge.shell.completer.PluginCommandCompleter;
import org.jboss.seam.forge.shell.exceptions.CommandExecutionException;
import org.jboss.seam.forge.shell.exceptions.CommandParserException;
import org.jboss.seam.forge.shell.exceptions.NoSuchCommandException;
import org.jboss.seam.forge.shell.exceptions.PluginExecutionException;
import org.jboss.seam.forge.shell.exceptions.ShellException;
import org.jboss.seam.forge.shell.exceptions.ShellExecutionException;
import org.jboss.seam.forge.shell.plugins.events.AcceptUserInput;
import org.jboss.seam.forge.shell.plugins.events.PostStartup;
import org.jboss.seam.forge.shell.plugins.events.Shutdown;
import org.jboss.seam.forge.shell.plugins.events.Startup;
import org.jboss.seam.forge.shell.util.Files;
import org.jboss.weld.environment.se.bindings.Parameters;
import org.mvel2.DataConversion;
import org.mvel2.MVEL;
import org.mvel2.PropertyAccessException;
import org.slf4j.Logger;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Singleton
public class ShellImpl implements Shell
{
   private static final String PROP_CWD = "$CWD";
   private static final String PROP_PROMPT = "$PROMPT";
   private final Map<String, Object> properties = new HashMap<String, Object>();
   private static final Pattern validCommand = Pattern.compile("^[a-zA-Z0-9\\-_]{0,}$");

   @Inject
   @Parameters
   private List<String> parameters;

   @Inject
   private Logger log;

   @Inject
   private ExecutionParser parser;

   @Inject
   private Event<PostStartup> postStartup;

   @Inject
   private CurrentProjectHolder cph;

   private ConsoleReader reader;
   private Completer completer;

   private boolean verbose = false;
   private boolean pretend = false;
   private boolean exitRequested = false;

   private InputStream inputStream;
   private Writer outputWriter;

   void init(@Observes final Startup event, final PluginCommandCompleter pluginCompleter) throws Exception
   {
      log.info("Seam Forgeell - Starting up.");

      BooleanConverter booleanConverter = new BooleanConverter();
      DataConversion.addConversionHandler(boolean.class, booleanConverter);
      DataConversion.addConversionHandler(Boolean.class, booleanConverter);
      DataConversion.addConversionHandler(File.class, new FileConverter());

      initStreams();
      initCompleters(pluginCompleter);
      initParameters();
      printWelcomeBanner();

      postStartup.fire(new PostStartup());
   }

   private void initCompleters(final PluginCommandCompleter pluginCompleter)
   {
      List<Completer> completers = new ArrayList<Completer>();
      completers.add(new CommandCompleterAdaptor(pluginCompleter));
      completers.add(new FileOptionCompleter(this));

      completer = new AggregateCompleter(completers);
      this.reader.addCompleter(completer);
   }

   private void initStreams() throws IOException
   {
      if (inputStream == null)
      {
         inputStream = System.in;
      }
      if (outputWriter == null)
      {
         outputWriter = new PrintWriter(System.out);
      }
      this.reader = new ConsoleReader(inputStream, outputWriter);
      this.reader.setHistoryEnabled(true);
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

      String projectPath = null;
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

      File targetDirectory = null;
      if (projectPath == null)
      {
         targetDirectory = getCurrentDirectory();
      }
      else
      {
         targetDirectory = new File(projectPath).getAbsoluteFile();
      }
      setCurrentDirectory(targetDirectory);
   }

   private void printWelcomeBanner()
   {
      System.out.println("   ____                          _____                    ");
      System.out.println("  / ___|  ___  __ _ _ __ ___    |  ___|__  _ __ __ _  ___ ");
      System.out.println("  \\___ \\ / _ \\/ _` | '_ ` _ \\   | |_ / _ \\| '__/ _` |/ _ \\  \\\\");
      System.out.println("   ___) |  __/ (_| | | | | | |  |  _| (_) | | | (_| |  __/  //");
      System.out.println("  |____/ \\___|\\__,_|_| |_| |_|  |_|  \\___/|_|  \\__, |\\___| ");
      System.out.println("                                                |___/      ");
      System.out.println("");
   }

   void teardown(@Observes final Shutdown event)
   {
      exitRequested = true;
   }

   void doShell(@Observes final AcceptUserInput event)
   {
      String line = "";
      try
      {
         reader.setPrompt(getPrompt());
         while ((exitRequested != true) && ((line = readLine()) != null))
         {
            if (!"".equals(line))
            {
               execute(line);
            }
            reader.setPrompt(getPrompt());
         }
         println();
      }
      catch (IOException e)
      {
         throw new IllegalStateException("Shell input stream failure", e);
      }
   }

   @Override
   public String readLine() throws IOException
   {
      return reader.readLine();
   }

   @Override
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

   private String execScript(final String script)
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
         if (!validCommand.matcher(tokens[0]).matches())
         {
            println("error executing command:\n" + e.getMessage());
         }
         else if (tokens.length == 1)
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
      public void shell(final String cmd)
      {
         execute(cmd);
      }

      public String time()
      {
         return new SimpleDateFormat("hh:mm").format(new Date());
      }
   }

   /*
    * Shell Print Methods
    */
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
   public void clear()
   {
      try
      {
         reader.clearScreen();
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

   @Override
   public void setInputStream(final InputStream is) throws IOException
   {
      this.inputStream = is;
      initStreams();
   }

   @Override
   public void setOutputWriter(final Writer os) throws IOException
   {
      this.outputWriter = os;
      initStreams();
   }

   @Override
   public void setProperty(final String name, final Object value)
   {
      properties.put(name, value);
   }

   @Override
   public Object getProperty(final String name)
   {
      return properties.get(name);
   }

   @Override
   public Map<String, Object> getProperties()
   {
      return properties;
   }

   @Override
   public void setDefaultPrompt()
   {
      setPrompt("");
   }

   @Override
   public void setPrompt(final String prompt)
   {
      setProperty(PROP_PROMPT, prompt);
   }

   @Override
   public String getPrompt()
   {
      // TODO this needs to be refactored
      String suffix = "[no project]";
      if (cph.getCurrentProject() != null)
      {
         // FIXME eventually, this cannot reference the MavenFacet directly.
         suffix = "[" + cph.getCurrentProject().getFacet(MavenFacet.class).getPOM().getArtifactId() + "]";
      }
      String path = getCurrentDirectory().getAbsolutePath();
      return suffix + " " + path + " $ ";
   }

   @Override
   public File getCurrentDirectory()
   {
      String cwd = (String) getProperty(PROP_CWD);
      if (cwd == null)
      {
         cwd = "";
      }
      return new File(cwd);
   }

   @Override
   public void setCurrentDirectory(final File directory)
   {
      try
      {
         setProperty(PROP_CWD, directory.getCanonicalPath());
      }
      catch (IOException e)
      {
         throw new ShellException(e);
      }
   }

   /*
    * Shell Prompts
    */
   @Override
   public String prompt()
   {
      return prompt("");
   }

   @Override
   public String prompt(final String message)
   {
      return promptWithCompleter(message, (Completer) null);
   }

   private String promptWithCompleter(final String message, final CommandCompleter completer)
   {
      Completer tempCompleter = new CommandCompleterAdaptor(completer);
      return promptWithCompleter(message, tempCompleter);
   }

   private String promptWithCompleter(String message, final Completer tempCompleter)
   {
      if (!message.isEmpty() && message.matches("^.*\\S$"))
      {
         message = message + " ";
      }

      try
      {
         reader.removeCompleter(this.completer);
         reader.addCompleter(tempCompleter);
         reader.setHistoryEnabled(false);
         reader.setPrompt(message);
         String line = readLine();
         reader.removeCompleter(tempCompleter);
         reader.addCompleter(this.completer);
         reader.setHistoryEnabled(true);
         reader.setPrompt("");
         return line;
      }
      catch (IOException e)
      {
         throw new IllegalStateException("Shell input stream failure", e);
      }
   }

   @Override
   public String promptRegex(final String message, final String regex)
   {
      String input = "";
      do
      {
         input = prompt(message);
      }
      while (!input.matches(regex));
      return input;
   }

   @Override
   public String promptRegex(final String message, final String pattern, final String defaultIfEmpty)
   {
      if (!defaultIfEmpty.matches(pattern))
      {
         throw new IllegalArgumentException("Default value [" + defaultIfEmpty + "] does not match required pattern ["
                  + pattern + "]");
      }

      String input = "";
      do
      {
         input = prompt(message + " [" + defaultIfEmpty + "]");
         if ("".equals(input.trim()))
         {
            input = defaultIfEmpty;
         }
      }
      while (!input.matches(pattern));
      return input;
   }

   @Override
   @SuppressWarnings("unchecked")
   public <T> T prompt(final String message, final Class<T> clazz)
   {
      Object result = null;
      Object input = "";
      do
      {
         input = prompt(message);
         try
         {
            result = DataConversion.convert(input, clazz);
         }
         catch (Exception e)
         {
            result = InvalidInput.INSTANCE;
         }
      }
      while ((result instanceof InvalidInput));

      return (T) result;
   }

   @Override
   @SuppressWarnings("unchecked")
   public <T> T prompt(final String message, final Class<T> clazz, final T defaultIfEmpty)
   {
      Object result = null;
      String input = "";
      do
      {
         input = prompt(message);
         if ((input == null) || "".equals(input.trim()))
         {
            result = defaultIfEmpty;
         }
         else
         {
            input = input.trim();
            try
            {
               result = DataConversion.convert(input, clazz);
            }
            catch (Exception e)
            {
               result = InvalidInput.INSTANCE;
            }
         }
      }
      while ((result instanceof InvalidInput));

      return (T) result;
   }

   @Override
   public boolean promptBoolean(final String message)
   {
      return promptBoolean(message, true);
   }

   @Override
   public boolean promptBoolean(final String message, final boolean defaultIfEmpty)
   {
      String query = " [Y/n] ";
      if (!defaultIfEmpty)
      {
         query = " [y/N] ";
      }

      return prompt(message + query, Boolean.class, defaultIfEmpty);
   }

   @Override
   public int promptChoice(final String message, final Object... options)
   {
      return promptChoice(message, Arrays.asList(options));
   }

   @Override
   public int promptChoice(final String message, final List<?> options)
   {
      if (options == null)
      {
         throw new IllegalArgumentException(
                  "promptChoice() Cannot ask user to select from a list of nothing. Ensure you have values in your options list.");
      }

      int count = 1;
      println(message);

      Object result = InvalidInput.INSTANCE;

      while (result instanceof InvalidInput)
      {
         println();
         for (Object entry : options)
         {
            println("  " + count + " - [" + entry + "]");
            count++;
         }
         println();
         int input = prompt("Choose an option by typing the number of the selection: ", Integer.class) - 1;
         if (input < options.size())
         {
            return input;
         }
         else
         {
            println("Invalid selection, please try again.");
         }
      }
      return -1;
   }

   @Override
   public <T> T promptChoiceTyped(final String message, final T... options)
   {
      return promptChoiceTyped(message, Arrays.asList(options));
   }

   @Override
   @SuppressWarnings("unchecked")
   public <T> T promptChoiceTyped(final String message, final List<T> options)
   {
      if (options == null)
      {
         throw new IllegalArgumentException(
                  "promptChoice() Cannot ask user to select from a list of nothing. Ensure you have values in your options list.");
      }

      int count = 1;
      println(message);

      Object result = InvalidInput.INSTANCE;

      while (result instanceof InvalidInput)
      {
         println();
         for (T entry : options)
         {
            println("  " + count + " - [" + entry + "]");
            count++;
         }
         println();
         int input = prompt("Choose an option by typing the number of the selection: ", Integer.class) - 1;
         if (input < options.size())
         {
            result = options.get(input);
         }
         else
         {
            println("Invalid selection, please try again.");
         }
      }
      return (T) result;
   }

   @Override
   @SuppressWarnings("unchecked")
   public <T> T promptChoice(final String message, final Map<String, T> options)
   {
      int count = 1;
      println(message);
      List<Entry<String, T>> entries = new ArrayList<Map.Entry<String, T>>();
      entries.addAll(options.entrySet());

      Object result = InvalidInput.INSTANCE;
      while (result instanceof InvalidInput)
      {
         println();
         for (Entry<String, T> entry : entries)
         {
            println("  " + count + " - [" + entry.getKey() + "]");
            count++;
         }
         println();
         String input = prompt("Choose an option by typing the name or number of the selection: ");
         if (options.containsKey(input))
         {
            result = options.get(input);
         }
      }
      return (T) result;
   }

   @Override
   public String promptCommon(final String message, final PromptType type)
   {
      return promptRegex(message, type.getPattern());
   }

   @Override
   public String promptCommon(final String message, final PromptType type, final String defaultIfEmpty)
   {
      return promptRegex(message, type.getPattern(), defaultIfEmpty);
   }

   @Override
   public File promptFile(String message)
   {
      String path = "";
      while ((path == null) || path.trim().isEmpty())
      {
         path = promptWithCompleter(message, new FileOptionCompleter(this));
      }

      try
      {
         path = Files.canonicalize(path);
         return new File(path).getCanonicalFile();
      }
      catch (IOException e)
      {
         throw new ShellException(e);
      }
   }

   @Override
   public File promptFile(String message, File defaultIfEmpty)
   {
      File result = defaultIfEmpty;
      String path = promptWithCompleter(message, new FileOptionCompleter(this));
      if (!"".equals(path) && (path != null) && !path.trim().isEmpty())
      {
         try
         {
            path = Files.canonicalize(path);
            result = new File(path).getCanonicalFile();
         }
         catch (IOException e)
         {
            throw new ShellException(e);
         }
      }
      return result;
   }
}