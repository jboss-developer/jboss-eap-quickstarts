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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.maven.model.Profile;
import org.jboss.seam.forge.project.AbstractResource;
import org.jboss.seam.forge.project.Resource;
import org.jboss.seam.forge.project.dependencies.Dependency;
import org.jboss.seam.forge.project.dependencies.MavenDependencyAdapter;

/**
 * MavenProfileResource
 *
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
public class MavenProfileResource extends AbstractResource<Profile>
{
   private Profile profile;
   
   public MavenProfileResource(Resource<?> parent, Profile profile)
   {
      super(null, parent);
      this.profile = profile;
   }

   /* (non-Javadoc)
    * @see org.jboss.seam.forge.project.Resource#getName()
    */
   @Override
   public String getName()
   {
      return profile.getId();
   }

   /* (non-Javadoc)
    * @see org.jboss.seam.forge.project.Resource#createFrom(java.lang.Object)
    */
   @Override
   public Resource<Profile> createFrom(Profile file)
   {
      throw new RuntimeException("not implemented");
   }

   /* (non-Javadoc)
    * @see org.jboss.seam.forge.project.Resource#listResources()
    */
   @Override
   public List<Resource<?>> listResources()
   {
      List<Resource<?>> children = new ArrayList<Resource<?>>();
      List<Dependency> dependencies = MavenDependencyAdapter.fromMavenList(profile.getDependencies());
      for(Dependency dep : dependencies)
      {
         children.add(new MavenDependencyResource(this, dep));
      }
      return children;
   }

   /* (non-Javadoc)
    * @see org.jboss.seam.forge.project.Resource#getUnderlyingResourceObject()
    */
   @Override
   public Profile getUnderlyingResourceObject()
   {
      return profile;
   }

   /* (non-Javadoc)
    * @see org.jboss.seam.forge.project.Resource#getChild(java.lang.String)
    */
   @Override
   public Resource<?> getChild(String name)
   {
      throw new RuntimeException("not implemented");
   }
}
