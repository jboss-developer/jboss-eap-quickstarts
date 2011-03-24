package org.jboss.seam.forge.project.facets;

import org.jboss.seam.forge.project.Facet;
import org.jboss.seam.forge.project.Project;

/**
 * A base convenience {@link Facet} abstract class.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public abstract class BaseFacet implements Facet
{
   protected Project project;

   @Override
   public Project getProject()
   {
      return this.project;
   }

   @Override
   public void setProject(final Project project)
   {
      this.project = project;
   }

   @Override
   public boolean uninstall()
   {
      return false;
   }

}
