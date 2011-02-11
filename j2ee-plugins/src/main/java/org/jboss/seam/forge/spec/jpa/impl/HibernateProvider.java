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
package org.jboss.seam.forge.spec.jpa.impl;

import org.jboss.seam.forge.spec.jpa.api.ContainerType;
import org.jboss.seam.forge.spec.jpa.api.PersistenceProvider;
import org.jboss.shrinkwrap.descriptor.api.spec.jpa.persistence.TransactionType;
import org.jboss.shrinkwrap.descriptor.impl.spec.jpa.persistence.PersistenceModel;
import org.jboss.shrinkwrap.descriptor.impl.spec.jpa.persistence.PersistenceUnit;
import org.jboss.shrinkwrap.descriptor.impl.spec.jpa.persistence.Property;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class HibernateProvider implements PersistenceProvider
{

   @Override
   public String getName()
   {
      return "HIBERNATE";
   }

   @Override
   public boolean isActive(final PersistenceModel config)
   {
      return false;
   }

   @Override
   public PersistenceModel setup(final PersistenceModel config, final ContainerType container)
   {
      PersistenceUnit unit = null;

      for (PersistenceUnit u : config.getPersistenceUnits())
      {
         if (PersistenceProvider.DEFAULT_UNIT_NAME.equals(u.getName()))
         {
            unit = u;
         }
      }

      if ((unit != null) || config.getPersistenceUnits().isEmpty())
      {
         unit = new PersistenceUnit();
         unit.setDescription("Forge Default Persistence Unit");
         unit.setName(PersistenceProvider.DEFAULT_UNIT_NAME);
         unit.setJtaDataSource(container.getDefaultDataSource());
         unit.setTransactionType(TransactionType.JTA);

         unit.setExcludeUnlistedClasses(false);
         unit.getProperties().add(new Property("hibernate.hbm2ddl.auto", "create-drop"));
         unit.getProperties().add(new Property("hibernate.show_sql", "true"));
         unit.getProperties().add(new Property("hibernate.format_sql", "true"));
         unit.getProperties().add(new Property("hibernate.transaction.flush_before_completion", "true"));

         config.getPersistenceUnits().add(unit);
      }

      return config;
   }
}
