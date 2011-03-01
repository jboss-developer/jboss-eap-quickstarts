/*
 * 
 */

package org.jboss.seam.forge.dev;

import java.util.List;

import javax.inject.Inject;

import org.jboss.seam.forge.project.dependencies.Dependency;
import org.jboss.seam.forge.project.dependencies.DependencyBuilder;
import org.jboss.seam.forge.project.facets.BaseFacet;
import org.jboss.seam.forge.project.facets.DependencyFacet;
import org.jboss.seam.forge.project.facets.PackagingFacet;
import org.jboss.seam.forge.project.packaging.PackagingType;
import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.plugins.Alias;
import org.jboss.seam.forge.shell.plugins.RequiresFacet;
import org.jboss.seam.forge.shell.plugins.RequiresPackagingType;
import org.jboss.seam.forge.spec.cdi.CDIFacet;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Alias("forge.api")
@RequiresFacet({ DependencyFacet.class, PackagingFacet.class, CDIFacet.class })
@RequiresPackagingType(PackagingType.JAR)
public class ForgeAPIFacet extends BaseFacet
{

   @Inject
   private Shell shell;

   @Override
   public boolean install()
   {
      DependencyFacet deps = project.getFacet(DependencyFacet.class);

      List<Dependency> versions = deps.resolveAvailableVersions("org.jboss.seam.forge:forge-shell-api:[,]");
      Dependency version = shell.promptChoiceTyped("Install which version of the Forge API?", versions);
      deps.setProperty("forge.api.version", version.getVersion());
      DependencyBuilder dep = DependencyBuilder.create("org.jboss.seam.forge:forge-shell-api:${forge.api.version}");
      deps.addDependency(dep);
      return true;
   }

   @Override
   public boolean isInstalled()
   {
      Dependency dep = DependencyBuilder.create("org.jboss.seam.forge:forge-shell-api");
      PackagingType packagingType = project.getFacet(PackagingFacet.class).getPackagingType();
      return project.getFacet(DependencyFacet.class).hasDependency(dep)
               && PackagingType.JAR.equals(packagingType);
   }
}
