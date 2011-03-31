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
import java.util.Arrays;
import java.util.List;
import java.util.ServiceLoader;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.jboss.seam.forge.resources.DependencyResource;
import org.jboss.seam.forge.shell.util.BeanManagerUtils;

/**
 * Used to resolve {@link Dependency} versions, {@link DependencyResource} artifacts, and dependencies of a given
 * {@link Dependency}
 * <p>
 * <b>Example usage:</b>
 * <p>
 * 
 * <pre>
 * &#064;Inject
 * DepenencyResolver resolver;
 * 
 * Dependency query =
 *          DependencyBuilder.create(&quot;com.example:example:[1.0],[2.0,)&quot;);
 * 
 * List&lt;Dependency&gt; versions = resolver.resolveVersions(query);
 * List&lt;DependencyResource&gt; artifacts = resolver.resolveArtifacts(query);
 * List&lt;DependencyResource&gt; dependencies = resolver.resolveDependencies(query);
 * </pre>
 * <p>
 * <b>Version query syntax is as follows:</b>
 * <table>
 * <tr>
 * <td>1.0</td>
 * <td>version == 1.0</td>
 * </tr>
 * <tr>
 * <td>[1.0,2.0)</td>
 * <td>1.0 &lt;= version &lt; 2.0</td>
 * </tr>
 * <tr>
 * <td>[1.0,2.0]</td>
 * <td>1.0 &lt;= version &lt;= 2.0</td>
 * </tr>
 * <tr>
 * <td>[1.5,)</td>
 * <td>1.5 &lt;= version</td>
 * </tr>
 * <tr>
 * <td>(,1.0],[1.2,)</td>
 * <td>version &lt;= 1.0, and version &gt;= 2.0</td>
 * </tr>
 * </table>
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@Singleton
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
      if (this.providers == null || this.providers.isEmpty())
      {
         throw new IllegalStateException(
                  "No configured implementations for [" + DependencyResolverProvider.class.getName()
                           + "] could be found.");
      }
   }

   /**
    * Resolve a set of {@link DependencyResource} artifacts matching the given query, searching in the default
    * repository.
    */
   public List<DependencyResource> resolveArtifacts(Dependency query)
   {
      return resolveArtifacts(query, new ArrayList<DependencyRepository>());
   }

   /**
    * Resolve a set of {@link DependencyResource} artifacts matching the given query, searching in only the given
    * {@link DependencyRepository}.
    */
   public List<DependencyResource> resolveArtifacts(final Dependency query, final DependencyRepository repository)
   {
      return resolveArtifacts(query, Arrays.asList(repository));
   }

   /**
    * Resolve a set of {@link DependencyResource} artifacts matching the given query, searching in only the given list
    * of {@link DependencyRepository} instances.
    */
   public List<DependencyResource> resolveArtifacts(final Dependency query,
            final List<DependencyRepository> repositories)
   {
      init();
      for (DependencyResolverProvider p : providers)
      {
         List<DependencyResource> artifacts = p.resolveArtifacts(query, repositories);
         if (artifacts != null && !artifacts.isEmpty())
         {
            return artifacts;
         }
      }
      return new ArrayList<DependencyResource>();
   }

   /**
    * Resolve a set of {@link DependencyResource} dependencies for the given query, searching in the default repository.
    * <p>
    * 
    * @return a list of {@link DependencyResource} dependencies on which the given query artifact depends.
    */
   public List<DependencyResource> resolveDependencies(final Dependency query)
   {
      return resolveDependencies(query, new ArrayList<DependencyRepository>());
   }

   /**
    * Resolve a set of {@link DependencyResource} dependencies for the given query, searching in only the given
    * {@link DependencyRepository}.
    * <p>
    * 
    * @return a list of {@link DependencyResource} dependencies on which the given query artifact depends.
    */
   public List<DependencyResource> resolveDependencies(final Dependency query,
            final DependencyRepository repository)
   {
      return resolveDependencies(query, Arrays.asList(repository));
   }

   /**
    * Resolve a set of {@link DependencyResource} dependencies for the given query, searching in only the given list of
    * {@link DependencyRepository} instances.
    * <p>
    * 
    * @return a list of {@link DependencyResource} dependencies on which the given query artifact depends.
    */
   public List<DependencyResource> resolveDependencies(final Dependency query,
            final List<DependencyRepository> repositories)
   {
      init();
      for (DependencyResolverProvider p : providers)
      {
         List<DependencyResource> artifacts = p.resolveDependencies(query, repositories);
         if (artifacts != null && !artifacts.isEmpty())
         {
            return artifacts;
         }
      }
      return new ArrayList<DependencyResource>();
   }

   /**
    * Resolve a set of {@link Dependency} versions matching the given query, searching in the default repository.
    */
   public List<Dependency> resolveVersions(final Dependency query)
   {
      return resolveVersions(query);
   }

   /**
    * Resolve a set of {@link Dependency} versions matching the given query, searching in only the given
    * {@link DependencyRepository}.
    */
   public List<Dependency> resolveVersions(final Dependency query, final DependencyRepository repository)
   {
      return resolveVersions(query, Arrays.asList(repository));
   }

   /**
    * Resolve a set of {@link Dependency} versions matching the given query, searching in only the given list of
    * {@link DependencyRepository} instances.
    */
   public List<Dependency> resolveVersions(final Dependency query, final List<DependencyRepository> repositories)
   {
      init();
      for (DependencyResolverProvider p : providers)
      {
         List<Dependency> artifacts = p.resolveVersions(query, repositories);
         if (artifacts != null && !artifacts.isEmpty())
         {
            return artifacts;
         }
      }
      return new ArrayList<Dependency>();
   }
}
