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
package org.jboss.seam.forge.shell.test;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.seam.forge.shell.ShellPrompt;
import org.jboss.seam.forge.shell.exceptions.AbortedException;
import org.jboss.seam.forge.shell.plugins.Alias;
import org.jboss.seam.forge.shell.plugins.DefaultCommand;
import org.jboss.seam.forge.shell.plugins.Plugin;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@ApplicationScoped
@Alias("test-map")
public class MockAbortingPlugin implements Plugin
{
   private boolean aborted = false;
   private boolean executed = false;

   @Inject
   ShellPrompt prompt;

   @DefaultCommand
   public void run()
   {
      try
      {
         this.executed = true;
         this.aborted = false;
         String value = prompt.prompt("Waiting for EOF");
         System.out.println(value);
      }
      catch (AbortedException e)
      {
         this.aborted = true;
      }
   }

   public boolean isAborted()
   {
      return aborted;
   }

   public boolean isExecuted()
   {
      return executed;
   }
}
