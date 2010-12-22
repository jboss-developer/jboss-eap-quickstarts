package org.jboss.seam.forge.shell.completer;


public class NullCommandCompleter implements CommandCompleter
{

   @Override
   public void complete(CommandCompleterState state)
   {
      throw new UnsupportedOperationException("The " + getClass().getSimpleName() + " completer should be replaced with an actual CommandCompleter");
   }

}
