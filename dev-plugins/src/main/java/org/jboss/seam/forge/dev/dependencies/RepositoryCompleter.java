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
package org.jboss.seam.forge.dev.dependencies;

import java.util.List;

import javax.inject.Inject;

import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.dependencies.DependencyRepository;
import org.jboss.seam.forge.project.facets.DependencyFacet;
import org.jboss.seam.forge.shell.completer.CommandCompleter;
import org.jboss.seam.forge.shell.completer.CommandCompleterState;

/**
 * Provides completion for project build properties
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class RepositoryCompleter implements CommandCompleter
{
   @Inject
   private Project project;

   @Override
   public void complete(final CommandCompleterState state)
   {
      DependencyFacet deps = project.getFacet(DependencyFacet.class);

      List<DependencyRepository> repositories = deps.getRepositories();
      String peek = state.getTokens().peek();

      if ((state.getTokens().size() <= 1))
      {
         for (DependencyRepository repo : repositories)
         {
            if (repo.getUrl().startsWith(peek == null ? "" : peek))
            {
               state.getCandidates().add(repo.getUrl());
               state.setIndex(state.getIndex() - (peek == null ? 0 : peek.length()));
            }
         }
      }
   }

}
