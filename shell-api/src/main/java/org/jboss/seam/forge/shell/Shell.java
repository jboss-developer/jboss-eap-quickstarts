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

import java.io.*;
import java.util.List;
import java.util.Map;

import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.shell.util.ShellColor;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface Shell extends ShellPrintWriter
{
   /**
    * Return the current working directory of the shell. (This value may change
    * through execution of plugins or other operations.)
    */
   File getCurrentDirectory();

   Resource<?> getCurrentResource();

   Class<? extends Resource<?>> getCurrentResourceScope();

   void setCurrentResource(Resource<?> resource);

   void setCurrentResource(File file);

   Project getCurrentProject();

   /**
    * Return true if this shell is currently running in pretend mode.
    * <p/>
    * Modifications to files made while running in pretend mode are made in a
    * temporary directory, and the output is produced as a Diff that can then be
    * applied to the project.
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
    * Prompt for user input, and return as a String.
    */
   String prompt();

   /**
    * Prompt for user input, first printing the given line, then return user
    * input as a String.
    */
   String prompt(String message);

   /**
    * Prompt for user input, first printing the given message, then return user
    * input cast to the type provided.
    */
   <T> T prompt(String message, Class<T> clazz);

   /**
    * Prompt for boolean user input (Y/n), first printing the given message,
    * then returning user input as a boolean. The value returned will default to
    * <code>true</code> if an empty or whitespace-only user input is read.
    */
   boolean promptBoolean(String string);

   /**
    * Prompt for boolean user input (Y/n), first printing the given message,
    * then returning user input as a boolean.
    * 
    * @param defaultIfEmpty The value to be returned when an empty or
    *           whitespace-only user input is read.
    */
   boolean promptBoolean(String message, boolean defaultIfEmpty);

   /**
    * Prompt for user input, first printing the given message, followed by an
    * enumerated list of options (printing the String value of each item in the
    * list.) Loop until the user enters a number corresponding to one of the
    * options, then return the index of that option from the list.
    * 

    * @param message The prompt message to display until valid input is entered
    * @param options The list of selection options
    * @return the index of selected option
    */
   int promptChoice(String message, Object... options);

   /**
    * Prompt for user input, first printing the given message, followed by an
    * enumerated list of options (printing the String value of each item in the
    * list.) Loop until the user enters a number corresponding to one of the
    * options, then return the index of that option from the list.
    * 
    * @param message The prompt message to display until valid input is entered
    * @param options The list of selection options
    * @return the index of the selected option
    */
   int promptChoice(String message, List<?> options);

   /**
    * Prompt for user input, first printing the given message, followed by an
    * enumerated list of options (printing the String value of each item in the
    * list.) Loop until the user enters a number corresponding to one of the
    * options, then return that option from the list.
    * 
    * @param <T> The type of the objects contained in the list
    * @param message The prompt message to display until valid input is entered
    * @param options The list of selection options
    * @return the selected option
    */
   <T> T promptChoiceTyped(String message, List<T> options);

   /**
    * Prompt for user input, first printing the given message, followed by an
    * enumerated list of options (printing the String value of each item in the
    * list.) Loop until the user enters a number corresponding to one of the
    * options, then return that option from the list.
    * 
    * @param <T> The type of the objects contained in the list
    * @param message The prompt message to display until valid input is entered
    * @param options The list of selection options
    * @return the selected option
    */
   <T> T promptChoiceTyped(String message, T... options);

   /**
    * Prompt for user input, first printing the given message, followed by a
    * keyed list of options. Loop until the user enters a key corresponding to
    * one of the options, then return the value of that option from the map.
    * 
    * @param <T> The type of the objects contained in the map
    * @param message The prompt message to display until valid input is entered
    * @param options The map of selection options
    * @return the selected option
    */
   <T> T promptChoice(String message, Map<String, T> options);

   /**
    * Prompt for user input, first printing the given message, then returning
    * user input as a String. The prompt will repeat until input matching the
    * prompt type is entered.
    * 
    * @param message The prompt message to display until valid input is entered
    * @param type The prompt type to which valid input must be matched
    */
   String promptCommon(String message, PromptType type);

   /**
    * Prompt for user input in the form of a file path, first printing the given
    * message, then returning user input as a File. The prompt will repeat until
    * input matching the prompt type is entered.
    * 
    * @param message The prompt message to display until valid input is entered
    */
   File promptFile(String message);

   /**
    * Prompt for user input in the form of a file path, first printing the given
    * message, then returning user input as a File. The prompt will repeat until
    * input matching the prompt type is entered.
    * 
    * @param message The prompt message to display until valid input is entered
    * @param defaultIfEmpty The value to be returned when an empty or
    *           whitespace-only user input is read.
    */
   File promptFile(String message, File defaultIfEmpty);

   /**
    * Same as {@link #promptCommon(String, PromptType)}, but will default to a
    * given value if user input is empty.
    * 
    * @param message The prompt message to display until valid input is entered
    * @param type The prompt type to which valid input must be matched
    * @param defaultIfEmpty The value to be returned when an empty or
    *           whitespace-only user input is read.
    */
   String promptCommon(String message, PromptType type, String defaultIfEmpty);

   /**
    * Prompt for user input (Y/n), first printing the given message, then
    * returning user input as a String. The prompt will repeat until input
    * matching the regular expression is entered.
    * 
    * @param message The prompt message to display until valid input is entered
    * @param regex The regular expression to which valid input must be matched
    */
   String promptRegex(String message, String regex);

   /**
    * Same as {@link #promptRegex(String, String)}, but will default to a given
    * value if user input is empty.
    * 
    * @param message The prompt message to display until valid input is entered
    * @param pattern The regular expression to which valid input must be matched
    * @param defaultIfEmpty The value to be returned when an empty or
    *           whitespace-only user input is read.
    */
   String promptRegex(String message, String pattern, String defaultIfEmpty);

   /**
    * Prompt for user input, first printing the given line, then returning user
    * input as a converted value.
    * 
    * @param clazz The type to which the value will be converted, if possible.
    * @param defaultIfEmpty The value to be returned when an empty or
    *           whitespace-only user input is read.
    */
   <T> T prompt(String message, Class<T> clazz, T defaultIfEmpty);

   /**
    * Print output to the console.
    */
   void print(String output);

   /**
    * Print output to the console, followed by the newline character.
    */
   void println(String output);

   /**
    * Print a blank line to the console.
    */
   void println();

   /**
    * Write output to the console, only if {@link #isVerbose()}
    * <code> == true</code>.
    */
   void printlnVerbose(String output);

      /**
    * Print color output to the console.
    */
   void print(ShellColor color, String output);

   /**
    * Print color output to the console, followed by the newline character.
    */
   void println(ShellColor color, String output);


   /**
    * Write color output to the console, only if {@link #isVerbose()} <code> == true</code>.
    */
   void printlnVerbose(ShellColor color, String output);


   /**
    * Render a color for the current terminal emulation by encapsulating the string is the appropriate escape codes
    * @param color
    * @param output
    * @return
    */
   public String renderColor(ShellColor color, String output);

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
    * Return the current height, in characters, of the current shell console.
    * (<strong>Warning:</strong> This may change in the time between when the
    * method is called and when the result is used. Be sure to call the method
    * as close to its actual use as possible.)
    */
   int getHeight();

   /**
    * Return the current width, in characters, of the current shell console.
    * (<strong>Warning:</strong> This may change in the time between when the
    * method is called and when the result is used. Be sure to call the method
    * as close to its actual use as possible.)
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
