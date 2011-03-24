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

import java.util.List;
import java.util.Map;

import org.jboss.seam.forge.resources.FileResource;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface ShellPrompt
{

   /**
    * Prompt for user input, and return as a String.
    */
   String prompt();

   /**
    * This works like prompt, however when a CR is received, the method returns the value, but does not produce a CR in
    * the actual terminal buffer. This is useful when a plugin is using manual cursor control.
    * 
    * @return
    */
   String promptAndSwallowCR();

   /**
    * Prompt for user input, first printing the given line, then return user input as a String.
    */
   String prompt(String message);

   /**
    * Prompt for user input, first printing the given message, then return user input cast to the type provided.
    */
   <T> T prompt(String message, Class<T> clazz);

   /**
    * Prompt for boolean user input (Y/n), first printing the given message, then returning user input as a boolean. The
    * value returned will default to <code>true</code> if an empty or whitespace-only user input is read.
    */
   boolean promptBoolean(String string);

   /**
    * Prompt for boolean user input (Y/n), first printing the given message, then returning user input as a boolean.
    * 
    * @param defaultIfEmpty The value to be returned when an empty or whitespace-only user input is read.
    */
   boolean promptBoolean(String message, boolean defaultIfEmpty);

   /**
    * Prompt for user input, first printing the given message, followed by an enumerated list of options (printing the
    * String value of each item in the list.) Loop until the user enters a number corresponding to one of the options,
    * then return the index of that option from the list.
    * 
    * @param message The prompt message to display until valid input is entered
    * @param options The list of selection options
    * @return the index of selected option
    */
   int promptChoice(String message, Object... options);

   /**
    * Prompt for user input, first printing the given message, followed by an enumerated list of options (printing the
    * String value of each item in the list.) Loop until the user enters a number corresponding to one of the options,
    * then return the index of that option from the list.
    * 
    * @param message The prompt message to display until valid input is entered
    * @param options The list of selection options
    * @return the index of the selected option
    */
   int promptChoice(String message, List<?> options);

   /**
    * Prompt for user input, first printing the given message, followed by an enumerated list of options (printing the
    * String value of each item in the list.) Loop until the user enters a number corresponding to one of the options,
    * then return that option from the list.
    * 
    * @param <T> The type of the objects contained in the list
    * @param message The prompt message to display until valid input is entered
    * @param options The list of selection options
    * @return the selected option
    */
   <T> T promptChoiceTyped(String message, List<T> options);

   /**
    * Prompt for user input, first printing the given message, followed by an enumerated list of options (printing the
    * String value of each item in the list.) Loop until the user enters a number corresponding to one of the options,
    * then return that option from the list.
    * 
    * @param <T> The type of the objects contained in the list
    * @param message The prompt message to display until valid input is entered
    * @param options The list of selection options
    * @param defaultIfEmpty The value to be returned when an empty or whitespace-only user input is read.
    * @return the selected option
    */
   <T> T promptChoiceTyped(String message, List<T> options, T defaultIfEmpty);

   /**
    * Prompt for user input, first printing the given message, followed by a keyed list of options. Loop until the user
    * enters a key corresponding to one of the options, then return the value of that option from the map.
    * 
    * @param <T> The type of the objects contained in the map
    * @param message The prompt message to display until valid input is entered
    * @param options The map of selection options
    * @return the selected option
    */
   <T> T promptChoice(String message, Map<String, T> options);

   /**
    * Prompt for user input, first printing the given message, then returning user input as a String. The prompt will
    * repeat until input matching the prompt type is entered.
    * 
    * @param message The prompt message to display until valid input is entered
    * @param type The prompt type to which valid input must be matched
    */
   String promptCommon(String message, PromptType type);

   /**
    * Prompt for user input in the form of an {@link Enum}, first printing the given message, then returning user input
    * as a {@link Enum}. The prompt will repeat until input matching the prompt type is entered.
    * 
    * @param message The prompt message to display until valid input is entered
    */
   <T extends Enum<T>> T promptEnum(String message, Class<T> type);

   /**
    * Prompt for user input in the form of an {@link Enum}, first printing the given message, then returning user input
    * as a {@link Enum}. The prompt will repeat until input matching the prompt type is entered.
    * 
    * @param message The prompt message to display until valid input is entered
    * @param defaultIfEmpty The value to be returned when an empty or whitespace-only user input is read.
    */
   <T extends Enum<T>> T promptEnum(String message, Class<T> type, T defaultIfEmpty);

   /**
    * Prompt for user input in the form of a file path, first printing the given message, then returning user input as a
    * File. The prompt will repeat until input matching the prompt type is entered.
    * 
    * @param message The prompt message to display until valid input is entered
    */
   FileResource<?> promptFile(String message);

   /**
    * Prompt for user input in the form of a file path, first printing the given message, then returning user input as a
    * File. The prompt will repeat until input matching the prompt type is entered.
    * 
    * @param message The prompt message to display until valid input is entered
    * @param defaultIfEmpty The value to be returned when an empty or whitespace-only user input is read.
    */
   FileResource<?> promptFile(String message, FileResource<?> defaultIfEmpty);

   /**
    * Same as {@link #promptCommon(String, PromptType)}, but will default to a given value if user input is empty.
    * 
    * @param message The prompt message to display until valid input is entered
    * @param type The prompt type to which valid input must be matched
    * @param defaultIfEmpty The value to be returned when an empty or whitespace-only user input is read.
    */
   String promptCommon(String message, PromptType type, String defaultIfEmpty);

   /**
    * Prompt for user input (Y/n), first printing the given message, then returning user input as a String. The prompt
    * will repeat until input matching the regular expression is entered.
    * 
    * @param message The prompt message to display until valid input is entered
    * @param regex The regular expression to which valid input must be matched
    */
   String promptRegex(String message, String regex);

   /**
    * Same as {@link #promptRegex(String, String)}, but will default to a given value if user input is empty.
    * 
    * @param message The prompt message to display until valid input is entered
    * @param pattern The regular expression to which valid input must be matched
    * @param defaultIfEmpty The value to be returned when an empty or whitespace-only user input is read.
    */
   String promptRegex(String message, String pattern, String defaultIfEmpty);

   /**
    * Prompt for user input, first printing the given line, then returning user input as a converted value.
    * 
    * @param clazz The type to which the value will be converted, if possible.
    * @param defaultIfEmpty The value to be returned when an empty or whitespace-only user input is read.
    */
   <T> T prompt(String message, Class<T> clazz, T defaultIfEmpty);
}
