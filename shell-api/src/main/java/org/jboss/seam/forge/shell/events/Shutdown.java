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
package org.jboss.seam.forge.shell.events;

/**
 * Fired as a signal to the shell that it should shut down now.
 * <p>
 * <strong>For example:</strong>
 * <p>
 * <code>@Inject Event&lt;Shutdown&gt shutdown; 
 * <br/>
 *    ...
 * <br/>
 * shutdown.fire(new Shutdown(Shutdown.Status.NORMAL));
 * </code>
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public final class Shutdown
{
   private final Status status;

   /**
    * Status of the shell during shutdown.
    */
   public enum Status
   {
      /**
       * The shell is shutting down normally.
       */
      NORMAL,
      /**
       * A fatal error has forced the shell to shut down.
       */
      ERROR
   }

   /**
    * Defaults to {@link Status#NORMAL}
    */
   public Shutdown()
   {
      this.status = Status.NORMAL;
   }

   /**
    * Inform the shell to shut down with the given {@link Status}, now.
    */
   public Shutdown(final Status status)
   {
      this.status = status;
   }

   /**
    * Get the status with which the shell should shut down.
    */
   public Status getStatus()
   {
      return status;
   }
}
