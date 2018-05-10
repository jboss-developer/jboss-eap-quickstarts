package org.kitchensinkfx.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * Borrowed from Kitchensink project
 * 
 * @author William Antônio
 * 
 */
@XmlRootElement
@ org.codehaus.jackson.annotate.JsonIgnoreProperties("id")
public class Member {

	private String name;
	private String email;
	private String phoneNumber;

	public Member() {
		super();

	}

	public Member(String name, String email, String phoneNumber) {
		super();
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
