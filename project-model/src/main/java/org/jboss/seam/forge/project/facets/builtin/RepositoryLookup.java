package org.jboss.seam.forge.project.facets.builtin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.apache.maven.repository.internal.MavenRepositorySystemSession;
import org.codehaus.plexus.PlexusContainer;
import org.jboss.seam.forge.project.ProjectModelException;
import org.jboss.seam.forge.project.dependencies.DependencyRepository;
import org.jboss.seam.forge.project.facets.DependencyFacet;
import org.jboss.seam.forge.shell.util.OSUtils;
import org.sonatype.aether.RepositoryException;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.repository.LocalRepository;
import org.sonatype.aether.repository.RemoteRepository;
import org.sonatype.aether.resolution.VersionRangeRequest;
import org.sonatype.aether.resolution.VersionRangeResult;
import org.sonatype.aether.util.artifact.DefaultArtifact;
import org.sonatype.aether.version.Version;

@Dependent
public class RepositoryLookup
{
   private final PlexusContainer container;

   @Inject
   public RepositoryLookup(final MavenContainer container)
   {
      this.container = container.getContainer();
   }

   public List<String> getAvailableVersions(final String gavs, final List<DependencyRepository> repositories)
   {
      List<DependencyRepository> internal = new ArrayList<DependencyRepository>();
      internal.addAll(repositories);

      try
      {
         if (!internal.contains(DependencyFacet.KnownRepository.CENTRAL.toRepository()))
         {
            internal.add(DependencyFacet.KnownRepository.CENTRAL.toRepository());
         }

         List<RemoteRepository> remoteRepos = new ArrayList<RemoteRepository>();
         for (DependencyRepository deprep : internal)
         {
            remoteRepos.add(new RemoteRepository(deprep.getId(), "default", deprep.getUrl()));
         }

         RepositorySystem repoSystem = container.lookup(RepositorySystem.class);
         return getVersions(repoSystem, gavs, remoteRepos);
      }
      catch (Exception e)
      {
         throw new ProjectModelException("Failed to look up versions for [" + gavs + "]", e);
      }
   }

   public List<String> getAvailableVersions(final String gavs, final DependencyRepository... dependencyRepos)
   {
      return getAvailableVersions(gavs, Arrays.asList(dependencyRepos));
   }

   private List<String> getVersions(final RepositorySystem repoSystem, final String gavs,
            final List<RemoteRepository> repositories)
            throws RepositoryException
   {

      MavenRepositorySystemSession session = new MavenRepositorySystemSession();

      // TODO this reference to the M2_REPO should probably be centralized
      LocalRepository localRepo = new LocalRepository(OSUtils.getUserHomeDir().getAbsolutePath() + "/.m2/repository");
      session.setLocalRepositoryManager(repoSystem.newLocalRepositoryManager(localRepo));
      session.setOffline(false);

      session.setTransferErrorCachingEnabled(false);
      session.setNotFoundCachingEnabled(false);

      // FIXME session.getCache() might need to be fussed with. reporequests cache data between projects.

      VersionRangeRequest rangeRequest = new VersionRangeRequest();
      rangeRequest.setArtifact(new DefaultArtifact(gavs));
      for (RemoteRepository repository : repositories)
      {
         rangeRequest.addRepository(repository);
      }

      VersionRangeResult rangeResult = repoSystem.resolveVersionRange(session, rangeRequest);

      List<String> results = new ArrayList<String>();
      for (Version version : rangeResult.getVersions())
      {
         results.add(version.toString());
      }
      return results;
   }

}