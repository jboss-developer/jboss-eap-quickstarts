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

import static org.mvel2.util.ParseTools.balancedCapture;

import org.mvel2.util.ParseTools;

/**
 * @author Mike Brock .
 */
public class FSHParser
{
   private char[] expr;
   private int cursor;
   private final int length;

   private Node firstNode;
   private Node node;

   public FSHParser(final String expr)
   {
      this.length = (this.expr = expr.toCharArray()).length;
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
      if (cursor >= length)
      {
         return null;
      }

      try
      {
         skipWhitespace();
         int start = cursor;
         switch (expr[cursor])
         {
         // literals
         case '\'':
         case '"':
         case '(':
            cursor = balancedCapture(expr, cursor, expr[cursor]);
            return new FSHParser(new String(expr, ++start, cursor - start)).parse();

         default:
            return new TokenNode(captureToken());
         }
      }
      finally
      {
         cursor++;
      }
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

      boolean pipe = false;
      boolean script = false;

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

            break;
         }
         else if (tokenMatch(d, "|"))
         {
            pipe = true;
            break;
         }
         else if (!script && tokenIsOperator(d))
         {
            script = true;
         }

         if (n != d)
         {
            n.setNext(n = d);
         }
      }

      LogicalStatement logicalStatement = script ? new ScriptNode(start) : new LogicalStatement(start);

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
         while ((cursor != length) && Parse.isTokenPart(expr[cursor]))
         {
            cursor++;
         }
      }
      else
      {
         Skip: while (cursor != length)
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
      while ((cursor != length) && ParseTools.isWhitespace(expr[cursor]))
      {
         cursor++;
      }
   }

   private void addNode(final Node n)
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

   private static boolean tokenIsOperator(final Node n)
   {
      return (n instanceof TokenNode) && Parse.isOperator(((TokenNode) n).getValue());
   }

   private static boolean tokenMatch(final Node n, final String text)
   {
      return (n instanceof TokenNode) && ((TokenNode) n).getValue().equals(text);
   }

   public static void main(final String[] args)
   {
      Node n = new FSHParser("this-command (1 + 1); abc * 3 | foo").parse();

      System.out.println(Parse.disassemble(n));
   }
}
