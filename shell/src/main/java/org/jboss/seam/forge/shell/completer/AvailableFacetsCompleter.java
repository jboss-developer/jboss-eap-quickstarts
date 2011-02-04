package org.jboss.seam.forge.shell.completer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.jboss.seam.forge.project.Facet;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.constraints.ConstraintInspector;
import org.jboss.seam.forge.project.services.FacetFactory;
import org.jboss.seam.forge.shell.Shell;

public class AvailableFacetsCompleter implements CommandCompleter
{
   @Inject
   private FacetFactory factory;

   @Inject
   private Shell shell;

   @Override
   public void complete(final CommandCompleterState st)
   {
      PluginCommandCompleterState state = ((PluginCommandCompleterState) st);

      Project project = shell.getCurrentProject();
      List<Facet> allFacets = factory.getFacets();
      List<Class<? extends Facet>> uninstalledFacets = new ArrayList<Class<? extends Facet>>();

      for (Facet facet : allFacets)
      {
         if (!project.hasFacet(facet.getClass()))
         {
            uninstalledFacets.add(facet.getClass());
         }
      }

      String peek = state.getTokens().peek();
      if (peek == null)
      {
         for (Class<? extends Facet> type : uninstalledFacets)
         {
            String name = ConstraintInspector.getName(type);
            state.getCandidates().add(name + " ");
         }
      }
      else
      {
         for (Class<? extends Facet> type : uninstalledFacets)
         {
            String name = ConstraintInspector.getName(type);
            if (name.startsWith(peek))
            {
               state.getCandidates().add(name + " ");
               state.setIndex(state.getOriginalIndex() - (peek == null ? 0 : peek.length()));
            }
         }
      }
   }

}
