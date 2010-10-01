package org.jboss.seam.forge.shell.exceptions;

import org.jboss.seam.forge.shell.command.CommandMetadata;

/**
 * User: christopherbrock
 * Date: 1-Sep-2010
 * Time: 7:07:39 PM
 */
public class NoSuchCommandException extends CommandExecutionException
{
   private static final long serialVersionUID = -5108590337529122915L;

   public NoSuchCommandException(CommandMetadata command, String message)
   {
      super(command, message);
   }

   public NoSuchCommandException(CommandMetadata command, Throwable e)
   {
      super(command, e);
   }

   public NoSuchCommandException(CommandMetadata command, String message, Throwable e)
   {
      super(command, message, e);
   }
}
