package org.jboss.seam.forge.shell.completer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.jboss.seam.forge.project.Facet;
import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.constraints.ConstraintInspector;
import org.jboss.seam.forge.project.services.FacetFactory;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.completer.CommandCompleter;

public class AvailableFacetsCompleter implements CommandCompleter
{
   @Inject 
   private FacetFactory factory;
   
   @Inject 
   private Shell shell;

   @Override
   public int complete(String buffer, int cursor, List<CharSequence> candidates)
   {
      Project project = shell.getCurrentProject();
      List<Facet> allFacets = factory.getFacets();
      List<Class<? extends Facet>> uninstalledFacets = new ArrayList<Class<? extends Facet>>();
      
      for (Facet facet : allFacets)
      {
         if(!project.hasFacet(facet.getClass()))
         {
            uninstalledFacets.add(facet.getClass());
         }
      }
      
      for (Class<? extends Facet> type : uninstalledFacets)
      {
         String name = ConstraintInspector.getName(type);
         if(name.startsWith(buffer))
         {
            candidates.add(name + " ");
         }
      }
      
      if(candidates.isEmpty())
      {
         return 0;
      }
      if(buffer.length() == 0)
      {
         return cursor;
      }
      return buffer.length();
   }

}
