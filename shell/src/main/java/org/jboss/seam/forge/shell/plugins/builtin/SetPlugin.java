package org.jboss.seam.forge.shell.plugins.builtin;

import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.*;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

/**
 * @author Mike Brock .
 */
@Named("set")
@Topic("Shell Environment")
@Help("Sets and lists environment variables")
public class SetPlugin implements Plugin
{
   private Shell shell;

   @Inject
   public SetPlugin(Shell shell)
   {
      this.shell = shell;
   }

   @DefaultCommand
   public void run(@Option(description = "varname") String variable,
                   @Option(description = "value") String... value)
   {

      if (variable == null)
      {
         listVars();
      }
      else
      {
         shell.setProperty(variable, Echo.tokensToString(value));
      }
   }

   private void listVars()
   {
      for (Map.Entry<String, Object> entry : shell.getProperties().entrySet())
      {
         shell.println(entry.getKey() + "=" + entry.getValue());
      }
   }

}
