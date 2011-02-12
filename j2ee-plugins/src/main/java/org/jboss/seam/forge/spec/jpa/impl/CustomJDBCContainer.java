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

import javax.inject.Inject;

import org.jboss.seam.forge.shell.ShellMessages;
import org.jboss.seam.forge.shell.ShellPrintWriter;
import org.jboss.seam.forge.spec.jpa.api.JPADataSource;
import org.jboss.seam.forge.spec.jpa.api.PersistenceContainer;
import org.jboss.shrinkwrap.descriptor.api.spec.jpa.persistence.TransactionType;
import org.jboss.shrinkwrap.descriptor.impl.spec.jpa.persistence.PersistenceUnit;
import org.jboss.shrinkwrap.descriptor.impl.spec.jpa.persistence.Property;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class CustomJDBCContainer implements PersistenceContainer
{
   @Inject
   private ShellPrintWriter writer;

   @Override
   public PersistenceUnit setupConnection(PersistenceUnit unit, JPADataSource dataSource)
   {
      unit.setTransactionType(TransactionType.RESOURCE_LOCAL);
      if (dataSource.getJndiDataSource() != null)
      {
         ShellMessages.info(writer, "Ignoring JNDI data-source [" + dataSource.getJndiDataSource() + "]");
      }
      if (!dataSource.hasNonDefaultDatabase())
      {
         throw new RuntimeException("Must specify database type for JDBC connections.");
      }

      unit.setNonJtaDataSource(null);
      unit.setJtaDataSource(null);

      unit.getProperties().add(new Property("javax.persistence.jdbc.driver", dataSource.getJdbcDriver()));
      unit.getProperties().add(new Property("javax.persistence.jdbc.url", dataSource.getDatabaseURL()));
      unit.getProperties().add(new Property("javax.persistence.jdbc.user", dataSource.getUsername()));
      unit.getProperties().add(new Property("javax.persistence.jdbc.password", dataSource.getPassword()));

      return unit;
   }

}
