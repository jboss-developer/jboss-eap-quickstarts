/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc., and individual contributors
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
package org.jboss.seam.forge.shell.completer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import jline.console.ConsoleReader;
import jline.console.CursorBuffer;
import jline.console.completer.CandidateListCompletionHandler;
import jline.console.completer.CompletionHandler;

import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.command.CommandMetadata;
import org.jboss.seam.forge.shell.util.ShellColor;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class OptionAwareCompletionHandler implements CompletionHandler
{
   // TODO: handle quotes and escaped quotes && enable automatic escaping of whitespace
   private final CompletedCommandHolder commandHolder;
   private final Shell shell;

   public OptionAwareCompletionHandler(CompletedCommandHolder commandHolder, Shell shell)
   {
      this.commandHolder = commandHolder;
      this.shell = shell;
   }

   @Override
   public boolean complete(final ConsoleReader reader, final List<CharSequence> candidates, final int pos) throws
            IOException
   {
      CursorBuffer buf = reader.getCursorBuffer();

      // if there is only one completion, then fill in the buffer
      if (candidates.size() == 1)
      {
         CharSequence value = candidates.get(0);

         // fail if the only candidate is the same as the current buffer
         if (value.equals(buf.toString()))
         {
            return false;
         }

         setBuffer(reader, value, pos);

         return true;
      }
      else if (candidates.size() > 1)
      {
         String value = getUnambiguousCompletions(candidates);
         setBuffer(reader, value, pos);
      }

      printCandidates(reader, candidates);

      // redraw the current console buffer
      reader.drawLine();

      return true;
   }

   public static void setBuffer(final ConsoleReader reader, final CharSequence value, final int offset) throws
            IOException
   {
      while ((reader.getCursorBuffer().cursor > offset) && reader.backspace())
      {
         // empty
      }

      reader.putString(value);
      reader.setCursorPosition(offset + value.length());
   }

   /**
    * Print out the candidates. If the size of the candidates is greater than the
    * {@link ConsoleReader#getAutoprintThreshold}, they prompt with a warning.
    * 
    * @param candidates the list of candidates to print
    */
   public void printCandidates(final ConsoleReader reader, Collection<CharSequence> candidates) throws
            IOException
   {
      Set<CharSequence> distinct = new HashSet<CharSequence>(candidates);

      if (distinct.size() > reader.getAutoprintThreshold())
      {
         // noinspection StringConcatenation
         reader.print(Messages.DISPLAY_CANDIDATES.format(candidates.size()));
         reader.flush();

         int c;

         String noOpt = Messages.DISPLAY_CANDIDATES_NO.format();
         String yesOpt = Messages.DISPLAY_CANDIDATES_YES.format();
         char[] allowed = { yesOpt.charAt(0), noOpt.charAt(0) };

         while ((c = reader.readCharacter(allowed)) != -1)
         {
            String tmp = new String(new char[] { (char) c });

            if (noOpt.startsWith(tmp))
            {
               reader.println();
               return;
            }
            else if (yesOpt.startsWith(tmp))
            {
               break;
            }
            else
            {
               reader.beep();
            }
         }
      }

      // copy the values and make them distinct, without otherwise affecting the ordering. Only do it if the sizes
      // differ.
      if (distinct.size() != candidates.size())
      {
         Collection<CharSequence> copy = new ArrayList<CharSequence>();

         for (CharSequence next : candidates)
         {
            if (!copy.contains(next))
            {
               copy.add(next);
            }
         }

         candidates = copy;
      }

      reader.println();

      Collection<CharSequence> colorizedCandidates = new ArrayList<CharSequence>();
      for (CharSequence seq : candidates)
      {
         boolean processed = false;
         CommandMetadata command = commandHolder.getCommandMetadata();
         if (command != null && seq.toString().startsWith("--"))
         {
            String str = seq.toString().trim();
            if (str.startsWith("--"))
            {
               str = str.substring(2);
            }

            if (command.hasOption(str) && command.getNamedOption(str).isRequired())
            {
               seq = shell.renderColor(ShellColor.BLUE, seq.toString());
               colorizedCandidates.add(seq);
               processed = true;
            }
         }

         if (!processed)
         {
            colorizedCandidates.add(seq);
         }
      }

      reader.printColumns(colorizedCandidates);
   }

   /**
    * Returns a root that matches all the {@link String} elements of the specified {@link List}, or null if there are no
    * commonalities. For example, if the list contains <i>foobar</i>, <i>foobaz</i>, <i>foobuz</i>, the method will
    * return <i>foob</i>.
    */
   private String getUnambiguousCompletions(final List<CharSequence> candidates)
   {
      if (candidates == null || candidates.isEmpty())
      {
         return null;
      }

      // convert to an array for speed
      String[] strings = candidates.toArray(new String[candidates.size()]);

      String first = strings[0];
      StringBuilder candidate = new StringBuilder();

      for (int i = 0; i < first.length(); i++)
      {
         if (startsWith(first.substring(0, i + 1), strings))
         {
            candidate.append(first.charAt(i));
         }
         else
         {
            break;
         }
      }

      return candidate.toString();
   }

   /**
    * @return true is all the elements of <i>candidates</i> start with <i>starts</i>
    */
   private boolean startsWith(final String starts, final String[] candidates)
   {
      for (String candidate : candidates)
      {
         if (!candidate.startsWith(starts))
         {
            return false;
         }
      }

      return true;
   }

   private static enum Messages
   {
       DISPLAY_CANDIDATES,
       DISPLAY_CANDIDATES_YES,
       DISPLAY_CANDIDATES_NO, ;

      private static final ResourceBundle bundle =
               ResourceBundle.getBundle(CandidateListCompletionHandler.class.getName());

      public String format(final Object... args)
      {
         return String.format(bundle.getString(name()), args);
      }
   }
}
