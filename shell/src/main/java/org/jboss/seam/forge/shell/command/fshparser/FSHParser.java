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

package org.jboss.seam.forge.shell.command.fshparser;

import org.mvel2.util.ParseTools;

import static org.mvel2.util.ParseTools.balancedCapture;
import static org.mvel2.util.ParseTools.isWhitespace;
import static org.mvel2.util.ParseTools.subset;

/**
 * @author Mike Brock .
 */
public class FSHParser
{
   private char[] expr;
   private int cursor;
   private int length;

   private Node firstNode;
   private Node node;

   private boolean nest = false;

   public FSHParser(String expr)
   {
      this.length = (this.expr = expr.toCharArray()).length;
   }

   public FSHParser(String expr, boolean nest)
   {
      this.length = (this.expr = expr.toCharArray()).length;
      this.nest = nest;
   }


   public Node parse()
   {
      Node n;
      while ((n = captureLogicalStatement()) != null)
      {
         addNode(n);
      }
      return firstNode;
   }

   private Node nextNode()
   {


      skipWhitespace();
      int start = cursor;

      if (cursor >= length)
      {
         return null;
      }

      switch (expr[cursor])
      {
      //literals
      case '\'':
      case '"':
         cursor = balancedCapture(expr, cursor, expr[cursor]);
         return new TokenNode(new String(expr, start, cursor++ - start + 1));

      case '(':
         cursor = balancedCapture(expr, cursor, expr[cursor]);
         return new FSHParser(new String(expr, ++start, cursor++ - start), true).parse();

      default:
         String tk = captureToken();

         if (Parse.isReservedWord(tk))
         {
            boolean block = "for".equals(tk) || "if".equals(tk) || "while".equals(tk);

            start = cursor;
            SkipLoop:
            while (cursor <= length)
            {
               switch (expr[cursor])
               {
               case '\'':
               case '"':
               case '(':
                  cursor = balancedCapture(expr, cursor, expr[cursor]);

                  if (block)
                  {
                     cursor++;
                     while (cursor != length && Character.isWhitespace(expr[cursor])) cursor++;

                     if (cursor != length)
                     {
                        do
                        {


                           boolean openBracket = expr[cursor] == '{';

                           if (openBracket)
                           {
                              cursor++;
                           }

                           tk += new String(expr, start, cursor - start);
                           tk += "shell(\"";

                           start = cursor;

                           if (openBracket)
                           {
                              cursor = balancedCapture(expr, cursor, '{');
                           }
                           else
                           {
                              while (cursor != length && expr[cursor] != ';') cursor++;
                           }

                           int offset = cursor != length && expr[cursor] == '}' ? -1 : 0;

                           String subStmt = new String(expr, start, cursor - start)
                                 .replace("\"", "\\\"");

                           StringBuilder execString = new StringBuilder();
                           boolean terminated = false;
                           for (int i = 0; i < subStmt.length(); i++)
                           {
                              if (subStmt.charAt(i) == '$')
                              {
                                 execString.append("\"+");
                                 i++;

                                 while (i < subStmt.length()
                                       && Character.isJavaIdentifierPart(subStmt.charAt(i)))
                                    execString.append(subStmt.charAt(i++));

                                 if (i < subStmt.length())
                                 {
                                    execString.append("+\"");
                                    i--;
                                 }
                                 else
                                 {
                                    execString.append(")");
                                    terminated = true;
                                 }
                              }
                              else
                              {
                                 execString.append(subStmt.charAt(i));
                              }
                           }

                           if (!terminated)
                           {
                              execString.append("\")");
                           }

                           tk += execString.toString();

                           if (offset == -1)
                           {
                              tk += "}";
                              cursor++;
                           }

                           start = cursor;
                        }
                        while (ifThenElseBlockContinues());

                        return new ScriptNode(new TokenNode(tk), false);
                     }
                  }


                  break;

               case ';':
                  break SkipLoop;
               }

               cursor++;
            }

            tk += new String(expr, start, cursor - start);
         }

         return tk.startsWith("$") ? new ScriptNode(new TokenNode(tk), false) : new TokenNode(tk);
      }
   }

   protected boolean ifThenElseBlockContinues()
   {
      skipWhitespace();
      if ((cursor + 4) < length)
      {
         if (expr[cursor] != ';')
         {
            cursor--;
         }
         skipWhitespace();

         if (expr[cursor] == 'e' && expr[cursor + 1] == 'l' && expr[cursor + 2] == 's' && expr[cursor + 3] == 'e'
               && (isWhitespace(expr[cursor + 4]) || expr[cursor + 4] == '{'))
         {

            cursor += 4;
            skipWhitespace();

            if ((cursor + 1) < length && expr[cursor] == 'i' && expr[cursor + 1] == 'f')
            {
               cursor += 2;

               expectNext('(');
               cursor = balancedCapture(expr, cursor, '(') + 1;
            }

            skipWhitespace();

            return true;
         }
      }
      return false;
   }

   private LogicalStatement captureLogicalStatement()
   {
      if (cursor >= length)
      {
         return null;
      }

      Node start = null;
      Node n = null;
      Node d;

      int tokens = 0;
      boolean pipe = false;
      boolean script = false;
      boolean nocommand = false;

      while ((d = nextNode()) != null)
      {
         if (start == null)
         {
            start = n = d;
         }

         if (tokenMatch(d, ";"))
         {
            /**
             * If the first token is a ';' we reset to the next node, skipping it.
             */
            if (d == start)
            {
               start = n = nextNode();
               continue;
            }
            cursor++;

            break;
         }
         else if (tokenMatch(d, "|"))
         {
            pipe = true;

            cursor++;
            break;
         }
         else if (tokens == 0 && tokenMatch(d, "@"))
         {
            script = nocommand = true;
            continue;
         }
         else if (nest && !script && tokenIsOperator(d))
         {
            script = true;
         }
         else if (d == start && (tokenIsReservedWorld(d) || tokenIsVarRef(d)))
         {
            script = true;
         }

         if (n != d)
         {
            tokens++;
            n.setNext(n = d);
         }
      }

      LogicalStatement logicalStatement
            = new LogicalStatement(script ? new ScriptNode(start, nocommand) : start);

      if (pipe)
      {
         PipeNode pipeNode = new PipeNode(captureLogicalStatement());
         logicalStatement.setNext(pipeNode);
      }

      return logicalStatement;
   }

   private String captureToken()
   {
      if (cursor >= length)
      {
         return null;
      }

      int start = cursor;

      if (Parse.isTokenPart(expr[cursor]))
      {
         while (cursor != length && Parse.isTokenPart(expr[cursor])) cursor++;
      }
      else
      {
         Skip:
         while (cursor != length)
         {
            switch (expr[cursor])
            {
            case ' ':
            case '\t':
            case '\r':
               break Skip;
            case ';':
               return ";";

            default:
               if (Parse.isTokenPart(expr[cursor]))
               {
                  break Skip;
               }
            }
            cursor++;
         }
      }

      return new String(expr, start, cursor - start);
   }

   private void skipWhitespace()
   {
      while (cursor != length && ParseTools.isWhitespace(expr[cursor])) cursor++;
   }

   private void addNode(Node n)
   {
      if (node == null)
      {
         firstNode = node = n;
      }
      else
      {
         node.setNext(node = n);
      }
   }

   private void expectNext(char c)
   {
      while (cursor != length && expr[cursor] != c) cursor++;

      if (cursor == length || expr[cursor] != c)
      {
         throw new RuntimeException("expected '('");
      }
   }

   private static boolean tokenIsVarRef(Node n)
   {
      return n instanceof TokenNode && ((TokenNode) n).getValue().startsWith("$");
   }

   private static boolean tokenIsReservedWorld(Node n)
   {
      return n instanceof TokenNode && Parse.isReservedWord(((TokenNode) n).getValue());
   }

   private static boolean tokenIsOperator(Node n)
   {
      return n instanceof TokenNode && Parse.isOperator(((TokenNode) n).getValue());
   }

   public static boolean tokenMatch(Node n, String text)
   {
      return n instanceof TokenNode && ((TokenNode) n).getValue().equals(text);
   }

   private static String getTokenString(Node n)
   {
      return n instanceof TokenNode ? ((TokenNode) n).getValue() : "";
   }

   public static void main(String[] args)
   {
      Node n = new FSHParser("this-command (x = ''; for (count:100) x += count); abc * 3 | foo").parse();
      System.out.println(n);
   }
}
