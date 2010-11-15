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

import com.sun.tools.javac.util.Log;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.command.Execution;
import org.jboss.seam.forge.shell.command.ExecutionParser;
import org.jboss.seam.forge.shell.command.PluginRegistry;
import org.mvel2.MVEL;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Queue;

/**
 * @author Mike Brock .
 */
@Singleton
public class FSHRuntime
{
   private final Shell shell;
   private final PluginRegistry pluginRegistry;
   private final Instance<Execution> executionInstance;
   private final ExecutionParser executionParser;

   @Inject
   public FSHRuntime(Shell shell, PluginRegistry pluginRegistry,
                     Instance<Execution> executionInstance,
                     ExecutionParser executionParser)
   {
      this.shell = shell;
      this.pluginRegistry = pluginRegistry;
      this.executionInstance = executionInstance;
      this.executionParser = executionParser;
   }

   public void run(final String str)
   {
      run(new FSHParser(str).parse());
   }

   public Object run(final Node startNode)
   {
      Execution execution = executionInstance.get();
      AutoReducingQueue arQueue = null;

      Node n = startNode;
      do
      {
         if (n instanceof LogicalStatement)
         {
            arQueue = new AutoReducingQueue(((LogicalStatement) n).getNest(), this);
         }
         else if (n instanceof PipeNode)
         {
            System.out.print(" -pipe-> " + n);
         }
         else
         {
            throw new RuntimeException("badly formed stack:" + n);
         }

         for (String s : arQueue)
         {
            System.out.print(s);
            System.out.print(" ");
         }
         System.out.println();
      }
      while ((n = n.next) != null);


      return null;
   }

   public Shell getShell()
   {
      return shell;
   }

   public PluginRegistry getPluginRegistry()
   {
      return pluginRegistry;
   }

   public Instance<Execution> getExecutionInstance()
   {
      return executionInstance;
   }
}
