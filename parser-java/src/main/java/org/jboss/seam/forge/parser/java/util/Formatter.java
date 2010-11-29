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

package org.jboss.seam.forge.parser.java.util;

import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;
import org.jboss.seam.forge.parser.java.JavaClass;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public abstract class Formatter
{
   public static String format(JavaClass javaClass)
   {
      // TODO locate user's eclipse project settings, use those if we can.
      Properties options = readConfig("org.eclipse.jdt.core.prefs");

      final CodeFormatter codeFormatter = ToolFactory.createCodeFormatter(options);
      String result = formatFile(javaClass, codeFormatter);

      return result;
   }

   private static String formatFile(JavaClass javaClass, CodeFormatter codeFormatter)
   {
      String contents = javaClass.toString();

      IDocument doc = new Document(contents);
      try
      {
         TextEdit edit = codeFormatter.format(CodeFormatter.K_COMPILATION_UNIT, contents, 0, contents.length(), 0, null);
         if (edit != null)
         {
            edit.apply(doc);
         }
         else
         {
            return contents;
         }
      }
      catch (BadLocationException e)
      {
         throw new RuntimeException(e);
      }

      return doc.get();
   }

   private static Properties readConfig(String filename)
   {
      BufferedInputStream stream = null;
      try
      {
         stream = new BufferedInputStream(Formatter.class.getResourceAsStream(filename));
         final Properties formatterOptions = new Properties();
         formatterOptions.load(stream);
         return formatterOptions;
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
      finally
      {
         if (stream != null)
         {
            try
            {
               stream.close();
            }
            catch (IOException e)
            {
               /* ignore */
            }
         }
      }
   }
}
