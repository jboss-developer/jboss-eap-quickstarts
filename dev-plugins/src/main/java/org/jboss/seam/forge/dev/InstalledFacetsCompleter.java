package org.jboss.seam.forge.dev;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.jboss.seam.forge.project.Facet;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.services.FacetFactory;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.completer.SimpleTokenCompleter;
import org.jboss.seam.forge.shell.util.ConstraintInspector;

public class InstalledFacetsCompleter extends SimpleTokenCompleter
{
   @Inject
   private FacetFactory factory;

   @Inject
   private Shell shell;

   @Override
   public List<Object> getCompletionTokens()
   {
      List<Object> result = new ArrayList<Object>();

      Project project = shell.getCurrentProject();
      List<Facet> allFacets = factory.getFacets();
      for (Facet facet : allFacets)
      {
         if (project.hasFacet(facet.getClass()))
         {
            result.add(ConstraintInspector.getName(facet.getClass()));
         }
      }

      return result;
   }

}
