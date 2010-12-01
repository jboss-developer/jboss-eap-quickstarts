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

package org.jboss.seam.forge.shell.plugins.builtin;

import org.fusesource.jansi.Ansi;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.resources.builtin.DirectoryResource;
import org.jboss.seam.forge.shell.PromptType;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.*;
import org.jboss.seam.forge.shell.util.ShellColor;
import org.mvel2.util.ParseTools;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * @author Mike Brock
 */
@Named("echo")
@Help("Writes input to output.")
@Topic("Shell Environment")
public class Echo implements Plugin
{
   @Inject
   Shell shell;

   @DefaultCommand
   public void run(
         @Option(help = "The text to be echoed") final String[] tokens,
         final PipeOut out)
   {
      if (tokens == null || tokens.length == 0)
      {
         return;
      }

      out.println(echo(shell, tokensToString(tokens)));
   }

   public static String tokensToString(String... tokens)
   {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < tokens.length; i++)
      {
         sb.append(tokens[i]);
         if (i + 1 < tokens.length)
         {
            sb.append(" ");
         }
      }

      return sb.toString();
   }

   public static String promptExpressionParser(Shell shell, String input)
   {
      StringBuilder builder = new StringBuilder();
      char[] expr = input.toCharArray();

      int i = 0;
      int start = 0;
      for (; i < expr.length; i++)
      {
         switch (expr[i])
         {
         case '\\':
            if (i + 1 < expr.length)
            {
               /**
                * Handle escape codes here.
                */
               switch (expr[++i])
               {
               case 'w':
                  builder.append(new String(expr, start, i - start - 1));
                  builder.append(shell.getProperty("CWD"));
                  start = i + 1;
                  break;

               case 'W':
                  builder.append(new String(expr, start, i - start - 1));
                  String v = (String) shell.getProperty("CWD");
                  builder.append(v.substring(v.lastIndexOf('/') + 1));
                  start = i + 1;
                  break;

               case 'd':
                  builder.append(new String(expr, start, i - start - 1));
                  builder.append(new SimpleDateFormat("EEE MMM dd").format(new Date()));
                  start = i + 1;
                  break;

               case 't':
                  builder.append(new String(expr, start, i - start - 1));
                  builder.append(new SimpleDateFormat("HH:mm:ss").format(new Date()));
                  start = i + 1;
                  break;

               case 'T':
                  builder.append(new String(expr, start, i - start - 1));
                  builder.append(new SimpleDateFormat("hh:mm:ss").format(new Date()));
                  start = i + 1;
                  break;

               case '@':
                  builder.append(new String(expr, start, i - start - 1));
                  builder.append(new SimpleDateFormat("KK:mmaa").format(new Date()));
                  start = i + 1;
                  break;

               case '$':
                  builder.append(new String(expr, start, i - start - 1));
                  builder.append("\\$");
                  start = i + 1;
                  break;

               case 'r':
                  builder.append(new String(expr, start, i - start - 1));
                  builder.append("\r");
                  start = i + 1;
                  break;
               case 'n':
                  builder.append(new String(expr, start, i - start - 1));
                  builder.append("\n");
                  start = i + 1;
                  break;

               case 'c':
                  if (i + 1 < expr.length)
                  {
                     switch (expr[++i])
                     {
                     case '{':
                        builder.append(new String(expr, start, i - start - 2));

                        start = i;
                        while (i < input.length() && input.charAt(i) != '}') i++;

                        if (i == input.length() && input.charAt(i) != '}')
                        {
                           builder.append(new String(expr, start, i - start));
                        }
                        else
                        {
                           String color = new String(expr, start + 1, i - start - 1);

                           start = ++i;

                           Capture:
                           while (i < expr.length)
                           {
                              switch (expr[i])
                              {
                              case '\\':
                                 if (i + 1 < expr.length)
                                 {
                                    if (expr[i + 1] == 'c')
                                    {
                                       break Capture;
                                    }
                                 }

                              default:
                                 i++;
                              }
                           }

                           ShellColor c;
                           if ("red".equals(color))
                           {
                              c = ShellColor.RED;
                           }
                           else if ("white".equals(color))
                           {
                              c = ShellColor.WHITE;
                           }
                           else if ("blue".equals(color))
                           {
                              c = ShellColor.BLUE;
                           }
                           else if ("yellow".equals(color))
                           {
                              c = ShellColor.YELLOW;
                           }
                           else if ("bold".equals(color))
                           {
                              c = ShellColor.BOLD;
                           }
                           else if ("black".equals(color))
                           {
                              c = ShellColor.BLACK;
                           }
                           else if ("cyan".equals(color))
                           {
                              c = ShellColor.CYAN;
                           }
                           else if ("green".equals(color))
                           {
                              c = ShellColor.GREEN;
                           }
                           else if ("magenta".equals(color))
                           {
                              c = ShellColor.MAGENTA;
                           }
                           else
                           {
                              c = ShellColor.NONE;
                           }

                           String toColorize = promptExpressionParser(shell, new String(expr, start, i - start));
                           String cStr = shell.renderColor(c, toColorize);

                           builder.append(cStr);
                           start = i += 2;
                        }

                        break;


                     default:
                        builder.append(promptExpressionParser(shell, new String(expr, start, i - start - 2)));
                        start = ++i;
                     }
                  }
               }
            }
         }
      }

      if (start < expr.length && i > start)
      {
         builder.append(new String(expr, start, i - start));
      }

      return builder.toString();
   }

   public static String echo(Shell shell, String input)
   {
      char[] expr = input.toCharArray();
      StringBuilder out = new StringBuilder();
      int start = 0;
      int i = 0;
      while (i < expr.length)
      {
         if (i >= expr.length)
         {
            break;
         }

         switch (expr[i])
         {
         case '\'':
         case '"':
            out.append(new String(expr, start, i - start));
            start = i;
            i = ParseTools.balancedCapture(expr, i, expr[i]);
            out.append(new String(expr, start + 1, i - start - 1));
            start = ++i;
            break;

         case '\\':
            if (i + 1 < expr.length && expr[i + 1] == '$')
            {
               out.append(new String(expr, start, i - start));
               out.append('$');
               start = i += 2;
            }
            break;

         case '$':
            out.append(new String(expr, start, i - start));
            start = ++i;
            while (i != expr.length && Character.isJavaIdentifierPart(expr[i]) && expr[i] != 27)
            {
               i++;
            }

            String var = new String(expr, start, i - start);
            if (shell.getProperties().containsKey(var))
            {
               out.append(String.valueOf(shell.getProperties().get(var)));
            }

            start = i;
            break;

         default:
            if (Character.isWhitespace(expr[i]))
            {
               out.append(new String(expr, start, i - start));

               start = i;
               while (i != expr.length && Character.isWhitespace(expr[i]))
               {
                  i++;
               }

               out.append(new String(expr, start, i - start));

               start = i;
               continue;
            }
         }
         i++;
      }

      if (start < expr.length && i > start)
      {
         out.append(new String(expr, start, i - start));
      }

      return out.toString();
   }

   public static void main(String[] args)
   {

      final Map<String, Object> map = new HashMap<String, Object>();
      map.put("foo", "hey man!");
      map.put("CWD", "/home/foo");
      map.put("PROJECT_NAME", "seam-forge");

      Shell shell = new Shell()
      {
         @Override
         public DirectoryResource getCurrentDirectory()
         {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public Resource<?> getCurrentResource()
         {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public Class<? extends Resource<?>> getCurrentResourceScope()
         {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public void setCurrentResource(Resource<?> resource)
         {
            //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public void setCurrentResource(File file)
         {
            //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public Project getCurrentProject()
         {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public boolean isPretend()
         {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public boolean isVerbose()
         {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public void setVerbose(boolean verbose)
         {
            //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public void clear()
         {
            //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public void execute(String command)
         {
            //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public String prompt()
         {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public String prompt(String message)
         {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public <T> T prompt(String message, Class<T> clazz)
         {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public boolean promptBoolean(String string)
         {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public boolean promptBoolean(String message, boolean defaultIfEmpty)
         {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public int promptChoice(String message, Object... options)
         {
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public int promptChoice(String message, List<?> options)
         {
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public <T> T promptChoiceTyped(String message, List<T> options)
         {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public <T> T promptChoiceTyped(String message, T... options)
         {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public <T> T promptChoice(String message, Map<String, T> options)
         {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public String promptCommon(String message, PromptType type)
         {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public File promptFile(String message)
         {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public File promptFile(String message, File defaultIfEmpty)
         {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public String promptCommon(String message, PromptType type, String defaultIfEmpty)
         {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public String promptRegex(String message, String regex)
         {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public String promptRegex(String message, String pattern, String defaultIfEmpty)
         {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public <T> T prompt(String message, Class<T> clazz, T defaultIfEmpty)
         {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public void print(String output)
         {
            //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public void println(String output)
         {
            //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public void println()
         {
            //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public void printlnVerbose(String output)
         {
            //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public void print(ShellColor color, String output)
         {
            //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public void println(ShellColor color, String output)
         {
            //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public void printlnVerbose(ShellColor color, String output)
         {
            //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public String renderColor(final ShellColor color, final String output)
         {
//            if (true)
//            {
//               return output;
//            }

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
         public void setProperty(String name, Object value)
         {
            //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public Map<String, Object> getProperties()
         {
            return map;
         }

         @Override
         public Object getProperty(String name)
         {
            return map.get(name);
         }

         @Override
         public void setDefaultPrompt()
         {
            //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public void setPrompt(String string)
         {
            //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public String getPrompt()
         {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public void setInputStream(InputStream inputStream) throws IOException
         {
            //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public void setOutputWriter(Writer writer) throws IOException
         {
            //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public int getHeight()
         {
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public int getWidth()
         {
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public String readLine() throws IOException
         {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
         }

         @Override
         public void write(byte b)
         {
            //To change body of implemented methods use File | Settings | File Templates.
         }
      };

      //  System.out.println(shell.renderColor(ShellColor.GREEN, "foo"));

      String pre = promptExpressionParser(shell, "[\\c{yellow}OH NO!\\c] \\@ \\c{bold}\\W\\c $");

      System.out.println(echo(shell, pre));
   }
}
