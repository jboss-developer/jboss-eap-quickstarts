package com.mastertheboss.model;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PostRemove;

@Entity
public class SimpleProperty {
 
	
	@Id 
	@Column(name="id")
	private String key;

	private String value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return "SimpleProperty [key=" + key + ", value=" + value + "]";
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

    

}
