package org.jboss.seam.sidekick.shell.cli.builtin;

import org.jboss.seam.sidekick.shell.Shell;
import org.jboss.seam.sidekick.shell.cli.PluginRegistry;
import org.jboss.seam.sidekick.shell.plugins.plugins.DefaultCommand;
import org.jboss.seam.sidekick.shell.plugins.plugins.Help;
import org.jboss.seam.sidekick.shell.plugins.plugins.Option;
import org.jboss.seam.sidekick.shell.plugins.plugins.Plugin;
import org.mvel2.util.StringAppender;

import javax.inject.Inject;
import javax.inject.Named;

import static java.lang.String.valueOf;
import static org.mvel2.MVEL.eval;

/**
 * User: christopherbrock
 * Date: 1-Sep-2010
 * Time: 5:04:36 PM
 */
@Named("exec")
@Help("Executes an expression")
public class ScriptExecPlugin implements Plugin
{
   private final Shell shell;
   private final PluginRegistry registry;

   @Inject
   public ScriptExecPlugin(Shell shell, PluginRegistry registry)
   {
      this.shell = shell;
      this.registry = registry;
   }

   @DefaultCommand
   public void execScript(@Option(required = true, description = "expr") String... expr)
   {
      StringAppender appender = new StringAppender();
      for (String s : expr)
         appender.append(s);

      Object retVal = eval(appender.toString(), new ScriptContext(), shell.getProperties());

      if (retVal != null)
         shell.println(valueOf(retVal));
   }

   public class ScriptContext
   {
      public void cmd(String cmd)
      {
         shell.execute(cmd);
      }
   }
}
