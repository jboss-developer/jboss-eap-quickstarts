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
package org.jboss.seam.forge.spec.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.constraints.RequiresFacet;
import org.jboss.seam.forge.shell.ShellPrompt;
import org.jboss.seam.forge.shell.plugins.Command;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.spec.jpa.api.ContainerType;
import org.jboss.seam.forge.spec.jpa.api.PersistenceProvider;
import org.jboss.shrinkwrap.descriptor.impl.spec.jpa.persistence.PersistenceModel;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@Named("persistence")
@RequiresFacet(PersistenceFacet.class)
public class PersistencePlugin implements Plugin
{
   @Inject
   private Project project;

   @Inject
   private Instance<PersistenceProvider> providers;

   @Inject
   private Instance<ContainerType> containers;

   @Inject
   private ShellPrompt prompt;

   @Command("setup")
   public void setup()
   {
      PersistenceFacet jpa = project.getFacet(PersistenceFacet.class);

      /*
       * Ask user for their preferred container/provider.
       */
      PersistenceProvider provider = getPersistenceProvider();
      ContainerType container = getContainerType();

      // Perform installation
      PersistenceModel config = provider.setup(jpa.getConfig(), container);
      jpa.saveConfig(config);
   }

   private ContainerType getContainerType()
   {
      // Select a persistence provider
      List<String> providerNames = new ArrayList<String>();
      List<ContainerType> providerList = new ArrayList<ContainerType>();
      for (ContainerType provider : containers)
      {
         providerList.add(provider);
         providerNames.add(provider.getName());
      }

      int promptChoice = prompt.promptChoice("Select a target Container.", providerNames);
      ContainerType provider = providerList.get(promptChoice);
      return provider;
   }

   private PersistenceProvider getPersistenceProvider()
   {
      // Select a persistence provider
      List<String> providerNames = new ArrayList<String>();
      List<PersistenceProvider> providerList = new ArrayList<PersistenceProvider>();
      for (PersistenceProvider provider : providers)
      {
         providerList.add(provider);
         providerNames.add(provider.getName());
      }

      int promptChoice = prompt.promptChoice("Select a JPA provider.", providerNames);
      PersistenceProvider provider = providerList.get(promptChoice);
      return provider;
   }
}
