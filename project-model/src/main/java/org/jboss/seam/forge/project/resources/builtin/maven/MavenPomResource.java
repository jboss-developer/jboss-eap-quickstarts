/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.seam.forge.project.resources.builtin.maven;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.maven.model.Model;
import org.apache.maven.model.Profile;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.ResourceFlag;
import org.jboss.seam.forge.project.ResourceHandles;
import org.jboss.seam.forge.project.dependencies.Dependency;
import org.jboss.seam.forge.project.dependencies.MavenDependencyAdapter;
import org.jboss.seam.forge.project.resources.FileResource;
import org.jboss.seam.forge.project.services.ResourceFactory;

/**
 * MavenPomResource
 *
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
@ResourceHandles("pom.xml")
public class MavenPomResource extends FileResource
{
   private Model currentModel;
   
   @Inject
   public MavenPomResource(final ResourceFactory factory)
   {
      super(factory, null);
   }

   public MavenPomResource(final ResourceFactory factory, final File file)
   {
      super(factory, file);
      setFlag(ResourceFlag.ProjectSourceFile);
   }

   
   /* (non-Javadoc)
    * @see org.jboss.seam.forge.project.Resource#listResources()
    */
   @Override
   public List<Resource<?>> listResources()
   {
      List<Resource<?>> children = new ArrayList<Resource<?>>(); 
      listDependencies(children);
      listProfiles(children);
      return children;
   }

   private void listDependencies(List<Resource<?>> children)
   {
      Model model = getCurrentModel();
      List<Dependency> dependencies = MavenDependencyAdapter.fromMavenList(model.getDependencies());
      for(Dependency dep : dependencies)
      {
         children.add(new MavenDependencyResource(this, dep));
      }
   }
   
   private void listProfiles(List<Resource<?>> children)
   {
      Model model = getCurrentModel();
      List<Profile> profiles = model.getProfiles();
      for(Profile profile : profiles)
      {
         children.add(new MavenProfileResource(this, profile));
      }
   }

   public Model getCurrentModel()
   {
      initialize();
      return currentModel;
   }
   
   /* (non-Javadoc)
    * @see org.jboss.seam.forge.project.resources.FileResource#createFrom(java.io.File)
    */
   @Override
   public Resource<File> createFrom(File file)
   {
      return new MavenPomResource(resourceFactory, file);
   }

   private void initialize()
   {
      if(currentModel == null)
      {
         try
         {
            MavenXpp3Reader reader = new MavenXpp3Reader();
            FileInputStream stream = new FileInputStream(getUnderlyingResourceObject());
            if (stream.available() > 0)
            {
               currentModel = reader.read(stream);
            }
            stream.close();

            currentModel.setPomFile(getUnderlyingResourceObject());
         }
         catch (Exception e)
         {
            throw new RuntimeException(e);
         }
      }
   }
   
   /* (non-Javadoc)
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString()
   {
      return file.getName();
   }
}
