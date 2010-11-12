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

package org.jboss.seam.forge.project.facets.builtin;

import java.io.File;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryPolicy;
import org.apache.maven.artifact.repository.MavenArtifactRepository;
import org.apache.maven.artifact.repository.layout.ArtifactRepositoryLayout;
import org.apache.maven.project.DefaultProjectBuildingRequest;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.project.ProjectBuildingRequest;
import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.logging.console.ConsoleLoggerManager;
import org.jboss.seam.forge.project.ProjectModelException;
import org.sonatype.aether.impl.internal.SimpleLocalRepositoryManager;
import org.sonatype.aether.util.DefaultRepositorySystemSession;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@ApplicationScoped
public class MavenContainer
{
   private ProjectBuildingRequest request;
   private DefaultPlexusContainer container = null;
   private ProjectBuilder builder = null;

   @PostConstruct
   public void bootstrapMaven()
   {
      try
      {
         container = new DefaultPlexusContainer();
         ConsoleLoggerManager loggerManager = new ConsoleLoggerManager();
         loggerManager.setThreshold("ERROR");
         container.setLoggerManager(loggerManager);

         builder = container.lookup(ProjectBuilder.class);

         // TODO this needs to be configurable via the project/.forge //
         // file.
         String localRepository = getUserHomeDir().getAbsolutePath() + "/.m2/repository";

         request = new DefaultProjectBuildingRequest();
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
                     "Could not initialize maven", e);
      }
   }

   private File getUserHomeDir()
   {
      return new File(System.getProperty("user.home")).getAbsoluteFile();
   }

   public ProjectBuildingRequest getRequest()
   {
      return request;
   }

   public ProjectBuilder getBuilder()
   {
      return builder;
   }
}
