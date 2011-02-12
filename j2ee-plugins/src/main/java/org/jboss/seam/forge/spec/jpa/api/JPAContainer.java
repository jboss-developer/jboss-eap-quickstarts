package org.jboss.seam.forge.spec.jpa.api;

import javax.enterprise.inject.spi.BeanManager;

import org.jboss.seam.forge.project.util.BeanManagerUtils;
import org.jboss.seam.forge.spec.jpa.impl.GlassFish3Container;
import org.jboss.seam.forge.spec.jpa.impl.JBossAS6Container;

public enum JPAContainer
{
   JBOSS_6(JBossAS6Container.class),
   GLASSFISH_3(GlassFish3Container.class);

   private Class<? extends PersistenceContainer> type;

   private JPAContainer(Class<? extends PersistenceContainer> type)
   {
      this.type = type;
   }

   public PersistenceContainer getContainer(BeanManager manager)
   {
      return BeanManagerUtils.getContextualInstance(manager, type);
   }
}
