package com.mastertheboss.producer;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class GenericProducer {
	@SuppressWarnings("unused")
	@Produces
	@PersistenceContext
	private EntityManager em;
}
