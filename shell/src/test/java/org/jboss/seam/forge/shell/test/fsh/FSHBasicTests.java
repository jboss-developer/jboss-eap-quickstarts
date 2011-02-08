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

package org.jboss.seam.forge.shell.test.fsh;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.forge.shell.command.fshparser.FSHParser;
import org.jboss.seam.forge.shell.command.fshparser.FSHRuntime;
import org.jboss.seam.forge.shell.command.fshparser.Parse;
import org.jboss.seam.forge.test.AbstractShellTest;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

/**
 * @author Mike Brock .
 */
@RunWith(Arquillian.class)
public class FSHBasicTests extends AbstractShellTest
{
   @Inject
   public FSHRuntime runtime;

   @Test
   public void testSimple()
   {
      runtime.run("@dir = '/'; for (i=0;i<4;i++) { ls -l $dir }");
   }

   @Test
   public void testSimple2()
   {
      runtime.run("@foo = 0; while (foo < 2) { if (foo == 1) { ls -l /; @foo++; } else { ls /; @foo++; };  }");
   }

   @Test
   public void testSimple3()
   {
      runtime.run("if (true) { ls -l (2 * 2) } else { ls / | cat }");
   }

   @Test
   public void testSimple4()
   {
      runtime.run("if (false) { ls -l (2 * 2) } else if (false) { ls / } else if (true) { @System.out.println('Hello') }");
   }

   @Test
   public void testSimple5()
   {
      runtime.run("@MySetting=true; if (MySetting) { @System.out.println('Yep (' + MySetting + ')'); }");
   }

   @Test
   public void testSimple6()
   {
      runtime.run("for (dir : ['/', '~', '..']) { ls -l $dir | wc -l }");
   }


   @Test
   public void testSimple7()
   {
      runtime.run("ls -l /; ls");
   }

   @Test
   public void testSimple8()
   {
      runtime.run("ls -l *.txt");
   }


   @Test
   public void testSimple10()
   {
      runtime.run("@myVar='ls'; echo $myVar.toUpperCase()");
   }

//   @Test
//   public void testSimple11()
//   {
//      runtime.run("for (i=0;i<1000;i++) { ls }");
//   }

   @Test
   public void testSimple12()
   {
      runtime.run("for (i = 0; i < 2; i++) { @System.out.println(\"foo\"); }");
   }

   @Test
   public void testSimple13()
   {
      runtime.run("if (isdef $FOO) { }");
   }


   @Test
   public void testSimple14()
   {
      runtime.run("echo \"$PROMPT\"");
   }


   @Test
   public void testExpressionLoop()
   {
      runtime.run("for (file : new java.io.File(\".\").listFiles()) { echo $file.getName().toUpperCase(); }");
   }

   @Test
   public void testStatement()
   {
      runtime.run("@System.out.println('hello')");
   }

   @Test
   public void testParse()
   {
      String s = Parse.disassemble(new FSHParser("for (i=0;i<4;i++) { ls -l $dir }").parse());

      System.out.println(s);
   }

   @Test
   public void testLargeNest() {
      runtime.run("@NO_MOTD = false;\n\n" +
            "if ($NO_MOTD) {    \n" +
            "   echo \"   ____                          _____                    \";\n" +
            "   echo \"  / ___|  ___  __ _ _ __ ___    |  ___|__  _ __ __ _  ___ \";\n" +
            "   echo \"  \\\\___ \\\\ / _ \\\\/ _` | '_ ` _ \\\\   | |_ / _ \\\\| '__/ _` |/ _ \\\\  \\c{yellow}\\\\\\\\\\c\";\n" +
            "   echo \"   ___) |  __/ (_| | | | | | |  |  _| (_) | | | (_| |  __/  \\c{yellow}//\\c\";\n" +
            "   echo \"  |____/ \\\\___|\\\\__,_|_| |_| |_|  |_|  \\\\___/|_|  \\\\__, |\\\\___| \";\n" +
            "   echo \"                                                |___/      \";\n" +
            "}");

   }

}
