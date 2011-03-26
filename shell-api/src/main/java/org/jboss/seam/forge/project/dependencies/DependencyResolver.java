/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc., and individual contributors
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
package org.jboss.seam.forge.project.dependencies;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.jboss.seam.forge.resources.DependencyResource;
import org.jboss.seam.forge.shell.util.BeanManagerUtils;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@ApplicationScoped
public class DependencyResolver
{
   @Inject
   private BeanManager manager;

   private List<DependencyResolverProvider> providers;

   private void init()
   {
      if (this.providers == null)
      {
         this.providers = new ArrayList<DependencyResolverProvider>();

         List<DependencyResolverProvider> providers = new ArrayList<DependencyResolverProvider>();
         ServiceLoader<DependencyResolverProvider> loader = ServiceLoader.load(DependencyResolverProvider.class);

         for (DependencyResolverProvider p : loader)
         {
            providers.add(p);
         }

         for (DependencyResolverProvider p : providers)
         {
            this.providers.add(BeanManagerUtils.getContextualInstance(manager, p.getClass()));
         }
      }
   }

   public List<DependencyResource> resolveArtifacts(final Dependency dep, final List<DependencyRepository> repositories)
   {
      init();
      for (DependencyResolverProvider p : providers)
      {
         List<DependencyResource> artifacts = p.resolveArtifacts(dep, repositories);
         if (artifacts != null && !artifacts.isEmpty())
         {
            return artifacts;
         }
      }
      return new ArrayList<DependencyResource>();
   }

   public List<Dependency> resolveVersions(final Dependency dep, final List<DependencyRepository> repositories)
   {
      init();
      for (DependencyResolverProvider p : providers)
      {
         List<Dependency> artifacts = p.resolveVersions(dep, repositories);
         if (artifacts != null && !artifacts.isEmpty())
         {
            return artifacts;
         }
      }
      return new ArrayList<Dependency>();
   }

}
