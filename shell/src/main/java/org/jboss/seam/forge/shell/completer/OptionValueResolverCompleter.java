package org.jboss.seam.forge.shell.completer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
      if (option != null)
      {
         ArrayList<String> results = new ArrayList<String>();

         Map<OptionMetadata, Object> optionValueMap = state.getOptionValueMap();
         if (optionValueMap.isEmpty() || optionValueMap.containsKey(option))
         {
            if (option.hasCustomCompleter())
            {
               CommandCompleter completer = BeanManagerUtils.getContextualInstance(manager, option.getCompleterType());
               final List<CharSequence> candidates = new ArrayList<CharSequence>();

               completer.complete(state);
               for (CharSequence c : candidates)
               {
                  results.add(c.toString());
               }
            }
            else if (isResourceAssignable(option))
            {
               String[] values;

               if (optionValueMap.isEmpty())
               {
                  values = new String[] { "" };
               }
               else if (optionValueMap.get(option) instanceof String[])
               {
                  values = (String[]) optionValueMap.get(option);
               }
               else if (optionValueMap.get(option) == null)
               {
                  values = new String[] { "" };
               }
               else
               {
                  values = new String[] { String.valueOf(optionValueMap.get(option)) };
               }

               String val = values[values.length - 1];

               for (Resource<?> r : new PathspecParser(resourceFactory, shell.getCurrentResource(), val + "*")
                        .resolve())
               {
                  // Add result to the results list, and append a '/' if the
                  // resource has children.
                  results.add(r.getName() + (r.isFlagSet(ResourceFlag.Node) ? "/" : ""));
               }

               int lastNest = val.lastIndexOf(File.separatorChar);

               // Record the current index point in the buffer. If we're at
               // the separator char
               // set the value ahead by 1.
               state.setIndex(state.getIndex() - val.length() + (lastNest != -1 ? lastNest + 1 : 0));
            }
         }
         state.getCandidates().addAll(results);
      }
   }

   private boolean isResourceAssignable(final OptionMetadata option)
   {
      return Resource[].class.isAssignableFrom(option.getBoxedType())
               || Resource.class.isAssignableFrom(option.getBoxedType());
   }

}
