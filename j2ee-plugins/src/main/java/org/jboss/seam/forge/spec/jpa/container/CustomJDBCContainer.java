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

import org.jboss.seam.forge.parser.java.util.Strings;
import org.jboss.seam.forge.shell.ShellMessages;
import org.jboss.seam.forge.shell.ShellPrintWriter;
import org.jboss.seam.forge.spec.jpa.api.JPADataSource;
import org.jboss.seam.forge.spec.jpa.api.PersistenceContainer;
import org.jboss.shrinkwrap.descriptor.api.spec.jpa.persistence.PersistenceUnitDef;
import org.jboss.shrinkwrap.descriptor.api.spec.jpa.persistence.TransactionType;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class CustomJDBCContainer implements PersistenceContainer
{
   @Inject
   private ShellPrintWriter writer;

   @Override
   public PersistenceUnitDef setupConnection(PersistenceUnitDef unit, JPADataSource dataSource)
   {
      unit.transactionType(TransactionType.RESOURCE_LOCAL);
      if (dataSource.getJndiDataSource() != null)
      {
         ShellMessages.info(writer, "Ignoring JNDI data-source [" + dataSource.getJndiDataSource() + "]");
      }

      if (!dataSource.hasNonDefaultDatabase())
      {
         throw new RuntimeException("Must specify database type for JDBC connections.");
      }
      if (Strings.isNullOrEmpty(dataSource.getDatabaseURL()))
      {
         throw new RuntimeException("Must specify database URL for JDBC connections.");
      }
      if (Strings.isNullOrEmpty(dataSource.getUsername()))
      {
         throw new RuntimeException("Must specify username for JDBC connections.");
      }
      if (Strings.isNullOrEmpty(dataSource.getPassword()))
      {
         throw new RuntimeException("Must specify password for JDBC connections.");
      }

      unit.nonJtaDataSource(null);
      unit.jtaDataSource(null);

      unit.property("javax.persistence.jdbc.driver", dataSource.getJdbcDriver());
      unit.property("javax.persistence.jdbc.url", dataSource.getDatabaseURL());
      unit.property("javax.persistence.jdbc.user", dataSource.getUsername());
      unit.property("javax.persistence.jdbc.password", dataSource.getPassword());

      return unit;
   }

}
