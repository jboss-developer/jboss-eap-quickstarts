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

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.constraints.RequiresFacet;
import org.jboss.seam.forge.shell.ShellPrompt;
import org.jboss.seam.forge.shell.plugins.Command;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.spec.jpa.api.JPAContainer;
import org.jboss.seam.forge.spec.jpa.api.JPADataSource;
import org.jboss.seam.forge.spec.jpa.api.JPAProvider;
import org.jboss.seam.forge.spec.jpa.api.PersistenceContainer;
import org.jboss.seam.forge.spec.jpa.api.PersistenceProvider;
import org.jboss.seam.forge.spec.jpa.impl.JPADataSourceImpl;
import org.jboss.shrinkwrap.descriptor.impl.spec.jpa.persistence.PersistenceModel;
import org.jboss.shrinkwrap.descriptor.impl.spec.jpa.persistence.PersistenceUnit;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@Named("persistence")
@RequiresFacet(PersistenceFacet.class)
public class PersistencePlugin implements Plugin
{
   public static final String DEFAULT_UNIT_NAME = "forge-default";

   private static final String DEFAULT_UNIT_DESC = "Forge Persistence Unit";

   @Inject
   private Project project;

   @Inject
   private ShellPrompt prompt;

   @Inject
   private BeanManager manager;

   @Command("setup")
   public void setup(
            @Option(name = "provider", required = true) JPAProvider jpap,
            @Option(name = "container", required = true) JPAContainer jpac,
            @Option(name = "unitName", defaultValue = DEFAULT_UNIT_NAME) String unitName,
            @Option(name = "unitDesc", defaultValue = DEFAULT_UNIT_DESC) String unitDescription,
            @Option(name = "jndiName") String jndiName,
            @Option(name = "jdbcDriver") String jdbcDriver,
            @Option(name = "databaseURL") String databaseURL,
            @Option(name = "username") String username,
            @Option(name = "password") String password)
   {

      PersistenceFacet jpa = project.getFacet(PersistenceFacet.class);
      PersistenceModel config = jpa.getConfig();

      PersistenceUnit unit = null;
      for (PersistenceUnit u : config.getPersistenceUnits())
      {
         if (unitName != null && unitName.equals(u.getName()))
         {
            unit = u;
         }
      }

      if (unit == null)
      {
         unit = new PersistenceUnit();
         unit.setName(unitName);
         unit.setDescription(unitDescription);
         config.getPersistenceUnits().add(unit);
      }

      JPADataSource ds = new JPADataSourceImpl(jndiName, jdbcDriver, databaseURL, username, password);

      PersistenceProvider provider = jpap.getProvider(manager);
      PersistenceContainer container = jpac.getContainer(manager);
      provider.setup(unit);
      container.setupConnection(unit, ds);

      jpa.saveConfig(config);
   }
}
