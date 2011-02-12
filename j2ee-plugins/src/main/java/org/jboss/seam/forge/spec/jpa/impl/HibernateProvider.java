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

import org.jboss.seam.forge.spec.jpa.api.PersistenceProvider;
import org.jboss.shrinkwrap.descriptor.impl.spec.jpa.persistence.PersistenceUnit;
import org.jboss.shrinkwrap.descriptor.impl.spec.jpa.persistence.Property;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class HibernateProvider implements PersistenceProvider
{
   @Override
   public PersistenceUnit setup(PersistenceUnit unit)
   {
      unit.setProvider("org.hibernate.ejb.HibernatePersistence");

      unit.setExcludeUnlistedClasses(false);
      unit.getProperties().add(new Property("hibernate.hbm2ddl.auto", "create-drop"));
      unit.getProperties().add(new Property("hibernate.show_sql", "true"));
      unit.getProperties().add(new Property("hibernate.format_sql", "true"));
      unit.getProperties().add(new Property("hibernate.transaction.flush_before_completion", "true"));
      return unit;
   }
}
