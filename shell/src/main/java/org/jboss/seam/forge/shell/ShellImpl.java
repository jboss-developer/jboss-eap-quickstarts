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

package org.jboss.seam.forge.shell;

import jline.console.ConsoleReader;
import jline.console.completer.AggregateCompleter;
import jline.console.completer.Completer;
import org.fusesource.jansi.Ansi;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.resources.builtin.DirectoryResource;
import org.jboss.seam.forge.project.services.ResourceFactory;
import org.jboss.seam.forge.project.util.ResourceUtil;
import org.jboss.seam.forge.shell.command.ExecutionParser;
import org.jboss.seam.forge.shell.command.PromptTypeConverter;
import org.jboss.seam.forge.shell.command.convert.BooleanConverter;
import org.jboss.seam.forge.shell.command.convert.FileConverter;
import org.jboss.seam.forge.shell.command.fshparser.FSHRuntime;
import org.jboss.seam.forge.shell.completer.CommandCompleterAdaptor;
import org.jboss.seam.forge.shell.completer.FileOptionCompleter;
import org.jboss.seam.forge.shell.completer.PluginCommandCompleter;
import org.jboss.seam.forge.shell.exceptions.*;
import org.jboss.seam.forge.shell.plugins.builtin.Echo;
import org.jboss.seam.forge.shell.plugins.events.AcceptUserInput;
import org.jboss.seam.forge.shell.plugins.events.PostStartup;
import org.jboss.seam.forge.shell.plugins.events.Shutdown;
import org.jboss.seam.forge.shell.plugins.events.Startup;
import org.jboss.seam.forge.shell.project.CurrentProject;
import org.jboss.seam.forge.shell.util.Files;
import org.jboss.seam.forge.shell.util.GeneralUtils;
import org.jboss.seam.forge.shell.util.ShellColor;
import org.jboss.weld.environment.se.bindings.Parameters;
import org.mvel2.ConversionHandler;
import org.mvel2.DataConversion;
import org.mvel2.MVEL;
import org.mvel2.PropertyAccessException;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import static org.jboss.seam.forge.shell.util.Parsing.firstToken;
import static org.jboss.seam.forge.shell.util.Parsing.firstWhitespace;
import static org.mvel2.DataConversion.addConversionHandler;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Singleton
public class ShellImpl implements Shell
{
   private static final String PROP_PROMPT = "PROMPT";
   private static final String PROP_PROMPT_NO_PROJ = "PROMPT_NOPROJ";

   private static final String DEFAULT_PROMPT = "[\\c{green}$PROJECT_NAME\\c] \\c{white}\\W\\c \\c{green}\\$\\c ";
   private static final String DEFAULT_PROMPT_NO_PROJ = "[\\c{red}no project\\c] \\c{white}\\W\\c \\c{red}\\$\\c ";

   private final Map<String, Object> properties = new HashMap<String, Object>();
   private static final Pattern validCommand = Pattern.compile("^[a-zA-Z0-9\\-_]{0,}$");

   @Inject
   @Parameters
   private List<String> parameters;

   @Inject
   private ExecutionParser parser;

   @Inject
   private Event<PostStartup> postStartup;

   @Inject
   private CurrentProject projectContext;

   // TODO Resource API needs to be separated from Project API
   @Inject
   private ResourceFactory resourceFactory;
   private Resource<?> lastResource;

   // inject the runtime of the shell scripting engine
   @Inject
   private FSHRuntime fshRuntime;

   @Inject
   private PromptTypeConverter promptTypeConverter;

   private ConsoleReader reader;
   private Completer completer;

   private boolean verbose = false;
   private boolean pretend = false;
   private boolean exitRequested = false;

   private InputStream inputStream;
   private Writer outputWriter;

   private final boolean colorEnabled = Boolean.getBoolean("seam.forge.shell.colorEnabled");

   private final ConversionHandler resourceConversionHandler = new ConversionHandler()
   {
      @Override
      @SuppressWarnings("rawtypes")
      public Resource[] convertFrom(final Object obl)
      {
         return GeneralUtils.parseSystemPathspec(
               resourceFactory,
               lastResource,
               getCurrentResource(),
               obl instanceof String[] ? (String[]) obl : new String[]{obl.toString()});
      }

      @SuppressWarnings("rawtypes")
      @Override
      public boolean canConvertFrom(final Class aClass)
      {
         return true;
      }
   };

   void init(@Observes final Startup event, final PluginCommandCompleter pluginCompleter) throws Exception
   {
      BooleanConverter booleanConverter = new BooleanConverter();

      addConversionHandler(boolean.class, booleanConverter);
      addConversionHandler(Boolean.class, booleanConverter);
      addConversionHandler(File.class, new FileConverter());

      addConversionHandler(Resource[].class, resourceConversionHandler);
      addConversionHandler(Resource.class, new ConversionHandler()

      {
         @Override
         public Object convertFrom(final Object o)
         {
            Resource<?>[] res = (Resource<?>[]) resourceConversionHandler.convertFrom(o);
            if (res.length > 1)
            {
               throw new RuntimeException("ambiguous paths");
            }
            else if (res.length == 0)
            {
               return ResourceUtil.parsePathspec(resourceFactory, getCurrentResource(), o.toString()).get(0);
            }
            else
            {
               return res[0];
            }
         }

         @SuppressWarnings("rawtypes")
         @Override
         public boolean canConvertFrom(final Class aClass)
         {
            return resourceConversionHandler.canConvertFrom(aClass);
         }
      });

      initStreams();
      initCompleters(pluginCompleter);
      initParameters();

      properties.put(PROP_PROMPT, DEFAULT_PROMPT);
      properties.put(PROP_PROMPT_NO_PROJ, DEFAULT_PROMPT_NO_PROJ);

      printWelcomeBanner();

      postStartup.fire(new PostStartup());
   }

   private void initCompleters(final PluginCommandCompleter pluginCompleter)
   {
      List<Completer> completers = new ArrayList<Completer>();
      completers.add(new CommandCompleterAdaptor(pluginCompleter));
      // completers.add(new FileOptionCompleter());

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

      if ((parameters != null) && !parameters.isEmpty())
      {
         // this is where we will initialize other parameters... e.g. accepting
         // a path
      }
   }

   private void printWelcomeBanner()
   {
      System.out.println("   ____                          _____                    ");
      System.out.println("  / ___|  ___  __ _ _ __ ___    |  ___|__  _ __ __ _  ___ ");
      System.out.println("  \\___ \\ / _ \\/ _` | '_ ` _ \\   | |_ / _ \\| '__/ _` |/ _ \\  "
            + renderColor(ShellColor.YELLOW, "\\\\"));
      System.out.println("   ___) |  __/ (_| | | | | | |  |  _| (_) | | | (_| |  __/  "
            + renderColor(ShellColor.YELLOW, "//"));
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
      String line;
      try
      {
         reader.setPrompt(getPrompt());
         while ((!exitRequested) && ((line = readLine()) != null))
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
         // ?
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
      try
      {
         fshRuntime.run(line);
      }
      catch (NoSuchCommandException e)
      {
         println("[" + e.getCommand() + "]" + e.getMessage());
         if (verbose)
         {
            e.printStackTrace();
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
         println(firstToken(script) + ": command or property not found.");

         if (verbose)
         {
            e.printStackTrace();
         }
      }
      catch (Exception e)
      {
         if (!validCommand.matcher(firstToken(script)).matches())
         {
            println("error executing command:\n" + e.getMessage());
         }
         else if (firstWhitespace(script) != -1)
         {
            println(firstToken(script) + ": command or property not found.");
         }
         else
         {
            println(firstToken(script) + ": error executing statement: " + e.getMessage());
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
   public void print(final ShellColor color, final String output)
   {
      print(renderColor(color, output));
   }

   @Override
   public void println(final ShellColor color, final String output)
   {
      println(renderColor(color, output));
   }

   @Override
   public void printlnVerbose(final ShellColor color, final String output)
   {
      printlnVerbose(renderColor(color, output));
   }

   @Override
   public String renderColor(final ShellColor color, final String output)
   {
      if (!colorEnabled)
      {
         return output;
      }

      Ansi ansi = new Ansi();

      switch (color)
      {
      case BLACK:
         ansi.fg(Ansi.Color.BLACK);
         break;
      case BLUE:
         ansi.fg(Ansi.Color.BLUE);
         break;
      case CYAN:
         ansi.fg(Ansi.Color.CYAN);
         break;
      case GREEN:
         ansi.fg(Ansi.Color.GREEN);
         break;
      case MAGENTA:
         ansi.fg(Ansi.Color.MAGENTA);
         break;
      case RED:
         ansi.fg(Ansi.Color.RED);
         break;
      case WHITE:
         ansi.fg(Ansi.Color.WHITE);
         break;
      case YELLOW:
         ansi.fg(Ansi.Color.YELLOW);
         break;
      case BOLD:
         ansi.a(Ansi.Attribute.INTENSITY_BOLD);
         break;

      default:
         ansi.fg(Ansi.Color.WHITE);
      }

      return ansi.render(output).reset().toString();
   }


   @Override
   public void write(byte b)
   {
      System.out.print((char) b);
   }

   @Override
   public void clear()
   {
      print(new Ansi().cursor(0, 0).eraseScreen().toString());
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
      if (projectContext.getCurrent() != null)
      {
         return Echo.echo(this, Echo.promptExpressionParser(this, (String) properties.get(PROP_PROMPT)));
      }
      else
      {
         return Echo.echo(this, Echo.promptExpressionParser(this, (String) properties.get(PROP_PROMPT_NO_PROJ)));
      }
   }

   @Override
   public DirectoryResource getCurrentDirectory()
   {
      Resource<?> r = getCurrentResource();
      Resource<File> curr = ResourceUtil.getContextDirectory(r);
      
      if(curr instanceof DirectoryResource)
      {
          return (DirectoryResource) curr;
      }
      return null;
   }

   @Override
   public Resource<?> getCurrentResource()
   {
      Resource<?> result = this.projectContext.getCurrentResource();
      if (result == null)
      {
         result = this.resourceFactory.getResourceFrom(Files.getWorkingDirectory());
         properties.put("CWD", result.getFullyQualifiedName());
      }

      return result;
   }

   @Override
   @SuppressWarnings("unchecked")
   public Class<? extends Resource<?>> getCurrentResourceScope()
   {
      return (Class<? extends Resource<?>>) getCurrentResource().getClass();
   }

   @Override
   public void setCurrentResource(final Resource<?> resource)
   {
      lastResource = getCurrentResource();
      projectContext.setCurrentResource(resource);
      properties.put("CWD", resource.getFullyQualifiedName());
   }

   @Override
   public void setCurrentResource(final File file)
   {
      setCurrentResource((this.resourceFactory.getResourceFrom(file.getAbsoluteFile())));
   }

   @Override
   public Project getCurrentProject()
   {
      return this.projectContext.getCurrent();
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
      return promptWithCompleter(message, null);
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
      String input;
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

      String input;
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
      Object result;
      Object input;
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
      Object result;
      String input;
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
   public int getHeight()
   {
      return reader.getTerminal().getHeight();
   }

   @Override
   public int getWidth()
   {
      return reader.getTerminal().getWidth();
   }

   public String escapeCode(final int code, final String value)
   {
      return new Ansi().a(value).fg(Ansi.Color.BLUE).toString();
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
      String result = promptRegex(message, type.getPattern());
      result = promptTypeConverter.convert(type, result);
      return result;
   }

   @Override
   public String promptCommon(final String message, final PromptType type, final String defaultIfEmpty)
   {
      String result = promptRegex(message, type.getPattern(), defaultIfEmpty);
      result = promptTypeConverter.convert(type, result);
      return result;
   }

   @Override
   public File promptFile(final String message)
   {
      String path = "";
      while ((path == null) || path.trim().isEmpty())
      {
         path = promptWithCompleter(message, new FileOptionCompleter());
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
   public File promptFile(final String message, final File defaultIfEmpty)
   {
      File result = defaultIfEmpty;
      String path = promptWithCompleter(message, new FileOptionCompleter());
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