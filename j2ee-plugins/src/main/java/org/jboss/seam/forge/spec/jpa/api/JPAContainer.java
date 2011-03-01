package org.jboss.seam.forge.spec.jpa.api;

import javax.enterprise.inject.spi.BeanManager;

import org.jboss.seam.forge.shell.util.BeanManagerUtils;
import org.jboss.seam.forge.spec.jpa.container.CustomJDBCContainer;
import org.jboss.seam.forge.spec.jpa.container.CustomJTAContainer;
import org.jboss.seam.forge.spec.jpa.container.GlassFish3Container;
import org.jboss.seam.forge.spec.jpa.container.JBossAS6Container;
import org.jboss.seam.forge.spec.jpa.container.NonJTAContainer;

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
