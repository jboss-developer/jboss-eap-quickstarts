package org.jboss.seam.forge.shell.completer;

import java.util.List;

public class NullCommandCompleter implements CommandCompleter
{

   @Override
   public int complete(String buffer, int cursor, List<CharSequence> candidates)
   {
      throw new UnsupportedOperationException("The " + getClass().getSimpleName() + " completer should be replaced with an actual CommandCompleter");
   }

}
