package org.jboss.seam.forge.spec.jpa.impl;

import org.jboss.seam.forge.spec.jpa.api.PersistenceProvider;
import org.jboss.shrinkwrap.descriptor.impl.spec.jpa.persistence.PersistenceUnit;
import org.jboss.shrinkwrap.descriptor.impl.spec.jpa.persistence.Property;

public class EclipseLinkProvider implements PersistenceProvider
{

   @Override
   public PersistenceUnit setup(PersistenceUnit unit)
   {
      unit.setProvider("org.eclipse.persistence.jpa.PersistenceProvider");

      unit.setExcludeUnlistedClasses(false);
      unit.getProperties().add(new Property("eclipselink.ddl-generation", "drop-and-create-tables"));
      return unit;
   }

}
