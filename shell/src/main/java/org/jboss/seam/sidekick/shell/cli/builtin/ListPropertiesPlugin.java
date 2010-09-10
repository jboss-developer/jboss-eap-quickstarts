package org.jboss.seam.sidekick.shell.cli.builtin;

import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.sidekick.shell.Shell;
import org.jboss.seam.sidekick.shell.plugins.DefaultCommand;
import org.jboss.seam.sidekick.shell.plugins.Help;
import org.jboss.seam.sidekick.shell.plugins.Plugin;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Named("list-properties")
@Help("Lists all current shell properties")
public class ListPropertiesPlugin implements Plugin
{
   final Shell shell;

   @Inject
   public ListPropertiesPlugin(final Shell shell)
   {
      this.shell = shell;
   }

   @DefaultCommand
   public void listProperties()
   {
      Map<String, Object> properties = shell.getProperties();

      for (Entry<String, Object> entry : properties.entrySet())
      {
         String key = entry.getKey();
         Object value = entry.getValue();

         shell.print(key + "=");
         if (value != null)
         {
            shell.print(value.toString());
         }
         shell.println();
      }
   }
}
