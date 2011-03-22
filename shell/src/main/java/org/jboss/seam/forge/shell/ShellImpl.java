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

import static org.mvel2.DataConversion.addConversionHandler;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import jline.Terminal;
import jline.TerminalFactory;
import jline.TerminalFactory.Type;
import jline.console.ConsoleReader;
import jline.console.completer.AggregateCompleter;
import jline.console.completer.Completer;
import jline.console.history.MemoryHistory;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.dependencies.Dependency;
import org.jboss.seam.forge.project.facets.JavaSourceFacet;
import org.jboss.seam.forge.project.services.ResourceFactory;
import org.jboss.seam.forge.resources.DirectoryResource;
import org.jboss.seam.forge.resources.FileResource;
import org.jboss.seam.forge.resources.Resource;
import org.jboss.seam.forge.resources.java.JavaResource;
import org.jboss.seam.forge.shell.command.PromptTypeConverter;
import org.jboss.seam.forge.shell.command.convert.BooleanConverter;
import org.jboss.seam.forge.shell.command.convert.DependencyIdConverter;
import org.jboss.seam.forge.shell.command.convert.FileConverter;
import org.jboss.seam.forge.shell.command.fshparser.FSHRuntime;
import org.jboss.seam.forge.shell.completer.CompletedCommandHolder;
import org.jboss.seam.forge.shell.completer.OptionAwareCompletionHandler;
import org.jboss.seam.forge.shell.completer.PluginCommandCompleter;
import org.jboss.seam.forge.shell.events.AcceptUserInput;
import org.jboss.seam.forge.shell.events.PostStartup;
import org.jboss.seam.forge.shell.events.PreShutdown;
import org.jboss.seam.forge.shell.events.Shutdown;
import org.jboss.seam.forge.shell.events.Startup;
import org.jboss.seam.forge.shell.exceptions.AbortedException;
import org.jboss.seam.forge.shell.exceptions.CommandExecutionException;
import org.jboss.seam.forge.shell.exceptions.CommandParserException;
import org.jboss.seam.forge.shell.exceptions.PluginExecutionException;
import org.jboss.seam.forge.shell.exceptions.ShellExecutionException;
import org.jboss.seam.forge.shell.plugins.builtin.Echo;
import org.jboss.seam.forge.shell.project.CurrentProject;
import org.jboss.seam.forge.shell.util.Files;
import org.jboss.seam.forge.shell.util.GeneralUtils;
import org.jboss.seam.forge.shell.util.JavaPathspecParser;
import org.jboss.seam.forge.shell.util.OSUtils;
import org.jboss.seam.forge.shell.util.ResourceUtil;
import org.jboss.weld.environment.se.bindings.Parameters;
import org.mvel2.ConversionHandler;

import sun.misc.Signal;
import sun.misc.SignalHandler;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@ApplicationScoped
@SuppressWarnings("restriction")
public class ShellImpl extends AbstractShellPrompt implements Shell
{
   static final String PROP_FORGE_CONFIG_DIR = "FORGE_CONFIG_DIR";
   static final String PROP_PROMPT = "PROMPT";
   static final String PROP_PROMPT_NO_PROJ = "PROMPT_NOPROJ";

   static final String DEFAULT_PROMPT = "[\\c{green}$PROJECT_NAME\\c] \\c{blue}\\W\\c \\c{green}\\$\\c ";
   static final String DEFAULT_PROMPT_NO_PROJ = "[\\c{red}no project\\c] \\c{blue}\\W\\c \\c{red}\\$\\c ";

   static final String PROP_DEFAULT_PLUGIN_REPO = "DEFFAULT_PLUGIN_REPO";
   static final String DEFAULT_PLUGIN_REPO = "http://seamframework.org/service/File/148617";

   static final String PROP_VERBOSE = "VERBOSE";

   static final String PROP_IGNORE_EOF = "IGNOREEOF";
   static final int DEFAULT_IGNORE_EOF = 1;

   public static final String FORGE_CONFIG_DIR = System.getProperty("user.home") + "/.forge/";
   public static final String FORGE_COMMAND_HISTORY_FILE = "cmd_history";
   public static final String FORGE_CONFIG_FILE = "config";

   private final Map<String, Object> properties = new HashMap<String, Object>();

   @Inject
   @Parameters
   private List<String> parameters;

   @Inject
   private Event<PostStartup> postStartup;

   @Inject
   private Event<Shutdown> shutdown;

   @Inject
   private CurrentProject projectContext;

   @Inject
   ResourceFactory resourceFactory;
   private Resource<?> lastResource;

   @Inject
   private FSHRuntime fshRuntime;

   @Inject
   PromptTypeConverter promptTypeConverter;

   @Inject
   private CompletedCommandHolder commandHolder;

   private ConsoleReader reader;
   private Completer completer;

   private boolean pretend = false;
   private boolean exitRequested = false;

   private InputStream inputStream;
   private OutputStream outputStream;

   private OutputStream historyOutstream;

   private final boolean colorEnabled = Boolean.getBoolean("seam.forge.shell.colorEnabled");

   private final ConversionHandler resourceConversionHandler = new ConversionHandler()
   {
      @Override
      @SuppressWarnings("rawtypes")
      public Resource[] convertFrom(final Object obl)
      {
         return GeneralUtils.parseSystemPathspec(resourceFactory, lastResource, getCurrentResource(),
                  obl instanceof String[] ? (String[]) obl : new String[] { obl.toString() });
      }

      @SuppressWarnings("rawtypes")
      @Override
      public boolean canConvertFrom(final Class aClass)
      {
         return true;
      }
   };

   private final ConversionHandler javaResourceConversionHandler = new ConversionHandler()
   {
      @Override
      public JavaResource[] convertFrom(final Object obj)
      {
         if (getCurrentProject().hasFacet(JavaSourceFacet.class))
         {
            String[] strings = obj instanceof String[] ? (String[]) obj : new String[] { obj.toString() };
            List<Resource<?>> resources = new ArrayList<Resource<?>>();
            for (String string : strings)
            {
               resources.addAll(new JavaPathspecParser(getCurrentProject().getFacet(JavaSourceFacet.class),
                        string).resolve());
            }

            List<JavaResource> filtered = new ArrayList<JavaResource>();
            for (Resource<?> resource : resources)
            {
               if (resource instanceof JavaResource)
               {
                  filtered.add((JavaResource) resource);
               }
            }

            JavaResource[] result = new JavaResource[filtered.size()];
            result = filtered.toArray(result);
            return result;
         }
         else
            return null;
      }

      @SuppressWarnings("rawtypes")
      @Override
      public boolean canConvertFrom(final Class aClass)
      {
         return true;
      }
   };

   private int numEOF = 0;
   private boolean executing;

   @Inject
   private ShellConfig shellConfig;

   void init(@Observes final Startup event, final PluginCommandCompleter pluginCompleter) throws Exception
   {
      BooleanConverter booleanConverter = new BooleanConverter();

      addConversionHandler(boolean.class, booleanConverter);
      addConversionHandler(Boolean.class, booleanConverter);
      addConversionHandler(File.class, new FileConverter());
      addConversionHandler(Dependency.class, new DependencyIdConverter());

      addConversionHandler(JavaResource[].class, javaResourceConversionHandler);
      addConversionHandler(JavaResource.class, new ConversionHandler()
      {

         @Override
         public Object convertFrom(final Object obj)
         {
            JavaResource[] res = (JavaResource[]) javaResourceConversionHandler.convertFrom(obj);
            if (res.length > 1)
            {
               throw new RuntimeException("ambiguous paths");
            }
            else if (res.length == 0)
            {
               if (getCurrentProject().hasFacet(JavaSourceFacet.class))
               {
                  JavaSourceFacet java = getCurrentProject().getFacet(JavaSourceFacet.class);
                  try
                  {
                     JavaResource resource = java.getJavaResource(obj.toString());
                     return resource;
                  }
                  catch (FileNotFoundException e)
                  {
                     throw new RuntimeException(e);
                  }
               }
               return null;
            }
            else
            {
               return res[0];
            }
         }

         @Override
         @SuppressWarnings("rawtypes")
         public boolean canConvertFrom(final Class type)
         {
            return javaResourceConversionHandler.canConvertFrom(type);
         }
      });
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

         @Override
         @SuppressWarnings("rawtypes")
         public boolean canConvertFrom(final Class aClass)
         {
            return resourceConversionHandler.canConvertFrom(aClass);
         }
      });

      projectContext.setCurrentResource(resourceFactory.getResourceFrom(event.getWorkingDirectory()));
      properties.put("CWD", getCurrentDirectory().getFullyQualifiedName());

      configureOSTerminal();
      initReaderAndStreams();
      initCompleters(pluginCompleter);
      initParameters();
      initSignalHandlers();

      if (event.isRestart())
      {
         // suppress the MOTD if this is a restart.
         properties.put("NO_MOTD", true);
      }
      else
      {
         properties.put("NO_MOTD", false);
      }

      properties.put("OS_NAME", OSUtils.getOsName());
      properties.put(PROP_FORGE_CONFIG_DIR, FORGE_CONFIG_DIR);
      properties.put(PROP_PROMPT, "> ");
      properties.put(PROP_PROMPT_NO_PROJ, "> ");

      shellConfig.loadConfig(this);

      postStartup.fire(new PostStartup());
   }

   private void initSignalHandlers()
   {
      try
      {
         // check to see if we have something to work with.
         Class.forName("sun.misc.SignalHandler");

         SignalHandler signalHandler = new SignalHandler()
         {
            @Override
            public void handle(final Signal signal)
            {
               try
               {
                  reader.println("^C");
                  reader.drawLine();
                  reader.resetPromptLine(reader.getPrompt(), "", -1);
               }
               catch (IOException e)
               {
                  if (isVerbose())
                     e.printStackTrace();
               }
            }
         };

         Signal.handle(new Signal("INT"), signalHandler);
      }
      catch (ClassNotFoundException e)
      {
         // signal trapping not supported. Oh well, switch to a Sun-based JVM, loser!
      }
   }

   @Override
   public void writeToHistory(final String command)
   {
      try
      {
         for (int i = 0; i < command.length(); i++)
         {
            historyOutstream.write(command.charAt(i));
         }
         historyOutstream.write('\n');
      }
      catch (IOException e)
      {
      }
   }

   @Override
   public void setHistoryOutputStream(OutputStream stream)
   {
      historyOutstream = stream;
      Runtime.getRuntime().addShutdownHook(new Thread()
      {
         @Override
         public void run()
         {
            try
            {
               historyOutstream.flush();
               historyOutstream.close();
            }
            catch (Exception e)
            {
            }
         }
      });
   }

   @Override
   public void setHistory(List<String> lines)
   {
      MemoryHistory history = new MemoryHistory();

      for (String line : lines)
      {
         history.add(line);
      }

      reader.setHistory(history);
   }

   private void initCompleters(final PluginCommandCompleter pluginCompleter)
   {
      List<Completer> completers = new ArrayList<Completer>();
      completers.add(pluginCompleter);

      completer = new AggregateCompleter(completers);
      this.reader.addCompleter(completer);
      this.reader.setCompletionHandler(new OptionAwareCompletionHandler(commandHolder, this));
   }

   private void initReaderAndStreams() throws IOException
   {
      if (inputStream == null)
      {
         inputStream = System.in;
      }
      if (outputStream == null)
      {
         outputStream = System.out;
      }
      if (OSUtils.isWindows())
      {
         this.reader = setupReaderForWindows(inputStream, outputStream);
      }
      else
         this.reader = new ConsoleReader(inputStream, new OutputStreamWriter(outputStream));
      this.reader.setHistoryEnabled(true);
      this.reader.setBellEnabled(false);
   }

   private void initParameters()
   {
      properties.put(PROP_VERBOSE, String.valueOf(parameters.contains("--verbose")));

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

   void teardown(@Observes final Shutdown shutdown, final Event<PreShutdown> preShutdown)
   {
      preShutdown.fire(new PreShutdown(shutdown.getStatus()));
      exitRequested = true;
   }

   void doShell(@Observes final AcceptUserInput event)
   {
      String line;
      reader.setPrompt(getPrompt());
      while (!exitRequested)
      {
         try
         {
            line = readLine();

            if (line != null)
            {
               if (!"".equals(line.trim()))
               {
                  writeToHistory(line);
                  execute(line);
               }
               reader.setPrompt(getPrompt());
            }
         }
         catch (Exception e)
         {
            handleException(e);
         }
      }
      println();
   }

   private void handleException(final Exception original)
   {
      try
      {
         // unwrap any aborted exceptions
         Throwable cause = original;
         while (cause != null)
         {
            if (cause instanceof AbortedException)
               throw (AbortedException) cause;

            cause = cause.getCause();
         }

         throw original;
      }
      catch (AbortedException e)
      {
         ShellMessages.info(this, "Aborted.");
         if (isVerbose())
         {
            e.printStackTrace();
         }
      }
      catch (CommandExecutionException e)
      {
         ShellMessages.error(this, formatSourcedError(e.getCommand()) + e.getMessage());
         if (isVerbose())
         {
            e.printStackTrace();
         }
      }
      catch (CommandParserException e)
      {
         ShellMessages.error(this, "[" + formatSourcedError(e.getCommand()) + "] " + e.getMessage());
         if (isVerbose())
         {
            e.printStackTrace();
         }
      }
      catch (PluginExecutionException e)
      {
         ShellMessages.error(this, "[" + formatSourcedError(e.getPlugin()) + "] " + e.getMessage());
         if (isVerbose())
         {
            e.printStackTrace();
         }
      }
      catch (ShellExecutionException e)
      {
         ShellMessages.error(this, e.getMessage());
         if (isVerbose())
         {
            e.printStackTrace();
         }
      }
      catch (Exception e)
      {
         if (!isVerbose())
         {
            ShellMessages.error(this, "Exception encountered: " + e.getMessage()
                     + " (type \"set VERBOSE true\" to enable stack traces)");
         }
         else
         {
            ShellMessages.error(this, "Exception encountered: (type \"set VERBOSE false\" to disable stack traces)");
            e.printStackTrace();
         }
      }
   }

   private String formatSourcedError(final Object obj)
   {
      return (obj == null ? "" : ("[" + obj.toString() + "] "));
   }

   @Override
   public String readLine() throws IOException
   {
      String line = reader.readLine();

      if (isExecuting() && line == null)
      {
         reader.println();
         reader.flush();
         throw new AbortedException();
      }
      else if (line == null)
      {
         String eofs = (String) getProperty(PROP_IGNORE_EOF);

         int propEOFs;
         try
         {
            propEOFs = Integer.parseInt(eofs);
         }
         catch (NumberFormatException e)
         {
            if (isVerbose())
               ShellMessages.info(this, "Unable to parse Shell property [" + PROP_IGNORE_EOF + "]");

            propEOFs = DEFAULT_IGNORE_EOF;
         }

         if (this.numEOF < propEOFs)
         {
            println();
            println("(Press CTRL-D again or type 'exit' to quit.)");
            this.numEOF++;
         }
         else
         {
            print("exit");
            shutdown.fire(new Shutdown());
         }
         reader.flush();
      }
      else
      {
         numEOF = 0;
      }
      return line;
   }

   @Override
   public int scan()
   {
      try
      {
         return reader.readVirtualKey();
      }
      catch (IOException e)
      {
         return -1;
      }
   }

   @Override
   public void clearLine()
   {
      print(new Ansi().eraseLine(Ansi.Erase.ALL).toString());
   }

   @Override
   public void cursorLeft(final int x)
   {
      print(new Ansi().cursorLeft(x).toString());
   }

   @Override
   public void execute(final String line)
   {
      try
      {
         executing = true;
         fshRuntime.run(line);
      }
      catch (Exception e)
      {
         handleException(e);
      }
      finally
      {
         executing = false;
      }
   }

   @Override
   public void execute(final File file) throws IOException
   {
      StringBuilder buf = new StringBuilder();
      InputStream instream = new BufferedInputStream(new FileInputStream(file));
      try
      {
         byte[] b = new byte[25];
         int read;

         while ((read = instream.read(b)) != -1)
         {
            for (int i = 0; i < read; i++)
            {
               buf.append((char) b[i]);
            }
         }

         instream.close();

         execute(buf.toString());
      }
      finally
      {
         instream.close();
      }
   }

   @Override
   public void execute(final File file, final String... args) throws IOException
   {
      StringBuilder buf = new StringBuilder();

      String funcName = file.getName().replaceAll("\\.", "_") + "_" + String.valueOf(hashCode()).replaceAll("\\-", "M");

      buf.append("def ").append(funcName).append('(');
      if (args != null)
      {
         for (int i = 0; i < args.length; i++)
         {
            buf.append("_").append(String.valueOf(i));
            if (i + 1 < args.length)
            {
               buf.append(", ");
            }
         }
      }

      buf.append(") {\n");

      if (args != null)
      {
         buf.append("@_vararg = new String[").append(args.length).append("];\n");

         for (int i = 0; i < args.length; i++)
         {
            buf.append("@_vararg[").append(String.valueOf(i)).append("] = ")
                     .append("_").append(String.valueOf(i)).append(";\n");
         }
      }

      InputStream instream = new BufferedInputStream(new FileInputStream(file));
      try
      {
         byte[] b = new byte[25];
         int read;

         while ((read = instream.read(b)) != -1)
         {
            for (int i = 0; i < read; i++)
            {
               buf.append((char) b[i]);
            }
         }

         buf.append("\n}; \n@").append(funcName).append('(');

         if (args != null)
         {
            for (int i = 0; i < args.length; i++)
            {
               buf.append("\"").append(args[i].replaceAll("\\\"", "\\\\\\\"")).append("\"");
               if (i + 1 < args.length)
               {
                  buf.append(", ");
               }
            }
         }

         buf.append(");\n");

         execute(buf.toString());
      }
      finally
      {
         properties.remove(funcName);
         instream.close();
      }
   }

   /*
    * Shell Print Methods
    */
   @Override
   public void printlnVerbose(final String line)
   {
      if (isVerbose())
      {
         try
         {
            reader.println(line);
            reader.flush();
         }
         catch (IOException e)
         {
            throw new RuntimeException(e);
         }
      }
   }

   @Override
   public void print(final String output)
   {
      try
      {
         reader.print(output);
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
   }

   @Override
   public void println(final String line)
   {
      try
      {
         reader.println(line);
         reader.flush();
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
   }

   @Override
   public void println()
   {
      try
      {
         reader.println();
         reader.flush();
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
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
      case ITALIC:
         ansi.a(Ansi.Attribute.ITALIC);
         break;

      default:
         return output;
      }

      return ansi.render(output).reset().toString();
   }

   @Override
   public void write(final byte b)
   {
      try
      {
         reader.print(new String(new byte[] { b }));
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
   }

   @Override
   public void clear()
   {
      print(new Ansi().cursor(0, 0).eraseScreen().toString());
   }

   @Override
   public boolean isVerbose()
   {
      Object s = properties.get(PROP_VERBOSE);
      return (s != null) && "true".equals(s);
   }

   @Override
   public void setVerbose(final boolean verbose)
   {
      properties.put(PROP_VERBOSE, String.valueOf(verbose));
   }

   @Override
   public boolean isPretend()
   {
      return pretend;
   }

   @Override
   public boolean isExecuting()
   {
      return executing;
   }

   @Override
   public void setInputStream(final InputStream is) throws IOException
   {
      this.inputStream = is;
      initReaderAndStreams();
   }

   @Override
   public void setOutputStream(final OutputStream stream) throws IOException
   {
      this.outputStream = stream;
      initReaderAndStreams();
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
      return ResourceUtil.getContextDirectory(r);
   }

   @Override
   public DirectoryResource getConfigDir()
   {
      return resourceFactory.getResourceFrom(new File((String) getProperty(PROP_FORGE_CONFIG_DIR))).reify(
               DirectoryResource.class);
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
   public Project getCurrentProject()
   {
      return this.projectContext.getCurrent();
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
   protected String promptWithCompleter(String message, final Completer tempCompleter)
   {
      if (!message.isEmpty() && message.matches("^.*\\S$"))
      {
         message = message + " ";
      }

      try
      {
         reader.removeCompleter(this.completer);
         if (tempCompleter != null)
         {
            reader.addCompleter(tempCompleter);
         }
         reader.setHistoryEnabled(false);
         reader.setPrompt(message);
         String line = readLine();
         return line;
      }
      catch (IOException e)
      {
         throw new IllegalStateException("Shell input stream failure", e);
      }
      finally
      {
         if (tempCompleter != null)
         {
            reader.removeCompleter(tempCompleter);
         }
         reader.addCompleter(this.completer);
         reader.setHistoryEnabled(true);
         reader.setPrompt("");
      }
   }

   @Override
   protected PromptTypeConverter getPromptTypeConverter()
   {
      return promptTypeConverter;
   }

   @Override
   protected ResourceFactory getResourceFactory()
   {
      return resourceFactory;
   }

   @Override
   public void setAnsiSupported(boolean value)
   {
      if (value != isAnsiSupported())
      {
         try
         {
            if (value)
            {
               configureOSTerminal();
            }
            else
            {
               TerminalFactory.configure(Type.NONE);
               TerminalFactory.reset();
            }
            initReaderAndStreams();
         }
         catch (IOException e)
         {
            throw new RuntimeException("Failed to reset Terminal instance for ANSI configuration", e);
         }
      }
   }

   private void configureOSTerminal() throws IOException
   {
      if (OSUtils.isLinux() || OSUtils.isOSX())
      {
         TerminalFactory.configure(Type.UNIX);
         TerminalFactory.reset();
      }
      else if (OSUtils.isWindows())
      {
         TerminalFactory.configure(Type.WINDOWS);
         TerminalFactory.reset();
      }
      else
      {
         TerminalFactory.configure(Type.NONE);
         TerminalFactory.reset();
      }
      initReaderAndStreams();
   }

   private ConsoleReader setupReaderForWindows(InputStream inputStream, OutputStream outputStream)
   {
      try
      {
         final OutputStream ansiOut = AnsiConsole.wrapOutputStream(outputStream);

         TerminalFactory.configure(Type.WINDOWS);
         Terminal terminal = TerminalFactory.get();
         ConsoleReader consoleReader = new ConsoleReader(inputStream, new PrintWriter(
                             new OutputStreamWriter(ansiOut, System.getProperty(
                                      "jline.WindowsTerminal.output.encoding", System.getProperty("file.encoding")))),
                    null, terminal);
         return consoleReader;
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }

   @Override
   public boolean isAnsiSupported()
   {
      return reader.getTerminal().isAnsiSupported();
   }

   @Override
   public DirectoryResource getPluginDirectory()
   {
      String pluginPath = getProperty("FORGE_CONFIG_DIR") + "plugins/";
      FileResource<?> resource = (FileResource<?>) resourceFactory.getResourceFrom(new File(pluginPath));
      if (!resource.exists())
      {
         resource.mkdirs();
      }
      return resource.reify(DirectoryResource.class);
   }
}