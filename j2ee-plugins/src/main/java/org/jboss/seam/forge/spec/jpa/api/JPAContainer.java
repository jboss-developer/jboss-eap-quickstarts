package org.jboss.seam.forge.spec.jpa.api;

import javax.enterprise.inject.spi.BeanManager;

import org.jboss.seam.forge.project.util.BeanManagerUtils;
import org.jboss.seam.forge.spec.jpa.impl.GlassFish3Container;
import org.jboss.seam.forge.spec.jpa.impl.JBossAS6Container;
import org.jboss.seam.forge.spec.jpa.impl.NonJTAContainer;

public enum JPAContainer
{
   JBOSS_6_JTA(JBossAS6Container.class),
   GLASSFISH_3_JTA(GlassFish3Container.class),
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
