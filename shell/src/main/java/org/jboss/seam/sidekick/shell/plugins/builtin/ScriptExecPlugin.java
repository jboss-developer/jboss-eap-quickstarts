package org.jboss.seam.sidekick.shell.plugins.builtin;

import static java.lang.String.valueOf;
import static org.mvel2.MVEL.eval;

import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.sidekick.shell.Shell;
import org.jboss.seam.sidekick.shell.plugins.DefaultCommand;
import org.jboss.seam.sidekick.shell.plugins.Help;
import org.jboss.seam.sidekick.shell.plugins.Option;
import org.jboss.seam.sidekick.shell.plugins.Plugin;
import org.mvel2.util.StringAppender;

/**
 * User: christopherbrock Date: 1-Sep-2010 Time: 5:04:36 PM
 */
@Named("exec")
@Help("Executes an expression")
public class ScriptExecPlugin implements Plugin
{
   private final Shell shell;

   @Inject
   public ScriptExecPlugin(final Shell shell)
   {
      this.shell = shell;
   }

   @DefaultCommand
   public void execScript(@Option(required = true, description = "expr") final String... expr)
   {
      StringAppender appender = new StringAppender();
      for (String s : expr)
      {
         appender.append(s);
      }

      Object retVal = eval(appender.toString(), new ScriptContext(), shell.getProperties());

      if (retVal != null)
      {
         shell.println(valueOf(retVal));
      }
   }

   public class ScriptContext
   {
      public void cmd(final String cmd)
      {
         shell.execute(cmd);
      }
   }
}
