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

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

/**
 * @author Mike Brock .
 */
public abstract class Parse
{
   private static final Set<String> reservedWords = new HashSet<String>();
   private static final Set<String> operators = new HashSet<String>();

   static
   {
      reservedWords.add("if");
      reservedWords.add("else");
      reservedWords.add("for");
      reservedWords.add("new");
      reservedWords.add("do");

      operators.add("+");
      operators.add("-");
      operators.add("/");
      operators.add("*");
      operators.add("%");
      operators.add("&&");
      operators.add("||");
      operators.add("=");
   }

   public static boolean isReservedWord(String word)
   {
      return reservedWords.contains(word);
   }


   public static boolean isTokenPart(char c)
   {
      switch (c)
      {
      case '-':
         return true;
      default:
         return Character.isJavaIdentifierPart(c);
      }
   }

   public static boolean isOperator(String str)
   {
      return operators.contains(str);
   }


   public static String disassemble(Node n)
   {
      if (n == null)
      {
         return "";
      }

      StringBuilder build = new StringBuilder();

      do
      {
         if (n instanceof PipeNode)
         {
             build.append('|')
                  .append(disassemble(((NestedNode) n).getNest()));
         }
         else if (n instanceof NestedNode)
         {
            build.append('(')
                  .append(disassemble(((NestedNode) n).getNest()))
                  .append(')');
         }
         else if (n instanceof TokenNode)
         {
            build.append(((TokenNode) n).getValue());
         }

      }
      while ((n = n.getNext()) != null);

      return build.toString();
   }

   public static String queueToString(Queue<String> tokens)
   {
      StringBuilder sb = new StringBuilder();
      for (String s : tokens)
      {
         sb.append(s);
      }
      return sb.toString();
   }
}
