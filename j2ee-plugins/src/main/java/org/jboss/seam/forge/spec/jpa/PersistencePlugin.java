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

import javax.enterprise.event.Event;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.forge.project.Project;
import org.jboss.seam.forge.project.constraints.RequiresFacet;
import org.jboss.seam.forge.project.facets.JavaSourceFacet;
import org.jboss.seam.forge.shell.events.InstallFacet;
import org.jboss.seam.forge.shell.plugins.Command;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.spec.jpa.api.DatabaseType;
import org.jboss.seam.forge.spec.jpa.api.JPAContainer;
import org.jboss.seam.forge.spec.jpa.api.JPADataSource;
import org.jboss.seam.forge.spec.jpa.api.JPAProvider;
import org.jboss.seam.forge.spec.jpa.api.PersistenceContainer;
import org.jboss.seam.forge.spec.jpa.api.PersistenceProvider;
import org.jboss.shrinkwrap.descriptor.impl.spec.jpa.persistence.PersistenceModel;
import org.jboss.shrinkwrap.descriptor.impl.spec.jpa.persistence.PersistenceUnit;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
@Named("persistence")
@RequiresFacet(JavaSourceFacet.class)
public class PersistencePlugin implements Plugin
{
   public static final String DEFAULT_UNIT_NAME = "forge-default";

   private static final String DEFAULT_UNIT_DESC = "Forge Persistence Unit";

   @Inject
   private Project project;

   @Inject
   private Event<InstallFacet> request;

   @Inject
   private BeanManager manager;

   @Command("setup")
   public void setup(
            @Option(name = "provider", required = true) JPAProvider jpap,
            @Option(name = "container", required = true) JPAContainer jpac,
            @Option(name = "database", defaultValue = "DEFAULT") DatabaseType databaseType,
            @Option(name = "jndiDataSource") String jtaDataSource,
            @Option(name = "jdbcDriver") String jdbcDriver,
            @Option(name = "jdbcURL") String jdbcURL,
            @Option(name = "jdbcUsername") String jdbcUsername,
            @Option(name = "jdbcPassword") String jdbcPassword,
            @Option(name = "named", defaultValue = DEFAULT_UNIT_NAME) String unitName)
   {
      installPersistence();

      PersistenceFacet jpa = project.getFacet(PersistenceFacet.class);
      PersistenceModel config = jpa.getConfig();

      PersistenceUnit unit = null;
      for (PersistenceUnit u : config.getPersistenceUnits())
      {
         if (DEFAULT_UNIT_NAME.equals(u.getName())
                  || (unitName != null && unitName.equals(u.getName())))
         {
            unit = u;
         }
      }

      if (unit == null)
      {
         unit = new PersistenceUnit();
         unit.setName(unitName);
         unit.setDescription(DEFAULT_UNIT_DESC);
         config.getPersistenceUnits().add(unit);
      }
      unit.getProperties().clear();

      JPADataSource ds = new JPADataSource()
               .setJndiDataSource(jtaDataSource)
               .setDatabaseType(databaseType)
               .setJdbcDriver(jdbcDriver)
               .setDatabaseURL(jdbcURL)
               .setUsername(jdbcUsername)
               .setPassword(jdbcPassword);

      PersistenceContainer container = jpac.getContainer(manager);
      PersistenceProvider provider = jpap.getProvider(manager);

      container.setupConnection(unit, ds);
      provider.setup(unit, ds);

      jpa.saveConfig(config);
   }

   private void installPersistence()
   {
      if (!project.hasFacet(PersistenceFacet.class))
      {
         request.fire(new InstallFacet(PersistenceFacet.class));
      }
   }
}
