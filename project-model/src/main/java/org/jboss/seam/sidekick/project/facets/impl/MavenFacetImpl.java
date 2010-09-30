/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.seam.sidekick.project.facets.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.enterprise.inject.Typed;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryPolicy;
import org.apache.maven.artifact.repository.MavenArtifactRepository;
import org.apache.maven.artifact.repository.layout.ArtifactRepositoryLayout;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.apache.maven.project.DefaultProjectBuildingRequest;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.project.ProjectBuildingRequest;
import org.apache.maven.project.ProjectBuildingResult;
import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.logging.console.ConsoleLoggerManager;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.jboss.seam.sidekick.project.Facet;
import org.jboss.seam.sidekick.project.Project;
import org.jboss.seam.sidekick.project.ProjectModelException;
import org.jboss.seam.sidekick.project.facets.MavenFacet;
import org.sonatype.aether.impl.internal.SimpleLocalRepositoryManager;
import org.sonatype.aether.util.DefaultRepositorySystemSession;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Typed()
public class MavenFacetImpl implements MavenFacet
{
   private final ProjectBuildingRequest request = new DefaultProjectBuildingRequest();
   private DefaultPlexusContainer container = null;
   private ProjectBuilder builder = null;

   private Project project;
   private ProjectBuildingResult buildingResult;

   private void bootstrapMaven()
   {
      if (!initialized())
      {
         try
         {
            if (!isInstalled())
            {
               throw new ProjectModelException("No POM file found at [" + getPOMFile().getAbsolutePath() + "]");
            }

            container = new DefaultPlexusContainer();
            ConsoleLoggerManager loggerManager = new ConsoleLoggerManager();
            loggerManager.setThreshold("ERROR");
            container.setLoggerManager(loggerManager);

            builder = container.lookup(ProjectBuilder.class);

            // TODO this needs to be configurable via the project/.sidekick
            // file.
            String localRepository = getUserHomeDir().getAbsolutePath() + "/.m2/repository";

            request.setLocalRepository(new MavenArtifactRepository(
                     "local", new File(localRepository).toURI().toURL().toString(),
                     container.lookup(ArtifactRepositoryLayout.class),
                     new ArtifactRepositoryPolicy(true, ArtifactRepositoryPolicy.UPDATE_POLICY_NEVER,
                              ArtifactRepositoryPolicy.CHECKSUM_POLICY_WARN),
                     new ArtifactRepositoryPolicy(true, ArtifactRepositoryPolicy.UPDATE_POLICY_NEVER,
                              ArtifactRepositoryPolicy.CHECKSUM_POLICY_WARN)));
            request.setRemoteRepositories(new ArrayList<ArtifactRepository>());

            DefaultRepositorySystemSession repositorySession = new DefaultRepositorySystemSession();
            repositorySession.setLocalRepositoryManager(new SimpleLocalRepositoryManager(localRepository));
            repositorySession.setOffline(true);

            request.setRepositorySession(repositorySession);
            request.setProcessPlugins(true);
            request.setResolveDependencies(true);
         }
         catch (Exception e)
         {
            throw new ProjectModelException(
                     "Could not initialize maven project located in: " + project.getProjectRoot(), e);
         }
      }
   }

   private boolean initialized()
   {
      return request == null;
   }

   /*
    * POM manipulation methods
    */
   @Override
   public ProjectBuildingResult getProjectBuildingResult()
   {
      // FIXME This method is SLOW: about 2-5 seconds/call (needs optimization!)
      bootstrapMaven();
      try
      {
         if (this.buildingResult == null)
         {
            buildingResult = builder.build(getPOMFile(), request);
         }
         return buildingResult;
      }
      catch (ProjectBuildingException e)
      {
         throw new ProjectModelException(e);
      }
   }

   private void invalidateBuildingResult()
   {
      this.buildingResult = null;
   }

   @Override
   public void addDependency(final Dependency dep)
   {
      bootstrapMaven();
      if (!hasDependency(dep))
      {
         Model pom = getPOM();
         List<Dependency> dependencies = pom.getDependencies();
         dependencies.add(dep);
         setPOM(pom);
      }
      invalidateBuildingResult();
   }

   @Override
   public boolean hasDependency(final Dependency dep)
   {
      bootstrapMaven();
      List<Dependency> dependencies = getProjectBuildingResult().getProject().getDependencies();

      for (Dependency dependency : dependencies)
      {
         if (areEquivalent(dependency, dep))
         {
            return true;
         }
      }
      return false;
   }

   @Override
   public void removeDependency(final Dependency dep)
   {
      bootstrapMaven();
      Model pom = getPOM();
      List<Dependency> dependencies = pom.getDependencies();

      List<Dependency> toBeRemoved = new ArrayList<Dependency>();
      for (Dependency dependency : dependencies)
      {
         if (areEquivalent(dependency, dep))
         {
            toBeRemoved.add(dependency);
         }
      }
      dependencies.removeAll(toBeRemoved);
      setPOM(pom);
      invalidateBuildingResult();
   }

   @Override
   public Model getPOM()
   {
      try
      {
         Model result = new Model();

         MavenXpp3Reader reader = new MavenXpp3Reader();
         FileInputStream stream = new FileInputStream(getPOMFile());
         if (stream.available() > 0)
         {
            result = reader.read(stream);
         }
         stream.close();

         result.setPomFile(getPOMFile());
         return result;
      }
      catch (IOException e)
      {
         throw new ProjectModelException("Could not open POM file: " + getPOMFile(), e);
      }
      catch (XmlPullParserException e)
      {
         throw new ProjectModelException("Could not parse POM file: " + getPOMFile(), e);
      }
   }

   @Override
   public void setPOM(final Model pom)
   {
      try
      {
         MavenXpp3Writer writer = new MavenXpp3Writer();
         FileWriter fw = new FileWriter(getPOMFile());
         writer.write(fw, pom);
         fw.close();
      }
      catch (IOException e)
      {
         throw new ProjectModelException("Could not write POM file: " + getPOMFile(), e);
      }
      invalidateBuildingResult();
   }

   private Model createPOM()
   {
      File pomFile = getPOMFile();
      if (!pomFile.exists())
      {
         project.writeFile(new char[] {}, pomFile);
      }
      Model pom = getPOM();
      pom.setGroupId("org.jboss.seam");
      pom.setArtifactId("scaffolding");
      pom.setVersion("1.0.0-SNAPSHOT");
      pom.setPomFile(getPOMFile());
      pom.setModelVersion("4.0.0");
      setPOM(pom);
      return pom;
   }

   private File getPOMFile()
   {
      File file = new File(project.getProjectRoot() + File.separator + "pom.xml");
      return file;
   }

   private File getUserHomeDir()
   {
      return new File(System.getProperty("user.home")).getAbsoluteFile();
   }

   @SuppressWarnings("unchecked")
   private boolean areEquivalent(final Dependency left, final Dependency right)
   {
      boolean result = false;
      if (left.getGroupId().equals(right.getGroupId()) && left.getArtifactId().equals(right.getArtifactId()))
      {
         ArtifactVersion lversion = new DefaultArtifactVersion(left.getVersion());
         ArtifactVersion rversion = new DefaultArtifactVersion(right.getVersion());

         if (lversion.compareTo(rversion) == 0)
         {
            result = true;
         }
      }
      return result;
   }

   @Override
   public Project getProject()
   {
      return project;
   }

   @Override
   public Facet init(final Project project)
   {
      this.project = project;
      return this;
   }

   @Override
   public boolean isInstalled()
   {
      return getPOMFile().exists();
   }

   @Override
   public Facet install()
   {
      createPOM();
      bootstrapMaven();
      project.registerFacet(this);
      return this;
   }

   @Override
   public Set<Class<? extends Facet>> getDependencies()
   {
      return null;
   }

   @Override
   public MavenProject getMavenProject()
   {
      return getProjectBuildingResult().getProject();
   }
}
