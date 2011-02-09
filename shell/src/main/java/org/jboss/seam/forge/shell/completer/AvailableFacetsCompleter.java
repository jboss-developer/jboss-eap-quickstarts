package org.jboss.seam.forge.shell.completer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.jboss.seam.forge.project.Facet;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.constraints.ConstraintInspector;
import org.jboss.seam.forge.project.services.FacetFactory;
import org.jboss.seam.forge.shell.Shell;

public class AvailableFacetsCompleter extends SimpleTokenCompleter
{
   @Inject
   private FacetFactory factory;

   @Inject
   private Shell shell;

   @Override
   public List<Object> getCompletionTokens()
   {
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

      List<Object> result = new ArrayList<Object>();
      for (Class<? extends Facet> type : uninstalledFacets)
      {
         String name = ConstraintInspector.getName(type);
         result.add(name + " ");
      }
      return result;
   }

}
