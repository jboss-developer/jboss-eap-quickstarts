package org.jboss.seam.forge.shell.plugins.builtin;

import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.resources.builtin.DirectoryResource;
import org.jboss.seam.forge.shell.plugins.*;
import org.jboss.seam.forge.shell.util.GeneralUtils;

import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple port of the unix 'wc' command for forge.
 * 
 * @author Mike Brock .
 */
@Named("wc")
@Topic("File & Resources")
@Help("word, line, character and byte count")
public class WordCountPlugin implements Plugin
{
   @DefaultCommand
   public void run(
         @PipeIn final InputStream pipeIn,
         @Option(name = "lines", shortName = "l", flagOnly = true) boolean lineCount,
         @Option(name = "words", shortName = "w", flagOnly = true) boolean wordCount,
         @Option(name = "characters", shortName = "c", flagOnly = true) boolean charCount,
         @Option(description = "file ...", required = false) Resource<?>[] resources,
         final PipeOut out
         ) throws IOException
   {

      if (!(lineCount || wordCount || charCount))
      {
         wordCount = true;
      }

      if (resources != null)
      {
         List<String> results = new ArrayList<String>();
         for (Resource<?> r : resources)
         {
            if (r instanceof DirectoryResource)
            {
               System.err.println("wc: " + r.getName() + ": is a directory.");
               continue;
            }

            InputStream instream = r.getResourceInputStream();
            try
            {
               Results countResults = count(instream, lineCount, wordCount, charCount);
               results.addAll(countResults.getResults(r.getName()));
            }
            finally
            {
               instream.close();
            }
         }
         Results x = new Results();
         x.countLines = lineCount;
         x.countWords = wordCount;
         x.countChars = charCount;

         printOutResults(out, x.getColumns(), results);
      }
      else if (pipeIn != null)
      {
         Results countResults = count(pipeIn, lineCount, wordCount, charCount);
         printOutResults(out, countResults.getColumns(), countResults.getResults("<pipe>"));
      }
   }

   private static void printOutResults(PipeOut out, int cols, List<String> results)
   {
      boolean[] colJust = new boolean[cols];
      for (int i = 0; i < cols; i++)
      {
         colJust[i] = true;
      }
      colJust[cols - 1] = false;

      GeneralUtils.printOutTables(results, colJust, out, null);
   }

   private static Results count(InputStream stream, boolean lines, boolean words, boolean chars)
         throws IOException
   {
      byte[] buffer = new byte[1024];
      int read;

      Results res = new Results();
      res.countLines = lines;
      res.countWords = words;
      res.countChars = chars;

      boolean capture = false;

      byte c;

      while ((read = stream.read(buffer)) != -1)
      {
         res.characters += read;
         for (int i = 0; i < read; i++)
         {
            if (Character.isWhitespace(c = buffer[i]))
            {
               if (capture)
               {
                  capture = false;
                  res.words++;
               }

               /**
                * Handle CRLF
                */
               if (c == '\r')
               {
                  if ((i + 1 < read) && (buffer[i + 1] == '\n'))
                  {
                     i++;
                     c = '\n';
                  }
               }

               if (c == '\n')
               {
                  res.lines++;
               }
            }
            else
            {
               capture = true;
            }
         }
      }

      return res;
   }

   private static class Results
   {
      int words;
      int characters;
      int lines;

      boolean countWords;
      boolean countChars;
      boolean countLines;

      int getColumns()
      {
         int cols = countWords ? 1 : 0;
         cols += countChars ? 1 : 0;
         cols += countLines ? 1 : 0;

         return cols + 1;
      }

      public int getValue(int index)
      {
         // note: the lack of breaks here is intentional, the cases are supposed
         // to fall-through,
         // to the next case if the condition is not satisfied.
         switch (index)
         {
         case 0:
            if (countLines)
            {
               return lines;
            }
         case 1:
            if (countWords)
            {
               return words;
            }
         case 2:
            if (countChars)
            {
               return characters;
            }
         }

         return 0;
      }

      List<String> getResults(String name)
      {
         int cols = getColumns();

         List<String> results = new ArrayList<String>(cols);

         for (int i = 0; i < cols - 1; i++)
         {
            results.add("    " + String.valueOf(getValue(i)));
         }

         results.add(name);

         return results;
      }
   }
}
