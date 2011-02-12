package org.jboss.seam.forge.shell.completer;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.ResourceFlag;
import org.jboss.seam.forge.project.services.ResourceFactory;
import org.jboss.seam.forge.project.util.BeanManagerUtils;
import org.jboss.seam.forge.project.util.PathspecParser;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.command.OptionMetadata;
import org.jboss.seam.forge.shell.command.parser.CommandParserContext;

public class OptionValueResolverCompleter implements CommandCompleter
{

   @Inject
   private Shell shell;

   @Inject
   private BeanManager manager;
   @Inject
   private ResourceFactory resourceFactory;

   @Override
   public void complete(final CommandCompleterState st)
   {
      PluginCommandCompleterState state = ((PluginCommandCompleterState) st);

      OptionMetadata option = state.getOption();
      if ((option != null) && (state.getCommandContext() != null))
      {

         CommandParserContext commandContext = state.getCommandContext();
         Map<OptionMetadata, Object> valueMap = commandContext.getValueMap();
         if (valueMap.isEmpty() || valueMap.containsKey(option))
         {
            if (option.hasCustomCompleter())
            {
               CommandCompleter completer = BeanManagerUtils.getContextualInstance(manager, option.getCompleterType());
               completer.complete(state);
            }
            if (option.isEnum())
            {
               @SuppressWarnings("unchecked")
               EnumCompleter completer = new EnumCompleter((Class<Enum<?>>) option.getType());
               completer.complete(state);
            }
            else if (isResourceAssignable(option))
            {
               ArrayList<String> results = new ArrayList<String>();
               String[] values;

               if (valueMap.isEmpty())
               {
                  values = new String[] { "" };
               }
               else if (valueMap.get(option) instanceof String[])
               {
                  values = (String[]) valueMap.get(option);
               }
               else if (valueMap.get(option) == null)
               {
                  values = new String[] { "" };
               }
               else
               {
                  values = new String[] { String.valueOf(valueMap.get(option)) };
               }

               String val = values[values.length - 1];

               for (Resource<?> r : new PathspecParser(resourceFactory, shell.getCurrentResource(), val + "*")
                        .resolve())
               {
                  // Add result to the results list, and append a '/' if the
                  // resource has children.
                  String name = ("~".equals(val) ? "~/" : "") + r.getName()
                           + (r.isFlagSet(ResourceFlag.Node) ? "/" : "");
                  results.add(name);
               }

               int lastNest = val.lastIndexOf(File.separatorChar);

               // Record the current index point in the buffer. If we're at
               // the separator char
               // set the value ahead by 1.
               state.setIndex(state.getIndex() - val.length() + (lastNest != -1 ? lastNest + 1 : 0));
               state.getCandidates().addAll(results);
            }
         }
      }
   }

   private boolean isResourceAssignable(final OptionMetadata option)
   {
      return Resource[].class.isAssignableFrom(option.getBoxedType())
               || Resource.class.isAssignableFrom(option.getBoxedType());
   }

}
