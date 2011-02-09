package org.jboss.seam.forge.scaffold.plugins;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.jboss.seam.forge.shell.completer.SimpleTokenCompleter;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class ProfileCompleter extends SimpleTokenCompleter
{
   @Inject
   private GenPlugin gen;

   @Override
   public List<Object> getCompletionTokens()
   {
      return new ArrayList<Object>(gen.getProfiles().keySet());
   }

}