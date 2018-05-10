package com.mastertheboss.bean;


import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
 
import javax.inject.Inject;
import javax.inject.Named;

 
import com.mastertheboss.ejb.ServiceBean;
import com.mastertheboss.model.SimpleProperty;
import com.mastertheboss.producer.Producer;

@Model
public class Manager {
  
	@Inject
	ServiceBean ejb;
	
	@Produces
	@Named
	SimpleProperty property;
	
	@Inject
	Producer producer;
	
	@PostConstruct
	public void initNewProperty() {
		property = new SimpleProperty();
	}
	 
	public void save() {
		ejb.put(property);
		initNewProperty();
	}

	public void clear(SimpleProperty property) {
		ejb.delete(property);
		 
	}
 

}
