package org.jboss.seam.forge.shell.completer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.ResourceFlag;
import org.jboss.seam.forge.project.services.ResourceFactory;
import org.jboss.seam.forge.project.util.BeanManagerUtils;
import org.jboss.seam.forge.project.util.PathspecParser;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.command.OptionMetadata;
import org.jboss.seam.forge.shell.command.parser.CommandParser;
import org.jboss.seam.forge.shell.command.parser.CommandParserContext;
import org.jboss.seam.forge.shell.command.parser.CompositeCommandParser;
import org.jboss.seam.forge.shell.command.parser.NamedBooleanOptionParser;
import org.jboss.seam.forge.shell.command.parser.NamedValueOptionParser;
import org.jboss.seam.forge.shell.command.parser.NamedValueVarargsOptionParser;
import org.jboss.seam.forge.shell.command.parser.OrderedValueOptionParser;
import org.jboss.seam.forge.shell.command.parser.OrderedValueVarargsOptionParser;

public class OptionValueResolverCompleter implements CommandCompleter
{
   private final CommandParser commandParser = new CompositeCommandParser(
         new NamedBooleanOptionParser(),
         new NamedValueOptionParser(),
         new NamedValueVarargsOptionParser(),
         new OrderedValueOptionParser(),
         new OrderedValueVarargsOptionParser());

   @Inject
   private Shell shell;

   @Inject
   private BeanManager manager;
   @Inject
   private ResourceFactory resourceFactory;

   @Override
   public void complete(CommandCompleterState st)
   {
      PluginCommandCompleterState state = ((PluginCommandCompleterState) st);

      OptionMetadata option = state.getOption();
      if (option != null)
      {
         Queue<String> tokens = state.getTokens();
         ArrayList<String> results = new ArrayList<String>();
         Map<OptionMetadata, Object> valueMap = commandParser.parse(state.getCommand(), tokens, new CommandParserContext());

         // TODO determine which option came last, if it had a value, if the
         // value
         // can be hinted

         if (!valueMap.containsKey(option) && option.isRequired())
         {
            if (option.isNamed())
            {
               String prefix = "";
               if (!state.isFinalTokenComplete())
               {
                  prefix = " ";
               }
               results.add(prefix + "--" + option.getName() + " ");
            }
            else
            {
               results.add("");
            }
         }
         else if (valueMap.isEmpty() || valueMap.containsKey(option))
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

               if (valueMap.isEmpty())
               {
                  values = new String[] { "" };
               }
               else if (valueMap.get(option) instanceof String[])
               {
                  values = (String[]) valueMap.get(option);
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
