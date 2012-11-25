package org.kitchensinkfx.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * A simple bean with the messages resulting on Server response
 * 
 * @author William Antônio
 * 
 */
@XmlRootElement()
public class Messages {
	private String phoneNumber;
	private String email;
	private String name;

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
