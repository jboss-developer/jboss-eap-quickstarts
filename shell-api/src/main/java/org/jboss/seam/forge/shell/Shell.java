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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Map;

import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.resources.DirectoryResource;
import org.jboss.seam.forge.resources.Resource;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface Shell extends ShellPrintWriter, ShellPrompt
{
   /**
    * Return the current working directory resource of the shell. Start with {@link #getCurrentResource()} and move up
    * the hierarchy until a {@link DirectoryResource} is found. (This value may change through execution of plug-ins or
    * other operations.)
    */
   DirectoryResource getCurrentDirectory();

   /**
    * Return the current working {@link Resource} of the shell. (This value may change through execution of plug-ins or
    * other operations.)
    * 
    * TODO implement ResourceBag for multiple resources
    */
   Resource<?> getCurrentResource();

   Class<? extends Resource<?>> getCurrentResourceScope();

   void setCurrentResource(Resource<?> resource);

   Project getCurrentProject();

   /**
    * Return true if this shell is currently running in pretend mode.
    * <p/>
    * Modifications to files made while running in pretend mode are made in a temporary directory, and the output is
    * produced as a Diff that can then be applied to the project.
    */
   boolean isPretend();

   /**
    * Return true if this shell is currently running in verbose mode.
    */
   boolean isVerbose();

   /**
    * Toggle verbose mode.
    */
   void setVerbose(boolean verbose);

   /**
    * Write output to the console, only if {@link Shell#isVerbose()} <code> == true</code>.
    */
   void printlnVerbose(String output);

   /**
    * Write color output to the console, only if {@link #isVerbose()} <code> == true</code>.
    */
   void printlnVerbose(ShellColor color, String output);

   /**
    * Clear the console.
    */
   void clear();

   /**
    * Execute a shell command.
    * 
    * @param command
    */
   void execute(String command);

   /**
    * Execute a shell script from the specified file.
    * 
    * @param file
    */
   void execute(File file) throws IOException;

   void execute(File file, String... args) throws IOException;

   /**
    * Wait for input. Return as soon as any key is pressed and return the scancode.
    * 
    * @return
    */
   int scan();

   /**
    * Clear the current line of any text.
    */
   void clearLine();

   /**
    * Move the cursor x the specified number of positions.
    * 
    * @param x
    */
   void cursorLeft(int x);

   /**
    * Set a property in the shell context.
    * 
    * @param name
    * @param value
    */
   void setProperty(String name, Object value);

   /**
    * Get a map of properties for the current shell context.
    * 
    * @return
    */
   Map<String, Object> getProperties();

   /**
    * Get a named property for the shell context
    * 
    * @param name
    * @return
    */
   Object getProperty(String name);

   /**
    * Reset the shell prompt to default.
    */
   void setDefaultPrompt();

   /**
    * Set the current shell prompt, followed by '> '.
    */
   void setPrompt(String string);

   /**
    * Return the current shell prompt;
    */
   String getPrompt();

   /**
    * Set the stream from which the shell should read input.
    */
   void setInputStream(InputStream inputStream) throws IOException;

   /**
    * Set the writer to which the shell should print output.
    */
   void setOutputWriter(Writer writer) throws IOException;

   /**
    * Return the current height, in characters, of the current shell console. (<strong>Warning:</strong> This may change
    * in the time between when the method is called and when the result is used. Be sure to call the method as close to
    * its actual use as possible.)
    */
   int getHeight();

   /**
    * Return the current width, in characters, of the current shell console. (<strong>Warning:</strong> This may change
    * in the time between when the method is called and when the result is used. Be sure to call the method as close to
    * its actual use as possible.)
    */
   int getWidth();

   /**
    * Ask the current {@link InputStream} for data.
    * 
    * @return any read data as a string, or null if none available.
    * @throws IOException on error
    */
   String readLine() throws IOException;
}
