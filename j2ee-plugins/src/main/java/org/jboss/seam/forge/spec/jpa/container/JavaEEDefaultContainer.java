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
package org.jboss.seam.forge.spec.jpa.container;

import javax.inject.Inject;

import org.jboss.seam.forge.shell.ShellMessages;
import org.jboss.seam.forge.shell.ShellPrintWriter;
import org.jboss.seam.forge.spec.jpa.api.DatabaseType;
import org.jboss.seam.forge.spec.jpa.api.JPADataSource;
import org.jboss.seam.forge.spec.jpa.api.PersistenceContainer;
import org.jboss.shrinkwrap.descriptor.api.spec.jpa.persistence.PersistenceUnitDef;
import org.jboss.shrinkwrap.descriptor.api.spec.jpa.persistence.TransactionType;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public abstract class JavaEEDefaultContainer implements PersistenceContainer
{
   @Inject
   private ShellPrintWriter writer;

   @Override
   public PersistenceUnitDef setupConnection(PersistenceUnitDef unit, JPADataSource dataSource)
   {
      unit.transactionType(TransactionType.JTA);
      if (dataSource.getJndiDataSource() != null)
      {
         ShellMessages.info(writer, "Ignoring JNDI data-source [" + dataSource.getJndiDataSource() + "]");
      }
      if (dataSource.hasNonDefaultDatabase())
      {
         ShellMessages.info(writer, "Ignoring database [" + dataSource.getDatabase() + "]");
      }
      if (dataSource.hasJdbcConnectionInfo())
      {
         ShellMessages.info(writer, "Ignoring jdbc connection info [" + dataSource.getJdbcConnectionInfo() + "]");
      }

      dataSource.setDatabase(setup(unit));
      return unit;
   }

   public abstract DatabaseType setup(PersistenceUnitDef unit);
}
