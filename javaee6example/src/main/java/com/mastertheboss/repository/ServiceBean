package com.mastertheboss.ejb;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

 
 
import com.mastertheboss.model.SimpleProperty;



@Stateless

public class  ServiceBean   {

  @Inject
	private Event<SimpleProperty> propEventSrc;
	
	@Inject
	private EntityManager em;

	
	public void put(SimpleProperty p){
		 
	      em.persist(p);
	      propEventSrc.fire(p);
	}
	 
	public void delete(SimpleProperty p){

		                Query query = em.createQuery("delete FROM com.mastertheboss.model.SimpleProperty p where p.key='"+p.getKey()+"'");
		query.executeUpdate();
		propEventSrc.fire(p);
		 	      
	}



}
