package org.jboss.seam.forge.spec.jpa.api;

import org.jboss.seam.forge.project.util.BeanManagerUtils;
import org.jboss.seam.forge.spec.jpa.container.*;

import javax.enterprise.inject.spi.BeanManager;

public enum JPAContainer
{
   JBOSS_6(JBossAS6Container.class),
   GLASSFISH_3(GlassFish3Container.class),
   CUSTOM_JDBC(CustomJDBCContainer.class),
   CUSTOM_JTA(CustomJTAContainer.class),
   CUSTOM_NON_JTA(NonJTAContainer.class);

   private Class<? extends PersistenceContainer> containerType;

   private JPAContainer(Class<? extends PersistenceContainer> containerType)
   {
      this.containerType = containerType;
   }

   public PersistenceContainer getContainer(BeanManager manager)
   {
      return BeanManagerUtils.getContextualInstance(manager, containerType);
   }
}
