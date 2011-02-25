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
package org.jboss.seam.forge.spec.jpa.provider;

import org.jboss.seam.forge.spec.jpa.api.DatabaseType;
import org.jboss.seam.forge.spec.jpa.api.JPADataSource;
import org.jboss.seam.forge.spec.jpa.api.PersistenceProvider;
import org.jboss.shrinkwrap.descriptor.api.spec.jpa.persistence.PersistenceUnitDef;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class HibernateProvider implements PersistenceProvider
{
   private static Map<DatabaseType, String> dialects = new HashMap<DatabaseType, String>();

   static
   {
      dialects.put(DatabaseType.DERBY, "org.hibernate.dialect.DerbyDialect");
      dialects.put(DatabaseType.DB2, "org.hibernate.dialect.DB2Dialect");
      dialects.put(DatabaseType.DB2_AS400, "org.hibernate.dialect.DB2400Dialect");
      dialects.put(DatabaseType.DB2_OS390, "org.hibernate.dialect.DB2390Dialect");
      dialects.put(DatabaseType.POSTGRES, "org.hibernate.dialect.PostgreSQLDialect");
      dialects.put(DatabaseType.MYSQL, "org.hibernate.dialect.MySQLDialect");
      dialects.put(DatabaseType.MYSQL_INNODB, "org.hibernate.dialect.MySQLInnoDBDialect");
      dialects.put(DatabaseType.MYSQL_ISAM, "org.hibernate.dialect.MySQLMyISAMDialect");
      dialects.put(DatabaseType.ORACLE, "org.hibernate.dialect.OracleDialect");
      dialects.put(DatabaseType.ORACLE_9I, "org.hibernate.dialect.Oracle9iDialect");
      dialects.put(DatabaseType.ORACLE_10G, "org.hibernate.dialect.Oracle10gDialect");
      dialects.put(DatabaseType.ORACLE_11G, "org.hibernate.dialect.OracleDialect");
      dialects.put(DatabaseType.SYBASE, "org.hibernate.dialect.SybaseDialect");
      dialects.put(DatabaseType.SYBASE_ANYWHERE, "org.hibernate.dialect.SybaseAnywhereDialect");
      dialects.put(DatabaseType.SQL_SERVER, "org.hibernate.dialect.SQLServerDialect");
      dialects.put(DatabaseType.SAP_DB, "org.hibernate.dialect.SAPDBDialect");
      dialects.put(DatabaseType.INFORMIX, "org.hibernate.dialect.InformixDialect");
      dialects.put(DatabaseType.HSQLDB, "org.hibernate.dialect.HSQLDialect");
      dialects.put(DatabaseType.HSQLDB_IN_MEMORY, "org.hibernate.dialect.HSQLDialect");
      dialects.put(DatabaseType.INGRES, "org.hibernate.dialect.IngresDialect");
      dialects.put(DatabaseType.PROGRESS, "org.hibernate.dialect.ProgressDialect");
      dialects.put(DatabaseType.MCKOI, "org.hibernate.dialect.MckoiDialect");
      dialects.put(DatabaseType.INTERBASE, "org.hibernate.dialect.InterbaseDialect");
      dialects.put(DatabaseType.POINTBASE, "org.hibernate.dialect.PointbaseDialect");
      dialects.put(DatabaseType.FRONTBASE, "org.hibernate.dialect.FrontbaseDialect");
      dialects.put(DatabaseType.FIREBIRD, "org.hibernate.dialect.FirebirdDialect");
   }

   @Override
   public PersistenceUnitDef setup(PersistenceUnitDef unit, JPADataSource ds)
   {
      unit.provider("org.hibernate.ejb.HibernatePersistence");

      unit.includeUnlistedClasses();
      unit.property("hibernate.hbm2ddl.auto", "create-drop");
      unit.property("hibernate.show_sql", "true");
      unit.property("hibernate.format_sql", "true");
      unit.property("hibernate.transaction.flush_before_completion", "true");

      if (!DatabaseType.DEFAULT.equals(ds.getDatabase()))
      {
         String dialect = dialects.get(ds.getDatabase());
         if (dialect == null)
         {
            throw new RuntimeException("Unsupported database type for Hibernate [" + ds.getDatabase() + "]");
         }
         unit.property("hibernate.dialect", dialect);
      }

      return unit;
   }

}
