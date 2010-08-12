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
import javax.inject.Inject;
import javax.inject.Singleton;

import jline.console.ConsoleReader;
import jline.console.completer.Completer;

import org.jboss.seam.sidekick.shell.cli.Execution;
import org.jboss.seam.sidekick.shell.cli.ExecutionParser;
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
public class Shell
{
   private final String prompt = "encore> ";

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

   void init(@Observes final Startup event) throws Exception
   {
      System.out.println("Startup");
      log.info("Encore Shell - Starting up.");

      reader = new ConsoleReader();
      reader.setHistoryEnabled(true);
      reader.setPrompt(prompt);
      reader.addCompleter(completer);

      if (parameters.contains("--verbose"))
      {
         // TODO set verbose mode
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
         try
         {
            execution.perform();
         }
         catch (Exception e)
         {
            System.err.println("Error executing command: " + execution.getOriginalStatement() + " : " + e.getMessage());
         }
      }
      catch (Exception e)
      {
         System.err.println("Error parsing input: " + e.getMessage());
      }
   }

   void teardown(@Observes final Shutdown event)
   {
      exitRequested = true;
   }

   /**
    * Prompt the user for input, using {@link message} as the prompt text.
    */
   public String prompt(final String message)
   {
      System.out.print(message);
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
   public void write(final String line)
   {
      System.out.println(line);
   }

   public String prompt()
   {
      return prompt("");
   }

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