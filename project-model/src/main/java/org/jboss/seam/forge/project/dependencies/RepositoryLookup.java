package org.jboss.seam.forge.project.dependencies;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.apache.maven.repository.internal.MavenRepositorySystemSession;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.jboss.seam.forge.parser.java.util.Strings;
import org.jboss.seam.forge.project.ProjectModelException;
import org.jboss.seam.forge.project.facets.DependencyFacet.KnownRepository;
import org.jboss.seam.forge.project.facets.builtin.MavenContainer;
import org.jboss.seam.forge.project.services.ResourceFactory;
import org.jboss.seam.forge.resources.DependencyResource;
import org.jboss.seam.forge.shell.util.OSUtils;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.collection.CollectRequest;
import org.sonatype.aether.repository.ArtifactRepository;
import org.sonatype.aether.repository.LocalRepository;
import org.sonatype.aether.repository.RemoteRepository;
import org.sonatype.aether.resolution.ArtifactRequest;
import org.sonatype.aether.resolution.ArtifactResolutionException;
import org.sonatype.aether.resolution.ArtifactResult;
import org.sonatype.aether.resolution.DependencyRequest;
import org.sonatype.aether.resolution.DependencyResult;
import org.sonatype.aether.resolution.VersionRangeRequest;
import org.sonatype.aether.resolution.VersionRangeResult;
import org.sonatype.aether.util.artifact.DefaultArtifact;
import org.sonatype.aether.version.Version;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Dependent
public class RepositoryLookup implements DependencyResolverProvider
{
   private PlexusContainer container;
   private ResourceFactory factory;

   public RepositoryLookup()
   {
   }

   // public static final String ALT_USER_SETTINGS_XML_LOCATION = "org.apache.maven.user-settings";
   // public static final String ALT_GLOBAL_SETTINGS_XML_LOCATION = "org.apache.maven.global-settings";

   // public static final String ALT_LOCAL_REPOSITORY_LOCATION = "maven.repo.local";

   // private static final String DEFAULT_USER_SETTINGS_PATH = OSUtils.getUserHomePath().concat( "/.m2/settings.xml");
   // private static final String DEFAULT_REPOSITORY_PATH = OSUtils.getUserHomePath().concat( "/.m2/repository");

   // public static void setRemoteRepository()
   // {
   // System.setProperty(ALT_USER_SETTINGS_XML_LOCATION, "target/settings/profiles/settings.xml");
   // System.setProperty(ALT_LOCAL_REPOSITORY_LOCATION, "target/the-other-repository");
   // }

   @Inject
   public RepositoryLookup(final MavenContainer container, ResourceFactory factory)
   {
      this.container = container.getContainer();
      this.factory = factory;
   }

   @Override
   public List<Dependency> resolveVersions(final Dependency dep, final List<DependencyRepository> repositories)
   {
      List<Dependency> result = new ArrayList<Dependency>();

      List<RemoteRepository> remoteRepos = convertToMavenRepos(repositories);
      VersionRangeResult r = getVersions(dep, remoteRepos);

      for (Version v : r.getVersions())
      {
         result.add(DependencyBuilder.create(dep).setVersion(v.toString()));
      }

      return result;
   }

   @Override
   public List<DependencyResource> resolveArtifacts(Dependency dep, final List<DependencyRepository> repositories)
   {
      List<DependencyResource> result = new ArrayList<DependencyResource>();

      try
      {
         RepositorySystem system = container.lookup(RepositorySystem.class);
         MavenRepositorySystemSession session = setupRepoSession(system);

         session.setIgnoreInvalidArtifactDescriptor(true);
         session.setIgnoreMissingArtifactDescriptor(true);

         VersionRangeResult versions = getVersions(dep, convertToMavenRepos(repositories));

         VERSION: for (Version version : versions.getVersions())
         {
            ArtifactRequest request = new ArtifactRequest();
            ArtifactRepository ar = versions.getRepository(version);
            if (ar instanceof RemoteRepository)
            {
               RemoteRepository remoteRepo = new RemoteRepository(ar.getId(), ar.getContentType(),
                           ((RemoteRepository) ar).getUrl());
               request.addRepository(remoteRepo);
               DependencyBuilder currentVersion = DependencyBuilder.create(dep).setVersion(version.toString());
               request.setArtifact(new DefaultArtifact(currentVersion.toCoordinates()));

               try
               {
                  ArtifactResult a = system.resolveArtifact(session, request);

                  File file = a.getArtifact().getFile();
                  DependencyResource resource = new DependencyResource(factory, file, currentVersion);
                  if (!result.contains(resource))
                  {
                     result.add(resource);
                     continue VERSION;
                  }
               }
               catch (ArtifactResolutionException e)
               {
                  System.out.println(e.getMessage());
               }
            }
         }
      }
      catch (ComponentLookupException e)
      {
         throw new ProjectModelException("Error in dependency resolution container", e);
      }
      return result;
   }

   @Override
   public List<DependencyResource> resolveDependencies(Dependency dep, final List<DependencyRepository> repositories)
   {
      List<DependencyResource> result = new ArrayList<DependencyResource>();

      try
      {
         if (Strings.isNullOrEmpty(dep.getVersion()))
         {
            dep = DependencyBuilder.create(dep).setVersion("[,)");
         }

         RepositorySystem system = container.lookup(RepositorySystem.class);
         MavenRepositorySystemSession session = setupRepoSession(system);

         DependencyRequest request = new DependencyRequest(new CollectRequest(new org.sonatype.aether.graph.Dependency(
                  new DefaultArtifact(
                           dep.toCoordinates()), null), convertToMavenRepos(repositories)), null);

         DependencyResult artifacts = system.resolveDependencies(session, request);

         for (ArtifactResult a : artifacts.getArtifactResults())
         {
            File file = a.getArtifact().getFile();
            Dependency d = DependencyBuilder.create().setArtifactId(a.getArtifact().getArtifactId())
                     .setGroupId(a.getArtifact().getGroupId())
                     .setVersion(a.getArtifact().getVersion());
            DependencyResource resource = new DependencyResource(factory, file, d);
            result.add(resource);
         }
         return result;
      }
      catch (Exception e)
      {
         throw new ProjectModelException("Unable to resolve an artifact", e);
      }
   }

   private VersionRangeResult getVersions(Dependency dep, final List<RemoteRepository> repositories)
   {
      try
      {
         String version = dep.getVersion();
         if (Strings.isNullOrEmpty(version))
         {
            dep = DependencyBuilder.create(dep).setVersion("[,)");
         }
         else if (!version.matches("(\\(|\\[).*?(\\)|\\])"))
         {
            dep = DependencyBuilder.create(dep).setVersion("[" + version + "]");
         }

         RepositorySystem maven = container.lookup(RepositorySystem.class);
         MavenRepositorySystemSession session = setupRepoSession(maven);

         VersionRangeRequest rangeRequest = new VersionRangeRequest(new DefaultArtifact(dep.toCoordinates()),
                  repositories, null);

         VersionRangeResult rangeResult = maven.resolveVersionRange(session, rangeRequest);
         return rangeResult;
      }
      catch (Exception e)
      {
         throw new ProjectModelException("Failed to look up versions for [" + dep + "]", e);
      }
   }

   private MavenRepositorySystemSession setupRepoSession(RepositorySystem repoSystem)
   {
      MavenRepositorySystemSession session = new MavenRepositorySystemSession();

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

      if (temp.isEmpty())
      {
         temp.add(central);
      }

      List<RemoteRepository> remoteRepos = new ArrayList<RemoteRepository>();
      for (DependencyRepository deprep : temp)
      {
         remoteRepos.add(convertToMavenRepo(deprep));
      }
      return remoteRepos;
   }

   private RemoteRepository convertToMavenRepo(DependencyRepository repo)
   {
      return new RemoteRepository(repo.getId(), "default", repo.getUrl());
   }

}