/*
 * 
 */

package org.jboss.seam.forge.dev;

import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.forge.project.PackagingType;
import org.jboss.seam.forge.project.constraints.RequiresFacets;
import org.jboss.seam.forge.project.constraints.RequiresPackagingType;
import org.jboss.seam.forge.project.dependencies.DependencyBuilder;
import org.jboss.seam.forge.project.facets.BaseFacet;
import org.jboss.seam.forge.project.facets.DependencyFacet;
import org.jboss.seam.forge.project.facets.PackagingFacet;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.spec.cdi.CDIFacet;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Named("forge.api")
@RequiresFacets({ DependencyFacet.class, PackagingFacet.class, CDIFacet.class })
@RequiresPackagingType(PackagingType.JAR)
public class ForgeAPIFacet extends BaseFacet
{
   private final DependencyBuilder dep = DependencyBuilder.create("org.jboss.seam.forge:forge-shell-api");

   @Inject
   private Shell shell;

   @Override
   public boolean install()
   {
      DependencyFacet deps = project.getFacet(DependencyFacet.class);

      // FIXME This needs to dynamically resolve artifact versions from repos
      deps.setProperty("forge.api.version", "1.0.0-SNAPSHOT");
      DependencyBuilder dep = DependencyBuilder.create("org.jboss.seam.forge:forge-shell-api:${forge.api.version}");
      deps.addDependency(dep);
      return true;
   }

   @Override
   public boolean isInstalled()
   {
      PackagingType packagingType = project.getFacet(PackagingFacet.class).getPackagingType();
      return project.getFacet(DependencyFacet.class).hasDependency(dep)
               && PackagingType.JAR.equals(packagingType);
   }
}
