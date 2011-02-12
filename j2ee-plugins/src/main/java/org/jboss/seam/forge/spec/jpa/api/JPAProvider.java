package org.jboss.seam.forge.spec.jpa.api;

import javax.enterprise.inject.spi.BeanManager;

import org.jboss.seam.forge.project.util.BeanManagerUtils;
import org.jboss.seam.forge.spec.jpa.impl.EclipseLinkProvider;
import org.jboss.seam.forge.spec.jpa.impl.HibernateProvider;

public enum JPAProvider {
	HIBERNATE(HibernateProvider.class),
	ECLIPSE_LINK(EclipseLinkProvider.class);

	private Class<? extends PersistenceProvider> type;

	private JPAProvider(Class<? extends PersistenceProvider> type) {
		this.type = type;
	}

	public PersistenceProvider getProvider(BeanManager manager) {
		return BeanManagerUtils.getContextualInstance(manager, type);
	}
}
