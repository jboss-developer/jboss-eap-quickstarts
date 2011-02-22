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

import java.util.HashMap;
import java.util.Map;

import org.jboss.seam.forge.spec.jpa.api.DatabaseType;
import org.jboss.seam.forge.spec.jpa.api.JPADataSource;
import org.jboss.seam.forge.spec.jpa.api.PersistenceProvider;
import org.jboss.shrinkwrap.descriptor.api.spec.jpa.persistence.PersistenceUnitDef;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class OpenJPAProvider implements PersistenceProvider
{
   private static Map<DatabaseType, String> dictionary = new HashMap<DatabaseType, String>();

   static
   {
      dictionary.put(DatabaseType.ACCESS, "access");
      dictionary.put(DatabaseType.DERBY, "derby");
      dictionary.put(DatabaseType.DB2, "db2");
      dictionary.put(DatabaseType.DB2_AS400, "db2");
      dictionary.put(DatabaseType.DB2_OS390, "db2");
      dictionary.put(DatabaseType.POSTGRES, "postgres");
      dictionary.put(DatabaseType.MYSQL, "mysql");
      dictionary.put(DatabaseType.MYSQL_INNODB, "mysql");
      dictionary.put(DatabaseType.MYSQL_ISAM, "mysql");
      dictionary.put(DatabaseType.ORACLE, "oracle");
      dictionary.put(DatabaseType.ORACLE_9I, "oracle");
      dictionary.put(DatabaseType.ORACLE_10G, "oracle");
      dictionary.put(DatabaseType.ORACLE_11G, "oracle");
      dictionary.put(DatabaseType.SYBASE, "sybase");
      dictionary.put(DatabaseType.SYBASE_ANYWHERE, "sybase");
      dictionary.put(DatabaseType.SQL_SERVER, "sqlserver");
      dictionary.put(DatabaseType.SAP_DB, null);
      dictionary.put(DatabaseType.INFORMIX, "informix");
      dictionary.put(DatabaseType.HSQLDB, "hsql");
      dictionary.put(DatabaseType.HSQLDB_IN_MEMORY, "hsql");
      dictionary.put(DatabaseType.INGRES, null);
      dictionary.put(DatabaseType.PROGRESS, null);
      dictionary.put(DatabaseType.MCKOI, null);
      dictionary.put(DatabaseType.INTERBASE, "");
      dictionary.put(DatabaseType.POINTBASE, "pointbase");
      dictionary.put(DatabaseType.FRONTBASE, "");
      dictionary.put(DatabaseType.FIREBIRD, "");
   }

   @Override
   public PersistenceUnitDef setup(PersistenceUnitDef unit, JPADataSource ds)
   {
      unit.provider("org.apache.openjpa.persistence.PersistenceProviderImpl");

      unit.includeUnlistedClasses();

      if (!DatabaseType.DEFAULT.equals(ds.getDatabase()))
      {
         String dialect = dictionary.get(ds.getDatabase());
         if (dialect == null)
         {
            throw new RuntimeException("Unsupported database type for OpenJPA [" + ds.getDatabase() + "]");
         }
         unit.property("openjpa.jdbc.DBDictionary", dialect);
      }

      return unit;
   }

}
