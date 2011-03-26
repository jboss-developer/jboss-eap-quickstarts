package org.jboss.seam.forge.project.facets.builtin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.apache.maven.repository.internal.MavenRepositorySystemSession;
import org.codehaus.plexus.PlexusContainer;
import org.jboss.seam.forge.project.ProjectModelException;
import org.jboss.seam.forge.project.dependencies.Dependency;
import org.jboss.seam.forge.project.dependencies.DependencyBuilder;
import org.jboss.seam.forge.project.dependencies.DependencyRepository;
import org.jboss.seam.forge.project.dependencies.DependencyRepositoryImpl;
import org.jboss.seam.forge.project.dependencies.DependencyResolverProvider;
import org.jboss.seam.forge.project.facets.DependencyFacet.KnownRepository;
import org.jboss.seam.forge.project.services.ResourceFactory;
import org.jboss.seam.forge.resources.DependencyResource;
import org.jboss.seam.forge.shell.util.OSUtils;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.collection.CollectRequest;
import org.sonatype.aether.repository.LocalRepository;
import org.sonatype.aether.repository.RemoteRepository;
import org.sonatype.aether.resolution.ArtifactResult;
import org.sonatype.aether.resolution.DependencyRequest;
import org.sonatype.aether.resolution.DependencyResult;
import org.sonatype.aether.resolution.VersionRangeRequest;
import org.sonatype.aether.resolution.VersionRangeResult;
import org.sonatype.aether.util.artifact.DefaultArtifact;
import org.sonatype.aether.version.Version;

@Dependent
public class RepositoryLookup implements DependencyResolverProvider
{
   private PlexusContainer container;
   private ResourceFactory factory;

   public RepositoryLookup()
   {
   }

   @Inject
   public RepositoryLookup(final MavenContainer container, ResourceFactory factory)
   {
      this.container = container.getContainer();
      this.factory = factory;
   }

   @Override
   public List<Dependency> resolveVersions(final Dependency dep, final List<DependencyRepository> repositories)
   {
      List<RemoteRepository> remoteRepos = convertToMavenRepos(repositories);
      List<String> versions = getVersions(dep, remoteRepos);

      List<Dependency> result = new ArrayList<Dependency>();
      for (String version : versions)
      {
         result.add(DependencyBuilder.create(dep).setVersion(version));
      }

      return result;
   }

   @Override
   public List<DependencyResource> resolveArtifacts(final Dependency dep, final List<DependencyRepository> repositories)
   {
      List<DependencyResource> result = new ArrayList<DependencyResource>();

      try
      {
         RepositorySystem system = container.lookup(RepositorySystem.class);
         MavenRepositorySystemSession session = setupRepoSession(system);

         DependencyRequest request = new DependencyRequest(new CollectRequest(new org.sonatype.aether.graph.Dependency(
                  new DefaultArtifact(
                           dep.toCoordinates()), null), convertToMavenRepos(repositories)), null);

         DependencyResult artifacts = system.resolveDependencies(session, request);
         Collection<File> files = new ArrayList<File>();
         for (ArtifactResult artifact : artifacts.getArtifactResults())
         {
            Artifact a = artifact.getArtifact();
            if ("pom".equals(a.getExtension()))
            {
               continue;
            }
            files.add(a.getFile());
         }

         for (ArtifactResult a : artifacts.getArtifactResults())
         {
            File file = a.getArtifact().getFile();
            DependencyResource resource = new DependencyResource(factory, file, dep);
            result.add(resource);
         }
         return result;
      }
      catch (Exception e)
      {
         throw new ProjectModelException("Unable to resolve an artifact", e);
      }
   }

   private List<String> getVersions(final Dependency dep, final List<RemoteRepository> repositories)
   {
      try
      {
         RepositorySystem maven = container.lookup(RepositorySystem.class);
         MavenRepositorySystemSession session = setupRepoSession(maven);

         // FIXME session.getCache() might need to be fussed with. reporequests cache data between projects.

         VersionRangeRequest rangeRequest = new VersionRangeRequest();
         rangeRequest.setArtifact(new DefaultArtifact(dep.toCoordinates()));
         for (RemoteRepository repository : repositories)
         {
            rangeRequest.addRepository(repository);
         }

         VersionRangeResult rangeResult = maven.resolveVersionRange(session, rangeRequest);

         List<String> results = new ArrayList<String>();
         for (Version version : rangeResult.getVersions())
         {
            results.add(version.toString());
         }
         return results;
      }
      catch (Exception e)
      {
         throw new ProjectModelException("Failed to look up versions for [" + dep + "]", e);
      }
   }

   private MavenRepositorySystemSession setupRepoSession(RepositorySystem repoSystem)
   {
      MavenRepositorySystemSession session = new MavenRepositorySystemSession();

      // TODO this reference to the M2_REPO should probably be centralized
      LocalRepository localRepo = new LocalRepository(OSUtils.getUserHomeDir().getAbsolutePath() + "/.m2/repository");
      session.setLocalRepositoryManager(repoSystem.newLocalRepositoryManager(localRepo));
      session.setOffline(false);

      session.setTransferErrorCachingEnabled(false);
      session.setNotFoundCachingEnabled(false);
      return session;
   }

   private List<RemoteRepository> convertToMavenRepos(final List<DependencyRepository> repositories)
   {
      List<DependencyRepository> temp = new ArrayList<DependencyRepository>();
      temp.addAll(repositories);
      DependencyRepository central = new DependencyRepositoryImpl(KnownRepository.CENTRAL.getId(),
                  KnownRepository.CENTRAL.getUrl());

      if (!temp.contains(central))
      {
         temp.add(central);
      }

      List<RemoteRepository> remoteRepos = new ArrayList<RemoteRepository>();
      for (DependencyRepository deprep : temp)
      {
         remoteRepos.add(new RemoteRepository(deprep.getId(), "default", deprep.getUrl()));
      }
      return remoteRepos;
   }

}